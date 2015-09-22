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

package es.tid.fiware.rss.expenditureLimit.server.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.tid.fiware.rss.exception.RSSException;
import es.tid.fiware.rss.exception.UNICAExceptionType;
import es.tid.fiware.rss.expenditureLimit.api.LimitBean;
import es.tid.fiware.rss.expenditureLimit.api.LimitGroupBean;
import es.tid.fiware.rss.expenditureLimit.api.UserExpenditureLimitInfoBean;
import es.tid.fiware.rss.expenditureLimit.dao.DbeExpendLimitDao;
import es.tid.fiware.rss.expenditureLimit.model.DbeExpendLimit;
import es.tid.fiware.rss.expenditureLimit.model.DbeExpendLimitPK;
import es.tid.fiware.rss.model.BmCurrency;

/**
 * 
 * 
 */
@Service
@Transactional
public class ExpenditureLimitManager {

    /**
     * Variable to print the trace.
     */
    private static Logger logger = LoggerFactory.getLogger(ExpenditureLimitManager.class);

    /**
     * 
     */
    @Autowired
    private ExpenditureLimitDataChecker checker;

    @Autowired
    private DbeExpendLimitDao expLimitDao;

    /**
     * Get the general expenditure limits defined for a service provider in a 
     * given service
     * 
     * @param aggregator, Id of the aggregator where the provider belongs
     * @param appProvider, id of the service provider
     * @param service, concrete service where expenditure limits are applied
     * @param currency, concrete currency of the expenditure limits
     * @param type, type of the limits (transaction, daily, monthly,)
     * @return The filtered limits for the service provider
     * @throws RSSException
     */
    public LimitGroupBean getGeneralProviderExpLimitsBean(
            String aggregator, String appProvider,
            String service, String currency, String type) 
        throws RSSException {

        ExpenditureLimitManager.logger.debug("Into getGeneralProviderExpLimitsBean method");

        // check mandatory information
        checker.checkRequiredSearchParameters(
                DbeExpendLimitDao.NO_USER_ID, service, aggregator, appProvider);

        // Check valid currency
        BmCurrency bmCurrency = null;
        if (currency != null && !currency.trim().isEmpty()) {
            bmCurrency = checker.checkCurrency(currency);
        }

        // check valid appPorviderId
        checker.checkDbeAppProvider(aggregator, appProvider);

        // check valid elType
        checker.checkElType(type);

        // Get All limits associated to a provider
        HashMap<String, List<DbeExpendLimit>> limitsHash = expLimitDao.getOrdExpLimitsForUserAppProvCurrency(
            DbeExpendLimitDao.NO_USER_ID, aggregator, appProvider, bmCurrency);

        List<DbeExpendLimit> result;
        if (null != type && type.length() > 0) {
            result = new ArrayList<>();
            if (null != limitsHash.get(DbeExpendLimitDao.APP_PROV_KEY)
                && limitsHash.get(DbeExpendLimitDao.APP_PROV_KEY).size() > 0) {
                for (DbeExpendLimit limit : limitsHash.get(DbeExpendLimitDao.APP_PROV_KEY)) {
                    if (type.equalsIgnoreCase(limit.getId().getTxElType())) {
                        result.add(limit);
                    }
                }
            }
        } else {
            result = limitsHash.get(DbeExpendLimitDao.APP_PROV_KEY);
        }
        // change format
        LimitGroupBean limits = fillLimitGroupBean(result);
        limits.setService(service);
        return limits;
    }

    /**
     * Save Expenditure control information for a given provider.
     * 
     * @param aggregator, Aggregtaor where the provider belongs
     * @param provider, Provider that owns the new expenditure limits
     * @param expLimits, New limits for the service provider
     * @return, Returns the applied expenditure limits
     * @throws RSSException
     */
    public LimitGroupBean storeGeneralProviderExpLimit(
            String aggregator, String provider, LimitGroupBean expLimits)
        throws RSSException {

        ExpenditureLimitManager.logger.debug("Into storeGeneralUserExpLimit method");

        createUserExpLimit(aggregator, provider, DbeExpendLimitDao.NO_USER_ID, expLimits);
        return expLimits;
    }

    /**
     * Delete Provider Limits.
     * 
     * @param aggregator, Id of the aggregator where the provider belongs
     * @param providerId, Id of the service provider whose limits are going to be deleted
     * @param service, Service where limits are applied
     * @param currency, Currency used to filter the limits
     * @param type, Type of the limits to be deleted (perTransaction, monthly,
     * dayly, weekly)
     * @throws RSSException
     */
    public void deleteProviderLimits(
            String aggregator, String providerId, String service, String currency, String type)
        throws RSSException {

        ExpenditureLimitManager.logger.debug("Into deleteProviderLimits method");
        // Delete limits noUserId
        deleteUserLmits(aggregator, providerId, DbeExpendLimitDao.NO_USER_ID, service, currency, type);
    }

    /**
     * Get limits for given user for a concrete provider.
     * 
     * @param endUserId, ID of the user
     * @param aggregator, Id of the aggregator where the provider belongs
     * @param appProvider, ID of the service provider
     * @param service, Service where expenditure limits are applied
     * @param currency, Currency used to filter the limits
     * @param type, Type of the expenditure limits to be returned (perTransaction,
     * monthly, dayly, weekly)
     * @return, Returns the limits of the user
     * @throws RSSException
     */
    public UserExpenditureLimitInfoBean getGeneralUserExpLimitsBean(
            String endUserId, String aggregator, String appProvider,
            String service, String currency, String type) throws RSSException {

        ExpenditureLimitManager.logger.debug("Into getGeneralUserExpLimitsBean method. User:" + endUserId);

        // check mandatory information
        checker.checkRequiredSearchParameters(endUserId, service, aggregator, appProvider);

        // Check valid currency
        BmCurrency bmCurrency = null;
        if (currency != null && !currency.trim().isEmpty()) {
            bmCurrency = checker.checkCurrency(currency);
        }
        // check valid appProvider
        checker.checkDbeAppProvider(aggregator, appProvider);
        // check valid elType
        checker.checkElType(type);

        // Get All limits associated to a user
        HashMap<String, List<DbeExpendLimit>> limitsHash = expLimitDao.getOrdExpLimitsForUserAppProvCurrency(
            endUserId, aggregator, appProvider, bmCurrency);

        // Fill in limits
        UserExpenditureLimitInfoBean userLimits = new UserExpenditureLimitInfoBean();
        userLimits.setService(service);
        userLimits.setAppProvider(appProvider);

        // service limits
        List<DbeExpendLimit> result = getLimitsFiltered(type, limitsHash.get(DbeExpendLimitDao.ALL_GENERIC_KEY));
        userLimits.setServiceLimits(getListBeans(result));

        // provider limits
        result = getLimitsFiltered(type, limitsHash.get(DbeExpendLimitDao.APP_PROV_KEY));
        userLimits.setAppProvidersLimits(getListBeans(result));

        // User limits
        result = getLimitsFiltered(type, limitsHash.get(DbeExpendLimitDao.USER_APP_PROV_KEY));
        userLimits.setGeneralUserLimits(getListBeans(result));

        // return result
        return userLimits;
    }

    /**
     * Store the Expenditure control information given.
     * 
     * @param aggregator
     * @param provider
     * @param userId
     * @param expLimits 
     * @throws RSSException
     * @return
     */

    public UserExpenditureLimitInfoBean storeGeneralUserExpLimit(
            String aggregator, String provider, String userId, LimitGroupBean expLimits) 
        throws RSSException {

        ExpenditureLimitManager.logger.debug("Into storeGeneralUserExpLimit method");

        createUserExpLimit(aggregator, provider, userId, expLimits);
        return getGeneralUserExpLimitsBean(userId, aggregator, provider, expLimits.getService(), null, null);
    }

    /**
     * Update limits
     * 
     * @param provider
     * @param userId
     * @param expLimits
     * @throws RSSException
     */
    private void createUserExpLimit(
            String aggregator, String provider, String userId,
        LimitGroupBean expLimits) throws RSSException {

        ExpenditureLimitManager.logger.debug("Into createUserExpLimit method");

        if (expLimits == null || expLimits.getLimits() == null || expLimits.getLimits().size() <= 0) {
            String[] args = { "LimitGroupBean" };
            throw new RSSException(UNICAExceptionType.MISSING_MANDATORY_PARAMETER, args);
        }

        if (aggregator == null || aggregator.trim().isEmpty()) {
            String[] args = { "Aggregator Identifier" };
            throw new RSSException(UNICAExceptionType.MISSING_MANDATORY_PARAMETER, args);
        }

        if (provider == null || provider.trim().isEmpty()) {
            String[] args = { "Provider Identifier" };
            throw new RSSException(UNICAExceptionType.MISSING_MANDATORY_PARAMETER, args);
        }
        // check valid appPorviderId
        checker.checkDbeAppProvider(aggregator, provider);

        List<DbeExpendLimit> newLimits = new ArrayList<>();

        Iterator<LimitBean> it = expLimits.getLimits().iterator();
        LimitBean limitBean;
        BmCurrency bmCurrency;

        // Get and check limits from request
        while (it.hasNext()) {
            limitBean = it.next();
            checker.checkExpLimitData(limitBean);
            // Check valid currency
            bmCurrency = checker.checkCurrency(limitBean.getCurrency());
            // add new limit
            newLimits.add(fillLimitDao(aggregator, provider, userId, bmCurrency, limitBean));
        }

        // Get actual limits data
        List<DbeExpendLimit> oldLimits = expLimitDao.getExpendLimitsByProviderUserService(
                aggregator, provider, userId, null);

        // Delete old limits.
        if ((oldLimits != null) && (oldLimits.size() > 0)) {
            // Delete old limits
            deleteLimits(oldLimits);
        }
        // Insert new Limits
        insertNewLimits(newLimits);

    }

    /**
     * /**
     * Delete user limits.
     * 
     * @param aggregator
     * @param providerId
     * @param userId
     * @param service
     * @param currency
     * @param type
     * @throws RSSException
     */
    public void deleteUserLmits(
            String aggregator, String providerId, String userId, String service, String currency, String type)
        throws RSSException {

        ExpenditureLimitManager.logger.debug("Into deleteUserLmits method:" + providerId + " userId:" + userId);
        // check mandatory information
        checker.checkRequiredSearchParameters(userId, service, aggregator, providerId);

        // Check valid currency
        BmCurrency bmCurrency = null;
        if (currency != null && currency.trim().length() > 0) {
            bmCurrency = checker.checkCurrency(currency);
        }
        // check valid appPorviderId
        checker.checkDbeAppProvider(aggregator, providerId);
        // check valid elType
        if (null != type && type.length() > 0) {
            ExpenditureLimitManager.logger.debug("Delete type:" + type);
            checker.checkElType(type);
        }

        List<DbeExpendLimit> limits = expLimitDao.getExpendLimitsByProviderUserService(
                aggregator, providerId, userId, bmCurrency);

        // Delete limits
        if (limits != null && limits.size() > 0) {
            for (DbeExpendLimit limit : limits) {
                // if a limit type is specified
                if (null != type && type.length() > 0) {
                    if (type.equalsIgnoreCase(limit.getId().getTxElType())) {
                        expLimitDao.deleteById(limit.getId());
                    }
                } else {
                    expLimitDao.deleteById(limit.getId());
                }
            }
        }

    }

    /**
     * Add New Limits.
     * 
     * @param newLimits
     */
    private void insertNewLimits(List<DbeExpendLimit> newLimits) {
        ExpenditureLimitManager.logger.debug("Into insertNewLimits method. Limits size: " + newLimits.size());
        for (DbeExpendLimit limit : newLimits) {
            expLimitDao.createOrUpdate(limit);
        }
    }

    /**
     * Delete Limits
     * 
     * @param oldLimits
     */
    private void deleteLimits(List<DbeExpendLimit> oldLimits) {
        ExpenditureLimitManager.logger.debug("Into deleteLimits method. Limits size: " + oldLimits.size());
        for (DbeExpendLimit limit : oldLimits) {
            expLimitDao.deleteById(limit.getId());
        }
    }

    /**
     * Get filtered limits
     * 
     * @param type
     * @param limits
     * @return
     */
    private List<DbeExpendLimit> getLimitsFiltered(String type, List<DbeExpendLimit> limits) {
        List<DbeExpendLimit> result;
        if (null != type && type.length() > 0) {
            result = new ArrayList<>();
            if (null != limits && limits.size() > 0) {
                for (DbeExpendLimit limit : limits) {
                    if (type.equalsIgnoreCase(limit.getId().getTxElType())) {
                        result.add(limit);
                    }
                }
            }
        } else {
            result = limits;
        }

        return result;
    }

    /**
     * Fill in Limit group data bean.
     * 
     * @param dbeLimits
     * @return
     */
    private LimitGroupBean fillLimitGroupBean(List<DbeExpendLimit> dbeLimits) {
        ExpenditureLimitManager.logger.debug("Into fillLimitGroupBean method");
        LimitGroupBean limitGroup = new LimitGroupBean();
        limitGroup.setLimits(getListBeans(dbeLimits));
        return limitGroup;
    }

    /**
     * Get bean limits list.
     * 
     * @param dbeLimits
     * @return
     */
    private List<LimitBean> getListBeans(List<DbeExpendLimit> dbeLimits) {
        List<LimitBean> limits = new ArrayList<>();
        if ((dbeLimits != null) && (dbeLimits.size() > 0)) {
            Iterator<DbeExpendLimit> it = dbeLimits.iterator();
            LimitBean limitBean;
            while (it.hasNext()) {
                limitBean = fillLimitBean(it.next());
                if (limitBean != null) {
                    limits.add(limitBean);
                }
            }
        }
        return limits;
    }

    /**
     * Fill external limit to return.
     * 
     * @param next
     * @return
     */
    private LimitBean fillLimitBean(DbeExpendLimit next) {
        ExpenditureLimitManager.logger.debug("Into getGeneralProviderExpLimitsBean method");
        LimitBean limitBean = new LimitBean();
        limitBean.setCurrency(next.getBmCurrency().getTxIso4217Code());
        limitBean.setMaxAmount(next.getFtMaxAmount());
        limitBean.setNotificationAmounts(checker.getNumberAmounts(next.getTxNotifAmounts()));
        limitBean.setType(next.getId().getTxElType());
        return limitBean;
    }

    /**
     * Get the model object from the bean one.
     * 
     * @param service
     * @param appProvider
     * @param endUserId
     * @param currency
     * @param limitBean
     * @return
     */
    private DbeExpendLimit fillLimitDao(
            String aggregator, String appProvider, String endUserId, 
            BmCurrency currency, LimitBean limitBean) {

        ExpenditureLimitManager.logger.debug("Into fillLimitDao method");

        // Build new expenditure Limit
        DbeExpendLimit expendLimit = new DbeExpendLimit();
        expendLimit.setFtMaxAmount(limitBean.getMaxAmount());
        expendLimit.setTxNotifAmounts(checker.getStringFromLisAmounts(limitBean.getNotificationAmounts()));
        expendLimit.setBmCurrency(currency);

        // Build expenditure limit id
        DbeExpendLimitPK id = new DbeExpendLimitPK();
        id.setTxAggregatorId(aggregator);
        id.setTxAppProviderId(appProvider);
        id.setNuCurrencyId(currency.getNuCurrencyId());
        id.setTxElType(limitBean.getType());
        id.setTxEndUserId(endUserId);

        expendLimit.setId(id);
        return expendLimit;
    }

}