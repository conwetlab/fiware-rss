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

package es.tid.fiware.rss.expenditureLimit.processing.test;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import es.tid.fiware.rss.common.Constants;
import es.tid.fiware.rss.common.test.DatabaseLoader;
import es.tid.fiware.rss.dao.DbeTransactionDao;
import es.tid.fiware.rss.exception.RSSException;
import es.tid.fiware.rss.exception.UNICAExceptionType;
import es.tid.fiware.rss.expenditureLimit.dao.DbeExpendControlDao;
import es.tid.fiware.rss.expenditureLimit.model.DbeExpendControl;
import es.tid.fiware.rss.expenditureLimit.processing.ProcessingLimitService;
import es.tid.fiware.rss.model.BmCurrency;
import es.tid.fiware.rss.model.BmObCountry;
import es.tid.fiware.rss.model.BmObCountryId;
import es.tid.fiware.rss.model.BmObMop;
import es.tid.fiware.rss.model.BmObMopId;
import es.tid.fiware.rss.model.BmService;
import es.tid.fiware.rss.model.DbeTransaction;

/**
 * 
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:database.xml" })
public class ProcessingLimitServiceTest {
    /**
     * Logging system.
     */
    private static Logger logger = LoggerFactory.getLogger(ProcessingLimitServiceTest.class);
    @Autowired
    private DataSource dataSource;
    @Autowired
    private ProcessingLimitService limitService;
    @Autowired
    private DbeExpendControlDao controlService;
    @Autowired
    private DbeTransactionDao transactionDao;
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
     * Generate Transaction test.
     * 
     * @return transaction
     */
    public static DbeTransaction generateTransaction() {
        DbeTransaction tx = new DbeTransaction();
        tx.setTxTransactionId("transactionsId");
        tx.setTcTransactionType(Constants.CAPTURE_TYPE);
        tx.setTcTransactionStatus(Constants.CAPTURE_STATUS);
        BmService service = new BmService();
        tx.setBmService(service);
        service.setNuServiceId(1);
        BmObCountry obcountry = new BmObCountry();
        BmObCountryId id = new BmObCountryId();
        id.setNuCountryId(1);
        id.setNuObId(1);
        obcountry.setId(id);
        BmCurrency currency = new BmCurrency();
        currency.setNuCurrencyId(1);
        tx.setBmCurrency(currency);
        BmObMop bmObMop = new BmObMop();
        bmObMop.setBmObCountry(obcountry);
        BmObMopId idmop = new BmObMopId();
        idmop.setNuCountryId(1);
        idmop.setNuMopId(1);
        idmop.setNuObId(1);
        bmObMop.setId(idmop);
        tx.setBmObMop(bmObMop);
        tx.setBmClientObCountry(obcountry);
        tx.setTxEndUserId("txEndUserId");
        tx.setTxAppProvider("app123456");
        tx.setFtInternalTotalAmount(new BigDecimal(20));
        // Valores comodin txUserId, appproviderId
        return tx;
    }

    /**
     * Check that the limits are updated.
     */
    @Test
    public void updateControls() {
        try {
            DbeTransaction tx = ProcessingLimitServiceTest.generateTransaction();
            // Set user for testing
            tx.setTxEndUserId("userIdUpdate");
            tx.setFtChargedTotalAmount(new BigDecimal(2));
            List<DbeExpendControl> controls = controlService.getExpendDataForUserAppProvCurrencyObCountry(
                tx.getTxEndUserId(),
                tx.getBmService(), tx.getTxAppProvider(), tx.getBmCurrency(), tx.getBmObMop().getBmObCountry());
            // Reset dates to current date--> if not test fail
            updateDate(controls);
            limitService.updateLimit(tx);
            List<DbeExpendControl> controls2 = controlService.getExpendDataForUserAppProvCurrencyObCountry(
                tx.getTxEndUserId(),
                tx.getBmService(), tx.getTxAppProvider(), tx.getBmCurrency(), tx.getBmObMop().getBmObCountry());
            for (DbeExpendControl control : controls) {
                if (controls2.get(0).getId().getTxElType().
                    equalsIgnoreCase(control.getId().getTxElType())) {
                    BigDecimal total = control.getFtExpensedAmount().add(tx.getFtChargedTotalAmount());
                    Assert.assertTrue(total.compareTo(controls2.get(0).getFtExpensedAmount()) == 0);
                }
            }
        } catch (RSSException e) {
            Assert.fail("Exception not expected" + e.getMessage());
        }
    }

    /**
     * Update control dates
     * 
     * @param controls
     */
    private void updateDate(List<DbeExpendControl> controls) {
        GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_MONTH, 1);
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = transactionManager.getTransaction(def);
        for (DbeExpendControl control : controls) {
            control.setDtNextPeriodStart(cal.getTime());
            controlService.createOrUpdate(control);
        }
        transactionManager.commit(status);

    }

    /**
     * Check that the acummulated is set to 0 and added a negative value (refund)
     */
    @Test
    public void updateResetControls() {
        try {
            DbeTransaction tx = ProcessingLimitServiceTest.generateTransaction();
            // Set user for testing
            tx.setTxEndUserId("userIdUpdate");
            tx.setTcTransactionType(Constants.REFUND_TYPE);
            tx.setFtChargedTotalAmount(new BigDecimal(2));
            List<DbeExpendControl> controls = controlService.getExpendDataForUserAppProvCurrencyObCountry(
                tx.getTxEndUserId(),
                tx.getBmService(), tx.getTxAppProvider(), tx.getBmCurrency(), tx.getBmObMop().getBmObCountry());
            DbeExpendControl control = controls.get(0);
            // Reset period
            Date date = new Date();
            date.setTime((new Date()).getTime() - 100000000);
            control.setDtNextPeriodStart(date);
            DefaultTransactionDefinition def = new DefaultTransactionDefinition();
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
            TransactionStatus status = transactionManager.getTransaction(def);
            controlService.createOrUpdate(control);
            transactionManager.commit(status);
            limitService.updateLimit(tx);
            List<DbeExpendControl> controls2 = controlService.getExpendDataForUserAppProvCurrencyObCountry(
                tx.getTxEndUserId(),
                tx.getBmService(), tx.getTxAppProvider(), tx.getBmCurrency(), tx.getBmObMop().getBmObCountry());
            for (DbeExpendControl controlAux : controls2) {
                if (control.getId().getTxElType().
                    equalsIgnoreCase(controlAux.getId().getTxElType())) {
                    Assert.assertTrue(controlAux.getFtExpensedAmount().compareTo(new BigDecimal(-2)) == 0);
                }
            }

        } catch (RSSException e) {
            Assert.fail("Exception not expected" + e.getMessage());
        }
    }

    /**
     * 
     * Check that not existing control are created.
     */
    @Test
    public void creationControls() {
        try {
            DbeTransaction tx = ProcessingLimitServiceTest.generateTransaction();
            // Set user for testing
            tx.setTxEndUserId("userForCreation");
            List<DbeExpendControl> controls = controlService.getExpendDataForUserAppProvCurrencyObCountry(
                tx.getTxEndUserId(),
                tx.getBmService(), tx.getTxAppProvider(), tx.getBmCurrency(), tx.getBmObMop().getBmObCountry());
            Assert.assertTrue(controls.size() == 0);
            // Update limits.
            limitService.proccesLimit(tx);
            controls = controlService.getExpendDataForUserAppProvCurrencyObCountry(tx.getTxEndUserId(),
                tx.getBmService(), tx.getTxAppProvider(), tx.getBmCurrency(), tx.getBmObMop().getBmObCountry());
            Assert.assertNotNull(controls);
            Assert.assertTrue(controls.size() == 3);
            // All the new control have to be set to 0
            for (DbeExpendControl control : controls) {
                Assert.assertTrue(control.getFtExpensedAmount().compareTo(new BigDecimal(0)) == 0);
            }
            ProcessingLimitServiceTest.logger.debug("Controls:" + controls.size());
        } catch (RSSException e) {
            ProcessingLimitServiceTest.logger.debug("Error: " + e.getMessage());

        }
    }

    /**
     * Update periods and check amounts.
     */
    @Test
    public void checkControls() {
        DbeTransaction tx = ProcessingLimitServiceTest.generateTransaction();
        tx.setTxEndUserId("userIdUpdate");
        try {
            List<DbeExpendControl> controlsBefore = controlService.getExpendDataForUserAppProvCurrencyObCountry(
                tx.getTxEndUserId(),
                tx.getBmService(), tx.getTxAppProvider(), tx.getBmCurrency(), tx.getBmObMop().getBmObCountry());
            Assert.assertNotNull(controlsBefore);
            // Reset dates to current date--> if not test fail
            GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.DAY_OF_MONTH, 1);
            DefaultTransactionDefinition def = new DefaultTransactionDefinition();
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
            TransactionStatus status = transactionManager.getTransaction(def);
            for (DbeExpendControl control : controlsBefore) {
                control.setDtNextPeriodStart(cal.getTime());
                controlService.createOrUpdate(control);
            }
            transactionManager.commit(status);
            limitService.proccesLimit(tx);
            List<DbeExpendControl> controlsAfter = controlService.getExpendDataForUserAppProvCurrencyObCountry(
                tx.getTxEndUserId(),
                tx.getBmService(), tx.getTxAppProvider(), tx.getBmCurrency(), tx.getBmObMop().getBmObCountry());
            ProcessingLimitServiceTest.logger.debug("Controls:" + controlsAfter.size());
            for (DbeExpendControl controlInit : controlsBefore) {
                for (DbeExpendControl controlEnd : controlsAfter) {
                    if (controlInit.getId().getTxElType().equalsIgnoreCase(controlEnd.getId().getTxElType())) {
                        // All the values without modification
                        Assert
                            .assertTrue(controlInit.getFtExpensedAmount().compareTo(controlEnd.getFtExpensedAmount()) == 0);
                        break;
                    }
                }
            }
        } catch (RSSException e) {
            ProcessingLimitServiceTest.logger.debug("Error: " + e.getMessage());
            Assert.fail("Exception not expected");
        }
        // check error
        try {
            tx.setFtChargedTotalAmount(null);
            tx.setFtInternalTotalAmount(new BigDecimal(1000));
            limitService.proccesLimit(tx);
            Assert.fail("Exception expected");
        } catch (RSSException e) {
            ProcessingLimitServiceTest.logger.debug("Exception received: " + e.getMessage());
            // "SVC3705",
            Assert.assertTrue(e.getMessage().contains("Insufficient payment method balance"));
        }
        // check that
        try {
            tx.setFtChargedTotalAmount(null);
            tx.setFtInternalTotalAmount(new BigDecimal(30));
            List<DbeExpendControl> controlsBefore = controlService.getExpendDataForUserAppProvCurrencyObCountry(
                tx.getTxEndUserId(),
                tx.getBmService(), tx.getTxAppProvider(), tx.getBmCurrency(), tx.getBmObMop().getBmObCountry());
            // Reset period
            DbeExpendControl control = controlsBefore.get(0);
            GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.MONTH, -1);
            control.setDtNextPeriodStart(cal.getTime());
            DefaultTransactionDefinition def = new DefaultTransactionDefinition();
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
            TransactionStatus status = transactionManager.getTransaction(def);
            controlService.update(control);
            transactionManager.commit(status);
            limitService.proccesLimit(tx);
            List<DbeExpendControl> controlsAfter = controlService.getExpendDataForUserAppProvCurrencyObCountry(
                tx.getTxEndUserId(),
                tx.getBmService(), tx.getTxAppProvider(), tx.getBmCurrency(), tx.getBmObMop().getBmObCountry());
            boolean finded = false;
            for (DbeExpendControl checkControl : controlsAfter) {
                if (checkControl.getFtExpensedAmount().compareTo(new BigDecimal(0)) == 0) {
                    finded = true;
                    break;
                }
            }
            // reset control found
            Assert.assertTrue(finded);
        } catch (RSSException e) {
            ProcessingLimitServiceTest.logger.debug("Exception received: " + e.getMessage());
            Assert.fail("Exception expected");
        }
    }

    /**
     * Verifies that the limit perTransaction is applied correctly
     */
    @Test
    public void perTransactionLimit() {
        DbeTransaction tx = ProcessingLimitServiceTest.generateTransaction();
        tx.setTxEndUserId("userId01");
        tx.setTxAppProvider("123456");
        tx.setFtInternalTotalAmount(new BigDecimal("60"));
        try {
            limitService.proccesLimit(tx);
            Assert.fail("Limit surpassed");
        } catch (RSSException e) {
            if (e.getExceptionType().getExceptionId() == UNICAExceptionType.INSUFFICIENT_MOP_BALANCE.getExceptionId()) {
                Assert.assertTrue("Limit Exceeded", true);
            } else {
                Assert.fail("Exception unexpected");
            }
        }

        tx = ProcessingLimitServiceTest.generateTransaction();
        tx.setTxEndUserId("userId01");
        tx.setTxAppProvider("123456");
        try {
            limitService.proccesLimit(tx);
            Assert.assertTrue("Limit passed", true);
        } catch (Exception e) {
            Assert.fail("Exception expected");
        }
    }
}
