package com.example.demo.view.response;

import com.example.demo.dto.Hotel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HotelResponse extends BaseResponse {
    private String name;
    private String contactName;
    private String contactNumber;
    private String address;
    private Double lat;
    private Double lng;
    private String description;
    private Double rating;

    public HotelResponse(Hotel hotel) {
        super(); // Calls the BaseResponse constructor
        this.uniqueIdentifier = hotel.getUniqueIdentifier();
        this.name = hotel.getName();
        this.contactName = hotel.getContactName(); // Corrected field mapping
        this.contactNumber = hotel.getContactNumber();
        this.address = hotel.getAddress();
        this.lat = hotel.getLat() != null ? hotel.getLat().doubleValue() : null; // Handle potential nulls
        this.lng = hotel.getLng() != null ? hotel.getLng().doubleValue() : null;
        this.description = hotel.getDescription();
        this.rating = hotel.getRating() != null ? hotel.getRating().doubleValue() : null;
    }
}
