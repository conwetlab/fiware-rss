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

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The persistent class for the BM_LANGUAGE database table.
 * 
 */
@Entity
@Table(name = "bm_language")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class BmLanguage implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "NU_LANGUAGE_ID")
    private long nuLanguageId;

    @Column(name = "TX_ISO639_1_CODE")
    private String txIso6391Code;

    @Column(name = "TX_ISO639_CODE")
    private String txIso6391Code2Char;

    @Column(name = "TX_NAME")
    private String txName;

    /**
     * Constructor.
     */
    public BmLanguage() {
    }

    public long getNuLanguageId() {
        return this.nuLanguageId;
    }

    public void setNuLanguageId(long nuLanguageId) {
        this.nuLanguageId = nuLanguageId;
    }

    public String getTxIso6391Code() {
        return this.txIso6391Code;
    }

    public void setTxIso6391Code(String txIso6391Code) {
        this.txIso6391Code = txIso6391Code;
    }

    /**
     * @return the txIso6391Code2Char
     */
    public final String getTxIso6391Code2Char() {
        return txIso6391Code2Char;
    }

    /**
     * @param txIso6391Code2Char
     *            the txIso6391Code2Char to set
     */
    public final void setTxIso6391Code2Char(String txIso6391Code2Char) {
        this.txIso6391Code2Char = txIso6391Code2Char;
    }

    public String getTxName() {
        return this.txName;
    }

    public void setTxName(String txName) {
        this.txName = txName;
    }

}