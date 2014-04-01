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

package es.tid.fiware.rss.expenditureLimit.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.tid.fiware.rss.expenditureLimit.api.LimitBean;
import es.tid.fiware.rss.expenditureLimit.api.LimitGroupBean;

/**
 * 
 * 
 */
public class ExpenditureLimitBeanConstructor {

    private static Logger logger = LoggerFactory.getLogger(ExpenditureLimitBeanConstructor.class);

    /**
     * Return a specific request previously created
     * 
     * @param type
     * @return
     * @throws Exception
     */
    public static LimitBean createExpCtrlBean() {
        ExpenditureLimitBeanConstructor.logger.debug("Into create limitBean method");
        List<Long> notifications = new ArrayList<Long>();
        notifications.add(Long.parseLong("50"));
        notifications.add(Long.parseLong("75"));
        return ExpenditureLimitBeanConstructor.createLimitBean("EUR", "daily", BigDecimal.valueOf(100), notifications);
    }

    /**
     * Generate LimitBean
     * 
     * @param currency
     * @param type
     * @param amount
     * @param notifications
     * @return
     */
    public static LimitBean createLimitBean(String currency, String type, BigDecimal amount, List<Long> notifications) {
        ExpenditureLimitBeanConstructor.logger.debug("Into create limitBean method");
        LimitBean expCtrl = new LimitBean();
        expCtrl.setCurrency(currency);
        expCtrl.setMaxAmount(amount);
        expCtrl.setType(type);
        expCtrl.setNotificationAmounts(notifications);
        return expCtrl;
    }

    /**
     * Generate LimitGroupBean for test purpose.
     * 
     * @return
     */
    public static LimitGroupBean generateLimitGroupBean() {
        LimitGroupBean limitGroupBean = new LimitGroupBean();
        limitGroupBean.setService("ServiceTest1");
        List<LimitBean> limits = new ArrayList<LimitBean>();
        List<Long> notifications = new ArrayList<Long>();
        notifications.add(Long.parseLong("50"));
        notifications.add(Long.parseLong("75"));
        limits.add(ExpenditureLimitBeanConstructor.createLimitBean("EUR", "daily", BigDecimal.valueOf(100),
            notifications));
        limits
            .add(ExpenditureLimitBeanConstructor.createLimitBean("EUR", "monthly", BigDecimal.valueOf(500),
                notifications));
        limits
            .add(ExpenditureLimitBeanConstructor.createLimitBean("GBP", "monthly", BigDecimal.valueOf(100),
                notifications));
        limitGroupBean.setLimits(limits);
        return limitGroupBean;
    }

}
