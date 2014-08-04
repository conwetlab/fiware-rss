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

package es.tid.fiware.rss.model;

import java.math.BigDecimal;

public class RSSModel {
    /**
     * 
     */
    private String appProviderId;
    /**
     * 
     */
    private String productClass;
    /**
     * 
     */
    private BigDecimal percRevenueShare;

    /**
     * @return the appProviderId
     */
    public String getAppProviderId() {
        return appProviderId;
    }

    /**
     * @param appProviderId
     *            the appProviderId to set
     */
    public void setAppProviderId(String appProviderId) {
        this.appProviderId = appProviderId;
    }

    /**
     * @return the productClass
     */
    public String getProductClass() {
        return productClass;
    }

    /**
     * @param productClass
     *            the productClass to set
     */
    public void setProductClass(String productClass) {
        this.productClass = productClass;
    }

    /**
     * @return the percRevenueShare
     */
    public BigDecimal getPercRevenueShare() {
        return percRevenueShare;
    }

    /**
     * @param percRevenueShare
     *            the percRevenueShare to set
     */
    public void setPercRevenueShare(BigDecimal percRevenueShare) {
        this.percRevenueShare = percRevenueShare;
    }

}
