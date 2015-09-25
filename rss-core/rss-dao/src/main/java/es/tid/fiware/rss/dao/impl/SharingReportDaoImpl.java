/**
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

import es.tid.fiware.rss.dao.SharingReportDao;
import es.tid.fiware.rss.model.SharingReport;


@Repository
public class SharingReportDaoImpl extends GenericDaoImpl<SharingReport, Integer> implements
        SharingReportDao {
    /**
     * Variable to print the trace.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SharingReportDaoImpl.class);

    @Override
    protected Class<SharingReport> getDomainClass() {
        return SharingReport.class;
    }

    @Override
    public List<SharingReport> getSharingReportsByParameters(String aggregator, String providerId, String productClass) {
        // Build queries
        String hql = "from SharingReport l";

        if (null != aggregator && !aggregator.isEmpty()) {
            hql += " where l.owner.id.aggregator.txEmail= '" + aggregator + "'";

            if (null != providerId && !providerId.isEmpty()) {
                hql += " and l.owner.id.txAppProviderId= '" + providerId + "'";

                if (null != productClass && !productClass.isEmpty()) {
                    hql += " and l.productClass= '" + productClass + "'";
                }
            }
        }

        List<SharingReport> resultList;
        try {
            List list = this.getSession().createQuery(hql).list();
            resultList = Collections.checkedList(list, SharingReport.class);
        } catch (Exception e) {
            return null;
        }

        if (null == resultList || resultList.isEmpty()) {
            resultList = null;
        }

        return resultList;
    }


}