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

package es.tid.fiware.rss.ws;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import javax.annotation.PostConstruct;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import es.tid.fiware.rss.common.test.DatabaseLoader;
import es.tid.fiware.rss.oauth.model.ValidatedToken;
import es.tid.fiware.rss.oauth.service.OauthManager;
import es.tid.fiware.rss.service.CdrsManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:database.xml", "/META-INF/spring/application-context.xml",
    "classpath:cxf-beans.xml"})
public class CdrsServiceTest {
    private final Logger logger = LoggerFactory.getLogger(CdrsServiceTest.class);
    /**
     * 
     */
    @Autowired
    private DatabaseLoader databaseLoader;
    /**
     * /**
     * 
     */
    @Autowired
    private CdrsService cdrsService;
    /**
     * 
     */
    private OauthManager oauthManager;
    /**
     * 
     */
    private CdrsManager cdrsManager;

    /**
     * 
     */
    @PostConstruct
    public void init() throws Exception {
        ValidatedToken validToken = new ValidatedToken();
        validToken.setEmail("mail@mail.com");
        oauthManager = Mockito.mock(OauthManager.class);
        Mockito.when(oauthManager.checkAuthenticationToken("authToken")).thenReturn(validToken);
        ReflectionTestUtils.setField(cdrsService, "oauthManager", oauthManager);
        UriInfo mockUriInfo = Mockito.mock(UriInfo.class);
        Mockito.when(mockUriInfo.getBaseUri()).thenReturn(new URI("http://www.test.com/go"));
        ReflectionTestUtils.setField(cdrsService, "ui", mockUriInfo);
        cdrsManager = Mockito.mock(CdrsManager.class);
        ReflectionTestUtils.setField(cdrsService, "cdrsManager", cdrsManager);
    }

    /**
     * Method to insert data before test.
     * 
     * @throws Exception
     *             from dbb
     */
    @Before
    public void setUp() throws Exception {
        databaseLoader.cleanInsert("dbunit/CREATE_DATATEST_TRANSACTIONS.xml", true);
    }

    /**
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        databaseLoader.deleteAll("dbunit/CREATE_DATATEST_TRANSACTIONS.xml", true);
    }

    /**
     * 
     */
    @Test
    public void testCreateCdr() throws Exception {
        logger.debug("Into testCreateCdr test");
        File file = new File("file");
        Mockito.when(cdrsManager.runCdrToDB()).thenReturn(null);
        logger.debug("Error creating file");
        Response response = cdrsService.createCdr("authToken", "xml");
        Assert.assertEquals(500, response.getStatus());
        Mockito.when(cdrsManager.getFile()).thenReturn(file);
        logger.debug("Normal End");
        response = cdrsService.createCdr("authToken", "xml");
        Assert.assertEquals(200, response.getStatus());
        logger.debug("fail processing");
        Mockito.when(cdrsManager.runCdrToDB()).thenReturn("ERROR");
        response = cdrsService.createCdr("authToken", "xml");
        Assert.assertEquals(500, response.getStatus());
        logger.debug("Exception processing");
        Mockito.doThrow(new IOException("Exception")).when(cdrsManager).runCdrToDB();
        response = cdrsService.createCdr("authToken", "xml");
        Assert.assertEquals(500, response.getStatus());
    }

}
