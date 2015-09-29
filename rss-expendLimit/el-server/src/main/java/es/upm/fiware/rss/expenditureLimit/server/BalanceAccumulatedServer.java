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

package es.upm.fiware.rss.expenditureLimit.server;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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

import es.upm.fiware.rss.common.properties.AppProperties;
import es.upm.fiware.rss.expenditureControl.api.AccumsExpend;
import es.upm.fiware.rss.expenditureControl.api.ExpendControl;
import es.upm.fiware.rss.expenditureLimit.server.common.ExpenditureLimitCommon;
import es.upm.fiware.rss.expenditureLimit.server.service.BalanceAccumulateManager;
import es.upm.fiware.rss.expenditureLimit.server.service.ExpenditureLimitDataChecker;

/**
 * 
 * 
 */
@WebService(serviceName = "BalanceAccumulated", name = "balanceAccumulated")
@Produces("application/json")
public class BalanceAccumulatedServer {

    /**
     * Variable to print the trace.
     */
    private static Logger logger = LoggerFactory.getLogger(BalanceAccumulatedServer.class);
    /**
         * 
         */
    public static final String RESOURCE = "/balanceAccumulated";

    @Autowired
    private BalanceAccumulateManager balanceAccumulateManager;
    @Autowired
    private ExpenditureLimitDataChecker checker;
    /**
         * 
         */

    /**
     * Information about the context of the application.
     */
    @Context
    private static UriInfo ui;

    /**
     * Get the expenditure control for the user given.
     * 
     * @param urlEndUserId
     * @param service
     * @param aggregator
     * @param appProvider
     * @param currency
     * @param type
     * @return
     * @throws Exception
     */
    @WebMethod
    @GET
    @Path("/{endUserId}")
    public Response getUserAccumulated(
            @PathParam("endUserId") final String urlEndUserId,
            @QueryParam("service") String service,
            @QueryParam("aggregator") String aggregator,
            @QueryParam("appProvider") String appProvider,
            @QueryParam("currency") String currency,
            @QueryParam("type") String type)
        throws Exception {

        BalanceAccumulatedServer.logger.debug("Into getUserAccumulated() with endUserId="
            + urlEndUserId);

        // check security
        checker.checkCurrentUserPermissions();

        // Call service
        AccumsExpend expLimits = balanceAccumulateManager.
            getUserAccumulated(urlEndUserId, service, aggregator, appProvider, currency, type);

        ResponseBuilder rb = Response.status(Response.Status.OK.getStatusCode());

        rb.entity(expLimits);
        return rb.build();
    }

    /**
     * Check user balance.
     * 
     * @param urlEndUserId
     * @param expendControl
     * @return
     */
    @WebMethod
    @POST
    @Path("/{endUserId}")
    @Consumes("application/json")
    public Response checkUserBalance(
            @PathParam("endUserId") final String urlEndUserId,
            ExpendControl expendControl)
        throws Exception {

        BalanceAccumulatedServer.logger.debug("Into createModifUserExpLimit method");
        // check security
        checker.checkCurrentUserPermissions();

        // Call service
        AccumsExpend expInfoBean = balanceAccumulateManager.checkUserBalance(urlEndUserId, expendControl);

        String resourceURL = ExpenditureLimitCommon.getResourceUrl(BalanceAccumulatedServer.ui,
            urlEndUserId, ExpenditureLimitServer.RESOURCE);

        // Building response
        ResponseBuilder rb = Response.status(Response.Status.CREATED.getStatusCode());
        rb.header("Location", resourceURL);
        rb.entity(expInfoBean);
        return rb.build();
    }

    /**
     * Update Accumulated.
     * 
     * @param urlEndUserId
     * @param expendControl
     * @return
     * @throws Exception
     */
    @WebMethod
    @PUT
    @Path("/{endUserId}")
    @Consumes("application/json")
    public Response updateUserAccumulated(
            @PathParam("endUserId") final String urlEndUserId,
            ExpendControl expendControl)
        throws Exception {

        BalanceAccumulatedServer.logger.debug("Into updateUserAccumulated method");

        // check security
        checker.checkCurrentUserPermissions();

        // Call service
        AccumsExpend expInfoBean = balanceAccumulateManager.updateUserAccumulated(urlEndUserId, expendControl);

        String resourceURL = ExpenditureLimitCommon.getResourceUrl(BalanceAccumulatedServer.ui,
            urlEndUserId, ExpenditureLimitServer.RESOURCE);

        // Building response
        ResponseBuilder rb = Response.status(Response.Status.CREATED.getStatusCode());
        rb.header("Location", resourceURL);
        rb.entity(expInfoBean);
        return rb.build();
    }

    /**
     * Delete accumulated.
     * 
     * @param urlEndUserId
     * @param expendControl
     * @return
     * @throws Exception
     */
    @WebMethod
    @PUT
    @Path("/{endUserId}/reset")
    @Consumes("application/json")
    public Response deleteUserAccumulated(
            @PathParam("endUserId") final String urlEndUserId,
            ExpendControl expendControl)
        throws Exception {

        BalanceAccumulatedServer.logger.debug("Into deleteUserAccumulated method");
        // check security
        checker.checkCurrentUserPermissions();

        balanceAccumulateManager.deleteUserAccumulated(urlEndUserId, expendControl);

        // Building response
        ResponseBuilder rb = Response.status(Response.Status.OK.getStatusCode());
        return rb.build();
    }

}
