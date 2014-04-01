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

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * DbeAppProvider.
 * 
 */
@Entity
@Table(name = "dbe_aggregator_appprovider")
public class DbeAggregatorAppProvider implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private DbeAggregatorAppProviderId id;
    private DbeAggregator dbeAggregator;
    private DbeAppProvider dbeAppProvider;

    /**
     * 
     */
    public DbeAggregatorAppProvider() {
    }

    /**
     * @return the id
     */
    @EmbeddedId
    @AttributeOverrides({
        @AttributeOverride(name = "tx_email", column = @Column(name = "TX_EMAIL", nullable = false, length = 50)),
        @AttributeOverride(name = "tx_appprovider", column = @Column(name = "TX_APPPROVIDER", nullable = false,
            length = 256))
    })
    public DbeAggregatorAppProviderId getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(DbeAggregatorAppProviderId id) {
        this.id = id;
    }

    /**
     * @return the dbeAggredator
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TX_EMAIL", nullable = false, insertable = false, updatable = false)
    public DbeAggregator getDbeAggregator() {
        return dbeAggregator;
    }

    /**
     * @param dbeAggredator
     *            the dbeAggredator to set
     */
    public void setDbeAggregator(DbeAggregator dbeAggregator) {
        this.dbeAggregator = dbeAggregator;
    }

    /**
     * @return the dbeAppProvider
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TX_APPPROVIDER_ID", nullable = false, insertable = false, updatable = false)
    public DbeAppProvider getDbeAppProvider() {
        return dbeAppProvider;
    }

    /**
     * @param dbeAppProvider
     *            the dbeAppProvider to set
     */
    public void setDbeAppProvider(DbeAppProvider dbeAppProvider) {
        this.dbeAppProvider = dbeAppProvider;
    }

}
