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

package es.upm.fiware.rss.ws;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import es.upm.fiware.rss.model.RSSModel;
import es.upm.fiware.rss.service.RSSModelsManager;
import es.upm.fiware.rss.exception.RSSException;
import es.upm.fiware.rss.exception.UNICAExceptionType;
import es.upm.fiware.rss.model.RSUser;
import es.upm.fiware.rss.service.UserManager;

/**
 * 
 * 
 */
@WebService(serviceName = "RSSModelService", name = "RSSModelService")
@Produces("application/json")
@Consumes("application/json")
@Path("/")
public class RSSModelService {

    /**
     * Variable to print the trace.
     */
    private static Logger logger = LoggerFactory.getLogger(RSSModelService.class);
    /**
         * 
         */
    public static final String RESOURCE = "/rssModelManagement";

    @Autowired
    private RSSModelsManager rssModelsManager;
    /**
     * Oauth manager.
     */
    @Autowired
    private UserManager userManager;

    /**
     * Get Rss Models.
     * 
     * @param appProvider
     * @param productClass
     * @param aggregatorId
     * @return
     * @throws Exception
     */
    @WebMethod
    @GET
    @Path("/")
    public Response getRssModels(@QueryParam("appProviderId") String appProvider,
        @QueryParam("productClass") String productClass,
        @QueryParam("aggregatorId") String aggregatorId)
        throws Exception {

        RSSModelService.logger.debug("Into getRssModels()");

        RSUser user = userManager.getCurrentUser();
        String effectiveAggregator;

        if (userManager.isAdmin()) {
            effectiveAggregator = aggregatorId;
        } else if (null == aggregatorId || aggregatorId.equals(user.getEmail())){
            effectiveAggregator = user.getEmail();
        } else {
            String[] args = {"You are not allowed to retrieve RS models for the given aggregator"};
            throw new RSSException(UNICAExceptionType.NON_ALLOWED_OPERATION, args);
        }

        // Call service
        List<RSSModel> rssModels = rssModelsManager.getRssModels(effectiveAggregator, appProvider, productClass);

        // Response
        ResponseBuilder rb = Response.status(Response.Status.OK.getStatusCode());
        rb.entity(rssModels);
        return rb.build();
    }

    /**
     * Create Rss Model
     * 
     * @param rssModel
     * @return
     * @throws Exception
     */
    @WebMethod
    @POST
    @Path("/")
    @Consumes("application/json")
    public Response createRSSModel(RSSModel rssModel) throws Exception {
        RSSModelService.logger.debug("Into createRSSModel method");
        // check security
        RSUser user = userManager.getCurrentUser();

        // Validate that the user can create a RS model for the given aggregator
        if (!userManager.isAdmin() &&
                !rssModel.getAggregatorId().equals(user.getEmail())) {
            String[] args = {"You are not allowed to create a RS model for the given aggregatorId"};
            throw new RSSException(UNICAExceptionType.NON_ALLOWED_OPERATION, args);
        }
        // Call service
        RSSModel model = rssModelsManager.createRssModel(rssModel);
        // Building response
        ResponseBuilder rb = Response.status(Response.Status.CREATED.getStatusCode());
        rb.entity(model);
        return rb.build();
    }

    /**
     * Update Rss model
     * 
     * @param rssModel
     * @return
     * @throws Exception
     */
    @WebMethod
    @PUT
    @Path("/")
    @Consumes("application/json")
    public Response modifyRSSModel(RSSModel rssModel) throws Exception {
        RSSModelService.logger.debug("Into modifyRSSModel method");

        RSUser user = userManager.getCurrentUser();

        // Validate that the user can modify a RS model for the given aggregator
        if (!userManager.isAdmin() &&
                !rssModel.getAggregatorId().equals(user.getEmail())) {
            String[] args = {"You are not allowed to create a RS model for the given aggregatorId"};
            throw new RSSException(UNICAExceptionType.NON_ALLOWED_OPERATION, args);
        }

        // Call service
        RSSModel model = rssModelsManager.updateRssModel(rssModel);
        // Building response
        ResponseBuilder rb = Response.status(Response.Status.CREATED.getStatusCode());
        rb.entity(model);
        return rb.build();
    }

    /**
     * Delete Rss Models.
     * 
     * @param aggregatorId
     * @param appProvider
     * @param productClass
     * @return
     * @throws Exception
     */
    @WebMethod
    @DELETE
    @Path("/")
    @Consumes("application/json")
    public Response deleteRSSModel(
        @QueryParam("aggregatorId") String aggregatorId,
        @QueryParam("appProviderId") String appProvider,
        @QueryParam("productClass") String productClass) throws Exception {
        RSSModelService.logger.debug("Into deleteRSSModel method");
        // check security
        RSUser user = userManager.getCurrentUser();
        String effectiveAggregator;

        if (userManager.isAdmin()) {
            effectiveAggregator = aggregatorId;
        } else if (null == aggregatorId || aggregatorId.equals(user.getEmail())){
            effectiveAggregator = user.getEmail();
        } else {
            String[] args = {"You are not allowed to remove RS Models for the given aggregator"};
            throw new RSSException(UNICAExceptionType.NON_ALLOWED_OPERATION, args);
        }
        // Call service
        rssModelsManager.deleteRssModel(effectiveAggregator, appProvider, productClass);
        // Building response
        ResponseBuilder rb = Response.status(Response.Status.OK.getStatusCode());
        return rb.build();
    }

}
