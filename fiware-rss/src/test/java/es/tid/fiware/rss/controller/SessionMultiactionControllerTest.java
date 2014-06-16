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
package es.tid.fiware.rss.controller;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import es.tid.fiware.rss.common.test.DatabaseLoader;
import es.tid.fiware.rss.dao.DbeAggregatorDao;
import es.tid.fiware.rss.model.DbeAggregator;
import es.tid.fiware.rss.oauth.model.OauthLoginWebSessionData;
import es.tid.fiware.rss.oauth.service.OauthManager;

/**
 * 
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:database.xml"})
public class SessionMultiactionControllerTest {
    /**
     * Logging system.
     */
    private static Logger logger = LoggerFactory.getLogger(SessionMultiactionControllerTest.class);
    /**
     * 
     */
    @Autowired
    private DatabaseLoader databaseLoader;
    /**
     * 
     */
    private OauthManager oauth;
    /**
     * 
     */
    private DbeAggregatorDao dbeAggregatorDao;
    /**
     * 
     */
    private HttpServletResponse response;
    /**
     * 
     */
    private HttpServletRequest request;
    /**
     * 
     */
    private HttpSession session;
    /**
     * 
     */
    private OauthLoginWebSessionData userSession;
    /**
     * 
     */
    private DbeAggregator aggregator;
    /**
     * 
     */
    private SessionMultiActionController controller = new SessionMultiActionController();

    @PostConstruct
    public void init() {
        oauth = Mockito.mock(OauthManager.class);
        Mockito.when(oauth.hasExternalLogin()).thenReturn(true);
        dbeAggregatorDao = Mockito.mock(DbeAggregatorDao.class);
        ReflectionTestUtils.setField(controller, "oauth", oauth);
        ReflectionTestUtils.setField(controller, "dbeAggregatorDao", dbeAggregatorDao);
        response = Mockito.mock(HttpServletResponse.class);
        request = Mockito.mock(HttpServletRequest.class);
        session = Mockito.mock(HttpSession.class);
        Mockito.when(request.getSession()).thenReturn(session);
        userSession = new OauthLoginWebSessionData();
        userSession.setExpiresIn(1000);
        userSession.setAccessToken("Token");
        userSession.setRole("consumer");
        userSession.setEmail("email");
        aggregator = new DbeAggregator();
        aggregator.setTxEmail("email");
    }

    /**
     * Method to insert data before test.
     * 
     * @throws Exception
     *             from dbb
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
     * 
     */
    @Test
    public void postHandle() throws Exception {
        controller.postHandle(null, null, null, null);
    }

    /**
     * 
     */
    @Test
    public void afterCompletion() throws Exception {
        controller.afterCompletion(null, null, null, null);
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void preHandleWithoutLogin() throws Exception {
        Mockito.when(oauth.hasExternalLogin()).thenReturn(false);
        Assert.assertTrue(controller.preHandle(request, response, null));
        Mockito.when(oauth.hasExternalLogin()).thenReturn(true);
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void preHandleFirstcall() throws Exception {
        Assert.assertFalse(controller.preHandle(request, response, null));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void preHandleWithSession() throws Exception {
        Mockito.when(session.getAttribute(SessionMultiActionController.USER_SESSION)).thenReturn(userSession);
        Assert.assertTrue(controller.preHandle(request, response, null));
        Mockito.when(session.getAttribute(SessionMultiActionController.USER_SESSION)).thenReturn(null);
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void preHandleWithCodeError() throws Exception {
        Mockito.when(request.getParameter("code")).thenReturn("code");
        Assert.assertTrue(controller.preHandle(request, response, null));
        Mockito.when(request.getParameter("code")).thenReturn(null);
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void preHandleWithCode() throws Exception {
        Mockito.when(request.getParameter("code")).thenReturn("code");
        Mockito.when(oauth.getToken("code")).thenReturn(userSession);
        Mockito.doNothing().when(oauth)
            .checkUserPermisions(Matchers.any(OauthLoginWebSessionData.class));
        Mockito.when(oauth.getGrantedRole()).thenReturn("Provider");
        Mockito.when(dbeAggregatorDao.getById(Matchers.any(String.class))).thenReturn(aggregator);
        Assert.assertTrue(controller.preHandle(request, response, null));
        Mockito.when(request.getParameter("code")).thenReturn(null);
        Mockito.when(oauth.getGrantedRole()).thenReturn(null);
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void preHandleWithCodeNoAggregator() throws Exception {
        Mockito.when(request.getParameter("code")).thenReturn("code");
        Mockito.when(oauth.getToken("code")).thenReturn(userSession);
        Mockito.doNothing().when(oauth)
            .checkUserPermisions(Matchers.any(OauthLoginWebSessionData.class));
        Mockito.when(oauth.getGrantedRole()).thenReturn("Provider");
        Mockito.when(dbeAggregatorDao.getById(Matchers.any(String.class))).thenReturn(null);
        Assert.assertTrue(controller.preHandle(request, response, null));
        Mockito.when(request.getParameter("code")).thenReturn(null);
        Mockito.when(oauth.getGrantedRole()).thenReturn(null);
    }

}