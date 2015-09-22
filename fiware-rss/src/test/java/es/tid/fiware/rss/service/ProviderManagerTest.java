package es.tid.fiware.rss.service;

/**
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

import es.tid.fiware.rss.dao.DbeAggregatorDao;
import es.tid.fiware.rss.dao.DbeAppProviderDao;
import es.tid.fiware.rss.exception.RSSException;
import es.tid.fiware.rss.model.DbeAggregator;
import es.tid.fiware.rss.model.DbeAppProvider;
import es.tid.fiware.rss.model.DbeAppProviderId;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author francisco
 */

public class ProviderManagerTest {

    private final Logger logger = LoggerFactory.getLogger(ProviderManagerTest.class);

    @Mock private DbeAppProviderDao appProviderDao;
    @Mock private DbeAggregatorDao aggregatorDao;
    @Mock private RSSModelsManager modelsManager;
    @InjectMocks private ProviderManager toTest;

    public ProviderManagerTest() {}

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void createProviderTest() throws RSSException {
        String providerId = "provider@mail.com";
        String providerName = "providerName";
        String aggregatorId = "aggregator@mail.com";

        DbeAggregator aggregator = new DbeAggregator("aggregatorName", aggregatorId);

        when(aggregatorDao.getById(aggregatorId)).thenReturn(aggregator);

        toTest.createProvider(providerId, providerName, aggregatorId);

    }

    @Test
    (expected = RSSException.class)
    public void createProviderRSSExceptionMissingParameter1NullTest() throws RSSException {
        String providerId = null;
        String providerName = "providerName";
        String aggregatorId = "aggregator@mail.com";

        DbeAggregator aggregator = new DbeAggregator("aggregatorName", aggregatorId);

        when(aggregatorDao.getById(aggregatorId)).thenReturn(aggregator);

        toTest.createProvider(providerId, providerName, aggregatorId);
    }

    @Test
    (expected = RSSException.class)
    public void createProviderRSSExceptionMissingParameter1VoidTest() throws RSSException {
        String providerId = "";
        String providerName = "providerName";
        String aggregatorId = "aggregator@mail.com";

        DbeAggregator aggregator = new DbeAggregator("aggregatorName", aggregatorId);

        when(aggregatorDao.getById(aggregatorId)).thenReturn(aggregator);

        toTest.createProvider(providerId, providerName, aggregatorId);
    }

    @Test
    (expected = RSSException.class)
    public void createProviderRSSExceptionMissingParameter2NullTest() throws RSSException {
        String providerId = "provider@mail.com";
        String providerName = null;
        String aggregatorId = "aggregator@mail.com";

        DbeAggregator aggregator = new DbeAggregator("aggregatorName", aggregatorId);

        when(aggregatorDao.getById(aggregatorId)).thenReturn(aggregator);

        toTest.createProvider(providerId, providerName, aggregatorId);
    }

    @Test
    (expected = RSSException.class)
    public void createProviderRSSExceptionMissingParameter2VoidTest() throws RSSException {
        String providerId = "provider@mail.com";
        String providerName = "";
        String aggregatorId = "aggregator@mail.com";

        DbeAggregator aggregator = new DbeAggregator("aggregatorName", aggregatorId);

        when(aggregatorDao.getById(aggregatorId)).thenReturn(aggregator);

        toTest.createProvider(providerId, providerName, aggregatorId);
    }

    @Test
    (expected = RSSException.class)
    public void createProviderRSSExceptionMissingParameter3NullTest() throws RSSException {
        String providerId = "provider@mail.com";
        String providerName = "providerName";
        String aggregatorId = null;

        DbeAggregator aggregator = new DbeAggregator("aggregatorName", aggregatorId);

        when(aggregatorDao.getById(aggregatorId)).thenReturn(aggregator);

        toTest.createProvider(providerId, providerName, aggregatorId);
    }

    @Test
    (expected = RSSException.class)
    public void createProviderRSSExceptionMissingParameter3VoidTest() throws RSSException {
        String providerId = "provider@mail.com";
        String providerName = "providerName";
        String aggregatorId = "";

        DbeAggregator aggregator = new DbeAggregator("aggregatorName", aggregatorId);

        when(aggregatorDao.getById(aggregatorId)).thenReturn(aggregator);

        toTest.createProvider(providerId, providerName, aggregatorId);
    }

    @Test
    (expected = RSSException.class)
    public void createProviderRSSExceptionNotExistentResourceTest() throws RSSException {
        String providerId = "provider@mail.com";
        String providerName = "providerName";
        String aggregatorId = "aggregator@mail.com";

        when(aggregatorDao.getById(aggregatorId)).thenReturn(null);

        toTest.createProvider(providerId, providerName, aggregatorId);
    }

    @Test
    (expected = RSSException.class)
    public void createProviderRSSExceptionResourceAlreadyExistsTest() throws RSSException {
        String providerId = "provider@mail.com";
        String providerName = "providerName";
        String aggregatorId = "aggregator@mail.com";

        DbeAggregator aggregator = new DbeAggregator("aggregatorName", aggregatorId);

        when(aggregatorDao.getById(aggregatorId)).thenReturn(aggregator);
        doThrow(org.hibernate.NonUniqueObjectException.class).when(appProviderDao).create(isA(DbeAppProvider.class));

        toTest.createProvider(providerId, providerName, aggregatorId);
    }

    @Test
    public void getAPIProvidersTest() throws RSSException {
        String aggregatorId = "aggregator@mail.com";
        String providerId = "provider@mail.com";

        DbeAggregator dbeAggregator = new DbeAggregator("aggegatorName", aggregatorId);

        DbeAppProviderId dbeAppProviderId = new DbeAppProviderId();
        dbeAppProviderId.setAggregator(dbeAggregator);
        dbeAppProviderId.setTxAppProviderId(providerId);

        DbeAppProvider provModel = new DbeAppProvider();
        provModel.setId(dbeAppProviderId);
        provModel.setModels(null);
        provModel.setTxCorrelationNumber(Integer.MIN_VALUE);
        provModel.setTxName(providerId);
        provModel.setTxTimeStamp(new Date());

        List <DbeAppProvider> providers = new LinkedList<>();
        providers.add(provModel);

        when(appProviderDao.getProvidersByAggregator(aggregatorId)).thenReturn(providers);

        toTest.getAPIProviders(aggregatorId);
    }

    @Test
    public void getProvidersTest() throws RSSException {
        String aggregatorId = "aggregator@mail.com";

        List <DbeAppProvider> providers = new LinkedList<>();
        DbeAppProvider appProvider = new DbeAppProvider();
        providers.add(appProvider);

        when(appProviderDao.getProvidersByAggregator(aggregatorId)).thenReturn(providers);

        List <DbeAppProvider> returned = toTest.getProviders(aggregatorId);
        Assert.assertEquals(providers, returned);
    }

    @Test
    public void getProvidersNullTest() throws RSSException {
        String aggregatorId = null;

        List <DbeAppProvider> providers = new LinkedList<>();
        DbeAppProvider appProvider = new DbeAppProvider();
        providers.add(appProvider);

        when(appProviderDao.getAll()).thenReturn(providers);

        List <DbeAppProvider> returned = toTest.getProviders(aggregatorId);
        Assert.assertEquals(providers, returned);
    }

    @Test
    public void getProvidersVoidTest() throws RSSException {
        String aggregatorId = "";

        List <DbeAppProvider> providers = new LinkedList<>();
        DbeAppProvider appProvider = new DbeAppProvider();
        providers.add(appProvider);

        when(appProviderDao.getAll()).thenReturn(providers);

        List <DbeAppProvider> returned = toTest.getProviders(aggregatorId);
        Assert.assertEquals(providers, returned);
    }

    @Test
    public void getProvidersVoidListTest() throws RSSException {
        String aggregatorId = "aggregator@mail.com";

        List <DbeAppProvider> providers = new LinkedList<>();
        DbeAppProvider appProvider = new DbeAppProvider();
        providers.add(appProvider);

        when(appProviderDao.getProvidersByAggregator(aggregatorId)).thenReturn(null);

        List <DbeAppProvider> returned = toTest.getProviders(aggregatorId);
        Assert.assertTrue(returned.isEmpty());
    }



    @Test
    public void getProviderTest() throws RSSException {
        String aggregatorId = "aggregator@mail.com";
        String providerId = "provider@mail.com";

        DbeAggregator dbeAggregator = new DbeAggregator("aggegatorName", aggregatorId);

        DbeAppProviderId dbeAppProviderId = new DbeAppProviderId();
        dbeAppProviderId.setAggregator(dbeAggregator);
        dbeAppProviderId.setTxAppProviderId(providerId);

        DbeAppProvider provModel = new DbeAppProvider();
        provModel.setId(dbeAppProviderId);
        provModel.setModels(null);
        provModel.setTxCorrelationNumber(Integer.MIN_VALUE);
        provModel.setTxName(providerId);
        provModel.setTxTimeStamp(new Date());


        doNothing().when(modelsManager).checkValidAppProvider(aggregatorId, providerId);
        when(appProviderDao.getProvider(aggregatorId, providerId)).thenReturn(provModel);

        toTest.getProvider(aggregatorId, providerId);
    }

    @Test
    (expected = RSSException.class)
    public void getProviderRSSExceptionNotValidAppProviderTest() throws RSSException {
        String aggregatorId = "aggregator@mail.com";
        String providerId = "provider@mail.com";

        doThrow(RSSException.class).when(modelsManager).checkValidAppProvider(aggregatorId, providerId);

        toTest.getProvider(aggregatorId, providerId);
    }

    @Test
    (expected = RSSException.class)
    public void getProviderRSSExceptionNonExistentResourceTest() throws RSSException {
        String aggregatorId = "aggregator@mail.com";
        String providerId = "provider@mail.com";

        doNothing().when(modelsManager).checkValidAppProvider(aggregatorId, providerId);
        when(appProviderDao.getProvider(aggregatorId, providerId)).thenReturn(null);

        toTest.getProvider(aggregatorId, providerId);
    }
}
