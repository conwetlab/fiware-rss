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

package es.tid.fiware.rss.dao.impl;

import java.util.Collections;
import java.util.List;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import es.tid.fiware.rss.dao.CountryDao;
import es.tid.fiware.rss.model.BmCountry;

/**
 * 
 * Class that extends GenericDaoImpl and implements CountryDao.
 * 
 */
@Repository
public class CountryDaoImpl extends GenericDaoImpl<BmCountry, Long> implements CountryDao {

    /**
     * Variable to print the trace.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CountryDaoImpl.class);

    /**
     * 
     * @param factory
     *            hiberante session factory
     */
    @Autowired
    public CountryDaoImpl(final SessionFactory factory) {
        setSessionFactory(factory);
    }

    /*
     * (non-Javadoc)
     * 
     * @see es.tid.greta.bmms.dao.impl.GenericDaoImpl#getDomainClass()
     */
    @Override
    protected Class<BmCountry> getDomainClass() {
        return BmCountry.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see es.tid.greta.bmms.dao.CountryDao#getByIso3166Code(java.lang.String)
     */
    @Override
    public BmCountry getByIso3166Code(final String iso3166Code) {
        // TODO Auto-generated method stub
        String hql = "from BmCountry c where c.txIso3166Code = '" + iso3166Code + "'";
        List<BmCountry> lpp = this.listCountryQuery(hql);

        if (lpp.size() > 0) {
            return lpp.get(0);
        } else {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see es.tid.greta.bmms.dao.CountryDao#getByItuT212Code(java.lang.String)
     */
    @Override
    public BmCountry getByItuT212Code(final String ituT212Code) {
        // TODO Auto-generated method stub
        String hql = "from BmCountry c where c.txItuT212Code = '" + ituT212Code + "'";
        List<BmCountry> lpp = this.listCountryQuery(hql);

        if (lpp.size() > 0) {
            return lpp.get(0);
        } else {
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
    private List<BmCountry> listCountryQuery(final String hql) {
        CountryDaoImpl.LOGGER.debug(hql);
        // @SuppressWarnings("rawtypes")
        List list = getHibernateTemplate().find(hql);
        // @SuppressWarnings("unchecked")
        List<BmCountry> resultList = Collections.checkedList(list, BmCountry.class);
        return resultList;
    }
}
