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
import es.tid.fiware.rss.dao.ServiceDeploymentDao;
import es.tid.fiware.rss.model.BmServiceDeployment;

/**
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:database.xml"})
public class ServiceDeploymentDaoImplTest {

    /**
     * DAO for country.
     */
    @Autowired
    private ServiceDeploymentDao serviceDeploymentDao;

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
     * Test method for {@link
     * es.tid.fiware.rss.dao.impl.ServiceDeploymentDaoImpl#getDeploymentsbyServiceId(java.lang.long)}.
     */
    @Test
    public void testGetDeploymentsbyServiceIdId() {
        // Call method to test
        List<BmServiceDeployment> c = serviceDeploymentDao.getDeploymentsbyServiceId(1);
        // Check result
        Assert.assertNotNull("List<BmServiceDeployment> is null", c);
        Assert.assertNotNull("no result return", c.size() > 0);
        Assert.assertTrue("Number deployments not equeal", c.size() == 7);
        Assert.assertTrue("Service ID not equal", c.get(0).getBmService().getNuServiceId() == 1);
    }

    /**
     * Test method for
     * {@link es.tid.fiware.rss.dao.impl.ServiceDeploymentDaoImpl#getBySvcCountryOB(java.lang.Long,java.lang.Long,java.lang. Long)}
     * .
     */
    @Test
    public void testGetBySvcCountryOB() {
        // Call method to test
        BmServiceDeployment c = serviceDeploymentDao.getBySvcCountryOB(new Long(1), new Long(1), new Long(1));
        // Check result
        Assert.assertNotNull("BmServiceDeployment is null", c);
        Assert.assertTrue("ID not equal", c.getNuDeploymentId() == 1);
        Assert.assertTrue("ID not equal", c.getBmService().getNuServiceId() == 1);
        Assert.assertTrue("ID not equal", c.getBmObCountry().getId().getNuCountryId() == 1);
        Assert.assertTrue("ID not equal", c.getBmObCountry().getId().getNuObId() == 1);
        Assert.assertTrue("ID not equal", c.getTcStatus().equalsIgnoreCase("A"));
    }

}
