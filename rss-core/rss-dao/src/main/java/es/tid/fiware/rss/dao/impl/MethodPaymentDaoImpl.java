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

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;

import es.tid.fiware.rss.dao.MethodPaymentDao;
import es.tid.fiware.rss.model.BmMethodsOfPayment;

/**
 * 
 * 
 */
@Repository
@Transactional
public class MethodPaymentDaoImpl extends GenericDaoImpl<BmMethodsOfPayment, Long> implements MethodPaymentDao {

    /*
     * (non-Javadoc)
     * 
     * @see es.tid.greta.bmms.dao.impl.GenericDaoImpl#getDomainClass()
     */
    @Override
    protected Class<BmMethodsOfPayment> getDomainClass() {
        return BmMethodsOfPayment.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see es.tid.greta.bmms.dao.MethodPaymentDao#getMopByName(java.lang.String)
     */
    @Override
    public BmMethodsOfPayment getMopByName(final String name) {
        Criteria criteria = getSession().createCriteria(BmMethodsOfPayment.class);
        criteria.add(Restrictions.eq("txName", name));
        List<BmMethodsOfPayment> result = criteria.list();
        if (result != null && result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
    }

    @Override
    public BmMethodsOfPayment getMopByCode(String code) {
        Criteria criteria = getSession().createCriteria(BmMethodsOfPayment.class);
        criteria.add(Restrictions.eq("txCode", code));
        List<BmMethodsOfPayment> result = criteria.list();
        if (result != null && result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
    }
}
