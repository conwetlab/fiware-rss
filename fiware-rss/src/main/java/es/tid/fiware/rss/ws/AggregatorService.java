/**
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

package es.tid.fiware.rss.ws;

import es.tid.fiware.rss.exception.RSSException;
import es.tid.fiware.rss.exception.UNICAExceptionType;
import es.tid.fiware.rss.model.Aggregator;
import es.tid.fiware.rss.oauth.model.ValidatedToken;
import es.tid.fiware.rss.oauth.service.OauthManager;
import es.tid.fiware.rss.service.SettlementManager;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author fdelavega
 */
@Path("/")
@WebService(serviceName = "aggregators", name="aggregators")
public class AggregatorService {

    @Autowired
    OauthManager oauthManager;

    @Autowired
    SettlementManager settlementManager;

    @WebMethod
    @Produces("application/json")
    @GET
    public Response getAggregators(@HeaderParam("X-Auth-Token") final String authToken) 
            throws Exception{

        ValidatedToken validToken = oauthManager.checkAuthenticationToken(authToken);

        if (!oauthManager.isAdmin(validToken)) {
            String[] args = {"You are not allowed to retrieve aggregators"};
            throw new RSSException(UNICAExceptionType.NON_ALLOWED_OPERATION, args);
        }

        List<Aggregator> aggregators = settlementManager.getAPIAggregators();
        Response.ResponseBuilder rb = Response.status(Response.Status.OK.getStatusCode());
        rb.entity(aggregators);
        return rb.build();
    }
}
