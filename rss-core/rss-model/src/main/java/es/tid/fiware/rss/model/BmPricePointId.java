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

package es.tid.fiware.rss.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the BM_PRICE_POINT database table.
 * 
 */
@Embeddable
public class BmPricePointId implements Serializable {
    // default serial version id, required for serializable classes.
    private static final long serialVersionUID = 1L;

    @Column(name = "TX_PRICE_POINT_ID", nullable = false, length = 64)
    private String txPricePointId;

    @Column(name = "NU_COUNTRY_ID", nullable = false, precision = 10, scale = 0)
    private long nuCountryId;

    @Column(name = "NU_OB_ID", nullable = false, precision = 10, scale = 0)
    private long nuObId;

    /**
     * Constructor.
     */
    public BmPricePointId() {
    }

    public String getTxPricePointId() {
        return this.txPricePointId;
    }

    public void setTxPricePointId(String txPricePointId) {
        this.txPricePointId = txPricePointId;
    }

    public long getNuCountryId() {
        return this.nuCountryId;
    }

    public void setNuCountryId(long nuCountryId) {
        this.nuCountryId = nuCountryId;
    }

    public long getNuObId() {
        return this.nuObId;
    }

    public void setNuObId(long nuObId) {
        this.nuObId = nuObId;
    }

    /**
     * Overriden.
     * 
     * @param other
     * @return boolean
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof BmPricePointId)) {
            return false;
        }
        BmPricePointId castOther = (BmPricePointId) other;
        return this.txPricePointId.equals(castOther.txPricePointId)
            && (this.nuCountryId == castOther.nuCountryId)
            && (this.nuObId == castOther.nuObId);

    }

    /**
     * Overriden.
     * 
     * @return int
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.txPricePointId.hashCode();
        hash = hash * prime + ((int) (this.nuCountryId ^ (this.nuCountryId >>> 32)));
        hash = hash * prime + ((int) (this.nuObId ^ (this.nuObId >>> 32)));

        return hash;
    }
}
