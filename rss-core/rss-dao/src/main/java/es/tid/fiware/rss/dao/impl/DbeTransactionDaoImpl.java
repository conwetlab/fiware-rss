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

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
@Transactional
public class DbeTransactionDaoImpl extends GenericDaoImpl<DbeTransaction, String> implements DbeTransactionDao {

    /**
     * MINIMUN_SIZE_TRANSACTION_ID establish the minimun size for the transaction_id. it is necessary to be longer than
     * we user to extracts id for the partition
     */
    private static final int MINIMUN_SIZE_TRANSACTION_ID = 2;
    /**
     * Variable to print the trace.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DbeTransactionDaoImpl.class);

    /**
     * 
     * 
     * @see es.tid.fiware.rss.dao.impl.GenericDaoImpl#getDomainClass()
     */
    @Override
    protected Class<DbeTransaction> getDomainClass() {
        return DbeTransaction.class;
    }

    /**
     * create.
     */
    @Override
    public void create(DbeTransaction object) {
        DbeTransactionDaoImpl.LOGGER.debug("Entering create...");
        String transactionId;
        if (object != null && object.getTxTransactionId() != null) {
            transactionId = object.getTxTransactionId();
            DbeTransactionDaoImpl.LOGGER.debug("TransactionId comes in the request:" + transactionId);
            if (transactionId.length() < DbeTransactionDaoImpl.MINIMUN_SIZE_TRANSACTION_ID) {
                DbeTransactionDaoImpl.LOGGER
                    .error("TransactionId size is lower than 2 char and it is not possible to obtain partition");
                DbeTransactionDaoImpl.LOGGER.debug("NEW TransactionId obteined for this request:" + transactionId);
                transactionId = java.util.UUID.randomUUID().toString();

            }
        } else {

            transactionId = java.util.UUID.randomUUID().toString();
            DbeTransactionDaoImpl.LOGGER.debug("TransactionId obteined for this request:" + transactionId);

        }

        int sizeTransactionId = transactionId.length();
        object.setTxTransactionId(transactionId);
        String partition = transactionId.substring(sizeTransactionId - 2, sizeTransactionId);
        object.setTxPartition(partition);
        if (object.getTxReferenceCode() == null) {
            object.setTxReferenceCode(object.getTxTransactionId());
        }

        DbeTransactionDaoImpl.LOGGER.debug("Partition:" + object.getTxPartition());
        DbeTransactionDaoImpl.LOGGER.debug("Rest of params of the object");
        DbeTransactionDaoImpl.LOGGER.debug("end user id:" + object.getTxEndUserId());
        DbeTransactionDaoImpl.LOGGER.debug("service:" + object.getBmService().getNuServiceId());
        DbeTransactionDaoImpl.LOGGER.debug("ts-request:" + object.getTsRequest().toString());
        DbeTransactionDaoImpl.LOGGER.debug("tcTransactionType:" + object.getTcTransactionType());
        DbeTransactionDaoImpl.LOGGER.debug("tcTransactionStatus:" + object.getTcTransactionStatus());
        DbeTransactionDaoImpl.LOGGER.debug("txReferenceCode:" + object.getTxReferenceCode());
        DbeTransactionDaoImpl.LOGGER.debug("ftRequestAmount:" + object.getFtRequestAmount());
        DbeTransactionDaoImpl.LOGGER.debug("txRequestDesc:" + object.getTxRequestAmountDesc());

        super.create(object);

    }

    /**
     * createOrUpdate.
     */
    @Override
    public void createOrUpdate(DbeTransaction object) {
        DbeTransactionDaoImpl.LOGGER.debug("Entering createOrUpdate...");
        String transactionId;
        if (object != null && object.getTxTransactionId() != null) {
            transactionId = object.getTxTransactionId();
            DbeTransactionDaoImpl.LOGGER.debug("TransactionId comes in the request:" + transactionId);
            if (transactionId.length() < DbeTransactionDaoImpl.MINIMUN_SIZE_TRANSACTION_ID) {
                DbeTransactionDaoImpl.LOGGER
                    .error("TransactionId size is lower than 2 char and it is not possible to obtain partition");
                DbeTransactionDaoImpl.LOGGER.debug("NEW TransactionId obteined for this request:" + transactionId);
                transactionId = java.util.UUID.randomUUID().toString();

            }
        } else {

            transactionId = java.util.UUID.randomUUID().toString();
            DbeTransactionDaoImpl.LOGGER.debug("TransactionId obteined for this request:" + transactionId);

        }

        int sizeTransactionId = transactionId.length();
        object.setTxTransactionId(transactionId);
        String partition = transactionId.substring(sizeTransactionId - 2, sizeTransactionId);
        object.setTxPartition(partition);
        if (object.getTxReferenceCode() == null) {
            object.setTxReferenceCode(object.getTxTransactionId());
        }

        DbeTransactionDaoImpl.LOGGER.debug("Partition:" + object.getTxPartition());
        DbeTransactionDaoImpl.LOGGER.debug("Rest of params of the object");
        DbeTransactionDaoImpl.LOGGER.debug("end user id:" + object.getTxEndUserId());
        DbeTransactionDaoImpl.LOGGER.debug("service:" + object.getBmService().getNuServiceId());
        DbeTransactionDaoImpl.LOGGER.debug("ts-request:" + object.getTsRequest().toString());
        DbeTransactionDaoImpl.LOGGER.debug("tcTransactionType:" + object.getTcTransactionType());
        DbeTransactionDaoImpl.LOGGER.debug("tcTransactionStatus:" + object.getTcTransactionStatus());
        DbeTransactionDaoImpl.LOGGER.debug("txReferenceCode:" + object.getTxReferenceCode());
        DbeTransactionDaoImpl.LOGGER.debug("ftRequestAmount:" + object.getFtRequestAmount());
        DbeTransactionDaoImpl.LOGGER.debug("txRequestDesc:" + object.getTxRequestAmountDesc());
        super.createOrUpdate(object);
    }

    @Override
    public DbeTransaction getTransactionByTxIdWithoutLazy(String txid) {
        String hql = "from DbeTransaction as trans left join trans.bmObMop.bmObCountry left join trans.bmProduct "
            + "where trans.txTransactionId=:transaction";
        DbeTransaction dbetrBD = null;
        Object[] obj = null;
        try {
            obj = (Object[]) this.getSession().createQuery(hql).setParameter("transaction", txid).list().get(0);
            dbetrBD = (DbeTransaction) obj[0];
        } catch (Exception e) {
            DbeTransactionDaoImpl.LOGGER.error("Error db", e);
            return null;
        }
        return dbetrBD;
    }

    /*
     * (non-Javadoc)
     * 
     * @see es.tid.rss.dbe.dao.DbeTransactionDao#getTransactionByTxPbCorrelationId(java.lang.String)
     */
    @Override
    public List<DbeTransaction> getTransactionByTxPbCorrelationId(String pbCorrelationId) {
        String hql = "from DbeTransaction as trans where trans.txPbCorrelationId=:correlation";
        List<DbeTransaction> resultList = null;
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
    public DbeTransaction getTransactionByRfCdeSvc(final long nuServiceId, final String txReferenceCode,
        final String applicationId) {
        DbeTransactionDaoImpl.LOGGER.debug("Entering getTransactionByRfCdeSvc...");
        // RVD SQLi
        String hql = " from DbeTransaction l where l.bmService.nuServiceId = " + nuServiceId
            + " and l.txReferenceCode = '" + txReferenceCode + "'" + " and l.txApplicationId = '" + applicationId + "'";
        List<DbeTransaction> lpp = this.listDbeTransactionQuery(hql);
        if (lpp.size() > 0) {
            // must be only one
            return lpp.get(0);

        } else { // <=0
            return null;
        }
    }

    @Override
    public List<DbeTransaction> getTransactionByOrgSvrRfCde(final String orgSrvrReferenceCode) {
        DbeTransactionDaoImpl.LOGGER.debug("Entering getTransactionByOrgSvrRfCde...");
        // RVD SQLi
        String hql = " from DbeTransaction l where l.txOrgTransactionId  = '" + orgSrvrReferenceCode + "'";
        List<DbeTransaction> lpp = this.listDbeTransactionQuery(hql);
        return lpp;

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

            if (txs.size() == 0) {
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
    public List<DbeTransaction> getTransactionBySvcPrdtUserDate(final Long nuServiceId, final Long nuProductId,
        final String enduserid, final Date rqDate) {
        DbeTransactionDaoImpl.LOGGER.debug("Entering getTransactionBySvcPrdtUserDate...");

        Criteria criteria = this.getSession().createCriteria(DbeTransaction.class);
        criteria.add(Restrictions.eq("bmService.nuServiceId", nuServiceId));
        if (nuProductId != null) {
            DbeTransactionDaoImpl.LOGGER.debug("product is NOT NULL");
            criteria.add(Restrictions.eq("bmProduct.nuProductId", nuProductId));

        }
        criteria.add(Restrictions.eq("txEndUserId", enduserid));

        DbeTransactionDaoImpl.LOGGER.debug("date of request:" + rqDate.toString());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(rqDate);
        int monthreq = calendar.get(Calendar.MONTH);
        DbeTransactionDaoImpl.LOGGER.debug("MONTH ini:" + monthreq);
        int yearreq = calendar.get(Calendar.YEAR);
        DbeTransactionDaoImpl.LOGGER.debug("YEAR ini:" + yearreq);
        int dayreq = calendar.get(Calendar.DATE);
        DbeTransactionDaoImpl.LOGGER.debug("DAY ini:" + dayreq);

        Calendar calini = Calendar.getInstance();
        calini.set(Calendar.YEAR, yearreq);
        calini.set(Calendar.MONTH, monthreq);
        calini.set(Calendar.DAY_OF_MONTH, dayreq);
        calini.set(Calendar.HOUR_OF_DAY, 0);
        calini.set(Calendar.MINUTE, 0);
        calini.set(Calendar.SECOND, 0);
        calini.set(Calendar.MILLISECOND, 0);
        Date dateini = calini.getTime();
        DbeTransactionDaoImpl.LOGGER.debug("string date ini:" + dateini.toString());
        DbeTransactionDaoImpl.LOGGER.debug("date ini:" + calini.get(Calendar.YEAR) + "-" + calini.get(Calendar.MONTH)
            + "-" + calini.get(Calendar.DATE) + "-" + calini.get(Calendar.HOUR) + "-" + calini.get(Calendar.MINUTE));

        Calendar calfin = Calendar.getInstance();
        calfin.set(Calendar.YEAR, yearreq);
        calfin.set(Calendar.MONTH, monthreq);
        calfin.set(Calendar.DAY_OF_MONTH, dayreq);
        calfin.set(Calendar.HOUR_OF_DAY, 23);
        calfin.set(Calendar.MINUTE, 59);
        calfin.set(Calendar.SECOND, 59);
        calfin.set(Calendar.MILLISECOND, 999);
        Date datefin = calfin.getTime();
        DbeTransactionDaoImpl.LOGGER.debug("string date fin:" + datefin.toString());
        DbeTransactionDaoImpl.LOGGER.debug("date fin:" + calfin.get(Calendar.YEAR) + "-" + calfin.get(Calendar.MONTH)
            + "-" + calfin.get(Calendar.DATE) + "-" + calfin.get(Calendar.HOUR) + "-" + calfin.get(Calendar.MINUTE));

        criteria.add(Restrictions.between("tsRequest", dateini, datefin));
        List<DbeTransaction> result = criteria.list();
        return result;

    }

    /*
     * (non-Javadoc) Obtains the list of transactions with a maximum limit and ordered by date.
     */
    @Override
    public List<DbeTransaction> getLimitedTxBySvcRefcPrdtUserDateOrderByDate(final String transactionId,
        final Long nuServiceId, final String refCode, final Long nuProductId, final Long nuMopId,
        final String endUserId, final Date fromDate, final Date untilDate, final Integer offset, final Integer limit,
        final String applicationId, final String[] txTypes, final String operationNature, final String originalTxId,
        final String gUserId, final Long paymentMethodType) {
        DbeTransactionDaoImpl.LOGGER.debug("Entering getLimitedTxBySvcPrdtUserDateOrderByDate...");

        Criteria criteria = getCriteriaTxBySvcRefcPrdtUserDate(transactionId, nuServiceId, refCode, nuProductId,
            nuMopId, endUserId, fromDate, untilDate, applicationId, txTypes, operationNature, originalTxId, gUserId,
            paymentMethodType);

        criteria.addOrder(Order.desc("tsClientDate"));
        criteria.setFirstResult(offset);
        criteria.setMaxResults(limit); // and rownum <=limit

        List<DbeTransaction> result = criteria.list();

        return result;
    }

    private Criteria getCriteriaTxBySvcRefcPrdtUserDate(String transactionId, Long nuServiceId, String refCode,
        Long nuProductId, Long nuMopId, String enduserid, Date fromDate, Date untilDate, String applicationId,
        String[] txTypes, String operationNature, String originalTxId, String gUserId, Long paymentMethodType) {
        Criteria criteria = this.getSession().createCriteria(DbeTransaction.class);

        if (null != transactionId) {
            criteria.add(Restrictions.eq("txTransactionId", transactionId));
        }
        if (null != nuServiceId) {
            criteria.add(Restrictions.eq("bmService.nuServiceId", nuServiceId));
        }
        if (null != refCode) {
            criteria.add(Restrictions.eq("txReferenceCode", refCode));
        }
        if (null != nuProductId) {
            criteria.add(Restrictions.eq("bmProduct.nuProductId", nuProductId));
        }
        if (null != nuMopId) {
            criteria.add(Restrictions.eq("bmObMop.id.nuMopId", nuMopId));
        }
        if (null != enduserid) {
            criteria.add(Restrictions.eq("txEndUserId", enduserid));
        }
        if (null != applicationId) {
            criteria.add(Restrictions.eq("txApplicationId", applicationId));
        }
        if (null != fromDate) {
            criteria.add(Restrictions.ge("tsClientDate", fromDate));
        }
        if (null != untilDate) {
            criteria.add(Restrictions.le("tsClientDate", untilDate));
        }
        if ((null != txTypes) && (txTypes.length > 0)) {
            Criterion crTxTypes = Restrictions.eq("tcTransactionType", txTypes[0]);
            for (int i = 1; i < txTypes.length; i++) {
                crTxTypes = Restrictions.or(crTxTypes, Restrictions.eq("tcTransactionType", txTypes[i]));
            }
            criteria.add(crTxTypes);
        }
        if (null != operationNature) {
            criteria.add(Restrictions.eq("txOperationNature", operationNature));
        }
        if (null != originalTxId) {
            criteria.add(Restrictions.eq("txOrgTransactionId", originalTxId));
        }
        if (null != gUserId) {
            criteria.add(Restrictions.eq("txGlobalUserId", gUserId));
        }
        if (null != paymentMethodType) {
            criteria.add(Restrictions.eq("bmObMop.id.nuMopId", paymentMethodType));
        }

        return criteria;
    }

    /**
     * getLimitedTxBySvcPrdtUserDateOrderByDate obtains number of transactions without a maximum limit.
     * 
     */
    @Override
    public Long getNumTxBySvcRefcPrdtUserDate(final String transactionId, final Long nuServiceId, final String refCode,
        final Long nuProductId, final Long nuMopId, final String enduserid, final Date fromDate, final Date untilDate,
        final String applicationId, final String[] txTypes, final String operationNature, final String originalTxId,
        final String gUserId, final Long paymentMethodType) {
        DbeTransactionDaoImpl.LOGGER.debug("Entering getNumTxBySvcRefcPrdtUserDate...");

        Criteria criteria = getCriteriaTxBySvcRefcPrdtUserDate(transactionId, nuServiceId, refCode, nuProductId,
            nuMopId, enduserid, fromDate, untilDate, applicationId, txTypes, operationNature, originalTxId, gUserId,
            paymentMethodType);

        criteria.setProjection(Projections.rowCount());

        @SuppressWarnings("unchecked")
        List<Long> result = criteria.list();

        return result.get(0);

    }

    @Override
    public void deleteTransactionsByProviderId(String providerId) {
        StringBuilder sbSql = new StringBuilder("delete from dbe_transaction where ");
        sbSql.append("tx_app_provider = ?");
        Query query = super.getSession().createSQLQuery(sbSql.toString()).addEntity(DbeTransaction.class);
        query.setString(0, providerId);
        query.executeUpdate();
    }

    @Override
    public List<DbeTransaction> getTransactionsByProviderId(String providerId) {
        DbeTransactionDaoImpl.LOGGER.debug("getTransactionsByProviderId..");
        String hql = "from DbeTransaction l where l.txAppProvider='" + providerId + "'";
        try {
            List<DbeTransaction> txs = listDbeTransactionQuery(hql);
            if (txs.size() == 0) {
                DbeTransactionDaoImpl.LOGGER.debug("There is no data");
                return null;
            } else {
                return txs;
            }
        } catch (Exception e) {
            DbeTransactionDaoImpl.LOGGER.error("Error db", e);
            return null;
        }
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
        List list = this.getSession().createQuery(hql).list();
        List<DbeTransaction> resultList = Collections.checkedList(list, DbeTransaction.class);
        if (resultList != null) {
            DbeTransactionDaoImpl.LOGGER.debug("there is something to return");
        }
        return resultList;

    }

}
