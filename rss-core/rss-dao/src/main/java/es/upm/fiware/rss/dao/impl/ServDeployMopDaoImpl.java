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

package es.upm.fiware.rss.dao.impl;

import java.util.Collections;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import es.upm.fiware.rss.dao.ServDeployMopDao;
import es.upm.fiware.rss.model.BmServdeployMop;

/**
 * 
 * 
 */
@Repository
public class ServDeployMopDaoImpl extends GenericDaoImpl<BmServdeployMop, Long> implements ServDeployMopDao {

    /**
     * Variable to print the trace.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GenericDaoImpl.class);

    @Override
    protected Class<BmServdeployMop> getDomainClass() {
        return BmServdeployMop.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see es.upm.greta.bmms.dao.ServDeployMopDao
     */
    @Override
    public List<BmServdeployMop> getServDeployMopFilter(final Long idServDeployment, final Long idCustomerType) {
        ServDeployMopDaoImpl.LOGGER.debug("into getServDeployMopFilter");
        String hql;
        if (idCustomerType == null) {
            hql = "from BmServdeployMop s where s.bmServiceDeployment = '" + idServDeployment + "'";
        } else {
            hql =
                "from BmServdeployMop s where s.bmServiceDeployment = " + idServDeployment
                    + " and s.bmCustomerType=" + idCustomerType;
        }

        ServDeployMopDaoImpl.LOGGER.debug("call to dao method: listServDeployMopQuery");
        List<BmServdeployMop> lsdmop = this.listServDeployMopQuery(hql);

        /*
         * if (lsdmop.size() > 0) {
         * return lsdmop;
         * } else {
         * return null;
         * }
         */
        return lsdmop;
    }

    @Override
    public BmServdeployMop getDefaultMop(final Long idervdeployment, final Long idcustomertype) {
        ServDeployMopDaoImpl.LOGGER.debug("into getDefaultMop");

        String hql =
            "from BmServdeployMop s where s.bmServiceDeployment = " + idervdeployment
                + " and s.bmCustomerType=" + idcustomertype + "and s.tcDefaultYn='Y'";

        List<BmServdeployMop> lsdmop = this.listServDeployMopQuery(hql);

        if (lsdmop.size() > 0) {
            return lsdmop.get(0);
        } else {
            return null;
        }
    }

    /**
     * Method executes HQL query.
     * 
     * @param hql
     *            String with HQL query
     * @return resultList
     */
    private List<BmServdeployMop> listServDeployMopQuery(final String hql) {
        ServDeployMopDaoImpl.LOGGER.debug(hql);
        // @SuppressWarnings("rawtypes")
        List list = this.getSession().createQuery(hql).list();
        // @SuppressWarnings("unchecked")
        List<BmServdeployMop> resultList = Collections.checkedList(list, BmServdeployMop.class);
        return resultList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see es.upm.greta.bmms.dao.ServDeployMopDao#listServDeployMopsbyDeploymentId(long)
     */
    @Override
    public List<BmServdeployMop> listServDeployMopsbyDeploymentId(final Long deploymentId) {
        ServDeployMopDaoImpl.LOGGER.debug("Into listServDeployMopsbyDeploymentId method");
        ServDeployMopDaoImpl.LOGGER.debug("deploymentId: " + deploymentId);
        Criteria criteria = getSession().createCriteria(BmServdeployMop.class);
        criteria.createAlias("bmServiceDeployment", "deploy");
        criteria.add(Restrictions.eq("deploy.nuDeploymentId", deploymentId));
        List<BmServdeployMop> result = criteria.list();
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see es.upm.greta.bmms.dao.ServDeployMopDao#getServiceDeployMop(long, long)
     */
    @Override
    public BmServdeployMop getServiceDeployMop(final long idServDeployment, final long idMop) {
        ServDeployMopDaoImpl.LOGGER.debug("into getServiceDeployMop");

        String hql =
            "from BmServdeployMop s where s.bmServiceDeployment = " + idServDeployment
                + " and s.bmMethodsOfPayment.nuMopId=" + idMop;
        List<BmServdeployMop> lsdmop = this.listServDeployMopQuery(hql);
        if (lsdmop.size() > 0) {
            return lsdmop.get(0);
        } else {
            return null;
        }
    }

}
