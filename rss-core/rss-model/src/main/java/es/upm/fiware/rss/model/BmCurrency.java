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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The persistent class for the BM_CURRENCY database table.
 * 
 */
@Entity
@Table(name = "bm_currency")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class BmCurrency implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "NU_CURRENCY_ID")
    private long nuCurrencyId;

    @Column(name = "TC_SYMBOL")
    private String tcSymbol;

    @Column(name = "TX_DESCRIPTION")
    private String txDescription;

    @Column(name = "TX_ISO4217_CODE")
    private String txIso4217Code;

    @Column(name = "TX_ISO4217_CODE_NUM")
    private String txIso4217CodeNum;

    @Column(name = "NU_ISO4217_DECIMALS")
    private long nuIso4217Decimals;

    /**
     * Constructor.
     */
    public BmCurrency() {
    }

    public long getNuCurrencyId() {
        return this.nuCurrencyId;
    }

    public void setNuCurrencyId(long nuCurrencyId) {
        this.nuCurrencyId = nuCurrencyId;
    }

    public String getTcSymbol() {
        return tcSymbol;
    }

    public void setTcSymbol(String tcSymbol) {
        this.tcSymbol = tcSymbol;
    }

    public String getTxDescription() {
        return this.txDescription;
    }

    public void setTxDescription(String txDescription) {
        this.txDescription = txDescription;
    }

    public String getTxIso4217Code() {
        return this.txIso4217Code;
    }

    public void setTxIso4217Code(String txIso4217Code) {
        this.txIso4217Code = txIso4217Code;
    }

    /**
     * @return the txIso4217CodeNum
     */
    public final String getTxIso4217CodeNum() {
        return txIso4217CodeNum;
    }

    /**
     * @param txIso4217CodeNum
     *            the txIso4217CodeNum to set
     */
    public final void setTxIso4217CodeNum(String txIso4217CodeNum) {
        this.txIso4217CodeNum = txIso4217CodeNum;
    }

    /**
     * @return the txIso4217Decimals
     */
    public final long getNuIso4217Decimals() {
        return nuIso4217Decimals;
    }

    /**
     * 
     * 
     * @param nuIso4217Decimals
     */
    public final void setNuIso4217Decimals(long nuIso4217Decimals) {
        this.nuIso4217Decimals = nuIso4217Decimals;
    }

}