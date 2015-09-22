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

package es.tid.fiware.rss.expenditureLimit.dao;

import java.util.List;

import es.tid.fiware.rss.dao.GenericDao;
import es.tid.fiware.rss.expenditureLimit.model.DbeExpendControl;
import es.tid.fiware.rss.expenditureLimit.model.DbeExpendLimitPK;
import es.tid.fiware.rss.model.BmCurrency;
import es.tid.fiware.rss.model.BmObCountry;
import es.tid.fiware.rss.model.BmService;

/**
 * 
 * 
 */
public interface DbeExpendControlDao extends GenericDao<DbeExpendControl, DbeExpendLimitPK> {

    /**
     * 
     * Get the limits data for the user and application provider
     * 
     * @param urlEndUserId
     * @param appProviderId
     * @param bmCurrency
     * @return
     */
    List<DbeExpendControl> getExpendDataForUserAppProvCurrency(String urlEndUserId,
        String aggregatorId, String appProviderId, BmCurrency bmCurrency);

    /**
     * Store the data passed
     * 
     * @param expedData
     */
    void saveDbeExpendControl(DbeExpendControl expedData);
}
