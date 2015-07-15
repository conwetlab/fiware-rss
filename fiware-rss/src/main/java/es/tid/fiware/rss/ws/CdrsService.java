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

package es.tid.fiware.rss.ws;

import es.tid.fiware.rss.exception.RSSException;
import es.tid.fiware.rss.exception.UNICAExceptionType;
import es.tid.fiware.rss.model.CDR;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import es.tid.fiware.rss.service.CdrsManager;
import es.tid.fiware.rss.service.UserManager;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;


@WebService(serviceName = "cdrs", name = "cdrs")
@Path("/")
public class CdrsService {

    /***
     * Logging system.
     */
    private final Logger logger = LoggerFactory.getLogger(CdrsService.class);

    @Autowired
    private CdrsManager cdrsManager;

    @Autowired
    private UserManager userManager;

    /**
     * Web service used to receive new CDRs defining a set of transactions.
     * 
     * @param cdrs, List of CDR document defining different transactions
     * @return, A CREATED response
     * @throws Exception, When a problem occur saving the transactions
     */
    @WebMethod
    @POST
    @Consumes("application/json")
    public Response createCdr(List<CDR> cdrs) throws Exception {
        logger.info("createCdr POST Start.");

        this.cdrsManager.createCDRs(cdrs);
        Response.ResponseBuilder rb = Response.status(Response.Status.CREATED.getStatusCode());
        return rb.build();
    }

    @WebMethod
    @GET
    @Produces("application/json")
    public Response getCDRs(@QueryParam("aggregatorId") String aggregatorId,
            @QueryParam("providerId") String providerId) throws Exception {

        logger.debug("getCDRs GET start");

        // Validate permissions
        if (aggregatorId != null && !this.userManager.isAdmin() && 
                !this.userManager.getCurrentUser().getEmail().equalsIgnoreCase(aggregatorId)) {

            String[] args = {"You are not allowed to retrieve transactions of the Store owned by " + aggregatorId};
            throw new RSSException(UNICAExceptionType.NON_ALLOWED_OPERATION, args);
        }

        String effectiveAggregator = aggregatorId;
        if (!this.userManager.isAdmin()) {
            effectiveAggregator = this.userManager.getCurrentUser().getEmail();
        }

        List<CDR> resp = this.cdrsManager.getCDRs(effectiveAggregator, providerId);

        Response.ResponseBuilder rb = Response.status(Response.Status.OK.getStatusCode());
        rb.entity(resp);
        return rb.build();
    }
}
