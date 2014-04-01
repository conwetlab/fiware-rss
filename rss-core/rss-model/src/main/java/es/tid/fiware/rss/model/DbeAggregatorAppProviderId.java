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

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class DbeAggregatorAppProviderId implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private String txAppProviderId;
    private String txEmail;

    /**
     * Constructor.
     */
    public DbeAggregatorAppProviderId() {
    }

    /**
     * Constructor.
     * 
     * @param txAppProviderId
     * @param txEmail
     */
    public DbeAggregatorAppProviderId(String txAppProviderId, String txEmail) {
        this.txAppProviderId = txAppProviderId;
        this.txEmail = txEmail;
    }

    /**
     * @return the txAppProviderId
     */
    @Column(name = "TX_APPPROVIDER_ID", length = 50)
    public String getTxAppProviderId() {
        return txAppProviderId;
    }

    /**
     * @param txAppProviderId
     *            the txAppProviderId to set
     */
    public void setTxAppProviderId(String txAppProviderId) {
        this.txAppProviderId = txAppProviderId;
    }

    /**
     * @return the txEmail
     */
    @Column(name = "TX_EMAIL", length = 256)
    public String getTxEmail() {
        return txEmail;
    }

    /**
     * @param txEmail
     *            the txEmail to set
     */
    public void setTxEmail(String txEmail) {
        this.txEmail = txEmail;
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
        result = prime * result + ((txAppProviderId == null) ? 0 : txAppProviderId.hashCode());
        result = prime * result + ((txEmail == null) ? 0 : txEmail.hashCode());
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
        DbeAggregatorAppProviderId other = (DbeAggregatorAppProviderId) obj;
        if (txAppProviderId == null) {
            if (other.txAppProviderId != null) {
                return false;
            }
        } else if (!txAppProviderId.equals(other.txAppProviderId)) {
            return false;
        }
        if (txEmail == null) {
            if (other.txEmail != null) {
                return false;
            }
        } else if (!txEmail.equals(other.txEmail)) {
            return false;
        }
        return true;
    }

}