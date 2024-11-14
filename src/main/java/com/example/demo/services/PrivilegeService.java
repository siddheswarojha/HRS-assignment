package com.example.demo.services;

import com.example.demo.auth.HRSAuthorisations;
import com.example.demo.dao.PrivilegeRepository;
import com.example.demo.dao.RoleRepository;
import com.example.demo.dto.Privilege;
import com.example.demo.dto.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Service class to handle business logic related to privileges.
 */
@Service
public class PrivilegeService extends BaseService {

    private static final Logger logger = LoggerFactory.getLogger(PrivilegeService.class);

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserService userService;

    /**
     * Initializes the application with default privileges and the TECHADMIN role.
     * Creates privileges defined in {@link HRSAuthorisations.Privileges} if they don't exist,
     * associates them with the TECHADMIN role, and creates a default Tech Admin user.
     */
    @PostConstruct
    public void init() {
        addPrivilegesToDatabase();
        userService.createTechAdmin();
    }

    private void addPrivilegesToDatabase() {
        Field[] privilegeFields = HRSAuthorisations.Privileges.class.getDeclaredFields();

        for (Field field : privilegeFields) {
            if (Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers())) {
                String privilegeName = field.getName();
                logger.info("Processing privilege: {}", privilegeName);

                if (!privilegeExists(privilegeName)) {
                    Privilege privilege = savePrivilege(privilegeName);
                    associatePrivilegeWithTechAdmin(privilege);
                }
            }
        }
    }

    /**
     * Checks if a privilege exists in the database.
     *
     * @param privilegeName the name of the privilege.
     * @return true if the privilege exists, false otherwise.
     */
    private boolean privilegeExists(String privilegeName) {
        return privilegeRepository.countByName(privilegeName) > 0;
    }

    /**
     * Saves a new privilege to the database.
     *
     * @param privilegeName the name of the privilege to save.
     * @return the saved Privilege entity.
     */
    private Privilege savePrivilege(String privilegeName) {
        Privilege privilege = new Privilege();
        privilege.setName(privilegeName);
        privilegeRepository.saveAndFlush(privilege);
        logger.info("Saved privilege: {}", privilegeName);
        return privilege;
    }

    /**
     * Associates a privilege with the TECHADMIN role.
     *
     * @param privilege the privilege to associate.
     */
    private void associatePrivilegeWithTechAdmin(Privilege privilege) {
        Role techAdminRole = roleRepository.getOneByName("TECHADMIN");

        if (techAdminRole == null) {
            techAdminRole = createTechAdminRole();
        }

        techAdminRole.addPrivilege(privilege);
        roleRepository.saveAndFlush(techAdminRole);
        logger.info("Associated privilege '{}' with TECHADMIN role.", privilege.getName());
    }

    /**
     * Creates and saves the TECHADMIN role if it does not exist.
     *
     * @return the created Role entity.
     */
    private Role createTechAdminRole() {
        Role role = new Role();
        role.setName("TECHADMIN");
        roleRepository.saveAndFlush(role);
        logger.info("Created TECHADMIN role.");
        return role;
    }
}
