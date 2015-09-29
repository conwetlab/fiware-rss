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
package es.upm.fiware.rss.exception.test;

import org.junit.Assert;
import org.junit.Test;

import es.upm.fiware.rss.exception.RSSException;
import es.upm.fiware.rss.exception.RSSExceptionType;
import es.upm.fiware.rss.exception.UNICAExceptionType;

/**
 * 
 */
public class RSSExceptionTest {

    /**
     * Test method for {@link es.upm.fiware.rss.exception.RSSException#GRETAException(java.lang.String)}.
     */
    @Test
    public void testGRETAExceptionString() {
        String msg = "Mensaje de error";
        // Call method
        RSSException exception = new RSSException(msg);

        Assert.assertTrue("Message not equal (" + exception.getMessage() + ")",
            exception.getMessage().compareTo(msg) == 0);
        Assert.assertEquals("ExceptionType not equal (" + exception.getExceptionType() + ")",
            RSSExceptionType.GENERIC_EXCEPTION, exception.getExceptionType());
    }

    /**
     * Test method for
     * {@link es.upm.fiware.rss.exception.RSSException#GRETAException(es.upm.fiware.rss.exception.InterfaceExceptionType, java.lang.Object[])}
     * .
     */
    @Test
    public void testGRETAExceptionInterfaceExceptionTypeObjectArray() {
        Object[] args = {"Mensaje de error"};
        // Call method
        RSSException exception = new RSSException(UNICAExceptionType.NON_EXISTENT_RESOURCE_ID, args);

        Assert.assertTrue(
            "Message not equal (" + exception.getMessage() + ")",
            exception.getMessage().compareTo(
                String.format(UNICAExceptionType.NON_EXISTENT_RESOURCE_ID.getFormatText(), args)) == 0);
        Assert.assertEquals("ExceptionType not equal (" + exception.getExceptionType() + ")",
            UNICAExceptionType.NON_EXISTENT_RESOURCE_ID, exception.getExceptionType());
    }

    /**
     * Test method for {@link es.upm.fiware.rss.exception.RSSException#getExceptionType()}.
     */
    @Test
    public void testGetExceptionType() {
        Object[] args = {"Mensaje de error"};
        // Call method
        RSSException exception = new RSSException(UNICAExceptionType.NON_EXISTENT_RESOURCE_ID, args);

        Assert.assertEquals("ExceptionType not equal (" + exception.getExceptionType() + ")",
            UNICAExceptionType.NON_EXISTENT_RESOURCE_ID, exception.getExceptionType());
    }
}
