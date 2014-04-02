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

package es.tid.fiware.rss.expenditureControl.api;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * 
 */
public class AccumExpend {

    private String type;
    private String currency;
    private Date nextPeriodStartDate;
    private BigDecimal expensedAmount;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Date getNextPeriodStartDate() {
        return nextPeriodStartDate;
    }

    public void setNextPeriodStartDate(Date nextPeriodStartDate) {
        this.nextPeriodStartDate = nextPeriodStartDate;
    }

    public BigDecimal getExpensedAmount() {
        return expensedAmount;
    }

    public void setExpensedAmount(BigDecimal expensedAmount) {
        this.expensedAmount = expensedAmount;
    }

}
