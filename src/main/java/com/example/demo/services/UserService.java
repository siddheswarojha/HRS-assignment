package com.example.demo.services;

import com.example.demo.auth.AuthCacheProvider;
import com.example.demo.dao.PrivilegeRepository;
import com.example.demo.dao.RoleRepository;
import com.example.demo.dao.UserRepository;
import com.example.demo.dto.CurrentUser;
import com.example.demo.dto.HRSUser;
import com.example.demo.dto.Role;
import com.example.demo.view.request.LoginRequest;
import com.example.demo.view.request.RoleRequest;
import com.example.demo.view.request.UserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.regex.Pattern;

/**
 * Service class to handle user-related business logic.
 */
@Service
public class UserService extends BaseService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private static final String SUPER_ADMIN_USERNAME = "super_admin";
    private static final String SUPER_ADMIN_EMAIL = "admin@hrs.com";
    private static final String SUPER_ADMIN_PASSWORD = "Admin@123";
    private static final String PASSWORD_VALIDATION_MESSAGE =
            "Password must contain at least 1 lowercase letter, 1 uppercase letter, 1 digit, " +
                    "1 special character, and be between 8 to 10 characters long.";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private AuthCacheProvider authCache;

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * Creates a super admin user with the role TECHADMIN if not already exists.
     */
    public void createTechAdmin() {
        HRSUser techAdmin = userRepository.findByUsername(SUPER_ADMIN_USERNAME);
        if (techAdmin == null) {
            techAdmin = new HRSUser();
            techAdmin.setUsername(SUPER_ADMIN_USERNAME);
            techAdmin.setPasswordHash(encoder.encode(SUPER_ADMIN_PASSWORD));
            techAdmin.setFirstname("Tech");
            techAdmin.setLastname("Admin");
            techAdmin.setEmail(SUPER_ADMIN_EMAIL);
            techAdmin.addRole(roleRepository.getOneByName("TECHADMIN"));
            userRepository.save(techAdmin);
        }
    }

    /**
     * Validates user login credentials.
     *
     * @param request the login request containing username and password
     * @param ip      the IP address of the user
     * @return the authenticated user, or null if authentication fails
     */
    public HRSUser validateLogin(LoginRequest request, String ip) {
        HRSUser user = userRepository.findByEmailOrUsername(request.getUsername(), request.getUsername());
        if (user != null && encoder.matches(request.getPassword(), user.getPasswordHash())) {
            user.setLastLogin(new Date());
            userRepository.saveAndFlush(user);
            return user;
        }
        logger.info("Authentication failed: Invalid username or password.");
        return null;
    }

    /**
     * Loads user details by username, required for Spring Security authentication.
     *
     * @param subject the username or email
     * @return the user details
     * @throws UsernameNotFoundException if the user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String subject) throws UsernameNotFoundException {
        HRSUser user = userRepository.findByEmailOrUsername(subject, subject);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User %s was not found", subject));
        }
        return new CurrentUser(user, new String[]{"ROLE_USER"});
    }

    /**
     * Creates a new user.
     *
     * @param request the user creation request
     * @return the created user
     */
    public HRSUser createUser(UserRequest request) {
        validateUser(request);
        validatePassword(request.getPassword());

        if (userRepository.findByEmailOrUsername(request.getEmail(), request.getUserName()) != null) {
            throw new RuntimeException(String.format("User with email %s already exists.", request.getEmail()));
        }

        HRSUser user = request.toEntity();
        request.getRoles().forEach(role -> user.addRole(roleRepository.getOneByName(role)));
        user.setPasswordHash(encoder.encode(request.getPassword()));

        return userRepository.save(user);
    }

    /**
     * Updates an existing user.
     *
     * @param request the user update request
     * @return the updated user
     */
    public HRSUser updateUser(UserRequest request) {
        validateUser(request);
        HRSUser user = userRepository.findByEmail(request.getEmail());
        request.toEntity(user);

        user.getRoles().clear();
        request.getRoles().forEach(role -> user.addRole(roleRepository.getOneByName(role)));

        return userRepository.save(user);
    }

    /**
     * Retrieves user details by UUID.
     *
     * @param userId the UUID of the user
     * @return the user details
     */
    public HRSUser getUserDetail(String userId) {
        return userRepository.findByUniqueIdentifier(userId).orElseThrow(() -> new RuntimeException("User not found"));
    }

    /**
     * Adds a new role.
     *
     * @param request the role request
     * @return the created role
     */
    public Role saveRole(RoleRequest request) {
        // Retrieve the role by name
        Role existingRole = roleRepository.findByName(request.getName());

        final Role role; // Declare as final or effectively final
        if (existingRole == null) {
            // Create a new role
            role = request.toEntity(null);
        } else {
            // Update the existing role
            role = existingRole; // No reassignment; just use as effectively final
            request.toEntity(role);
            role.getPrivileges().clear(); // Clear current privileges before updating
        }

        // Add privileges to the role
        request.getPrivileges().forEach(privilege ->
                role.addPrivilege(privilegeRepository.getOneByName(privilege))
        );

        // Save and return the role
        return roleRepository.save(role);
    }

    /**
     * Retrieves a role by UUID.
     *
     * @param roleId the UUID of the role
     * @return the role details
     */
    public Role getRole(String roleId) {
        return roleRepository.findOneByUuid(roleId);
    }

    /**
     * Updates the user's password.
     *
     * @param request the user request containing the new password
     */
    public void updatePassword(UserRequest request) {
        validatePassword(request.getPassword());
        HRSUser user = userRepository.findByUniqueIdentifier(request.getUid())
                .orElseThrow(() -> new RuntimeException("Invalid User ID: " + request.getUid()));

        user.setPasswordHash(encoder.encode(request.getPassword()));
        user.setLastPassModifiedDate(new Date());
        userRepository.saveAndFlush(user);

        authCache.getAuthCache().invalidateAll();
    }

    /**
     * Validates the password against the required criteria.
     *
     * @param password the password to validate
     * @return true if the password is valid, otherwise throws an exception
     */
    private boolean validatePassword(String password) {
        if (password.isEmpty() || !Pattern.compile("(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[^A-Za-z0-9]).{8,10}$")
                .matcher(password).matches()) {
            throw new RuntimeException(PASSWORD_VALIDATION_MESSAGE);
        }
        return true;
    }

    /**
     * Validates the user creation or update request.
     *
     * @param request the user request
     */
    private void validateUser(UserRequest request) {
        if (request.getFirstName().isEmpty()) throw new RuntimeException("First Name is required.");
        if (request.getLastName().isEmpty()) throw new RuntimeException("Last Name is required.");
        if (request.getEmail().isEmpty()) throw new RuntimeException("Email is required.");
        if (request.getUserName().isEmpty()) throw new RuntimeException("Username is required.");
    }
}
