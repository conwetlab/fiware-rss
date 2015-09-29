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

package es.upm.fiware.rss.expenditureLimit.processing;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.upm.fiware.rss.common.Constants;
import es.upm.fiware.rss.exception.RSSException;
import es.upm.fiware.rss.exception.UNICAExceptionType;
import es.upm.fiware.rss.expenditureLimit.model.DbeExpendControl;
import es.upm.fiware.rss.expenditureLimit.model.DbeExpendLimit;
import es.upm.fiware.rss.expenditureLimit.model.DbeExpendLimitPK;
import es.upm.fiware.rss.model.DbeTransaction;

/**
 * 
 * 
 */
public class ProcessingLimitUtil {
    /**
     * Variable to print the trace.
     */
    private static Logger logger = LoggerFactory.getLogger(ProcessingLimitUtil.class);

    /**
     * Calculate the nexPeriodToStart
     * 
     * @param limit
     * @throws RSSException
     */
    public void updateNextPeriodToStart(DbeExpendControl control) throws RSSException {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
        cal.setTime(new Date());

        if (ProcessingLimitService.DAY_PERIOD_TYPE.equalsIgnoreCase(control.getId().getTxElType())) {
            cal.add(Calendar.DAY_OF_MONTH, 1);
        } else if (ProcessingLimitService.MONTH_PERIOD_TYPE.equalsIgnoreCase(control.getId().getTxElType())) {
            // reset to day 1 and add a month
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.add(Calendar.MONTH, 1);
        } else if (ProcessingLimitService.WEEK_TYPE.equalsIgnoreCase(control.getId().getTxElType())) {
            // Get date of week (Sunday=1,Monday=2)
            int currentDate = cal.get(Calendar.DAY_OF_WEEK);
            if (currentDate == 1) {
                // if Sunday -->set proper day
                currentDate = 8;
            }
            // adjust date to the current Monday of this week
            cal.add(Calendar.DAY_OF_YEAR, -(currentDate - 2));
            cal.add(Calendar.WEEK_OF_YEAR, 1);
        } else {
            ProcessingLimitUtil.logger.error("Period: " + control.getId().getTxElType());
            String[] args = { "Period: " + control.getId().getTxElType() };
            throw new RSSException(UNICAExceptionType.INVALID_PARAMETER, args);
        }
        String dateParsed = format.format(cal.getTime());
        try {
            control.setDtNextPeriodStart(format.parse(dateParsed));
        } catch (Exception e) {
            ProcessingLimitUtil.logger.error("Error parsing date:" + dateParsed);
            String[] args = { "Date: " + dateParsed };
            throw new RSSException(UNICAExceptionType.INVALID_PARAMETER, args);
        }
    }

    /**
     * Calculate the new value of the limit.
     * 
     * @param limit
     * @param tx
     * @return
     */
    public BigDecimal updateAcccumalateValue(DbeExpendControl control, DbeTransaction tx) {
        BigDecimal total = new BigDecimal(0);
        total = total.add(control.getFtExpensedAmount());
        // first check TotalAmount -->if not, then amount + taxes amount
        BigDecimal amountToAdd = getValueToAddFromTx(tx);
        if (tx.getTcTransactionType()
            .equalsIgnoreCase(Constants.REFUND_TYPE)) {
            // the amount could be less than zero because of a refund.
            total = total.subtract(amountToAdd);
        } else {
            total = total.add(amountToAdd);
        }
        return total;
    }

    /**
     * Value to Add;
     * 
     * @param tx
     * @return
     */
    public BigDecimal getValueToAddFromTx(DbeTransaction tx) {
        // first check TotalAmount -->if not, then amount + taxes amount
        BigDecimal amountToAdd = new BigDecimal(0);

        if (null != tx.getFtChargedAmount()) {
            amountToAdd = amountToAdd.add(tx.getFtChargedAmount());
            // tax could be null
            if (null != tx.getFtChargedTaxAmount()) {
                amountToAdd = amountToAdd.add(tx.getFtChargedTaxAmount());
            }
        }
        return amountToAdd;
    }

    /**
     * Generate a new accumulate.
     * 
     * @param tx
     * @param limit
     * @return
     * @throws RSSException
     */
    public DbeExpendControl createControl(DbeTransaction tx, DbeExpendLimit limit) 
            throws RSSException {

        DbeExpendControl control = new DbeExpendControl();
        DbeExpendLimitPK expendLimitPK = new DbeExpendLimitPK();

        expendLimitPK.setTxEndUserId(tx.getTxEndUserId());
        expendLimitPK.setTxElType(limit.getId().getTxElType());
        expendLimitPK.setTxAggregatorId(tx.getAppProvider().getId().getAggregator().getTxEmail());
        expendLimitPK.setTxAppProviderId(tx.getAppProvider().getId().getTxAppProviderId());
        expendLimitPK.setNuCurrencyId(tx.getBmCurrency().getNuCurrencyId());

        control.setId(expendLimitPK);
        control.setFtExpensedAmount(new BigDecimal(0));
        control.setBmCurrency(tx.getBmCurrency());

        // set next period
        updateNextPeriodToStart(control);
        // notifications
        // Only notifications sent.
        return control;
    }

    /**
     * Get Limits from string.
     * 
     * @param limits
     * @return
     */
    public List<BigDecimal> getLimitsFromString(String limits) {
        if (null != limits && limits.trim().length() > 0) {
            if (limits.startsWith("[")) {
                limits = limits.substring(1);
            }
            if (limits.trim().endsWith("]")) {
                limits = limits.substring(0, limits.lastIndexOf("]"));
            }
            String[] values = limits.split(",");
            if (values != null && values.length > 0) {
                List<BigDecimal> listValues = new ArrayList<BigDecimal>();
                BigDecimal limit;
                for (String value : values) {
                    try {
                        limit = new BigDecimal(value.trim());
                        listValues.add(limit);
                    } catch (Exception e) {
                    }
                }
                return listValues;
            }
        }
        return null;
    }

    /**
     * Get last notification sent.
     * 
     * @param limits
     * @return
     */
    public BigDecimal getLastNotificationSent(String limits) {
        List<BigDecimal> notifications = getLimitsFromString(limits);
        BigDecimal lastNotification = new BigDecimal(0);
        if (null != notifications && notifications.size() > 0) {
            for (BigDecimal value : notifications) {
                if (value.compareTo(lastNotification) > 0) {
                    lastNotification = value;
                }
            }
            return lastNotification;
        }
        return null;
    }

    /**
     * Add the notifications sent to list
     * 
     * @param value
     * @param limit
     * @return
     */
    public String addValueToLimits(BigDecimal value, String limit) {
        String result = "";
        if (limit != null && limit.trim().length() > 1) {
            result = limit;
            if (result.startsWith("[")) {
                result = result.substring(1);
            }
            if (result.trim().endsWith("]")) {
                result = result.substring(0, result.lastIndexOf("]"));
            }
            if (result != null && result.trim().length() > 0) {
                return "[" + result + "," + value.toString() + "]";
            } else {
                return "[" + value.toString() + "]";
            }
        } else {
            return "[" + value.toString() + "]";
        }
    }
}
