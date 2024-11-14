package com.example.demo.view.request;

import com.example.demo.dto.Hotel;
import com.github.f4b6a3.ulid.UlidCreator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class HotelRequest extends BaseRequest {
    private String name;
    private String contactName;
    private String contactNumber;
    private String address;
    private Double lat;
    private Double lng;
    private String description;
    private Double rating;

    public Hotel ToEntity(Hotel hotel) {
        if(hotel == null){
            hotel = new Hotel();
            hotel.uniqueIdentifier = generateULID();
        }
        hotel.name = this.getName();
        hotel.contactName = this.getContactName();
        hotel.contactNumber = this.getContactNumber();
        hotel.address = this.getAddress();
        hotel.lat = BigDecimal.valueOf(this.getLat());
        hotel.lng = BigDecimal.valueOf(this.getLng());
        hotel.description = this.getDescription();
        hotel.rating = BigDecimal.valueOf(this.getRating());
        return hotel;
    }

    // You can use a utility method or third-party library for generating ULID
    private String generateULID() {
        return UlidCreator.getUlid().toString(); // Requires adding a ULID library
    }


}
