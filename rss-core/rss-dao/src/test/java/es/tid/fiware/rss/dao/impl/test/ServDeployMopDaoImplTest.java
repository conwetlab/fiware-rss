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
import es.tid.fiware.rss.dao.ServDeployMopDao;
import es.tid.fiware.rss.model.BmServdeployMop;

/**
 * 
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:database.xml"})
public class ServDeployMopDaoImplTest {

    @Autowired
    private ServDeployMopDao servDeployMopDao;

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
     * {@link es.tid.fiware.rss.dao.impl.ServDeployMopDaoImpl#getServDeployMopFilter(java.lang.Long, java.lang.Long)}.
     */
    @Test
    public void test10GetServDeployMopFilter() {
        // Call method to test
        List<BmServdeployMop> list = servDeployMopDao.getServDeployMopFilter(new Long(1), new Long(1));

        // Check result
        Assert.assertTrue("Size not equal", list.size() == 3);
        for (int i = 0; i < list.size(); i++) {
            BmServdeployMop servdeployMop = list.get(i);

            long idCustomerType = 1;
            long idDeployment = 0;
            long idMop = 0;
            String defaultYn = "";
            switch (servdeployMop.getNuServiceMopId().intValue()) {
                case 1:
                    idCustomerType = 1;
                    idDeployment = 1;
                    idMop = 1;
                    defaultYn = "N";
                    break;
                case 2:
                    idCustomerType = 1;
                    idDeployment = 1;
                    idMop = 2;
                    defaultYn = "N";
                    break;
                case 6:
                    idCustomerType = 1;
                    idDeployment = 1;
                    idMop = 3;
                    defaultYn = "N";
                    break;
            }

            Assert.assertTrue("Customer type ID not equal",
                servdeployMop.getBmCustomerType().getNuCustomerTypeId() == idCustomerType);
            Assert.assertTrue("Deployment ID not equal",
                servdeployMop.getBmServiceDeployment().getNuDeploymentId() == idDeployment);
            Assert.assertTrue("MOP ID not equal", servdeployMop.getBmMethodsOfPayment().getNuMopId() == idMop);
            Assert.assertTrue("defaultYn not equal", servdeployMop.getTcDefaultYn().compareTo(defaultYn) == 0);
        }

        // call null parameter
        list = servDeployMopDao.getServDeployMopFilter(new Long(1), null);
        Assert.assertNotNull("Size not equal", list);
        Assert.assertTrue("Size not equal", list.size() == 6);
        Assert.assertTrue("Size not equal", list.get(0).getBmServiceDeployment().getNuDeploymentId() == 1);

    }

    /**
     * Test method for
     * {@link es.tid.fiware.rss.dao.impl.ServDeployMopDaoImpl#getDefaultMop(java.lang.Long, java.lang.Long)}.
     */
    @Test
    public void test20GetDefaultMop() {
        // Call method to test
        BmServdeployMop list = servDeployMopDao.getDefaultMop(new Long(1), new Long(2));

        // Check result
        // Assert.assertTrue("Size not equal", list.size() == 1);
        BmServdeployMop servdeployMop = list;
        Assert.assertTrue("Customer type ID not equal", servdeployMop.getBmCustomerType().getNuCustomerTypeId() == 2);
        Assert.assertTrue("Deployment ID not equal", servdeployMop.getBmServiceDeployment().getNuDeploymentId() == 1);
        Assert.assertTrue("MOP ID not equal", servdeployMop.getBmMethodsOfPayment().getNuMopId() == 1);
        Assert.assertTrue("defaultYn not equal", servdeployMop.getTcDefaultYn().compareTo("Y") == 0);
    }

    /**
     * Test method for
     * {@link es.tid.fiware.rss.dao.impl.ServDeployMopDaoImpl#getDefaultMop(java.lang.Long, java.lang.Long)}.
     */
    @Test
    public void testGetServiceDeployMop() {
        // Call method to test
        BmServdeployMop list = servDeployMopDao.getServiceDeployMop(new Long(1), new Long(1));

        // Check result
        BmServdeployMop servdeployMop = list;
        Assert.assertTrue("Customer type ID not equal", servdeployMop.getBmCustomerType().getNuCustomerTypeId() == 1);
        Assert.assertTrue("Deployment ID not equal", servdeployMop.getBmServiceDeployment().getNuDeploymentId() == 1);
        Assert.assertTrue("MOP ID not equal", servdeployMop.getBmMethodsOfPayment().getNuMopId() == 1);
        Assert.assertTrue("defaultYn not equal", servdeployMop.getTcDefaultYn().compareTo("N") == 0);
    }

    /**
     * Test method for
     * {@link es.tid.fiware.rss.dao.impl.ServDeployMopDaoImpl#listServDeployMopsbyDeploymentId(java.lang.Long)}.
     */
    @Test
    public void testListServDeployMopsbyDeploymentId() {
        // Call method to test
        List<BmServdeployMop> list = servDeployMopDao.listServDeployMopsbyDeploymentId(new Long(1));

        // Check result
        Assert.assertNotNull("No result", list);
        Assert.assertTrue("Size not equal", list.size() > 0);
        Assert.assertTrue("Size not equal", list.get(0).getBmServiceDeployment().getNuDeploymentId() == 1);
    }

}
