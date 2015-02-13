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

package es.tid.fiware.rss.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;

import es.tid.fiware.rss.dao.ProductDao;
import es.tid.fiware.rss.model.BmProduct;

/**
 * 
 * 
 */
@Repository
@Transactional
public class ProductDaoImpl extends GenericDaoImpl<BmProduct, Long> implements ProductDao {

    /**
     * Variable to print the trace.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductDaoImpl.class);

    /*
     * (non-Javadoc)
     * 
     * @see es.tid.greta.bmms.dao.impl.GenericDaoImpl#getDomainClass()
     */
    @Override
    protected Class<BmProduct> getDomainClass() {
        return BmProduct.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see es.tid.greta.bmms.dao.ProductDao#getProductByName(java.lang.String)
     */
    @Override
    public BmProduct getProductByName(final String name) {
        ProductDaoImpl.LOGGER.debug("Into getProductByName method. Name = " + name);
        Criteria criteria = getSession().createCriteria(BmProduct.class);
        criteria.add(Restrictions.eq("txName", name));
        List<BmProduct> result = criteria.list();
        if (result != null && result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
    }

    @Override
    public BmProduct getByDefault(long serviceId) {
        Criteria criteria = getSession().createCriteria(BmProduct.class)
            .add(Restrictions.eq("bmService.nuServiceId", serviceId)).add(Restrictions.eq("tcIsServiceProductYn", "Y"));
        BmProduct result = (BmProduct) criteria.uniqueResult();
        return result;
    }

}
