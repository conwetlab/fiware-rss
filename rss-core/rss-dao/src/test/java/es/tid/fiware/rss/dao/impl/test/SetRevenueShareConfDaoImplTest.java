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

/**
 * 
 */
package es.tid.fiware.rss.dao.impl.test;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import es.tid.fiware.rss.common.test.DatabaseLoader;
import es.tid.fiware.rss.dao.SetRevenueShareConfDao;
import es.tid.fiware.rss.model.SetRevenueShareConf;

/**
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:database.xml"})
public class SetRevenueShareConfDaoImplTest {

    /**
     * DAO for country.
     */
    @Autowired
    private SetRevenueShareConfDao setRevenueShareConfDao;

    @Autowired
    private DatabaseLoader databaseLoader;
    @Autowired
    @Qualifier("transactionManager")
    private HibernateTransactionManager transactionManager;

    /**
     * Method to insert data before test.
     * 
     * @throws Exception
     *             from db
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
     * Test method for
     * {@link es.tid.fiware.rss.dao.impl.SetRevenueShareConfDaoImpl#getRevenueModelsByProviderId(java.lang.String)}.
     */
    @Test
    public void testGetRevenueModelsByProviderId() {
        // Call method to test
        List<SetRevenueShareConf> c = setRevenueShareConfDao.getRevenueModelsByProviderId("123456");
        // Check result
        Assert.assertNotNull("List<SetRevenueShareConf> is null", c);
        Assert.assertTrue("It has no results", c.size() > 0);
        Assert.assertTrue("ID not equal", c.get(0).getId().getTxAppProviderId().equalsIgnoreCase("123456"));
        Assert.assertTrue("ID not equal", c.get(0).getId().getCountryId() == 1);
        Assert.assertTrue("ID not equal", c.get(0).getId().getNuObId() == 1);
        // non existing provider
        c = setRevenueShareConfDao.getRevenueModelsByProviderId("nonExisting");
        Assert.assertNull("List<SetRevenueShareConf> is not null", c);
    }

    /**
     * Test method for
     * {@link es.tid.fiware.rss.dao.impl.SetRevenueShareConfDaoImpl#getRevenueModelsByParameters(java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testGetRevenueModelsByParameters() {
        // tx_email="mail@mail.com"
        // tx_appprovider_id="123456"
        // tx_product_class="productClass"
        // Call method to test
        // check only aggregator
        List<SetRevenueShareConf> result = setRevenueShareConfDao.getRevenueModelsByParameters("mail@mail.com", "",
            null);
        checkResult(result);
        // check aggregator and provider
        result = setRevenueShareConfDao.getRevenueModelsByParameters("mail@mail.com", "123456", null);
        checkResult(result);
        // check aggregator and provider and class
        result = setRevenueShareConfDao.getRevenueModelsByParameters("mail@mail.com", "123456", null);
        checkResult(result);
        // check aggregator and provider
        result = setRevenueShareConfDao.getRevenueModelsByParameters("mail@mail.com", "", "productClass");
        checkResult(result);
        // empty product class
        result = setRevenueShareConfDao.getRevenueModelsByParameters("mail@mail.com", "", "");
        checkResult(result);
        // non existing product class
        result = setRevenueShareConfDao.getRevenueModelsByParameters("mail@mail.com", "", "nonExisting");
        Assert.assertNull("List<SetRevenueShareConf> is not null", result);
    }

    /**
     * Check the result.
     * 
     * @param result
     */
    private void checkResult(List<SetRevenueShareConf> result) {
        // Check result
        Assert.assertNotNull("List<SetRevenueShareConf> is null", result);
        Assert.assertTrue("It has no results", result.size() > 0);
        Assert.assertTrue("ID not equal", result.get(0).getId().getTxAppProviderId().equalsIgnoreCase("123456"));
        Assert.assertTrue("ID not equal", result.get(0).getId().getCountryId() == 1);
        Assert.assertTrue("ID not equal", result.get(0).getId().getNuObId() == 1);
    }

}
