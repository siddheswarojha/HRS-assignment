package com.example.demo.view.request;

import com.example.demo.dto.Booking;
import com.example.demo.enums.BookingStatus;
import com.github.f4b6a3.ulid.UlidCreator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class BookingRequest extends BaseRequest {
    private String roomId;
    private Date checkIn;
    private Date checkOut;
    private Double price;
    private BookingStatus bookingStatus;
    private Boolean isPaid;

    public Booking ToEntity(Booking booking){
      if(booking == null){
          booking = new Booking();
          booking.checkIn = this.getCheckIn().getTime() / 1000 / 3600;
          booking.checkOut = this.getCheckOut().getTime() / 1000 / 3600;
      }
      return booking;
    }

    private String generateULID() {
        return UlidCreator.getUlid().toString(); // Requires adding a ULID library
    }
}
