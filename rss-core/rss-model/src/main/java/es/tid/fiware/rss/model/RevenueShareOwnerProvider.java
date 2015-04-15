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

package es.tid.fiware.rss.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author fdelavega
 */
@Embeddable
public class RevenueShareOwnerProvider implements Serializable{

    // Application provider which is the owner of the revenue sharing model
    private DbeAppProvider modelOwner;
    // Value applied for the owner in the RS models
    private BigDecimal ownerValue;

    public RevenueShareOwnerProvider() {
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MODEL_OWNER_PROVIDER", nullable = false, insertable = false, updatable = false)
    public DbeAppProvider getModelOwner() {
        return modelOwner;
    }

    public void setModelOwner(DbeAppProvider modelOwner) {
        this.modelOwner = modelOwner;
    }

    @Column(name = "OWNER_VALUE", nullable = false, precision = 5, scale = 0)
    public BigDecimal getOwnerValue() {
        return this.ownerValue;
    }

    public void setOwnerValue(BigDecimal ownerValue) {
        this.ownerValue = ownerValue;
    }

}
