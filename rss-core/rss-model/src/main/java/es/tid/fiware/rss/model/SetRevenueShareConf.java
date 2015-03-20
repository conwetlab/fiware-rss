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

package es.tid.fiware.rss.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "set_revenue_share_conf")
public class SetRevenueShareConf implements Serializable{

    // Composite id of the revenue sharing model
    private SetRevenueShareConfId id;

    // Type of algorithm that is going to be used
    private String algorithmType;

    // Store (Aggregator) that is part of the revenue model
    private RevenueShareAggregator shareAggregator;

    // Application provider which is the owner of the revenue sharing model
    private DbeAppProvider modelOwner;

    // List of stakeholders (AppProviders) involved in the revenue sharing model
    // including its sharing value
    private Set<ModelProvider> stakeholders;

    /**
     * Constructor.
     */
    public SetRevenueShareConf() {
    }

    /**
     * Constructor.
     * 
     * @param id
     * @param algorithmType
     * @param shareAggregator
     * @param modelOwner
     * @param stakeholders
     * @param aggregatorPerc
     */
    public SetRevenueShareConf(SetRevenueShareConfId id, String algorithmType,
            RevenueShareAggregator shareAggregator, DbeAppProvider modelOwner,
            Set<ModelProvider> stakeholders,BigDecimal aggregatorPerc) {

        this.id = id;
        this.algorithmType = algorithmType;
        this.shareAggregator = shareAggregator;
        this.modelOwner = modelOwner;
        this.stakeholders = stakeholders;
    }

    @EmbeddedId
    @AttributeOverrides({
        @AttributeOverride(name = "txAppProviderId", column = @Column(name = "TX_APPPROVIDER_ID", nullable = false,
            length = 50)),
        @AttributeOverride(name = "nuCountryId", column = @Column(name = "NU_COUNTRY_OB_ID", nullable = false,
            precision = 10, scale = 0)),
        @AttributeOverride(name = "txProductClass", column = @Column(name = "TX_PRODUCT_CLASS", nullable = true,
            length = 40))
    })
    public SetRevenueShareConfId getId() {
        return this.id;
    }

    public void setId(SetRevenueShareConfId id) {
        this.id = id;
    }

    @Column(name = "ALGORITHM_TYPE", length = 255)
    public String getAlgorithmType() {
        return this.algorithmType;
    }

    public void setAlgorithmType(String algorithmType) {
        this.algorithmType = algorithmType;
    }

    @OneToMany(fetch = FetchType.EAGER, targetEntity = ModelProvider.class)
    public Set<ModelProvider> getStakeholders() {
        return this.stakeholders;
    }

    public void setStakeholders(Set<ModelProvider> stakeholders) {
        this.stakeholders = stakeholders;
    }

    @Embedded
    public RevenueShareAggregator getShareAggregator() {
        return this.shareAggregator;
    }

    public void setShareAggregator(RevenueShareAggregator shareAggregator) {
        this.shareAggregator = shareAggregator;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MODEL_OWNER_PROVIDER", nullable = false, insertable = false, updatable = false)
    public DbeAppProvider getModelOwner() {
        return modelOwner;
    }

    public void setModelOwner(DbeAppProvider modelOwner) {
        this.modelOwner = modelOwner;
    }

    
}
