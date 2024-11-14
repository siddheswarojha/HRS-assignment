package com.example.demo.view.response;

import com.example.demo.dto.HRSUser;
import com.example.demo.dto.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class UserResponse extends BaseResponse{
    private String username;
    private String email;
    private String phone;
    private String firstname;
    private String lastname;
    private Date lastLogin;
    private List<String> roles;
    private Date lastPassModifiedDate;

    public UserResponse(HRSUser user) {
        this.uniqueIdentifier = user.getUniqueIdentifier();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.firstname = user.getFirstname();
        this.lastname = user.getLastname();
        this.lastLogin = user.getLastLogin();
        this.roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());
        this.lastPassModifiedDate = user.getLastPassModifiedDate();
    }
}
