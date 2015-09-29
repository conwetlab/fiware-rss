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

package es.upm.fiware.rss.exception;

import org.springframework.http.HttpStatus;

/***
 * This class is an enum to control the possible exception types.
 * 
 * 
 */
public enum RSSExceptionType implements InterfaceExceptionType {

    /***
     * Generic error for GRETA.
     */
    GENERIC_EXCEPTION("GRT0001", "Generic error: %s", HttpStatus.INTERNAL_SERVER_ERROR);

    /***
     * ID exception.
     */
    private String exceptionId;

    /***
     * Text exception.
     */
    private String formatText;

    /***
     * Associated status HTTP.
     */
    private HttpStatus status;

    /**
     * @return the exceptionId
     */
    @Override
    public String getExceptionId() {
        return exceptionId;
    }

    /**
     * @return the text
     */
    @Override
    public String getFormatText() {
        return formatText;
    }

    /**
     * @return the status
     */
    @Override
    public HttpStatus getStatus() {
        return status;
    }

    /***
     * Constructor.
     * 
     * @param exceptionId
     *            to set
     * @param text
     *            to set
     * @param statusHTTP
     *            to set
     */
    private RSSExceptionType(final String exceptionId, final String text, final HttpStatus statusHTTP) {
        this.exceptionId = exceptionId;
        this.formatText = text;
        this.status = statusHTTP;
    }
};
