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

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import es.tid.fiware.rss.dao.GenericDao;

/**
 * @author Implement the basic operations of a DAO.
 * @param <DomainObject>
 * @param <PK>
 */
public abstract class GenericDaoImpl<DomainObject, PK extends Serializable> extends HibernateDaoSupport implements
    GenericDao<DomainObject, PK> {

    /**
     * Variable to print the trace.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GenericDaoImpl.class);

    /**
    * 
    */
    private final Class<DomainObject> domainClass = getDomainClass();

    /**
     * 
     * @return Class<DomainObject> Class<DomainObject>
     */
    protected abstract Class<DomainObject> getDomainClass();

    /*
     * (non-Javadoc)
     * 
     * @see org.slasoi.businessManager.common.dao.AbstractHibernateDAO#getById(KeyType)
     */
    @Override
    public DomainObject getById(final PK id) {
        return getHibernateTemplate().get(getDomainClass(), id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.slasoi.businessManager.common.dao.AbstractHibernateDAO#update(DomainObject)
     */
    @Override
    public void update(final DomainObject object) {
        getHibernateTemplate().update(object);
    }

    /*
     * (non-Javadoc)
     * 
     * @see es.tid.fiware.rss.dao.AbstractHibernateDAO#save(DomainObject)
     */
    @Override
    public void create(final DomainObject object) {
        getHibernateTemplate().save(object);
    }

    /*
     * (non-Javadoc)
     * 
     * @see es.tid.fiware.rss.dao.AbstractHibernateDAO#create(DomainObject)
     */
    @Override
    public void createOrUpdate(final DomainObject object) {
        // System.out.println("a");
        getHibernateTemplate().saveOrUpdate(object);
    }

    /*
     * (non-Javadoc)
     * 
     * @see es.tid.fiware.rss.dao.AbstractHibernateDAO#delete(DomainObject)
     */
    @Override
    public void delete(final DomainObject object) {
        getHibernateTemplate().delete(object);
    }

    /*
     * (non-Javadoc)
     * 
     * @see es.tid.fiware.rss.dao.AbstractHibernateDAO#deleteById(KeyType)
     */
    @Override
    public void deleteById(final PK id) {
        Object obj = getById(id);
        getHibernateTemplate().delete(obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see es.tid.fiware.rss.dao.AbstractHibernateDAO#getAll()
     */
    @Override
    public List<DomainObject> getAll() {
        return (getHibernateTemplate().find("from " + domainClass.getName() + " o"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see es.tid.fiware.rss.dao.AbstractHibernateDAO#deleteAll()
     */
    @Override
    public void deleteAll() {
        getHibernateTemplate().execute(new HibernateCallback() {
            @Override
            public Object doInHibernate(final Session session) {
                String hqlDelete = "delete " + domainClass.getName();
                int deletedEntities = session.createQuery(hqlDelete).executeUpdate();
                return null;
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see es.tid.fiware.rss.dao.GenericDAO#count()
     */
    @Override
    public int count() {
        List list = getHibernateTemplate().find("select count(*) from " + domainClass.getName() + " o");
        Long count = (Long) list.get(0);
        return count.intValue();
    }

    /*
     * (non-Javadoc)
     * 
     * @see es.tid.greta.bmms.dao.GenericDao#exists(java.io.Serializable)
     */
    @Override
    public boolean exists(final PK id) {
        GenericDaoImpl.LOGGER.debug("Into exists method");
        Object entity = this.getById(id);
        return (entity != null);
    }
}
