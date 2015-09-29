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

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.test.context.transaction.TransactionConfiguration;

import es.upm.fiware.rss.common.test.DatabaseLoader;
import es.upm.fiware.rss.dao.DbeSystemPropertiesDao;
import es.upm.fiware.rss.model.DbeSystemProperties;

/**
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:database.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DbeSystemPropertiesDaoImplTest {

    /**
     * DAO for country.
     */
    @Autowired
    private DbeSystemPropertiesDao dbeSystemPropertiesDao;

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
     * {@link es.upm.fiware.rss.dao.impl.DbeSystemPropertiesDaoImpl#getAllByParamClass(java.lang.String)}.
     */
    @Test
    @Transactional
    public void testaGetAllByParamClass() {
        // Call method to test
        List<DbeSystemProperties> c = dbeSystemPropertiesDao.getAllByParamClass("paramClass");

        // Check result
        Assert.assertNotNull("List<List<DbeSystemProperties> is null", c);
        Assert.assertNotNull("List<BmObCountry> is null", c.size() > 0);
        Assert.assertTrue("PricePoint", c.get(0).getTxParamClass().equalsIgnoreCase("paramClass"));

    }

    @Test
    @Transactional
    public void testbExit() {
        Assert.assertTrue(dbeSystemPropertiesDao.exists("name"));
        Assert.assertFalse(dbeSystemPropertiesDao.exists("noname"));
    }

    @Test
    @Transactional
    public void testcSize() {
        Assert.assertEquals(2, dbeSystemPropertiesDao.count());
    }

    @Test
    @Transactional
    public void testdGetAll() {
        Assert.assertTrue(dbeSystemPropertiesDao.getAll().size() == 2);
    }

    @Test
    @Transactional(propagation = Propagation.SUPPORTS)
    public void testeUpdate() {
        DbeSystemProperties c = dbeSystemPropertiesDao.getById("name");
        c.setTxParamDescription("new description");
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(Propagation.REQUIRES_NEW.value());
        TransactionStatus status = transactionManager.getTransaction(def);
        dbeSystemPropertiesDao.update(c);
        transactionManager.commit(status);
        c = dbeSystemPropertiesDao.getById("name");
        Assert.assertTrue(dbeSystemPropertiesDao.getById("name").getTxParamDescription()
            .equalsIgnoreCase("new description"));
    }

    @Test
    @Transactional(propagation = Propagation.SUPPORTS)
    public void testfDelete() {
        DbeSystemProperties c = dbeSystemPropertiesDao.getById("name");
        dbeSystemPropertiesDao.delete(c);
        Assert.assertTrue(dbeSystemPropertiesDao.getById("name") == null);
    }

    @Test
    @Transactional(propagation = Propagation.SUPPORTS)
    public void testgDeleteById() {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(Propagation.REQUIRES_NEW.value());
        TransactionStatus status = transactionManager.getTransaction(def);
        dbeSystemPropertiesDao.deleteById("name2");
        transactionManager.commit(status);
        Assert.assertTrue(dbeSystemPropertiesDao.getById("name2") == null);
    }

    @Test
    @Transactional(propagation = Propagation.SUPPORTS)
    public void testhDeleteAll() {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(Propagation.REQUIRES_NEW.value());
        TransactionStatus status = transactionManager.getTransaction(def);
        dbeSystemPropertiesDao.deleteAll();
        transactionManager.commit(status);
        Assert.assertTrue(dbeSystemPropertiesDao.getAll().size() == 0);
    }

}
