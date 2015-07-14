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

package es.tid.fiware.rss.dao;

import java.util.List;

import es.tid.fiware.rss.exception.RSSException;
import es.tid.fiware.rss.model.DbeTransaction;

/**
 * 
 * 
 */
public interface DbeTransactionDao extends GenericDao<DbeTransaction, String> {

    /**
     * List of the transactions associated to a PBCorrelationID.
     * 
     * @param txid
     * @return
     */
    List<DbeTransaction> getTransactionByTxPbCorrelationId(String pbCorrelationId);

    /**
     * Described by the name.
     */
    DbeTransaction getTransactionByRfCdeSvc(final String txReferenceCode,
        final String applicationId);

    /**
     * Described by the name.
     */
    DbeTransaction getTransactionByTxId(String transactionId) throws RSSException;


    /**
     * Described by the name.
     */
    @Override
    void create(DbeTransaction object);

    /**
     * Described by the name.
     */
    @Override
    void createOrUpdate(DbeTransaction object);

    /**
     * Delete transactionByProviderId.
     * 
     * @param providerId
     */
    void deleteTransactionsByProviderId(String providerId);

    /**
     * Delete transactionByProviderId.
     * 
     * @param providerId
     */
    List<DbeTransaction> getTransactionsByProviderId(String providerId);

}
