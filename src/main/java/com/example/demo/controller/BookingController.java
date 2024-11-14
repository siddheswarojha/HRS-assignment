package com.example.demo.controller;

import com.example.demo.services.BookingService;
import com.example.demo.view.request.BookingRequest;
import com.example.demo.view.response.BookingResponse;
import com.example.demo.view.response.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.auth.HRSAuthorisations.Privileges.BOOKING_READ;
import static com.example.demo.auth.HRSAuthorisations.Privileges.BOOKING_WRITE;

/**
 * REST Controller for managing booking operations.
 */
@RestController
@RequestMapping("/api/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    /**
     * Creates a new booking record.
     *
     * @param request the booking details
     * @return a response containing the details of the created booking
     */
    @PostMapping
  //  @Secured(BOOKING_WRITE)
    public BookingResponse addBooking(@RequestBody BookingRequest request) {
        return new BookingResponse(bookingService.createBooking(request));
    }

    /**
     * Retrieves details of a specific booking by its ID.
     *
     * @param uniqueIdentifier the unique identifier of the booking
     * @return a response containing the booking details
     */
    @GetMapping
//    @Secured(BOOKING_READ)
    public BookingResponse fetchBookingById(@RequestParam String uniqueIdentifier) {
        return new BookingResponse(bookingService.getBookingById(uniqueIdentifier));
    }

    /**
     * Retrieves a paginated list of bookings for the current user.
     *
     * @param page the page number to retrieve
     * @param size the number of items per page
     * @return a paginated response containing booking details
     */
    @GetMapping("/list")
  //  @Secured(BOOKING_READ)
    public PageResponse<BookingResponse> fetchUserBookings(
            @SortDefault.SortDefaults({@SortDefault(sort = "name", direction = Sort.Direction.DESC)})
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return bookingService.getAllBookingsForUser(pageable);
    }

    /**
     * Cancels an existing booking.
     *
     * @param uniqueIdentifier the unique identifier of the booking to be canceled
     */
    @PutMapping("/cancel")
 //   @Secured(BOOKING_WRITE)
    public void cancelExistingBooking(@RequestParam String uniqueIdentifier) {
        bookingService.cancelBooking(uniqueIdentifier);
    }

    @GetMapping("/bookings")
    public PageResponse<BookingResponse> getBookingsForUser(Pageable pageable, @RequestParam(required = false) String searchKey) {
        // Correcting the method name to match the one in BookingService
        return bookingService.getBookingsForUser(pageable);
    }
}
