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

package es.tid.fiware.rss.expenditureLimit.server.common;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.hibernate.exception.GenericJDBCException;
import org.hibernate.exception.JDBCConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.tid.fiware.rss.common.Constants;
import es.tid.fiware.rss.common.properties.AppProperties;
import es.tid.fiware.rss.exception.RSSException;
import es.tid.fiware.rss.exception.RSSExceptionType;
import es.tid.fiware.rss.exception.UNICAExceptionType;
import es.tid.fiware.rss.expenditureLimit.api.ExceptionTypeBean;

/**
 * 
 */
public final class FactoryResponse {
    /**
     * 
     */
    private static Logger logger = LoggerFactory.getLogger(FactoryResponse.class);

    /**
     * Private constructor.
     */
    private FactoryResponse() {
    }

    /**
     * Response error for a GRETA exception.
     * 
     * @param exception
     *            GRETAException
     * @param exceptObj
     *            Object
     * @return Response
     */
    public static Response createResponseError(final RSSException exception, Object exceptObj) {

        int statusHTTP = exception.getExceptionType().getStatus().value();
        return Response.status(statusHTTP).entity(exceptObj).build();
    }

    /**
     * Generate an errorMsg from a exception.
     * 
     * @param exception
     * @return
     */
    public static String createErrorMsg(Exception exception) {
        StringBuffer msg = new StringBuffer(exception.toString() + "\n");
        StackTraceElement[] trace = exception.getStackTrace();
        for (int i = 0; i < trace.length; i++) {
            msg.append("   at ");
            msg.append(trace[i].toString() + "\n");
        }
        return msg.toString();
    }

    /**
     * Generate ExceptionTypeBean.
     * 
     * @param exception
     * @param resource
     * @return
     */
    public static ExceptionTypeBean exceptionJson(AppProperties dbeProperties,
        UriInfo ui, RSSException exception, String resource) {

        ExceptionTypeBean oErr = new ExceptionTypeBean();
        oErr.setExceptionId(exception.getExceptionType().getExceptionId());
        oErr.setExceptionText(exception.getMessage());
        if ((null != resource) && (null != exception.getMoreInfo()) && (exception.getMoreInfo().length() > 0)) {
            oErr.setMoreInfo(ExpenditureLimitCommon.getResourceUrl(dbeProperties, ui, exception.getMoreInfo(),
                resource));
        }
        oErr.setUserMessage(exception.getUserMessage());
        return oErr;
    }

    /**
     * Response error for a string. Payment API.
     * 
     * @param message
     *            String message
     * @return Response
     */
    public static Response createResponseErrorJson(AppProperties dbeProperties, UriInfo ui, final String message,
        final String resource) {
        String[] args = { message };
        RSSException exception = new RSSException(RSSExceptionType.GENERIC_EXCEPTION, args);
        ExceptionTypeBean exceptObj = FactoryResponse.exceptionJson(dbeProperties, ui,
            exception, resource);
        return FactoryResponse.createResponseError(exception, exceptObj);
    }

    /**
     * Generate an exception related to create a new DB connection.
     * 
     * @param e
     *            exception
     * @param txId
     * @param resource
     * @return
     */
    public static Response catchNewConnectionJson(AppProperties dbeProperties, UriInfo ui, GenericJDBCException e,
        String resource, String txId) {
        // ALARM DETECTED
        FactoryResponse.logger.error(Constants.LOG_ALARM + " Cannot open connection with the database");

        // Write response
        String[] args = { "Cannot open connection with the database" };
        RSSException newException = new RSSException(UNICAExceptionType.GENERIC_SERVER_FAULT, args, e, txId, null);
        FactoryResponse.logger.error("Return GRETAException: [" + newException.getExceptionType().getExceptionId()
            + "] "
            + newException.getMessage(), e);
        ExceptionTypeBean exceptObj = FactoryResponse.exceptionJson(dbeProperties, ui,
            newException, resource);
        return FactoryResponse.createResponseError(newException, exceptObj);
    }

    /**
     * Generate an exception related to get a DB connection.
     * 
     * @param e
     *            exception
     * @param txId
     * @param resource
     * @return
     */
    public static Response catchConnectionJDBCJson(AppProperties dbeProperties, UriInfo ui, JDBCConnectionException e,
        String resource, String txId) {
        // ALARM DETECTED
        FactoryResponse.logger.error(Constants.LOG_ALARM + " Problems connecting to the database: {}", resource);

        // Write response
        String[] args = { "Problems connecting to the database: " + resource };
        RSSException newException = new RSSException(UNICAExceptionType.GENERIC_SERVER_FAULT, args, e, txId, null);
        FactoryResponse.logger.error("Return GRETAException: [" + newException.getExceptionType().getExceptionId()
            + "] "
            + newException.getMessage(), e);
        ExceptionTypeBean exceptObj = FactoryResponse.exceptionJson(dbeProperties, ui,
            newException, resource);
        return FactoryResponse.createResponseError(newException, exceptObj);
    }

    /**
     * Generate an exception related to get a DB connection.
     * 
     * @param e
     *            exception
     * @param resource
     * @return
     */
    public static Response catchExceptionJson(AppProperties dbeProperties, UriInfo ui,
        RSSException e, String resource) {
        // ALARM DETECTED
        FactoryResponse.logger.error("Error: " + e.toString(), e);
        ExceptionTypeBean exceptObj = FactoryResponse.exceptionJson(dbeProperties, ui,
            e, resource);
        return FactoryResponse.createResponseError(e, exceptObj);
    }

}
