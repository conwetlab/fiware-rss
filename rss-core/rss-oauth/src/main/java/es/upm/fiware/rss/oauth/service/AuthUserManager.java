/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upm.fiware.rss.oauth.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.pac4j.oauth.profile.JsonHelper;
import org.springframework.stereotype.Service;

import es.upm.fiware.rss.dao.DbeAggregatorDao;
import es.upm.fiware.rss.dao.RoleDao;
import es.upm.fiware.rss.dao.UserDao;
import es.upm.fiware.rss.model.RSUser;
import es.upm.fiware.rss.oauth.model.Role;

/**
 *
 * @author fdelavega
 */
@Service
@Transactional
public class AuthUserManager {
    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private DbeAggregatorDao aggregatorDao;

    /**
     * Update Database users accordint to the provided FIWAREOProfile
     * @param profile, FIWAREProfile of the user
     */
    public void updateUser(FIWAREProfile profile) {
        RSUser user;
        // Get basic user info
        String username = (String) profile.getUsername();
        String email = (String) profile.getEmail();
        String displayName = (String) profile.getDisplayName();
        // Modify the existing user
        user = userDao.getById(username);

        // Check if th user do not have any role and not exists, return
        if (!profile.getRSRoles().isEmpty()) {
            if (user == null) {
                // Create a new user
                user = new RSUser();
            }

            // Set field values
            user.setId(username);
            user.setDisplayName(displayName);
            user.setEmail(email);

            // The user must exists before creating roles in the database
            userDao.createOrUpdate(user);
            this.populateUserRoles(user, profile);

            // Save user roles to the database
            userDao.createOrUpdate(user);
        } else if (user != null) {
            userDao.delete(user);
        }
    }

    /**
     * Builds a list of Revenue Sharing roles according to the roles retrieved
     * from the idm and the email of the user
     * @param rolesNode, A JSON array containing the roles provided by the idm
     * @param email, Email of the user
     * @return List of revenue sharing roles
     */
    public List<Role> buildUserRoles(ArrayNode rolesNode, String email) {
        List<Role> userRoles = new ArrayList<>();

        // Include idm defined roles
        for (JsonNode node : rolesNode) {
            Role r = new Role();
            String role = (String) JsonHelper.get(node, "name");

            r.setId((String) JsonHelper.get(node, "id"));
            r.setName(role.toLowerCase());
            userRoles.add(r);
        }

        // Check aggregator role
        if (this.aggregatorDao.getById(email) != null) {
            Role ag = new Role();
            ag.setId("0");
            ag.setName("aggregator");
            userRoles.add(ag);
        }

        return userRoles;
    }

    private void populateUserRoles(RSUser user, FIWAREProfile profile) {
        Set<es.upm.fiware.rss.model.Role> exRoles = user.getRoles();

        if (exRoles == null) {
            exRoles = new HashSet<>();
        }

        // Create new roles if needed
        for (Role r: profile.getRSRoles()) {
            // Check if the role already exists
            es.upm.fiware.rss.model.Role dbRole = this.roleDao.getById(r.getId());

            if (dbRole == null) {
                dbRole = new es.upm.fiware.rss.model.Role();
                dbRole.setId(r.getId());
            }
            // Add role name
            dbRole.setName(r.getName());

            // Add user to the role
            Set<RSUser> users = dbRole.getUsers();
            if (users == null) {
                users = new HashSet<>();
            }

            users.add(user);
            dbRole.setUsers(users);
            this.roleDao.createOrUpdate(dbRole);

            exRoles.add(dbRole);
        }
        user.setRoles(exRoles);
    }
}
