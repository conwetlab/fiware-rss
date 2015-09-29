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

package es.upm.fiware.rss.expenditureLimit.server.service;

import es.upm.fiware.rss.common.properties.AppProperties;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.upm.fiware.rss.dao.CurrencyDao;
import es.upm.fiware.rss.dao.DbeAppProviderDao;
import es.upm.fiware.rss.dao.ServiceDao;
import es.upm.fiware.rss.dao.UserDao;
import es.upm.fiware.rss.exception.RSSException;
import es.upm.fiware.rss.exception.UNICAExceptionType;
import es.upm.fiware.rss.expenditureLimit.api.LimitBean;
import es.upm.fiware.rss.model.BmCurrency;
import es.upm.fiware.rss.model.BmService;
import es.upm.fiware.rss.model.DbeAppProvider;
import es.upm.fiware.rss.model.RSUser;
import es.upm.fiware.rss.model.Role;
import java.util.Iterator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 */
@Service
@Transactional
public class ExpenditureLimitDataChecker {

    /**
     * Variable to print the trace.
     */
    private static Logger logger = LoggerFactory.getLogger(ExpenditureLimitDataChecker.class);

    @Autowired
    private CurrencyDao currencyDao;

    @Autowired
    private ServiceDao serviceDao;

    @Autowired
    private DbeAppProviderDao dbeAppProviderDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    @Qualifier(value = "oauthProperties")
    private AppProperties oauthProperties;
    /**
     * 
     */
    // Default ob-country
    public static final long countryId = 1;
    public static final long obId = 1;
    //
    public static final String monthly = "monthly";
    public static final String daily = "daily";
    public static final String weekly = "weekly";
    public static final String perTransaction = "perTransaction";

    /**
     * Check the userId existence.
     * 
     * @param datum
     * @throws RSSException
     */
    public void checkMandatoryDatumExistence(String datum, String datumName) throws RSSException {
        ExpenditureLimitDataChecker.logger.debug("Accessing checkEndUserId..");
        if ((datum == null) || (datum.trim().length() == 0)) {
            String[] args = {datumName};
            throw new RSSException(UNICAExceptionType.MISSING_MANDATORY_PARAMETER, args);
        }
    }

    /**
     * Make basic checks over expenditure limit data.
     * The controls are:
     * - Exists type and currency
     * - No notification limit exceed the amount limit.
     * 
     * @param expLimitBean
     */
    public void checkExpLimitData(LimitBean expLimitBean) throws RSSException {
        if (expLimitBean != null) {
            checkMandatoryDatumExistence(expLimitBean.getType(), "Limit type");
            checkMandatoryDatumExistence(expLimitBean.getCurrency(), "Limit Currency");
            checkCurrency(expLimitBean.getCurrency());
            checkNotificationLimits(expLimitBean.getMaxAmount().longValue(), expLimitBean.getNotificationAmounts());
        } else {
            String[] args = {"Currency and Type"};
            throw new RSSException(UNICAExceptionType.MISSING_MANDATORY_PARAMETER, args);
        }
    }

    /**
     * 
     * @param maxAmount
     * @param defaultProvThresholds
     */
    private void checkNotificationLimits(Long maxAmount, List<Long> sNotifAmounts) throws RSSException {

        if ((maxAmount > 0) && (sNotifAmounts != null) && (sNotifAmounts.size() > 0)) {
            ListIterator<Long> litr = sNotifAmounts.listIterator();
            while (litr.hasNext()) {
                long notifAmount = litr.next().longValue();
                if (notifAmount > maxAmount) {
                    String[] args = {"Amount notification (" + notifAmount + ") is greather than total limit ("
                        + maxAmount + ")"};
                    throw new RSSException(UNICAExceptionType.INVALID_INPUT_VALUE, args);
                }
            }
        }
    }

    /**
     * Get value numbers from String
     * 
     * @param sNotifAmounts
     * @return
     */
    public List<Long> getNumberAmounts(String sNotifAmounts) {
        if (null != sNotifAmounts && sNotifAmounts.trim().length() > 0) {
            if (sNotifAmounts.startsWith("[")) {
                sNotifAmounts = sNotifAmounts.substring(1);
            }
            if (sNotifAmounts.trim().endsWith("]")) {
                sNotifAmounts = sNotifAmounts.substring(0, sNotifAmounts.lastIndexOf("]"));
            }
            String[] values = sNotifAmounts.split(",");
            if (values != null && values.length > 0) {
                List<Long> listValues = new ArrayList<Long>();
                Long limit;
                for (String value : values) {
                    try {
                        limit = new Long(value.trim());
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
     * Get String from value numbers
     * 
     * @param sNotifAmounts
     * @return
     */
    public String getStringFromLisAmounts(List<Long> sNotifAmounts) {
        String result = "";

        if (null != sNotifAmounts && sNotifAmounts.size() > 0) {
            result = "[";
            for (Long amount : sNotifAmounts) {
                result = result + amount.toString() + ',';
            }
            result = result.substring(0, result.length() - 2);
            result += "]";
            return result;
        }
        return null;
    }

    /**
     * Verify and get the currency.
     * 
     * @param currency
     * @return
     */
    public BmCurrency checkCurrency(String userCurrency) throws RSSException {
        BmCurrency currency = currencyDao.getByIso4217StringCode(userCurrency);
        if (null == currency) {
            String[] args = {"Currency Not found."};
            throw new RSSException(UNICAExceptionType.NON_EXISTENT_RESOURCE_ID, args);
        }

        return currency;
    }

    /**
     * Verify and get the service.
     * 
     * @param service
     * @return
     * @throws RSSException
     */
    /*public BmService checkService(String serviceName) throws RSSException {
        BmService service = serviceDao.getServiceByName(serviceName);
        if (null == service) {
            String[] args = {"Service Not found."};
            throw new RSSException(UNICAExceptionType.NON_EXISTENT_RESOURCE_ID, args);
        }
        return service;
    }*/

    /**
     * Verify and get the DbeAppProvider.
     * 
     * @param appProviderId, Id of the provider
     * @param aggregatorId , Id of the given aggregator
     * @return
     */
    public DbeAppProvider checkDbeAppProvider(String aggregatorId,
            String appProviderId) throws RSSException {

        DbeAppProvider appProvider = dbeAppProviderDao.getProvider(aggregatorId, appProviderId);
        if (null == appProvider) {
            String[] args = {"AppProvider Not found."};
            throw new RSSException(UNICAExceptionType.NON_EXISTENT_RESOURCE_ID, args);
        }

        return appProvider;
    }

    /**
     * Verify El type.
     * 
     * @param eltype
     * @return
     */
    public void checkElType(String eltype) throws RSSException {
        if (null != eltype && !ExpenditureLimitDataChecker.monthly.equalsIgnoreCase(eltype)
            && !ExpenditureLimitDataChecker.daily.equalsIgnoreCase(eltype)
            && !ExpenditureLimitDataChecker.weekly.equalsIgnoreCase(eltype)) {
            String[] args = {"eltype Not found."};
            throw new RSSException(UNICAExceptionType.NON_EXISTENT_RESOURCE_ID, args);
        }
    }

    /**
     * Verify charge type.
     * 
     * @param chargeType
     * @return
     */
    public void checkChargeType(String chargeType) throws RSSException {
        if (!"R".equalsIgnoreCase(chargeType) && !"C".equalsIgnoreCase(chargeType)) {
            String[] args = {"chageType Not found."};
            throw new RSSException(UNICAExceptionType.NON_EXISTENT_RESOURCE_ID, args);
        }
    }

    /**
     * 
     * @param urlEndUserId
     * @param appPorviderId
     * @param currency
     * @throws RSSException
     */
    public void checkChargeRequiredParameters(
            String urlEndUserId, String service, String aggregator,
            String appPorviderId, String currency, String chargeType,
            BigDecimal amount)
        throws RSSException {

        if (null == urlEndUserId || urlEndUserId.trim().isEmpty() ||
            null == service || service.trim().isEmpty() ||
            null == aggregator || aggregator.trim().isEmpty() ||
            null == appPorviderId || appPorviderId.trim().isEmpty() ||
            null == currency || currency.trim().isEmpty() ||
            null == chargeType || chargeType.trim().isEmpty() ||
            null == amount || amount.compareTo(new BigDecimal(0)) < 1) {
            String[] args = {"Required parameters not found:enUserId, service, aggregator, appProvider, currency, chargeType, amount."};
            throw new RSSException(UNICAExceptionType.NON_EXISTENT_RESOURCE_ID, args);
        }

    }

    /**
     * Check required information
     * 
     * @param urlEndUserId
     * @param appPorviderId
     * @param currency
     */
    public void checkRequiredParameters(
            String service, String urlEndUserId,
            String aggregator, String appPorviderId, String currency)
        throws RSSException {

        if (null == urlEndUserId || urlEndUserId.trim().isEmpty() ||
            null == service || service.trim().isEmpty() ||
            null == aggregator || aggregator.trim().isEmpty() ||
            null == appPorviderId || appPorviderId.trim().isEmpty() ||
            null == currency || currency.trim().isEmpty()) {
            String[] args = {"Required parameters not found:enUserId, service, appProvider, currency."};
            throw new RSSException(UNICAExceptionType.NON_EXISTENT_RESOURCE_ID, args);
        }

    }

    /**
     * Check required information
     * 
     * @param aggregator
     * @param service
     * @param urlEndUserId
     * @param appPorviderId
     */
    public void checkRequiredSearchParameters(
            String service, String urlEndUserId, String aggregator, String appPorviderId)
        throws RSSException {

        if (null == urlEndUserId || urlEndUserId.trim().isEmpty() ||
            null == service || service.trim().isEmpty() ||
            null == aggregator || aggregator.trim().isEmpty() ||
            null == appPorviderId || appPorviderId.trim().isEmpty()) {
            String[] args = {"Required parameters not found:endUserId, service, aggregator, appProvider."};
            throw new RSSException(UNICAExceptionType.NON_EXISTENT_RESOURCE_ID, args);
        }

    }

    public void checkCurrentUserPermissions() throws RSSException {
        boolean found = false;
        RSUser user = userDao.getCurrentUser();

        String adminRole = oauthProperties.getProperty("config.grantedRole");
        Iterator<Role> rolesIt = user.getRoles().iterator();

        // If the user is admin or an aggregator, is authorized
        while (!found && rolesIt.hasNext()) {
            String role = rolesIt.next().getName();
            if (role.equalsIgnoreCase(adminRole) ||
                    role.equalsIgnoreCase("aggregator")) {
                found = true;
            }
        }

        // No allowed role has been found
        if (!found) {
            String[] args = {"The user is not authorized to retrieve ecpenditure limits"};
            throw new RSSException(UNICAExceptionType.NON_ALLOWED_OPERATION, args);
        }
    }
}
