/**
 * Revenue Settlement and Sharing System GE
 * Copyright (C) 2011-2014, Javier Lucio - lucio@tid.es
 * Telefonica Investigacion y Desarrollo, S.A.
 *
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

/*
 * DbeAppProvider.java
 * 
 * 2013 ®, Telefónica I+D, all rights reserved
 */
package es.tid.fiware.rss.model;

import java.util.Set;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * DbeAppProvider.
 * 
 */
@Entity
@Table(name = "dbe_appprovider")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class DbeAppProvider implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private String txAppProviderId;
    private String txName;
    private Set<ModelProvider> models;

    /**
     * 
     */
    public DbeAppProvider() {
    }

    /**
     * @param txAppProviderId
     * @param txName
     * @param models
     */
    public DbeAppProvider(String txAppProviderId, String txName, 
            Set<ModelProvider> models) {
        this.txAppProviderId = txAppProviderId;
        this.txName = txName;
        this.models = models;
    }

    /**
     * @param txAppProviderId
     *            the txAppProviderId to set
     */
    public void setTxAppProviderId(String txAppProviderId) {
        this.txAppProviderId = txAppProviderId;
    }

    /**
     * @return the txAppProviderId
     */
    @Id
    @Column(name = "TX_APPPROVIDER_ID", length = 50)
    public String getTxAppProviderId() {
        return txAppProviderId;
    }

    /**
     * @param txName
     *            the txName to set
     */
    public void setTxName(String txName) {
        this.txName = txName;
    }

    /**
     * @return the txName
     */
    @Column(name = "TX_NAME", length = 256)
    public String getTxName() {
        return txName;
    }

    @OneToMany(fetch = FetchType.EAGER, targetEntity = ModelProvider.class)
    public Set<ModelProvider> getModels() {
        return models;
    }

    public void setModels(Set<ModelProvider> models) {
        this.models = models;
    }
}
