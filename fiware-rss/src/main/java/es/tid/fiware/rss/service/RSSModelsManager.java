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

package es.tid.fiware.rss.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.tid.fiware.rss.dao.DbeAggregatorAppProviderDao;
import es.tid.fiware.rss.dao.DbeAppProviderDao;
import es.tid.fiware.rss.dao.SetRevenueShareConfDao;
import es.tid.fiware.rss.exception.RSSException;
import es.tid.fiware.rss.exception.UNICAExceptionType;
import es.tid.fiware.rss.model.DbeAggregatorAppProvider;
import es.tid.fiware.rss.model.DbeAppProvider;
import es.tid.fiware.rss.model.RSSModel;
import es.tid.fiware.rss.model.SetRevenueShareConf;
import es.tid.fiware.rss.model.SetRevenueShareConfId;

@Service
@Transactional
public class RSSModelsManager {

    /***
     * Logging system.
     */
    private final Logger logger = LoggerFactory.getLogger(RSSModelsManager.class);
    /**
     * 
     */
    @Autowired
    private DbeAggregatorAppProviderDao aggregatorAppProviderDao;
    /**
     * 
     */
    @Autowired
    private DbeAppProviderDao appProviderDao;
    /**
     * 
     */
    @Autowired
    private SetRevenueShareConfDao revenueShareConfDao;
    /**
     * private properties
     */
    private Long countryId = Long.valueOf(1);
    private Long obId = Long.valueOf(1);

    /**
     * Get models.
     * 
     * @param aggregatorId
     * @param appProviderId
     * @param productClass
     * @return
     * @throws Exception
     */
    public List<RSSModel> getRssModels(String aggregatorId, String appProviderId, String productClass) throws Exception {
        logger.debug("Into getRssModels() method");
        if (null != appProviderId && !appProviderId.equalsIgnoreCase("")) {
            checkValidAppProvider(aggregatorId, appProviderId);
        }
        List<RSSModel> models = new ArrayList<RSSModel>();
        List<SetRevenueShareConf> result = revenueShareConfDao.getRevenueModelsByParameters(aggregatorId,
            appProviderId, productClass);
        // convert result to api model.
        if (null != result && result.size() > 0) {
            for (SetRevenueShareConf model : result) {
                models.add(convertIntoApiModel(model));
            }
        }
        return models;
    }

    /**
     * Create RSS Model.
     * 
     * @param aggregatorId
     * @param rssModel
     * @return
     * @throws Exception
     */
    public RSSModel createRssModel(String aggregatorId, RSSModel rssModel) throws Exception {
        logger.debug("Into createRssModel() method");
        // check valid rssModel
        checkValidRSSModel(rssModel);
        // check valid appProvider
        checkValidAppProvider(aggregatorId, rssModel.getAppProviderId());
        // Insert data into model
        SetRevenueShareConf model = new SetRevenueShareConf();
        SetRevenueShareConfId id = new SetRevenueShareConfId();
        model.setId(id);
        model.setNuPercRevenueShare(rssModel.getPercRevenueShare());
        id.setTxAppProviderId(rssModel.getAppProviderId());
        id.setProductClass(rssModel.getProductClass());
        id.setNuObId(obId);
        id.setCountryId(countryId);
        // Save model into database
        revenueShareConfDao.create(model);
        // return model
        return convertIntoApiModel(model);
    }

    /**
     * Update RSS model.
     * 
     * @param aggregatorId
     * @param rssModel
     * @return
     * @throws Exception
     */
    public RSSModel updateRssModel(String aggregatorId, RSSModel rssModel) throws Exception {
        logger.debug("Into updateRssModel() method");
        // check valid rssModel
        checkValidRSSModel(rssModel);
        // check valid appProvider
        checkValidAppProvider(aggregatorId, rssModel.getAppProviderId());
        // Insert data into model
        SetRevenueShareConfId id = new SetRevenueShareConfId();
        id.setTxAppProviderId(rssModel.getAppProviderId());
        id.setProductClass(rssModel.getProductClass());
        id.setNuObId(obId);
        id.setCountryId(countryId);
        SetRevenueShareConf model = revenueShareConfDao.getById(id);
        if (null == model) {
            String[] args = {"Non existing Rss Model."};
            throw new RSSException(UNICAExceptionType.NON_EXISTENT_RESOURCE_ID, args);
        }
        model.setNuPercRevenueShare(rssModel.getPercRevenueShare());
        // Save model into database
        revenueShareConfDao.update(model);
        // return model
        return convertIntoApiModel(model);
    }

    /**
     * Delete RSS models.
     * 
     * @param aggregatorId
     * @param appProviderId
     * @param productClass
     * @throws Exception
     */
    public void deleteRssModel(String aggregatorId, String appProviderId, String productClass) throws Exception {
        logger.debug("Into deleteRssModel() method");
        // check valid appProvider
        if (null == appProviderId || appProviderId.equalsIgnoreCase("")) {
            String[] args = {"Required parameters not found: appProviderId."};
            throw new RSSException(UNICAExceptionType.NON_EXISTENT_RESOURCE_ID, args);
        } else {
            checkValidAppProvider(aggregatorId, appProviderId);
        }
        // Get models and delete them
        List<SetRevenueShareConf> result = revenueShareConfDao.getRevenueModelsByParameters(aggregatorId,
            appProviderId, productClass);
        // convert result to api model.
        if (null != result && result.size() > 0) {
            for (SetRevenueShareConf model : result) {
                revenueShareConfDao.delete(model);
            }
        }
    }

    /**
     * Check that a appProviderId is valid for the current aggregator.
     * 
     * @param aggregatorId
     * @param appProviderId
     * @throws Exception
     */
    public void checkValidAppProvider(String aggregatorId, String appProviderId) throws Exception {
        logger.debug("Into checkValidAppProvider mehtod : aggregator:{} provider:{}", aggregatorId, appProviderId);
        DbeAppProvider provider = appProviderDao.getById(appProviderId);
        if (null == provider) {
            String[] args = {"Non existing: appProviderId"};
            throw new RSSException(UNICAExceptionType.NON_EXISTENT_RESOURCE_ID, args);
        }
        List<DbeAggregatorAppProvider> provsAgg = aggregatorAppProviderDao
            .getDbeAggregatorAppProviderByAggregatorId(aggregatorId);
        if (null != provsAgg && provsAgg.size() > 0) {
            for (DbeAggregatorAppProvider provAgg : provsAgg) {
                if (provAgg.getDbeAppProvider().getTxAppProviderId().equalsIgnoreCase(appProviderId)) {
                    return;
                }
            }
        }
        // not valid for this provider
        String[] args = {"Non existing: appProviderId"};
        throw new RSSException(UNICAExceptionType.NON_EXISTENT_RESOURCE_ID, args);
    }

    /**
     * Check that a RssModels contains all required Information.
     * 
     * @param aggregatorId
     * @param appProviderId
     * @throws Exception
     */
    public void checkValidRSSModel(RSSModel rssModel) throws Exception {
        logger.debug("Into checkValidRSSModel mehtod");
        if (null == rssModel.getAppProviderId() || "".equalsIgnoreCase(rssModel.getAppProviderId())) {
            String[] args = {"Required parameters not found: appProviderId"};
            throw new RSSException(UNICAExceptionType.NON_EXISTENT_RESOURCE_ID, args);
        }
        if (null == rssModel.getPercRevenueShare()) {
            String[] args = {"Required parameters not found: percRevenueShare"};
            throw new RSSException(UNICAExceptionType.NON_EXISTENT_RESOURCE_ID, args);
        } else if (rssModel.getPercRevenueShare().compareTo(BigDecimal.ZERO) <= 0) {
            String[] args = {"percRevenueShare must be greater than 0"};
            throw new RSSException(UNICAExceptionType.INVALID_INPUT_VALUE, args);
        }
    }

    /**
     * Convert to api model.
     * 
     * @param model
     * @return
     */
    public RSSModel convertIntoApiModel(SetRevenueShareConf model) {
        RSSModel rssModel = new RSSModel();
        rssModel.setAppProviderId(model.getId().getTxAppProviderId());
        rssModel.setProductClass(model.getId().getProductClass());
        rssModel.setPercRevenueShare(model.getNuPercRevenueShare());
        return rssModel;
    }

}
