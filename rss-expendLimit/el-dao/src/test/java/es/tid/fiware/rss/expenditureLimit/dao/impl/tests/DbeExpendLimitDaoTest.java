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

/*
 * DbeSystemPropertiesDaoTest.java
 * 
 * 2012 ®, Telefónica I+D, all rights reserved
 */
package es.tid.fiware.rss.expenditureLimit.dao.impl.tests;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.context.transaction.TransactionConfiguration;

import es.tid.fiware.rss.common.test.DatabaseLoader;
import es.tid.fiware.rss.expenditureLimit.dao.DbeExpendLimitDao;
import es.tid.fiware.rss.expenditureLimit.model.DbeExpendLimit;
import es.tid.fiware.rss.model.BmCurrency;

/**
 * 
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:database.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class DbeExpendLimitDaoTest {

    /**
     * Logging system.
     */
    private static Logger logger = LoggerFactory.getLogger(DbeExpendLimitDaoTest.class);

    @Autowired
    private DataSource dataSource;

    @Autowired
    private DbeExpendLimitDao expLimitDao;

    @Autowired
    private DatabaseLoader databaseLoader;

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

    @After
    public void tearDown() throws Exception {
        databaseLoader.deleteAll("dbunit/CREATE_DATATEST_EXPLIMIT.xml", true);
    }

    @Test
    public void testGetExpenditureLimitInfByUser() {

        BmCurrency bmCurrency = new BmCurrency();
        bmCurrency.setNuCurrencyId(1);
        List<DbeExpendLimit> l = expLimitDao.getExpendLimitsForUserAppProvCurrency("userId01",
            "app123456", bmCurrency);

        Assert.assertTrue("Elements founds", l != null && l.size() == 6);
        Iterator<DbeExpendLimit> it = l.iterator();
        while (it.hasNext()) {
            DbeExpendLimit el = it.next();
            if (!el.getId().getTxAppProviderId().equalsIgnoreCase(DbeExpendLimitDao.NO_APP_PROVIDER_ID)
                && !el.getId().getTxAppProviderId().equalsIgnoreCase("app123456")) {
                Assert.fail("Application provider invalid: " + el.getId().getTxAppProviderId());
            }
            if (!el.getId().getTxEndUserId().equalsIgnoreCase(DbeExpendLimitDao.NO_USER_ID)
                && !el.getId().getTxEndUserId().equalsIgnoreCase("userId01")) {
                Assert.fail("User invalid: " + el.getId().getTxEndUserId());
            }
        }
    }

    @Test
    public void testGetExpenditureLimitInfByUserNullCurrency() {
        List<DbeExpendLimit> l = expLimitDao.getExpendLimitsForUserAppProvCurrency("userId01",
            "app123456", null);

        Assert.assertTrue("Elements founds", l != null && l.size() == 6);
        Iterator<DbeExpendLimit> it = l.iterator();
        while (it.hasNext()) {
            DbeExpendLimit el = it.next();
            if (!el.getId().getTxAppProviderId().equalsIgnoreCase(DbeExpendLimitDao.NO_APP_PROVIDER_ID)
                && !el.getId().getTxAppProviderId().equalsIgnoreCase("app123456")) {
                Assert.fail("Application provider invalid: " + el.getId().getTxAppProviderId());
            }
            if (!el.getId().getTxEndUserId().equalsIgnoreCase(DbeExpendLimitDao.NO_USER_ID)
                && !el.getId().getTxEndUserId().equalsIgnoreCase("userId01")) {
                Assert.fail("User invalid: " + el.getId().getTxEndUserId());
            }
        }
    }

    @Test
    public void testOrderedGetExpenditureLimitInfByUser() {

        BmCurrency bmCurrency = new BmCurrency();
        bmCurrency.setNuCurrencyId(1);
        HashMap<String, List<DbeExpendLimit>> h = expLimitDao.getOrdExpLimitsForUserAppProvCurrency(
            "userId01","app123456", bmCurrency);

        Assert.assertTrue("List founds", h != null && h.size() == 4);
        List<DbeExpendLimit> l = h.get(DbeExpendLimitDao.USER_APP_PROV_KEY);
        Assert.assertTrue("Especific elements founds", l != null && l.size() == 1);
        Iterator<DbeExpendLimit> it = l.iterator();
        while (it.hasNext()) {
            DbeExpendLimit el = it.next();
            if (!el.getId().getTxAppProviderId().equalsIgnoreCase("app123456")) {
                Assert.fail("Application provider invalid: " + el.getId().getTxAppProviderId());
            }
            if (!el.getId().getTxEndUserId().equalsIgnoreCase("userId01")) {
                Assert.fail("User invalid: " + el.getId().getTxEndUserId());
            }
        }

        l = h.get(DbeExpendLimitDao.USER_KEY);
        Assert.assertTrue("User elements founds", l != null && l.size() == 1);
        it = l.iterator();
        while (it.hasNext()) {
            DbeExpendLimit el = it.next();
            if (!el.getId().getTxAppProviderId().equalsIgnoreCase(DbeExpendLimitDao.NO_APP_PROVIDER_ID)) {
                Assert.fail("Application provider invalid: " + el.getId().getTxAppProviderId());
            }
            if (!el.getId().getTxEndUserId().equalsIgnoreCase("userId01")) {
                Assert.fail("User invalid: " + el.getId().getTxEndUserId());
            }
        }

        l = h.get(DbeExpendLimitDao.APP_PROV_KEY);
        Assert.assertTrue("App provider elements founds", l != null && l.size() == 1);
        it = l.iterator();
        while (it.hasNext()) {
            DbeExpendLimit el = it.next();
            if (!el.getId().getTxAppProviderId().equalsIgnoreCase("app123456")) {
                Assert.fail("Application provider invalid: " + el.getId().getTxAppProviderId());
            }
            if (!el.getId().getTxEndUserId().equalsIgnoreCase(DbeExpendLimitDao.NO_USER_ID)) {
                Assert.fail("User invalid: " + el.getId().getTxEndUserId());
            }
        }

        l = h.get(DbeExpendLimitDao.ALL_GENERIC_KEY);
        Assert.assertTrue("Generic elements founds", l != null && l.size() == 3);
        it = l.iterator();
        while (it.hasNext()) {
            DbeExpendLimit el = it.next();
            if (!el.getId().getTxAppProviderId().equalsIgnoreCase(DbeExpendLimitDao.NO_APP_PROVIDER_ID)) {
                Assert.fail("Application provider invalid: " + el.getId().getTxAppProviderId());
            }
            if (!el.getId().getTxEndUserId().equalsIgnoreCase(DbeExpendLimitDao.NO_USER_ID)) {
                Assert.fail("User invalid: " + el.getId().getTxEndUserId());
            }
        }
    }

    @Test
    public void getExpendLimitsByProviderUserService() {
        BmCurrency bmCurrency = new BmCurrency();
        bmCurrency.setNuCurrencyId(1);;
        List<DbeExpendLimit> l = expLimitDao.getExpendLimitsByProviderUserService(
            "app123456", "userId01", bmCurrency);

        Assert.assertTrue("Elements founds", l != null && l.size() == 1);
        Iterator<DbeExpendLimit> it = l.iterator();
        while (it.hasNext()) {
            DbeExpendLimit el = it.next();
            if (!el.getId().getTxAppProviderId().equalsIgnoreCase(DbeExpendLimitDao.NO_APP_PROVIDER_ID)
                && !el.getId().getTxAppProviderId().equalsIgnoreCase("app123456")) {
                Assert.fail("Application provider invalid: " + el.getId().getTxAppProviderId());
            }
            if (!el.getId().getTxEndUserId().equalsIgnoreCase(DbeExpendLimitDao.NO_USER_ID)
                && !el.getId().getTxEndUserId().equalsIgnoreCase("userId01")) {
                Assert.fail("User invalid: " + el.getId().getTxEndUserId());
            }
        }
    }

    @Test
    public void getExpendLimitsByProviderUserServiceNullCurrency() {
        List<DbeExpendLimit> l = expLimitDao.getExpendLimitsByProviderUserService(
            "app123456", "userId01", null);

        Assert.assertTrue("Elements founds", l != null && l.size() == 1);
        Iterator<DbeExpendLimit> it = l.iterator();
        while (it.hasNext()) {
            DbeExpendLimit el = it.next();
            if (!el.getId().getTxAppProviderId().equalsIgnoreCase(DbeExpendLimitDao.NO_APP_PROVIDER_ID)
                && !el.getId().getTxAppProviderId().equalsIgnoreCase("app123456")) {
                Assert.fail("Application provider invalid: " + el.getId().getTxAppProviderId());
            }
            if (!el.getId().getTxEndUserId().equalsIgnoreCase(DbeExpendLimitDao.NO_USER_ID)
                && !el.getId().getTxEndUserId().equalsIgnoreCase("userId01")) {
                Assert.fail("User invalid: " + el.getId().getTxEndUserId());
            }
        }
    }

}
