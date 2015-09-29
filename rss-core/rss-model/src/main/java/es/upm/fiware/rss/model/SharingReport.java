/**
 * Revenue Settlement and Sharing System GE
 * Copyright (C) 2011-2014, Javier Lucio - lucio@tid.es
 * Telefonica Investigacion y Desarrollo, S.A.
 * 
 * Copyright (C) 2015 CoNWeT Lab., Universidad Politécnica de Madrid
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
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "sharing_report")
public class SharingReport implements Serializable{

    // Composite id of the sharing report
    private int id;
    private String productClass;

    // Type of algorithm that is going to be used
    private String algorithmType;
    private Date date;

    // Owner of the applied revenue sharing model
    private DbeAppProvider owner;
    private BigDecimal aggregatorValue;

    // Value applied for the owner in the RS models
    private BigDecimal ownerValue;

    private BmCurrency currency;

    // List of stakeholders (AppProviders) involved in the revenue sharing model
    // including its sharing value
    private Set<ReportProvider> stakeholders;

    /**
     * Constructor.
     */
    public SharingReport() {
    }

    /**
     * Constructor.
     * 
     * @param id
     * @param algorithmType
     * @param owner
     * @param aggregatorValue
     * @param ownerValue
     * @param stakeholders
     * @param aggregatorPerc
     */
    public SharingReport(Integer id, String algorithmType,
            DbeAppProvider owner, BigDecimal aggregatorValue,
            BigDecimal ownerValue, Set<ReportProvider> stakeholders,BigDecimal aggregatorPerc) {

        this.id = id;
        this.algorithmType = algorithmType;
        this.owner = owner;
        this.aggregatorValue = aggregatorValue;
        this.ownerValue = ownerValue;
        this.stakeholders = stakeholders;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "PRODUCT_CLASS", length = 255)
    public String getProductClass() {
        return productClass;
    }

    public void setProductClass(String productClass) {
        this.productClass = productClass;
    }

    @Column(name = "ALGORITHM_TYPE", length = 255)
    public String getAlgorithmType() {
        return this.algorithmType;
    }

    public void setAlgorithmType(String algorithmType) {
        this.algorithmType = algorithmType;
    }

    @OneToMany(fetch = FetchType.LAZY, targetEntity = ReportProvider.class, mappedBy = "id.report")
    public Set<ReportProvider> getStakeholders() {
        return this.stakeholders;
    }

    public void setStakeholders(Set<ReportProvider> stakeholders) {
        this.stakeholders = stakeholders;
    }

    @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumns({
            @JoinColumn(name = "OWNER_ID", referencedColumnName = "TX_APPPROVIDER_ID"),
            @JoinColumn(name = "AGGREGATOR_ID", referencedColumnName = "TX_AGGREGATOR_ID")})
    public DbeAppProvider getOwner() {
        return this.owner;
    }

    public void setOwner(DbeAppProvider owner) {
        this.owner = owner;
    }

    @Column(name = "AGGREGATOR_VALUE", nullable = false, precision = 5, scale = 0)
    public BigDecimal getAggregatorValue() {
        return this.aggregatorValue;
    }

    public void setAggregatorValue (BigDecimal aggregatorValue) {
        this.aggregatorValue = aggregatorValue;
    }

    @Column(name = "OWNER_VALUE", nullable = false, precision = 5, scale = 0)
    public BigDecimal getOwnerValue() {
        return this.ownerValue;
    }

    public void setOwnerValue(BigDecimal ownerValue) {
        this.ownerValue = ownerValue;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TIME_STAMP", length = 7)
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CURRENCY_ID")
    public BmCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(BmCurrency currency) {
        this.currency = currency;
    }
}
