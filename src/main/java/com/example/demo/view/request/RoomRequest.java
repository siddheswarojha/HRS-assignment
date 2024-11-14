package com.example.demo.view.request;

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

    public Room ToEntity(Room room) {
        if (room == null) {
            room = new Room();
            room.uniqueIdentifier = generateULID();
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
        return room;
    }



    // Utility method for ULID generation
    private String generateULID() {
        return UlidCreator.getUlid().toString();
    }

}
