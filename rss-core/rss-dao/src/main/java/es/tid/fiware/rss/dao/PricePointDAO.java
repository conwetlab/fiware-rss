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

import java.util.List;

import es.tid.fiware.rss.model.BmOb;
import es.tid.fiware.rss.model.BmObCountry;
import es.tid.fiware.rss.model.BmPricePoint;
import es.tid.fiware.rss.model.BmPricePointId;

/**
 * 
 * 
 * This class implements the DAO for access price points.
 * 
 */
public interface PricePointDAO extends GenericDao<BmPricePoint, BmPricePointId> {
    /* Methods for retrieve price point list */

    /**
     * Method retrieves list of all price points for an operator.
     * 
     * @param ob
     *            Operator object
     * @return List<BmPricePoint>
     */
    List<BmPricePoint> getListPricePoint(final BmOb ob);

    /**
     * Method retrieves list of all price points for an operator.
     * 
     * @param ob
     *            Operator object
     * @return List<BmPricePoint>
     */
    List<BmPricePoint> getListPricePoint(final BmObCountry ob);

    /**
     * Method retrieves list of price points with an amount.
     * 
     * @param price
     *            Amount of the price point
     * @return List<BmPricePoint>
     */
    List<BmPricePoint> getListPricePoint(final float price);

    /**
     * Method retrieves list of price points when amount is in range [minPrice,maxPrice].
     * 
     * @param minPrice
     *            Minimun amount of the searched price point
     * @param maxPrice
     *            Maximun amount of the searched price point
     * @return List<BmPricePoint>
     */
    List<BmPricePoint> getListPricePoint(final float minPrice, final float maxPrice);

    /* Methods for retrieve a price point */

    /**
     * Method retrieves a price point.
     * 
     * @param op
     * @param txPricePointId
     * @return
     */
    BmPricePoint getPricePoint(final BmObCountry op, final String txPricePointId);

    /**
     * Method retrieves a price point.
     * 
     * @param op
     * @param txPricePointId
     * @return
     */
    BmPricePoint getPricePoint(final BmObCountry op, final float price);
}
