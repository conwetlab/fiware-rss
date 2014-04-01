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

/**
 * 
 */
package es.tid.fiware.rss.expenditureLimit.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * 
 * The primary key class for the DBE_EXPEND_LIMIT database table.
 * 
 * 
 */

@Embeddable
public class DbeExpendLimitPK implements Serializable {

    // default serial version id, required for serializable classes.

    private static final long serialVersionUID = 1L;

    @Column(name = "NU_SERVICE_ID")
    private long nuServiceId;

    @Column(name = "TX_END_USER_ID")
    private String txEndUserId;

    @Column(name = "TX_APPPROVIDER_ID")
    private String txAppProviderId;

    @Column(name = "NU_CURRENCY_ID")
    private long nuCurrencyId;

    @Column(name = "TX_EL_TYPE")
    private String txElType;

    @Column(name = "NU_OB_ID")
    private long nuObId;

    @Column(name = "NU_COUNTRY_ID")
    private long nuCountryId;

    public DbeExpendLimitPK() {
    }

    public long getNuServiceId() {
        return this.nuServiceId;
    }

    public void setNuServiceId(long nuServiceId) {
        this.nuServiceId = nuServiceId;
    }

    public String getTxEndUserId() {
        return this.txEndUserId;
    }

    public void setTxEndUserId(String txEndUserId) {
        this.txEndUserId = txEndUserId;
    }

    public String getTxAppProviderId() {
        return this.txAppProviderId;
    }

    public void setTxAppProviderId(String txAppProviderId) {
        this.txAppProviderId = txAppProviderId;
    }

    public long getNuCurrencyId() {
        return this.nuCurrencyId;
    }

    public void setNuCurrencyId(long nuCurrencyId) {
        this.nuCurrencyId = nuCurrencyId;
    }

    public String getTxElType() {
        return this.txElType;
    }

    public void setTxElType(String txElType) {
        this.txElType = txElType;
    }

    public long getNuObId() {
        return this.nuObId;
    }

    public void setNuObId(long nuObId) {
        this.nuObId = nuObId;
    }

    public long getNuCountryId() {
        return this.nuCountryId;
    }

    public void setNuCountryId(long nuCountryId) {
        this.nuCountryId = nuCountryId;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof DbeExpendLimitPK)) {
            return false;
        }
        DbeExpendLimitPK castOther = (DbeExpendLimitPK) other;

        return (this.nuServiceId == castOther.nuServiceId)
            && this.txEndUserId.equals(castOther.txEndUserId)
            && this.txAppProviderId.equals(castOther.txAppProviderId)
            && (this.nuCurrencyId == castOther.nuCurrencyId)
            && this.txElType.equals(castOther.txElType)
            && (this.nuObId == castOther.nuObId)
            && (this.nuCountryId == castOther.nuCountryId);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 17;

        hash = hash * prime + ((int) (this.nuServiceId ^ (this.nuServiceId >>> 32)));
        hash = hash * prime + this.txEndUserId.hashCode();
        hash = hash * prime + this.txAppProviderId.hashCode();
        hash = hash * prime + ((int) (this.nuCurrencyId ^ (this.nuCurrencyId >>> 32)));
        hash = hash * prime + this.txElType.hashCode();
        hash = hash * prime + ((int) (this.nuObId ^ (this.nuObId >>> 32)));
        hash = hash * prime + ((int) (this.nuCountryId ^ (this.nuCountryId >>> 32)));

        return hash;
    }
}