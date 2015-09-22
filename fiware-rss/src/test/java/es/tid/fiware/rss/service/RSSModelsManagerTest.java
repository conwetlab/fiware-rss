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

package es.tid.fiware.rss.service;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import es.tid.fiware.rss.dao.DbeAggregatorDao;
import es.tid.fiware.rss.dao.DbeAppProviderDao;
import es.tid.fiware.rss.dao.ModelProviderDao;
import es.tid.fiware.rss.dao.SetRevenueShareConfDao;
import es.tid.fiware.rss.exception.RSSException;
import es.tid.fiware.rss.model.DbeAggregator;
import es.tid.fiware.rss.model.DbeAppProvider;
import es.tid.fiware.rss.model.DbeAppProviderId;
import es.tid.fiware.rss.model.ModelProvider;
import es.tid.fiware.rss.model.RSSModel;
import es.tid.fiware.rss.model.SetRevenueShareConf;
import es.tid.fiware.rss.model.SetRevenueShareConfId;
import es.tid.fiware.rss.model.StakeholderModel;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import org.junit.Before;
import org.mockito.InjectMocks;
import static org.mockito.Matchers.anyString;
import org.mockito.Mock;
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


    /**
     * Method to insert data before test.
     *
     * @throws Exception
     *             from dbb
     */

    /**
     * @throws Exception
     */

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

    //TODO::
    @Test
    public void convertIntoApiModelTest() {
        String aggregatorId = "aggregator@mail.com";
        String aggregatorName = "aggregatorName";
        String appProviderId = "appProvider@mail.com";
        String algorithmType = "FIXED_PERCENTAGE";
        String stakeholderId = "stakeholderId@mail.com";

        DbeAggregator dbeAggregator = new DbeAggregator(aggregatorName, aggregatorId);

        DbeAppProviderId dbeAppProviderId = new DbeAppProviderId();
        dbeAppProviderId.setTxAppProviderId(appProviderId);

        DbeAppProvider dbeAppProvider = new DbeAppProvider();
        dbeAppProvider.setId(dbeAppProviderId);

        SetRevenueShareConfId setRevenueShareConfId = new SetRevenueShareConfId();
        setRevenueShareConfId.setModelOwner(dbeAppProvider);
        setRevenueShareConfId.setProductClass(algorithmType);

        SetRevenueShareConf setRevenueShareConf = new SetRevenueShareConf();
        setRevenueShareConf.setAggregator(dbeAggregator);
        setRevenueShareConf.setAggregatorValue(BigDecimal.ZERO);
        setRevenueShareConf.setAlgorithmType(algorithmType);
        setRevenueShareConf.setId(setRevenueShareConfId);
        setRevenueShareConf.setOwnerValue(BigDecimal.ZERO);
        setRevenueShareConf.setStakeholders(null);

        Set <ModelProvider> stakeholders = new HashSet<ModelProvider>();
        ModelProvider modelProvider = new ModelProvider();
        modelProvider.setId(null);
        modelProvider.setModel(setRevenueShareConf);
        modelProvider.setModelValue(BigDecimal.ZERO);
        modelProvider.setStakeholder(dbeAppProvider);

        toTest.convertIntoApiModel(setRevenueShareConf);
    }

    @Test
    //TODO:
    public void createRssModelTest() throws RSSException {
        String aggregatorId = "aggregator@mail.com";
        String algorithmType = "FIXED_PERCENTAGE";
        String ownerProviderId = "aggregator@mail.com";
        String productClass = "productClass@mail.com";
        String stakeholderId = "aggregator@mail.com";
        String appProviderId = "provider@mail.com";


        RSSModel rSSModel = new RSSModel();
        rSSModel.setAggregatorId(aggregatorId);
        rSSModel.setAggregatorShare(BigDecimal.valueOf(20));
        rSSModel.setAlgorithmType(algorithmType);
        rSSModel.setOwnerProviderId(ownerProviderId);
        rSSModel.setOwnerValue(BigDecimal.valueOf(40));
        rSSModel.setProductClass(productClass);
        rSSModel.setStakeholders(null);

        toTest.createRssModel(rSSModel);
    }
    @Test
    public void deleteRssModelTest() {

    }
    @Test
    public void existModelTest() {

    }
    @Test
    public void getRssModelsTest() {

    }
    @Test
    public void updateRssModelTest() {

    }
}
