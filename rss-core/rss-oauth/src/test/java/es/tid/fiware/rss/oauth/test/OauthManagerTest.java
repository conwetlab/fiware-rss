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
package es.tid.fiware.rss.oauth.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.tid.fiware.rss.exception.RSSException;
import es.tid.fiware.rss.oauth.model.ApplicationInfo;
import es.tid.fiware.rss.oauth.model.OauthLoginWebSessionData;
import es.tid.fiware.rss.oauth.model.Role;
import es.tid.fiware.rss.oauth.model.ValidatedToken;
import es.tid.fiware.rss.oauth.service.OauthManager;
import es.tid.fiware.rss.oauth.service.ResponseHandler;

/**
 * 
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:context.xml"})
public class OauthManagerTest {
    private static final Logger log = LoggerFactory.getLogger(OauthManagerTest.class);
    /**
     * 
     */
    @Autowired
    private OauthManager oauthManager;

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    /**
     * 
     */
    private DefaultHttpClient httpClient = Mockito.mock(DefaultHttpClient.class);
    /**
     * 
     */
    private HttpResponse mHttpResponseMock = Mockito.mock(HttpResponse.class);

    private StatusLine mStatusLineMock = Mockito.mock(StatusLine.class);

    private HttpEntity mHttpEntityMock = Mockito.mock(HttpEntity.class);
    private HttpPost mockHttpPost = Mockito.mock(HttpPost.class);

    private ClientConnectionManager conectionManager = Mockito.mock(ClientConnectionManager.class);

    /**
     * 
     */
    @Test
    public void getGrantedRoleTest() {
        Assert.assertTrue(oauthManager.getGrantedRole().equalsIgnoreCase("Provider"));
    }

    /**
     * 
     */
    @Test
    public void buildHeaderTest() {
        OauthManagerTest.log.debug(oauthManager.buildHeader());
        Assert
            .assertTrue(oauthManager
                .buildHeader()
                .equalsIgnoreCase(
                    "Basic MTM6YmZjZmMwZGMwODNmNWZiMmQ4OWM1NzI5NjQ2NjY0OGQxYWYyOWI3YzgzYmI3MThjNzhmNjk1YjYyY2FkZTRiMTcxODAxMjA1ZmZiODIxOTRkYmNiMTBiZWQ5NWUxNDM2MGYwMmY3OTdiZGYxMDAzNmUyODA0NDJhNmE0NDVmODI="));
    }

    /**
     * 
     */
    @Test
    public void hasExternalLoginTest() {
        Assert.assertTrue(oauthManager.hasExternalLogin());
        ReflectionTestUtils.setField(oauthManager, "externalLogin", "N");
        Assert.assertFalse(oauthManager.hasExternalLogin());
    }

    /**
     * 
     */
    @Test
    public void getAuthorizationUrlTest() {
        OauthManagerTest.log.debug(oauthManager.getAuthorizationUrl());
        Assert
            .assertTrue(oauthManager
                .getAuthorizationUrl()
                .equalsIgnoreCase(
                    "https://account.testbed.fi-ware.org/oauth2/authorize?response_type=code&client_id=13&state=xyz&redirect_uri=http://rss.testbed.fi-ware.org:8080/fiware-rss/settlement/settlement.html"));
    }

    /**
     * 
     */
    @Test
    public void getTokenUrlTest() {
        OauthManagerTest.log.debug("getTokenUrlTest");
        Assert
            .assertTrue(oauthManager
                .getTokenUrl()
                .equalsIgnoreCase(
                    "https://account.testbed.fi-ware.org/token"));
    }

    /**
     * 
     */
    @Test
    public void getInfoUserUrlTest() {
        OauthManagerTest.log.debug("getInfoUserUrlTest");
        Assert.assertTrue(oauthManager.getInfoUserUrl("token").equalsIgnoreCase(
            "https://account.testbed.fi-ware.org/user?access_token=token"));
    }

    /**
     * 
     */
    @Test
    public void getAplicationsUrlTest() {
        OauthManagerTest.log.debug("getAplicationsUrlTest");
        Assert.assertTrue(oauthManager.getAplicationsUrl("actorId", "token").equalsIgnoreCase(
            "https://account.testbed.fi-ware.org/applications.json?actor_id=actorId&access_token=token"));
    }

    /**
     * 
     */
    @Test
    public void checkApplictionIdsTest() throws Exception {
        OauthManagerTest.log.debug("checkApplictionIdsTest");
        ApplicationInfo application = new ApplicationInfo();
        application.setId("13");
        ApplicationInfo[] applications = {application};
        // no exception produced
        oauthManager.checkApplictionIds(applications);
        thrown.expect(RSSException.class);
        thrown.expectMessage("User has not permission");
        applications[0].setId("notExisting");
        oauthManager.checkApplictionIds(applications);
        // other checking
        oauthManager.checkApplictionIds(null);
        // other checking
        applications[0] = null;
        oauthManager.checkApplictionIds(null);
    }

    /**
     * 
     */
    @Test
    public void getTokenTest() throws Exception {
        OauthManagerTest.log.debug("Into getTokenTest");
        OauthLoginWebSessionData session = new OauthLoginWebSessionData();
        session.setAccessToken("accessToken");
        session.setTokenType("bearer");
        session.setExpiresIn(2000);
        session.setRefreshToken("refreshToken");
        // mocks
        ResponseHandler handler = new ResponseHandler();
        ReflectionTestUtils.setField(handler, "responseContent", parseRequest2JSON(session));
        ReflectionTestUtils.setField(handler, "status", HttpStatus.SC_OK);
        ReflectionTestUtils.setField(handler, "content", true);
        ReflectionTestUtils.setField(oauthManager, "handler", handler);
        // mock response
        Mockito.when(mHttpResponseMock.getStatusLine()).thenReturn(mStatusLineMock);
        Mockito.when(mockHttpPost.getURI()).thenReturn(new URI("http://test.com"));
        Mockito.when(httpClient.getConnectionManager()).thenReturn(conectionManager);
        Mockito.when(httpClient.execute(Matchers.any(HttpRequestBase.class),
            Matchers.any(ResponseHandler.class))).thenAnswer(
            new Answer<HttpResponse>() {
                @Override
                public HttpResponse answer(InvocationOnMock invocation) throws IOException {
                    return mHttpResponseMock;
                }
            });
        // set client.
        ReflectionTestUtils.setField(oauthManager, "httpclient", httpClient);
        OauthLoginWebSessionData sessionObtained = oauthManager.getToken("Token");
        Assert.assertEquals(session.getExpiresIn(), sessionObtained.getExpiresIn());
        Assert.assertEquals(session.getAccessToken(), sessionObtained.getAccessToken());
        // Error in request
        ReflectionTestUtils.setField(handler, "status", HttpStatus.SC_FORBIDDEN);
        thrown.expect(RSSException.class);
        oauthManager.getToken("Token");
        // other error
        ReflectionTestUtils.setField(handler, "responseContent", parseRequest2JSON(session));
        oauthManager.getToken("Token");

    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void getTokenTestErrorNoContent() throws Exception {
        OauthManagerTest.log.debug("Into getTokenTestErrorNoContent");
        // mocks
        ResponseHandler handler = new ResponseHandler();
        ReflectionTestUtils.setField(handler, "status", HttpStatus.SC_FORBIDDEN);
        ReflectionTestUtils.setField(handler, "content", false);
        ReflectionTestUtils.setField(oauthManager, "handler", handler);
        // mock response
        Mockito.when(mHttpResponseMock.getStatusLine()).thenReturn(mStatusLineMock);
        Mockito.when(mockHttpPost.getURI()).thenReturn(new URI("http://test.com"));
        Mockito.when(httpClient.getConnectionManager()).thenReturn(conectionManager);
        Mockito.when(httpClient.execute(Matchers.any(HttpRequestBase.class),
            Matchers.any(ResponseHandler.class))).thenAnswer(
            new Answer<HttpResponse>() {
                @Override
                public HttpResponse answer(InvocationOnMock invocation) throws IOException {
                    return mHttpResponseMock;
                }
            });
        // set client.
        ReflectionTestUtils.setField(oauthManager, "httpclient", httpClient);
        thrown.expect(RSSException.class);
        oauthManager.getToken("Token");
    }

    @Test
    public void getTokenTestError() throws Exception {
        OauthManagerTest.log.debug("Into getTokenTest");
        OauthLoginWebSessionData session = new OauthLoginWebSessionData();
        session.setAccessToken("accessToken");
        session.setTokenType("bearer");
        session.setExpiresIn(2000);
        session.setRefreshToken("refreshToken");
        // mocks
        ResponseHandler handler = new ResponseHandler();
        ReflectionTestUtils.setField(handler, "responseContent", "error");
        ReflectionTestUtils.setField(handler, "status", HttpStatus.SC_OK);
        ReflectionTestUtils.setField(handler, "content", true);
        ReflectionTestUtils.setField(oauthManager, "handler", handler);
        // mock response
        Mockito.when(mHttpResponseMock.getStatusLine()).thenReturn(mStatusLineMock);
        Mockito.when(mockHttpPost.getURI()).thenReturn(new URI("http://test.com"));
        Mockito.when(httpClient.getConnectionManager()).thenReturn(conectionManager);
        Mockito.when(httpClient.execute(Matchers.any(HttpRequestBase.class),
            Matchers.any(ResponseHandler.class))).thenAnswer(
            new Answer<HttpResponse>() {
                @Override
                public HttpResponse answer(InvocationOnMock invocation) throws IOException {
                    return mHttpResponseMock;
                }
            });
        // set client.
        ReflectionTestUtils.setField(oauthManager, "httpclient", httpClient);
        // Error in request
        thrown.expect(Exception.class);
        oauthManager.getToken("Token");
    }

    /**
     * 
     */
    @Test
    public void checkUserPermisionsTest() throws Exception {
        OauthManagerTest.log.debug("Into checkUserPermisionsTest");
        // request object
        OauthLoginWebSessionData session = new OauthLoginWebSessionData();
        session.setAccessToken("accessToken");
        // response object
        ValidatedToken userPermission = new ValidatedToken();
        List<Role> roles = new ArrayList<Role>();
        userPermission.setRoles(roles);
        Role role = new Role();
        role.setId(oauthManager.getGrantedRole());
        role.setName(oauthManager.getGrantedRole());
        roles.add(role);
        // mocks
        ResponseHandler handler = new ResponseHandler();
        ReflectionTestUtils.setField(handler, "responseContent", parseRequest2JSON(userPermission));
        ReflectionTestUtils.setField(handler, "status", HttpStatus.SC_OK);
        ReflectionTestUtils.setField(handler, "content", true);
        ReflectionTestUtils.setField(oauthManager, "handler", handler);
        // mock response
        Mockito.when(mHttpResponseMock.getStatusLine()).thenReturn(mStatusLineMock);
        Mockito.when(mStatusLineMock.getStatusCode()).thenReturn(HttpStatus.SC_OK);
        Mockito.when(mockHttpPost.getURI()).thenReturn(new URI("http://test.com"));
        Mockito.when(httpClient.getConnectionManager()).thenReturn(conectionManager);
        Mockito.when(httpClient.execute(Matchers.any(HttpRequestBase.class),
            Matchers.any(ResponseHandler.class))).thenAnswer(
            new Answer<HttpResponse>() {
                @Override
                public HttpResponse answer(InvocationOnMock invocation) throws IOException {
                    return mHttpResponseMock;
                }
            });
        // set client.
        ReflectionTestUtils.setField(oauthManager, "httpclient", httpClient);
        // nothing happends
        oauthManager.checkUserPermisions(session);
        // Expected exception
        // roles.get(0).setId("other");
        // roles.get(0).setName("other");
        userPermission.setRoles(new ArrayList<Role>());
        ReflectionTestUtils.setField(handler, "responseContent", parseRequest2JSON(userPermission));
        thrown.expect(Exception.class);
        oauthManager.checkUserPermisions(session);
    }

    /**
     * 
     */
    @Test
    public void checkAuthenticationTokenTest() throws Exception {
        OauthManagerTest.log.debug("Into checkAuthenticationTokenTest");
        // request object
        OauthLoginWebSessionData session = new OauthLoginWebSessionData();
        session.setAccessToken("accessToken");
        // response object
        ValidatedToken userPermission = new ValidatedToken();
        userPermission.setEmail("email");
        List<Role> roles = new ArrayList<Role>();
        userPermission.setRoles(roles);
        Role role = new Role();
        role.setId(oauthManager.getGrantedRole());
        role.setName(oauthManager.getGrantedRole());
        roles.add(role);
        // mocks
        ResponseHandler handler = new ResponseHandler();
        ReflectionTestUtils.setField(handler, "responseContent", parseRequest2JSON(userPermission));
        ReflectionTestUtils.setField(handler, "status", HttpStatus.SC_OK);
        ReflectionTestUtils.setField(handler, "content", true);
        ReflectionTestUtils.setField(oauthManager, "handler", handler);
        // httpResponse mock
        Mockito.when(mHttpResponseMock.getStatusLine()).thenReturn(mStatusLineMock);
        Mockito.when(mockHttpPost.getURI()).thenReturn(new URI("http://test.com"));
        Mockito.when(httpClient.getConnectionManager()).thenReturn(conectionManager);
        Mockito.when(httpClient.execute(Matchers.any(HttpRequestBase.class),
            Matchers.any(ResponseHandler.class))).thenAnswer(
            new Answer<HttpResponse>() {
                @Override
                public HttpResponse answer(InvocationOnMock invocation) throws IOException {
                    return mHttpResponseMock;
                }
            });
        // set client.
        ReflectionTestUtils.setField(oauthManager, "httpclient", httpClient);
        // call
        ValidatedToken validatedToken = oauthManager.checkAuthenticationToken("userToken");
        Assert.assertEquals("email", validatedToken.getEmail());
        // Expected exception
        thrown.expect(RSSException.class);
        thrown.expectMessage("X-Auth-Token header is required");
        oauthManager.checkAuthenticationToken("");
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void checkNoAuthenticationTokenTest() throws Exception {
        ReflectionTestUtils.setField(oauthManager, "useOauth", false);
        ValidatedToken validatedToken = oauthManager.checkAuthenticationToken("userToken");
        Assert.assertNull(validatedToken);
        ReflectionTestUtils.setField(oauthManager, "useOauth", true);
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void getClientIdApplicationsTest() throws Exception {
        OauthManagerTest.log.debug("Into getClientIdApplicationsTest");
        // response object
        ApplicationInfo[] applicationsResponse = new ApplicationInfo[1];
        ApplicationInfo application = new ApplicationInfo();
        applicationsResponse[0] = application;
        application.setDescription("description");
        application.setId("id");
        // mocks
        ResponseHandler handler = new ResponseHandler();
        ReflectionTestUtils.setField(handler, "responseContent", parseRequest2JSON(applicationsResponse));
        ReflectionTestUtils.setField(handler, "status", HttpStatus.SC_OK);
        ReflectionTestUtils.setField(handler, "content", true);
        ReflectionTestUtils.setField(oauthManager, "handler", handler);
        // mock response
        Mockito.when(mHttpResponseMock.getStatusLine()).thenReturn(mStatusLineMock);
        Mockito.when(mockHttpPost.getURI()).thenReturn(new URI("http://test.com"));
        Mockito.when(httpClient.getConnectionManager()).thenReturn(conectionManager);
        Mockito.when(httpClient.execute(Matchers.any(HttpRequestBase.class),
            Matchers.any(ResponseHandler.class))).thenAnswer(
            new Answer<HttpResponse>() {
                @Override
                public HttpResponse answer(InvocationOnMock invocation) throws IOException {
                    return mHttpResponseMock;
                }
            });
        // set client.
        ReflectionTestUtils.setField(oauthManager, "httpclient", httpClient);
        ApplicationInfo[] aplications = oauthManager.getClientIdApplications("userToken", "actorId");
        Assert.assertEquals(applicationsResponse[0].getId(), aplications[0].getId());
        // Expected exception
        ReflectionTestUtils.setField(handler, "status", HttpStatus.SC_FORBIDDEN);
        thrown.expect(RSSException.class);
        oauthManager.getClientIdApplications("userToken", "actorId");
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void getClientIdApplicationsTestNoContent() throws Exception {
        OauthManagerTest.log.debug("Into getClientIdApplicationsTestNoContent");
        // mocks
        ResponseHandler handler = new ResponseHandler();
        ReflectionTestUtils.setField(handler, "status", HttpStatus.SC_FORBIDDEN);
        ReflectionTestUtils.setField(handler, "content", false);
        ReflectionTestUtils.setField(oauthManager, "handler", handler);
        // mock response
        Mockito.when(mHttpResponseMock.getStatusLine()).thenReturn(mStatusLineMock);
        Mockito.when(mockHttpPost.getURI()).thenReturn(new URI("http://test.com"));
        Mockito.when(httpClient.getConnectionManager()).thenReturn(conectionManager);
        Mockito.when(httpClient.execute(Matchers.any(HttpRequestBase.class),
            Matchers.any(ResponseHandler.class))).thenAnswer(
            new Answer<HttpResponse>() {
                @Override
                public HttpResponse answer(InvocationOnMock invocation) throws IOException {
                    return mHttpResponseMock;
                }
            });
        // set client.
        ReflectionTestUtils.setField(oauthManager, "httpclient", httpClient);
        // Expected exception
        thrown.expect(Exception.class);
        oauthManager.getClientIdApplications("userToken", "actorId");
    }

    /**
     * 
     * @param userToken
     * @param actorId
     * @throws Exception
     */
    @Test
    public void getClientIdApplicationsTestError() throws Exception {
        OauthManagerTest.log.debug("Into getClientIdApplicationsTest");
        // mocks
        ResponseHandler handler = new ResponseHandler();
        ReflectionTestUtils.setField(handler, "responseContent", "error");
        ReflectionTestUtils.setField(handler, "status", HttpStatus.SC_OK);
        ReflectionTestUtils.setField(handler, "content", true);
        ReflectionTestUtils.setField(oauthManager, "handler", handler);
        // mock response
        Mockito.when(mHttpResponseMock.getStatusLine()).thenReturn(mStatusLineMock);
        Mockito.when(mockHttpPost.getURI()).thenReturn(new URI("http://test.com"));
        Mockito.when(httpClient.getConnectionManager()).thenReturn(conectionManager);
        Mockito.when(httpClient.execute(Matchers.any(HttpRequestBase.class),
            Matchers.any(ResponseHandler.class))).thenAnswer(
            new Answer<HttpResponse>() {
                @Override
                public HttpResponse answer(InvocationOnMock invocation) throws IOException {
                    return mHttpResponseMock;
                }
            });
        // set client.
        ReflectionTestUtils.setField(oauthManager, "httpclient", httpClient);
        // Expected exception
        thrown.expect(Exception.class);
        oauthManager.getClientIdApplications("userToken", "actorId");
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void getUserInfoTest() throws Exception {
        OauthManagerTest.log.debug("Into getUserInfoTest");
        // mocks
        ResponseHandler handler = new ResponseHandler();
        ReflectionTestUtils.setField(handler, "responseContent", "Error");
        ReflectionTestUtils.setField(handler, "status", HttpStatus.SC_OK);
        ReflectionTestUtils.setField(handler, "content", true);
        ReflectionTestUtils.setField(oauthManager, "handler", handler);
        // httpResponse mock
        Mockito.when(mHttpResponseMock.getStatusLine()).thenReturn(mStatusLineMock);
        Mockito.when(mockHttpPost.getURI()).thenReturn(new URI("http://test.com"));
        Mockito.when(httpClient.getConnectionManager()).thenReturn(conectionManager);
        Mockito.when(httpClient.execute(Matchers.any(HttpRequestBase.class),
            Matchers.any(ResponseHandler.class))).thenAnswer(
            new Answer<HttpResponse>() {
                @Override
                public HttpResponse answer(InvocationOnMock invocation) throws IOException {
                    return mHttpResponseMock;
                }
            });
        // set client.
        ReflectionTestUtils.setField(oauthManager, "httpclient", httpClient);
        // Expected exception
        thrown.expect(Exception.class);
        // call
        oauthManager.getUserInfo("userToken");

    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void getUserInfoTestNocontent() throws Exception {
        OauthManagerTest.log.debug("Into getUserInfoTest");
        // mocks
        ResponseHandler handler = new ResponseHandler();
        ReflectionTestUtils.setField(handler, "responseContent", "Error");
        ReflectionTestUtils.setField(handler, "status", HttpStatus.SC_FORBIDDEN);
        ReflectionTestUtils.setField(handler, "content", false);
        ReflectionTestUtils.setField(oauthManager, "handler", handler);
        // httpResponse mock
        Mockito.when(mHttpResponseMock.getStatusLine()).thenReturn(mStatusLineMock);
        Mockito.when(mockHttpPost.getURI()).thenReturn(new URI("http://test.com"));
        Mockito.when(httpClient.getConnectionManager()).thenReturn(conectionManager);
        Mockito.when(httpClient.execute(Matchers.any(HttpRequestBase.class),
            Matchers.any(ResponseHandler.class))).thenAnswer(
            new Answer<HttpResponse>() {
                @Override
                public HttpResponse answer(InvocationOnMock invocation) throws IOException {
                    return mHttpResponseMock;
                }
            });
        // set client.
        ReflectionTestUtils.setField(oauthManager, "httpclient", httpClient);
        // Expected exception
        thrown.expect(Exception.class);
        // call
        oauthManager.getUserInfo("userToken");
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void getUserInfoTestErrorContent() throws Exception {
        OauthManagerTest.log.debug("Into getUserInfoTest");
        // mocks
        ResponseHandler handler = new ResponseHandler();
        ReflectionTestUtils.setField(handler, "responseContent", "Error");
        ReflectionTestUtils.setField(handler, "status", HttpStatus.SC_FORBIDDEN);
        ReflectionTestUtils.setField(handler, "content", true);
        ReflectionTestUtils.setField(oauthManager, "handler", handler);
        // httpResponse mock
        Mockito.when(mHttpResponseMock.getStatusLine()).thenReturn(mStatusLineMock);
        Mockito.when(mockHttpPost.getURI()).thenReturn(new URI("http://test.com"));
        Mockito.when(httpClient.getConnectionManager()).thenReturn(conectionManager);
        Mockito.when(httpClient.execute(Matchers.any(HttpRequestBase.class),
            Matchers.any(ResponseHandler.class))).thenAnswer(
            new Answer<HttpResponse>() {
                @Override
                public HttpResponse answer(InvocationOnMock invocation) throws IOException {
                    return mHttpResponseMock;
                }
            });
        // set client.
        ReflectionTestUtils.setField(oauthManager, "httpclient", httpClient);
        // Expected exception
        thrown.expect(Exception.class);
        // call
        oauthManager.getUserInfo("userToken");
    }

    /**
     * Parse request object to JSON.
     * 
     * @param request
     * @return JSON String
     * @throws GRETAException
     */
    private String parseRequest2JSON(Object request) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ByteArrayOutputStream baosRequest = new ByteArrayOutputStream();
        try {
            mapper.writeValue(baosRequest, request);
            return baosRequest.toString();
        } catch (Exception e) {
            OauthManagerTest.log.error("Error:" + e.toString());
            throw new Exception("Wrong request " + request + ".Error: " + e.getMessage());
        } finally {
            if (baosRequest != null) {
                try {
                    baosRequest.close();
                } catch (IOException e) {
                    OauthManagerTest.log.error("Error closing ByteOutputStream.Message:" + e.getMessage());
                }
            }
        }
    }

}
