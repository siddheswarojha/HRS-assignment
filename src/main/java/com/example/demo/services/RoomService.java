package com.example.demo.services;

import com.example.demo.dao.HotelRepository;
import com.example.demo.dao.RoomRepository;
import com.example.demo.dto.Hotel;
import com.example.demo.dto.Room;
import com.example.demo.view.request.RoomRequest;
import com.example.demo.view.response.PageResponse;
import com.example.demo.view.response.RoomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class to handle business logic related to hotel rooms.
 */
@Service
public class RoomService extends BaseService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private HotelRepository hotelRepository;

    /**
     * Creates a new room associated with a hotel.
     *
     * @param request the details of the room to create.
     * @return the created Room entity.
     */
    public Room createRoom(RoomRequest request) {
        Optional<Room> optionalRoom = roomRepository.findByUniqueIdentifier(request.getUniqueIdentifier());
        if (optionalRoom.isPresent()) {
            Room room = optionalRoom.get();
            room = request.ToEntity(room);
            return roomRepository.saveAndFlush(room);
        } else {
            Room room = request.ToEntity(null);
            return roomRepository.saveAndFlush(room);
        }

    }


    /**
     * Retrieves a paginated list of rooms with an optional search key.
     *
     * @param paging    the pagination information.
     * @param searchKey an optional search term to filter rooms by hotel name.
     * @return a PageResponse containing a list of RoomResponse objects.
     */
    public PageResponse<RoomResponse> getAllRooms(Pageable paging, String searchKey) {
        Page<Room> rooms;

        if (searchKey == null || searchKey.isBlank()) {
            rooms = roomRepository.findAll(paging);
        } else {
            List<Hotel> hotels = hotelRepository.findAllByNameContaining(searchKey);
            rooms = roomRepository.findAllByHotelIn(hotels, paging);
        }

        return buildPageResponse(rooms);
    }

    /**
     * Retrieves room details using the room's UUID.
     *
     * @param uniqueIdentifier the UUID of the room.
     * @return the Room entity.
     */
    public Room getRoomDetails(String uniqueIdentifier) {
        return roomRepository.findByUniqueIdentifier(uniqueIdentifier)
                .orElseThrow(() -> new RuntimeException("Room Not Found"));
    }

    /**
     * Builds a PageResponse object from a Page of Room entities.
     *
     * @param rooms the Page of Room entities.
     * @return a PageResponse containing RoomResponse objects.
     */
    private PageResponse<RoomResponse> buildPageResponse(Page<Room> rooms) {
        PageResponse<RoomResponse> pageResponse = new PageResponse<>();
        pageResponse.setTotalPages(rooms.getTotalPages());
        pageResponse.setPageNumber(rooms.getNumber());
        pageResponse.setPageSize(rooms.getSize());
        pageResponse.setTotalCount(rooms.getTotalElements());
        pageResponse.setList(rooms.stream().map(RoomResponse::new).collect(Collectors.toList()));
        return pageResponse;
    }
}
