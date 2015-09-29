/**
 * Revenue Settlement and Sharing System GE
 * Copyright (C) 2011-2014,  Javier Lucio - lucio@tid.es
 * Telefonica Investigacion y Desarrollo, S.A.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * 
 */
package es.upm.fiware.rss.expenditureLimit.api;

import java.math.BigDecimal;
import java.util.List;

public class LimitBean {
    private String type;
    private String currency;
    private BigDecimal maxAmount;
    private List<Long> notificationAmounts;

    /**
     * 
     * 
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * 
     * 
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 
     * 
     * @return
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * 
     * 
     * @param currency
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * 
     * 
     * @return
     */
    public BigDecimal getMaxAmount() {
        return maxAmount;
    }

    /**
     * 
     * 
     * @param maxAmount
     */
    public void setMaxAmount(BigDecimal maxAmount) {
        this.maxAmount = maxAmount;
    }

    /**
     * 
     * 
     * @return
     */
    public List<Long> getNotificationAmounts() {
        return notificationAmounts;
    }

    /**
     * 
     * 
     * @param notificationAmounts
     */
    public void setNotificationAmounts(List<Long> notificationAmounts) {
        this.notificationAmounts = notificationAmounts;
    }

}
