/**
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

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;

@Embeddable
public class SetRevenueShareConfId implements Serializable {
    private static final long serialVersionUID = 1L;
    // Provider that owns the RS Model
    // Application provider which is the owner of the revenue sharing model
    private DbeAppProvider modelOwner;
    private String productClass;

    public SetRevenueShareConfId() {
    }

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = DbeAppProvider.class)
    @JoinColumns({
            @JoinColumn(name = "MODEL_OWNER_PROVIDER", referencedColumnName = "TX_APPPROVIDER_ID"),
            @JoinColumn(name = "AGGREGATOR_ID", referencedColumnName = "TX_AGGREGATOR_ID")})
    public DbeAppProvider getModelOwner() {
        return modelOwner;
    }

    public void setModelOwner(DbeAppProvider modelOwner) {
        this.modelOwner = modelOwner;
    }

    /**
     * @return the productClass
     */
    @Column(name = "TX_PRODUCT_CLASS", nullable = true, length = 40)
    public String getProductClass() {
        return productClass;
    }

    /**
     * @param productClass
     *            the productClass to set
     */
    public void setProductClass(String productClass) {
        this.productClass = productClass;
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
        result = prime * result + ((productClass == null) ? 0 : productClass.hashCode());
        result = prime * result + 
                ((modelOwner.getId().getTxAppProviderId() == null) ? 0 : modelOwner.getId().getTxAppProviderId().hashCode());
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
        SetRevenueShareConfId other = (SetRevenueShareConfId) obj;
        if (productClass == null) {
            if (other.productClass != null) {
                return false;
            }
        } else if (!productClass.equals(other.productClass)) {
            return false;
        }
        if (modelOwner.getId().getTxAppProviderId() == null) {
            if (other.getModelOwner().getId().getTxAppProviderId() != null) {
                return false;
            }
        } else if (!modelOwner.getId().getTxAppProviderId().
                equals(other.getModelOwner().getId().getTxAppProviderId())) {
            return false;
        }
        return true;
    }

}
