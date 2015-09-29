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

package es.upm.fiware.rss.expenditureLimit.server.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.upm.fiware.rss.common.properties.AppProperties;
import es.upm.fiware.rss.exception.RSSException;
import es.upm.fiware.rss.exception.UNICAExceptionType;

/**
 * 
 */
public final class ExpenditureLimitCommon {
    /**
     * Variable to print the trace.
     */
    private static Logger logger = LoggerFactory.getLogger(ExpenditureLimitCommon.class);

    /**
     * Service URL.
     */
    private static String serviceUrl;
    /**
     * Format of the date used.
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";

    /**
     * TimeZone for parseDate.
     */
    private static final TimeZone UTC = TimeZone.getTimeZone("UTC");

    /**
     * Identifier for the default provider.
     * 
     * It stores sum period expenses.
     */
    public final static String DEF_PROV_ID = "DefaulProviderId";

    /**
     * 
     * @param strDate
     * @return date;
     * @throws RSSException
     */
    public static Date parseDate(String strDate) throws RSSException {
        SimpleDateFormat formatter = new SimpleDateFormat(ExpenditureLimitCommon.DATE_FORMAT);
        formatter.setTimeZone(ExpenditureLimitCommon.UTC);
        Date date;
        try {
            date = formatter.parse(strDate);
            return date;
        } catch (ParseException ex) {
            ExpenditureLimitCommon.logger.error("Error parsing string to date " + ex.getMessage());
            String[] args = {"'fromTime/untilTime' hasn't correct format (Example: 2012-05-29 12:32)"};
            throw new RSSException(UNICAExceptionType.INVALID_PARAMETER, args);
        }
    }

    /**
     * Generate and return the resource url.
     * 
     * @param profileId
     * @param resource
     * @return
     */
    public static String getResourceUrl(AppProperties appProperties, UriInfo ui, String profileId, String resource) {
        String resourceUrl, urlEnd;
        String serviceUrl = ExpenditureLimitCommon.getServiceUrl(appProperties);

        // Init urlEnd
        if (profileId == null || profileId.equals("")) {
            urlEnd = "";
        } else {
            urlEnd = "/" + profileId;
        }

        if (resource == null) {
            resource = "/";
        }

        // Set resourceUrl
        if (null == serviceUrl || "".equalsIgnoreCase(serviceUrl) && (null != ui) && (null != ui.getBaseUri())) {
            // get default path from context
            String urlBase = ui.getBaseUri().toString().replace(resource, "/");
            if (urlBase.endsWith("/")) {
                resourceUrl = urlBase + resource.substring(1) + urlEnd;
            } else {
                resourceUrl = urlBase + resource + urlEnd;
            }
        } else {
            // Use in case of load balancer
            resourceUrl = serviceUrl + resource + urlEnd;
        }
        return resourceUrl;
    }

    /**
     * Take the service.url property from the file.
     * 
     * @return the serviceUrl
     */
    public static String getServiceUrl(AppProperties appProperties) {
        ExpenditureLimitCommon.logger.debug("Into getServiceUrl method.");
        if (ExpenditureLimitCommon.serviceUrl == null) {
            ExpenditureLimitCommon.serviceUrl = appProperties.getProperty("service.url");
            if (ExpenditureLimitCommon.serviceUrl == null) {
                ExpenditureLimitCommon.logger.error("Property 'service.url' not defined");
            }
        }
        return ExpenditureLimitCommon.serviceUrl;
    }

}