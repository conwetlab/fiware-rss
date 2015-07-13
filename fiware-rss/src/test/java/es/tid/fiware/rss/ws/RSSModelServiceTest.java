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

import java.math.BigDecimal;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import javax.ws.rs.core.Response;

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
import es.tid.fiware.rss.model.RSSModel;
import es.tid.fiware.rss.oauth.model.ValidatedToken;
import es.tid.fiware.rss.service.RSSModelsManager;

/**
 * 
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:database.xml", "classpath:cxf-beans.xml"})
public class RSSModelServiceTest {
    /**
     * Logging system.
     */
    private static Logger logger = LoggerFactory.getLogger(RSSModelServiceTest.class);
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
    private RSSModelService rssModelService;

    /**
     * 
     */
    private RSSModelsManager rssModelsManager;
    /**
     * 
     */
    private RSSModel rssModel;

    /**
     * Method to insert data before test.
     * 
     * @throws Exception
     *             from db
     */
    @Before
    public void setUp() throws Exception {
        databaseLoader.cleanInsert("dbunit/CREATE_DATATEST_EXPLIMIT.xml", true);
    }

    /**
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        databaseLoader.deleteAll("dbunit/CREATE_DATATEST_EXPLIMIT.xml", true);
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void deleteRSSModel() throws Exception {
        RSSModelServiceTest.logger.debug("Into deleteUserAccumulated method");
        Response response = rssModelService.deleteRSSModel("authToken", "appProvider", "productClass");
        Mockito.doNothing().when(rssModelsManager).deleteRssModel("mail@mail.com", "appProvider", "productClass");
        Assert.assertEquals(200, response.getStatus());
    }
}
