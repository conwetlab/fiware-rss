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

package es.upm.fiware.rss.controller;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ModelMap;

import es.upm.fiware.rss.common.test.DatabaseLoader;
import es.upm.fiware.rss.oauth.model.OauthLoginWebSessionData;
import es.upm.fiware.rss.service.SettlementManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class SettlementControllerTest {
    /***
     * Logging system.
     */
    private final Logger logger = LoggerFactory.getLogger(SettlementControllerTest.class);
    /**
     * 
     */
    @Autowired
    private DatabaseLoader databaseLoader;
    /**
     * 
     */
    private SettlementManager settlementManager;
    /**
     * 
     */
    @Autowired
    private SettlementController controller;
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
    private ModelMap model;
    /**
     * 
     */
    private OauthLoginWebSessionData sessionData;
    /**
     * User session attribute.
     */
    private final String USER_SESSION = "userSession";
    /**
     * 
     */
    private ServletOutputStream output;

    /**
     * Method to insert data before test.
     * 
     * @throws Exception
     *             from dbb
     */
    @Before
    public void setUp() throws Exception {
        model = new ModelMap();
        sessionData = new OauthLoginWebSessionData();
        // prepare mockito
        request = Mockito.mock(HttpServletRequest.class);
        response = Mockito.mock(HttpServletResponse.class);
        session = Mockito.mock(HttpSession.class);
        output = Mockito.mock(ServletOutputStream.class);
        Mockito.when(request.getSession()).thenReturn(session);
        settlementManager = Mockito.mock(SettlementManager.class);
        ReflectionTestUtils.setField(controller, "settlementManager", settlementManager);
    }


    /**
     * 
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public void testSettlement() {
        logger.debug("into testLogout method");
        String response = controller.settlement(null, model);
        Assert.assertEquals("error", response);
        logger.debug("Correct logout");
        response = controller.settlement(request, model);
        Assert.assertEquals("settlement", response);

        logger.debug("Correct logout with session data");
        Mockito.when(session.getAttribute(USER_SESSION)).thenReturn(sessionData);
        response = controller.settlement(request, model);
        Assert.assertEquals("settlement", response);
    }


    /**
     * 
     */
    public void testViewTransactions() throws Exception {
        logger.debug("into viewTransactions method");
        ReflectionTestUtils.setField(controller, "settlementManager", null);
        String response = controller.viewTransactions(null, model);
        Assert.assertEquals("error", response);
        logger.debug("Correct logout");
        ReflectionTestUtils.setField(controller, "settlementManager", settlementManager);
        response = controller.viewTransactions("aggregatorId", model);
        Assert.assertEquals("viewTransactions", response);
    }

}
