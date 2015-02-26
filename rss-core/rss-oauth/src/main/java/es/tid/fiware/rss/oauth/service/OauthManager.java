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

package es.tid.fiware.rss.oauth.service;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.tid.fiware.rss.common.properties.AppProperties;
import es.tid.fiware.rss.exception.RSSException;
import es.tid.fiware.rss.exception.UNICAExceptionType;
import es.tid.fiware.rss.oauth.model.ApplicationInfo;
import es.tid.fiware.rss.oauth.model.OauthLoginWebSessionData;
import es.tid.fiware.rss.oauth.model.Role;
import es.tid.fiware.rss.oauth.model.ValidatedToken;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OauthManager {
    private static final Logger log = LoggerFactory.getLogger(OauthManager.class);
    /**
     * private properties.
     */
    private String clientId;
    private String clientSecret;
    private String baseSite;
    private String authorizeUrl;
    private String accessTokenUrl;
    private String callbackURL;
    private String externalLogin;
    private String useOauth = "";
    private String userInfoUrl;
    private String getApplicationsUrl;
    private String grantedRole;
    private final String authMethod = "Basic ";
    @Autowired
    @Qualifier(value = "oauthProperties")
    private AppProperties oauthProperties;
    /**
     * 
     */
    private final DefaultHttpClient httpclient = new DefaultHttpClient();
    /**
     * handler that manages responses
     */
    private ResponseHandler handler = new ResponseHandler();

    /**
     * Read needed properties from file.
     */
    @PostConstruct
    private void readProperties() throws Exception {
        externalLogin = oauthProperties.getProperty("config.externalLogin");
        baseSite = oauthProperties.getProperty("config.baseUrl");
        clientId = oauthProperties.getProperty("config.client_id");
        clientSecret = oauthProperties.getProperty("config.client_secret");
        authorizeUrl = oauthProperties.getProperty("config.authorizeUrl");
        accessTokenUrl = oauthProperties.getProperty("config.accessTokenUrl");
        callbackURL = oauthProperties.getProperty("config.callbackURL");
        userInfoUrl = oauthProperties.getProperty("config.userInfoUrl");
        grantedRole = oauthProperties.getProperty("config.grantedRole");
        getApplicationsUrl = oauthProperties.getProperty("config.getApplications");
        useOauth = oauthProperties.getProperty("config.useOauth");
        // avoid certificate checking for problems regarding with them.
        SSLContext ctx = SSLContext.getInstance("TLS");
        X509TrustManager tm = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        ctx.init(null, new TrustManager[]{tm}, null);
        SSLSocketFactory ssf = new SSLSocketFactory(ctx);
        ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        httpclient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", ssf, 443));
    }

    /**
     * Destroy connection.
     */
    @PreDestroy
    private void destroyConnection() {
        httpclient.getConnectionManager().shutdown();
    }

    /**
     * @return the grantedRole
     */
    public String getGrantedRole() {
        return grantedRole;
    }

    /**
     * Get authentication header.
     * 
     * @return
     */
    public String buildHeader() {
        String pair = clientId + ":" + clientSecret;
        String encoded = new String(Base64.encodeBase64(pair.getBytes()));
        String authentication = authMethod + encoded;
        return authentication;
    }

    /**
     * Check if external login is allowed.
     * 
     * @return
     */
    public boolean hasExternalLogin() {
        if ("Y".equalsIgnoreCase(externalLogin)) {
            return true;
        }
        return false;
    }

    /**
     * Get authorization Url.
     * 
     * @return
     */
    public String getAuthorizationUrl() {
        return this.baseSite + this.authorizeUrl + "?response_type=code&client_id=" + this.clientId
            + "&state=xyz&redirect_uri=" + this.callbackURL;
    }

    /**
     * Get token url.
     * 
     * @param code
     * @return
     */
    public String getTokenUrl(String code) {
        return this.baseSite + this.accessTokenUrl + "?grant_type=authorization_code&code=" + code
            + "&redirect_uri=" + this.callbackURL;
    }

    /**
     * Get user Info Url.
     * 
     * @param token
     * @return
     */
    public String getInfoUserUrl(String token) {
        return this.baseSite + this.userInfoUrl + token;
    }

    /**
     * Get user Info Url.
     * 
     * @param actorId
     * @param token
     * @return
     */
    public String getAplicationsUrl(String actorId, String token) {
        return this.baseSite + this.getApplicationsUrl + actorId
            + "&access_token=" + token;
    }

    /**
     * Get token data.
     * 
     * @param code
     *            Code to build url.
     * @throws Exception
     */
    public OauthLoginWebSessionData getToken(String code) throws Exception {
        OauthManager.log.debug("getToken method");
        // grant_type=authorization_code&code=SplxlOBeZQQYbYS6WxSbIA
        // &redirect_uri=https%3A%2F%2Fclient%2Eexample%2Ecom%2Fcallback_url
        try {
            OauthManager.log.debug("Code:" + code);
            HttpPost httppost = new HttpPost(getTokenUrl(code));
            httppost.addHeader("Authorization", buildHeader());
            httppost.addHeader("Content-Type", "application/x-www-form-urlencoded");

            OauthManager.log.debug("executing request" + httppost.getRequestLine());
            // send request
            HttpResponse response = httpclient.execute(httppost, handler);
            OauthManager.log.debug("----------------------------------------");
            OauthManager.log.debug(response.getStatusLine().toString());
            if (handler.hasContent()) {
                OauthManager.log.debug("----------------------------------------");
                OauthManager.log.debug("Response content:");
                OauthManager.log.debug(handler.getResponseContent());
                if (handler.getStatus() != 200) {
                    OauthManager.log.error("Error Status different to 200 received " + handler.getResponseContent());
                    throw new RSSException(handler.getResponseContent());
                }
                ObjectMapper mapper = new ObjectMapper();
                /*
                 * {
                 * "access_token":"2YotnFZFEjr1zCsicMWpAA",
                 * "token_type":"bearer",
                 * "expires_in":3600,
                 * "refresh_token":"tGzv3JOkF0XG5Qx2TlKWIA",
                 * }
                 */
                OauthLoginWebSessionData session = mapper.readValue(handler.getResponseContent(),
                    OauthLoginWebSessionData.class);
                return session;
            } else if (handler.getStatus() != 200) {
                OauthManager.log.error("Error Status different to 200 received " + handler.getResponseContent());
                throw new RSSException(handler.getResponseContent());
            }
            return null;
        } catch (RSSException ex) {
            throw ex;
        } catch (Exception e) {
            OauthManager.log.error("Error obtaining token:" + e.toString(), e);
            throw new Exception("Error obtaining token: " + e.toString());
        } finally {
            httpclient.getConnectionManager().closeExpiredConnections();
        }

    }

    /**
     * Check if the user has permissions to access to web.
     * 
     * @param session
     * @throws Exception
     */
    public void checkUserPermisions(OauthLoginWebSessionData session) throws Exception {
        OauthManager.log.debug("getUserInfo method");
        ValidatedToken userPermission = getUserInfo(session.getAccessToken());
        session.setEmail(userPermission.getEmail());
        if (null != userPermission.getRoles() && userPermission.getRoles().size() > 0) {
            for (Role role : userPermission.getRoles()) {
                if (grantedRole.equalsIgnoreCase(role.getName())) {
                    session.setRole(role.getName());
                    break;
                }
            }
        } else {
            // no roles --> not allowed to enter
            OauthManager.log.debug("Access not granted for this user.");
            String[] args = {"Restricted Area"};
            throw new RSSException(UNICAExceptionType.CONSUMER_LOGIN_NOT_ALLOWED, args);
        }
    }

    /**
     * Get user Info.
     * 
     * @param token
     * @return
     * @throws Exception
     */
    public ValidatedToken getUserInfo(String token) throws Exception {
        OauthManager.log.debug("getUserInfo method. Token: {}", token);
        try {
            HttpGet httpget = new HttpGet(getInfoUserUrl(token));
            OauthManager.log.debug("executing request" + httpget.getRequestLine());
            // send request
            HttpResponse response = httpclient.execute(httpget, handler);
            OauthManager.log.debug("----------------------------------------");
            OauthManager.log.debug(response.getStatusLine().toString());
            if (handler.hasContent()) {
                OauthManager.log.debug("----------------------------------------");
                OauthManager.log.debug("Response content:");
                OauthManager.log.debug(handler.getResponseContent());
                if (handler.getStatus() != 200) {
                    OauthManager.log.error("Error Status different to 200 received " + handler.getResponseContent());
                    throw new RSSException(handler.getResponseContent());
                }
                ObjectMapper mapper = new ObjectMapper();
                ValidatedToken session = mapper.readValue(handler.getResponseContent(), ValidatedToken.class);
                return session;
            } else if (handler.getStatus() != 200) {
                OauthManager.log.error("Error Status different to 200 received " + handler.getResponseContent());
                throw new RSSException(handler.getResponseContent());
            }
            return null;
        } catch (RSSException ex) {
            throw ex;
        } catch (Exception e) {
            OauthManager.log.error("Error obtaining token:" + e.toString(), e);
            throw new Exception("Error obtaining token: " + e.toString());
        } finally {
            httpclient.getConnectionManager().closeExpiredConnections();
        }
    }

    /**
     * Get applications by client id.
     * 
     * @param userToken
     * @param actorId
     * @return
     * @throws Exception
     */
    public ApplicationInfo[] getClientIdApplications(String userToken, String actorId) throws Exception {
        OauthManager.log.debug("getClientIdApplications method. Token: {}", userToken);
        try {
            HttpGet httpget = new HttpGet(getAplicationsUrl(actorId, userToken));
            OauthManager.log.debug("executing request" + httpget.getRequestLine());
            // send request
            HttpResponse response = httpclient.execute(httpget, handler);
            OauthManager.log.debug("----------------------------------------");
            OauthManager.log.debug(response.getStatusLine().toString());
            if (handler.hasContent()) {
                OauthManager.log.debug("----------------------------------------");
                OauthManager.log.debug("Response content:");
                if (handler.getStatus() != 200) {
                    OauthManager.log.error("Error Status different to 200 received " + handler.getResponseContent());
                    throw new RSSException(handler.getResponseContent());
                }
                OauthManager.log.debug(handler.getResponseContent());
                ObjectMapper mapper = new ObjectMapper();
                ApplicationInfo[] applications = mapper
                    .readValue(handler.getResponseContent(), ApplicationInfo[].class);
                return applications;
            } else if (handler.getStatus() != 200) {
                OauthManager.log.error("Error Status different to 200 received " + handler.getResponseContent());
                throw new Exception(handler.getResponseContent());
            }
            return null;
        } catch (RSSException ex) {
            throw ex;
        } catch (Exception e) {
            OauthManager.log.error("Error obtaining information:" + e.toString(), e);
            throw new Exception("Error obtaining information: " + e.toString());
        } finally {
            httpclient.getConnectionManager().closeExpiredConnections();
        }

    }

    /**
     * Check if the user has sent a valid token.
     * 
     * @param userToken
     * @throws Exception
     */
    public ValidatedToken checkAuthenticationToken(String userToken) throws Exception {
        OauthManager.log.debug("Into checkAuthenticationToken. Token:" + userToken);
        if ("Y".equalsIgnoreCase(useOauth)) {
            if (null != userToken && userToken.length() > 0) {
                // Get user Info
                return getUserInfo(userToken);
                /*
                 * ValidatedToken userInfo = getUserInfo(userToken);
                 * // check user token
                 * ApplicationInfo[] applications = getClientIdApplications(userToken, userInfo.getActorId());
                 * // Finally check if the user has access to the application.
                 * checkApplictionIds(applications);
                 */
            } else {
                String[] args = {"X-Auth-Token header is required"};
                throw new RSSException(UNICAExceptionType.INVALID_OAUTH_TOKEN, args);
            }
        }
        return null;
    }

    /**
     * Check the existence of application Id.
     * 
     * @param applications
     */
    public void checkApplictionIds(ApplicationInfo[] applications) throws Exception {
        boolean found = false;
        if (null != applications && applications.length > 0) {
            for (ApplicationInfo application : applications) {
                if (application.getId().equalsIgnoreCase(this.clientId)) {
                    found = true;
                    break;
                }
            }
        }
        if (!found) {
            String[] args = {"User has not permission"};
            throw new RSSException(UNICAExceptionType.INVALID_OAUTH_TOKEN, args);
        }

    }

}
