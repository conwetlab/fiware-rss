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
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * DbeAppProvider.
 * 
 */
@Entity
@Table(name = "dbe_aggregator")
public class DbeAggregator implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private String txName;
    private String txEmail;

    /**
     * 
     */
    public DbeAggregator() {
    }

    /**
     * @param txEmail
     * @param txName
     */
    public DbeAggregator(String txName, String txEmail) {
        this.txName = txName;
        this.txEmail = txEmail;
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

    /**
     * @return the txEmail
     */
    @Id
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

}
