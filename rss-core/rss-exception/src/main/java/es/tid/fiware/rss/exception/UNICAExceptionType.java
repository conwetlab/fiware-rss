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

package es.tid.fiware.rss.exception;

import org.springframework.http.HttpStatus;

/***
 * This class is an enum to control the possible exception types for UNICA.
 * 
 */
public enum UNICAExceptionType implements InterfaceExceptionType {
    /* Service exception */

    /***
     * API Request without mandatory field: UserID, UE ID.
     */
    MISSING_MANDATORY_PARAMETER("SVC1000", "Missing mandatory parameter: %s", HttpStatus.BAD_REQUEST),

    /***
     * API Request with an element not conforming to XML Schema or to list of allowed Query Parameters.
     */
    INVALID_PARAMETER("SVC1001", "Invalid parameter: %s", HttpStatus.BAD_REQUEST),

    /***
     * API Request with an element or attribute value not conforming to XML Schema definition or to list of allowed
     * Query Parameter values.
     */
    INVALID_INPUT_VALUE("SVC0002", "Invalid parameter value: %s", HttpStatus.BAD_REQUEST),

    /***
     * API Request with an element or attribute value not conforming to XML Schema or to list of allowed Query Parameter
     * values, because the element or attribute value belongs to an enumerated list of possible values.
     */
    INVALID_INPUT_LIST_VALUE("SVC0003", "Invalid parameter value: %s. Possible values are: %s", HttpStatus.BAD_REQUEST),

    /***
     * Requested API port or resource does not exist.
     */
    INVALID_REQUEST_URI("SVC0003", "Requested URI does not exist: %s", HttpStatus.NOT_FOUND),

    /***
     * Requested Operation does not exist.
     */
    INVALID_REQUEST_OPERATION("SVC1003", "Requested Operation does not exist: %s", HttpStatus.BAD_REQUEST),

    /***
     * UNICA API Generic, wildcard fault response.
     */
    GENERIC_CLIENT_ERROR("SVC0001", "Generic Client Error: %s", HttpStatus.BAD_REQUEST),

    /***
     * Correlator specified in a request message is invalid.
     */
    DUPLICATE_CORRELATOR("SVC0005", "Correlator %s specified in message part %s is a duplicate", HttpStatus.BAD_REQUEST),

    /***
     * UNICA API error response for requests over a deprecated versión of the API.
     */
    DEPRECATED_API_VERSION("SVC1004", "Requested version of API is deprecated. Use %s", HttpStatus.BAD_REQUEST),

    /***
     * Request with a non existing User ID within the body fields. Note: in case of wrong authenticated User ID
     * (headers) an “Invalid Requestor Id�? security exception will be thrown instead.
     */
    NON_EXISTENT_SUBSCRIBER("SVC1005", "User does not exist: %s", HttpStatus.NOT_FOUND),

    /***
     * Polling over a resource identifier which does not exist.
     */
    NON_EXISTENT_RESOURCE_ID("SVC1006", "Resource %s does not exist", HttpStatus.NOT_FOUND),

    /***
     * Requested operation failed because it couldn’t be charged for a quota problem.
     */
    QUOTA_EXCEEDED("SVC1009", "Quota Exceeded", HttpStatus.FORBIDDEN),

    /***
     * Operation syntax is right, but it does not fulfil the conditions (permissions,...) of the scenario or service.
     */
    NON_ALLOWED_OPERATION("SVC1013", "%s Operation is not allowed: %s", HttpStatus.FORBIDDEN),

    /***
     * UNICA API error response for requests with the absence of some expected parameter. I.e.: a parameter that is
     * needed in certain use case, even when the parameter is optional in XSD/WSDL.
     */
    EXPECTED_PARAMETER_MISSING("SVC1020", "Needed parameter was not found. %s", HttpStatus.BAD_REQUEST),

    /***
     * The body of a SOAP or REST request is not correctly formed, i.e.: the XML or JSON body is not well formed.
     */
    CONTENT_NOT_WELL_FORMED("SVC1023", "Parser Error: %s content not well formed", HttpStatus.BAD_REQUEST),

    /***
     * The operation failed, and the charge was not applied. This would be the typical case that the end-user has not
     * enough credit or some restriction on their account.
     */
    CHARGE_FAILED("SVC3700", "Charge request failed when attempting it: %s", HttpStatus.FORBIDDEN),

    /***
     * Returned when the currency code included in the purchase information is invalid according to ISO specification
     * 4217 or the virtual units identifiers that the operator consider.
     */
    CURRENCY_CODE_INVALID("SVC3701", "Invalid Currency or Virtual Units code: %s", HttpStatus.FORBIDDEN),

    /***
     * Returned when the taxes indicated in the purchase information are invalid for whatever reason (they do not
     * correspond with the legal percentage value, etc.).
     */
    TAXES_INVALID("SVC3702", "Indicated taxes are invalid: %s", HttpStatus.FORBIDDEN),

    /***
     * The code specified within the purchase information is not valid.
     */
    INVALID_CODE("SVC3703", "Invalid purchase code: %s", HttpStatus.FORBIDDEN),

    /***
     * The value specified within this parameter is wrong.
     */
    INVALID_TRANSACTIONID("SVC3704", "Invalid Transaction Id: %s", HttpStatus.NOT_FOUND),

    /***
     * The value specified within this parameter is wrong.
     */
    INSUFFICIENT_MOP_BALANCE("SVC3705", "Insufficient payment method balance: %s", HttpStatus.NOT_FOUND),

    /***
     * The value specified within this parameter is wrong.
     */
    INVALID_TAXES("SVC3706", "amount + taxAmount must be equal to totalAmount %s", HttpStatus.FORBIDDEN),

    /***
     * There was a problem with the card used for the payment operation. The card is not usable for payments (i.e.
     * expired, unknown, invalid, blocked).
     */
    CARD_ERROR("SVC3707", "Card error.", HttpStatus.FORBIDDEN),

    /***
     * The billing system reports that refund was impossible without further information.
     */
    REFUND_IMPOSSIBLE("SVC3708", "Refund impossible.", HttpStatus.FORBIDDEN),

    /***
     * The billing system reports that a previous operation is already in progress (not finished) and so this new
     * request is discarded.
     */
    PAYPMENT_OPERATION_ALREADY_IN_PROGRESS("SVC3709", "Operation already in progress.", HttpStatus.FORBIDDEN),

    /***
     * The billing system does not admit any further refunds (total or parcial) on the specified transaction.
     */
    TRANSACTION_ALREADY_REFUNDED("SVC3710", "Transaction already refunded.", HttpStatus.FORBIDDEN),

    /*
     * Policy exception.
     */

    /***
     * UNICA API Request over an unallowed API or unallowed information.
     */
    ACCESS_SCOPE_VIOLATION("POL1000", "Restricted Information: %s", HttpStatus.FORBIDDEN),

    /***
     * API Request attempts to include a User Identity on behalf of whom the request is being made.
     */
    REQUESTOR_IDENTITY_NOT_ALLOWED("POL1001", "Requestors are not authorized %s", HttpStatus.FORBIDDEN),

    /***
     * The API Consumer is not a trusted partner. API request attempts to include an unreliable User Identity
     */
    ACCESS_TOKEN_REQUIRED("POL1002", "Access Token required: %s", HttpStatus.FORBIDDEN),

    /***
     * Consumer login access mode not permisible.
     */
    CONSUMER_LOGIN_NOT_ALLOWED("POL1003", "Session authentication not allowed to User Id: %s", HttpStatus.FORBIDDEN),

    /***
     * Taxes indicated are not applicable because the policy is “Delegated Invoicing�?. Operator must apply the VAT
     * itself and produce the final invoice for the end-user.
     */
    TAXES_NOT_APPLICABLE("POL3700", "Merchant is not allowed to apply taxes: %s", HttpStatus.FORBIDDEN),

    /***
     * Although the currency may belong to ISO 4217, it does not belong to the values agreed with the operator.
     */
    CURRENCY_CODE_UNALLOWED("POL3701", "This currency code is not allowed by the operator: %s", HttpStatus.FORBIDDEN),

    /***
     * It indicates whether the one-off charge limit, or the cumulative charge for a given time period, has been
     * reached.
     */
    CHARGEABLE_AMOUNT_EXCEEDED("POL3702", "Chargeable amount exceeded. %s", HttpStatus.FORBIDDEN),

    /***
     * It indicates a refund request as failed.
     */
    REFUND_FAILED("POL3703", "Refund request failed: %s", HttpStatus.FORBIDDEN),

    /***
     * It indicates a credit request as failed.
     */
    CREDIT_FAILED("POL3704", "Credit request failed: %s", HttpStatus.FORBIDDEN),

    /***
     * It indicates a reservation request as failed.
     */
    RESERVATION_FAILED("POL3705", "Reservation request failed: %s", HttpStatus.FORBIDDEN),

    /***
     * It indicates a payment operations was refused by user.
     */
    PAYMENT_OPERATION_FAILED("POL3706", "Payment operation refused by user. %s", HttpStatus.FORBIDDEN),

    /***
     * It indicates that operation didn’t take effect because it exceeds the maximum allowed number of captures per
     * purchase or subscription period.
     */
    MAXIMUN_ALLOWED_CAPTURES_THRESHOLD_EXCEEDED("POL3707", "Maximum allowed captured exceeded. %s",
        HttpStatus.FORBIDDEN),
    /***
     * It indicates that charging this user is not allowed. (e.g. account might be blocked).
     */
    CHARGES_NOT_ALLOWED_FOR_THIS_USER("POL3709", "Charges not allowed for this user. %s", HttpStatus.FORBIDDEN),
    /***
     * It indicates that operation didn’t take effect because it exceeds the maximum allowed number of captures per
     * purchase or subscription period.
     */
    INVALID_NUMBER_DECIMALS("POL3710", "Not supported number of digits after decimal point in %s.",
        HttpStatus.FORBIDDEN),
    /***
     * It indicates that charging this user is not allowed because the account is not enabled in the payment service.
     */
    USERS_ACCOUNT_NOT_ENABLED("POL3711", "User’s account not enabled. %s", HttpStatus.FORBIDDEN),
    /***
     * It indicates that charging this user is not allowed because the account is suspended in the payment service.
     */
    USERS_ACCOUNT_SUSPENDED("POL3712", "User’s account suspended. %s", HttpStatus.FORBIDDEN),

    /***
     * It indicates the cumulative charge for a given time period has been reached.
     */
    CUMULATIVE_CHARGEABLE_AMOUNT_EXCEEDED("POL3713", "Cumulative chargeable amount exceeded. %s.", HttpStatus.FORBIDDEN),

    /*
     * Security exception.
     */

    /***
     * Request includes a wrong consumer identification.
     */
    INVALID_CONSUMER_KEY("SEC1000", "Invalid Consumer Id: %s", HttpStatus.UNAUTHORIZED),

    /***
     * Request has not successfully passed consumer authentication procedures.
     */
    INVALID_CONSUMER_SIGNATURE("SEC1001", "Invalid Consumer Signature. Consumer Authentication Failed: %s",
        HttpStatus.UNAUTHORIZED),

    /***
     * Request has not successfully passed security validations, when login access mode is used, due to wrong or missing
     * session token , as described in [2].
     */
    INVALID_CONSUMER_SESSION_TOKEN("SEC1002", "Invalid or Missing Session Token. Consumer authentication failed: %s",
        HttpStatus.UNAUTHORIZED),

    /***
     * Request has not successfully passed security validations due to wrong or invalid Requestor Id [2]. Request has
     * not successfully passed user authentication procedures (trusted 3 legged mode) due to wrong xoauth_requestor_id.
     */
    INVALID_REQUESTOR_ID("SEC1003", "Invalid Requestor: %s", HttpStatus.UNAUTHORIZED),

    /***
     * Request has not successfully passed security validations due to wrong or missing Token as described in [2].
     */
    INVALID_OAUTH_TOKEN("SEC1004", "Invalid Token: %s", HttpStatus.UNAUTHORIZED),

    /***
     * Request has not successfully passed security validations due to wrong nonce parameter. Request has not
     * successfully passed user authentication procedures (trusted 3 legged mode) due to wrong oauth_nonce or nonce.
     */
    INVALID_NONCE("SEC1005", "Invalid Nonce: %s", HttpStatus.UNAUTHORIZED),

    /***
     * API request contains an expired oauth_token when No-Login mode is used, as described in [2].
     */
    OAUTH_TOKEN_EXPIRED("SEC1006", "Expired oAuth Token: %s", HttpStatus.UNAUTHORIZED),

    /***
     * Login access mode is applicable, but API Request contains an expired session_token.
     */
    SESSION_TOKEN_EXPIRED("SEC1007", "Expired Authentication Session: %s", HttpStatus.UNAUTHORIZED),

    /***
     * Request has invalid security parameters present in message headers. NOTE: If any of the above errors match, they
     * have precedence over this.
     */
    INVALID_SECURITY_HEADER("SEC1008", "Invalid Security Header: %s", HttpStatus.UNAUTHORIZED),

    /***
     * There was a non specific security problem during the request processing.
     */
    GENERIC_SECURITY_FAULT("SEC1011", "Generic security error. %s", HttpStatus.UNAUTHORIZED),

    /*
     * Server exception.
     */

    /***
     * There was a problem in the Service Providers network that prevented to carry out the request.
     */
    GENERIC_SERVER_FAULT("SVR1000", "Generic Service Error: %s", HttpStatus.INTERNAL_SERVER_ERROR),

    /**
     * There was a problem in the Service Charging process that prevented to carry out the request.
     */
    GENERIC_CHARGIN_FAULT("SVR1005", "Generic Charging Error: %s", HttpStatus.INTERNAL_SERVER_ERROR),

    /**
     * There was a problem in the server side while processing the request. It’s allowed to retry with the same request
     * parameters. However if the application does not finally retry within a configured period of time the system will
     * roll back to the original status.
     */
    ERROR_RETRY("SVR1007", "Server Error in Request Processing, retry is allowed: %s", HttpStatus.INTERNAL_SERVER_ERROR),

    /**
     * There was a timeout when waiting for operation confirmation from backend systems. The result of the operation in
     * the billing system is unknown.
     */
    TIMEOUT_ON_OPERATION_CONFIRMATION("SVR3700", "Timeout: operation status unknown: %s",
        HttpStatus.INTERNAL_SERVER_ERROR);

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

    @Override
    public String getExceptionId() {
        return this.exceptionId;
    }

    @Override
    public String getFormatText() {
        return this.formatText;
    }

    /*
     * public void setFormatText(String formatText) { this.formatText = formatText; }
     */

    @Override
    public HttpStatus getStatus() {
        return this.status;
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
    private UNICAExceptionType(final String exceptionId, final String text, final HttpStatus statusHTTP) {
        this.exceptionId = exceptionId;
        this.formatText = text;
        this.status = statusHTTP;
    }

}
