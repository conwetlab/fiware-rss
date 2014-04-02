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

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import es.tid.fiware.rss.oauth.service.OauthManager;
import es.tid.fiware.rss.service.CdrsManager;

@WebService(serviceName = "Cdrs", name = "CdrsService")
@Path("")
public class CdrsService {

    /***
     * Logging system.
     */
    private final Logger logger = LoggerFactory.getLogger(CdrsService.class);

    /**
     * Information about the context of the application.
     */
    @Context
    private static UriInfo ui;

    @Autowired
    private CdrsManager cdrsManager;
    /**
     * Oauth manager.
     */
    @Autowired
    private OauthManager oauthManager;

    /**
     * receive cdrs.
     * 
     * @param authToken
     *            oauth authentication token.
     * @param xml
     *            cdrs file.
     * @return
     * @throws Exception
     */
    @WebMethod
    @POST
    @Path("")
    @Consumes("application/xml")
    @Produces("application/xml")
    public Response createCdr(@HeaderParam("X-Auth-Token") final String authToken, String xml) throws Exception {
        logger.info("createCdr POST Start. Authenctication header: " + authToken);
        // check security
        oauthManager.checkAuthenticationToken(authToken);
        try {
            InputStream stream = new ByteArrayInputStream(xml.getBytes());
            OutputStream out = new FileOutputStream(cdrsManager.getFile());

            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = stream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            stream.close();
            out.flush();
            out.close();
        } catch (Exception e) {
            logger.error("Error saving cdr file: ", e);
            return Response.serverError().entity(e.getMessage()).build();
        }
        logger.debug("XML file saved");
        try {
            String error = cdrsManager.runCdrToDB();
            if (error != null) {
                return Response.serverError().entity(error).build();
            }
        } catch (Exception e) {
            logger.error("Error running settlement: ", e);
            return Response.serverError().entity(e.getMessage()).build();
        }
        logger.info("createCdr POST: OK");
        return Response.ok("CDRs saved").build();
    }

}
