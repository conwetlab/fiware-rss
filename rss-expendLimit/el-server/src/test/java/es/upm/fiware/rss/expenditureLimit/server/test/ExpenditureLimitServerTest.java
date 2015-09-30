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
import es.upm.fiware.rss.expenditureLimit.api.LimitGroupBean;
import es.upm.fiware.rss.expenditureLimit.server.ExpenditureLimitServer;
import es.upm.fiware.rss.expenditureLimit.server.service.ExpenditureLimitDataChecker;
import es.upm.fiware.rss.expenditureLimit.test.ExpenditureLimitBeanConstructor;
import es.upm.fiware.rss.oauth.model.ValidatedToken;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 *
 */
public class ExpenditureLimitServerTest {
    /**
     * Logging system.
     */
    private static Logger logger = LoggerFactory.getLogger(ExpenditureLimitServer.class);
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
    private ExpenditureLimitServer server;

    /**
     * 
     */
    private ExpenditureLimitDataChecker checker;
    /**
     * Data
     */
    private final String aggregator = "agg123";
    private final String appProvider = "app123456";
    private final String currency = "EUR";
    private final String type = "daily";
    private final String service = "ServiceTest1";
    private final String userId = "userId01";

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
    public void getUserExpLimits() throws Exception {
        Response response = server.getUserExpLimits(aggregator, appProvider, userId,
            service, currency, type);
        Assert.assertEquals(200, response.getStatus());
    }

    /**
     * 
     * @throws Exception
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public void getProviderExpLimits() throws Exception {
        Response response = server.getProviderExpLimits(aggregator, appProvider, service, currency, type);
        Assert.assertEquals(200, response.getStatus());
    }

    /**
     * @throws Exception
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public void storeGeneralProviderExpLimits() throws Exception {
        ExpenditureLimitServerTest.logger.debug("Into storeGeneralProviderExpLimits method");
        LimitGroupBean expLimits = ExpenditureLimitBeanConstructor.generateLimitGroupBean();
        Response response = server.createModifProviderExpLimit(aggregator, appProvider, expLimits);
        Assert.assertEquals(201, response.getStatus());
    }

    /**
     * @throws Exception
     */
    //@Test
    @Transactional(propagation = Propagation.SUPPORTS)
    public void storeGeneralUserExpLimits() throws Exception {
        ExpenditureLimitServerTest.logger.debug("Into storeGeneralUserExpLimits method");
        LimitGroupBean expLimits = ExpenditureLimitBeanConstructor.generateLimitGroupBean();
        Response response = server.createModifUserExpLimit(aggregator, appProvider, userId, expLimits);
        Assert.assertEquals(201, response.getStatus());
    }

    /**
     * 
     * @throws Exception
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public void deleteProviderExpLimits() throws Exception {
        ExpenditureLimitServerTest.logger.debug("Into deleteProviderExpLimits method");
        Response response = server.deleteProviderExpLimits(aggregator, appProvider, service, currency, type);
        Assert.assertEquals(200, response.getStatus());
    }

    /**
     * 
     * @throws Exception
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public void deleteGeneralUserExpLimits() throws Exception {
        ExpenditureLimitServerTest.logger.debug("Into deleteUserAccumulated method");
        Response response = server.deleteUserExpCtrl(aggregator, appProvider, userId, service, currency, type);
        Assert.assertEquals(200, response.getStatus());
    }
}
