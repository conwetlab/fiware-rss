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
package es.tid.fiware.rss.exceptionhandles;

import java.net.URI;
import java.sql.SQLException;

import javax.sql.DataSource;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.hibernate.exception.GenericJDBCException;
import org.hibernate.exception.JDBCConnectionException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import es.tid.fiware.rss.common.test.DatabaseLoader;
import es.tid.fiware.rss.exception.RSSException;

/**
 * 
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:database.xml", "classpath:cxf-beans.xml"})
public class JsonExceptionMapperTest {
    /**
     * Logging system.
     */
    private static Logger logger = LoggerFactory.getLogger(JsonExceptionMapperTest.class);
    /**
     * For database access.
     */
    @Autowired
    private DataSource dataSource;
    /**
     * 
     */
    @Autowired
    private DatabaseLoader databaseLoader;

    private JsonExceptionMapper mapper = new JsonExceptionMapper();

    @Before
    public void setUp() throws Exception {
        databaseLoader.cleanInsert("dbunit/CREATE_DATATEST_EXPLIMIT.xml", true);
    }

    /**
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        databaseLoader.deleteAll("dbunit/CREATE_DATATEST_EXPLIMIT.xml", true);
    }

    @Test
    public void toResponse() throws Exception {
        UriInfo mockUriInfo = Mockito.mock(UriInfo.class);
        Mockito.when(mockUriInfo.getAbsolutePath()).thenReturn(new URI("http://www.test.com/go"));
        ReflectionTestUtils.setField(mapper, "uriInfo", mockUriInfo);

        RSSException e = new RSSException("RssException");
        Response response = mapper.toResponse(e);
        Assert.assertTrue(true);

        GenericJDBCException ex = new GenericJDBCException("sql", new SQLException("reason"));
        response = mapper.toResponse(ex);
        Assert.assertTrue(true);

        JDBCConnectionException ex1 = new JDBCConnectionException("sql", new SQLException("reason"));
        response = mapper.toResponse(ex1);
        Assert.assertTrue(true);

        NotFoundException ex2 = new NotFoundException();
        response = mapper.toResponse(ex2);
        Assert.assertTrue(true);

        Exception ex3 = new Exception("RssException");
        response = mapper.toResponse(ex3);
        Assert.assertTrue(true);

        Exception ex4 = new Exception("RssException", ex);
        response = mapper.toResponse(ex4);
        Assert.assertTrue(true);

        Exception ex5 = new Exception("RssException", ex1);
        response = mapper.toResponse(ex5);
        Assert.assertTrue(true);

    }
}
