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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import es.tid.fiware.rss.dao.DbeAggregatorDao;
import es.tid.fiware.rss.exception.RSSException;
import es.tid.fiware.rss.exception.UNICAExceptionType;
import es.tid.fiware.rss.model.DbeAggregator;
import es.tid.fiware.rss.oauth.model.OauthLoginWebSessionData;
import es.tid.fiware.rss.oauth.service.OauthManager;

public class SessionMultiActionController implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(SessionMultiActionController.class);

    private OauthLoginWebSessionData session;
    @Autowired
    private OauthManager oauth;
    @Autowired
    private DbeAggregatorDao dbeAggregatorDao;
    public static final String USER_SESSION = "userSession";

    @Override
    public boolean preHandle(HttpServletRequest request,
        HttpServletResponse response, Object handler) throws Exception {
        // Use login from idm?

        if (oauth.hasExternalLogin()) {
            try {
                SessionMultiActionController.log.info(">>>>>>>>>>>>>>>> Session Controller <<<<<<<<<<<<<<<<<");

                String code = request.getParameter("code");
                session = (OauthLoginWebSessionData) getSessionAttribute(request,
                    SessionMultiActionController.USER_SESSION);
                if (session != null) {
                    SessionMultiActionController.log.info(">>>>>> Provider Session : " + session.getExpiresIn());
                } else if (null != code) {
                    // get token from provider.
                    session = oauth.getToken(code);
                    // Check user permissions
                    oauth.checkUserPermisions(session);
                    if (!oauth.getGrantedRole().equalsIgnoreCase(session.getRole())) {
                        // If without role with total access --> get Provider Id
                        DbeAggregator dbeAggregator = dbeAggregatorDao.getById(session.getEmail());
                        if (null != dbeAggregator) {
                            session.setAggregatorId(dbeAggregator.getTxEmail());
                        } else {
                            SessionMultiActionController.log.debug("Access not granted for this user.");
                            String[] args = {"Restricted Area"};
                            throw new RSSException(UNICAExceptionType.CONSUMER_LOGIN_NOT_ALLOWED, args);
                        }
                    }
                    // Set provider Role
                    setSessionAttribute(request, SessionMultiActionController.USER_SESSION, session);
                } else {
                    SessionMultiActionController.log.info(">>>>>>>>>>>>>>>> No Session <<<<<<<<<<<<<<<<<");
                    response.setHeader("Authorization", oauth.buildHeader());
                    response.setHeader("Content-Type", "application/x-www-form-urlencoded");
                    response.sendRedirect(oauth.getAuthorizationUrl());
                    return false;
                }
                return true;
            } catch (RSSException ex) {
                SessionMultiActionController.log.info(">>>>>>>>>>>>>>>> No valid user <<<<<<<<<<<<<<<<<");
                response.sendRedirect(request.getContextPath() + "?validUser=false");
            } catch (Exception e) {
                SessionMultiActionController.log.error("Error:" + e.toString());
                response.sendRedirect(request.getContextPath() + "?error=" + e.toString());
            }
        } else {
            session = new OauthLoginWebSessionData();
            session.setRole(oauth.getGrantedRole());
            setSessionAttribute(request, SessionMultiActionController.USER_SESSION, session);
        }
        return true;
    }

    private Object getSessionAttribute(HttpServletRequest req, String name) {
        return req.getSession().getAttribute(name);
    }

    private void setSessionAttribute(HttpServletRequest req, String name, Object object) {
        req.getSession().setAttribute(name, object);
    }

    @Override
    public void postHandle(HttpServletRequest request,
        HttpServletResponse response, Object handler,
        ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
        HttpServletResponse response, Object handler, Exception ex)
        throws Exception {
    }

}
