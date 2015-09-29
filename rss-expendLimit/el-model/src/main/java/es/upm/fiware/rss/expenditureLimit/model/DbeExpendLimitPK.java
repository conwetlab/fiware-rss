/**
 * Revenue Settlement and Sharing System GE
 * Copyright (C) 2011-2014,  Javier Lucio - lucio@tid.es
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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * 
 */
package es.upm.fiware.rss.expenditureLimit.model;

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

    @Column(name = "TX_END_USER_ID")
    private String txEndUserId;

    @Column(name = "TX_AGGREGATOR_ID")
    private String txAggregatorId;

    @Column(name = "TX_APPPROVIDER_ID")
    private String txAppProviderId;

    @Column(name = "NU_CURRENCY_ID")
    private long nuCurrencyId;

    @Column(name = "TX_EL_TYPE")
    private String txElType;

    public DbeExpendLimitPK() {
    }

    public String getTxEndUserId() {
        return this.txEndUserId;
    }

    public void setTxEndUserId(String txEndUserId) {
        this.txEndUserId = txEndUserId;
    }

    public String getTxAggregatorId() {
        return txAggregatorId;
    }

    public void setTxAggregatorId(String txAggregatorId) {
        this.txAggregatorId = txAggregatorId;
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

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof DbeExpendLimitPK)) {
            return false;
        }
        DbeExpendLimitPK castOther = (DbeExpendLimitPK) other;

        return this.txEndUserId.equals(castOther.txEndUserId)
            && this.txAggregatorId.equals(castOther.txAggregatorId)
            && this.txAppProviderId.equals(castOther.txAppProviderId)
            && (this.nuCurrencyId == castOther.nuCurrencyId)
            && this.txElType.equals(castOther.txElType);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 17;

        hash = hash * prime + this.txEndUserId.hashCode();
        hash = hash * prime + this.txAggregatorId.hashCode();
        hash = hash * prime + this.txAppProviderId.hashCode();
        hash = hash * prime + ((int) (this.nuCurrencyId ^ (this.nuCurrencyId >>> 32)));
        hash = hash * prime + this.txElType.hashCode();

        return hash;
    }
}