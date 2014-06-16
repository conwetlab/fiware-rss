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

import java.net.URL;
import java.util.ArrayList;

import javax.servlet.ServletOutputStream;
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
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import es.tid.fiware.rss.common.test.DatabaseLoader;
import es.tid.fiware.rss.model.AppProviderParameter;
import es.tid.fiware.rss.model.DbeAppProvider;
import es.tid.fiware.rss.oauth.model.OauthLoginWebSessionData;
import es.tid.fiware.rss.service.SettlementManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:database.xml", "/META-INF/spring/application-context.xml",
    "/META-INF/spring/cxf-beans.xml"})
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
        databaseLoader.cleanInsert("dbunit/CREATE_DATATEST_TRANSACTIONS.xml", true);
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
     * @return
     * @throws Exception
     */
    /*
     * private SettlementManager unwrapSettlementManager() throws Exception {
     * if (AopUtils.isAopProxy(settlementManager) && settlementManager instanceof Advised) {
     * Object target = ((Advised) settlementManager).getTargetSource().getTarget();
     * return (SettlementManager) target;
     * }
     * return null;
     * }
     */

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
    @Test
    public void testLogout() {
        logger.debug("into testLogout method");
        String response = controller.logout(null, null, model);
        Assert.assertEquals("error", response);
        logger.debug("Correct logout");
        response = controller.logout(request, null, model);
        Assert.assertEquals("index", response);
    }

    /**
     * 
     */
    @Test
    public void testDoSettlement() throws Exception {
        logger.debug("into testDoSettlement method");
        JsonResponse response = controller.doSettlement("errorDate", "errorDate", null, null, null);
        Assert.assertFalse(response.getSuccess());
        logger.debug("into testDoSettlement without Date method");
        Mockito
            .doNothing()
            .when(settlementManager)
            .runSettlement(Matchers.any(String.class), Matchers.any(String.class), Matchers.any(String.class),
                Matchers.any(String.class));
        response = controller.doSettlement(null, null, null, null, null);
        Assert.assertTrue(response.getSuccess());
        logger.debug("into testDoSettlement with Date method");
        response = controller.doSettlement("2014/01", "2014/03", null, null, null);
        Assert.assertTrue(response.getSuccess());
    }

    /**
     * 
     */
    @Test
    public void testViewFiles() {
        logger.debug("into testViewFiles method");
        ReflectionTestUtils.setField(controller, "settlementManager", null);
        String response = controller.viewFiles(null, model);
        Assert.assertEquals("error", response);
        logger.debug("Correct logout");
        ReflectionTestUtils.setField(controller, "settlementManager", settlementManager);
        Mockito.when(settlementManager.getSettlementFiles(Matchers.any(String.class))).thenReturn(null);
        response = controller.viewFiles("aggregatorId", model);
        Assert.assertEquals("viewReportsList", response);
    }

    /**
     * 
     */
    @Test
    public void testViewFile() throws Exception {
        logger.debug("into testViewFile method");
        Mockito.when(request.getParameter("rssname")).thenReturn("rss.xls");
        controller.viewfile(request, response);
        URL url = getClass().getClassLoader().getResource("rss.properties");
        Mockito.when(request.getParameter("rssname")).thenReturn(url.getPath());
        Mockito.when(response.getOutputStream()).thenReturn(output);
        controller.viewfile(request, response);
    }

    /**
     * 
     */
    @Test
    public void testViewTransactions() throws Exception {
        logger.debug("into viewTransactions method");
        ReflectionTestUtils.setField(controller, "settlementManager", null);
        String response = controller.viewTransactions(null, model);
        Assert.assertEquals("error", response);
        logger.debug("Correct logout");
        ReflectionTestUtils.setField(controller, "settlementManager", settlementManager);
        Mockito.when(settlementManager.runSelectTransactions(Matchers.any(String.class))).thenReturn(null);
        response = controller.viewTransactions("aggregatorId", model);
        Assert.assertEquals("viewTransactions", response);
    }

    /**
     * 
     */
    @Test
    public void testViewRS() throws Exception {
        logger.debug("into testViewRS method");
        ReflectionTestUtils.setField(controller, "settlementManager", null);
        String response = controller.viewRS(null, model);
        Assert.assertEquals("error", response);
        logger.debug("Correct logout");
        ReflectionTestUtils.setField(controller, "settlementManager", settlementManager);
        Mockito.when(settlementManager.getRSModels(Matchers.any(String.class))).thenReturn(null);
        response = controller.viewRS("aggregatorId", model);
        Assert.assertEquals("viewRS", response);
    }

    /**
     * 
     */
    @Test
    public void testViewProviders() throws Exception {
        logger.debug("into testViewProviders method");
        ReflectionTestUtils.setField(controller, "settlementManager", null);
        String response = controller.viewProviders(null, model);
        Assert.assertEquals("error", response);
        logger.debug("Correct logout");
        ReflectionTestUtils.setField(controller, "settlementManager", settlementManager);
        Mockito.when(settlementManager.getProviders(Matchers.any(String.class))).thenReturn(
            new ArrayList<DbeAppProvider>());
        response = controller.viewProviders("aggregatorId", model);
        Assert.assertEquals("viewProviders", response);
    }

    /**
     * 
     */
    @Test
    public void testClean() throws Exception {
        logger.debug("into testClean method");
        AppProviderParameter provider = new AppProviderParameter();
        provider.setName("provider");
        ReflectionTestUtils.setField(controller, "settlementManager", null);
        JsonResponse response = controller.clean(provider, null, model);
        Assert.assertFalse(response.getSuccess());
        logger.debug("Correct logout");
        ReflectionTestUtils.setField(controller, "settlementManager", settlementManager);
        Mockito
            .doNothing()
            .when(settlementManager)
            .runClean(Matchers.any(String.class));
        BindingResult result = Mockito.mock(BindingResult.class);
        Mockito.when(result.hasErrors()).thenReturn(false);
        response = controller.clean(provider, result, model);
        Assert.assertTrue(response.getSuccess());
        Mockito.when(result.hasErrors()).thenReturn(true);
        response = controller.clean(null, result, model);
        Assert.assertFalse(response.getSuccess());
    }

    /**
     * 
     */
    @Test
    public void testCreateAggregator() throws Exception {
        logger.debug("into testCreateAggregator method");
        ReflectionTestUtils.setField(controller, "settlementManager", null);
        JsonResponse response = controller.createAggregator("agreggatorId", "agreggatorName", model);
        Assert.assertFalse(response.getSuccess());
        logger.debug("into testCreateAggregator without data");
        ReflectionTestUtils.setField(controller, "settlementManager", settlementManager);
        Mockito
            .doNothing()
            .when(settlementManager)
            .runCreateAggretator(Matchers.any(String.class), Matchers.any(String.class));
        response = controller.createAggregator(null, "agreggatorName", model);
        Assert.assertFalse(response.getSuccess());
        logger.debug("into testCreateAggregator with data");
        response = controller.createAggregator("agreggatorId", "agreggatorName", model);
        Assert.assertTrue(response.getSuccess());
    }

    /**
    * 
    */
    @Test
    public void testCreateProvider() throws Exception {
        logger.debug("into testCreateProvider method");
        ReflectionTestUtils.setField(controller, "settlementManager", null);
        JsonResponse response = controller.createProvider("providerId", "providerName", "aggredatorId", model);
        Assert.assertFalse(response.getSuccess());
        logger.debug("into testCreateProvider without data");
        ReflectionTestUtils.setField(controller, "settlementManager", settlementManager);
        Mockito
            .doNothing()
            .when(settlementManager)
            .runCreateAggretator(Matchers.any(String.class), Matchers.any(String.class));
        response = controller.createProvider(null, "providerName", "aggredatorId", model);
        Assert.assertFalse(response.getSuccess());
        logger.debug("into testCreateProvider with data");
        response = controller.createProvider("providerId", "providerName", "aggredatorId", model);
        Assert.assertTrue(response.getSuccess());
    }

    /**
     * 
     */
    @Test
    public void testCreateRSModel() throws Exception {
        logger.debug("into testCreateRSModel method");
        ReflectionTestUtils.setField(controller, "settlementManager", null);
        JsonResponse response = controller.createRSModel("providerId", "productClass", new Long(50), model);
        Assert.assertFalse(response.getSuccess());
        logger.debug("into testCreateRSModel without data");
        ReflectionTestUtils.setField(controller, "settlementManager", settlementManager);
        Mockito
            .doNothing()
            .when(settlementManager)
            .runCreateRSModel(Matchers.any(String.class), Matchers.any(String.class), Matchers.any(Long.class));
        response = controller.createRSModel(null, "productClass", new Long(50), model);
        Assert.assertFalse(response.getSuccess());
        logger.debug("into testCreateRSModel with data");
        response = controller.createRSModel("providerId", "productClass", new Long(50), model);
        Assert.assertTrue(response.getSuccess());
        response = controller.createRSModel("providerId", "", new Long(50), model);
        Assert.assertTrue(response.getSuccess());
    }
}
