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

package es.upm.fiware.rss.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.pac4j.springframework.security.authentication.ClientAuthenticationToken;

import es.upm.fiware.rss.dao.UserDao;
import es.upm.fiware.rss.model.RSUser;
import org.springframework.stereotype.Repository;
/**
 *
 * @author fdelavega
 */
@Repository
public class UserDaoImpl extends GenericDaoImpl<RSUser, String> 
    implements UserDao {

    private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    @Override
    protected Class<RSUser> getDomainClass() {
        return RSUser.class;
    }

    @Override
    public RSUser getCurrentUser() {
        String userName;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // When OAuth2 is being used, we should cast the authentication to read the correct user name
        userName = ((ClientAuthenticationToken) authentication).getUserProfile().getId();

        logger.info("User: {}", userName);
        return this.getById(userName);
    }
    
}
