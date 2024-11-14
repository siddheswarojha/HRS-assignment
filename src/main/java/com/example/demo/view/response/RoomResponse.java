package com.example.demo.view.response;

import com.example.demo.dto.Room;
import com.example.demo.enums.RoomType;
import com.example.demo.enums.RoomView;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoomResponse extends BaseResponse {
    private String hotelId;
    private String hotelName;
    private String name;
    private Double price;
    private Long timeframe;
    private RoomType roomType;
    private Long maxCapacity;
    private RoomView roomView;
    private Long floor;
    private Boolean smokingAllowed;

    public RoomResponse(Room room){
        this.hotelId = String.valueOf(room.getHotel().getId());
        this.uniqueIdentifier = room.getUniqueIdentifier();
        this.hotelName = room.getHotel().getName();
        this.name = room.getName();
        this.price = room.getPrice().doubleValue();
        this.timeframe = room.getTimeFrameInHours();
        this.roomType = RoomType.valueOf(room.getRoomType());
        this.maxCapacity = room.getMaxCapacity();
        this.roomView = RoomView.valueOf(room.getRoomView());
        this.floor = room.getFloor();
        this.smokingAllowed = room.getSmokingAllowed();
    }
}
