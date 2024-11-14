package com.example.demo.view.response;

import com.example.demo.dto.Booking;
import com.example.demo.enums.BookingStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class BookingResponse extends BaseResponse {
    private String roomId;
    private String roomName;
    private Date checkIn;
    private Date checkOut;
    private Double price;
    private BookingStatus bookingStatus;
    private Boolean paid;

    public BookingResponse(Booking booking){
        this.uid = booking.getUuid();
        this.uniqueIdentifier = booking.getUniqueIdentifier();
        this.roomId = booking.getRoom().getUuid();
        this.roomName = booking.getRoom().getName();
        this.checkIn = new Date(booking.getCheckIn() * 3600 * 1000);
        this.checkOut = new Date(booking.getCheckOut() * 3600 * 1000);
        this.price = booking.getPrice().doubleValue();
        this.bookingStatus = BookingStatus.valueOf(booking.getBookingStatus());
        this.paid = booking.isPaid();
    }
}
