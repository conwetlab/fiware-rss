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
package es.upm.fiware.rss.model;

/**
 * 
 * 
 */
public class ExceptionTypeBean {
    /**
     * 
     */
    private String exceptionId;
    /**
     * 
     */
    private String exceptionText;
    /**
    * 
    */
    private String moreInfo;
    /**
     * 
     */
    private String userMessage;

    /**
     * Gets the value of the exceptionId property.
     * 
     * @return
     * 
     */
    public String getExceptionId() {
        return exceptionId;
    }

    /**
     * Sets the value of the exceptionId property.
     * 
     * @param value
     * 
     */
    public void setExceptionId(String value) {
        this.exceptionId = value;
    }

    /**
     * Gets the value of the exceptionText property.
     * 
     * @return
     * 
     */
    public String getExceptionText() {
        return exceptionText;
    }

    /**
     * Sets the value of the exceptionText property.
     * 
     * @param value
     * 
     */
    public void setExceptionText(String value) {
        this.exceptionText = value;
    }

    /**
     * Gets the value of the moreInfo property.
     * 
     * @return
     * 
     */
    public String getMoreInfo() {
        return moreInfo;
    }

    /**
     * Sets the value of the moreInfo property.
     * 
     * @param value
     * 
     */
    public void setMoreInfo(String value) {
        this.moreInfo = value;
    }

    /**
     * Gets the value of the userMessage property.
     * 
     * @return
     * 
     * 
     */
    public String getUserMessage() {
        return userMessage;
    }

    /**
     * Sets the value of the userMessage property.
     * 
     * @param value
     * 
     */
    public void setUserMessage(String value) {
        this.userMessage = value;
    }

}
