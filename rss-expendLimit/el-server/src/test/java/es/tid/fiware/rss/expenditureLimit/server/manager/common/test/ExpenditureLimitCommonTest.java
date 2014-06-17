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
package es.tid.fiware.rss.expenditureLimit.server.manager.common.test;

import java.net.URI;
import java.util.Date;

import javax.sql.DataSource;
import javax.ws.rs.core.UriInfo;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import es.tid.fiware.rss.common.properties.AppProperties;
import es.tid.fiware.rss.common.test.DatabaseLoader;
import es.tid.fiware.rss.expenditureLimit.server.common.ExpenditureLimitCommon;

/**
 * 
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:database.xml"})
public class ExpenditureLimitCommonTest {
    /**
     * Logging system.
     */
    private static Logger logger = LoggerFactory.getLogger(ExpenditureLimitCommonTest.class);
    /**
     * For database access.
     */
    @Autowired
    private DataSource dataSource;
    /**
     * 
     */
    @Autowired
    private DatabaseLoader databaseLoader;
    /**
     * 
     */
    @Autowired
    private AppProperties appProperties;

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

        ExpenditureLimitCommon.getResourceUrl(appProperties, null, "profileId", "resource");
        ExpenditureLimitCommon.getResourceUrl(appProperties, mockUriInfo, "", "resource");
        Mockito.when(mockUriInfo.getBaseUri()).thenReturn(new URI("http://www.test.com/"));
        ExpenditureLimitCommon.getResourceUrl(appProperties, mockUriInfo, "", null);
        Assert.assertTrue(true);
    }

    /**
     * 
     */
    @Test
    public void getServiceUrl() {
        ExpenditureLimitCommon.getServiceUrl(appProperties);
        Assert.assertTrue(true);
    }
}
