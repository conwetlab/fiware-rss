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

import es.upm.fiware.rss.model.BmCurrency;

/**
 * 
 * Interface that extends GenericDao. Interface defines additional method.
 * 
 */
public interface CurrencyDao extends GenericDao<BmCurrency, Long> {

    /**
     * Method retrieves a BmCurrency that has a unique ISO 4217 code. Example: 978-EUR
     * 
     * @param iso4217Code
     *            ISO 4217 code
     * @return BmCurrency
     */
    // BmCurrency getByIso4217Code(String iso4217Code);

    /**
     * Method retrieves a BmCurrency that has a unique ISO 4217 code. Example: EUR
     * 
     * @param iso4217Code
     *            ISO 4217 code (String, 3 characters)
     * @return BmCurrency
     */
    BmCurrency getByIso4217StringCode(String iso4217Code);

    /**
     * Method retrieves a BmCurrency that has a unique ISO 4217 code. Example: 978
     * 
     * @param iso4217Code
     *            ISO 4217 code (Integer, 3 digit)
     * @return BmCurrency
     */
    BmCurrency getByIso4217IntegerCode(int iso4217Code);
}
