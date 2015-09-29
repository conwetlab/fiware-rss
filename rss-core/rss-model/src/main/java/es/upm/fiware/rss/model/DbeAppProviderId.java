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
import javax.persistence.ManyToOne;

/**
 *
 * @author fdelavega
 */
@Embeddable
public class DbeAppProviderId implements Serializable {

    private String txAppProviderId;
    private DbeAggregator aggregator;

    public DbeAppProviderId() {
    }


    /**
     * @return the txAppProviderId
     */
    @Column(name = "TX_APPPROVIDER_ID", length = 100)
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TX_AGGREGATOR_ID")
    public DbeAggregator getAggregator() {
        return aggregator;
    }

    public void setAggregator(DbeAggregator aggregator) {
        this.aggregator = aggregator;
    }
}
