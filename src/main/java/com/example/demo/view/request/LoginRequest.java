package com.example.demo.view.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest{
    private String username;
    private String email;
    private String password;
}