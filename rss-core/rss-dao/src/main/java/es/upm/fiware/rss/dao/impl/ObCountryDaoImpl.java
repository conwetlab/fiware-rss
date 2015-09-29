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

package es.upm.fiware.rss.dao.impl;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import es.upm.fiware.rss.dao.ObCountryDao;
import es.upm.fiware.rss.model.BmObCountry;
import es.upm.fiware.rss.model.BmObCountryId;

/**
 * 
 */
@Repository
public class ObCountryDaoImpl extends GenericDaoImpl<BmObCountry, BmObCountryId> implements ObCountryDao {

    /**
     * Variable to print the trace.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ObCountryDaoImpl.class);

    /*
     * (non-Javadoc)
     * 
     * @see es.upm.greta.bmms.dao.impl.GenericDaoImpl#getDomainClass()
     */
    @Override
    protected final Class<BmObCountry> getDomainClass() {
        return BmObCountry.class;
    }

    /**
     * 
     * @param hql
     * @return
     */
    private List<BmObCountry> listObCountryQuery(final String hql) {
        ObCountryDaoImpl.LOGGER.debug(hql);

        List<BmObCountry> list = this.getSession().createQuery(hql).list();
        List<BmObCountry> resultList = Collections.checkedList(list, BmObCountry.class);

        return resultList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see es.upm.greta.bmms.dao.ObCountryDao#listByPricePointYn(java.lang.String)
     */
    @Override
    public final List<BmObCountry> listByPricePointYn(final String flag) {
        String hql = "from BmObCountry c where c.tcPricepointsYn = '" + flag + "'";
        return this.listObCountryQuery(hql);
    }

    /*
     * (non-Javadoc)
     * 
     * @see es.upm.greta.bmms.dao.ObCountryDao#getBmObByITUData(java.lang.String)
     */
    @Override
    public final BmObCountry getBmObByITUData(final String txMncItuT212) {
        String hql = "from BmObCountry c where c.txMncItuT212 = '" + txMncItuT212 + "'";
        List<BmObCountry> resultList = this.listObCountryQuery(hql);

        if (resultList.size() == 1) {
            return resultList.get(0);
        }
        return null;
    }

}
