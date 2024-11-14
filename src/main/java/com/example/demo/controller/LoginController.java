package com.example.demo.controller;

import com.example.demo.dto.HRSUser;
import com.example.demo.services.UserService;
import com.example.demo.util.EncoderDecoder;
import com.example.demo.view.request.LoginRequest;
import com.example.demo.view.response.LoginResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;

/**
 * REST Controller to manage login functionality.
 * This controller does not use the "/api" prefix as it provides public access.
 */
@RestController
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private EncoderDecoder encoderDecoder;

    @Value("${hrs.auth.secretkey}")
    private String secretKey;

    private Key signingKey;

    @PostConstruct
    public void initializeKey() {
        this.signingKey = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    /**
     * Endpoint to handle user login and generate an authentication token.
     *
     * @param login   the login credentials provided by the user
     * @param request the HTTP request to capture client-specific details
     * @return a response containing the authentication token
     */
    @PostMapping("/login")
    public LoginResponse authenticateUser(@RequestBody LoginRequest login, HttpServletRequest request) {
        // Validate user credentials and retrieve user details
        HRSUser user = userService.validateLogin(login, request.getRemoteAddr());

        if (user == null) {
            throw new RuntimeException("Invalid username or email.");
        }

        LoginResponse response = new LoginResponse();

        // Generate an authentication token upon successful validation
        String authToken = Jwts.builder()
                .setSubject(login.getEmail() != null ? login.getEmail() : login.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(calculateExpiration(user.getRoles().stream().findFirst().get().getAuthHours()))
                .signWith(signingKey)
                .compact();

        response.setToken(encoderDecoder.encryptUsingSecretKey(authToken));
        return response;
    }

    /**
     * Calculates the expiration time for the authentication token.
     *
     * @param authHour the validity duration of the token in hours
     * @return the expiration date
     */
    private Date calculateExpiration(Long authHour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR_OF_DAY, authHour.intValue());
        return calendar.getTime();
    }
}
