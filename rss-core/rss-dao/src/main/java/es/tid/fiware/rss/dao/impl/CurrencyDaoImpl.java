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

import es.tid.fiware.rss.dao.CurrencyDao;
import es.tid.fiware.rss.model.BmCurrency;

/**
 * 
 * Class that extends GenericDaoImpl and implements CurrencyDao.
 * 
 */
@Repository
public class CurrencyDaoImpl extends GenericDaoImpl<BmCurrency, Long> implements CurrencyDao {

    /**
     * Variable to print the trace.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyDaoImpl.class);


    /*
     * (non-Javadoc)
     * 
     * @see es.tid.greta.bmms.dao.impl.GenericDaoImpl#getDomainClass()
     */
    @Override
    protected Class<BmCurrency> getDomainClass() {
        return BmCurrency.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see es.tid.greta.bmms.dao.CurrencyDao#getByIso4217Code(java.lang.String)
     */
    /*
     * @Override public BmCurrency getByIso4217Code(final String iso4217Code) { String hql =
     * "from BmCurrency c where c.txIso4217Code = '" + iso4217Code + "'"; List<BmCurrency> lpp =
     * this.listCurrencyQuery(hql);
     * 
     * if (lpp.size() > 0) { return lpp.get(0); } else { return null; } }
     */

    /*
     * (non-Javadoc)
     * 
     * @see es.tid.greta.bmms.dao.CurrencyDao#getByIso4217StringCode(java.lang.String)
     */
    @Override
    public BmCurrency getByIso4217StringCode(String iso4217Code) {
        String hql = "from BmCurrency c where c.txIso4217Code ='" + iso4217Code + "'";
        List<BmCurrency> lpp = this.listCurrencyQuery(hql);

        if (lpp.size() > 0) {
            return lpp.get(0);
        } else {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see es.tid.greta.bmms.dao.CurrencyDao#getByIso4217IntegerCode(int)
     */
    @Override
    public BmCurrency getByIso4217IntegerCode(int iso4217Code) {
        String hql = "from BmCurrency c where c.txIso4217CodeNum ='" + iso4217Code + "'";
        List<BmCurrency> lpp = this.listCurrencyQuery(hql);

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
    private List<BmCurrency> listCurrencyQuery(final String hql) {
        CurrencyDaoImpl.LOGGER.debug(hql);
        // @SuppressWarnings("rawtypes")
        List list = this.getSession().createQuery(hql).list();
        // entityManager.createQuery(hql).getResultList();
        // @SuppressWarnings("unchecked")
        List<BmCurrency> resultList = Collections.checkedList(list, BmCurrency.class);
        return resultList;
    }
}
