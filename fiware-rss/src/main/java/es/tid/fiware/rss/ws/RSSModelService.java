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

package es.tid.fiware.rss.ws;

import es.tid.fiware.rss.exception.RSSException;
import es.tid.fiware.rss.exception.UNICAExceptionType;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
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

import es.tid.fiware.rss.model.RSSModel;
import es.tid.fiware.rss.oauth.model.ValidatedToken;
import es.tid.fiware.rss.oauth.service.OauthManager;
import es.tid.fiware.rss.service.RSSModelsManager;

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
    private OauthManager oauthManager;

    /**
     * Get Rss Models.
     * 
     * @param authToken
     * @param appProvider
     * @param productClass
     * @return
     * @throws Exception
     */
    @WebMethod
    @GET
    @Path("/")
    public Response getRssModels(@HeaderParam("X-Auth-Token") final String authToken,
        @QueryParam("appProviderId") String appProvider,
        @QueryParam("productClass") String productClass)
        throws Exception {
        RSSModelService.logger.debug("Into getRssModels()");
        // check security
        ValidatedToken token = oauthManager.checkAuthenticationToken(authToken);
        // Call service
        List<RSSModel> rssModels = rssModelsManager.getRssModels(token.getEmail(), appProvider, productClass);

        // Response
        ResponseBuilder rb = Response.status(Response.Status.OK.getStatusCode());
        rb.entity(rssModels);
        return rb.build();
    }

    /**
     * Create Rss Model
     * 
     * @param authToken
     * @param rssModel
     * @return
     * @throws Exception
     */
    @WebMethod
    @POST
    @Path("/")
    @Consumes("application/json")
    public Response createRSSModel(@HeaderParam("X-Auth-Token") final String authToken,
        RSSModel rssModel) throws Exception {
        RSSModelService.logger.debug("Into createRSSModel method");
        // check security
        ValidatedToken token = oauthManager.checkAuthenticationToken(authToken);

        // Validate that the user can create a RS model for the given aggregator
        if (!oauthManager.isAdmin(token) && !token.getEmail().equals(rssModel.getAggregatorId())) {
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
     * @param authToken
     * @param rssModel
     * @return
     * @throws Exception
     */
    @WebMethod
    @PUT
    @Path("/")
    @Consumes("application/json")
    public Response modifyRSSModel(@HeaderParam("X-Auth-Token") final String authToken,
        RSSModel rssModel) throws Exception {
        RSSModelService.logger.debug("Into modifyRSSModel method");
        // check security
        ValidatedToken token = oauthManager.checkAuthenticationToken(authToken);

        // Validate that the user can update a RS model for the given aggregator
        if (!oauthManager.isAdmin(token) && !token.getEmail().equals(rssModel.getAggregatorId())) {
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
     * @param authToken
     * @param appProvider
     * @param productClass
     * @return
     * @throws Exception
     */
    @WebMethod
    @DELETE
    @Path("/")
    @Consumes("application/json")
    public Response deleteRSSModel(@HeaderParam("X-Auth-Token") final String authToken,
        @QueryParam("appProviderId") String appProvider,
        @QueryParam("productClass") String productClass) throws Exception {
        RSSModelService.logger.debug("Into deleteRSSModel method");
        // check security
        ValidatedToken token = oauthManager.checkAuthenticationToken(authToken);
        // Call service
        rssModelsManager.deleteRssModel(token.getEmail(), appProvider, productClass);
        // Building response
        ResponseBuilder rb = Response.status(Response.Status.OK.getStatusCode());
        return rb.build();
    }

}
