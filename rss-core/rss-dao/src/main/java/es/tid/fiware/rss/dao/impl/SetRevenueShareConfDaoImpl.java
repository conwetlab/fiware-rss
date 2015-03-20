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
package es.tid.fiware.rss.dao.impl;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import es.tid.fiware.rss.dao.SetRevenueShareConfDao;
import es.tid.fiware.rss.model.SetRevenueShareConf;
import es.tid.fiware.rss.model.SetRevenueShareConfId;


@Repository
public class SetRevenueShareConfDaoImpl extends GenericDaoImpl<SetRevenueShareConf, SetRevenueShareConfId> implements
    SetRevenueShareConfDao {
    /**
     * Variable to print the trace.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SetRevenueShareConfDaoImpl.class);


    /*
     * (non-Javadoc)
     * 
     * @see es.tid.fiware.rss.dao.impl.GenericDaoImpl#getDomainClass()
     */
    @Override
    protected Class<SetRevenueShareConf> getDomainClass() {
        return SetRevenueShareConf.class;
    }

    /**
     * Returns a list of revenue sharing models based on the id of the owner 
     * provider
     *
     * @param providerId, ID of the owner provider
     * @return A list of SetRevenueShareConf objects containing the revenue 
     * sharing models of the given provider
     */
    @Override
    public List<SetRevenueShareConf> getRevenueModelsByProviderId(String providerId) {
        SetRevenueShareConfDaoImpl.LOGGER.debug("getRevenueModelsByProviderId");

        // Build the query
        String hql = " from SetRevenueShareConf l where l.id.txAppProviderId ='" + providerId + "'";
        List list = this.getSession().createQuery(hql).list();
        List<SetRevenueShareConf> resultList = Collections.checkedList(list, SetRevenueShareConf.class);

        if (null == resultList || resultList.isEmpty()) {
            resultList = null;
        }
        return resultList;
    }

    /**
     * Returns a list of revenue sharing models filtered by some params
     * 
     * @param aggregatorId
     * @param appProviderId
     * @param productClass
     * @return
     */
    @Override
    public List<SetRevenueShareConf> getRevenueModelsByParameters(String aggregatorId, String appProviderId,
        String productClass) {
        SetRevenueShareConfDaoImpl.LOGGER.debug("getRevenueModelsByProviderId");
 
        // Build queries
        String hql = "select * from set_revenue_share_conf l where tx_appprovider_id ='" + appProviderId + "'";

        // if appProvider not specified
        if (null == appProviderId || appProviderId.isEmpty()) {
            hql = "select * from set_revenue_share_conf l where tx_appprovider_id in " +
                "(select tx_appprovider_id from dbe_aggregator_appprovider where tx_email='" + aggregatorId + "')";
        }

        // if product class specified
        if (null != productClass) {
            hql += " and  tx_product_class='" + productClass + "'";
        }

        List list = getSession().createSQLQuery(hql).addEntity(SetRevenueShareConf.class).list();
        List<SetRevenueShareConf> resultList = Collections.checkedList(list, SetRevenueShareConf.class);

        if (null == resultList || resultList.isEmpty()) {
            resultList = null;
        }

        return resultList;
    }

}
