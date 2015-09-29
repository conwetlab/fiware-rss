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

/**
 * 
 */
public class RSSException extends Exception {

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Status exception.
     */
    private final InterfaceExceptionType exceptionType;

    /**
     * Additional info.
     */
    private String moreInfo;

    /**
     * User message.
     */
    private String userMessage;

    /**
     * Constructor for exception.
     * 
     * @param msg
     *            Generic message
     */
    public RSSException(final String msg) {
        super(msg);
        this.exceptionType = RSSExceptionType.GENERIC_EXCEPTION;
    }

    /**
     * Constructor for exception.
     * 
     * @param cause
     */
    public RSSException(final Throwable cause) {
        super(cause);
        this.exceptionType = RSSExceptionType.GENERIC_EXCEPTION;
    }

    /**
     * Constructor for exception.
     * 
     * @param exceptionType
     *            exception type
     * @param args
     *            arguments for configure message
     */
    public RSSException(final InterfaceExceptionType exceptionType, final Object[] args) {
        super(String.format(exceptionType.getFormatText(), args));
        this.exceptionType = exceptionType;
    }

    /**
     * Constructor for exception.
     * 
     * @param exceptionType
     *            exception type
     * @param args
     *            arguments for configure message
     */
    public RSSException(final InterfaceExceptionType exceptionType, final Object[] args, String moreInfo,
        String userMessage) {
        super(String.format(exceptionType.getFormatText(), args));
        this.exceptionType = exceptionType;
        this.moreInfo = moreInfo;
        this.userMessage = userMessage;
    }

    /**
     * Greta exception with custom msg.
     * 
     * @param exceptionType
     * @param msg
     */
    public RSSException(final InterfaceExceptionType exceptionType, String msg) {
        super(msg);
        this.exceptionType = exceptionType;
    }

    /**
     * @param exceptionType
     * @param msg
     * @param moreInfo
     * @param userMessage
     */
    public RSSException(final InterfaceExceptionType exceptionType, String msg, String moreInfo, String userMessage) {
        super(msg);
        this.exceptionType = exceptionType;
        this.moreInfo = moreInfo;
    }

    /**
     * Constructor for exception.
     * 
     * @param exceptionType
     *            exception type
     * @param args
     *            arguments for configure message
     */
    public RSSException(final InterfaceExceptionType exceptionType, final Object[] args, Throwable cause) {
        super(String.format(exceptionType.getFormatText(), args), cause);
        this.exceptionType = exceptionType;
    }

    /**
     * Constructor for exception with moreInfo and userMessage.
     * 
     * @param exceptionType
     *            exception type
     * @param args
     *            arguments for configure message
     * @param cause
     * @param moreInfo
     * @param userMessage
     */
    public RSSException(final InterfaceExceptionType exceptionType, final Object[] args, Throwable cause,
        String moreInfo, String userMessage) {
        super(String.format(exceptionType.getFormatText(), args), cause);
        this.exceptionType = exceptionType;
        this.moreInfo = moreInfo;
        this.userMessage = userMessage;
    }

    /**
     * @return the status.
     */
    public InterfaceExceptionType getExceptionType() {
        return exceptionType;
    }

    /**
     * @return the moreInfo field.
     */
    public String getMoreInfo() {
        return moreInfo;
    }

    /**
     * moreInfo setter.
     * 
     * @param info
     */
    public void setMoreInfo(String info) {
        moreInfo = info;
    }

    /**
     * @return the userMessage field.
     */
    public String getUserMessage() {
        return userMessage;
    }

    /**
     * userMessage setter.
     * 
     * @param msg
     */
    public void setUserMessage(String msg) {
        userMessage = msg;
    }
}
