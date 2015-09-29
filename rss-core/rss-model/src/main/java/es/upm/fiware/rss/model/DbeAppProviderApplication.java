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

/*
 * DbeAppProviderApplication.java
 * 
 * 2013 ®, Telefónica I+D, all rights reserved
 */
package es.upm.fiware.rss.model;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 
 * 
 */
@Entity
@Table(name = "dbe_appprovider_application")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class DbeAppProviderApplication implements java.io.Serializable {

    /**
     * Default serial version UID
     */
    private static final long serialVersionUID = 1L;
    private String txAppProviderId;
    private String txApplicationId;

    /**
     * 
     */
    public DbeAppProviderApplication() {
    }

    /**
     * @param txAppProviderId
     * @param txApplicationId
     */
    public DbeAppProviderApplication(String txAppProviderId, String txApplicationId) {
        this.txAppProviderId = txAppProviderId;
        this.txApplicationId = txApplicationId;
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
    @Column(name = "TX_APPPROVIDER_ID", length = 50)
    public String getTxAppProviderId() {
        return txAppProviderId;
    }

    /**
     * @param txApplicationId
     *            the txApplicationId to set
     */
    public void setTxApplicationId(String txApplicationId) {
        this.txApplicationId = txApplicationId;
    }

    /**
     * @return the txApplicationId
     */
    @Id
    @Column(name = "TX_APPLICATION_ID", length = 50)
    public String getTxApplicationId() {
        return txApplicationId;
    }

}
