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
import es.tid.fiware.rss.model.RSUser;
import es.tid.fiware.rss.service.SettlementManager;
import es.tid.fiware.rss.service.UserManager;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author fdelavega
 */
@WebService(serviceName = "settlement", name = "settlement")
@Path("/")
public class SettlementService {

    @Autowired
    SettlementManager settlementManager;

    @Autowired
    UserManager userManager;

    @WebMethod
    @GET
    public Response launchSettlement(@QueryParam("aggregatorId") String aggregatorId,
            @QueryParam("providerId") String providerId,
            @QueryParam("productClass") String productClass)
            throws Exception {

        // Check basic permissions
        RSUser user = this.userManager.getCurrentUser();
        if (!this.userManager.isAdmin() &&
                (aggregatorId == null || !user.getEmail().equalsIgnoreCase(aggregatorId))) {

            String[] args = {"You are not allowed to launch the settlement process for the given parameters"};
            throw new RSSException(UNICAExceptionType.NON_ALLOWED_OPERATION, args);
        }

        // Launch process
        settlementManager.runSettlement(aggregatorId, providerId, productClass);
        Response.ResponseBuilder rb = Response.status(Response.Status.ACCEPTED.getStatusCode());
        return rb.build();
    }
}
