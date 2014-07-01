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

package es.tid.fiware.rss.service;

import java.math.BigDecimal;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import es.tid.fiware.rss.common.test.DatabaseLoader;
import es.tid.fiware.rss.exception.RSSException;
import es.tid.fiware.rss.model.RSSModel;
import es.tid.fiware.rss.model.SetRevenueShareConf;
import es.tid.fiware.rss.model.SetRevenueShareConfId;

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
    @Before
    public void setUp() throws Exception {
        databaseLoader.cleanInsert("dbunit/CREATE_DATATEST_TRANSACTIONS.xml", true);
        // prepare mockito
        // ReflectionTestUtils.setField(unwrapSettlementManager(), "runtime", runtime);
        rssModel = new RSSModel();
        rssModel.setAppProviderId(appProviderId);
        rssModel.setProductClass("newProductClass");
        rssModel.setPercRevenueShare(BigDecimal.valueOf(40));
    }

    /**
     * 
     * @return
     * @throws Exception
     */
    private RSSModelsManager unwrapSettlementManager() throws Exception {
        if (AopUtils.isAopProxy(rssModelsManager) && rssModelsManager instanceof Advised) {
            Object target = ((Advised) rssModelsManager).getTargetSource().getTarget();
            return (RSSModelsManager) target;
        }
        return null;
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
    public void convertIntoApiModelTest() {
        logger.debug("convertIntoApiModelTest");
        SetRevenueShareConf model = new SetRevenueShareConf();
        SetRevenueShareConfId id = new SetRevenueShareConfId();
        model.setId(id);
        id.setProductClass("productClass");
        id.setTxAppProviderId("txAppProviderId");
        model.setNuPercRevenueShare(BigDecimal.valueOf(30));
        RSSModel finalModel = rssModelsManager.convertIntoApiModel(model);
        Assert.assertEquals(id.getProductClass(), finalModel.getProductClass());
        Assert.assertEquals(id.getTxAppProviderId(), finalModel.getAppProviderId());
        Assert.assertTrue(model.getNuPercRevenueShare().compareTo(finalModel.getPercRevenueShare()) == 0);
    }

    /**
     * 
     */
    @Test
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
    public void checkValidRSSModelTest() throws Exception {
        logger.debug("checkValidRSSModelTest");
        // nothing happens
        rssModelsManager.checkValidRSSModel(rssModel);
        thrown.expect(RSSException.class);
        thrown.expectMessage("Required parameters not found: appProviderId");
        rssModel.setAppProviderId("");
        rssModelsManager.checkValidRSSModel(rssModel);
    }

    @Test
    public void checkValidRSSModelNonRevenueTest() throws Exception {
        logger.debug("checkValidRSSModelTest");
        // nothing happens
        rssModelsManager.checkValidRSSModel(rssModel);
        thrown.expect(RSSException.class);
        thrown.expectMessage("Required parameters not found: percRevenueShare");
        rssModel.setPercRevenueShare(null);
        rssModelsManager.checkValidRSSModel(rssModel);
    }

    @Test
    public void checkValidRSSModelRevenuelessThan0Test() throws Exception {
        logger.debug("checkValidRSSModelTest");
        // nothing happens
        rssModelsManager.checkValidRSSModel(rssModel);
        thrown.expect(RSSException.class);
        thrown.expectMessage("percRevenueShare must be greater than 0");
        rssModel.setPercRevenueShare(BigDecimal.valueOf(-1));
        rssModelsManager.checkValidRSSModel(rssModel);
    }

    /**
    * 
    */
    @Test
    public void getRssModelsTest() throws Exception {
        logger.debug("getRssModelsTest");
        // with provider
        List<RSSModel> models = rssModelsManager.getRssModels(aggregatorId, appProviderId, null);
        Assert.assertTrue(models.size() > 0);
        // without provider
        models = rssModelsManager.getRssModels(aggregatorId, null, null);
        Assert.assertTrue(models.size() > 0);
    }

    /**
     * 
     */
    @Test
    public void createRssModelTest() throws Exception {
        logger.debug("createRssModelTest");
        RSSModel model = rssModelsManager.createRssModel(aggregatorId, rssModel);
        Assert.assertEquals(rssModel.getProductClass(), model.getProductClass());
    }

    /**
     * 
     */
    @Test
    public void updateRssModelTest() throws Exception {
        logger.debug("createRssModelTest");
        RSSModel model = rssModelsManager.updateRssModel(aggregatorId, rssModel);
        Assert.assertEquals(rssModel.getProductClass(), model.getProductClass());
        thrown.expect(RSSException.class);
        thrown.expectMessage("Non existing Rss Model.");
        rssModel.setProductClass("notExistingPC");
        model = rssModelsManager.updateRssModel(aggregatorId, rssModel);
    }

    /**
     * 
     */
    @Test
    public void deleteRssModelTest() throws Exception {
        logger.debug("deleteRssModelTest");
        // non expected error
        rssModel.setProductClass("prodcutClassTest");
        RSSModel model = rssModelsManager.createRssModel(aggregatorId, rssModel);
        rssModelsManager.deleteRssModel(aggregatorId, appProviderId, "prodcutClassTest");
        thrown.expect(RSSException.class);
        thrown.expectMessage("Required parameters not found: appProviderId.");
        rssModelsManager.deleteRssModel(aggregatorId, null, null);
    }

}
