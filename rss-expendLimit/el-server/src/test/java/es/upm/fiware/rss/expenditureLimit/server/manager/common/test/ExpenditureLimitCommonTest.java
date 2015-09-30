/**
 * Revenue Settlement and Sharing System GE
 * Copyright (C) 2011-2014, Javier Lucio - lucio@tid.es
 * Telefonica Investigacion y Desarrollo, S.A.
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
package es.upm.fiware.rss.expenditureLimit.server.manager.common.test;

import java.net.URI;
import java.util.Date;

import javax.ws.rs.core.UriInfo;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import es.upm.fiware.rss.expenditureLimit.server.common.ExpenditureLimitCommon;


public class ExpenditureLimitCommonTest {
    /**
     * Logging system.
     */
    private static Logger logger = LoggerFactory.getLogger(ExpenditureLimitCommonTest.class);

    /**
     * 
     */
    @Test
    public void testParseDate() {
        try {
            Date date = ExpenditureLimitCommon.parseDate("2014-01-12 00:00");
            Assert.assertTrue(true);
        } catch (Exception e) {
            Assert.fail("Non expected exception");
        }
        // check error
        try {
            Date date = ExpenditureLimitCommon.parseDate("aaaa");
            Assert.fail("Non expected exception");
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    /**
     * 
     */
    @Test
    public void getResourceUrl() throws Exception {

        UriInfo mockUriInfo = Mockito.mock(UriInfo.class);
        Mockito.when(mockUriInfo.getBaseUri()).thenReturn(new URI("http://www.test.com/go"));

        ExpenditureLimitCommon.getResourceUrl(mockUriInfo, "", "resource");
        Mockito.when(mockUriInfo.getBaseUri()).thenReturn(new URI("http://www.test.com/"));
        ExpenditureLimitCommon.getResourceUrl(mockUriInfo, "", null);
        Assert.assertTrue(true);
    }
}
