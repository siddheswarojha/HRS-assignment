package com.example.demo.view.response;

import com.example.demo.dto.Privilege;
import com.example.demo.dto.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class RoleResponse {

    private String id;
    private String name;
    private List<String> privileges;


    public RoleResponse(Role role){
        this.name = role.getName();
        this.id = role.getUuid();
        this.privileges = role.getPrivileges().stream().map(Privilege::getName).collect(Collectors.toList());
    }
}
