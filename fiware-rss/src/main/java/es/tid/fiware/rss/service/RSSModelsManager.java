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

import es.tid.fiware.rss.algorithm.AlgorithmFactory;
import es.tid.fiware.rss.algorithm.AlgorithmProcessor;
import es.tid.fiware.rss.algorithm.Algorithms;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.tid.fiware.rss.dao.DbeAggregatorAppProviderDao;
import es.tid.fiware.rss.dao.DbeAggregatorDao;
import es.tid.fiware.rss.dao.DbeAppProviderDao;
import es.tid.fiware.rss.dao.ModelProviderDao;
import es.tid.fiware.rss.dao.SetRevenueShareConfDao;
import es.tid.fiware.rss.exception.RSSException;
import es.tid.fiware.rss.exception.UNICAExceptionType;
import es.tid.fiware.rss.model.DbeAggregator;
import es.tid.fiware.rss.model.DbeAggregatorAppProvider;
import es.tid.fiware.rss.model.DbeAppProvider;
import es.tid.fiware.rss.model.ModelProvider;
import es.tid.fiware.rss.model.ModelProviderId;
import es.tid.fiware.rss.model.RSSModel;
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

    @Autowired
    private ModelProviderDao modelProviderDao;
    /**
     * private properties
     */
    private final Long countryId = (long) 1;

    /**
     * Retrives a list of revenue sharing models filtered by aggregator, provider
     * and product class
     * 
     * @param aggregatorId, Id of the aggregator
     * @param appProviderId, Id if the provider owener of the revenue sharing models
     * @param productClass, Product class where the models are applied
     * @return
     * @throws RSSException
     */
    public List<RSSModel> getRssModels(String aggregatorId, String appProviderId,
            String productClass) throws RSSException {
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

    private SetRevenueShareConfId buildRSModelId(RSSModel rssModel) {
        // Create new model id
        SetRevenueShareConfId id = new SetRevenueShareConfId();
        id.setTxAppProviderId(rssModel.getOwnerProviderId());
        id.setCountryId(countryId);
        id.setProductClass(rssModel.getProductClass());
        return id;
    }

    private SetRevenueShareConf fillRSModelInfo(RSSModel rssModel,
            SetRevenueShareConf model) {

        model.setAlgorithmType(rssModel.getAlgorithmType());

        // Create aggregator sharing object
        DbeAggregator aggregator = this.aggregatorDao.getById(rssModel.getAggregatorId());

        model.setAggregator(aggregator);
        model.setAggregatorValue(rssModel.getAggregatorValue());

        // Set provider owner
        DbeAppProvider provider = this.appProviderDao.getById(rssModel.getOwnerProviderId());

        model.setModelOwner(provider);
        model.setOwnerValue(rssModel.getOwnerValue());

        // Set stakeholders
        if (rssModel.getStakeholders() != null) {
            Set<ModelProvider> stakeholders = new HashSet<>();

            for (StakeholderModel stakeholderModel: rssModel.getStakeholders()) {
                DbeAppProvider stakeholder = this.appProviderDao.getById(stakeholderModel.getStakeholderId());

                // Build stakeholder id
                ModelProviderId stModelId = new ModelProviderId();
                stModelId.setStakeholder(stakeholder);
                stModelId.setModel(model);

                // Build stakeholder
                ModelProvider stModel = new ModelProvider();
                stModel.setId(stModelId);
                stModel.setModelValue(stakeholderModel.getModelValue());

                // Add stakeholder to the set
                stakeholders.add(stModel);
            }

            model.setStakeholders(stakeholders);
        }
        return model;
    }

    private SetRevenueShareConf buildRSModel(RSSModel rssModel) {

        SetRevenueShareConfId id = this.buildRSModelId(rssModel);

        // Create new model
        SetRevenueShareConf model = new SetRevenueShareConf();
        model.setId(id);

        return this.fillRSModelInfo(rssModel, model);
    }

    /**
     * Creates a new RS Model.
     * 
     * @param rssModel
     * @return
     * @throws RSSException
     */
    public RSSModel createRssModel(RSSModel rssModel) throws RSSException {
        logger.debug("Into createRssModel() method");

        // check valid rssModel
        checkValidRSSModel(rssModel);

        // Build database model for RS Model
        SetRevenueShareConf model = this.buildRSModel(rssModel);
        Set<SetRevenueShareConf> models = model.getModelOwner().getModels();

        if (null == models) {
            models = new HashSet<>();
        }
        models.add(model);

        // Persist models in the database
        // Update provider model
        this.appProviderDao.update(model.getModelOwner());
        // Save new RS model into database
        try {
            this.revenueShareConfDao.create(model);
        } catch (org.hibernate.NonUniqueObjectException e) {
            String[] args = {"A model with the same Product Class already exists"};
            throw new RSSException(UNICAExceptionType.RESOURCE_ALREADY_EXISTS, args);
        }
        // Save model provider relationships for stakeholders
        for(ModelProvider st: model.getStakeholders()) {
            this.modelProviderDao.create(st);
        }
        // return model
        return rssModel;
    }

    /**
     * Update RSS model.
     * 
     * @param rssModel
     * @return
     * @throws Exception
     */
    public RSSModel updateRssModel(RSSModel rssModel) throws Exception {
        logger.debug("Into updateRssModel() method");
        // check valid rssModel
        checkValidRSSModel(rssModel);

        // Get exisintg RS model
        SetRevenueShareConfId id = this.buildRSModelId(rssModel);
        SetRevenueShareConf model = revenueShareConfDao.getById(id);

        // Check if the model does not exists
        if (null == model) {
            String[] args = {"Non existing Rss Model."};
            throw new RSSException(UNICAExceptionType.NON_EXISTENT_RESOURCE_ID, args);
        }

        // Save model into database
        revenueShareConfDao.update(this.fillRSModelInfo(rssModel, model));

        // return model
        return rssModel;
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

        // Get models
        List<SetRevenueShareConf> result = revenueShareConfDao.getRevenueModelsByParameters(aggregatorId,
            appProviderId, productClass);

        // Remeve models
        if (null != result && !result.isEmpty()) {
            for (SetRevenueShareConf model : result) {
                // Remove Stakeholders
                if (null != model.getStakeholders()) {
                    for (ModelProvider st: model.getStakeholders()) {
                        modelProviderDao.delete(st);
                    }
                }
                revenueShareConfDao.delete(model);
            }
        }
    }

    /**
     * Check that a appProviderId is valid for the current aggregator.
     * 
     * @param aggregatorId
     * @param appProviderId
     * @throws RSSException
     */
    public void checkValidAppProvider(String aggregatorId, String appProviderId)
            throws RSSException {
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

    private void checkNumberField(BigDecimal number, String name) throws RSSException{
        if (null == number) {
            String[] args = {"Required parameters not found: " + name};
            throw new RSSException(UNICAExceptionType.NON_EXISTENT_RESOURCE_ID, args);
        }
    }

    /**
     * Check that a RssModels contains all required Information.
     * 
     * @param rssModel
     * @throws RSSException
     */
    public void checkValidRSSModel(RSSModel rssModel) throws RSSException {
        logger.debug("Into checkValidRSSModel mehtod");

        // Validate basic fields
        this.checkField(rssModel.getAggregatorId(), "aggregatorId");
        this.checkField(rssModel.getOwnerProviderId(), "ownerProviderId");

        this.checkNumberField(rssModel.getOwnerValue(), "ownerValue");

        this.checkField(rssModel.getAlgorithmType(), "algorithmType");
        this.checkNumberField(rssModel.getAggregatorValue(), "aggregatorValue");

        this.checkField(rssModel.getProductClass(), "productClass");
        // Check valid provider owner
        this.checkValidAppProvider(rssModel.getAggregatorId(), rssModel.getOwnerProviderId());

        // Check stakeholders fields if existing
        if (rssModel.getStakeholders() != null) {
            for (StakeholderModel stModel: rssModel.getStakeholders()) {
                this.checkField(stModel.getStakeholderId(), "stakeholderId");
                this.checkNumberField(stModel.getModelValue(), "modelValue");

                this.checkValidAppProvider(rssModel.getAggregatorId(), stModel.getStakeholderId());

                // Check that the stakeholder is not the owner provider
                if (stModel.getStakeholderId().equalsIgnoreCase(rssModel.getOwnerProviderId())) {
                    String[] args = {"The RS model owner cannot be included as stakeholder"};
                    throw new RSSException(UNICAExceptionType.INVALID_PARAMETER, args);
                }
            }
        }

        // Check algorithm specific restrictions
        AlgorithmFactory algorithmFactory = new AlgorithmFactory();
        AlgorithmProcessor processor = algorithmFactory.
                getAlgorithmProcessor(rssModel.getAlgorithmType());

        processor.validateModel(rssModel);
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
        rssModel.setOwnerProviderId(
                model.getModelOwner().
                        getTxAppProviderId()
        );
        rssModel.setOwnerValue(model.getOwnerValue());
        rssModel.setAggregatorId(model.getAggregator().getTxEmail());
        rssModel.setAggregatorShare(model.getAggregatorValue());
        rssModel.setAlgorithmType(model.getAlgorithmType());
        rssModel.setProductClass(model.getId().getProductClass());

        // Fill stakeholders list
        List<StakeholderModel> stakeholdersList = new ArrayList<>();

        for (ModelProvider stk: model.getStakeholders()) {
            StakeholderModel stModel = new StakeholderModel();
            stModel.setStakeholderId(stk.getStakeholder().getTxAppProviderId());
            stModel.setModelValue(stk.getModelValue());
            stakeholdersList.add(stModel);
        }
        rssModel.setStakeholders(stakeholdersList);
        return rssModel;
    }
}
