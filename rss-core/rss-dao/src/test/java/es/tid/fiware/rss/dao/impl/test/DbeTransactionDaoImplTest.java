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

package es.tid.fiware.rss.dao.impl.test;

import java.util.List;

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
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import es.tid.fiware.rss.common.test.DatabaseLoader;
import es.tid.fiware.rss.dao.impl.DbeTransactionDaoImpl;
import es.tid.fiware.rss.dao.impl.ObMopDaoImpl;
import es.tid.fiware.rss.model.BmMethodsOfPayment;
import es.tid.fiware.rss.model.BmObMop;
import es.tid.fiware.rss.model.BmObMopId;
import es.tid.fiware.rss.model.BmPaymentbroker;
import es.tid.fiware.rss.model.BmService;
import es.tid.fiware.rss.model.DbeTransaction;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:database.xml"})
public class DbeTransactionDaoImplTest {

    /**
     * Variable to print the trace.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DbeTransactionDaoImplTest.class);

    /**
     * DAO for DBETransaction
     */
    @Autowired
    private DbeTransactionDaoImpl dbeTransactionDAO;

    /**
     * DAO for OB-Country relation.
     */
    @Autowired
    private ObMopDaoImpl obMopDAO;

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

    /*
     * @Test public void test50GetNumberOfTransactions() {
     * 
     * 
     * 
     * List<DbeTransaction> listDbeTr = dbeTransactionDAO.getNumberOfTransactions(0,100);
     * Assert.assertTrue("Transaction Id not equal", listDbeTr.get(0).getTxTransactionId().equals("1234"));
     * 
     * }
     */
    /**
     * test60GetTransactionByTxId searchs a inexistent SrvRfCode
     */

    @Test
    @Transactional
    public void test60GetTransactionByTxId() {

        String txId = new String("987");
        try {
            DbeTransaction dbetrBD = dbeTransactionDAO.getTransactionByTxId(txId);

            boolean result;
            if (dbetrBD == null) {
                result = true;
                DbeTransactionDaoImplTest.LOGGER.debug("null object dbe_transaction obtained from database");

            } else {
                result = false;
            }
            Assert.assertTrue("Transaction id does not exist", result);
        } catch (Exception e) { // this is not possible
            String msg = "test60GetTransactionBySvrRfCde:Error in database there are several Transactions for " + txId;
            DbeTransactionDaoImplTest.LOGGER.error(msg);
        }
    }

    /**
     * test65GetTransactionByTxId searchs a inexistent txId
     */

    @Test
    @Transactional
    public void test65GetTransactionByTxId() {

        String txId = new String("111");
        try {
            DbeTransaction dbetr = dbeTransactionDAO.getTransactionByTxId(txId);
            if (dbetr == null) {
                DbeTransactionDaoImplTest.LOGGER.debug("Obtained nothing");
                Assert.assertTrue("Transaction Id not found", true);
            } else {
                DbeTransactionDaoImplTest.LOGGER.debug("Obtained value of transaction");
                Assert.assertTrue("Transaction Id found", false);
            }
        } catch (Exception e) { // this is not possible
            String msg = "test65GetTransactionBySvrRfCde:Error in database there are several Transactions for " + txId;
            DbeTransactionDaoImplTest.LOGGER.error(msg);
            Assert.assertTrue("ERROR in Test ", false);
        }
    }

    // obtained the value of the datatest
    @Test
    @Transactional
    public void testGetTransactionByTxPbCorrelationId() {

        int txTransactionId = 1;
        Integer pbcorrelationid = 0;

        List<DbeTransaction> listDbeTr = dbeTransactionDAO.getTransactionByTxPbCorrelationId(pbcorrelationid);

        if (listDbeTr.size() >= 0) {
            DbeTransactionDaoImplTest.LOGGER.debug("looking result list data....");
            if (listDbeTr.size() == 0) {
                DbeTransactionDaoImplTest.LOGGER.error("Obtained 0 data is not possible with datatest values");
                Assert.assertTrue("0 data obtained ", false);
            } else {
                DbeTransactionDaoImplTest.LOGGER.debug("Obtained:" + listDbeTr.get(0).getTxTransactionId());
                Assert.assertTrue("0 data obtained ", listDbeTr.get(0).getTxTransactionId() == txTransactionId);
            }
        } else {
            DbeTransactionDaoImplTest.LOGGER.error("Obtained nothing is not possible with datatest values");
            Assert.assertTrue("nothing obtained ", false);
        }
    }

    @Test
    @Transactional
    public void testGetTransactionsByProviderId() {
        int txTransactionId = 1;
        String providerId = new String("provider");

        List<DbeTransaction> listDbeTr = dbeTransactionDAO.getTransactionsByProviderId(providerId);

        if (listDbeTr.size() >= 0) {
            DbeTransactionDaoImplTest.LOGGER.debug("looking result list data....");
            if (listDbeTr.size() == 0) {
                DbeTransactionDaoImplTest.LOGGER.error("Obtained 0 data is not possible with datatest values");
                Assert.assertTrue("0 data obtained ", false);
            } else {
                DbeTransactionDaoImplTest.LOGGER.debug("Obtained:" + listDbeTr.get(0).getTxTransactionId());
                Assert.assertTrue("0 data obtained ", listDbeTr.get(0).getTxTransactionId() == txTransactionId);
            }
        } else {
            DbeTransactionDaoImplTest.LOGGER.error("Obtained nothing is not possible with datatest values");
            Assert.assertTrue("nothing obtained ", false);
        }

    }

    @Test
    @Transactional(propagation = Propagation.SUPPORTS)
    public void testDeleteTransactionsByProviderId() {
        int txTransactionId = 1;
        String providerId = new String("provider");
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(Propagation.REQUIRES_NEW.value());
        TransactionStatus status = transactionManager.getTransaction(def);
        dbeTransactionDAO.deleteTransactionsByProviderId(providerId);
        transactionManager.commit(status);
        List<DbeTransaction> listDbeTr = dbeTransactionDAO.getTransactionsByProviderId(providerId);

        if (listDbeTr != null && listDbeTr.size() > 0) {
            DbeTransactionDaoImplTest.LOGGER.debug("looking result list data....");
            DbeTransactionDaoImplTest.LOGGER.error("Obtained:" + listDbeTr.get(0).getTxTransactionId());
            Assert.assertTrue("0 data obtained ", listDbeTr.get(0).getTxTransactionId() == txTransactionId);
        } else {
            DbeTransactionDaoImplTest.LOGGER.debug("Obtained 0 data is not possible with datatest values");
            Assert.assertTrue("0 data obtained ", true);
        }

    }

}
