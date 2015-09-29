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

package es.upm.fiware.rss.expenditureLimit.manager.test;

import java.math.BigDecimal;

import javax.sql.DataSource;

import org.junit.After;
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

import es.upm.fiware.rss.common.test.DatabaseLoader;
import es.upm.fiware.rss.exception.RSSException;
import es.upm.fiware.rss.expenditureLimit.api.LimitBean;
import es.upm.fiware.rss.expenditureLimit.server.service.ExpenditureLimitDataChecker;
import es.upm.fiware.rss.expenditureLimit.test.ExpenditureLimitBeanConstructor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:database.xml" })
public class ExpenditureLimitDataCheckerTest {
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
    @Rule
    public ExpectedException thrown = ExpectedException.none();

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
     * 
     */
    @Test
    @Transactional(propagation = Propagation.SUPPORTS)
    public void checkChargeRequiredParametersTest() throws RSSException {
        ExpenditureLimitDataCheckerTest.logger.debug("Into checkChargeRequiredParametersTest mehtod");
        thrown.expect(RSSException.class);
        thrown
            .expectMessage("Required parameters not found:enUserId, service, appProvider, currency, chargeType, amount.");
        checker.checkChargeRequiredParameters("urlEndUserId", "service", "aggId","appPorviderId", "currency", "chargeType",
            new BigDecimal(10));
        ExpenditureLimitDataCheckerTest.logger.debug("No exception expected");
        checker.checkChargeRequiredParameters(null, "service", "aggId","appPorviderId", "currency", "chargeType",
            new BigDecimal(10));

    }

    /**
     * 
     */
    @Test
    @Transactional(propagation = Propagation.SUPPORTS)
    public void checkRequiredParametersTest() throws RSSException {
        ExpenditureLimitDataCheckerTest.logger.debug("Into checkChargeRequiredParametersTest mehtod");
        thrown.expect(RSSException.class);
        thrown
            .expectMessage("Required parameters not found:enUserId, service, appProvider, currency.");
        checker.checkRequiredParameters("urlEndUserId", "service", "aggId", "appPorviderId", "currency");
        ExpenditureLimitDataCheckerTest.logger.debug("No exception expected");
        checker.checkRequiredParameters(null, "service", "aggId", "appPorviderId", "currency");

    }

    /**
     * 
     */
    @Test
    @Transactional(propagation = Propagation.SUPPORTS)
    public void checkRequiredSearchParametersTest() throws RSSException {
        ExpenditureLimitDataCheckerTest.logger.debug("Into checkRequiredSearchParametersTest mehtod");
        thrown.expect(RSSException.class);
        thrown
            .expectMessage("Required parameters not found:enUserId, service, appProvider.");
        checker.checkRequiredSearchParameters("urlEndUserId", "service", "aggId", "appPorviderId");
        ExpenditureLimitDataCheckerTest.logger.debug("No exception expected");
        checker.checkRequiredSearchParameters(null, "service", "aggId", "appPorvider");

    }

    /**
     * 
     */
    @Test
    @Transactional(propagation = Propagation.SUPPORTS)
    public void checkCurrencyTest() throws RSSException {
        ExpenditureLimitDataCheckerTest.logger.debug("Into checkCurrencyTest mehtod");
        thrown.expect(RSSException.class);
        thrown.expectMessage("Currency Not found.");
        checker.checkCurrency("EUR");
        ExpenditureLimitDataCheckerTest.logger.debug("No exception expected");
        checker.checkCurrency("dbc");

    }

    /**
     * 
     */
    @Test
    @Transactional(propagation = Propagation.SUPPORTS)
    public void checkServiceTest() throws RSSException {
        ExpenditureLimitDataCheckerTest.logger.debug("Into checkServiceTest mehtod");
        thrown.expect(RSSException.class);
        thrown.expectMessage("Service Not found.");
        //checker.checkService("ServiceTest1");
        ExpenditureLimitDataCheckerTest.logger.debug("No exception expected");
        //checker.checkService("bluevia");
    }

    /**
     * 
     */
    @Test
    @Transactional(propagation = Propagation.SUPPORTS)
    public void checkDbeAppProviderTest() throws RSSException {
        ExpenditureLimitDataCheckerTest.logger.debug("Into checkDbeAppProviderTest mehtod");
        thrown.expect(RSSException.class);
        thrown.expectMessage("AppProvider Not found.");
        checker.checkDbeAppProvider("agg123", "app123456");
        ExpenditureLimitDataCheckerTest.logger.debug("No exception expected");
        checker.checkDbeAppProvider("agg123", "newone");
    }

    /**
     * 
     */
    @Test
    @Transactional(propagation = Propagation.SUPPORTS)
    public void checkElTypeTest() throws RSSException {
        ExpenditureLimitDataCheckerTest.logger.debug("Into checkElTypeTest mehtod");
        thrown.expect(RSSException.class);
        thrown.expectMessage("eltype Not found.");
        checker.checkElType("monthly");
        ExpenditureLimitDataCheckerTest.logger.debug("No exception expected");
        checker.checkElType("bbb");
    }

    /**
     * 
     */
    @Test
    @Transactional(propagation = Propagation.SUPPORTS)
    public void checkChargeTypeTest() throws RSSException {
        ExpenditureLimitDataCheckerTest.logger.debug("Into checkChargeTypeTest mehtod");
        thrown.expect(RSSException.class);
        thrown.expectMessage("chageType Not found.");
        checker.checkChargeType("C");
        ExpenditureLimitDataCheckerTest.logger.debug("No exception expected");
        checker.checkChargeType("N");
    }

    /**
     * 
     */
    @Test
    @Transactional(propagation = Propagation.SUPPORTS)
    public void checkMandatoryDatumExistenceTest() throws RSSException {
        ExpenditureLimitDataCheckerTest.logger.debug("Into checkMandatoryDatumExistenceTest mehtod");
        thrown.expect(RSSException.class);
        thrown.expectMessage("datumName");
        checker.checkMandatoryDatumExistence("datum", "datumName");
        ExpenditureLimitDataCheckerTest.logger.debug("No exception expected");
        checker.checkMandatoryDatumExistence(null, "datumName");
    }

    /**
     * 
     */
    @Test
    @Transactional(propagation = Propagation.SUPPORTS)
    public void checkExpLimitDataTest() throws RSSException {
        ExpenditureLimitDataCheckerTest.logger.debug("Into checkExpLimitDataTest mehtod");
        thrown.expect(RSSException.class);
        thrown.expectMessage("Missing mandatory parameter: Limit Currency");
        LimitBean limitBean = ExpenditureLimitBeanConstructor.createExpCtrlBean();
        checker.checkExpLimitData(limitBean);
        ExpenditureLimitDataCheckerTest.logger.debug("No exception expected");
        limitBean.setCurrency(null);
        checker.checkExpLimitData(limitBean);
    }

}
