package com.example.demo.services;

import com.example.demo.dto.CurrentUser;
import com.example.demo.dto.HRSUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class BaseService {

    // Method to retrieve the currently authenticated user from the Spring Security context
    public static HRSUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null; // Return null if the authentication is not present or not authenticated
        }

        // Check if the principal is of type CurrentUser and extract the associated HRSUser
        if (authentication.getPrincipal() instanceof CurrentUser) {
            return ((CurrentUser) authentication.getPrincipal()).getUser();
        }

        // Return null if the principal is not of the expected type
        return null;
    }
}
