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
import org.springframework.stereotype.Repository;

import es.tid.fiware.rss.dao.CustomerTypeDao;
import es.tid.fiware.rss.model.BmCustomerType;

/**
 * 
 * 
 */
@Repository
public class CustomerTypeDaoImpl extends GenericDaoImpl<BmCustomerType, Long> implements CustomerTypeDao {

    /*
     * (non-Javadoc)
     * 
     * @see es.tid.greta.bmms.dao.impl.GenericDaoImpl#getDomainClass()
     */
    @Override
    protected Class<BmCustomerType> getDomainClass() {
        return BmCustomerType.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see es.tid.greta.bmms.dao.CustomerTypeDao#getCustomerTypeByName(java.lang.String)
     */
    @Override
    public BmCustomerType getCustomerTypeByName(final String name) {
        Criteria criteria = getSession().createCriteria(BmCustomerType.class);
        criteria.add(Restrictions.eq("txName", name));
        List<BmCustomerType> result = criteria.list();
        if (result != null && result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
    }

}
