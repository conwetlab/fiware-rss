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

package es.upm.fiware.rss.exception.factories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.upm.fiware.rss.exception.RSSException;
import es.upm.fiware.rss.exception.beans.ExceptionType;

/***
 * Create an error in the UNICA format.
 * 
 * For the change to UNICA common 2.0 this class is much more simple.
 * 
 * 
 */
public final class FactoryExceptionType {

    /***
     * Logging system.
     */
    private static Logger logger = LoggerFactory.getLogger(FactoryExceptionType.class);

    /***
     * Create and return an ExceptionType from the data passed.
     * 
     * @param exception
     *            Exception for create bean
     * @return ExceptionType with the information passed.
     */
    public static ExceptionType createExceptionBean(final RSSException exception) {
        FactoryExceptionType.logger.debug("Accessing createExceptionBean");

        ExceptionType oErr = new ExceptionType();

        oErr.setExceptionId(exception.getExceptionType().getExceptionId());
        oErr.setExceptionText(exception.getMessage());

        FactoryExceptionType.logger.error(exception.getMessage());

        return oErr;
    }
}
