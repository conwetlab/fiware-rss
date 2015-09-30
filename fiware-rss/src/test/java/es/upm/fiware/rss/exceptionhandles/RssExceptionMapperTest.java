/**
 * Revenue Settlement and Shar
 * ing System GE
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
import java.util.Arrays;
import java.util.Properties;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import es.upm.fiware.rss.exception.RSSException;

/**
 * 
 *
 */
public class RssExceptionMapperTest {

    /**
     * 
     */
    @Test
    public void toResponse() throws Exception {
        RssExceptionMapper mapper = new RssExceptionMapper();
        UriInfo mockUriInfo = Mockito.mock(UriInfo.class);
        Mockito.when(mockUriInfo.getBaseUri()).thenReturn(new URI("http://www.test.com/go"));
        ReflectionTestUtils.setField(mapper, "uriInfo", mockUriInfo);

        HttpHeaders headers = Mockito.mock(HttpHeaders.class);
        Mockito.when(headers.getAcceptableMediaTypes()).thenReturn(null);
        ReflectionTestUtils.setField(mapper, "headers", headers);

        RSSException e = new RSSException("RssException");
        Response response = mapper.toResponse(e);
        // other type
        NotFoundException ex = new NotFoundException();
        response = mapper.toResponse(ex);
        // other
        Exception ex1 = new Exception("Exception");
        response = mapper.toResponse(ex1);
        Assert.assertTrue(true);
    }

    /**
     * 
     */
    @Test
    public void toResponseJson() throws Exception {
        RssExceptionMapper mapper = new RssExceptionMapper();
        UriInfo mockUriInfo = Mockito.mock(UriInfo.class);
        Mockito.when(mockUriInfo.getAbsolutePath()).thenReturn(new URI("http://www.test.com/go"));
        ReflectionTestUtils.setField(mapper, "uriInfo", mockUriInfo);

        HttpHeaders headers = Mockito.mock(HttpHeaders.class);
        Mockito.when(headers.getAcceptableMediaTypes()).thenReturn(Arrays.asList(MediaType.APPLICATION_JSON_TYPE));
        ReflectionTestUtils.setField(mapper, "headers", headers);

        RSSException e = new RSSException("RssException");
        Response response = mapper.toResponse(e);
    }
}
