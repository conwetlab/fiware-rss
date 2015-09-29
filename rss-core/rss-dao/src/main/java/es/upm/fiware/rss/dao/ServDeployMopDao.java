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

package es.upm.fiware.rss.dao;

import java.util.List;

import es.upm.fiware.rss.model.BmServdeployMop;

/**
 * 
 * 
 */
public interface ServDeployMopDao extends GenericDao<BmServdeployMop, Long> {

    /**
     * 
     * @param idServDeployment
     *            idervdeployment
     * @param idCustomerType
     *            idcustomertype
     * @return List<BmServdeployMop> List of BmServdeployMop
     */
    List<BmServdeployMop> getServDeployMopFilter(final Long idServDeployment, final Long idCustomerType);

    /**
     * 
     * @param idServDeployment
     *            idervdeployment
     * @param idCustomerType
     *            idcustomertype
     * @return List<BmServdeployMop> List of BmServdeployMop
     */
    BmServdeployMop getDefaultMop(final Long idServDeployment, final Long idCustomerType);

    /**
     * Get Mops by service deployment.
     * 
     * @param deploymentId
     *            deploymentId
     * @return List <BmServdeployMop>
     */
    List<BmServdeployMop> listServDeployMopsbyDeploymentId(final Long deploymentId);

    /**
     * 
     * @param idServDeployment
     *            idervdeployment
     * @param idMop
     *            idMop
     * @return BmServdeployMop
     */
    BmServdeployMop getServiceDeployMop(final long idServDeployment, final long idMop);

}
