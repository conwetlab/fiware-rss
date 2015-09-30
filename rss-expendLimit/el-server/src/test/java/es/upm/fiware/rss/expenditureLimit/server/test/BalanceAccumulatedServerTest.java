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
package es.upm.fiware.rss.expenditureLimit.server.test;

import java.math.BigDecimal;
import java.net.URI;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import es.upm.fiware.rss.common.properties.AppProperties;
import es.upm.fiware.rss.common.test.DatabaseLoader;
import es.upm.fiware.rss.expenditureControl.api.ExpendControl;
import es.upm.fiware.rss.expenditureLimit.server.BalanceAccumulatedServer;
import es.upm.fiware.rss.expenditureLimit.server.service.ExpenditureLimitDataChecker;
import es.upm.fiware.rss.oauth.model.ValidatedToken;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


public class BalanceAccumulatedServerTest {
    /**
     * Logging system.
     */
    private static Logger logger = LoggerFactory.getLogger(BalanceAccumulatedServerTest.class);
    /**
     * For database access.
     */
    @Autowired
    private DataSource dataSource;
    /**
     * 
     */
    @Autowired
    private DatabaseLoader databaseLoader;
    /**
     * 
     */
    @Autowired
    private AppProperties appProperties;
    /**
     * 
     */
    @Autowired
    private BalanceAccumulatedServer server;
    /**
     * 
     */
    private final String endUserId = "userIdUpdate";
    private final String serviceName = "ServiceTest1";
    private ExpenditureLimitDataChecker checker;

    /**
     * 
     */
    @PostConstruct
    public void init() throws Exception {
        ValidatedToken validToken = new ValidatedToken();
        validToken.setEmail("mail@mail.com");
        checker = (ExpenditureLimitDataChecker) ReflectionTestUtils.getField(server, "checker");
        //ReflectionTestUtils.setField(checker, "oauthManager", oauth);
        UriInfo mockUriInfo = Mockito.mock(UriInfo.class);
        Mockito.when(mockUriInfo.getBaseUri()).thenReturn(new URI("http://www.test.com/go"));
        ReflectionTestUtils.setField(server, "ui", mockUriInfo);
    }

    /**
     * 
     * @throws Exception
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public void getUserAccumulated() throws Exception {

        Response response = server.getUserAccumulated(endUserId,
            serviceName, "agg123", "app123456", "EUR", "daily");
        Assert.assertEquals(200, response.getStatus());
    }

    /**
     * 
     * @throws Exception
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public void checkUserBalance() throws Exception {

        ExpendControl expendControl = generateExpendControl();
        Response response = server.checkUserBalance("endUserId", expendControl);
        Assert.assertEquals(201, response.getStatus());
    }

    /**
     * 
     * @throws Exception
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public void updateUserAccumulated() throws Exception {

        ExpendControl expendControl = generateExpendControl();
        Response response = server.updateUserAccumulated(endUserId, expendControl);
        Assert.assertEquals(201, response.getStatus());
    }

    /**
     * 
     * @throws Exception
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public void deleteUserAccumulated() throws Exception {
        BalanceAccumulatedServerTest.logger.debug("Into deleteUserAccumulated method");
        ExpendControl expendControl = generateExpendControl();
        Response response = server.deleteUserAccumulated(endUserId, expendControl);
        Assert.assertEquals(200, response.getStatus());
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
        expendControl.setAppProvider("app123456");
        expendControl.setChargeType("C");
        expendControl.setCurrency("EUR");
        expendControl.setType("daily");
        return expendControl;
    }
}
