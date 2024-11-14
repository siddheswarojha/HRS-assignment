package com.example.demo.dao;

import com.example.demo.dto.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// JPA Repository Interface for Roles
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    //    JPA Method to Get a Role by Name.
    Role getOneByName(String name);

    //    JPA Method to Get a role by Name.
    Role findByName(String name);

    //    JPA Method to Find a role by UUID.
    Role findOneByUuid(String id);
}
