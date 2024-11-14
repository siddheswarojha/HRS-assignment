package com.example.demo.view.request;

import com.example.demo.dto.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RoleRequest {
    private String id;
    private String name;
    private List<String> privileges = new ArrayList<>();

    public Role toEntity(Role role) {
        if (role == null) {
            role = new Role();
        }
        role.setName(this.name);
        return role;
    }
}