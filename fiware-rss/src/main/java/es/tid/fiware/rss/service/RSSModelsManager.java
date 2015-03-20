/**
 * Revenue Settlement and Sharing System GE
 * Copyright (C) 2011-2014, Javier Lucio - lucio@tid.es
 * Telefonica Investigacion y Desarrollo, S.A.
 *
 * Copyright (C) 2015 CoNWeT Lab., Universidad Politécnica de Madrid
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
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.tid.fiware.rss.dao.DbeAggregatorAppProviderDao;
import es.tid.fiware.rss.dao.DbeAggregatorDao;
import es.tid.fiware.rss.dao.DbeAppProviderDao;
import es.tid.fiware.rss.dao.SetRevenueShareConfDao;
import es.tid.fiware.rss.exception.RSSException;
import es.tid.fiware.rss.exception.UNICAExceptionType;
import es.tid.fiware.rss.model.DbeAggregator;
import es.tid.fiware.rss.model.DbeAggregatorAppProvider;
import es.tid.fiware.rss.model.DbeAppProvider;
import es.tid.fiware.rss.model.ModelProvider;
import es.tid.fiware.rss.model.RSSModel;
import es.tid.fiware.rss.model.RevenueShareAggregator;
import es.tid.fiware.rss.model.SetRevenueShareConf;
import es.tid.fiware.rss.model.SetRevenueShareConfId;
import es.tid.fiware.rss.model.StakeholderModel;

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

    @Autowired
    private DbeAggregatorDao aggregatorDao;

    /**
     * private properties
     */
    private final Long countryId = Long.valueOf(1);

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

        // Validate owner provider
        if (null != appProviderId && !appProviderId.isEmpty()) {
            checkValidAppProvider(aggregatorId, appProviderId);
        }

        List<RSSModel> models = new ArrayList<>();
        List<SetRevenueShareConf> result = revenueShareConfDao.getRevenueModelsByParameters(aggregatorId,
            appProviderId, productClass);

        // convert result to api model.
        if (null != result && !result.isEmpty()) {
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

        // Create new model id
        SetRevenueShareConfId id = new SetRevenueShareConfId();
        id.setTxAppProviderId(rssModel.getOwnerProviderId());
        id.setCountryId(countryId);
        id.setProductClass(rssModel.getProductClass());

        // Create new model
        SetRevenueShareConf model = new SetRevenueShareConf();
        model.setId(id);
        model.setAlgorithmType(rssModel.getAlgorithmType());

        // Create aggregator sharing object
        RevenueShareAggregator aggregatorSharing = new RevenueShareAggregator();

        DbeAggregator aggregator = this.aggregatorDao.getById(aggregatorId);

        aggregatorSharing.setAggregator(aggregator);
        aggregatorSharing.setAggregatorPerc(rssModel.getAggregatorShare());
        model.setShareAggregator(aggregatorSharing);

        // Set provider owner
        DbeAppProvider provider = this.appProviderDao.getById(rssModel.getOwnerProviderId());
        model.setModelOwner(provider);

        // TODO: Set stakeholders

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

        // Check if the list of aggregators is empty
        if (null == provsAgg || provsAgg.isEmpty()) {
            String[] args = {"Non existing: appProviderId"};
            throw new RSSException(UNICAExceptionType.NON_EXISTENT_RESOURCE_ID, args);
        }

        // Check that the given appProvider is included in an aggregator (Store)
        boolean found = false;
        Iterator<DbeAggregatorAppProvider> provsAggIt = provsAgg.iterator();

        while (provsAggIt.hasNext() && !found) {
            DbeAggregatorAppProvider provAgg = provsAggIt.next();
            if (provAgg.getDbeAppProvider().getTxAppProviderId().equalsIgnoreCase(appProviderId)) {
                    found = true;
            }
        }

        if (!found) {
            String[] args = {"Non existing: appProviderId"};
            throw new RSSException(UNICAExceptionType.NON_EXISTENT_RESOURCE_ID, args);
        }
    }

    private void checkField (String field, String name) throws RSSException{
        if (null == field || field.isEmpty()) {
            String[] args = {"Required parameters not found: " + name};
            throw new RSSException(UNICAExceptionType.NON_EXISTENT_RESOURCE_ID, args);
        }
    }

    private void checkNumberField(BigDecimal number) throws RSSException{
        if (null == number) {
            String[] args = {"Required parameters not found: percRevenueShare"};
            throw new RSSException(UNICAExceptionType.NON_EXISTENT_RESOURCE_ID, args);
        } else if (number.compareTo(BigDecimal.ZERO) <= 0) {
            String[] args = {"percentage must be greater than 0"};
            throw new RSSException(UNICAExceptionType.INVALID_INPUT_VALUE, args);
        } else if (number.compareTo(BigDecimal.valueOf(100)) > 0) {
            String[] args = {"percentage must be equal or lower than 100"};
            throw new RSSException(UNICAExceptionType.INVALID_INPUT_VALUE, args);
        }
    }

    /**
     * Check that a RssModels contains all required Information.
     * 
     * @param rssModel
     * @throws Exception
     */
    public void checkValidRSSModel(RSSModel rssModel) throws Exception {
        logger.debug("Into checkValidRSSModel mehtod");

        // Validate basic fields
        this.checkField(rssModel.getAggregatorId(), "aggregatorId");
        this.checkField(rssModel.getOwnerProviderId(), "ownerProviderId");
        // FIXME: Validate the concrete type

        this.checkField(rssModel.getAlgorithmType(), "algorithmType");
        this.checkNumberField(rssModel.getAggregatorShare());
        BigDecimal accumulatedValue = rssModel.getAggregatorShare();

        // Check valid provider owner
        this.checkValidAppProvider(rssModel.getAggregatorId(), rssModel.getOwnerProviderId());

        // Check stakeholders fileds if existing
        if (rssModel.getStakeholders() != null) {
            for (StakeholderModel stModel: rssModel.getStakeholders()) {
                this.checkField(stModel.getStakeholderId(), "stakeholderId");
                this.checkNumberField(stModel.getModelValue());
                accumulatedValue.add(stModel.getModelValue());

                this.checkValidAppProvider(rssModel.getAggregatorId(), stModel.getStakeholderId());

                // Check that the stakeholder is not the owner provider
                if (stModel.getStakeholderId().equalsIgnoreCase(rssModel.getOwnerProviderId())) {
                    String[] args = {"The RS model owner cannot be included as stakeholder"};
                    throw new RSSException(UNICAExceptionType.INVALID_PARAMETER, args);
                }
            }
        }

        // Check that the total percentage is not greater than 100
        if (accumulatedValue.compareTo(BigDecimal.valueOf(100)) > 0) {
            String[] args = {"The RS model owner cannot be included as stakeholder"};
            throw new RSSException(UNICAExceptionType.INVALID_PARAMETER, args);
        }
    }

    /**
     * Converts the model from objects to the bean that is serialized by the API.
     * 
     * @param model
     * @return
     */
    public RSSModel convertIntoApiModel(SetRevenueShareConf model) {
        RSSModel rssModel = new RSSModel();
        // Fill basic revenue sharing model info
        rssModel.setOwnerProviderId(model.getModelOwner().getTxAppProviderId());
        rssModel.setAggregatorId(model.getShareAggregator().getAggregator().getTxEmail());
        rssModel.setAggregatorShare(model.getShareAggregator().getAggregatorPerc());
        rssModel.setAlgorithmType(model.getAlgorithmType());
        rssModel.setProductClass(model.getId().getProductClass());

        // Fill stakeholders list
        List<StakeholderModel> stakeholdersList = new ArrayList<>();

        for (ModelProvider stk: model.getStakeholders()) {
            StakeholderModel stModel = new StakeholderModel();
            stModel.setStakeholderId(stk.getStakeholder().getTxAppProviderId());
            stModel.setModelValue(stk.getModelValue());
        }
        rssModel.setStakeholders(stakeholdersList);
        return rssModel;
    }

}
