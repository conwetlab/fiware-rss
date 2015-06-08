/**
 * Revenue Settlement and Sharing System GE
 * Copyright (C) 2015, CoNWeT Lab., Universidad Politécnica de Madrid
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package es.tid.fiware.rss.service;

import es.tid.fiware.rss.common.properties.AppProperties;
import java.util.Iterator;

import es.tid.fiware.rss.dao.UserDao;
import es.tid.fiware.rss.model.RSUser;
import es.tid.fiware.rss.model.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author fdelavega
 */
@Service
@Transactional
public class UserManager {

    @Autowired
    UserDao userDao;

    @Autowired
    @Qualifier(value = "oauthProperties")
    private AppProperties oauthProperties;

    /**
     * Returns the current user
     * @return RSUser object containig the info of the current user
     */
    public RSUser getCurrentUser() {
        return userDao.getCurrentUser();
    }

    /**
     * Checks whether the current a user contains a given role
     * @param role
     * @return true if the user contains the given role
     */
    public boolean checkRole(String role) {
        boolean found = false;
        RSUser user = this.getCurrentUser();
        Iterator<Role> roles = user.getRoles().iterator();

        while (roles.hasNext() && !found) {
            if (roles.next().getName().equalsIgnoreCase(role)) {
                found = true;
            }
        }
        return found;
    }

    /**
     * Check whether a user contains the role specified in the properties file 
     * as admin of the system
     * @return true, if the role is found
     */
    public boolean isAdmin() {
        return this.checkRole(
            oauthProperties.getProperty("config.grantedRole"));
    }
}
