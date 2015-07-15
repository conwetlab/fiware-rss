/**
 * Revenue Settlement and Sharing System GE
 * Copyright (C) 2011-2014, Javier Lucio - lucio@tid.es
 * Telefonica Investigacion y Desarrollo, S.A.
 *
 * Copyright (C) 2015, CoNWeT Lab., Universidad Politécnica de Madrid
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

package es.tid.fiware.rss.dao.impl;

import java.util.Collections;
import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import es.tid.fiware.rss.dao.DbeTransactionDao;
import es.tid.fiware.rss.exception.RSSException;
import es.tid.fiware.rss.model.DbeTransaction;

/**
 * 
 * Class that extends GenericDaoImpl and implements DbeTransactionDao.
 * 
 */
@Repository
public class DbeTransactionDaoImpl extends GenericDaoImpl<DbeTransaction, String> implements DbeTransactionDao {

    /**
     * MINIMUN_SIZE_TRANSACTION_ID establish the minimun size for the transaction_id. it is necessary to be longer than
     * we user to extracts id for the partition
     */

    /**
     * Variable to print the trace.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DbeTransactionDaoImpl.class);

    @Override
    protected Class<DbeTransaction> getDomainClass() {
        return DbeTransaction.class;
    }


    @Override
    public List<DbeTransaction> getTransactionByTxPbCorrelationId(Integer pbCorrelationId) {
        String hql = "from DbeTransaction as trans where trans.txPbCorrelationId=:correlation";
        List<DbeTransaction> resultList;
        try {
            List list = this.getSession().createQuery(hql).setParameter("correlation", pbCorrelationId).list();
            resultList = Collections.checkedList(list, DbeTransaction.class);
        } catch (Exception e) {
            DbeTransactionDaoImpl.LOGGER.error("Error db", e);
            return null;
        }
        return resultList;
    }

    @Override
    public DbeTransaction getTransactionByRfCdeSvc(final String txReferenceCode,
        final String applicationId) {
        DbeTransactionDaoImpl.LOGGER.debug("Entering getTransactionByRfCdeSvc...");
        // RVD SQLi
        String hql = " from DbeTransaction l where l.txReferenceCode = '" 
                + txReferenceCode + "'" + " and l.txApplicationId = '" + applicationId + "'";

        List<DbeTransaction> lpp = this.listDbeTransactionQuery(hql);

        if (!lpp.isEmpty()) {
            // must be only one
            return lpp.get(0);
        } else { // <=0
            return null;
        }
    }

    @Override
    public DbeTransaction getTransactionByTxId(final String transactionId) throws RSSException {

        DbeTransactionDaoImpl.LOGGER.debug("Entering getTransactionByTxId...");
        String hql = "from DbeTransaction l where l.txTransactionId=:txID";

        try {
            List<Object> txs = (List<Object>) this.getSession().
            		createQuery(hql).
            		setParameter("txID", transactionId).
            		list();

            if (txs.isEmpty()) {
                DbeTransactionDaoImpl.LOGGER.debug("There is no data");
                return null;
            } else {
                return (DbeTransaction) txs.get(0);
            }
        } catch (Exception e) {
            DbeTransactionDaoImpl.LOGGER.error("Error db", e);
            return null;
        }
    }

    @Override
    public void deleteTransactionsByProviderId(String providerId) {
        StringBuilder sbSql = new StringBuilder("delete from dbe_transaction where ");
        sbSql.append("app_provider.txAppProviderId = ?");
        Query query = super.getSession().createSQLQuery(sbSql.toString()).addEntity(DbeTransaction.class);
        query.setString(0, providerId);
        query.executeUpdate();
    }

    @Override
    public List<DbeTransaction> getTransactionsByProviderId(String providerId) {
        DbeTransactionDaoImpl.LOGGER.debug("getTransactionsByProviderId..");
        String hql = "from DbeTransaction l where l.appProvider.txAppProviderId='" + providerId + "'"
                + " and l.state='pending' order by l.txPbCorrelationId";

        return listDbeTransactionQuery(hql);
    }

    @Override
    public List<DbeTransaction> getTransactionByAggregatorId(String aggregatorId) {
        DbeTransactionDaoImpl.LOGGER.debug("getTransactionsByAggregatorId..");
        String hql = "from DbeTransaction l where l.cdrSource.txEmail='" + aggregatorId + "'"
                + " and l.state='pending' order by l.txPbCorrelationId";

        return listDbeTransactionQuery(hql);
    }

    @Override
    public List<DbeTransaction> getTransactions() {
        DbeTransactionDaoImpl.LOGGER.debug("getTransactionsByAggregatorId..");
        String hql = "from DbeTransaction l where l.state='pending'";
        return listDbeTransactionQuery(hql);
    }

    /* Private Methods */
    /**
     * Method executes HQL query.
     * 
     * @param hql
     *            String with HQL query
     * @return resultList
     */
    private List<DbeTransaction> listDbeTransactionQuery(final String hql) {
        DbeTransactionDaoImpl.LOGGER.debug("listDbeTransactionQuery hql-->" + hql);
        List<DbeTransaction> resultList;
        try {
            List list = this.getSession().createQuery(hql).list();
            resultList = Collections.checkedList(list, DbeTransaction.class);
        } catch (Exception e) {
            DbeTransactionDaoImpl.LOGGER.error("Error db", e);
            return null;
        }
        return resultList;
    }

}
