/**
 * Revenue Settlement and Sharing System GE
 * Copyright (C) 2011-2014, Javier Lucio - lucio@tid.es
 * Telefonica Investigacion y Desarrollo, S.A.
 *
 * Copyright (C) 2015, CoNWeT Lab., Universidad Politénica de Madrid
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

package es.upm.fiware.rss.service;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import es.upm.fiware.rss.dao.DbeAggregatorDao;
import es.upm.fiware.rss.dao.DbeAppProviderDao;
import es.upm.fiware.rss.dao.ModelProviderDao;
import es.upm.fiware.rss.dao.SetRevenueShareConfDao;
import es.upm.fiware.rss.exception.RSSException;
import es.upm.fiware.rss.model.DbeAggregator;
import es.upm.fiware.rss.model.DbeAppProvider;
import es.upm.fiware.rss.model.DbeAppProviderId;
import es.upm.fiware.rss.model.ModelProvider;
import es.upm.fiware.rss.model.ModelProviderId;
import es.upm.fiware.rss.model.RSSModel;
import es.upm.fiware.rss.model.SetRevenueShareConf;
import es.upm.fiware.rss.model.SetRevenueShareConfId;
import es.upm.fiware.rss.model.SharingReport;
import es.upm.fiware.rss.model.StakeholderModel;
import java.math.BigDecimal;
import java.util.AbstractSet;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.mockito.InjectMocks;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import org.mockito.Mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

public class RSSModelsManagerTest {
    /***
     * Logging system.
     */
    private final Logger logger = LoggerFactory.getLogger(RSSModelsManagerTest.class);

    @Mock private DbeAppProviderDao appProviderDao;
    @Mock private SetRevenueShareConfDao revenueShareConfDao;
    @Mock private DbeAggregatorDao aggregatorDao;
    @Mock private ModelProviderDao modelProviderDao;
    @InjectMocks private RSSModelsManager toTest;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void checkValidAppProviderTest() throws RSSException {
        String aggregatorId = "aggregator@mail.com";
        String appProviderId = "provider@mail.com";

        DbeAggregator dbeAggregator = new DbeAggregator("aggegatorName", aggregatorId);

        DbeAppProviderId dbeAppProviderId = new DbeAppProviderId();
        dbeAppProviderId.setAggregator(dbeAggregator);
        dbeAppProviderId.setTxAppProviderId(appProviderId);

        DbeAppProvider provModel = new DbeAppProvider();
        provModel.setId(dbeAppProviderId);
        provModel.setModels(null);
        provModel.setTxCorrelationNumber(Integer.MIN_VALUE);
        provModel.setTxName(appProviderId);
        provModel.setTxTimeStamp(new Date());

        when(appProviderDao.getProvider(aggregatorId, appProviderId)).thenReturn(provModel);

        toTest.checkValidAppProvider(aggregatorId, appProviderId);
    }

    @Test
            (expected = RSSException.class)
    public void checkValidAppProviderRSSExceptionNonExistentResourceTest() throws RSSException {
        String aggregatorId = "aggregator@mail.com";
        String appProviderId = "provider@mail.com";

        when(appProviderDao.getProvider(aggregatorId, appProviderId)).thenReturn(null);

        toTest.checkValidAppProvider(aggregatorId, appProviderId);
    }

    @Test
            (expected = RSSException.class)
    public void checkValidAppProviderRSSExceptionInvalidParameterTest() throws RSSException {
        String aggregatorId = "aggregator@mail.com";
        String appProviderId = "provider@mail.com";

        DbeAggregator dbeAggregator = new DbeAggregator("aggegatorName", "other@mail.com");

        DbeAppProviderId dbeAppProviderId = new DbeAppProviderId();
        dbeAppProviderId.setAggregator(dbeAggregator);
        dbeAppProviderId.setTxAppProviderId(appProviderId);

        DbeAppProvider provModel = new DbeAppProvider();
        provModel.setId(dbeAppProviderId);
        provModel.setModels(null);
        provModel.setTxCorrelationNumber(Integer.MIN_VALUE);
        provModel.setTxName(appProviderId);
        provModel.setTxTimeStamp(new Date());

        when(appProviderDao.getProvider(aggregatorId, appProviderId)).thenReturn(provModel);

        toTest.checkValidAppProvider(aggregatorId, appProviderId);
    }

    @Test
    public void checkValidRSSModelTest() throws RSSException {
        String aggregatorId = "aggregator@mail.com";
        String algorithmType = "FIXED_PERCENTAGE";
        String ownerProviderId = "aggregator@mail.com";
        String productClass = "productClass@mail.com";
        String stakeholderId = "stakeholder@mail.com";
        String appProviderId = "provider@mail.com";

        List <StakeholderModel> holdersModel = new LinkedList<>();
        StakeholderModel stakeholderModel = new StakeholderModel();
        stakeholderModel.setModelValue(BigDecimal.valueOf(20));
        stakeholderModel.setStakeholderId(stakeholderId);
        holdersModel.add(stakeholderModel);

        RSSModel rSSModel = new RSSModel();
        rSSModel.setAggregatorId(aggregatorId);
        rSSModel.setAggregatorShare(BigDecimal.valueOf(50));
        rSSModel.setAlgorithmType(algorithmType);
        rSSModel.setOwnerProviderId(ownerProviderId);
        rSSModel.setOwnerValue(BigDecimal.valueOf(30));
        rSSModel.setProductClass(productClass);
        rSSModel.setStakeholders(holdersModel);

        DbeAggregator dbeAggregator = new DbeAggregator("aggegatorName", aggregatorId);

        DbeAppProviderId dbeAppProviderId = new DbeAppProviderId();
        dbeAppProviderId.setAggregator(dbeAggregator);
        dbeAppProviderId.setTxAppProviderId(appProviderId);

        DbeAppProvider provModel = new DbeAppProvider();
        provModel.setId(dbeAppProviderId);
        provModel.setModels(null);
        provModel.setTxCorrelationNumber(Integer.MIN_VALUE);
        provModel.setTxName(appProviderId);
        provModel.setTxTimeStamp(new Date());

        when(appProviderDao.getProvider(anyString(), anyString())).thenReturn(provModel);

        toTest.checkValidRSSModel(rSSModel);
    }

    @Test
    public void checkValidRSSModel2Test() throws RSSException {
        String aggregatorId = "aggregator@mail.com";
        String algorithmType = "FIXED_PERCENTAGE";
        String ownerProviderId = "aggregator@mail.com";
        String productClass = "productClass@mail.com";
        String stakeholderId = "stakeholder@mail.com";
        String appProviderId = "provider@mail.com";

        List <StakeholderModel> holdersModel = null;

        RSSModel rSSModel = new RSSModel();
        rSSModel.setAggregatorId(aggregatorId);
        rSSModel.setAggregatorShare(BigDecimal.valueOf(50));
        rSSModel.setAlgorithmType("FIXED_PERCENTAGE");
        rSSModel.setOwnerProviderId(ownerProviderId);
        rSSModel.setOwnerValue(BigDecimal.valueOf(50));
        rSSModel.setProductClass(productClass);
        rSSModel.setStakeholders(holdersModel);

        DbeAggregator dbeAggregator = new DbeAggregator("aggegatorName", aggregatorId);

        DbeAppProviderId dbeAppProviderId = new DbeAppProviderId();
        dbeAppProviderId.setAggregator(dbeAggregator);
        dbeAppProviderId.setTxAppProviderId(appProviderId);

        DbeAppProvider provModel = new DbeAppProvider();
        provModel.setId(dbeAppProviderId);
        provModel.setModels(null);
        provModel.setTxCorrelationNumber(Integer.MIN_VALUE);
        provModel.setTxName(appProviderId);
        provModel.setTxTimeStamp(new Date());

        when(appProviderDao.getProvider(anyString(), anyString())).thenReturn(provModel);

        toTest.checkValidRSSModel(rSSModel);
    }

    @Test
            (expected = RSSException.class)
    public void checkValidRSSModelRSSExceptionCheckFieldNullTest() throws RSSException {
        String aggregatorId = null;
        String algorithmType = "FIXED_PERCENTAGE";
        String ownerProviderId = "aggregator@mail.com";
        String productClass = "productClass@mail.com";
        String stakeholderId = "stakeholder@mail.com";
        String appProviderId = "provider@mail.com";

        List <StakeholderModel> holdersModel = null;

        RSSModel rSSModel = new RSSModel();
        rSSModel.setAggregatorId(aggregatorId);
        rSSModel.setAggregatorShare(BigDecimal.valueOf(50));
        rSSModel.setAlgorithmType(algorithmType);
        rSSModel.setOwnerProviderId(ownerProviderId);
        rSSModel.setOwnerValue(BigDecimal.valueOf(30));
        rSSModel.setProductClass(productClass);
        rSSModel.setStakeholders(holdersModel);

        DbeAggregator dbeAggregator = new DbeAggregator("aggegatorName", aggregatorId);

        DbeAppProviderId dbeAppProviderId = new DbeAppProviderId();
        dbeAppProviderId.setAggregator(dbeAggregator);
        dbeAppProviderId.setTxAppProviderId(appProviderId);

        DbeAppProvider provModel = new DbeAppProvider();
        provModel.setId(dbeAppProviderId);
        provModel.setModels(null);
        provModel.setTxCorrelationNumber(Integer.MIN_VALUE);
        provModel.setTxName(appProviderId);
        provModel.setTxTimeStamp(new Date());

        when(appProviderDao.getProvider(anyString(), anyString())).thenReturn(provModel);

        toTest.checkValidRSSModel(rSSModel);
    }

    @Test
            (expected = RSSException.class)
    public void checkValidRSSModelRSSExceptionCheckFieldVoidTest() throws RSSException {
        String aggregatorId = "";
        String algorithmType = "FIXED_PERCENTAGE";
        String ownerProviderId = "aggregator@mail.com";
        String productClass = "productClass@mail.com";
        String stakeholderId = "stakeholder@mail.com";
        String appProviderId = "provider@mail.com";

        List <StakeholderModel> holdersModel = null;

        RSSModel rSSModel = new RSSModel();
        rSSModel.setAggregatorId(aggregatorId);
        rSSModel.setAggregatorShare(BigDecimal.valueOf(50));
        rSSModel.setAlgorithmType(algorithmType);
        rSSModel.setOwnerProviderId(ownerProviderId);
        rSSModel.setOwnerValue(BigDecimal.valueOf(30));
        rSSModel.setProductClass(productClass);
        rSSModel.setStakeholders(holdersModel);

        DbeAggregator dbeAggregator = new DbeAggregator("aggegatorName", aggregatorId);

        DbeAppProviderId dbeAppProviderId = new DbeAppProviderId();
        dbeAppProviderId.setAggregator(dbeAggregator);
        dbeAppProviderId.setTxAppProviderId(appProviderId);

        DbeAppProvider provModel = new DbeAppProvider();
        provModel.setId(dbeAppProviderId);
        provModel.setModels(null);
        provModel.setTxCorrelationNumber(Integer.MIN_VALUE);
        provModel.setTxName(appProviderId);
        provModel.setTxTimeStamp(new Date());

        when(appProviderDao.getProvider(anyString(), anyString())).thenReturn(provModel);

        toTest.checkValidRSSModel(rSSModel);
    }

    @Test
            (expected = RSSException.class)
    public void checkValidRSSModelRSSExceptionCheckNumberFieldTest() throws RSSException {
        String aggregatorId = "aggregator@mail.com";
        String algorithmType = "FIXED_PERCENTAGE";
        String ownerProviderId = "aggregator@mail.com";
        String productClass = "productClass@mail.com";
        String stakeholderId = "stakeholder@mail.com";
        String appProviderId = "provider@mail.com";

        List <StakeholderModel> holdersModel = null;

        RSSModel rSSModel = new RSSModel();
        rSSModel.setAggregatorId(aggregatorId);
        rSSModel.setAggregatorShare(BigDecimal.valueOf(50));
        rSSModel.setAlgorithmType(algorithmType);
        rSSModel.setOwnerProviderId(ownerProviderId);
        rSSModel.setOwnerValue(null);
        rSSModel.setProductClass(productClass);
        rSSModel.setStakeholders(holdersModel);

        DbeAggregator dbeAggregator = new DbeAggregator("aggegatorName", aggregatorId);

        DbeAppProviderId dbeAppProviderId = new DbeAppProviderId();
        dbeAppProviderId.setAggregator(dbeAggregator);
        dbeAppProviderId.setTxAppProviderId(appProviderId);

        DbeAppProvider provModel = new DbeAppProvider();
        provModel.setId(dbeAppProviderId);
        provModel.setModels(null);
        provModel.setTxCorrelationNumber(Integer.MIN_VALUE);
        provModel.setTxName(appProviderId);
        provModel.setTxTimeStamp(new Date());

        when(appProviderDao.getProvider(anyString(), anyString())).thenReturn(provModel);

        toTest.checkValidRSSModel(rSSModel);
    }

    @Test
            (expected = RSSException.class)
    public void checkValidRSSModelRSSExceptionInvalidParameterTest() throws RSSException {
        String aggregatorId = "aggregator@mail.com";
        String algorithmType = "FIXED_PERCENTAGE";
        String ownerProviderId = "aggregator@mail.com";
        String productClass = "productClass@mail.com";
        String stakeholderId = "aggregator@mail.com";
        String appProviderId = "provider@mail.com";

        List <StakeholderModel> holdersModel = new LinkedList<>();
        StakeholderModel stakeholderModel = new StakeholderModel();
        stakeholderModel.setModelValue(BigDecimal.valueOf(20));
        stakeholderModel.setStakeholderId(stakeholderId);
        holdersModel.add(stakeholderModel);

        RSSModel rSSModel = new RSSModel();
        rSSModel.setAggregatorId(aggregatorId);
        rSSModel.setAggregatorShare(BigDecimal.valueOf(50));
        rSSModel.setAlgorithmType(algorithmType);
        rSSModel.setOwnerProviderId(ownerProviderId);
        rSSModel.setOwnerValue(BigDecimal.valueOf(30));
        rSSModel.setProductClass(productClass);
        rSSModel.setStakeholders(holdersModel);

        DbeAggregator dbeAggregator = new DbeAggregator("aggegatorName", aggregatorId);

        DbeAppProviderId dbeAppProviderId = new DbeAppProviderId();
        dbeAppProviderId.setAggregator(dbeAggregator);
        dbeAppProviderId.setTxAppProviderId(appProviderId);

        DbeAppProvider provModel = new DbeAppProvider();
        provModel.setId(dbeAppProviderId);
        provModel.setModels(null);
        provModel.setTxCorrelationNumber(Integer.MIN_VALUE);
        provModel.setTxName(appProviderId);
        provModel.setTxTimeStamp(new Date());

        when(appProviderDao.getProvider(anyString(), anyString())).thenReturn(provModel);

        toTest.checkValidRSSModel(rSSModel);
    }


    @Test
    public void convertIntoApiModelTest() {
        String aggregatorId = "aggregator@mail.com";
        String aggregatorName = "aggregatorName";
        String appProviderId = "appProvider@mail.com";
        String algorithmType = "FIXED_PERCENTAGE";
        String stakeholderId = "stakeholderId@mail.com";

        DbeAggregator dbeAggregator = new DbeAggregator(aggregatorName, aggregatorId);
        DbeAppProviderId dbeAppProviderId = new DbeAppProviderId();
        DbeAppProvider dbeAppProvider = new DbeAppProvider();
        SetRevenueShareConfId setRevenueShareConfId = new SetRevenueShareConfId();
        Set <ModelProvider> stakeholders = new HashSet<ModelProvider>();
        SetRevenueShareConf setRevenueShareConf = new SetRevenueShareConf();
        ModelProviderId modelProviderId = new ModelProviderId();
        ModelProvider modelProvider = new ModelProvider();

        dbeAppProviderId.setTxAppProviderId(appProviderId);

        dbeAppProvider.setId(dbeAppProviderId);

        setRevenueShareConfId.setModelOwner(dbeAppProvider);
        setRevenueShareConfId.setProductClass(algorithmType);

        stakeholders.add(modelProvider);

        setRevenueShareConf.setAggregator(dbeAggregator);
        setRevenueShareConf.setAggregatorValue(BigDecimal.ZERO);
        setRevenueShareConf.setAlgorithmType(algorithmType);
        setRevenueShareConf.setId(setRevenueShareConfId);
        setRevenueShareConf.setOwnerValue(BigDecimal.ZERO);
        setRevenueShareConf.setStakeholders(stakeholders);

        modelProvider.setId(modelProviderId);
        modelProvider.setModel(setRevenueShareConf);
        modelProvider.setModelValue(BigDecimal.ZERO);
        modelProvider.setStakeholder(dbeAppProvider);

        toTest.convertIntoApiModel(setRevenueShareConf);
    }

    @Test
    public void createRssModelTest() throws RSSException {
        String aggregatorId = "aggregator@mail.com";
        String algorithmType = "FIXED_PERCENTAGE";
        String ownerProviderId = "aggregator@mail.com";
        String productClass = "productClass@mail.com";
        String stakeholderId = "stakeholder@mail.com";
        String appProviderId = "provider@mail.com";

        List <StakeholderModel> holdersModel = new LinkedList<>();
        StakeholderModel stakeholderModel = new StakeholderModel();
        stakeholderModel.setModelValue(BigDecimal.valueOf(20));
        stakeholderModel.setStakeholderId(stakeholderId);
        holdersModel.add(stakeholderModel);

        RSSModel rSSModel = new RSSModel();
        rSSModel.setAggregatorId(aggregatorId);
        rSSModel.setAggregatorShare(BigDecimal.valueOf(50));
        rSSModel.setAlgorithmType(algorithmType);
        rSSModel.setOwnerProviderId(ownerProviderId);
        rSSModel.setOwnerValue(BigDecimal.valueOf(30));
        rSSModel.setProductClass(productClass);
        rSSModel.setStakeholders(holdersModel);

        DbeAggregator dbeAggregator = new DbeAggregator("aggegatorName", aggregatorId);

        DbeAppProviderId dbeAppProviderId = new DbeAppProviderId();
        dbeAppProviderId.setAggregator(dbeAggregator);
        dbeAppProviderId.setTxAppProviderId(appProviderId);

        DbeAppProvider provModel = new DbeAppProvider();
        provModel.setId(dbeAppProviderId);
        provModel.setModels(null);
        provModel.setTxCorrelationNumber(Integer.MIN_VALUE);
        provModel.setTxName(appProviderId);
        provModel.setTxTimeStamp(new Date());

        when(appProviderDao.getProvider(anyString(), anyString())).thenReturn(provModel);

        toTest.createRssModel(rSSModel);
    }

    @Test
            (expected = RSSException.class)
    public void createRssModelRSSExceptionTest() throws RSSException {
        String aggregatorId = "aggregator@mail.com";
        String algorithmType = "FIXED_PERCENTAGE";
        String ownerProviderId = "aggregator@mail.com";
        String productClass = "productClass@mail.com";
        String stakeholderId = "stakeholder@mail.com";
        String appProviderId = "provider@mail.com";

        List <StakeholderModel> holdersModel = new LinkedList<>();
        StakeholderModel stakeholderModel = new StakeholderModel();
        stakeholderModel.setModelValue(BigDecimal.valueOf(20));
        stakeholderModel.setStakeholderId(stakeholderId);
        holdersModel.add(stakeholderModel);

        RSSModel rSSModel = new RSSModel();
        rSSModel.setAggregatorId(aggregatorId);
        rSSModel.setAggregatorShare(BigDecimal.valueOf(50));
        rSSModel.setAlgorithmType(algorithmType);
        rSSModel.setOwnerProviderId(ownerProviderId);
        rSSModel.setOwnerValue(BigDecimal.valueOf(30));
        rSSModel.setProductClass(productClass);
        rSSModel.setStakeholders(holdersModel);

        DbeAggregator dbeAggregator = new DbeAggregator("aggegatorName", aggregatorId);

        DbeAppProviderId dbeAppProviderId = new DbeAppProviderId();
        dbeAppProviderId.setAggregator(dbeAggregator);
        dbeAppProviderId.setTxAppProviderId(appProviderId);

        DbeAppProvider provModel = new DbeAppProvider();
        provModel.setId(dbeAppProviderId);
        provModel.setModels(null);
        provModel.setTxCorrelationNumber(Integer.MIN_VALUE);
        provModel.setTxName(appProviderId);
        provModel.setTxTimeStamp(new Date());

        when(appProviderDao.getProvider(anyString(), anyString())).thenReturn(provModel);
        doThrow(org.hibernate.NonUniqueObjectException.class).when(revenueShareConfDao).create(any(SetRevenueShareConf.class));

        toTest.createRssModel(rSSModel);
    }


    @Test
    public void deleteRssModelTest() throws Exception {
        String aggregatorId = "aggregator@mail.com";
        String algorithmType = "FIXED_PERCENTAGE";
        String ownerProviderId = "aggregator@mail.com";
        String productClass = "productClass@mail.com";
        String stakeholderId = "stakeholder@mail.com";
        String appProviderId = "provider@mail.com";

        List <StakeholderModel> holdersModel = new LinkedList<>();
        StakeholderModel stakeholderModel = new StakeholderModel();
        stakeholderModel.setModelValue(BigDecimal.valueOf(20));
        stakeholderModel.setStakeholderId(stakeholderId);
        holdersModel.add(stakeholderModel);

        RSSModel rSSModel = new RSSModel();
        rSSModel.setAggregatorId(aggregatorId);
        rSSModel.setAggregatorShare(BigDecimal.valueOf(50));
        rSSModel.setAlgorithmType(algorithmType);
        rSSModel.setOwnerProviderId(ownerProviderId);
        rSSModel.setOwnerValue(BigDecimal.valueOf(30));
        rSSModel.setProductClass(productClass);
        rSSModel.setStakeholders(holdersModel);

        DbeAggregator dbeAggregator = new DbeAggregator("aggegatorName", aggregatorId);

        DbeAppProviderId dbeAppProviderId = new DbeAppProviderId();
        dbeAppProviderId.setAggregator(dbeAggregator);
        dbeAppProviderId.setTxAppProviderId(appProviderId);

        DbeAppProvider provModel = new DbeAppProvider();
        provModel.setId(dbeAppProviderId);
        provModel.setModels(null);
        provModel.setTxCorrelationNumber(Integer.MIN_VALUE);
        provModel.setTxName(appProviderId);
        provModel.setTxTimeStamp(new Date());

        List<SetRevenueShareConf> revenueShareConfs = new LinkedList<>();

        Set<ModelProvider> stakeholders = new HashSet<>();
        ModelProvider modelProvider = new ModelProvider();
        stakeholders.add(modelProvider);

        SetRevenueShareConf revenueShareConf = new SetRevenueShareConf();
        revenueShareConf.setAggregator(dbeAggregator);
        revenueShareConf.setAggregatorValue(BigDecimal.ZERO);
        revenueShareConf.setAlgorithmType(algorithmType);
        revenueShareConf.setId(null);
        revenueShareConf.setOwnerValue(BigDecimal.ZERO);
        revenueShareConf.setStakeholders(stakeholders);

        when(appProviderDao.getProvider(anyString(), anyString())).thenReturn(provModel);
        when(revenueShareConfDao.getRevenueModelsByParameters(aggregatorId,
                appProviderId, productClass)).thenReturn(revenueShareConfs);

        toTest.deleteRssModel(aggregatorId, appProviderId, productClass);
    }

    @Test
    public void deleteRssModel2Test() throws Exception {
        String aggregatorId = "aggregator@mail.com";
        String algorithmType = "FIXED_PERCENTAGE";
        String ownerProviderId = "aggregator@mail.com";
        String productClass = "productClass@mail.com";
        String stakeholderId = "stakeholder@mail.com";
        String appProviderId = "provider@mail.com";

        List <StakeholderModel> holdersModel = new LinkedList<>();
        StakeholderModel stakeholderModel = new StakeholderModel();
        stakeholderModel.setModelValue(BigDecimal.valueOf(20));
        stakeholderModel.setStakeholderId(stakeholderId);
        holdersModel.add(stakeholderModel);

        RSSModel rSSModel = new RSSModel();
        rSSModel.setAggregatorId(aggregatorId);
        rSSModel.setAggregatorShare(BigDecimal.valueOf(50));
        rSSModel.setAlgorithmType(algorithmType);
        rSSModel.setOwnerProviderId(ownerProviderId);
        rSSModel.setOwnerValue(BigDecimal.valueOf(30));
        rSSModel.setProductClass(productClass);
        rSSModel.setStakeholders(holdersModel);

        DbeAggregator dbeAggregator = new DbeAggregator("aggegatorName", aggregatorId);

        DbeAppProviderId dbeAppProviderId = new DbeAppProviderId();
        dbeAppProviderId.setAggregator(dbeAggregator);
        dbeAppProviderId.setTxAppProviderId(appProviderId);

        DbeAppProvider provModel = new DbeAppProvider();
        provModel.setId(dbeAppProviderId);
        provModel.setModels(null);
        provModel.setTxCorrelationNumber(Integer.MIN_VALUE);
        provModel.setTxName(appProviderId);
        provModel.setTxTimeStamp(new Date());

        Set<ModelProvider> stakeholders = new HashSet<>();

        SetRevenueShareConf revenueShareConf = new SetRevenueShareConf();
        revenueShareConf.setAggregator(dbeAggregator);
        revenueShareConf.setAggregatorValue(BigDecimal.ZERO);
        revenueShareConf.setAlgorithmType(algorithmType);
        revenueShareConf.setId(null);
        revenueShareConf.setOwnerValue(BigDecimal.ZERO);
        revenueShareConf.setStakeholders(stakeholders);

        when(appProviderDao.getProvider(anyString(), anyString())).thenReturn(provModel);
        when(revenueShareConfDao.getRevenueModelsByParameters(aggregatorId,
                appProviderId, productClass)).thenReturn(null);

        toTest.deleteRssModel(aggregatorId, appProviderId, productClass);
    }

    @Test
            (expected = RSSException.class)
    public void deleteRssModelRSSExceptionAggregatorNullTest() throws Exception {
        String aggregatorId = null;
        String productClass = "productClass";
        String appProviderId = "provider@mail.com";

        toTest.deleteRssModel(aggregatorId, appProviderId, productClass);
    }

    @Test
            (expected = RSSException.class)
    public void deleteRssModelRSSExceptionAggregatorEmptyTest() throws Exception {
        String aggregatorId = "";
        String productClass = "productClass@mail.com";
        String appProviderId = "provider@mail.com";

        toTest.deleteRssModel(aggregatorId, appProviderId, productClass);
    }

    @Test
    public void existModelTrueTest() {
        String aggregatorId = "aggregatorId@mail.com";
        String productClass = "productClass@mail.com";
        String appProviderId = "provider@mail.com";

        List <SetRevenueShareConf> revenueShareConfs = mock(LinkedList.class);

        when(revenueShareConfs.isEmpty()).thenReturn(false);

        when(revenueShareConfDao.getRevenueModelsByParameters(aggregatorId,
                appProviderId, productClass)).thenReturn(revenueShareConfs);

        boolean returned = toTest.existModel(aggregatorId, appProviderId, productClass);

        Assert.assertTrue(returned);
    }

    @Test
    public void existModelFalse1Test() {
        String aggregatorId = "aggregatorId@mail.com";
        String productClass = "productClass@mail.com";
        String appProviderId = "provider@mail.com";

        List <SetRevenueShareConf> revenueShareConfs = mock(LinkedList.class);

        when(revenueShareConfs.isEmpty()).thenReturn(true);

        when(revenueShareConfDao.getRevenueModelsByParameters(aggregatorId,
                appProviderId, productClass)).thenReturn(revenueShareConfs);

        boolean returned = toTest.existModel(aggregatorId, appProviderId, productClass);

        Assert.assertFalse(returned);
    }

    @Test
    public void existModelFalse2Test() {
        String aggregatorId = "aggregatorId@mail.com";
        String productClass = "productClass@mail.com";
        String appProviderId = "provider@mail.com";

        when(revenueShareConfDao.getRevenueModelsByParameters(aggregatorId,
                appProviderId, productClass)).thenReturn(null);

        boolean returned = toTest.existModel(aggregatorId, appProviderId, productClass);

        Assert.assertFalse(returned);
    }

    @Test
    public void getRssModelsTest() throws RSSException {
        String aggregatorId = "aggregator@mail.com";
        String appProviderId = "appProvider@mail.com";
        String productClass = "productClass";

        DbeAppProvider dbeAppProvider = new DbeAppProvider();
        DbeAppProviderId dbeAppProviderId = new DbeAppProviderId();
        DbeAggregator dbeAggregator = new DbeAggregator(aggregatorId, aggregatorId);
        List <SetRevenueShareConf> revenueShareConfs = new LinkedList<>();
        SetRevenueShareConf revenueShareConf = new SetRevenueShareConf();
        SetRevenueShareConfId revenueShareConfId = new SetRevenueShareConfId();
        Set <ModelProvider> modelProviders = new HashSet<>();
        ModelProvider modelProvider = new ModelProvider();
        ModelProviderId modelProviderId = new ModelProviderId();

        dbeAppProvider.setId(dbeAppProviderId);
        dbeAppProviderId.setAggregator(dbeAggregator);
        revenueShareConfs.add(revenueShareConf);
        revenueShareConf.setAggregator(dbeAggregator);
        revenueShareConf.setAggregatorValue(BigDecimal.valueOf(50));
        revenueShareConf.setAlgorithmType("");
        revenueShareConf.setId(revenueShareConfId);
        revenueShareConf.setOwnerValue(BigDecimal.valueOf(40));
        revenueShareConf.setStakeholders(modelProviders);
        revenueShareConfId.setModelOwner(dbeAppProvider);
        revenueShareConfId.setProductClass(productClass);
        modelProviders.add(modelProvider);
        modelProvider.setId(modelProviderId);
        modelProvider.setModel(revenueShareConf);
        modelProvider.setModelValue(BigDecimal.valueOf(10));
        modelProvider.setStakeholder(dbeAppProvider);
        modelProviderId.setModel(revenueShareConf);
        modelProviderId.setStakeholder(dbeAppProvider);


        when(appProviderDao.getProvider(aggregatorId, appProviderId)).thenReturn(dbeAppProvider);
        when(revenueShareConfDao.getRevenueModelsByParameters(aggregatorId,
            appProviderId, productClass)).thenReturn(revenueShareConfs);

        toTest.getRssModels(aggregatorId, appProviderId, productClass);
    }

    @Test
    public void getRssModelsNullArgumentsTest() throws RSSException {
        String aggregatorId = "aggregator@mail.com";
        String appProviderId = "appProvider@mail.com";
        String productClass = "productClass";

        DbeAppProvider dbeAppProvider = new DbeAppProvider();
        DbeAppProviderId dbeAppProviderId = new DbeAppProviderId();
        DbeAggregator dbeAggregator = new DbeAggregator(aggregatorId, aggregatorId);
        List <SetRevenueShareConf> revenueShareConfs = new LinkedList<>();
        SetRevenueShareConf revenueShareConf = new SetRevenueShareConf();
        SetRevenueShareConfId revenueShareConfId = new SetRevenueShareConfId();
        Set <ModelProvider> modelProviders = new HashSet<>();
        ModelProvider modelProvider = new ModelProvider();
        ModelProviderId modelProviderId = new ModelProviderId();

        dbeAppProvider.setId(dbeAppProviderId);
        dbeAppProviderId.setAggregator(dbeAggregator);
        revenueShareConfs.add(revenueShareConf);
        revenueShareConf.setAggregator(dbeAggregator);
        revenueShareConf.setAggregatorValue(BigDecimal.valueOf(50));
        revenueShareConf.setAlgorithmType("");
        revenueShareConf.setId(revenueShareConfId);
        revenueShareConf.setOwnerValue(BigDecimal.valueOf(40));
        revenueShareConf.setStakeholders(modelProviders);
        revenueShareConfId.setModelOwner(dbeAppProvider);
        revenueShareConfId.setProductClass(productClass);
        modelProviders.add(modelProvider);
        modelProvider.setId(modelProviderId);
        modelProvider.setModel(revenueShareConf);
        modelProvider.setModelValue(BigDecimal.valueOf(10));
        modelProvider.setStakeholder(dbeAppProvider);
        modelProviderId.setModel(revenueShareConf);
        modelProviderId.setStakeholder(dbeAppProvider);


        when(appProviderDao.getProvider(aggregatorId, appProviderId)).thenReturn(dbeAppProvider);
        when(revenueShareConfDao.getRevenueModelsByParameters(aggregatorId,
            appProviderId, productClass)).thenReturn(revenueShareConfs);

        toTest.getRssModels(aggregatorId, null, productClass);
    }

    @Test
    public void updateRssModelTest() throws Exception {
        String aggregatorId = "aggregator@mail.com";
        String productClass = "productClass";
        String algorithmType = "FIXED_PERCENTAGE";
        String ownerProvider = "provider@mail.com";
        String appProviderId = "appProvider@mail.com";

        RSSModel rSSModel = new RSSModel();
        List <StakeholderModel> stakeholderModels = new LinkedList<>();
        StakeholderModel stakeholderModel = new StakeholderModel();
        DbeAppProvider dbeAppProvider = new DbeAppProvider();
        DbeAppProviderId dbeAppProviderId = new DbeAppProviderId();
        DbeAggregator dbeAggregator = new DbeAggregator();
        Set <SetRevenueShareConf> revenueShareConfs = new HashSet<>();
        SetRevenueShareConf revenueShareConf = new SetRevenueShareConf();
        Set <SharingReport> sharingReports = new HashSet<>();
        SharingReport sharingReport = new SharingReport();

        rSSModel.setAggregatorId(aggregatorId);
        rSSModel.setAggregatorShare(BigDecimal.valueOf(50));
        rSSModel.setAlgorithmType(algorithmType);
        rSSModel.setOwnerProviderId(ownerProvider);
        rSSModel.setOwnerValue(BigDecimal.valueOf(40));
        rSSModel.setProductClass(productClass);
        rSSModel.setStakeholders(stakeholderModels);
        stakeholderModels.add(stakeholderModel);
        stakeholderModel.setModelValue(BigDecimal.valueOf(10));
        stakeholderModel.setStakeholderId(aggregatorId);
        dbeAppProvider.setId(dbeAppProviderId);
        dbeAppProvider.setModels(revenueShareConfs);
        dbeAppProvider.setReports(null);
        dbeAppProvider.setTxCorrelationNumber(Integer.MIN_VALUE);
        dbeAppProvider.setTxName(productClass);
        dbeAppProvider.setTxTimeStamp(null);
        dbeAppProviderId.setAggregator(dbeAggregator);
        dbeAppProviderId.setTxAppProviderId(ownerProvider);
        dbeAggregator.setTxEmail(aggregatorId);
        dbeAggregator.setTxName(aggregatorId);
        revenueShareConfs.add(revenueShareConf);
        revenueShareConf.setAggregator(dbeAggregator);
        revenueShareConf.setAggregatorValue(BigDecimal.ZERO);
        revenueShareConf.setAlgorithmType(algorithmType);
        revenueShareConf.setId(null);
        revenueShareConf.setOwnerValue(BigDecimal.ZERO);
        revenueShareConf.setStakeholders(null);

        sharingReports.add(sharingReport);


        when(appProviderDao.getProvider(anyString(), anyString()))
                .thenReturn(dbeAppProvider);
        when(revenueShareConfDao.getById(any(SetRevenueShareConfId.class))).thenReturn(revenueShareConf);

        toTest.updateRssModel(rSSModel);
    }

    @Test
    (expected = RSSException.class)
    public void updateRssModelRSSExceptionTest() throws Exception {
        String aggregatorId = "aggregator@mail.com";
        String productClass = "productClass";
        String algorithmType = "FIXED_PERCENTAGE";
        String ownerProvider = "provider@mail.com";
        String appProviderId = "appProvider@mail.com";

        RSSModel rSSModel = new RSSModel();
        List <StakeholderModel> stakeholderModels = new LinkedList<>();
        StakeholderModel stakeholderModel = new StakeholderModel();
        DbeAppProvider dbeAppProvider = new DbeAppProvider();
        DbeAppProviderId dbeAppProviderId = new DbeAppProviderId();
        DbeAggregator dbeAggregator = new DbeAggregator();
        Set <SetRevenueShareConf> revenueShareConfs = new HashSet<>();
        SetRevenueShareConf revenueShareConf = new SetRevenueShareConf();
        Set <SharingReport> sharingReports = new HashSet<>();
        SharingReport sharingReport = new SharingReport();

        rSSModel.setAggregatorId(aggregatorId);
        rSSModel.setAggregatorShare(BigDecimal.valueOf(50));
        rSSModel.setAlgorithmType(algorithmType);
        rSSModel.setOwnerProviderId(ownerProvider);
        rSSModel.setOwnerValue(BigDecimal.valueOf(40));
        rSSModel.setProductClass(productClass);
        rSSModel.setStakeholders(stakeholderModels);
        stakeholderModels.add(stakeholderModel);
        stakeholderModel.setModelValue(BigDecimal.valueOf(10));
        stakeholderModel.setStakeholderId(aggregatorId);
        dbeAppProvider.setId(dbeAppProviderId);
        dbeAppProvider.setModels(revenueShareConfs);
        dbeAppProvider.setReports(null);
        dbeAppProvider.setTxCorrelationNumber(Integer.MIN_VALUE);
        dbeAppProvider.setTxName(productClass);
        dbeAppProvider.setTxTimeStamp(null);
        dbeAppProviderId.setAggregator(dbeAggregator);
        dbeAppProviderId.setTxAppProviderId(ownerProvider);
        dbeAggregator.setTxEmail(aggregatorId);
        dbeAggregator.setTxName(aggregatorId);
        revenueShareConfs.add(revenueShareConf);
        revenueShareConf.setAggregator(dbeAggregator);
        revenueShareConf.setAggregatorValue(BigDecimal.ZERO);
        revenueShareConf.setAlgorithmType(algorithmType);
        revenueShareConf.setId(null);
        revenueShareConf.setOwnerValue(BigDecimal.ZERO);
        revenueShareConf.setStakeholders(null);

        sharingReports.add(sharingReport);


        when(appProviderDao.getProvider(anyString(), anyString()))
                .thenReturn(dbeAppProvider);
        when(revenueShareConfDao.getById(any(SetRevenueShareConfId.class))).thenReturn(null);

        toTest.updateRssModel(rSSModel);
    }
}