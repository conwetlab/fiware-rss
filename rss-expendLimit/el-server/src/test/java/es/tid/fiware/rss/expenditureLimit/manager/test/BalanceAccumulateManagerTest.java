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

package es.tid.fiware.rss.expenditureLimit.manager.test;

import java.math.BigDecimal;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import es.tid.fiware.rss.common.test.DatabaseLoader;
import es.tid.fiware.rss.exception.RSSException;
import es.tid.fiware.rss.expenditureControl.api.AccumExpend;
import es.tid.fiware.rss.expenditureControl.api.AccumsExpend;
import es.tid.fiware.rss.expenditureControl.api.ExpendControl;
import es.tid.fiware.rss.expenditureLimit.server.service.BalanceAccumulateManager;
import es.tid.fiware.rss.expenditureLimit.server.service.ExpenditureLimitDataChecker;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:database.xml" })
public class BalanceAccumulateManagerTest {
    /**
     * Logging system.
     */
    private static Logger logger = LoggerFactory.getLogger(ExpenditureLimitDataCheckerTest.class);
    @Autowired
    private DataSource dataSource;
    @Autowired
    private DatabaseLoader databaseLoader;
    @Autowired
    private ExpenditureLimitDataChecker checker;
    @Autowired
    private BalanceAccumulateManager balanceAccumulateManager;
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final String endUserId = "userIdUpdate";
    private final String serviceName = "ServiceTest1";

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
     * Data to take into account in test
     * DBE_EXPEND_LIMIT NU_SERVICE_ID ="1"
     * TX_END_USER_ID = "noUserId"
     * TX_APPPROVIDER_ID ="-1"
     * NU_CURRENCY_ID = "1"
     * TX_EL_TYPE = "monthly"
     * FT_MAX_AMOUNT = "300"/>
     * 
     * <DBE_EXPEND_CONTROL NU_SERVICE_ID ="1"
     * TX_END_USER_ID = "userIdUpdate"
     * TX_APPPROVIDER_ID ="app123456"
     * TX_EL_TYPE = "monthly"
     * NU_CURRENCY_ID = "1"
     * FT_EXPENSED_AMOUNT = "41"/>
     */

    /**
     * 
     */
    @Test
    @Transactional(propagation = Propagation.SUPPORTS)
    public void getUserAccumulated() throws RSSException {
        BalanceAccumulateManagerTest.logger.debug("Into getUserAccumulated method.");
        AccumsExpend result = balanceAccumulateManager.getUserAccumulated(endUserId,
            serviceName, "agg123","app123456", "EUR", "daily");
        Assert.assertNotNull(result);
        Assert.assertTrue("No controls files found", result.getAccums().size() > 0);

    }

    /**
     * 
     */
    @Test
    @Transactional(propagation = Propagation.SUPPORTS)
    public void checkUserBalance() throws RSSException {
        BalanceAccumulateManagerTest.logger.debug("Into checkUserBalance method.");
        thrown.expect(RSSException.class);
        thrown
            .expectMessage("Insufficient payment method balance");
        ExpendControl control = generateExpendControl();
        AccumsExpend result = balanceAccumulateManager.checkUserBalance(endUserId, control);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.getAccums().size() > 0);
        control.setAmount(new BigDecimal(1000));
        result = balanceAccumulateManager.checkUserBalance(endUserId, control);

    }

    /**
     * 
     */
    @Test
    @Transactional(propagation = Propagation.SUPPORTS)
    public void updateUserAccumulated() throws RSSException {
        BalanceAccumulateManagerTest.logger.debug("Into updateUserAccumulated method.");
        ExpendControl control = generateExpendControl();
        AccumsExpend result = balanceAccumulateManager.updateUserAccumulated(endUserId, control);
        Assert.assertTrue(result.getAccums().size() > 0);
    }

    /**
     * 
     */
    @Test
    @Transactional(propagation = Propagation.SUPPORTS)
    public void deleteUserAccumulated() throws RSSException {
        BalanceAccumulateManagerTest.logger.debug("Into getUserAccumulated method.");
        ExpendControl control = generateExpendControl();
        balanceAccumulateManager.deleteUserAccumulated(endUserId, control);
        BalanceAccumulateManagerTest.logger.debug("Get objects after deleting.");

        AccumsExpend result = balanceAccumulateManager.getUserAccumulated(
                endUserId, control.getService(),
                control.getAggregator(),
                control.getAppProvider(),
                control.getCurrency(),
                control.getType());

        Assert.assertTrue(result.getAccums().size() > 0);
        boolean changed = false;
        for (AccumExpend accum : result.getAccums()) {
            if (accum.getType().equalsIgnoreCase(control.getType())
                && accum.getExpensedAmount().compareTo(BigDecimal.valueOf(0)) == 0) {
                changed = true;
            }
        }
        Assert.assertTrue(changed);
    }

    /**
     * Generate expend control test
     * 
     * @return
     */
    private ExpendControl generateExpendControl() {
        ExpendControl expendControl = new ExpendControl();
        expendControl.setService(serviceName);
        expendControl.setAmount(new BigDecimal(1));
        expendControl.setAggregator("agg123");
        expendControl.setAppProvider("app123456");
        expendControl.setChargeType("C");
        expendControl.setCurrency("EUR");
        expendControl.setType("daily");
        return expendControl;
    }

}
