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

package es.tid.fiware.rss.dao;

import es.tid.fiware.rss.model.BmLanguage;

/**
 * 
 * 
 * Interface that extends GenericDao. Interface defines additional methods.
 * 
 */
public interface LanguageDao extends GenericDao<BmLanguage, Long> {

    /**
     * Method retrieves a BmCountry that has a unique ISO 639-1 code.
     * 
     * @param iso6391Code
     *            ISO 639-1 code
     * @return BmLanguage
     */
    BmLanguage getByIso6391Code(String iso6391Code);
}
