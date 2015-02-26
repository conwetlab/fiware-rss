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
import org.springframework.transaction.annotation.Propagation;
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
import es.tid.fiware.rss.model.BmObCountry;
import es.tid.fiware.rss.model.BmObCountryId;
import es.tid.fiware.rss.model.BmService;

/**
 * 
 * 
 */
@Service
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
     * Get provider limits
     * 
     * @param appProvider
     * @param service
     * @param currency
     * @param type
     * @return
     * @throws RSSException
     */
    public LimitGroupBean getGeneralProviderExpLimitsBean(String appProvider, String service, String currency,
        String type) throws RSSException {
        ExpenditureLimitManager.logger.debug("Into getGeneralProviderExpLimitsBean method");
        // check mandatory information
        checker.checkRequiredSearchParameters(DbeExpendLimitDao.NO_USER_ID, service, appProvider);
        // check service Existence
        BmService bmService = checker.checkService(service);
        // Check valid currency
        BmCurrency bmCurrency = null;
        if (currency != null && currency.trim().length() > 0) {
            bmCurrency = checker.checkCurrency(currency);
        }
        // check valid appPorviderId
        checker.checkDbeAppProvider(appProvider);
        // check valid elType
        checker.checkElType(type);
        // unique ob
        BmObCountry obCountry = new BmObCountry();
        BmObCountryId id = new BmObCountryId();
        id.setNuCountryId(ExpenditureLimitDataChecker.countryId);
        id.setNuObId(ExpenditureLimitDataChecker.obId);
        obCountry.setId(id);
        // Get All limits associated to a provider
        HashMap<String, List<DbeExpendLimit>> limitsHash = expLimitDao.getOrdExpLimitsForUserAppProvCurrencyObCountry(
            DbeExpendLimitDao.NO_USER_ID, bmService, appProvider, bmCurrency, obCountry);

        List<DbeExpendLimit> result;
        if (null != type && type.length() > 0) {
            result = new ArrayList<DbeExpendLimit>();
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
     * Store the Expenditure control information given.
     * 
     * @param expCtrlBean
     * @param provider
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public LimitGroupBean storeGeneralProviderExpLimit(String provider, LimitGroupBean expLimits)
        throws RSSException {
        ExpenditureLimitManager.logger.debug("Into storeGeneralUserExpLimit method");
        if (expLimits == null || expLimits.getLimits() == null || expLimits.getLimits().size() <= 0) {
            String[] args = { "LimitGroupBean" };
            throw new RSSException(UNICAExceptionType.MISSING_MANDATORY_PARAMETER, args);
        }
        // check service Existence
        BmService bmService = null;
        if ((expLimits.getService() != null) && (expLimits.getService().trim().length() > 0)) {
            bmService = checker.checkService(expLimits.getService());
        } else {
            String[] args = { "Service Identifier" };
            throw new RSSException(UNICAExceptionType.MISSING_MANDATORY_PARAMETER, args);
        }
        // check valid appPorviderId
        if ((provider != null) && (provider.trim().length() > 0)) {
            checker.checkDbeAppProvider(provider);
        } else {
            String[] args = { "Provider Identifier" };
            throw new RSSException(UNICAExceptionType.MISSING_MANDATORY_PARAMETER, args);
        }

        List<DbeExpendLimit> newLimits = new ArrayList<DbeExpendLimit>();

        Iterator<LimitBean> it = expLimits.getLimits().iterator();
        LimitBean limitBean = null;
        BmCurrency bmCurrency = null;
        // Get and check limits from request
        while (it.hasNext()) {
            limitBean = it.next();
            checker.checkExpLimitData(limitBean);
            // Check valid currency
            bmCurrency = checker.checkCurrency(limitBean.getCurrency());
            // add new limit
            newLimits.add(fillLimitDao(bmService, provider, DbeExpendLimitDao.NO_USER_ID, bmCurrency, limitBean));
        }

        // Delete old provider limits
        BmObCountry bmobCountry = new BmObCountry();
        BmObCountryId id = new BmObCountryId();
        bmobCountry.setId(id);
        id.setNuCountryId(ExpenditureLimitDataChecker.countryId);
        id.setNuObId(ExpenditureLimitDataChecker.obId);

        // Get actual limits data
        List<DbeExpendLimit> oldLimits = expLimitDao.getExpendLimitsByProviderUserService(bmService, provider,
            DbeExpendLimitDao.NO_USER_ID, bmobCountry, null);
        // Delete old limits.
        if ((oldLimits != null) && (oldLimits.size() > 0)) {
            // Delete old limits
            deleteLimits(oldLimits);
        }
        // Insert new Limits
        insertNewLimits(newLimits);
        return expLimits;
    }

    /**
     * Delete Provider Limits.
     * 
     * @param urlEndUserId
     * @param providerId
     * @param fields
     * @return
     */
    public void deleteProviderLimits(String providerId, String service, String currency, String type)
        throws RSSException {
        ExpenditureLimitManager.logger.debug("Into deleteProviderLimits method");
        // Delete limits noUserId
        deleteUserLmits(providerId, DbeExpendLimitDao.NO_USER_ID, service, currency, type);
    }

    /**
     * Get limits for given user.
     * 
     * @param endUserId
     * @param appProvider
     * @param service
     * @param currency
     * @return
     * @throws RSSException
     */
    public UserExpenditureLimitInfoBean getGeneralUserExpLimitsBean(String endUserId, String appProvider,
        String service, String currency, String type) throws RSSException {
        ExpenditureLimitManager.logger.debug("Into getGeneralUserExpLimitsBean method. User:" + endUserId);
        // check mandatory information
        checker.checkRequiredSearchParameters(endUserId, service, appProvider);
        // check service Existence
        BmService bmService = checker.checkService(service);
        // Check valid currency
        BmCurrency bmCurrency = null;
        if (currency != null && currency.trim().length() > 0) {
            bmCurrency = checker.checkCurrency(currency);
        }
        // check valid appProvider
        checker.checkDbeAppProvider(appProvider);
        // check valid elType
        checker.checkElType(type);
        // unique ob
        BmObCountry obCountry = new BmObCountry();
        BmObCountryId id = new BmObCountryId();
        id.setNuCountryId(ExpenditureLimitDataChecker.countryId);
        id.setNuObId(ExpenditureLimitDataChecker.obId);
        obCountry.setId(id);
        // Get All limits associated to a user
        HashMap<String, List<DbeExpendLimit>> limitsHash = expLimitDao.getOrdExpLimitsForUserAppProvCurrencyObCountry(
            endUserId, bmService, appProvider, bmCurrency, obCountry);
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
     * @param expCtrlBean
     * @param urlEndUserId
     * @return
     */

    public UserExpenditureLimitInfoBean storeGeneralUserExpLimit(String provider, String userId,
        LimitGroupBean expLimits) throws RSSException {
        ExpenditureLimitManager.logger.debug("Into storeGeneralUserExpLimit method");
        createUserExpLimit(provider, userId, expLimits);
        return getGeneralUserExpLimitsBean(userId, provider, expLimits.getService(), null, null);
    }

    /**
     * Update limits
     * 
     * @param provider
     * @param userId
     * @param expLimits
     * @throws RSSException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    private void createUserExpLimit(String provider, String userId,
        LimitGroupBean expLimits) throws RSSException {
        ExpenditureLimitManager.logger.debug("Into createUserExpLimit method");
        if (expLimits == null || expLimits.getLimits() == null || expLimits.getLimits().size() <= 0) {
            String[] args = { "LimitGroupBean" };
            throw new RSSException(UNICAExceptionType.MISSING_MANDATORY_PARAMETER, args);
        }
        // check service Existence
        BmService bmService = null;
        if ((expLimits.getService() != null) && (expLimits.getService().trim().length() > 0)) {
            bmService = checker.checkService(expLimits.getService());
        } else {
            String[] args = { "Service Identifier" };
            throw new RSSException(UNICAExceptionType.MISSING_MANDATORY_PARAMETER, args);
        }
        // check valid appPorviderId
        if ((provider != null) && (provider.trim().length() > 0)) {
            checker.checkDbeAppProvider(provider);
        } else {
            String[] args = { "Provider Identifier" };
            throw new RSSException(UNICAExceptionType.MISSING_MANDATORY_PARAMETER, args);
        }

        List<DbeExpendLimit> newLimits = new ArrayList<DbeExpendLimit>();

        Iterator<LimitBean> it = expLimits.getLimits().iterator();
        LimitBean limitBean = null;
        BmCurrency bmCurrency = null;
        // Get and check limits from request
        while (it.hasNext()) {
            limitBean = it.next();
            checker.checkExpLimitData(limitBean);
            // Check valid currency
            bmCurrency = checker.checkCurrency(limitBean.getCurrency());
            // add new limit
            newLimits.add(fillLimitDao(bmService, provider, userId, bmCurrency, limitBean));
        }

        // Delete old limits (only provider limits or user limits too) and old accumulated?
        BmObCountry bmobCountry = new BmObCountry();
        BmObCountryId id = new BmObCountryId();
        bmobCountry.setId(id);
        id.setNuCountryId(ExpenditureLimitDataChecker.countryId);
        id.setNuObId(ExpenditureLimitDataChecker.obId);

        // Get actual limits data
        List<DbeExpendLimit> oldLimits = expLimitDao.getExpendLimitsByProviderUserService(bmService, provider,
            userId, bmobCountry, null);
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
     * @param providerId
     * @param userId
     * @param service
     * @param currency
     * @param type
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUserLmits(String providerId, String userId, String service, String currency, String type)
        throws RSSException {
        ExpenditureLimitManager.logger.debug("Into deleteUserLmits method:" + providerId + " userId:" + userId);
        // check mandatory information
        checker.checkRequiredSearchParameters(userId, service, providerId);
        // check service Existence
        BmService bmService = checker.checkService(service);
        // Check valid currency
        BmCurrency bmCurrency = null;
        if (currency != null && currency.trim().length() > 0) {
            bmCurrency = checker.checkCurrency(currency);
        }
        // check valid appPorviderId
        checker.checkDbeAppProvider(providerId);
        // check valid elType
        if (null != type && type.length() > 0) {
            ExpenditureLimitManager.logger.debug("Delete type:" + type);
            checker.checkElType(type);
        }

        // Get limits to delete
        BmObCountry bmobCountry = new BmObCountry();
        BmObCountryId id = new BmObCountryId();
        bmobCountry.setId(id);
        id.setNuCountryId(ExpenditureLimitDataChecker.countryId);
        id.setNuObId(ExpenditureLimitDataChecker.obId);

        List<DbeExpendLimit> limits = expLimitDao.getExpendLimitsByProviderUserService(bmService, providerId,
            userId, bmobCountry, bmCurrency);

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
            result = new ArrayList<DbeExpendLimit>();
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
        List<LimitBean> limits = new ArrayList<LimitBean>();
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
    private DbeExpendLimit fillLimitDao(BmService service, String appProvider, String endUserId,
        BmCurrency currency, LimitBean limitBean) {
        ExpenditureLimitManager.logger.debug("Into fillLimitDao method");
        DbeExpendLimit expendLimit = new DbeExpendLimit();
        expendLimit.setFtMaxAmount(limitBean.getMaxAmount());
        expendLimit.setTxNotifAmounts(checker.getStringFromLisAmounts(limitBean.getNotificationAmounts()));
        DbeExpendLimitPK id = new DbeExpendLimitPK();
        expendLimit.setId(id);
        id.setNuServiceId(service.getNuServiceId());
        id.setNuCountryId(ExpenditureLimitDataChecker.countryId);
        id.setNuObId(ExpenditureLimitDataChecker.obId);
        id.setTxAppProviderId(appProvider);
        id.setNuCurrencyId(currency.getNuCurrencyId());
        id.setTxElType(limitBean.getType());
        id.setTxEndUserId(endUserId);
        return expendLimit;
    }

}