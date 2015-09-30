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

import java.net.URI;
import java.sql.SQLException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.hibernate.exception.GenericJDBCException;
import org.hibernate.exception.JDBCConnectionException;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.upm.fiware.rss.exception.RSSException;
import es.upm.fiware.rss.model.ExceptionTypeBean;

/**
 * 
 *
 */
public class FactoryResponseTest {
    /**
     * Logging system.
     */
    private static Logger logger = LoggerFactory.getLogger(FactoryResponseTest.class);

    /**
     * 
     */
    @Test
    public void createResponseError() {
        RSSException exception = new RSSException("RssException");
        Response response = FactoryResponse.createResponseError(exception, "exceptObj");
        Assert.assertTrue(true);
    }

    /**
     * 
     */
    @Test
    public void createErrorMsg() {
        FactoryResponse factory = new FactoryResponse();
        RSSException exception = new RSSException("RssException");
        String response = FactoryResponse.createErrorMsg(exception);
        Assert.assertTrue(true);
    }

    /**
     * 
     */
    @Test
    public void exceptionJson() throws Exception {
        UriInfo mockUriInfo = Mockito.mock(UriInfo.class);
        Mockito.when(mockUriInfo.getBaseUri()).thenReturn(new URI("http://www.test.com/go"));
        RSSException exception = new RSSException("RssException");
        ExceptionTypeBean bean = FactoryResponse.exceptionJson(mockUriInfo,
            exception, "resource");
        Assert.assertTrue(true);
    }

    /**
     * 
     */
    @Test
    public void createResponseErrorJson() throws Exception {
        UriInfo mockUriInfo = Mockito.mock(UriInfo.class);
        Mockito.when(mockUriInfo.getBaseUri()).thenReturn(new URI("http://www.test.com/go"));
        Response bean = FactoryResponse.createResponseErrorJson(mockUriInfo,
            "message", "resource");
        Assert.assertTrue(true);
    }

    /**
     * 
     */
    @Test
    public void catchNewConnectionJson() throws Exception {
        UriInfo mockUriInfo = Mockito.mock(UriInfo.class);
        Mockito.when(mockUriInfo.getBaseUri()).thenReturn(new URI("http://www.test.com/go"));
        GenericJDBCException exception = new GenericJDBCException("sql", new SQLException("reason"));
        Response bean = FactoryResponse.catchNewConnectionJson(mockUriInfo,
            exception, "message", "resource");
        Assert.assertTrue(true);
    }

    /**
     * 
     */
    @Test
    public void catchConnectionJDBCJson() throws Exception {
        UriInfo mockUriInfo = Mockito.mock(UriInfo.class);
        Mockito.when(mockUriInfo.getBaseUri()).thenReturn(new URI("http://www.test.com/go"));
        JDBCConnectionException exception = new JDBCConnectionException("sql", new SQLException("reason"));
        Response bean = FactoryResponse.catchConnectionJDBCJson(mockUriInfo,
            exception, "resource", "txId");
        Assert.assertTrue(true);
    }

    /**
     * 
     */
    @Test
    public void catchExceptionJson() throws Exception {
        UriInfo mockUriInfo = Mockito.mock(UriInfo.class);
        Mockito.when(mockUriInfo.getBaseUri()).thenReturn(new URI("http://www.test.com/go"));
        RSSException exception = new RSSException("RssException");
        Response bean = FactoryResponse.catchExceptionJson(mockUriInfo, exception,
            "resource");
        Assert.assertTrue(true);
    }
}
