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

import es.tid.fiware.rss.dao.LanguageDao;
import es.tid.fiware.rss.model.BmLanguage;

/**
 * 
 * Class that extends GenericDaoImpl and implements LanguageDao.
 * 
 */
@Repository
public class LanguageDaoImpl extends GenericDaoImpl<BmLanguage, Long> implements LanguageDao {

    /**
     * Variable to print the trace.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(LanguageDaoImpl.class);

    /*
     * (non-Javadoc)
     * 
     * @see es.tid.greta.bmms.dao.impl.GenericDaoImpl#getDomainClass()
     */
    @Override
    protected Class<BmLanguage> getDomainClass() {
        return BmLanguage.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see es.tid.greta.bmms.dao.LanguageDao#getByIso6391Code(java.lang.String)
     */
    @Override
    public BmLanguage getByIso6391Code(final String iso6391Code) {
        // TODO Auto-generated method stub
        String hql = "from BmLanguage l where l.txIso6391Code = '" + iso6391Code + "'";
        List<BmLanguage> lpp = this.listLanguageQuery(hql);

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
    private List<BmLanguage> listLanguageQuery(final String hql) {
        LanguageDaoImpl.LOGGER.debug(hql);
        // @SuppressWarnings("rawtypes")
        List list = this.getSession().createQuery(hql).list();
        // entityManager.createQuery(hql).getResultList();
        // @SuppressWarnings("unchecked")
        List<BmLanguage> resultList = Collections.checkedList(list, BmLanguage.class);
        return resultList;
    }

}
