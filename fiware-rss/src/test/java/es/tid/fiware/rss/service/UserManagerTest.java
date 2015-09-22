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
import es.tid.fiware.rss.dao.UserDao;
import es.tid.fiware.rss.exception.RSSException;
import es.tid.fiware.rss.model.RSUser;
import es.tid.fiware.rss.model.Role;
import java.util.HashSet;
import java.util.Set;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

/**
 *
 * @author jortiz
 */

public class UserManagerTest {



    @Mock UserDao userDaoMock;
    @Mock AppProperties appProperties;
    @InjectMocks private UserManager userManager;

    public UserManagerTest() {}

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void checkRoleTest() throws RSSException {
        RSUser rSUser = new RSUser();
        rSUser.setDisplayName("nombre");
        rSUser.setEmail("mail@mail.com");
        rSUser.setId("mail@mail.com");

        Set <Role> roles = new HashSet<>();
        Role rol1 = new Role();
        rol1.setId("idRol1");
        rol1.setName("Rol1");
        Role rol2 = new Role();
        rol2.setId("idRol2");
        rol2.setName("Rol2");

        roles.add(rol1);
        roles.add(rol2);
        rSUser.setRoles(roles);

        when(userDaoMock.getCurrentUser()).thenReturn(rSUser);

        assertTrue(userManager.checkRole("Rol1"));
        assertTrue(userManager.checkRole("Rol2"));
        assertFalse(userManager.checkRole("Rol3"));
    }

    @Test
    (expected = RSSException.class)
    public void checkRoleRSSExceptionTest() throws RSSException {

        userManager.userDao = userDaoMock;
        when(userDaoMock.getCurrentUser()).thenReturn(null);

        userManager.checkRole("Rol1");
    }

    @Test
    public void getCurrentUserTest() throws RSSException {
        RSUser rSUser = new RSUser();

        userManager.userDao = userDaoMock;
        when(userDaoMock.getCurrentUser()).thenReturn(rSUser);

        RSUser returned = userManager.getCurrentUser();

        assertNotNull(returned);
    }

    @Test
    (expected = RSSException.class)
    public void getCurrentUserNullTest() throws RSSException{
        userManager.userDao = userDaoMock;
        when(userDaoMock.getCurrentUser()).thenReturn(null);

        userManager.getCurrentUser();
    }

    @Test
    public void isAdminTrueTest() throws RSSException {
        RSUser rSUser = new RSUser();
        Set <Role> roles = new HashSet<>();
        Role rol1 = new Role();
        rol1.setId("other");
        rol1.setName("Other");
        Role rol2 = new Role();
        rol2.setId("admin");
        rol2.setName("admin");

        roles.add(rol1);
        roles.add(rol2);
        rSUser.setRoles(roles);

        userManager.userDao = userDaoMock;
        when(userDaoMock.getCurrentUser()).thenReturn(rSUser);
        when(appProperties.getProperty("config.grantedRole")).thenReturn("admin");

        assertTrue(userManager.isAdmin());
    }

    @Test
    public void isAdminFalseTest() throws RSSException {
        RSUser rSUser = new RSUser();
        Set <Role> roles = new HashSet<>();
        Role rol1 = new Role();
        rol1.setId("idRol1");
        rol1.setName("Rol1");
        Role rol2 = new Role();
        rol2.setId("idRol1");
        rol2.setName("Rol1");

        roles.add(rol1);
        roles.add(rol2);
        rSUser.setRoles(roles);

        userManager.userDao = userDaoMock;
        when(userDaoMock.getCurrentUser()).thenReturn(rSUser);

        assertFalse(userManager.isAdmin());
    }

    @Test
    (expected = RSSException.class)
    public void isAdminRSSExceptionTest() throws RSSException {
        userManager.userDao = userDaoMock;
        when(userDaoMock.getCurrentUser()).thenReturn(null);

        userManager.isAdmin();
    }

}
