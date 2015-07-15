package es.tid.fiware.rss.service;

/**
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

import es.tid.fiware.rss.model.DbeAppProvider;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author francisco
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:database.xml", "/META-INF/spring/application-context.xml",
    "/META-INF/spring/cxf-beans.xml"})
public class ProviderManagerTest {

    private final Logger logger = LoggerFactory.getLogger(ProviderManagerTest.class);
    /**
     * 
     */
    private final String aggregatorId = "mail@mail.com";
    /**
     * 
     */

    @Autowired
    ProviderManager providerManager;

    @Test
    @Transactional(propagation = Propagation.SUPPORTS)
    public void CreateProviderTest() {
        logger.debug("CreateProviderTest");
        try {
            logger.debug("run with aggregator");
            providerManager.createProvider("newProvider1", "providerName", null);
            logger.debug("run without aggregator");
            providerManager.createProvider("newProvider12", "providerName", aggregatorId);
        } catch (Exception e) {
            Assert.fail();
        }

    }

   /**
     * 
     * @throws Exception
     */
    @Test
    @Transactional(propagation = Propagation.SUPPORTS)
    public void getProvidersTest() throws Exception {
        logger.debug("run all providers");
        List<DbeAppProvider> providers = providerManager.getProviders(null);
        Assert.assertNotNull(providers);
        logger.debug("run aggregatorId");
        providers = providerManager.getProviders(aggregatorId);
        Assert.assertNotNull(providers);
        logger.debug("run no aggregatorId");
        providers = providerManager.getProviders("nonExistingAggregatorId");
        Assert.assertTrue(providers.isEmpty());
    }
}
