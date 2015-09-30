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

package es.upm.fiware.rss.expenditureLimit.processing.test;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import es.upm.fiware.rss.common.Constants;
import es.upm.fiware.rss.common.test.DatabaseLoader;
import es.upm.fiware.rss.exception.RSSException;
import es.upm.fiware.rss.exception.UNICAExceptionType;
import es.upm.fiware.rss.expenditureLimit.dao.DbeExpendControlDao;
import es.upm.fiware.rss.expenditureLimit.model.DbeExpendControl;
import es.upm.fiware.rss.expenditureLimit.processing.ProcessingLimitService;
import es.upm.fiware.rss.model.BmCurrency;
import es.upm.fiware.rss.model.DbeAggregator;
import es.upm.fiware.rss.model.DbeAppProvider;
import es.upm.fiware.rss.model.DbeAppProviderId;
import es.upm.fiware.rss.model.DbeTransaction;
import java.net.URL;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.springframework.beans.factory.annotation.Value;

/**
 * 
 * 
 */
@ContextConfiguration({"classpath:database.xml"})
public class ProcessingLimitServiceTest {
    /**
     * Logging system.
     */
    private static Logger logger = LoggerFactory.getLogger(ProcessingLimitServiceTest.class);

    private @Value("${database.test.schema}") String schema;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ProcessingLimitService limitService;

    @Autowired
    private DbeExpendControlDao controlService;

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
        FlatXmlDataSetBuilder loader = new FlatXmlDataSetBuilder();
        IDatabaseConnection dbConn = new DatabaseConnection(DataSourceUtils.getConnection(dataSource), this.schema);
        URL url = this.getClass().getClassLoader().getResource("dbunit/CREATE_DATATEST_EXPLIMIT.xml");
        IDataSet ds = loader.build(url);
        DatabaseOperation.DELETE_ALL.execute(dbConn, ds);
        dbConn.getConnection().commit();
    }

    /**
     * Generate Transaction test.
     * 
     * @return transaction
     */
    public static DbeTransaction generateTransaction() {
        DbeTransaction tx = new DbeTransaction();
        tx.setTcTransactionType(Constants.CHARGE_TYPE);

        BmCurrency currency = new BmCurrency();
        currency.setNuCurrencyId(1);
        tx.setBmCurrency(currency);

        DbeAggregator ag = new DbeAggregator();
        ag.setTxEmail("test@email.com");
        ag.setTxName("test");

        DbeAppProviderId pid = new DbeAppProviderId();
        pid.setAggregator(ag);
        pid.setTxAppProviderId("app123456");

        DbeAppProvider provider = new DbeAppProvider();
        provider.setId(pid);

        tx.setTxEndUserId("txEndUserId");
        tx.setAppProvider(provider);

        tx.setFtChargedAmount(new BigDecimal(20));

        return tx;
    }

    /**
     * 
     * @param tx
     * @return
     */
    private List<DbeExpendControl> getExpenditureControls(DbeTransaction tx) {
    	return controlService.getExpendDataForUserAppProvCurrency(
                tx.getTxEndUserId(),
                tx.getAppProvider().getId().getAggregator().getTxEmail(),
                tx.getAppProvider().getId().getTxAppProviderId(),
                tx.getBmCurrency());
    }

    /**
     * Check that the limits are updated.
     */
    @Transactional
    public void updateControls() {
    	try {
        	ProcessingLimitServiceTest.logger.debug("==== Update Controls ====");

            DbeTransaction tx = ProcessingLimitServiceTest.generateTransaction();
            // Set user for testing
            tx.setTxEndUserId("userIdUpdate");
            tx.setFtChargedAmount(new BigDecimal(2));

            List<DbeExpendControl> controls = this.getExpenditureControls(tx);

            // Save expexted expenditure limits
            Map<String, BigDecimal> expectedLimits = new HashMap<>();

            for (DbeExpendControl control: controls) {
                expectedLimits.put(control.getId().getTxElType(), control.getFtExpensedAmount().add(tx.getFtChargedAmount()));
            }

            // Reset dates to current date--> in other case the test fails
            updateDate(controls);
            limitService.updateLimit(tx);

            List<DbeExpendControl> controls2 = this.getExpenditureControls(tx);

            for (DbeExpendControl control : controls2) {
                BigDecimal amount = control.getFtExpensedAmount();
                Assert.assertTrue(expectedLimits.get(control.getId().getTxElType()).compareTo(amount) == 0);
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
    @Transactional
    public void updateResetControls() {
    	try {
            DbeTransaction tx = ProcessingLimitServiceTest.generateTransaction();
            // Set user for testing
            tx.setTxEndUserId("userIdUpdate");
            tx.setTcTransactionType(Constants.REFUND_TYPE);
            tx.setFtChargedAmount(new BigDecimal(2));

            List<DbeExpendControl> controls = this.getExpenditureControls(tx);

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
            List<DbeExpendControl> controls2 = this.getExpenditureControls(tx);

            for (DbeExpendControl controlAux : controls2) {
                if (control.getId().getTxElType().
                    equalsIgnoreCase(controlAux.getId().getTxElType())) {
                    Assert.assertTrue("Expensed amount: " + controlAux.getFtExpensedAmount(), controlAux.getFtExpensedAmount().compareTo(new BigDecimal(-2)) == 0);
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
    @Transactional(propagation = Propagation.SUPPORTS)
    public void creationControls() {
        try {
            DbeTransaction tx = ProcessingLimitServiceTest.generateTransaction();
            // Set user for testing
            tx.setTxEndUserId("userForCreation");

            List<DbeExpendControl> controls = controlService.getExpendDataForUserAppProvCurrency(
                    tx.getTxEndUserId(),
                    tx.getAppProvider().getId().getAggregator().getTxEmail(),
                    tx.getAppProvider().getId().getTxAppProviderId(),
                    tx.getBmCurrency());

            Assert.assertTrue(controls.isEmpty());

            // Update limits.
            DefaultTransactionDefinition def = new DefaultTransactionDefinition();
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
            TransactionStatus status = transactionManager.getTransaction(def);
            limitService.proccesLimit(tx);
            transactionManager.commit(status);

            controls = controlService.getExpendDataForUserAppProvCurrency(
                    tx.getTxEndUserId(),
                    tx.getAppProvider().getId().getAggregator().getTxEmail(),
                    tx.getAppProvider().getId().getTxAppProviderId(),
                    tx.getBmCurrency());

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
    @Transactional(propagation = Propagation.SUPPORTS)
    public void checkControls() {
        DbeTransaction tx = ProcessingLimitServiceTest.generateTransaction();
        tx.setTxEndUserId("userIdUpdate");
        try {
            List<DbeExpendControl> controlsBefore = controlService.getExpendDataForUserAppProvCurrency(
                    tx.getTxEndUserId(),
                    tx.getAppProvider().getId().getAggregator().getTxEmail(),
                    tx.getAppProvider().getId().getTxAppProviderId(),
                    tx.getBmCurrency());

            Assert.assertNotNull(controlsBefore);
            // Reset dates to current date--> if not test fail
            GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.DAY_OF_MONTH, 1);

            for (DbeExpendControl control : controlsBefore) {
                control.setDtNextPeriodStart(cal.getTime());
                controlService.createOrUpdate(control);
            }

            limitService.proccesLimit(tx);
            List<DbeExpendControl> controlsAfter = controlService.getExpendDataForUserAppProvCurrency(
                    tx.getTxEndUserId(),
                    tx.getAppProvider().getId().getAggregator().getTxEmail(),
                    tx.getAppProvider().getId().getTxAppProviderId(),
                    tx.getBmCurrency());

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
            tx.setFtChargedAmount(new BigDecimal(1000));
            limitService.proccesLimit(tx);
            Assert.fail("Exception expected");
        } catch (RSSException e) {
            ProcessingLimitServiceTest.logger.debug("Exception received: " + e.getMessage());
            // "SVC3705",
            Assert.assertTrue(e.getMessage().contains("Insufficient payment method balance"));
        }

        // check that
        try {
            tx.setFtChargedAmount(new BigDecimal(30));
            List<DbeExpendControl> controlsBefore = controlService.getExpendDataForUserAppProvCurrency(
                    tx.getTxEndUserId(),
                    tx.getAppProvider().getId().getAggregator().getTxEmail(),
                    tx.getAppProvider().getId().getTxAppProviderId(),
                    tx.getBmCurrency());

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

            List<DbeExpendControl> controlsAfter = controlService.getExpendDataForUserAppProvCurrency(
                    tx.getTxEndUserId(),
                    tx.getAppProvider().getId().getAggregator().getTxEmail(),
                    tx.getAppProvider().getId().getTxAppProviderId(),
                    tx.getBmCurrency());

            boolean found = false;
            for (DbeExpendControl checkControl : controlsAfter) {
                if (checkControl.getFtExpensedAmount().compareTo(new BigDecimal(0)) == 0) {
                    found = true;
                    break;
                }
            }
            // reset control found
            Assert.assertTrue(found);
        } catch (RSSException e) {
            ProcessingLimitServiceTest.logger.debug("Exception received: " + e.getMessage());
            Assert.fail("Exception expected");
        }
    }

    /**
     * Verifies that the limit perTransaction is applied correctly
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public void perTransactionLimit() {
        DbeTransaction tx = ProcessingLimitServiceTest.generateTransaction();
        tx.setTxEndUserId("userId01");

        DbeAggregator ag = new DbeAggregator();
        ag.setTxEmail("test@email.com");
        ag.setTxName("test");

        DbeAppProviderId pid = new DbeAppProviderId();
        pid.setAggregator(ag);
        pid.setTxAppProviderId("123456");

        DbeAppProvider provider = new DbeAppProvider();
        provider.setId(pid);
        tx.setAppProvider(provider);

        tx.setFtChargedAmount(new BigDecimal("60"));

        try {
            limitService.proccesLimit(tx);
            Assert.fail("Limit surpassed");
        } catch (RSSException e) {
            if (e.getExceptionType().getExceptionId().
                    equals(UNICAExceptionType.INSUFFICIENT_MOP_BALANCE.getExceptionId())) {
                Assert.assertTrue("Limit Exceeded", true);
            } else {
                Assert.fail("Exception unexpected");
            }
        }

        tx = ProcessingLimitServiceTest.generateTransaction();
        tx.setTxEndUserId("userId01");
        tx.setAppProvider(provider);

        try {
            limitService.proccesLimit(tx);
            Assert.assertTrue("Limit passed", true);
        } catch (Exception e) {
            Assert.fail("Exception not expected " + e.getMessage());
        }
    }
}
