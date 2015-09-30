/**
 * Revenue Settlement and Sharing System GE
 * Copyright (C) 2011-2014, Javier Lucio - lucio@tid.es
 * Telefonica Investigacion y Desarrollo, S.A.
 *
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

package es.upm.fiware.rss.ws;

import javax.ws.rs.core.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import es.upm.fiware.rss.model.CDR;
import es.upm.fiware.rss.model.RSUser;
import es.upm.fiware.rss.service.CdrsManager;
import es.upm.fiware.rss.service.UserManager;
import java.util.LinkedList;
import java.util.List;
import java.util.MissingFormatArgumentException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;


public class CdrsServiceTest {

    @Mock private UserManager userManager;
    @Mock private CdrsManager cdrsManager;
    @InjectMocks private CdrsService toTest;

    @Before
    public void setUp() throws Exception {

    }

    @Before
    public void tearDown() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createCdrTest() throws Exception {

        List <CDR> list = new LinkedList<>();

        toTest.createCdr(list);
        verify(cdrsManager).createCDRs(list);
    }

    @Test
    public void getCDRsTest() throws Exception {
        String aggregatorId = "aggregator@mail.com";
        String providerId = "provider@mail.com";
        String userId = "user@mail.com";

        RSUser user = new RSUser();
        user.setDisplayName("username");
        user.setEmail("user@mail.com");

        List <CDR> list = new LinkedList<>();

        when(userManager.getCurrentUser()).thenReturn(user);
        when(userManager.isAdmin()).thenReturn(true);
        when(cdrsManager.getCDRs(userId, providerId)).thenReturn(list);

        Response response = toTest.getCDRs(aggregatorId, providerId);

        Assert.assertEquals(200, response.getStatus());
        Assert.assertEquals(list, response.getEntity());
    }


    @Test
    public void getCDRsNotAdminTest() throws Exception {
        String providerId = "provider@mail.com";
        String userId = "user@mail.com";

        RSUser user = new RSUser();
        user.setDisplayName("username");
        user.setEmail("user@mail.com");

        List <CDR> list = new LinkedList<>();

        when(userManager.getCurrentUser()).thenReturn(user);
        when(userManager.isAdmin()).thenReturn(false);
        when(cdrsManager.getCDRs(userId, providerId)).thenReturn(list);

        Response response = toTest.getCDRs(null, providerId);

        Assert.assertEquals(200, response.getStatus());
        Assert.assertEquals(list, response.getEntity());
    }

    @Test
    (expected = MissingFormatArgumentException.class)
    public void getCDRsNotAdmin2Test() throws Exception {
        String aggregatorId = "aggregator@mail.com";
        String providerId = "provider@mail.com";
        String userId = "user@mail.com";

        RSUser user = new RSUser();
        user.setDisplayName("username");
        user.setEmail("user@mail.com");

        List <CDR> list = new LinkedList<>();

        when(userManager.getCurrentUser()).thenReturn(user);
        when(userManager.isAdmin()).thenReturn(false);
        when(cdrsManager.getCDRs(userId, providerId)).thenReturn(list);

        Response response = toTest.getCDRs(aggregatorId, providerId);
        Assert.assertEquals(list, response.getEntity());
    }

}
