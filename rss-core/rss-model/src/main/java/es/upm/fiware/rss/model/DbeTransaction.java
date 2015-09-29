/**
 * Revenue Settlement and Sharing System GE
 * Copyright (C) 2011-2014, Javier Lucio - lucio@tid.es
 * Telefonica Investigacion y Desarrollo, S.A.
 *
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

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.CacheConcurrencyStrategy;


@Entity
@DynamicUpdate(value=true)
@DynamicInsert(value=true)
@Table(name = "dbe_transaction")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DbeTransaction implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;

    private int txTransactionId;
    private String txProductClass;
    private String state;
    private DbeAggregator cdrSource;
    private Integer txPbCorrelationId;
    private Date tsClientDate;
    private String txApplicationId;
    private String tcTransactionType;
    private String txEvent;
    private String txReferenceCode;
    private String txOperationNature;
    private BigDecimal ftChargedAmount;
    private BigDecimal ftChargedTaxAmount;
    private BmCurrency bmCurrency;
    private String txEndUserId;
    private DbeAppProvider appProvider;

    public DbeTransaction() {        
    }

    public DbeTransaction(int txTransactionId, String txProductClass, String state, 
            DbeAggregator cdrSource, Integer txPbCorrelationId, Date tsClientDate,
            String txApplicationId, String tcTransactionType, String txEvent,
            String txReferenceCode, String txOperationNature, BigDecimal ftChargedAmount,
            BigDecimal ftChargedTaxAmount, BmCurrency bmCurrency, String txEndUserId,
            DbeAppProvider appProvider) {

        this.txTransactionId = txTransactionId;
        this.txProductClass = txProductClass;
        this.state = state;
        this.cdrSource = cdrSource;
        this.txPbCorrelationId = txPbCorrelationId;
        this.tsClientDate = tsClientDate;
        this.txApplicationId = txApplicationId;
        this.tcTransactionType = tcTransactionType;
        this.txEvent = txEvent;
        this.txReferenceCode = txReferenceCode;
        this.txOperationNature = txOperationNature;
        this.ftChargedAmount = ftChargedAmount;
        this.ftChargedTaxAmount = ftChargedTaxAmount;
        this.bmCurrency = bmCurrency;
        this.txEndUserId = txEndUserId;
        this.appProvider = appProvider;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public int getTxTransactionId() {
        return txTransactionId;
    }

    public void setTxTransactionId(int txTransactionId) {
        this.txTransactionId = txTransactionId;
    }

    @Column(name = "PRODUCT_CLASS", length = 255)
    public String getTxProductClass() {
        return txProductClass;
    }

    @Column(name = "TX_STATE", length=10)
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    
    public void setTxProductClass(String txProductClass) {
        this.txProductClass = txProductClass;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SOURCE_AGGREGATOR")
    public DbeAggregator getCdrSource() {
        return cdrSource;
    }

    public void setCdrSource(DbeAggregator cdrSource) {
        this.cdrSource = cdrSource;
    }

    @Column(name = "CORRELATION_ID")
    public Integer getTxPbCorrelationId() {
        return txPbCorrelationId;
    }

    public void setTxPbCorrelationId(Integer txPbCorrelationId) {
        this.txPbCorrelationId = txPbCorrelationId;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TIME_STAMP", length = 7)
    public Date getTsClientDate() {
        return tsClientDate;
    }

    public void setTsClientDate(Date tsClientDate) {
        this.tsClientDate = tsClientDate;
    }

    @Column(name = "APPLICATION_ID", length=255)
    public String getTxApplicationId() {
        return txApplicationId;
    }

    public void setTxApplicationId(String txApplicationId) {
        this.txApplicationId = txApplicationId;
    }

    @Column(name = "TRANSACTION_TYPE", length=10)
    public String getTcTransactionType() {
        return tcTransactionType;
    }

    public void setTcTransactionType(String tcTransactionType) {
        this.tcTransactionType = tcTransactionType;
    }

    @Column(name = "TRANSACTION_EVENT", length=100)
    public String getTxEvent() {
        return txEvent;
    }

    public void setTxEvent(String txEvent) {
        this.txEvent = txEvent;
    }

    @Column(name = "REFERENCE_CODE", length=150)
    public String getTxReferenceCode() {
        return txReferenceCode;
    }

    public void setTxReferenceCode(String txReferenceCode) {
        this.txReferenceCode = txReferenceCode;
    }

    @Column(name = "OPERATION_NATURE", length=255)
    public String getTxOperationNature() {
        return txOperationNature;
    }

    public void setTxOperationNature(String txOperationNature) {
        this.txOperationNature = txOperationNature;
    }

    @Column(name = "CHARGED_AMOUNT" , precision = 8, scale = 4)
    public BigDecimal getFtChargedAmount() {
        return ftChargedAmount;
    }

    public void setFtChargedAmount(BigDecimal ftChargedAmount) {
        this.ftChargedAmount = ftChargedAmount;
    }

    @Column(name = "CHARGED_TAX_AMOUNT" , precision = 8, scale = 4, nullable = true)
    public BigDecimal getFtChargedTaxAmount() {
        return ftChargedTaxAmount;
    }

    public void setFtChargedTaxAmount(BigDecimal ftChargedTaxAmount) {
        this.ftChargedTaxAmount = ftChargedTaxAmount;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "NU_CURRENCY_ID")
    public BmCurrency getBmCurrency() {
        return bmCurrency;
    }

    public void setBmCurrency(BmCurrency bmCurrency) {
        this.bmCurrency = bmCurrency;
    }

    @Column(name = "END_USER_ID", length = 100)
    public String getTxEndUserId() {
        return txEndUserId;
    }

    public void setTxEndUserId(String txEndUserId) {
        this.txEndUserId = txEndUserId;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns({
            @JoinColumn(name = "TX_APPPROVIDER_ID", referencedColumnName = "TX_APPPROVIDER_ID"),
            @JoinColumn(name = "AGGRGATOR_ID", referencedColumnName = "TX_AGGREGATOR_ID")})
    public DbeAppProvider getAppProvider() {
        return appProvider;
    }

    public void setAppProvider(DbeAppProvider appProvider) {
        this.appProvider = appProvider;
    }
}
