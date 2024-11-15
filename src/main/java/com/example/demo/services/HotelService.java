package com.example.demo.services;

import com.example.demo.dao.HotelRepository;
import com.example.demo.dto.HRSUser;
import com.example.demo.dto.Hotel;
import com.example.demo.view.request.HotelRequest;
import com.example.demo.view.response.HotelResponse;
import com.example.demo.view.response.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for handling hotel-related business logic.
 */
@Service
public class HotelService extends BaseService {

    @Autowired
    private HotelRepository hotelRepository;

    /**
     * Creates a new hotel from the provided request.
     *
     * @param request the hotel details.
     * @return the created hotel entity.
     */
    public Hotel createHotel(HotelRequest request) {
        HRSUser user = getCurrentUser();
        Optional<Hotel> optionalHotel = hotelRepository.findByUniqueIdentifier(request.getUniqueIdentifier());
        if (optionalHotel.isPresent()) {
            Hotel hotel = optionalHotel.get();
           hotel = request.ToEntity(hotel,user);
            return hotelRepository.saveAndFlush(hotel);
        } else {
            Hotel hotel = request.ToEntity(null,user);
            return hotelRepository.saveAndFlush(hotel);
        }
    }


    /**
     * Retrieves a paginated list of hotels, optionally filtered by a search key.
     *
     * @param paging the pageable object for pagination.
     * @param searchKey the search key to filter hotels by name (optional).
     * @return a paginated response containing hotel details.
     */
    public PageResponse<HotelResponse> getAllHotels(Pageable paging, String searchKey) {
        Page<Hotel> hotels = searchKey == null || searchKey.isEmpty()
                ? hotelRepository.findAll(paging)
                : hotelRepository.findAllByNameContaining(searchKey, paging);

        return buildPageResponse(hotels);
    }

    /**
     * Retrieves details of a hotel by its UUID.
     *
     * @param uniqueIdentifier the UUID of the hotel.
     * @return the hotel entity.
     * @throws RuntimeException if the hotel with the provided ID is not found.
     */
    public Hotel getHotelDetails(String uniqueIdentifier) {
        return hotelRepository.findByUniqueIdentifier(uniqueIdentifier)
                .orElseThrow(() -> new RuntimeException("Unable to find hotel"));
    }

    /**
     * Builds a paginated response containing hotel details.
     *
     * @param hotels the page of hotels to convert.
     * @return the paginated response with hotel details.
     */
    private PageResponse<HotelResponse> buildPageResponse(Page<Hotel> hotels) {
        PageResponse<HotelResponse> pageResponse = new PageResponse<>();
        pageResponse.setTotalPages(hotels.getTotalPages());
        pageResponse.setPageNumber(hotels.getNumber());
        pageResponse.setPageSize(hotels.getSize());
        pageResponse.setTotalCount(hotels.getTotalElements());
        pageResponse.setList(hotels.stream()
                .map(HotelResponse::new)
                .collect(Collectors.toList()));
        return pageResponse;
    }
}
