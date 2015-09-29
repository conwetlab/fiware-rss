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

/**
 * 
 */
package es.upm.fiware.rss.exception.factories.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import es.upm.fiware.rss.exception.RSSException;
import es.upm.fiware.rss.exception.UNICAExceptionType;
import es.upm.fiware.rss.exception.beans.ExceptionType;
import es.upm.fiware.rss.exception.factories.FactoryExceptionType;

/**
 * 
 */
public class FactoryExceptionTypeTest {

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for
     * {@link es.upm.fiware.rss.exception.factories.FactoryExceptionType#createExceptionBean(es.upm.fiware.rss.exception.RSSException)}
     * .
     */
    @Test
    public void testCreateExceptionBean() {
        Object[] args = {"Mensaje de error"};
        RSSException exception = new RSSException(UNICAExceptionType.NON_EXISTENT_RESOURCE_ID, args);
        // Call method
        FactoryExceptionType factory = new FactoryExceptionType();
        ExceptionType bean = FactoryExceptionType.createExceptionBean(exception);

        Assert.assertTrue("ExceptionId not equal (" + bean.getExceptionId() + ")",
            bean.getExceptionId().compareTo(UNICAExceptionType.NON_EXISTENT_RESOURCE_ID.getExceptionId()) == 0);
        Assert.assertTrue("ExceptionText not equal (" + bean.getExceptionText() + ")", bean.getExceptionText()
            .compareTo(String.format(UNICAExceptionType.NON_EXISTENT_RESOURCE_ID.getFormatText(), args)) == 0);
    }

}
