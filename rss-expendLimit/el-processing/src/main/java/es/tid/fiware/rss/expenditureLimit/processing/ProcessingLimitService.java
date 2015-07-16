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

package es.tid.fiware.rss.expenditureLimit.processing;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.tid.fiware.rss.common.Constants;
import es.tid.fiware.rss.dao.DbeAppProviderDao;
import es.tid.fiware.rss.exception.RSSException;
import es.tid.fiware.rss.exception.UNICAExceptionType;
import es.tid.fiware.rss.expenditureLimit.dao.DbeExpendControlDao;
import es.tid.fiware.rss.expenditureLimit.dao.DbeExpendLimitDao;
import es.tid.fiware.rss.expenditureLimit.model.DbeExpendControl;
import es.tid.fiware.rss.expenditureLimit.model.DbeExpendLimit;
import es.tid.fiware.rss.model.DbeTransaction;

/**
 * 
 * 
 */
@Service("processLimitService")
@Transactional
public class ProcessingLimitService {
    /**
     * Variable to print the trace.
     */
    private static Logger logger = LoggerFactory.getLogger(ProcessingLimitService.class);
    public static String DAY_PERIOD_TYPE = "daily";
    public static String MONTH_PERIOD_TYPE = "monthly";
    public static String WEEK_TYPE = "weekly";
    public static String TRANSACTION_TYPE = "perTransaction";
    public static String EXCCED_PARAMETER_NAME = "%levelExcceded%";

    @Autowired
    private DbeExpendLimitDao expendLimitDao;

    @Autowired
    private DbeExpendControlDao expendControlDao;
    /**
     * 
     */
    @Autowired
    private DbeAppProviderDao appProviderDao;

    /**
     *
     * Check that a charge do not exceed control limit.
     * 
     * @param tx
     * @param update
     * @throws RSSException
     */
    public void proccesLimit(DbeTransaction tx) throws RSSException {
        ProcessingLimitService.logger.debug("=========== Process limit for tx:" + tx.getTxTransactionId());

        // transaction to take into account: charge, capture, refund
        String operationType = tx.getTcTransactionType();

        if (operationType.equalsIgnoreCase(Constants.CAPTURE_TYPE)
            || operationType.equalsIgnoreCase(Constants.CHARGE_TYPE)
            || operationType.equalsIgnoreCase(Constants.REFUND_TYPE)) {

        	ProcessingLimitService.logger.debug("===== IF");

            // Obtain accumulated
            List<DbeExpendControl> controls = getControls(tx);
            DbeExpendControl control;
            BigDecimal levelExceded = new BigDecimal(0);

            List<DbeExpendLimit> limits = getLimits(tx);

            if (null != limits && limits.size() > 0) {
            	ProcessingLimitService.logger.debug("===== IF LIMITS");
                boolean notification = false;

                for (DbeExpendLimit limit : limits) {
                    if (ProcessingLimitService.TRANSACTION_TYPE.equalsIgnoreCase(limit.getId().getTxElType())) {
                    	ProcessingLimitService.logger.debug("===== IF FOR");
                        ProcessingLimitUtil utils = new ProcessingLimitUtil();
                        BigDecimal total = utils.getValueToAddFromTx(tx);
                        checkMaxAmountExceed(total, limit, tx);
                    } else {
                    	ProcessingLimitService.logger.debug("===== ELSE FOR");
                        // Get control to check against the limit
                        control = getExpendControlToCheck(controls, limit, tx);
                        // check end period do not exceeded
                        // reset control limit if done
                        // Add amount.
                        // Obtain the limits and accumulates of an user.
                        // check if there is notifications
                        BigDecimal controlLevelExceded = checkLimit(control, limit, tx);
                        if (controlLevelExceded.compareTo(levelExceded) > 0) {
                            ProcessingLimitService.logger.debug("level exceeded: " + controlLevelExceded);
                            levelExceded = controlLevelExceded;
                            notification = true;
                        }
                    }
                }
                if (notification) {
                    ProcessingLimitService.logger.warn("Notification limit " + levelExceded + " exceed for tx:"
                        + tx.getTxTransactionId());
                }
            }
        }
    }

    /**
     * Get the Control limit to check. If not exist generate it.
     * 
     * @param controls
     * @param type
     * @param tx
     * @return
     */
    @Transactional
    private DbeExpendControl getExpendControlToCheck(List<DbeExpendControl> controls, DbeExpendLimit limit,
        DbeTransaction tx)
        throws RSSException {
        // Obtain accumulated --> if not exist --> create it.
        if (null != controls && controls.size() > 0) {
            for (DbeExpendControl control : controls) {
                if (limit.getId().getTxElType().equalsIgnoreCase(control.getId().getTxElType())) {
                    return control;
                }
            }
        }
        ProcessingLimitUtil utils = new ProcessingLimitUtil();
        // Create control
        DbeExpendControl control = utils.createControl(tx, limit);
        // save control.
        expendControlDao.createOrUpdate(control);
        // Add control to the list to take into account
        controls.add(control);
        return control;
    }

    /**
     * Update control limit after a charge.
     * 
     * @param tx
     * @param update
     * @throws RSSException
     */
    @Transactional
    public void updateLimit(DbeTransaction tx) throws RSSException {
        ProcessingLimitService.logger.debug("Update limit for tx: " + tx.getTxTransactionId());

        // transaction to take into account: charge, capture, refund
        String operationType = tx.getTcTransactionType();
        if ((operationType.equalsIgnoreCase(Constants.CAPTURE_TYPE)
            || operationType.equalsIgnoreCase(Constants.CHARGE_TYPE)
            || operationType.equalsIgnoreCase(Constants.REFUND_TYPE))) {

            ProcessingLimitUtil utils = new ProcessingLimitUtil();
            List<DbeExpendControl> controls = getControls(tx);
            // always update limits to take into account two charges at the same time
            if (null != controls && controls.size() > 0) {
                for (DbeExpendControl control : controls) {
                    // check the period in order to reset it.
                    ckeckPeriodLimit(control);
                    // update the quantity.
                    BigDecimal total = utils.updateAcccumalateValue(control, tx);
                    control.setFtExpensedAmount(total);
                    expendControlDao.createOrUpdate(control);
                }
            }
        }
    }

    /**
     * Get Controls.
     * 
     * @param tx
     * @throws RSSException
     */
    private List<DbeExpendControl> getControls(DbeTransaction tx) throws RSSException {
        // could be end user or globlaUser
        List<DbeExpendControl> controls = expendControlDao.getExpendDataForUserAppProvCurrency(tx.getTxEndUserId(),
                    tx.getAppProvider().getId().getTxAppProviderId(), tx.getBmCurrency());

        return controls;
    }

    /**
     * Get Limits.
     * 
     * @param tx
     * @throws RSSException
     */
    private List<DbeExpendLimit> getLimits(DbeTransaction tx) throws RSSException {
        // could be end user or globlaUser
        HashMap<String, List<DbeExpendLimit>> limitsHash = expendLimitDao.
                getOrdExpLimitsForUserAppProvCurrency(tx.getTxEndUserId(),
                tx.getAppProvider().getId().getTxAppProviderId(), tx.getBmCurrency());

        List<DbeExpendLimit> limits = new ArrayList<>();
        limits.addAll(limitsHash.get(DbeExpendLimitDao.USER_APP_PROV_KEY));
        limits.addAll(limitsHash.get(DbeExpendLimitDao.USER_KEY));
        limits.addAll(limitsHash.get(DbeExpendLimitDao.APP_PROV_KEY));
        limits.addAll(limitsHash.get(DbeExpendLimitDao.ALL_GENERIC_KEY));
        return limits;
    }

    /**
     * Check that once limit is not exceed
     * 
     * @param limit
     * @param tx
     * @throws RSSException
     * @return boolean
     */
    private BigDecimal checkLimit(DbeExpendControl control, DbeExpendLimit limit, DbeTransaction tx)
        throws RSSException {
        ProcessingLimitUtil utils = new ProcessingLimitUtil();
        // check end period do not exceed
        // reset control limit if done
        // check limit and update if needed.
        ckeckPeriodLimit(control);
        // Update value.
        BigDecimal total = utils.updateAcccumalateValue(control, tx);
        // check that the limit is not exceed
        checkMaxAmountExceed(total, limit, tx);
        // check notifications
        return checkNotification(control, limit, total);
    }

    /**
     * Check amounts and generate exceptions.
     * 
     * @param total
     * @param limit
     * @param tx
     * @throws RSSException
     */
    private void checkMaxAmountExceed(BigDecimal total, DbeExpendLimit limit, DbeTransaction tx) throws RSSException {
        // check that the limit is not exceed
        if (total.compareTo(limit.getFtMaxAmount()) > 0) {
            ProcessingLimitService.logger.error("Credit limit " + limit.getFtMaxAmount() + " exceeded "
                + limit.getId().getTxElType() + " for tx:" + tx.getTxTransactionId());
            String[] args = {
                "Limit " + limit.getId().getTxElType() + " exceeded by transactionId:" + tx.getTxTransactionId() };
            throw new RSSException(UNICAExceptionType.INSUFFICIENT_MOP_BALANCE, args);
        }
    }

    /**
     * Check notifications to sent
     * 
     * @param control
     * @param limit
     * @param total
     * @return
     */
    @Transactional
    private BigDecimal checkNotification(DbeExpendControl control, DbeExpendLimit limit, BigDecimal total) {
        ProcessingLimitUtil utils = new ProcessingLimitUtil();
        List<BigDecimal> limits = utils.getLimitsFromString(limit.getTxNotifAmounts());
        BigDecimal maxLevel = new BigDecimal(0);
        if (null != limits && limits.size() > 0) {
            for (BigDecimal value : limits) {
                // the total is equal or greater to the threshold
                if (total.compareTo(value) >= 0) {
                    // Obtain the maxLevel
                    if (value.compareTo(maxLevel) > 0) {
                        maxLevel = value;
                    }
                }
            }
        }
        ProcessingLimitService.logger.debug("MaxLevel reached: " + maxLevel);
        // Compare against the notifications sent
        BigDecimal lastNotificationSent = utils.getLastNotificationSent(control.getTxNotifications());
        if (null == lastNotificationSent) {
            lastNotificationSent = new BigDecimal(0);
        }
        ProcessingLimitService.logger.debug("Last notification sent: " + lastNotificationSent);
        if (maxLevel.compareTo(lastNotificationSent) > 0) {
            ProcessingLimitService.logger.debug("Add Notification for level: " + maxLevel);
            // Add new notification and sent.
            control.setTxNotifications(utils.addValueToLimits(maxLevel, control.getTxNotifications()));
            expendControlDao.createOrUpdate(control);
            return maxLevel;
        }
        return new BigDecimal(0);
    }

    /**
     * Check that a period has not been exceeded in order to reset it.
     * 
     * @param limit
     * @throws RSSException
     */
    @Transactional
    private void ckeckPeriodLimit(DbeExpendControl control) throws RSSException {
        if (null != control.getDtNextPeriodStart()
            && control.getDtNextPeriodStart().compareTo(new Date()) <= 0) {
            ProcessingLimitUtil utils = new ProcessingLimitUtil();
            ProcessingLimitService.logger.debug("Reset control " + control.getId().getTxElType());
            // if period has ended.
            // reset Amount
            control.setFtExpensedAmount(new BigDecimal(0));
            // reset notifications
            control.setTxNotifications("");
            // Update NextPeriodStart
            utils.updateNextPeriodToStart(control);
            expendControlDao.createOrUpdate(control);
        }
    }
}
