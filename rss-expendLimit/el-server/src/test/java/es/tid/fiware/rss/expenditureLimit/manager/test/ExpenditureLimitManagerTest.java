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

package es.tid.fiware.rss.expenditureLimit.manager.test;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
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
import es.tid.fiware.rss.expenditureLimit.api.LimitGroupBean;
import es.tid.fiware.rss.expenditureLimit.api.UserExpenditureLimitInfoBean;
import es.tid.fiware.rss.expenditureLimit.server.service.ExpenditureLimitDataChecker;
import es.tid.fiware.rss.expenditureLimit.server.service.ExpenditureLimitManager;
import es.tid.fiware.rss.expenditureLimit.test.ExpenditureLimitBeanConstructor;

/**
 * .
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:database.xml" })
public class ExpenditureLimitManagerTest {

    /**
     * Logging system.
     */
    private static Logger logger = LoggerFactory.getLogger(ExpenditureLimitManagerTest.class);
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
     */
    @Autowired
    ExpenditureLimitDataChecker checker;
    /**
     * 
     */
    @Autowired
    ExpenditureLimitManager elManager;
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * To get the data to be sent.
     */
    protected final ExpenditureLimitBeanConstructor constructor = new ExpenditureLimitBeanConstructor();

    private final String appProvider = "app123456";
    private final String currency = "EUR";
    private final String type = "daily";
    private final String service = "ServiceTest1";
    private final String userId = "userId01";

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
     */
    @Test
    public void getGeneralProviderExpLimits() throws RSSException {
        ExpenditureLimitManagerTest.logger.debug("Into getGeneralProviderExpLimits method");
        LimitGroupBean limits = elManager.getGeneralProviderExpLimitsBean(appProvider, service, currency, type);
        ExpenditureLimitManagerTest.logger.debug("Limits size:" + limits.getLimits().size());
        Assert.assertEquals(1, limits.getLimits().size());
        Assert.assertEquals(service, limits.getService());
        Assert.assertEquals(currency, limits.getLimits().get(0).getCurrency());
        Assert.assertEquals(type, limits.getLimits().get(0).getType());
    }

    /**
     * 
     */
    @Test
    public void storeGeneralProviderExpLimits() throws RSSException {
        ExpenditureLimitManagerTest.logger.debug("Into storeGeneralProviderExpLimits method");
        thrown.expect(RSSException.class);
        thrown.expectMessage("LimitGroupBean");
        LimitGroupBean expLimits = ExpenditureLimitBeanConstructor.generateLimitGroupBean();
        LimitGroupBean limits = elManager.storeGeneralProviderExpLimit(appProvider, expLimits);
        Assert.assertNotNull(limits);
        Assert.assertEquals(expLimits.getService(), limits.getService());
        Assert.assertEquals(expLimits.getLimits().size(), limits.getLimits().size());
        Assert.assertEquals(expLimits.getLimits().get(0).getCurrency(),
            limits.getLimits().get(0).getCurrency());
        ExpenditureLimitManagerTest.logger.debug("ObtainException Exception for test");
        limits = elManager.storeGeneralProviderExpLimit(appProvider, null);
    }

    /**
     * 
     * @throws RSSException
     */
    @Test
    public void storeGeneralProviderExpLimitsNoService() throws RSSException {
        ExpenditureLimitManagerTest.logger.debug("Into storeGeneralProviderExpLimits method");
        thrown.expect(RSSException.class);
        thrown.expectMessage("Service Identifier");
        LimitGroupBean expLimits = ExpenditureLimitBeanConstructor.generateLimitGroupBean();
        expLimits.setService(null);
        elManager.storeGeneralProviderExpLimit(appProvider, expLimits);
        ExpenditureLimitManagerTest.logger.debug("Exception expected");
    }

    /**
     * 
     * @throws RSSException
     */
    @Test
    public void storeGeneralProviderExpLimitsNoAppProvider() throws RSSException {
        ExpenditureLimitManagerTest.logger.debug("Into storeGeneralProviderExpLimits method");
        thrown.expect(RSSException.class);
        thrown.expectMessage("Provider Identifier");
        LimitGroupBean expLimits = ExpenditureLimitBeanConstructor.generateLimitGroupBean();
        elManager.storeGeneralProviderExpLimit(null, expLimits);
        ExpenditureLimitManagerTest.logger.debug("Exception expected");
    }

    /**
     * 
     */
    @Test
    public void deleteGeneralProviderExpLimits() throws RSSException {
        ExpenditureLimitManagerTest.logger.debug("Into deleteGeneralProviderExpLimits method");
        elManager.deleteProviderLimits(appProvider, service, currency, type);
        ExpenditureLimitManagerTest.logger.debug("No exception produced");
    }

    /**
     * 
     */
    @Test
    public void getGeneralUserExpLimits() throws RSSException {
        ExpenditureLimitManagerTest.logger.debug("Into getGeneralUserExpLimits method");
        UserExpenditureLimitInfoBean limits = elManager.getGeneralUserExpLimitsBean(userId, appProvider, service,
            currency, type);
        ExpenditureLimitManagerTest.logger.debug("Limits size:" + limits.getGeneralUserLimits());
        Assert.assertEquals(1, limits.getGeneralUserLimits().size());
        Assert.assertEquals(service, limits.getService());
        Assert.assertEquals(currency, limits.getGeneralUserLimits().get(0).getCurrency());
        Assert.assertEquals(type, limits.getGeneralUserLimits().get(0).getType());

    }

    /**
     * 
     */
    @Test
    public void storeGeneralUserExpLimits() throws RSSException {
        ExpenditureLimitManagerTest.logger.debug("Into storeGeneralUserExpLimits method");
        thrown.expect(RSSException.class);
        thrown.expectMessage("LimitGroupBean");
        LimitGroupBean expLimits = ExpenditureLimitBeanConstructor.generateLimitGroupBean();
        UserExpenditureLimitInfoBean limits = elManager.storeGeneralUserExpLimit(appProvider, userId, expLimits);
        Assert.assertNotNull(limits);
        Assert.assertEquals(expLimits.getService(), limits.getService());
        Assert.assertEquals(expLimits.getLimits().get(0).getCurrency(),
            limits.getGeneralUserLimits().get(0).getCurrency());
        ExpenditureLimitManagerTest.logger.debug("ObtainException Exception for test");
        elManager.storeGeneralUserExpLimit(appProvider, userId, null);
    }

    /**
     * 
     * @throws RSSException
     */
    @Test
    public void storeGeneralUserExpLimitsNoService() throws RSSException {
        ExpenditureLimitManagerTest.logger.debug("Into storeGeneralProviderExpLimits method");
        thrown.expect(RSSException.class);
        thrown.expectMessage("Service Identifier");
        LimitGroupBean expLimits = ExpenditureLimitBeanConstructor.generateLimitGroupBean();
        expLimits.setService(null);
        elManager.storeGeneralUserExpLimit(appProvider, userId, expLimits);
        ExpenditureLimitManagerTest.logger.debug("Exception expected");
    }

    /**
     * 
     * @throws RSSException
     */
    @Test
    public void storeGeneralUserExpLimitsNoAppProvider() throws RSSException {
        ExpenditureLimitManagerTest.logger.debug("Into storeGeneralProviderExpLimits method");
        thrown.expect(RSSException.class);
        thrown.expectMessage("Provider Identifier");
        LimitGroupBean expLimits = ExpenditureLimitBeanConstructor.generateLimitGroupBean();
        elManager.storeGeneralUserExpLimit(null, userId, expLimits);
        ExpenditureLimitManagerTest.logger.debug("Exception expected");
    }

    /**
     * 
     */
    @Test
    public void deleteGeneralUserExpLimits() throws RSSException {
        ExpenditureLimitManagerTest.logger.debug("Into deleteGeneralUserExpLimits method");
        elManager.deleteUserLmits(appProvider, userId, service, currency, type);
        ExpenditureLimitManagerTest.logger.debug("No exception produced");
    }

}
