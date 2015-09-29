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

package es.upm.fiware.rss.model;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The persistent class for the BM_COUNTRY database table.
 * 
 */
@Entity
@Table(name = "bm_country")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class BmCountry implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "NU_COUNTRY_ID")
    private long nuCountryId;

    @Column(name = "TX_ISO3166_CODE")
    private String txIso3166Code;

    @Column(name = "TX_ITU_T212_CODE")
    private String txItuT212Code;

    // uni-directional many-to-one association to BmLanguage
    @ManyToOne
    @JoinColumn(name = "NU_LANGUAGE_ID")
    private BmLanguage bmLanguage;

    // uni-directional many-to-one association to BmCurrency
    @ManyToOne
    @JoinColumn(name = "NU_CURRENCY_ID")
    private BmCurrency bmCurrency;

    /**
     * Constructor.
     */
    public BmCountry() {
    }

    public long getNuCountryId() {
        return this.nuCountryId;
    }

    public void setNuCountryId(long nuCountryId) {
        this.nuCountryId = nuCountryId;
    }

    public String getTxIso3166Code() {
        return this.txIso3166Code;
    }

    public void setTxIso3166Code(String txIso3166Code) {
        this.txIso3166Code = txIso3166Code;
    }

    public String getTxItuT212Code() {
        return this.txItuT212Code;
    }

    public void setTxItuT212Code(String txItuT212Code) {
        this.txItuT212Code = txItuT212Code;
    }

    public BmLanguage getBmLanguage() {
        return this.bmLanguage;
    }

    public void setBmLanguage(BmLanguage bmLanguage) {
        this.bmLanguage = bmLanguage;
    }

    public BmCurrency getBmCurrency() {
        return this.bmCurrency;
    }

    public void setBmCurrency(BmCurrency bmCurrency) {
        this.bmCurrency = bmCurrency;
    }

}