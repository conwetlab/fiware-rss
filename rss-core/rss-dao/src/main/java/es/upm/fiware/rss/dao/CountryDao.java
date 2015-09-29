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

package es.upm.fiware.rss.dao;

import es.upm.fiware.rss.model.BmCountry;

/**
 * 
 * Interface that extends GenericDao. Interface defines additional methods.
 * 
 */
public interface CountryDao extends GenericDao<BmCountry, Long> {

    /**
     * Method retrieves a BmCountry that has a unique ISO 3166 code.
     * 
     * @param iso3166Code
     *            ISO 3166 code
     * @return BmCountry
     */
    BmCountry getByIso3166Code(String iso3166Code);

    /**
     * Method retrieves a BmCountry that has a unique ITU T212 code.
     * 
     * @param ituT212Code
     *            ITU T212 code
     * @return BmCountry
     */
    BmCountry getByItuT212Code(String ituT212Code);
}
