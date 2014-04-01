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

package es.tid.fiware.rss.expenditureLimit.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import es.tid.fiware.rss.dao.impl.GenericDaoImpl;
import es.tid.fiware.rss.expenditureLimit.dao.DbeExpendLimitDao;
import es.tid.fiware.rss.expenditureLimit.model.DbeExpendLimit;
import es.tid.fiware.rss.expenditureLimit.model.DbeExpendLimitPK;
import es.tid.fiware.rss.model.BmCurrency;
import es.tid.fiware.rss.model.BmObCountry;
import es.tid.fiware.rss.model.BmService;

/**
 *
 */
@Repository
public class DbeExpendLimitDaoImpl extends GenericDaoImpl<DbeExpendLimit, DbeExpendLimitPK> implements
    DbeExpendLimitDao {

    private static Logger logger = LoggerFactory.getLogger(DbeExpendLimitDaoImpl.class);

    /**
     * 
     * @param factory
     *            hibernate session factory
     */
    @Autowired
    public DbeExpendLimitDaoImpl(final SessionFactory factory) {
        setSessionFactory(factory);
    }

    /*
     * (non-Javadoc)
     * 
     * @see es.tid.greta.dbe.dao.impl.GenericDaoImpl#getDomainClass()
     */
    @Override
    protected Class<DbeExpendLimit> getDomainClass() {
        return DbeExpendLimit.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see es.tid.greta.dbe.expenditureLimit.dao.DbeExpendLimitDao#getExpendLimitsByUser(java.lang.String,
     * java.lang.Long)
     */
    @Override
    public List<DbeExpendLimit> getExpendLimitsForUserAppProvCurrencyObCountry(String urlEndUserId,
        BmService bmService, String appProviderId, BmCurrency bmCurrency, BmObCountry bmObCountry) {
        DbeExpendLimitDaoImpl.logger.debug("Entering getExpendLimitsForUserCurrencyObCountry...");

        String hql = " from DbeExpendLimit el where (el.id.txEndUserId = ? or el.id.txEndUserId = ? )" +
            " and el.id.nuServiceId = ? and el.id.nuCountryId = ? and el.id.nuObId = ?" +
            " and (el.id.txAppProviderId = ? or el.id.txAppProviderId = ?)";

        @SuppressWarnings("unchecked")
        List<DbeExpendLimit> list;
        if (bmCurrency != null) {
            hql += " and el.id.nuCurrencyId = ?";
            list = getHibernateTemplate().find(hql, DbeExpendLimitDao.NO_USER_ID, urlEndUserId,
                bmService.getNuServiceId(), bmObCountry.getId().getNuCountryId(),
                bmObCountry.getId().getNuObId(), appProviderId, DbeExpendLimitDao.NO_APP_PROVIDER_ID,
                bmCurrency.getNuCurrencyId());

        } else {
            list = getHibernateTemplate().find(hql, DbeExpendLimitDao.NO_USER_ID, urlEndUserId,
                bmService.getNuServiceId(), bmObCountry.getId().getNuCountryId(),
                bmObCountry.getId().getNuObId(), appProviderId, DbeExpendLimitDao.NO_APP_PROVIDER_ID);
        }

        return list;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * es.tid.greta.dbe.expenditureLimit.dao.DbeExpendLimitDao#getOrdExpLimitsForUserAppProvCurrencyObCountry(java.lang
     * .String, es.tid.greta.dbe.model.BmService, java.lang.String, es.tid.greta.dbe.model.BmCurrency,
     * es.tid.greta.dbe.model.BmObCountry)
     */
    @Override
    public HashMap<String, List<DbeExpendLimit>> getOrdExpLimitsForUserAppProvCurrencyObCountry(String urlEndUserId,
        BmService bmService, String appProviderId, BmCurrency bmCurrency, BmObCountry bmObCountry) {

        HashMap<String, List<DbeExpendLimit>> hLimits = new HashMap<String, List<DbeExpendLimit>>();
        List<DbeExpendLimit> allLimits = getExpendLimitsForUserAppProvCurrencyObCountry(urlEndUserId, bmService,
            appProviderId,
            bmCurrency, bmObCountry);

        // Split the limits
        List<DbeExpendLimit> userAppLimits = new ArrayList<DbeExpendLimit>();
        List<DbeExpendLimit> userLimits = new ArrayList<DbeExpendLimit>();
        List<DbeExpendLimit> appLimits = new ArrayList<DbeExpendLimit>();
        List<DbeExpendLimit> genericLimits = new ArrayList<DbeExpendLimit>();

        Iterator<DbeExpendLimit> it = allLimits.iterator();
        while (it.hasNext()) {
            DbeExpendLimit el = it.next();
            if (el.getId().getTxEndUserId().equalsIgnoreCase(DbeExpendLimitDao.NO_USER_ID)) {
                // Generic user
                if (el.getId().getTxAppProviderId().equalsIgnoreCase(DbeExpendLimitDao.NO_APP_PROVIDER_ID)) {
                    // Generic app provider
                    genericLimits.add(el);
                } else {
                    // Specific app provider
                    appLimits.add(el);
                }

            } else if (el.getId().getTxAppProviderId().equalsIgnoreCase(DbeExpendLimitDao.NO_APP_PROVIDER_ID)) {
                // Specific user and generic app provider
                userLimits.add(el);
            } else {
                // Specific user and specific app provider
                userAppLimits.add(el);
            }
        }

        hLimits.put(DbeExpendLimitDao.USER_APP_PROV_KEY, userAppLimits);
        hLimits.put(DbeExpendLimitDao.USER_KEY, userLimits);
        hLimits.put(DbeExpendLimitDao.APP_PROV_KEY, appLimits);
        hLimits.put(DbeExpendLimitDao.ALL_GENERIC_KEY, genericLimits);

        return hLimits;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * es.tid.greta.dbe.expenditureLimit.dao.DbeExpendLimitDao#getExpendLimitsByProviderUserService(es.tid.greta.dbe
     * .model.BmService, java.lang.String, java.lang.String,es.tid.greta.dbe.model.bmObCountry, es.tid.greta.dbe
     * .model.BmCurrency)
     */
    @Override
    public List<DbeExpendLimit> getExpendLimitsByProviderUserService(BmService bmService, String provider,
        String userId, BmObCountry bmObCountry, BmCurrency bmCurrency) {
        DbeExpendLimitDaoImpl.logger.debug("Entering getExpendLimitsByProviderUserService...");

        String hql = " from DbeExpendLimit el where el.id.txEndUserId = ? and el.id.txAppProviderId = ? " +
            " and el.id.nuServiceId = ? and el.id.nuCountryId = ? and el.id.nuObId = ?";
        @SuppressWarnings("unchecked")
        List<DbeExpendLimit> list = null;
        if (bmCurrency != null) {
            hql += "and el.id.nuCurrencyId = ?";
            list = getHibernateTemplate().find(hql, userId, provider,
                bmService.getNuServiceId(), bmObCountry.getId().getNuCountryId(),
                bmObCountry.getId().getNuObId(), bmCurrency.getNuCurrencyId());
        } else {
            list = getHibernateTemplate().find(hql, userId, provider,
                bmService.getNuServiceId(), bmObCountry.getId().getNuCountryId(),
                bmObCountry.getId().getNuObId());
        }

        return list;
    }
}
