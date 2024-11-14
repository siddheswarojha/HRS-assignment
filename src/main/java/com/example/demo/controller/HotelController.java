package com.example.demo.controller;

import com.example.demo.services.HotelService;
import com.example.demo.services.RoomService;
import com.example.demo.view.request.HotelRequest;
import com.example.demo.view.request.RoomRequest;
import com.example.demo.view.response.HotelResponse;
import com.example.demo.view.response.PageResponse;
import com.example.demo.view.response.RoomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.auth.HRSAuthorisations.Privileges.HOTEL_READ;
import static com.example.demo.auth.HRSAuthorisations.Privileges.HOTEL_WRITE;

/**
 * REST Controller for managing hotel operations.
 */
@RestController
@RequestMapping("/api/hotel")
public class HotelController {

    @Autowired
    private HotelService hotelService;
    @Autowired
    private RoomService roomService;

    /**
     * Creates a new hotel record.
     *
     * @param request the request object containing hotel details
     * @return the response object with details of the created hotel
     */
    @PostMapping
  //  @Secured(HOTEL_WRITE)
    public HotelResponse addHotel(@RequestBody HotelRequest request) {
        return new HotelResponse(hotelService.createHotel(request));    // same method handles add and update feature for hotel
    }

    /**
     * Retrieves a paginated list of hotels, optionally filtered by a search keyword.
     *
     * @param searchKey optional search term for filtering hotels
     * @param page      the page number to retrieve
     * @param size      the number of items per page
     * @return paginated list of hotel details
     */
    @GetMapping("/list")
 //   @Secured(HOTEL_READ)
    public PageResponse<HotelResponse> fetchHotelList(
            @SortDefault.SortDefaults({@SortDefault(sort = "name", direction = Sort.Direction.DESC)})
            @RequestParam(required = false) String searchKey,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return hotelService.getAllHotels(pageable, searchKey);
    }

    /**
     * Retrieves details of a specific hotel by its unique identifier.
     *
     * @param uniqueIdentifier the unique identifier of the hotel
     * @return the response object with details of the specified hotel
     */
    @GetMapping("fetchHotel")
 //   @Secured(HOTEL_READ)
    public HotelResponse fetchHotelDetails(@RequestParam String uniqueIdentifier) {
        return new HotelResponse(hotelService.getHotelDetails(uniqueIdentifier));
    }

    /**
     * Creates a new hotel room.
     *
     * @param request payload containing room details
     * @return details of the created room
     */
    @PostMapping("room")
 //   @Secured(HOTEL_WRITE)
    public RoomResponse addNewRoom(@RequestBody RoomRequest request) {
        return new RoomResponse(roomService.createRoom(request));   //same method can handle add and update.
    }

    /**
     * Retrieves a paginated list of hotel rooms, with optional search by hotel name.
     *
     * @param searchKey optional search term for filtering rooms by name
     * @param page      page number for pagination
     * @param size      number of items per page
     * @return paginated list of room details
     */
    @GetMapping("rooms/list")
  //  @Secured(HOTEL_READ)
    public PageResponse<RoomResponse> fetchRoomList(
            @SortDefault.SortDefaults({@SortDefault(sort = "name", direction = Sort.Direction.DESC)})
            @RequestParam(required = false) String searchKey,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return roomService.getAllRooms(pageable, searchKey);
    }

    /**
     * Fetches detailed information about a specific hotel room by its ID.
     *
     * @param uniqueIdentifier the unique identifier of the room
     * @return details of the specified room
     */
    @GetMapping("fetchRoom")
  //  @Secured(HOTEL_READ)
    public RoomResponse fetchRoomDetails(@RequestParam String uniqueIdentifier) {
        return new RoomResponse(roomService.getRoomDetails(uniqueIdentifier));
    }
}
