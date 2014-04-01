/**
 * Revenue Settlement and Sharing System GE
 * Copyright (C) 2011-2014,  Javier Lucio - lucio@tid.es
 * Telefonica Investigacion y Desarrollo, S.A.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package es.tid.fiware.rss.expenditureLimit.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import es.tid.fiware.rss.model.BmCurrency;
import es.tid.fiware.rss.model.BmObCountry;
import es.tid.fiware.rss.model.BmService;
import es.tid.fiware.rss.model.DbeAppProvider;

/**
 * 
 * The persistent class for the DBE_EXPEND_LIMIT database table.
 * 
 * 
 */

@Entity
@Table(name = "dbe_expend_limit")
public class DbeExpendLimit implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private DbeExpendLimitPK id;

    @Column(name = "FT_MAX_AMOUNT")
    private BigDecimal ftMaxAmount;

    @Column(name = "TX_NOTIF_AMOUNTS")
    private String txNotifAmounts;

    // bi-directional many-to-one association to BmCurrency
    @ManyToOne
    @JoinColumn(name = "NU_CURRENCY_ID", updatable = false, insertable = false)
    private BmCurrency bmCurrency;

    // bi-directional many-to-one association to BmObCountry
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "NU_COUNTRY_ID", referencedColumnName = "NU_COUNTRY_ID", updatable = false,
            insertable = false),
        @JoinColumn(name = "NU_OB_ID", referencedColumnName = "NU_OB_ID", updatable = false, insertable = false)
    })
    private BmObCountry bmObCountry;

    // bi-directional many-to-one association to BmService
    @ManyToOne
    @JoinColumn(name = "NU_SERVICE_ID", updatable = false, insertable = false)
    private BmService bmService;

    // bi-directional many-to-one association to DbeAppProvider
    @ManyToOne
    @JoinColumn(name = "TX_APPPROVIDER_ID", updatable = false, insertable = false)
    private DbeAppProvider dbeAppProvider;

    public DbeExpendLimit() {
    }

    public DbeExpendLimitPK getId() {
        return this.id;
    }

    public void setId(DbeExpendLimitPK id) {
        this.id = id;
    }

    public BigDecimal getFtMaxAmount() {
        return this.ftMaxAmount;
    }

    public void setFtMaxAmount(BigDecimal ftMaxAmount) {
        this.ftMaxAmount = ftMaxAmount;
    }

    public String getTxNotifAmounts() {
        return this.txNotifAmounts;
    }

    public void setTxNotifAmounts(String txNotifAmounts) {
        this.txNotifAmounts = txNotifAmounts;
    }

    public BmCurrency getBmCurrency() {
        return this.bmCurrency;
    }

    public void setBmCurrency(BmCurrency bmCurrency) {
        this.bmCurrency = bmCurrency;
    }

    public BmObCountry getBmObCountry() {
        return this.bmObCountry;
    }

    public void setBmObCountry(BmObCountry bmObCountry) {
        this.bmObCountry = bmObCountry;
    }

    public BmService getBmService() {
        return this.bmService;
    }

    public void setBmService(BmService bmService) {
        this.bmService = bmService;
    }

    public DbeAppProvider getDbeAppProvider() {
        return this.dbeAppProvider;
    }

    public void setDbeAppProvider(DbeAppProvider dbeAppProvider) {
        this.dbeAppProvider = dbeAppProvider;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) bmCurrency.getNuCurrencyId();
        result = prime * result + ((bmObCountry == null) ? 0 : bmObCountry.getId().hashCode());
        result = prime * result + (int) bmService.getNuServiceId();
        result = prime * result + ((dbeAppProvider == null) ? 0 : dbeAppProvider.getTxAppProviderId().hashCode());
        result = prime * result + ((ftMaxAmount == null) ? 0 : ftMaxAmount.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((txNotifAmounts == null) ? 0 : txNotifAmounts.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        DbeExpendLimit other = (DbeExpendLimit) obj;
        if (bmCurrency == null) {
            if (other.bmCurrency != null) {
                return false;
            }
        } else if (bmCurrency.getNuCurrencyId() != other.bmCurrency.getNuCurrencyId()) {
            return false;
        }
        if (bmObCountry == null) {
            if (other.bmObCountry != null) {
                return false;
            }
        } else if (!bmObCountry.getId().equals(other.bmObCountry.getId())) {
            return false;
        }
        if (bmService == null) {
            if (other.bmService != null) {
                return false;
            }
        } else if (bmService.getNuServiceId() != other.bmService.getNuServiceId()) {
            return false;
        }
        if (dbeAppProvider == null) {
            if (other.dbeAppProvider != null) {
                return false;
            }
        } else if (!dbeAppProvider.getTxAppProviderId().equals(other.dbeAppProvider.getTxAppProviderId())) {
            return false;
        }
        if (ftMaxAmount == null) {
            if (other.ftMaxAmount != null) {
                return false;
            }
        } else if (ftMaxAmount.compareTo(other.ftMaxAmount) != 0) {
            return false;
        }
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        if (txNotifAmounts == null) {
            if (other.txNotifAmounts != null) {
                return false;
            }
        } else if (!txNotifAmounts.equals(other.txNotifAmounts)) {
            return false;
        }
        return true;
    }

}