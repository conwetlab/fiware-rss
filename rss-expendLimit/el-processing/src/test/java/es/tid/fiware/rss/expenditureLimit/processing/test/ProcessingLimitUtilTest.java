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

package es.tid.fiware.rss.expenditureLimit.processing.test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import es.tid.fiware.rss.common.Constants;
import es.tid.fiware.rss.common.test.DatabaseLoader;
import es.tid.fiware.rss.exception.RSSException;
import es.tid.fiware.rss.exception.UNICAExceptionType;
import es.tid.fiware.rss.expenditureLimit.model.DbeExpendControl;
import es.tid.fiware.rss.expenditureLimit.model.DbeExpendLimit;
import es.tid.fiware.rss.expenditureLimit.model.DbeExpendLimitPK;
import es.tid.fiware.rss.expenditureLimit.processing.ProcessingLimitService;
import es.tid.fiware.rss.expenditureLimit.processing.ProcessingLimitUtil;
import es.tid.fiware.rss.model.DbeAppProvider;
import es.tid.fiware.rss.model.DbeTransaction;

/**
 * 
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:database.xml" })
public class ProcessingLimitUtilTest {
    /**
     * Logging system.
     */
    private static Logger logger = LoggerFactory.getLogger(ProcessingLimitUtilTest.class);

    @Autowired
    private DatabaseLoader databaseLoader;

    /**
     * Object used to test the class ProcessingLimitUtil.
     */
    ProcessingLimitUtil utils;

    /**
     * Method to insert data before test.
     * 
     * @throws Exception
     *             from db
     */
    @Before
    public void setUp() throws Exception {
        databaseLoader.cleanInsert("dbunit/CREATE_DATATEST_EXPLIMIT.xml", true);
        utils = new ProcessingLimitUtil();
    }

    @After
    public void tearDown() throws Exception {
        databaseLoader.deleteAll("dbunit/CREATE_DATATEST_EXPLIMIT.xml", true);
        utils = null;
    }

    /**
     * Check the valid next period to start.
     */
    @Test
    public void updateNextPeriodToStart() throws RSSException {
        DbeExpendControl control = new DbeExpendControl();
        DbeExpendLimitPK id = new DbeExpendLimitPK();
        id.setTxElType(ProcessingLimitService.DAY_PERIOD_TYPE);
        control.setId(id);

        Date currentDate = new Date();
        control.setDtNextPeriodStart(currentDate);
        ProcessingLimitUtilTest.logger.debug("Current date: {}", control.getDtNextPeriodStart().toString());
        utils.updateNextPeriodToStart(control);
        ProcessingLimitUtilTest.logger.debug("New date: {}", control.getDtNextPeriodStart().toString());
        Assert.assertTrue(currentDate.compareTo(control.getDtNextPeriodStart()) < 0);

        // Week
        id.setTxElType(ProcessingLimitService.WEEK_TYPE);
        control.setId(id);
        currentDate = new Date();
        control.setDtNextPeriodStart(currentDate);
        ProcessingLimitUtilTest.logger.debug("Current date: {}", control.getDtNextPeriodStart().toString());
        utils.updateNextPeriodToStart(control);
        ProcessingLimitUtilTest.logger.debug("New date: {}", control.getDtNextPeriodStart().toString());
        Assert.assertTrue(currentDate.compareTo(control.getDtNextPeriodStart()) < 0);

        // Month
        id.setTxElType(ProcessingLimitService.MONTH_PERIOD_TYPE);
        control.setId(id);
        currentDate = new Date();
        control.setDtNextPeriodStart(currentDate);
        ProcessingLimitUtilTest.logger.debug("Current date: {}", control.getDtNextPeriodStart().toString());
        utils.updateNextPeriodToStart(control);
        ProcessingLimitUtilTest.logger.debug("New date: {}", control.getDtNextPeriodStart().toString());
        Assert.assertTrue(currentDate.compareTo(control.getDtNextPeriodStart()) < 0);
    }

    /**
     * Check the invalid next period to start.
     */
    @Test
    public void updateInvalidNextPeriodToStart() {
        DbeExpendControl control = new DbeExpendControl();
        DbeExpendLimitPK id = new DbeExpendLimitPK();
        id.setTxElType("InvalidPeriod");
        control.setId(id);
        Date currentDate = new Date();
        control.setDtNextPeriodStart(currentDate);
        try {
            utils.updateNextPeriodToStart(control);
            Assert.fail("Ok with invalid period");
        } catch (RSSException e) {
            Assert.assertEquals("Exeption type", e.getExceptionType(), UNICAExceptionType.INVALID_PARAMETER);
            Assert.assertTrue(e.getMessage().contains("Period"));
        }
    }

    @Test
    public void updateAcccumalateValue() {
        DbeExpendControl control = new DbeExpendControl();
        control.setFtExpensedAmount(new BigDecimal(0));
        DbeTransaction tx = new DbeTransaction();

        tx.setFtChargedAmount(new BigDecimal(4));
        tx.setTcTransactionType(Constants.CHARGE_TYPE);
        BigDecimal total = utils.updateAcccumalateValue(control, tx);
        Assert.assertTrue(new BigDecimal(4).compareTo(total) == 0);

        tx.setTcTransactionType(Constants.REFUND_TYPE);
        total = utils.updateAcccumalateValue(control, tx);
        Assert.assertTrue(new BigDecimal(-4).compareTo(total) == 0);
    }

    @Test
    public void createControl() throws Exception {
        DbeTransaction tx = ProcessingLimitServiceTest.generateTransaction();
        tx.setFtChargedAmount(new BigDecimal(4));
        tx.setTxEndUserId("endUserId");

        DbeAppProvider provider = new DbeAppProvider();
        provider.setTxAppProviderId("providerId");
        tx.setAppProvider(provider);

        DbeExpendLimit limit = new DbeExpendLimit();
        DbeExpendLimitPK id = new DbeExpendLimitPK();
        id.setTxElType(ProcessingLimitService.DAY_PERIOD_TYPE);
        limit.setId(id);

        DbeExpendControl control = utils.createControl(tx, limit);
        Assert.assertEquals(tx.getTxEndUserId(), control.getId().getTxEndUserId());
        Assert.assertTrue(new BigDecimal(0).compareTo(control.getFtExpensedAmount()) == 0);
        Assert.assertEquals(ProcessingLimitService.DAY_PERIOD_TYPE, control.getId().getTxElType());
        Assert.assertEquals(tx.getAppProvider().getTxAppProviderId(), control.getId().getTxAppProviderId());
        Assert.assertEquals(tx.getBmCurrency().getNuCurrencyId(), control.getId().getNuCurrencyId());
    }

    @Test
    public void getLimitsFromString() {
        List<BigDecimal> limits = utils.getLimitsFromString("[1,2,3]");
        Assert.assertEquals(3, limits.size());
        Assert.assertTrue(limits.contains(new BigDecimal(1)));
        limits = utils.getLimitsFromString("1,2,3");
        Assert.assertEquals(3, limits.size());
        Assert.assertTrue(limits.contains(new BigDecimal(2)));
    }

    @Test
    public void addValueToLimits() {
        String result = utils.addValueToLimits(new BigDecimal(3.5), "[1]");
        Assert.assertEquals(result, "[1,3.5]");
        result = utils.addValueToLimits(new BigDecimal(3.5), "[]");
        Assert.assertEquals(result, "[3.5]");
        result = utils.addValueToLimits(new BigDecimal(3.5), "");
        Assert.assertEquals(result, "[3.5]");

    }

    /**
     * Test the function to get charged and charged tax amount without total charged amount.
     */
    @Test
    public void getValueToAddFromTxChargedAndTax() {
        BigDecimal txAmount = new BigDecimal("10.10");
        BigDecimal txTaxAmount = new BigDecimal("0.50");
        DbeTransaction tx = ProcessingLimitServiceTest.generateTransaction();
        tx.setFtChargedAmount(txAmount);
        tx.setFtChargedTaxAmount(txTaxAmount);
        BigDecimal value = utils.getValueToAddFromTx(tx);
        Assert.assertEquals("Amount restored", value.intValue(), txAmount.intValue() + txTaxAmount.intValue());
    }

    /**
     * Test the function to get charged amount without total charged amount.
     */
    @Test
    public void getValueToAddFromTxChargedAndNotTax() {
        BigDecimal txAmount = new BigDecimal("10.10");
        DbeTransaction tx = ProcessingLimitServiceTest.generateTransaction();
        tx.setFtChargedAmount(txAmount);
        tx.setFtChargedTaxAmount(null);
        BigDecimal value = utils.getValueToAddFromTx(tx);
        Assert.assertEquals("Amount restored", value.intValue(), txAmount.intValue());
    }

    /**
     * Test the function to get internal amount.
     */
    @Test
    public void getValueToAddFromTxInternalAndNotTax() {
        BigDecimal txAmount = new BigDecimal("10.10");
        DbeTransaction tx = ProcessingLimitServiceTest.generateTransaction();
        tx.setFtChargedAmount(txAmount);
        tx.setFtChargedTaxAmount(null);
        BigDecimal value = utils.getValueToAddFromTx(tx);
        Assert.assertEquals("Amount restored", value.intValue(), txAmount.intValue());
    }

    /**
     * Test the function to get requested amount and requested tax.
     */
    @Test
    public void getValueToAddFromTxRequestedAmountAndTax() {
        BigDecimal txAmount = new BigDecimal("10.10");
        BigDecimal txTaxAmount = new BigDecimal("0.60");
        DbeTransaction tx = ProcessingLimitServiceTest.generateTransaction();
        tx.setFtChargedAmount(txAmount);
        tx.setFtChargedTaxAmount(txTaxAmount);
        BigDecimal value = utils.getValueToAddFromTx(tx);
        Assert.assertEquals("Amount restored", value.intValue(), txAmount.intValue() + txTaxAmount.intValue());
    }

    /**
     * Test the function to get nulled requested amount.
     */
    @Test
    public void getValueToAddFromTxNulledAmount() {
        BigDecimal txAmount = new BigDecimal("0");
        DbeTransaction tx = ProcessingLimitServiceTest.generateTransaction();
        tx.setFtChargedAmount(null);
        tx.setFtChargedTaxAmount(null);
        BigDecimal value = utils.getValueToAddFromTx(tx);
        Assert.assertEquals("Amount restored", value.intValue(), txAmount.intValue());
    }

}
