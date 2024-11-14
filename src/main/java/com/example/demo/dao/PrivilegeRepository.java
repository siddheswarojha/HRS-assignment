package com.example.demo.dao;

import com.example.demo.dto.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// JPA Repository Interface for Privileges
@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

    //    JPA Method to count Privileges by Name.
    int countByName(String name);

    //    JPA Method to get One Privilege by Name
    Privilege getOneByName(String privilege);
}
