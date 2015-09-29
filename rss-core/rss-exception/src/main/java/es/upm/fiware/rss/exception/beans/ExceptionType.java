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

package es.upm.fiware.rss.exception.beans;


/**
 * Returned by server in case of a fault
 * 
 * 
 **/
public class ExceptionType {
    /**
     * atributtes
     */
    protected String exceptionId;
    protected String exceptionText;
    protected String moreInfo;
    protected String userMessage;

    /**
     * Gets the value of the exceptionId property.
     * 
     * @return
     *         possible object is {@link String }
     * 
     */
    public String getExceptionId() {
        return exceptionId;
    }

    /**
     * Sets the value of the exceptionId property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setExceptionId(String value) {
        this.exceptionId = value;
    }

    /**
     * Gets the value of the exceptionText property.
     * 
     * @return
     *         possible object is {@link String }
     * 
     */
    public String getExceptionText() {
        return exceptionText;
    }

    /**
     * Sets the value of the exceptionText property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setExceptionText(String value) {
        this.exceptionText = value;
    }

    /**
     * Gets the value of the moreInfo property.
     * 
     * @return
     *         possible object is {@link String }
     * 
     */
    public String getMoreInfo() {
        return moreInfo;
    }

    /**
     * Sets the value of the moreInfo property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setMoreInfo(String value) {
        this.moreInfo = value;
    }

    /**
     * Gets the value of the userMessage property.
     * 
     * @return
     *         possible object is {@link String }
     * 
     */
    public String getUserMessage() {
        return userMessage;
    }

    /**
     * Sets the value of the userMessage property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setUserMessage(String value) {
        this.userMessage = value;
    }

}
