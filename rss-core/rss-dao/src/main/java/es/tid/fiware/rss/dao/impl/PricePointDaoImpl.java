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

/**
 * 
 */
package es.tid.fiware.rss.dao.impl;

import java.util.Collections;
import java.util.List;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;

import es.tid.fiware.rss.dao.PricePointDAO;
import es.tid.fiware.rss.model.BmOb;
import es.tid.fiware.rss.model.BmObCountry;
import es.tid.fiware.rss.model.BmPricePoint;
import es.tid.fiware.rss.model.BmPricePointId;

/**
 * 
 */
@Repository
@Transactional
public class PricePointDaoImpl extends GenericDaoImpl<BmPricePoint, BmPricePointId> implements PricePointDAO {

    /**
     * Variable to print the trace.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PricePointDaoImpl.class);

    /*
     * (non-Javadoc)
     * 
     * @see es.tid.greta.bmms.dao.impl.GenericDaoImpl#getDomainClass()
     */
    @Override
    protected Class<BmPricePoint> getDomainClass() {
        return BmPricePoint.class;
    }

    /* Methods for retrieve price point list */

    @Override
    public List<BmPricePoint> getListPricePoint(final BmObCountry obCountry) {
        String hql = "from BmPricePoint p where p.bmObCountry.id.nuCountryId = " + obCountry.getId().getNuCountryId()
            + " and p.bmObCountry.id.nuObId = " + obCountry.getId().getNuObId();
        return this.listPricePointQuery(hql);
    }

    @Override
    public List<BmPricePoint> getListPricePoint(final BmOb ob) {
        String hql = "from BmPricePoint p where p.bmObCountry.id.nuObId = " + ob.getNuObId();
        return this.listPricePointQuery(hql);
    }

    @Override
    public List<BmPricePoint> getListPricePoint(final float price) {
        String hql = "from BmPricePoint p where p.nuPrice = " + price;
        return this.listPricePointQuery(hql);
    }

    @Override
    public List<BmPricePoint> getListPricePoint(final float minPrice, final float maxPrice) {
        String hql = "from BmPricePoint p where p.nuPrice >= " + minPrice + " and p.nuPrice <= " + maxPrice;
        return this.listPricePointQuery(hql);
    }

    /* Methods for retrieve a price point */

    @Override
    public BmPricePoint getPricePoint(final BmObCountry ob, final String txPricePointId) {
        String hql = "from BmPricePoint p where p.id.nuCountryId = " + ob.getId().getNuCountryId()
            + " and p.id.nuObId = " + ob.getId().getNuObId() + " and p.id.txPricePointId = '" + txPricePointId
            + "' order by p.dtEditDate desc";

        List<BmPricePoint> lpp = this.listPricePointQuery(hql);
        if (lpp.size() > 0) {
            return lpp.get(0);
        } else {
            return null;
        }

        /*
         * Criteria criteria = getSession().createCriteria(BmPricePoint.class);
         * criteria.add(Restrictions.eq("txPricePointId", txPricePointId)); criteria.add(Restrictions.eq("nuCountryId",
         * ob.getId().getNuCountryId())); criteria.add(Restrictions.eq("nuObId", ob.getId().getNuObId()));
         * List<BmPricePoint> result = criteria.list(); if (result != null && result.size() > 0) { return result.get(0);
         * } else { return null; }
         */
    }

    @Override
    public BmPricePoint getPricePoint(BmObCountry ob, float price) {
        String hql = "from BmPricePoint p where p.id.nuCountryId = " + ob.getId().getNuCountryId()
            + " and p.id.nuObId = " + ob.getId().getNuObId() + " and p.nuPrice = " + price
            + " order by p.dtEditDate desc";

        List<BmPricePoint> lpp = this.listPricePointQuery(hql);
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
    private List<BmPricePoint> listPricePointQuery(final String hql) {
        PricePointDaoImpl.LOGGER.debug(hql);
        // @SuppressWarnings("rawtypes")
        List list = this.getSession().createQuery(hql).list();
        // @SuppressWarnings("unchecked")
        List<BmPricePoint> resultList = Collections.checkedList(list, BmPricePoint.class);
        return resultList;
    }

}
