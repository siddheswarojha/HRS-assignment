package com.example.demo.services;

import com.example.demo.dao.BookingRepository;
import com.example.demo.dao.RoomRepository;
import com.example.demo.dto.Booking;
import com.example.demo.dto.HRSUser;
import com.example.demo.dto.Room;
import com.example.demo.enums.BookingStatus;
import com.example.demo.view.request.BookingRequest;
import com.example.demo.view.response.BookingResponse;
import com.example.demo.view.response.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for handling business logic related to bookings.
 */
@Service
public class BookingService extends BaseService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoomRepository roomRepository;

    /**
     * Creates a new booking based on the provided request.
     *
     * @param request the booking details.
     * @return the created booking entity.
     */
    public Booking createBooking(BookingRequest request) {
        // Step 1: Fetch and validate the room
        Room room = fetchRoomById(request.getRoomId());

        // Step 2: Create a new booking and set its values
        Booking booking = prepareBooking(request, room);

        // Step 3: Check for overlapping bookings and set booking status
        setBookingStatus(booking, room);

        // Step 4: Save the booking
        return bookingRepository.saveAndFlush(booking);
    }

    private Room fetchRoomById(String roomId) {
        return roomRepository.findByUniqueIdentifier(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Room with ID " + roomId + " not found"));
    }

    private Booking prepareBooking(BookingRequest request, Room room) {
        Booking booking = request.ToEntity(null);
        booking.setRoom(room);
        booking.setPrice(room.getPrice());
        return booking;
    }

    private void setBookingStatus(Booking booking, Room room) {
        boolean hasOverlappingBookings = checkForOverlappingBookings(room.getId(), booking.getCheckIn(), booking.getCheckOut());
        booking.setBookingStatus(hasOverlappingBookings ? BookingStatus.PENDING.toString() : BookingStatus.CONFIRMED.toString());
    }

    private boolean checkForOverlappingBookings(Long roomId, Long checkIn, Long checkOut) {
        return bookingRepository.countOverlappingBookings(roomId, checkIn, checkOut) > 0;
    }


    /**
     * Retrieves a paginated list of bookings for the authenticated user.
     *
     * @param paging the pagination details.
     * @return a paginated response containing booking details.
     */
    public PageResponse<BookingResponse> getBookingsForUser(Pageable paging) {
        HRSUser currentUser = getCurrentUser();

        Page<Booking> bookings = bookingRepository.findAllByCreatedBy(currentUser, paging);

        return buildPageResponse(bookings);
    }

    /**
     * Retrieves booking details by ID.
     *
     * @param uniqueIdentifier the booking ID.
     * @return the booking entity.
     * @throws EntityNotFoundException if the booking is not found.
     */
    public Booking getBookingById(String uniqueIdentifier) {
        Booking booking = bookingRepository.findByUniqueIdentifier(uniqueIdentifier)
                .orElseThrow(() -> new EntityNotFoundException("Unable to find booking"));

        if (!booking.getCreatedBy().get().getUuid().equals(getCurrentUser().getUuid())) {
            throw new RuntimeException("Access denied!");
        }

        return booking;
    }

    /**
     * Cancels a booking by ID.
     *
     * @param uniqueIdentifier the booking ID.
     */
    public void cancelBooking(String uniqueIdentifier) {
        Booking booking = getBookingById(uniqueIdentifier);

        boolean isConfirmed = booking.getBookingStatus().equals(BookingStatus.CONFIRMED.toString());
        booking.setBookingStatus(BookingStatus.CANCELLED.toString());
        bookingRepository.saveAndFlush(booking);

        if (isConfirmed) {
            // Check for pending bookings in the same time slot using the custom @Query method
            List<Booking> pendingBookings = bookingRepository.findPendingBookings(
                    booking.getCheckIn(), booking.getCheckOut(), BookingStatus.PENDING.toString());

            // Update the status of the pending bookings if needed
            for (Booking pendingBooking : pendingBookings) {
                // You can add your logic here to determine whether to confirm the pending booking
                // For example, you could automatically confirm the first pending booking in the time slot
                pendingBooking.setBookingStatus(BookingStatus.CONFIRMED.toString());
                bookingRepository.saveAndFlush(pendingBooking);
                break; // to execute it once for single cancellations
            }
        }
    }


    /**
     * Helper method to build a paginated response for bookings.
     *
     * @param bookings the page of bookings.
     * @return the paginated response containing booking details.
     */
    private PageResponse<BookingResponse> buildPageResponse(Page<Booking> bookings) {
        PageResponse<BookingResponse> pageResponse = new PageResponse<>();
        pageResponse.setTotalPages(bookings.getTotalPages());
        pageResponse.setPageNumber(bookings.getNumber());
        pageResponse.setPageSize(bookings.getSize());
        pageResponse.setTotalCount(bookings.getTotalElements());
        pageResponse.setList(bookings.stream()
                .map(BookingResponse::new)
                .collect(Collectors.toList()));
        return pageResponse;
    }
    public PageResponse<BookingResponse> getAllBookingsForUser(Pageable paging) {
        HRSUser currentUser = getCurrentUser();

        Page<Booking> bookings = bookingRepository.findAllByCreatedBy(currentUser, paging);

        return buildPageResponse(bookings);
    }
}
