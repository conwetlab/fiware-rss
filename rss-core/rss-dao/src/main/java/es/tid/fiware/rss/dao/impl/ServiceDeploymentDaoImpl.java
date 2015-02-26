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

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import es.tid.fiware.rss.dao.ServiceDeploymentDao;
import es.tid.fiware.rss.model.BmServiceDeployment;

/**
 * 
 */
@Repository
public class ServiceDeploymentDaoImpl extends GenericDaoImpl<BmServiceDeployment, Long> implements
    ServiceDeploymentDao {

    /**
     * Variable to print the trace.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceDeploymentDaoImpl.class);

    /*
     * (non-Javadoc)
     * 
     * @see es.tid.greta.bmms.dao.impl.GenericDaoImpl#getDomainClass()
     */
    @Override
    protected Class<BmServiceDeployment> getDomainClass() {
        return BmServiceDeployment.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see es.tid.greta.bmms.dao.ServiceDeploymentDao#getDeploymentsbyServiceId(long)
     */
    @Override
    public List<BmServiceDeployment> getDeploymentsbyServiceId(final long serviceId) {

        Criteria criteria = getSession().createCriteria(BmServiceDeployment.class);
        criteria.createAlias("bmService", "bmService", CriteriaSpecification.LEFT_JOIN);
        criteria.add(Restrictions.eq("bmService.nuServiceId", serviceId));
        List<BmServiceDeployment> result = criteria.list();
        return result;
    }

    @Override
    public BmServiceDeployment getBySvcCountryOB(final Long serviceId, final Long countryId, final Long obId) {
        String hql =
            "from BmServiceDeployment l where l.bmService = " + serviceId
                + " and l.bmObCountry.id.nuCountryId = " + countryId + " and l.bmObCountry.id.nuObId = "
                + obId;

        ServiceDeploymentDaoImpl.LOGGER.debug("Query:" + hql);
        List<BmServiceDeployment> lpp = this.listServiceDeploymentQuery(hql);

        if (lpp.size() > 0) {
            return lpp.get(0);
        } else {
            // it could be only one
            return null;
        }
    }

    /**
     * listServiceDeploymentQuery to get all data from ServiceDeployment query.
     * 
     * @param hql
     *            the query to get ServiceDeployment id with the service, ob and country
     * @return a list with the results from the ServiceDeployment query
     */
    private List<BmServiceDeployment> listServiceDeploymentQuery(final String hql) {
        ServiceDeploymentDaoImpl.LOGGER.debug(hql);
        // @SuppressWarnings("rawtypes")
        List list = this.getSession().createQuery(hql).list();
        // entityManager.createQuery(hql).getResultList();
        // @SuppressWarnings("unchecked")
        List<BmServiceDeployment> resultList = Collections.checkedList(list, BmServiceDeployment.class);
        return resultList;
    }

}
