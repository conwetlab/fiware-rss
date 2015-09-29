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
package es.upm.fiware.rss.oauth.service;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 *
 */
public class ResponseHandler implements org.apache.http.client.ResponseHandler<HttpResponse> {
    private static final Logger log = LoggerFactory.getLogger(ResponseHandler.class);
    /**
     * 
     */
    private String responseContent;
    /**
     * 
     */
    private int status;
    /**
     * 
     */
    private boolean content;

    /**
     * handle response.
     * 
     * @throws IOException
     */
    @Override
    public HttpResponse handleResponse(final HttpResponse response) throws IOException {
        ResponseHandler.log.debug("into handleResponse method");
        status = response.getStatusLine().getStatusCode();
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            content = true;
            ResponseHandler.log.debug("----------------------------------------");
            ResponseHandler.log.debug("Response content:");
            responseContent = EntityUtils.toString(entity);
            ResponseHandler.log.debug(responseContent);
        } else {
            content = false;
        }
        return response;
    }

    /**
     * @return the responseContent
     */
    public String getResponseContent() {
        return responseContent;
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @return the content
     */
    public boolean hasContent() {
        return content;
    }

}
