package com.example.demo.dao;

import com.example.demo.dto.Hotel;
import com.example.demo.dto.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// JPA Repository Interface for Rooms
@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    //    JPA Method to get a Room by UUID.
    Optional<Room> findByUniqueIdentifier(String uniqueIdentifier);

    //    JPA Method to Page of Rooms which lies in the provided hotel List.
    Page<Room> findAllByHotelIn(List<Hotel> hotels, Pageable paging);
}
