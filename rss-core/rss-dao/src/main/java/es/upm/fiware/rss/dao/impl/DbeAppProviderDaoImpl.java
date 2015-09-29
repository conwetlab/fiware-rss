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

/*
 * DbeAppProviderDaoImpl.java
 * 
 * 2013 ®, Telefónica I+D, all rights reserved
 */
package es.upm.fiware.rss.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import es.upm.fiware.rss.dao.DbeAppProviderDao;
import es.upm.fiware.rss.model.DbeAppProvider;
import java.util.Collections;
import java.util.List;

/**
 * 
 * 
 */
@Repository
public class DbeAppProviderDaoImpl extends GenericDaoImpl<DbeAppProvider, String> implements DbeAppProviderDao {
    /**
     * Variable to print the trace.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DbeAppProviderDaoImpl.class);

    /*
     * (non-Javadoc)
     * 
     * @see es.upm.fiware.rss.dao.impl.GenericDaoImpl#getDomainClass()
     */
    @Override
    protected Class<DbeAppProvider> getDomainClass() {
        return DbeAppProvider.class;
    }

    @Override
    public List<DbeAppProvider> getProvidersByAggregator(String aggregatorId) {
        String hql = "from DbeAppProvider as p where p.id.aggregator='" + aggregatorId + "'";
        List<DbeAppProvider> resultList;
        try {
            List list = this.getSession().createQuery(hql).list();
            resultList = Collections.checkedList(list, DbeAppProvider.class);
        } catch (Exception e) {
            return null;
        }
        return resultList;
    }

    @Override
    public DbeAppProvider getProvider(String aggregatorId, String providerId) {
        String hql = "from DbeAppProvider as p where p.id.aggregator='" + aggregatorId + "' "
                + "and p.id.txAppProviderId='" + providerId + "'";

        DbeAppProvider provider;
        try {
            provider = (DbeAppProvider) this.getSession().createQuery(hql).uniqueResult();
        } catch (Exception e) {
            return null;
        }
        
        return provider;
    }
}
