package com.example.demo.dao;

import com.example.demo.dto.Booking;
import com.example.demo.dto.HRSUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// JPA Repository Interface for Bookings
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    //    JPA Method to get Count of bookings which overlap at a room between checkIn and Checkout time.
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.room.id = :roomId AND NOT (b.checkOut <= :checkIn OR b.checkIn >= :checkOut)")
    long countOverlappingBookings(@Param("roomId") Long roomId, @Param("checkIn") Long checkIn, @Param("checkOut") Long checkOut);

    //    JPA Method to find all booking of a User
    Page<Booking> findAllByCreatedBy(HRSUser user, Pageable paging);

    //    JPA Method to Find a Booking By UUID
    Optional<Booking> findByUniqueIdentifier(String uniqueIdentifier);


  // List<Booking> findBookingByBookingStatusContainingAndCheckInAndCheckOutAndBookingStatus(Long checkIn, Long checkOut, String bookingStatus);

    @Query("SELECT b FROM Booking b WHERE b.bookingStatus LIKE %:status% AND b.checkIn = :checkIn AND b.checkOut = :checkOut")
    List<Booking> findPendingBookings(
            @Param("checkIn") Long checkIn,
            @Param("checkOut") Long checkOut,
            @Param("status") String status);

}
