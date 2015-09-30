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

/**
 * 
 */
package es.upm.fiware.rss.dao.impl.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.upm.fiware.rss.common.test.DatabaseLoader;
import es.upm.fiware.rss.dao.impl.CurrencyDaoImpl;
import es.upm.fiware.rss.model.BmCurrency;

/**
 * 
 * 
 */
@ContextConfiguration({"classpath:database.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class CurrencyDaoImplTest {

    /**
     * Variable to print the trace.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyDaoImplTest.class);

    /**
     * DAO for currency.
     */
    @Autowired
    private CurrencyDaoImpl currencyDAO;

    @Autowired
    private DatabaseLoader databaseLoader;
    
    /*@Autowired
    @Qualifier("transactionManager")
    private HibernateTransactionManager transactionManager;*/

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
     * Test method for {@link es.upm.fiware.rss.dao.impl.CurrencyDaoImpl#getByIso4217Code(java.lang.String)}.
     */
    public void test10GetByIso4217Code() {
        CurrencyDaoImplTest.LOGGER.debug("Into test10GetByIso4217Code()");
        // Call method to test
        BmCurrency c = currencyDAO.getByIso4217StringCode("EUR");

        // Check result
        Assert.assertNotNull("BmCurrency is null", c);
        Assert.assertTrue("ID not equeal", c.getNuCurrencyId() == 1);
        // Assert.assertTrue("ISO 3166 code not equeal", c.getTxIso4217Code().compareTo("978-EUR") == 0);
        Assert.assertTrue("Description not equeal", c.getTxDescription().compareTo("Euro") == 0);
        Assert.assertTrue("Symbol not equeal", c.getTcSymbol().compareTo("€") == 0);
    }

    /**
     * Test method for {@link es.upm.fiware.rss.dao.impl.CurrencyDaoImpl#getByIso4217StringCode(java.lang.String)}.
     */
    public void test20GetByIso4217StringCode() {
        CurrencyDaoImplTest.LOGGER.debug("Into test20GetByIso4217StringCode()");
        // Call method to test
        BmCurrency c = currencyDAO.getByIso4217StringCode("EUR");

        // Check result
        Assert.assertNotNull("BmCurrency is null", c);
        Assert.assertTrue("ID not equeal", c.getNuCurrencyId() == 1);
        // Assert.assertTrue("ISO 3166 code not equeal", c.getTxIso4217Code().compareTo("978-EUR") == 0);
        Assert.assertTrue("ISO 3166 code not equeal", c.getTxIso4217Code().compareTo("EUR") == 0);
        Assert.assertTrue("Description not equeal", c.getTxDescription().compareTo("Euro") == 0);
        Assert.assertTrue("Symbol not equeal", c.getTcSymbol().compareTo("€") == 0);
    }

    /**
     * Test method for {@link es.upm.fiware.rss.dao.impl.CurrencyDaoImpl#getByIso4217IntegerCode(int)}.
     */
    public void test30GetByIso4217IntegerCode() {
        CurrencyDaoImplTest.LOGGER.debug("Into test30GetByIso4217IntegerCode()");
        // Call method to test
        BmCurrency c = currencyDAO.getByIso4217IntegerCode(978);

        // Check result
        Assert.assertNotNull("BmCurrency is null", c);
        Assert.assertTrue("ID not equeal", c.getNuCurrencyId() == 1);
        // Assert.assertTrue("ISO 3166 code not equeal", c.getTxIso4217Code().compareTo("978-EUR") == 0);
        Assert.assertTrue("ISO 3166 code not equeal", c.getTxIso4217Code().compareTo("EUR") == 0);
        Assert.assertTrue("Description not equeal", c.getTxDescription().compareTo("Euro") == 0);
        Assert.assertTrue("Symbol not equeal", c.getTcSymbol().compareTo("€") == 0);
    }

}
