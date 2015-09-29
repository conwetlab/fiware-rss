/**
 * Copyright (C) 2015, CoNWeT Lab., Universidad Politécnica de Madrid
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

package es.upm.fiware.rss.oauth.exceptions;

import es.upm.fiware.rss.exception.InterfaceExceptionType;
import es.upm.fiware.rss.exception.UNICAExceptionType;

/**
 *
 * @author fdelavega
 */
public class AuthException extends Exception{

    private final InterfaceExceptionType exceptionType;

    public AuthException(String msg) {
        super(msg);
        this.exceptionType = UNICAExceptionType.INVALID_OAUTH_TOKEN;
    }

    public AuthException(InterfaceExceptionType exceptionType, String msg) {
        super(msg);
        this.exceptionType = exceptionType;
    }
}
