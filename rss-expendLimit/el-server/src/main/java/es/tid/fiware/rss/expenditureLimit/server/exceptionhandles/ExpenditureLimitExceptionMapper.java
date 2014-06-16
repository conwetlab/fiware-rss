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

package es.tid.fiware.rss.expenditureLimit.server.exceptionhandles;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.hibernate.exception.GenericJDBCException;
import org.hibernate.exception.JDBCConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import es.tid.fiware.rss.common.properties.AppProperties;
import es.tid.fiware.rss.exception.RSSException;
import es.tid.fiware.rss.expenditureLimit.api.ExceptionTypeBean;
import es.tid.fiware.rss.expenditureLimit.server.common.FactoryResponse;

/**
 * 
 * 
 */
@Provider
public class ExpenditureLimitExceptionMapper implements ExceptionMapper<Exception> {

    @Autowired
    @Qualifier(value = "appProperties")
    private AppProperties dbeProperties;
    @Context
    private UriInfo ui;
    private final Logger logger = LoggerFactory.getLogger(ExpenditureLimitExceptionMapper.class);

    @Override
    public Response toResponse(Exception e) {

        if (e instanceof RSSException) {
            logger.error("Return GRETAException: [" + ((RSSException) e).getExceptionType().getExceptionId()
                + "] " + e.getMessage(), e);
            ExceptionTypeBean exceptObj = FactoryResponse.exceptionJson(dbeProperties, ui,
                ((RSSException) e), ui.getAbsolutePath().getPath());
            return FactoryResponse.createResponseError(((RSSException) e), exceptObj);
        } else if (e instanceof GenericJDBCException) {
            return FactoryResponse.catchNewConnectionJson(dbeProperties, ui, (GenericJDBCException) e,
                ui.getAbsolutePath().getPath(), null);
        } else if (e instanceof JDBCConnectionException) {
            return FactoryResponse.catchConnectionJDBCJson(dbeProperties, ui, (JDBCConnectionException) e,
                ui.getAbsolutePath().getPath(), null);
        } else if (e instanceof NotFoundException) {
            return Response.status(404).build();
        } else {
            logger.error("Return Exception: " + e.getMessage(), e);

            // Write response
            if (e.getCause() instanceof GenericJDBCException) {
                return FactoryResponse.catchNewConnectionJson(dbeProperties, ui, (GenericJDBCException) e.getCause(),
                    ui.getAbsolutePath().getPath(), null);
            } else if (e.getCause() instanceof JDBCConnectionException) {
                return FactoryResponse.catchConnectionJDBCJson(dbeProperties, ui,
                    (JDBCConnectionException) e.getCause(),
                    ui.getAbsolutePath().getPath(), null);
            } else {
                logger.error("Return Exception: " + e.getMessage(), e);

                // Write response
                return FactoryResponse.createResponseErrorJson(dbeProperties, ui,
                    FactoryResponse.createErrorMsg(e), ui.getAbsolutePath().getPath());
            }
        }
    }
}
