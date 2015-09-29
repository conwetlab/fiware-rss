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

package es.upm.fiware.rss.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author fdelavega
 */
public class CDR {
    private String cdrSource;
    private String productClass;
    private Integer correlationNumber;
    private Date timestamp;
    private String application;
    private String transactionType;
    private String event;
    private String referenceCode;
    private String description;
    private BigDecimal chargedAmount;
    private BigDecimal chargedTaxAmount;
    private String currency;
    private String customerId;
    private String appProvider;

    public String getCdrSource() {
        return cdrSource;
    }

    public void setCdrSource(String cdrSource) {
        this.cdrSource = cdrSource;
    }

    public String getProductClass() {
        return productClass;
    }

    public void setProductClass(String productClass) {
        this.productClass = productClass;
    }

    public Integer getCorrelationNumber() {
        return correlationNumber;
    }

    public void setCorrelationNumber(Integer correlationNumber) {
        this.correlationNumber = correlationNumber;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getReferenceCode() {
        return referenceCode;
    }

    public void setReferenceCode(String referenceCode) {
        this.referenceCode = referenceCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getChargedAmount() {
        return chargedAmount;
    }

    public void setChargedAmount(BigDecimal chargedAmount) {
        this.chargedAmount = chargedAmount;
    }

    public BigDecimal getChargedTaxAmount() {
        return chargedTaxAmount;
    }

    public void setChargedTaxAmount(BigDecimal chargedTaxAmount) {
        this.chargedTaxAmount = chargedTaxAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getAppProvider() {
        return appProvider;
    }

    public void setAppProvider(String appProvider) {
        this.appProvider = appProvider;
    }
}
