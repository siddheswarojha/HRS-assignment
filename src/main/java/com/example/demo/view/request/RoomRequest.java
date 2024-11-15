package com.example.demo.view.request;

import com.example.demo.dto.HRSUser;
import com.example.demo.dto.Hotel;
import com.example.demo.dto.Room;
import com.example.demo.enums.RoomType;
import com.example.demo.enums.RoomView;
import com.github.f4b6a3.ulid.UlidCreator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class RoomRequest extends BaseRequest {
    private String hotelId;
    private String name;
    private Double price;
    private Long timeframe;
    private RoomType roomType;
    private Long maxCapacity;
    private RoomView roomView;
    private Long floor;
    private Boolean smokingAllowed;

    /**
     * Converts this RoomRequest to a Room entity.
     * If the Room entity does not exist, a new one is created.
     *
     * @param room    the existing Room entity, or null if creating a new one
     * @param hotel   the Hotel entity to associate with the Room
     * @param user    the user creating or modifying the Room
     * @return the populated Room entity
     */
    public Room ToEntity(Room room, Hotel hotel, HRSUser user) {
        if (room == null) {
            room = new Room();
            room.uniqueIdentifier = generateULID();
            room.createdBy = user.getUniqueIdentifier();
        }

        if (this.getName() != null) {
            room.name = this.getName();
        }
        if (this.getPrice() != null) {
            room.price = BigDecimal.valueOf(this.getPrice());
        }
        if (this.getTimeframe() != null) {
            room.timeFrameInHours = this.getTimeframe();
        }
        if (this.getRoomType() != null) {
            room.roomType = this.getRoomType().toString();
        }
        if (this.getMaxCapacity() != null) {
            room.maxCapacity = this.getMaxCapacity();
        }
        if (this.getRoomView() != null) {
            room.roomView = this.getRoomView().toString();
        }
        if (this.getFloor() != null) {
            room.floor = this.getFloor();
        }
        if (this.getSmokingAllowed() != null) {
            room.smokingAllowed = this.getSmokingAllowed();
        }

        // Set the associated hotel
        if (hotel != null) {
            room.hotel = hotel;
        }

        return room;
    }

    // Utility method for ULID generation
    private String generateULID() {
        return UlidCreator.getUlid().toString();
    }
}
