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

/*
 * DbeAppProvider.java
 * 
 * 2013 ®, Telefónica I+D, all rights reserved
 */
package es.upm.fiware.rss.model;

import java.util.Date;
import java.util.Set;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * DbeAppProvider.
 * 
 */
@Entity
@Table(name = "dbe_appprovider")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class DbeAppProvider implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private DbeAppProviderId id;
    private String txName;
    private Integer txCorrelationNumber;
    private Date txTimeStamp;
    private Set<SetRevenueShareConf> models;
    private Set<SharingReport> reports;

    /**
     * 
     */
    public DbeAppProvider() {
    }

    /**
     * @param id
     * @param txName
     * @param models
     */
    public DbeAppProvider(DbeAppProviderId id, String txName,
            Set<SetRevenueShareConf> models) {

        this.id = id;
        this.txName = txName;
        this.models = models;
    }

    @EmbeddedId
    public DbeAppProviderId getId() {
        return id;
    }

    public void setId(DbeAppProviderId id) {
        this.id = id;
    }

    /**
     * @param txName
     *            the txName to set
     */
    public void setTxName(String txName) {
        this.txName = txName;
    }

    /**
     * @return the txName
     */
    @Column(name = "TX_NAME", length = 256)
    public String getTxName() {
        return txName;
    }

    @OneToMany(fetch = FetchType.LAZY, targetEntity = SetRevenueShareConf.class, mappedBy = "id.modelOwner")
    public Set<SetRevenueShareConf> getModels() {
        return models;
    }

    public void setModels(Set<SetRevenueShareConf> models) {
        this.models = models;
    }

    @OneToMany(fetch = FetchType.LAZY, targetEntity = SharingReport.class, mappedBy = "owner")
    public Set<SharingReport> getReports() {
        return reports;
    }

    public void setReports(Set<SharingReport> reports) {
        this.reports = reports;
    }

    @Column(name = "CORRELATION_NUMBER")
    public Integer getTxCorrelationNumber() {
        return txCorrelationNumber;
    }

    public void setTxCorrelationNumber(Integer txCorrelationNumber) {
        this.txCorrelationNumber = txCorrelationNumber;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TX_TIME_STAMP", length = 7)
    public Date getTxTimeStamp() {
        return txTimeStamp;
    }

    public void setTxTimeStamp(Date txTimeStamp) {
        this.txTimeStamp = txTimeStamp;
    }
    
}
