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

package es.tid.fiware.rss.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate4.HibernateTemplate;

/**
 * Abstract interface to be implemented by every DAO.
 * 
 * @param <DomainObject>
 * @param <PK>
 */
public abstract interface GenericDao<DomainObject, PK extends Serializable> {

    /**
     * @return List<T> Object list.
     */
    List<DomainObject> getAll();

    /**
     * @param id
     *            PK object.
     * @return T object.
     */
    DomainObject getById(PK id);

    /**
     * Update one instance.
     * 
     * @param object
     *            T object.
     */
    void update(DomainObject object);

    /**
     * Remove one object.
     * 
     * @param id
     *            T object
     */
    void deleteById(PK id);

    /**
     * Remove all object.
     * 
     */
    void deleteAll();

    /**
     * Remove one object.
     * 
     * @param object
     *            PK id
     */
    void delete(DomainObject object);

    /**
     * Insert one instance.
     * 
     * @param object
     *            T object.
     */
    void create(DomainObject object);

    /**
     * Insert or update one instance.
     * 
     * @param object
     *            T object.
     */
    void createOrUpdate(DomainObject object);

    /**
     * Check if an element exist.
     * 
     * @param paramPK
     *            PK object
     * @return boolean
     */
    boolean exists(PK paramPK);

    /**
     * 
     * @return int the number of object.
     */
    int count();
}
