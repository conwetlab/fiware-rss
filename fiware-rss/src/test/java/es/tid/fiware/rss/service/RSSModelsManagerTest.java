/**
 * Revenue Settlement and Sharing System GE
 * Copyright (C) 2011-2014, Javier Lucio - lucio@tid.es
 * Telefonica Investigacion y Desarrollo, S.A.
 *
 * Copyright (C) 2015, CoNWeT Lab., Universidad Politénica de Madrid
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

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import es.tid.fiware.rss.common.test.DatabaseLoader;
import es.tid.fiware.rss.exception.RSSException;
import es.tid.fiware.rss.model.RSSModel;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:database.xml", "/META-INF/spring/application-context.xml"})
public class RSSModelsManagerTest {
    /***
     * Logging system.
     */
    private final Logger logger = LoggerFactory.getLogger(RSSModelsManagerTest.class);
    /**
     * 
     */
    @Autowired
    private DatabaseLoader databaseLoader;
    /**
     * 
     */
    @Autowired
    private RSSModelsManager rssModelsManager;
    /**
     * 
     */
    private String appProviderId = "123456";
    /**
     * 
     */
    private String aggregatorId = "mail@mail.com";
    /**
     * 
     */
    private RSSModel rssModel;
    /**
     * 
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Method to insert data before test.
     * 
     * @throws Exception
     *             from dbb
     */

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
    @Transactional(propagation = Propagation.SUPPORTS)
    public void checkValidAppProviderTest() throws Exception {
        logger.debug("checkValidAppProviderTest");
        // nothing happens
        rssModelsManager.checkValidAppProvider(aggregatorId, appProviderId);
        thrown.expect(RSSException.class);
        thrown.expectMessage("Non existing: appProviderId");
        rssModelsManager.checkValidAppProvider(aggregatorId, "nonExisting");
    }

    /**
     * 
     */
    @Test
    @Transactional(propagation = Propagation.SUPPORTS)
    public void checkNonValidAppProviderTest() throws Exception {
        logger.debug("checkNonValidAppProviderTest");
        thrown.expect(RSSException.class);
        thrown.expectMessage("Non existing: appProviderId");
        rssModelsManager.checkValidAppProvider(aggregatorId, "app123456");
    }

    /**
    * 
    */
    @Test
    @Transactional(propagation = Propagation.SUPPORTS)
    public void getRssModelsTest() throws Exception {
        logger.debug("getRssModelsTest");
        // with provider
        List<RSSModel> models = rssModelsManager.getRssModels(aggregatorId, appProviderId, null);
        Assert.assertTrue(models.size() > 0);
        // without provider
        models = rssModelsManager.getRssModels(aggregatorId, null, null);
        Assert.assertTrue(models.size() > 0);
    }

}
