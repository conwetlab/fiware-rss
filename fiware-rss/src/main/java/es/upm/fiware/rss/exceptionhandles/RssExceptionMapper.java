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

package es.upm.fiware.rss.exceptionhandles;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.upm.fiware.rss.exception.RSSException;

/**
 * Return correct exception to the client.
 * 
 */
@Provider
public class RssExceptionMapper implements ExceptionMapper<Exception> {
    /**
     * 
     */
    private final Logger logger = LoggerFactory.getLogger(RssExceptionMapper.class);
    /**
     * 
     */
    @Context
    protected HttpHeaders headers;
    /**
     * 
     */
    @Context
    protected UriInfo uriInfo;

    /**
     * 
     * @return
     */
    protected boolean isJSONAccept() {
        return headers.getAcceptableMediaTypes() != null
            && headers.getAcceptableMediaTypes().size() > 0
            && headers.getAcceptableMediaTypes().get(0).getType().equals(MediaType.APPLICATION_JSON_TYPE.getType())
            && headers.getAcceptableMediaTypes().get(0).getSubtype()
                .equals(MediaType.APPLICATION_JSON_TYPE.getSubtype());
    }

    @Override
    public Response toResponse(Exception e) {
        if (isJSONAccept()) {
            // JSON
            JsonExceptionMapper mapperJSON = new JsonExceptionMapper();
            mapperJSON.setUriInfo(uriInfo);
            return mapperJSON.toResponse(e);
        } else if (e instanceof RSSException) {
            logger.error("Return RssException: [" + ((RSSException) e).getExceptionType().getStatus().value()
                + "] " + e.getMessage(), e);
            return Response.status(((RSSException) e).getExceptionType().getStatus().value())
                .entity(((RSSException) e).getMessage())
                .build();
        } else if (e instanceof NotFoundException) {
            return Response.status(404).build();
        } else {
            logger.error("Return Exception: " + e.getMessage(), e);
            // Write response
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

}
