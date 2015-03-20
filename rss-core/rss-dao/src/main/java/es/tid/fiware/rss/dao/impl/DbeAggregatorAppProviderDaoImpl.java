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

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import es.tid.fiware.rss.dao.DbeAggregatorAppProviderDao;
import es.tid.fiware.rss.model.DbeAggregatorAppProvider;
import es.tid.fiware.rss.model.DbeAggregatorAppProviderId;

/**
 * 
 * 
 */
@Repository
public class DbeAggregatorAppProviderDaoImpl extends
    GenericDaoImpl<DbeAggregatorAppProvider, DbeAggregatorAppProviderId> implements DbeAggregatorAppProviderDao {
    /**
     * Variable to print the trace.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DbeAggregatorAppProviderDaoImpl.class);

    /*
     * (non-Javadoc)
     * 
     * @see es.tid.fiware.rss.dao.impl.GenericDaoImpl#getDomainClass()
     */
    @Override
    protected Class<DbeAggregatorAppProvider> getDomainClass() {
        return DbeAggregatorAppProvider.class;
    }

    @Override
    public List<DbeAggregatorAppProvider> getDbeAggregatorAppProviderByAggregatorId(String aggregatorId) {
        DbeAggregatorAppProviderDaoImpl.LOGGER.debug("getDbeAggregatorAppProviderByAggregatorId");
        String hql = " from DbeAggregatorAppProvider l where l.id.txEmail ='" + aggregatorId + "'";
        List list = this.getSession().createQuery(hql).list();
        List<DbeAggregatorAppProvider> resultList = Collections.checkedList(list, DbeAggregatorAppProvider.class);
        if (null != resultList && resultList.size() > 0) {
            return resultList;
        } else { // <=0
            return null;
        }

    }

}
