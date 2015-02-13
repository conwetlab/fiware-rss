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

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;

import es.tid.fiware.rss.dao.PaymentBrokerDao;
import es.tid.fiware.rss.model.BmPaymentbroker;

/**
 * 
 * 
 */
@Repository
@Transactional
public class PaymentBrokerDaoImpl extends GenericDaoImpl<BmPaymentbroker, Long> implements PaymentBrokerDao {

    /*
     * (non-Javadoc)
     * 
     * @see es.tid.greta.bmms.dao.impl.GenericDaoImpl#getDomainClass()
     */
    @Override
    protected Class<BmPaymentbroker> getDomainClass() {
        return BmPaymentbroker.class;
    }

    @Override
    public BmPaymentbroker getByName(String txName) {
        Criteria criteria = getSession().createCriteria(BmPaymentbroker.class);
        criteria.add(Restrictions.eq("txName", txName));
        BmPaymentbroker result = (BmPaymentbroker) criteria.uniqueResult();
        return result;
    }

}
