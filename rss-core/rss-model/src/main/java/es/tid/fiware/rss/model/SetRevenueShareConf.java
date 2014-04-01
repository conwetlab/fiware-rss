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

package es.tid.fiware.rss.model;

import java.math.BigDecimal;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "set_revenue_share_conf")
public class SetRevenueShareConf {

    private SetRevenueShareConfId id;
    private BigDecimal nuPercRevenueShare;
    private DbeAppProvider appProvider;

    /**
     * Constructor.
     */
    public SetRevenueShareConf() {
    }

    /**
     * Constructor.
     * 
     * @param id
     * @param bmProduct
     * @param bmServiceDeployment
     */
    public SetRevenueShareConf(SetRevenueShareConfId id, DbeAppProvider appProvider, BigDecimal nuPercRevenueShare) {
        this.id = id;
        this.appProvider = appProvider;
        this.nuPercRevenueShare = nuPercRevenueShare;
    }

    @EmbeddedId
    @AttributeOverrides({
        @AttributeOverride(name = "txAppProviderId", column = @Column(name = "TX_APPPROVIDER_ID", nullable = false,
            length = 50)),
        @AttributeOverride(name = "nuObId", column = @Column(name = "NU_OB_ID", nullable = false,
            precision = 10, scale = 0)),
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

    @Column(name = "NU_PERC_REVENUE_SHARE", nullable = false, precision = 5, scale = 0)
    public BigDecimal getNuPercRevenueShare() {
        return nuPercRevenueShare;
    }

    public void setNuPercRevenueShare(BigDecimal nuPercRevenueShare) {
        this.nuPercRevenueShare = nuPercRevenueShare;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TX_APPPROVIDER_ID", nullable = false, insertable = false, updatable = false)
    public DbeAppProvider getAppProvider() {
        return appProvider;
    }

    public void setAppProvider(DbeAppProvider appProvider) {
        this.appProvider = appProvider;
    }

}
