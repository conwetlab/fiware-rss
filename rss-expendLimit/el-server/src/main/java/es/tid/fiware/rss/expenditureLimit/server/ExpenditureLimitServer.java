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

package es.tid.fiware.rss.expenditureLimit.server;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import es.tid.fiware.rss.common.properties.AppProperties;
import es.tid.fiware.rss.expenditureLimit.api.LimitGroupBean;
import es.tid.fiware.rss.expenditureLimit.api.UserExpenditureLimitInfoBean;
import es.tid.fiware.rss.expenditureLimit.server.common.ExpenditureLimitCommon;
import es.tid.fiware.rss.expenditureLimit.server.service.ExpenditureLimitDataChecker;
import es.tid.fiware.rss.expenditureLimit.server.service.ExpenditureLimitManager;

/**
 * 
 * 
 */
@WebService(serviceName = "LimitManagement", name = "limitManagement")
@Produces("application/json")
public class ExpenditureLimitServer {

    /**
     * Variable to print the trace.
     */
    private static Logger logger = LoggerFactory.getLogger(ExpenditureLimitServer.class);
    /**
     * 
     */
    public static final String RESOURCE = "/expenditureLimit/limitManagement";

    @Autowired
    private ExpenditureLimitManager expLimitManager;
    @Autowired
    private ExpenditureLimitDataChecker checker;
    /**
     * 
     */
    @Autowired
    @Qualifier(value = "appProperties")
    private AppProperties appProperties;
    /**
     * Information about the context of the application.
     */
    @Context
    private static UriInfo ui;

    /**
     * Get the expenditure limits for a provider given.
     * 
     * @param appProvider
     * @return
     * @throws Exception
     */
    @WebMethod
    @GET
    @Path("/{appProvider}")
    public Response getProviderExpLimits(@HeaderParam("X-Auth-Token") final String authToken,
        @PathParam("appProvider") final String appProvider,
        @QueryParam("service") String service, @QueryParam("currency") String currency, @QueryParam("type") String type)
        throws Exception {
        ExpenditureLimitServer.logger.debug("Into getProviderExpLimits() with appProvider=" + appProvider);
        // check security
        checker.checkAuthenticationToken(authToken);

        LimitGroupBean expLimits = expLimitManager
            .getGeneralProviderExpLimitsBean(appProvider, service, currency, type);

        ResponseBuilder rb = Response.status(Response.Status.OK.getStatusCode());

        rb.entity(expLimits);
        return rb.build();
    }

    /**
     * Create and update a expenditure control.
     * 
     * @param appProvider
     * @param expLimits
     * @return
     */
    @WebMethod
    @POST
    @Path("/{appProvider}")
    @Consumes("application/json")
    public Response createModifProviderExpLimit(@HeaderParam("X-Auth-Token") final String authToken,
        @PathParam("appProvider") final String appProvider,
        LimitGroupBean expLimits) throws Exception {

        ExpenditureLimitServer.logger.debug("Into createModifProviderExpLimit method");
        // check security
        checker.checkAuthenticationToken(authToken);

        LimitGroupBean expInfoBean = expLimitManager.storeGeneralProviderExpLimit(appProvider, expLimits);
        String resourceURL = ExpenditureLimitCommon.getResourceUrl(appProperties, ExpenditureLimitServer.ui,
            appProvider, ExpenditureLimitServer.RESOURCE);

        // Building response
        ResponseBuilder rb = Response.status(Response.Status.CREATED.getStatusCode());
        rb.header("Location", resourceURL);
        rb.entity(expInfoBean);
        return rb.build();
    }

    /**
     * Delete expenditure limits for a provider
     * 
     * @param appProvider
     * @param fields
     * @return
     * @throws Exception
     */
    @WebMethod
    @DELETE
    @Path("/{appProvider}")
    @Consumes("application/json")
    public Response deleteProviderExpLimits(@HeaderParam("X-Auth-Token") final String authToken,
        @PathParam("appProvider") final String appProvider,
        @QueryParam("service") String service, @QueryParam("currency") String currency,
        @QueryParam("type") String type) throws Exception {
        ExpenditureLimitServer.logger.debug("Into deleteUserExpCtrl method");
        // check security
        checker.checkAuthenticationToken(authToken);

        expLimitManager.deleteProviderLimits(appProvider, service, currency, type);

        // Building response
        ResponseBuilder rb = Response.status(Response.Status.OK.getStatusCode());
        return rb.build();
    }

    /**
     * Get the expenditure control for the user given.
     * 
     * @param appProvider
     * @param endUserId
     * @param service
     * @param currency
     * @param type
     * @return
     * @throws Exception
     */
    @WebMethod
    @GET
    @Path("/{appProvider}/{endUserId}")
    public Response getUserExpLimits(@HeaderParam("X-Auth-Token") final String authToken,
        @PathParam("appProvider") final String appProvider,
        @PathParam("endUserId") final String endUserId,
        @QueryParam("service") String service, @QueryParam("currency") String currency, @QueryParam("type") String type)
        throws Exception {
        ExpenditureLimitServer.logger.debug("Into getUserExpLimits() with endUserId=" + endUserId);
        // check security
        checker.checkAuthenticationToken(authToken);

        UserExpenditureLimitInfoBean expLimits = expLimitManager.getGeneralUserExpLimitsBean(endUserId, appProvider,
            service, currency, type);

        // build response
        ResponseBuilder rb = Response.status(Response.Status.OK.getStatusCode());
        rb.entity(expLimits);

        return rb.build();
    }

    /**
     * Create and update a expenditure limit.
     * 
     * @param appProvider
     * @param urlEndUserId
     * @return
     * @throws Exception
     */
    @WebMethod
    @POST
    @Path("/{appProvider}/{endUserId}")
    @Consumes("application/json")
    public Response createModifUserExpLimit(@HeaderParam("X-Auth-Token") final String authToken,
        @PathParam("appProvider") final String appProvider,
        @PathParam("endUserId") final String urlEndUserId, LimitGroupBean expLimits) throws Exception {
        ExpenditureLimitServer.logger.debug("Into createModifUserExpLimit method for user: " + urlEndUserId);
        // check security
        checker.checkAuthenticationToken(authToken);

        // Store directly the given
        UserExpenditureLimitInfoBean expInfoBean = expLimitManager.storeGeneralUserExpLimit(appProvider, urlEndUserId,
            expLimits);

        String resourceURL = ExpenditureLimitCommon.getResourceUrl(appProperties, ExpenditureLimitServer.ui,
            urlEndUserId, ExpenditureLimitServer.RESOURCE);

        // Building response
        ResponseBuilder rb = Response.status(Response.Status.CREATED.getStatusCode());
        rb.header("Location", resourceURL);
        rb.entity(expInfoBean);
        return rb.build();
    }

    /**
     * Delete the expenditure limits for an user.
     * 
     * @param appProvider
     * @param urlEndUserId
     * @param fields
     * @return
     * @throws Exception
     */
    @WebMethod
    @DELETE
    @Path("/{appProvider}/{endUserId}")
    @Consumes("application/json")
    public Response deleteUserExpCtrl(@HeaderParam("X-Auth-Token") final String authToken,
        @PathParam("appProvider") final String appProvider,
        @PathParam("endUserId") final String urlEndUserId,
        @QueryParam("service") String service, @QueryParam("currency") String currency,
        @QueryParam("type") String type) throws Exception {
        ExpenditureLimitServer.logger.debug("Into deleteUserExpCtrl method");
        // check security
        checker.checkAuthenticationToken(authToken);

        expLimitManager.deleteUserLmits(appProvider, urlEndUserId, service, currency, type);

        // Building response
        ResponseBuilder rb = Response.status(Response.Status.OK.getStatusCode());
        return rb.build();
    }

}
