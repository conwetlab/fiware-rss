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

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    @Test
    @Transactional
    public void test20GetTransactionBySvcPrdtUserDate() {
        String txTransactionId = new String("1234");

        Long nuServiceId = new Long(1);
        String user = new String("tel:+34913329702");

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd:hhmmss");
        String sdate = "2012-01-01:000000";
        // String sdate ="2012-01-01:000020"; it fail is seconds in database is 00
        Date date = null;
        try {

            date = formatter.parse(sdate);

        } catch (ParseException ex) {

            ex.printStackTrace();
            Assert.fail("ParseException received: " + ex.getMessage());
        }

        List<DbeTransaction> listDbeTr = dbeTransactionDAO.getTransactionBySvcPrdtUserDate(nuServiceId, null, user,
            date);
        if (listDbeTr.size() >= 0) {
            DbeTransactionDaoImplTest.LOGGER.debug("looking result list data....");
            if (listDbeTr.size() == 0) {
                DbeTransactionDaoImplTest.LOGGER.debug("Obtained 0 data");
            } else {
                DbeTransactionDaoImplTest.LOGGER.debug("Obtained:" + listDbeTr.get(0).getTxTransactionId());
            }
        } else {
            DbeTransactionDaoImplTest.LOGGER.debug("Obtained nothing");

        }
        Assert.assertTrue("Transaction Id not equal", listDbeTr.get(0).getTxTransactionId().equals(txTransactionId));

    }

    /**
     * tests the getLimitedTxBySvcPrdtUserDateOrderByDate
     */
    @Test
    @Transactional
    public void test25getLimitedTxBySvcRefcPrdtUserDateOrderByDate() {
        String txTransactionId = new String("1234");

        Long nuServiceId = new Long(1);
        String user = new String("tel:+34913329702");

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd:hhmmss");
        String sdate = "2012-01-01:000000";
        // String sdate ="2012-01-01:000020"; it fail is seconds in database is 00
        Date date = null;
        try {
            date = formatter.parse(sdate);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        List<DbeTransaction> listDbeTr = dbeTransactionDAO.getLimitedTxBySvcRefcPrdtUserDateOrderByDate(null,
            nuServiceId, null, null, null, user, date, null, 0, 50, null, null, null, null, null, null);
        boolean found = false;
        if (listDbeTr.size() >= 0) {
            for (DbeTransaction tx : listDbeTr) {
                if (tx.getTxTransactionId().equals(txTransactionId)) {
                    found = true;
                    break;
                }
            }
            DbeTransactionDaoImplTest.LOGGER.debug("looking result list data....");
            if (listDbeTr.size() == 0) {
                DbeTransactionDaoImplTest.LOGGER.debug("Obtained 0 data");
            } else {
                DbeTransactionDaoImplTest.LOGGER.debug("Obtained {} data rows." + listDbeTr.size());
            }
        } else {
            DbeTransactionDaoImplTest.LOGGER.debug("Obtained nothing");

        }
        Assert.assertTrue("Transaction Id not equal", found);

    }

    @Test
    @Transactional
    public void test30Create() {
        // to see what data we have in database
        // try{
        // Thread.sleep(60000);
        // }catch (Exception e){}

        DbeTransaction dbetr = new DbeTransaction();
        Date date = new Date();
        dbetr.setTsRequest(date);
        dbetr.setTxEndUserId("91334000000");
        dbetr.setTcTransactionType("C");
        dbetr.setTcTransactionStatus("A");
        dbetr.setTsStatusDate(new Date());
        dbetr.setTxReferenceCode("120");
        BigDecimal amount = new BigDecimal(30);
        dbetr.setFtRequestAmount(amount);
        BmService bmService = new BmService();
        bmService.setNuServiceId(1);
        dbetr.setBmService(bmService);
        dbetr.setTxRequestAmountDesc("test create without TxId");
        String nuGlobalUserId = "34000000";
        dbetr.setTxGlobalUserId(nuGlobalUserId);
        dbetr.setTxApplicationId("appId");

        // @@@@BmObMopId
        BmObMopId bmObMopId = new BmObMopId();
        bmObMopId.setNuCountryId(1);
        bmObMopId.setNuMopId(1);
        bmObMopId.setNuObId(1);

        // @@@@BmObMop
        BmObMop bmObMop = new BmObMop();
        // We obtained it from database to assure that it exists
        bmObMop = obMopDAO.getById(bmObMopId);

        // BmMethods...
        BmMethodsOfPayment bmMethodsOfPayment = new BmMethodsOfPayment(1, "Visa", "Code1");
        // ----setting part of BmObMop
        bmObMop.setBmMethodsOfPayment(bmMethodsOfPayment);
        // ---it is settled bmObMop

        // @@@@BmPaymentbroker
        BmPaymentbroker bmPaymentbroker = bmObMop.getBmPaymentbroker();

        dbetr.setBmObMop(bmObMop);
        dbetr.setBmPaymentbroker(bmPaymentbroker);
        dbetr.setBmMethodsOfPayment(bmMethodsOfPayment);

        dbeTransactionDAO.create(dbetr);
        DbeTransactionDaoImplTest.LOGGER.debug("Value of end user id:" + dbetr.getTxEndUserId());
        DbeTransactionDaoImplTest.LOGGER.debug("Value of tx id:" + dbetr.getTxTransactionId());
        DbeTransactionDaoImplTest.LOGGER.debug("Value of partition:" + dbetr.getTxPartition());

        // I don't know de tx id at first time
        Assert.assertTrue("End user id not equal", "91334000000".equals(dbetr.getTxEndUserId()));
        // to see: Asser
    }

    /**
     * test35Create creates the transaction in the database. we set transactionId with uuid class
     */

    @Test
    @Transactional
    public void test35Create() {
        // to see what data we have in database
        // try{
        // Thread.sleep(60000);
        // }catch
        // (Exception e){}
        String transactionId = java.util.UUID.randomUUID().toString();

        DbeTransaction dbetr = new DbeTransaction();
        dbetr.setTxTransactionId(transactionId);
        Date date = new Date();
        dbetr.setTsRequest(date);
        dbetr.setTxEndUserId("91334000000");
        dbetr.setTcTransactionType("C");
        dbetr.setTcTransactionStatus("A");
        dbetr.setTsStatusDate(new Date());
        dbetr.setTxReferenceCode("120");
        BigDecimal amount = new BigDecimal(30);
        dbetr.setFtRequestAmount(amount);
        BmService bmService = new BmService();
        bmService.setNuServiceId(1);
        dbetr.setBmService(bmService);
        dbetr.setTxRequestAmountDesc("test create Tx is settled UUID");
        String nuGlobalUserId = "34000000";
        dbetr.setTxGlobalUserId(nuGlobalUserId);
        dbetr.setTxApplicationId("appId");

        // @@@@BmObMopId
        BmObMopId bmObMopId = new BmObMopId();
        bmObMopId.setNuCountryId(1);
        bmObMopId.setNuMopId(1);
        bmObMopId.setNuObId(1);

        // @@@@BmObMop
        BmObMop bmObMop = new BmObMop();
        // We obtained it from database to assure that it exists
        bmObMop = obMopDAO.getById(bmObMopId);

        // BmMethods...
        BmMethodsOfPayment bmMethodsOfPayment = new BmMethodsOfPayment(1, "Visa", "Code1");
        // ----setting part of BmObMop
        bmObMop.setBmMethodsOfPayment(bmMethodsOfPayment);
        // ---it is settled bmObMop

        // @@@@BmPaymentbroker
        BmPaymentbroker bmPaymentbroker = bmObMop.getBmPaymentbroker();

        dbetr.setBmObMop(bmObMop);
        dbetr.setBmPaymentbroker(bmPaymentbroker);
        dbetr.setBmMethodsOfPayment(bmMethodsOfPayment);

        dbeTransactionDAO.create(dbetr);
        DbeTransactionDaoImplTest.LOGGER.debug("Value of end user id:" + dbetr.getTxEndUserId());
        DbeTransactionDaoImplTest.LOGGER.debug("Value of tx id:" + dbetr.getTxTransactionId());
        DbeTransactionDaoImplTest.LOGGER.debug("Value of partition:" + dbetr.getTxPartition());

        // I don't know de tx id at first time
        Assert.assertTrue("End user id not equal", "91334000000".equals(dbetr.getTxEndUserId())); // to see: Asser
    }

    /**
     * test35Create creates the transaction in the database. we set transactionId not with uuid class
     */
    @Test
    @Transactional
    public void test37Create() {
        // to see what data we have in database
        // try{
        // Thread.sleep(60000);
        // }catch
        // (Exception e){}
        String transactionId = new String("2");

        DbeTransaction dbetr = new DbeTransaction();
        dbetr.setTxTransactionId(transactionId);
        Date date = new Date();
        dbetr.setTsRequest(date);
        dbetr.setTxEndUserId("91334000000");
        dbetr.setTcTransactionType("C");
        dbetr.setTcTransactionStatus("A");
        dbetr.setTsStatusDate(new Date());
        dbetr.setTxReferenceCode("120");
        BigDecimal amount = new BigDecimal(30);
        dbetr.setFtRequestAmount(amount);
        BmService bmService = new BmService();
        bmService.setNuServiceId(1);
        dbetr.setBmService(bmService);
        dbetr.setTxRequestAmountDesc("test create Tx settled with error size");
        String nuGlobalUserId = "34000000";
        dbetr.setTxGlobalUserId(nuGlobalUserId);
        dbetr.setTxApplicationId("appId");

        // @@@@BmObMopId
        BmObMopId bmObMopId = new BmObMopId();
        bmObMopId.setNuCountryId(1);
        bmObMopId.setNuMopId(1);
        bmObMopId.setNuObId(1);

        // @@@@BmObMop
        BmObMop bmObMop = new BmObMop();
        // We obtained it from database to assure that it exists
        bmObMop = obMopDAO.getById(bmObMopId);

        // BmMethods...
        BmMethodsOfPayment bmMethodsOfPayment = new BmMethodsOfPayment(1, "Visa", "Code1");
        // ----setting part of BmObMop
        bmObMop.setBmMethodsOfPayment(bmMethodsOfPayment);
        // ---it is settled bmObMop

        // @@@@BmPaymentbroker
        BmPaymentbroker bmPaymentbroker = bmObMop.getBmPaymentbroker();

        dbetr.setBmObMop(bmObMop);
        dbetr.setBmPaymentbroker(bmPaymentbroker);
        dbetr.setBmMethodsOfPayment(bmMethodsOfPayment);

        dbeTransactionDAO.create(dbetr);
        DbeTransactionDaoImplTest.LOGGER.debug("Value of end user id:" + dbetr.getTxEndUserId());
        DbeTransactionDaoImplTest.LOGGER.debug("Value of tx id:" + dbetr.getTxTransactionId());
        DbeTransactionDaoImplTest.LOGGER.debug("Value of partition:" + dbetr.getTxPartition());

        // I don't know de tx id at first time
        Assert.assertTrue("End user id not equal", "91334000000".equals(dbetr.getTxEndUserId())); // to see: Asser
    }

    @Test
    @Transactional
    public void test40CreateOrUpdate() {
        // to see what data we have in database
        // try{
        // Thread.sleep(60000);
        // }catch
        // (Exception e){}

        DbeTransaction dbetr = new DbeTransaction();
        Date date = new Date();
        dbetr.setTsRequest(date);
        dbetr.setTxEndUserId("91334000000");
        dbetr.setTcTransactionType("C");
        dbetr.setTcTransactionStatus("A");
        dbetr.setTsStatusDate(new Date());
        dbetr.setTxReferenceCode("120");
        BigDecimal amount = new BigDecimal(30);
        dbetr.setFtRequestAmount(amount);
        BmService bmService = new BmService();
        bmService.setNuServiceId(1);
        dbetr.setBmService(bmService);
        dbetr.setTxRequestAmountDesc("test createorupdate");
        String nuGlobalUserId = "34000000";
        dbetr.setTxGlobalUserId(nuGlobalUserId);
        dbetr.setTxApplicationId("appId");

        // @@@@BmObMopId
        BmObMopId bmObMopId = new BmObMopId();
        bmObMopId.setNuCountryId(1);
        bmObMopId.setNuMopId(1);
        bmObMopId.setNuObId(1);

        // @@@@BmObMop
        BmObMop bmObMop = new BmObMop();
        // We obtained it from database to assure that it exists
        bmObMop = obMopDAO.getById(bmObMopId);

        // BmMethods...
        BmMethodsOfPayment bmMethodsOfPayment = new BmMethodsOfPayment(1, "Visa", "Code1");
        // ----setting part of BmObMop
        bmObMop.setBmMethodsOfPayment(bmMethodsOfPayment);
        // ---it is settled bmObMop

        // @@@@BmPaymentbroker
        BmPaymentbroker bmPaymentbroker = bmObMop.getBmPaymentbroker();

        dbetr.setBmObMop(bmObMop);
        dbetr.setBmPaymentbroker(bmPaymentbroker);
        dbetr.setBmMethodsOfPayment(bmMethodsOfPayment);

        dbeTransactionDAO.createOrUpdate(dbetr);
        DbeTransactionDaoImplTest.LOGGER.debug("**************Value of end user id:" + dbetr.getTxEndUserId());
        DbeTransactionDaoImplTest.LOGGER.debug("**************Value of tx id:" + dbetr.getTxTransactionId());
        DbeTransactionDaoImplTest.LOGGER.debug("************** Value of partition:" + dbetr.getTxPartition());

        // I don't know de tx id at first time
        Assert.assertTrue("End user id not equal", "91334000000".equals(dbetr.getTxEndUserId())); // to see: Asser
        dbetr.setTxEndUserId("UserTest");
        dbeTransactionDAO.createOrUpdate(dbetr);
        DbeTransactionDaoImplTest.LOGGER.debug("**************Value of end user id:" + dbetr.getTxEndUserId());
        DbeTransactionDaoImplTest.LOGGER.debug("**************Value of tx id:" + dbetr.getTxTransactionId());
        DbeTransactionDaoImplTest.LOGGER.debug("************** Value of partition:" + dbetr.getTxPartition());

        // I don't know de tx id at first time
        Assert.assertTrue("End user id not equal", "UserTest".equals(dbetr.getTxEndUserId())); // to see: Asser

        // I don't know de tx id at first time
        dbetr.setTxTransactionId("1");
        dbeTransactionDAO.createOrUpdate(dbetr);
        Assert.assertTrue("Equals TxId", !dbetr.getTxTransactionId().equalsIgnoreCase("1")); // to see: Asser

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

    // obtained nothing with datatest values
    @Test
    @Transactional
    public void test70GetTransactionByOrgSvrRfCde() {

        String orgSrvRefCode = new String("987");
        // there is no data in datatest for this value

        List<DbeTransaction> listDbeTr = dbeTransactionDAO.getTransactionByOrgSvrRfCde(orgSrvRefCode);
        boolean result;
        /*
         * if (listDbeTr == null) { result = true;
         * LOGGER.debug("null list of objects  of  dbe_transaction obtained from database");
         * 
         * } else { result = false; }
         */
        // the getTransactionByOrgSvrRfCde returns a list empty not null
        if (listDbeTr.size() == 0) {
            result = true;
            DbeTransactionDaoImplTest.LOGGER.debug("empty list of objects  of  dbe_transaction obtained from database");
        } else {
            result = false;
        }

        Assert.assertTrue(" Fake test: Transaction id does not exist", result);

    }

    // obtained the value of the datatest
    @Test
    @Transactional
    public void test75GetTransactionByOrgSvrRfCde() {

        String txTransactionId = new String("1234");
        String orgSrvRefCode = new String("111");

        List<DbeTransaction> listDbeTr = dbeTransactionDAO.getTransactionByOrgSvrRfCde(orgSrvRefCode);

        if (listDbeTr.size() >= 0) {
            DbeTransactionDaoImplTest.LOGGER.debug("looking result list data....");
            if (listDbeTr.size() == 0) {
                DbeTransactionDaoImplTest.LOGGER.error("Obtained 0 data is not possible with datatest values");
                Assert.assertTrue("0 data obtained ", false);
            } else {
                DbeTransactionDaoImplTest.LOGGER.debug("Obtained:" + listDbeTr.get(0).getTxTransactionId());
                Assert.assertTrue("0 data obtained ", (listDbeTr.get(0).getTxTransactionId()).equals(txTransactionId));
            }
        } else {
            DbeTransactionDaoImplTest.LOGGER.error("Obtained nothing is not possible with datatest values");
            Assert.assertTrue("nothing obtained ", false);
        }

    }

    /**
     * 
     */
    @Test
    @Transactional
    public void testGetTransactionByTxIdWithoutLazy() {

        String txId = new String("1234");
        try {
            DbeTransaction dbetr = dbeTransactionDAO.getTransactionByTxIdWithoutLazy(txId);
            if (dbetr == null) {
                DbeTransactionDaoImplTest.LOGGER.debug("Obtained nothing");
                Assert.assertTrue("Transaction Id not found", false);
            } else {
                DbeTransactionDaoImplTest.LOGGER.debug("Obtained value of transaction");
                Assert.assertTrue("Transaction Id found", true);
            }
        } catch (Exception e) { // this is not possible
            String msg = "test65GetTransactionBySvrRfCde:Error in database there are several Transactions for " + txId;
            DbeTransactionDaoImplTest.LOGGER.error(msg);
            Assert.assertTrue("ERROR in Test ", false);
        }
    }

    /**
     * 
     */
    @Test
    @Transactional
    public void testGetTransactionByRfCdeSvc() {

        String txReferenceCode = new String("100");
        String applicationId = new String("1");
        long nuServiceId = 1;
        try {
            DbeTransaction dbetr = dbeTransactionDAO.getTransactionByRfCdeSvc(nuServiceId, txReferenceCode,
                applicationId);
            if (dbetr == null) {
                DbeTransactionDaoImplTest.LOGGER.debug("value of transaction not Obtained");
                Assert.assertTrue("Transaction Id not found", false);
            } else {
                DbeTransactionDaoImplTest.LOGGER.debug("Obtained nothing");
                Assert.assertTrue("Transaction Id not found", dbetr.getTxTransactionId().equalsIgnoreCase("1234"));
            }
        } catch (Exception e) { // this is not possible
            String msg = "testGetTransactionByRfCdeSvc: Error in database";
            DbeTransactionDaoImplTest.LOGGER.error(msg);
            Assert.assertTrue("ERROR in Test ", false);
        }
    }

    // obtained the value of the datatest
    @Test
    @Transactional
    public void testGetTransactionByTxPbCorrelationId() {

        String txTransactionId = new String("1234");
        String pbcorrelationid = new String("null");

        List<DbeTransaction> listDbeTr = dbeTransactionDAO.getTransactionByTxPbCorrelationId(pbcorrelationid);

        if (listDbeTr.size() >= 0) {
            DbeTransactionDaoImplTest.LOGGER.debug("looking result list data....");
            if (listDbeTr.size() == 0) {
                DbeTransactionDaoImplTest.LOGGER.error("Obtained 0 data is not possible with datatest values");
                Assert.assertTrue("0 data obtained ", false);
            } else {
                DbeTransactionDaoImplTest.LOGGER.debug("Obtained:" + listDbeTr.get(0).getTxTransactionId());
                Assert.assertTrue("0 data obtained ", (listDbeTr.get(0).getTxTransactionId()).equals(txTransactionId));
            }
        } else {
            DbeTransactionDaoImplTest.LOGGER.error("Obtained nothing is not possible with datatest values");
            Assert.assertTrue("nothing obtained ", false);
        }
    }

    @Test
    @Transactional
    public void testGetTransactionsByProviderId() {
        String txTransactionId = new String("1234");
        String providerId = new String("provider");

        List<DbeTransaction> listDbeTr = dbeTransactionDAO.getTransactionsByProviderId(providerId);

        if (listDbeTr.size() >= 0) {
            DbeTransactionDaoImplTest.LOGGER.debug("looking result list data....");
            if (listDbeTr.size() == 0) {
                DbeTransactionDaoImplTest.LOGGER.error("Obtained 0 data is not possible with datatest values");
                Assert.assertTrue("0 data obtained ", false);
            } else {
                DbeTransactionDaoImplTest.LOGGER.debug("Obtained:" + listDbeTr.get(0).getTxTransactionId());
                Assert.assertTrue("0 data obtained ", (listDbeTr.get(0).getTxTransactionId()).equals(txTransactionId));
            }
        } else {
            DbeTransactionDaoImplTest.LOGGER.error("Obtained nothing is not possible with datatest values");
            Assert.assertTrue("nothing obtained ", false);
        }

    }

    @Test
    @Transactional(propagation = Propagation.SUPPORTS)
    public void testDeleteTransactionsByProviderId() {
        String txTransactionId = new String("1234");
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
            Assert.assertTrue("0 data obtained ", (listDbeTr.get(0).getTxTransactionId()).equals(txTransactionId));
        } else {
            DbeTransactionDaoImplTest.LOGGER.debug("Obtained 0 data is not possible with datatest values");
            Assert.assertTrue("0 data obtained ", true);
        }

    }

    @Test
    @Transactional
    public void testGetNumTxBySvcRefcPrdtUserDate() {
        String txTransactionId = new String("1234");
        Long number = dbeTransactionDAO.getNumTxBySvcRefcPrdtUserDate(txTransactionId, null, null, null, null, null,
            null, null, null, null, null, null, null, null);
        Assert.assertTrue("0 data obtained ", number == 1);
    }

}
