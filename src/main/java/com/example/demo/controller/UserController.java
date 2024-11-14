package com.example.demo.controller;

import com.example.demo.services.UserService;
import com.example.demo.view.request.RoleRequest;
import com.example.demo.view.request.UserRequest;
import com.example.demo.view.response.RoleResponse;
import com.example.demo.view.response.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.auth.HRSAuthorisations.Privileges.*;

/**
 * REST Controller for handling User and Role management endpoints.
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Retrieves the current user's details.
     *
     * @return details of the authenticated user
     */
    @GetMapping
  //  @Secured(SELF_READ)
    public UserResponse fetchCurrentUserDetails() {
        return new UserResponse(userService.getCurrentUser());
    }

    /**
     * Fetches details of a specific user by their identifier.
     *
     * @param userId the unique identifier of the user
     * @return user details
     */
    @GetMapping("/{userId}")
   // @Secured(USER_READ)
    public UserResponse fetchUserById(@PathVariable(name = "userId") String userId) {
        return new UserResponse(userService.getUserDetail(userId));
    }

    /**
     * Creates a new user with the provided details.
     *
     * @param request payload containing user details
     * @return the created user's details
     */
    @PostMapping
    @Secured(USER_WRITE)
    public UserResponse registerNewUser(@RequestBody UserRequest request) {
        return new UserResponse(userService.createUser(request));
    }

    /**
     * Updates the password of an existing user.
     *
     * @param request payload containing the user's password update details
     */
    @PutMapping("/update/password")
   // @Secured(USER_WRITE)
    public void modifyUserPassword(@RequestBody UserRequest request) {
        userService.updatePassword(request);
    }

    /**
     * Updates an existing user's details.
     *
     * @param request payload containing updated user details
     * @return the updated user's details
     */
    @PutMapping
  //  @Secured(USER_WRITE)
    public UserResponse modifyUserDetails(@RequestBody UserRequest request) {
        return new UserResponse(userService.updateUser(request));
    }


    /**
     * Adds a new role with the provided details.
     *
     * @param request payload containing role details
     * @return the created role's details
     */
    @PostMapping("/role")
   // @Secured(ROLE_WRITE)
    public RoleResponse createNewRole(@RequestBody RoleRequest request) {
        return new RoleResponse(userService.saveRole(request));   //handles add and update for role
    }

    @PostMapping("/signup")
    public UserResponse signup(@RequestBody UserRequest request) {
        return new UserResponse(userService.createUser(request));
    }
}
