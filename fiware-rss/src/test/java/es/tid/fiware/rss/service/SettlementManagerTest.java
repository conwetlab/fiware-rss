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

import org.junit.Before;
import org.junit.Test;
import es.tid.fiware.rss.dao.DbeTransactionDao;
import es.tid.fiware.rss.exception.RSSException;
import es.tid.fiware.rss.model.Aggregator;
import es.tid.fiware.rss.model.DbeTransaction;
import es.tid.fiware.rss.model.RSSModel;
import es.tid.fiware.rss.model.RSSProvider;
import es.tid.fiware.rss.settlement.ProductSettlementTask;
import es.tid.fiware.rss.settlement.SettlementTaskFactory;
import es.tid.fiware.rss.settlement.ThreadPoolManager;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import org.mockito.InjectMocks;
import static org.mockito.Matchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

public class SettlementManagerTest {

    @Mock private DbeTransactionDao transactionDao;
    @Mock private SettlementTaskFactory taskFactory;
    @Mock private AggregatorManager aggregatorManager;
    @Mock private ProviderManager providerManager;
    @Mock private RSSModelsManager modelsManager;
    @Mock private ThreadPoolManager poolManager;
    @InjectMocks private SettlementManager toTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void runSettlementTest() throws RSSException {
        String aggregatorId = "aggregator@mail.com";
        String providerId = "provider@mail.com";
        String productClass = "productClass";

        Aggregator aggregator = new Aggregator();
        aggregator.setAggregatorId(aggregatorId);
        aggregator.setAggregatorName(aggregatorId);

        List <RSSProvider> providers = new LinkedList<>();
        RSSProvider rSSProvider = new RSSProvider();
        rSSProvider.setAggregatorId(aggregatorId);
        rSSProvider.setProviderId(providerId);
        rSSProvider.setProviderName(providerId);
        providers.add(rSSProvider);

        List <RSSModel> models = new LinkedList<>();
        RSSModel model = new RSSModel();
        model.setAggregatorId(aggregatorId);
        model.setAggregatorShare(BigDecimal.ZERO);
        model.setAlgorithmType(aggregatorId);
        model.setOwnerProviderId(providerId);
        model.setOwnerValue(BigDecimal.ZERO);
        model.setProductClass(productClass);
        model.setStakeholders(null);
        models.add(model);

        when(modelsManager.existModel(aggregatorId, providerId, productClass)).thenReturn(Boolean.TRUE);
        when(aggregatorManager.getAggregator(aggregatorId)).thenReturn(aggregator);
        when(providerManager.getAPIProviders(aggregatorId)).thenReturn(providers);
        when(providerManager.getProvider(aggregatorId, providerId)).thenReturn(rSSProvider);
        when(modelsManager.getRssModels(aggregatorId, providerId, productClass)).thenReturn(models);

        toTest.runSettlement(aggregatorId, providerId, productClass);
    }

    @Test
    public void runSettlement2Test() throws RSSException {
        String aggregatorId = "aggregator@mail.com";
        String providerId = "provider@mail.com";
        String productClass = "productClass";

        Aggregator aggregator = new Aggregator();
        aggregator.setAggregatorId(aggregatorId);
        aggregator.setAggregatorName(aggregatorId);

        List <RSSProvider> providers = new LinkedList<>();
        RSSProvider rSSProvider = new RSSProvider();
        rSSProvider.setAggregatorId(aggregatorId);
        rSSProvider.setProviderId(providerId);
        rSSProvider.setProviderName(providerId);
        providers.add(rSSProvider);

        List <RSSModel> models = new LinkedList<>();
        RSSModel model = new RSSModel();
        model.setAggregatorId(aggregatorId);
        model.setAggregatorShare(BigDecimal.ZERO);
        model.setAlgorithmType(aggregatorId);
        model.setOwnerProviderId(providerId);
        model.setOwnerValue(BigDecimal.ZERO);
        model.setProductClass(productClass);
        model.setStakeholders(null);
        models.add(model);

        List <DbeTransaction> transactions = new LinkedList<>();
        DbeTransaction transaction = new DbeTransaction();
        transactions.add(transaction);

        ProductSettlementTask settlementTask = new ProductSettlementTask();
        ExecutorService executorService = mock(ExecutorService.class);

        when(modelsManager.existModel(aggregatorId, providerId, productClass)).thenReturn(true);
        when(aggregatorManager.getAggregator(aggregatorId)).thenReturn(aggregator);
        when(providerManager.getAPIProviders(aggregatorId)).thenReturn(providers);
        when(providerManager.getProvider(aggregatorId, providerId)).thenReturn(rSSProvider);
        when(modelsManager.getRssModels(aggregatorId, providerId, productClass)).thenReturn(models);
        when(transactionDao.getTransactions(aggregatorId, providerId, productClass)).thenReturn(transactions);
        when(taskFactory.getSettlementTask(any(RSSModel.class), any(List.class))).thenReturn(settlementTask);
        when(poolManager.getExecutorService()).thenReturn(executorService);

        toTest.runSettlement(aggregatorId, providerId, productClass);
    }

    @Test
    public void runSettlementNullArgumentsTest() throws RSSException {
        String aggregatorId = null;
        String providerId = null;
        String productClass = null;


        List <Aggregator> aggregators = new LinkedList();
        Aggregator aggregator = new Aggregator();
        aggregator.setAggregatorId(aggregatorId);
        aggregator.setAggregatorName(aggregatorId);
        aggregators.add(aggregator);

        List <RSSProvider> providers = new LinkedList<>();
        RSSProvider rSSProvider = new RSSProvider();
        rSSProvider.setAggregatorId(aggregatorId);
        rSSProvider.setProviderId(providerId);
        rSSProvider.setProviderName(providerId);
        providers.add(rSSProvider);

        List <RSSModel> models = new LinkedList<>();
        RSSModel model = new RSSModel();
        model.setAggregatorId(aggregatorId);
        model.setAggregatorShare(BigDecimal.ZERO);
        model.setAlgorithmType(aggregatorId);
        model.setOwnerProviderId(providerId);
        model.setOwnerValue(BigDecimal.ZERO);
        model.setProductClass(productClass);
        model.setStakeholders(null);
        models.add(model);

        when(modelsManager.existModel(aggregatorId, providerId, productClass)).thenReturn(Boolean.TRUE);
        when(aggregatorManager.getAPIAggregators()).thenReturn(aggregators);
        when(providerManager.getAPIProviders(aggregatorId)).thenReturn(providers);
        when(providerManager.getProvider(aggregatorId, providerId)).thenReturn(rSSProvider);
        when(modelsManager.getRssModels(aggregatorId, providerId, productClass)).thenReturn(models);

        toTest.runSettlement(aggregatorId, providerId, productClass);
    }

    @Test
    (expected = RSSException.class)
    public void runSettlementRSSExceptionTest() throws RSSException {
        String aggregatorId = "aggregator@mail.com";
        String providerId = "provider@mail.com";
        String productClass = "productClass";

        when(modelsManager.existModel(aggregatorId, providerId, productClass)).thenReturn(false);

        toTest.runSettlement(aggregatorId, providerId, productClass);
    }

    @Test
    public void setTxStateTest() {
        String state = "state";
        List <DbeTransaction> transactions = new LinkedList<>();

        DbeTransaction transaction = new DbeTransaction();
        transactions.add(transaction);

        toTest.setTxState(transactions, state, true);
    }

    @Test
    public void setTxStateNotFlushTest() {
        String state = "state";
        List <DbeTransaction> transactions = new LinkedList<>();

        DbeTransaction transaction = new DbeTransaction();
        transactions.add(transaction);

        toTest.setTxState(transactions, state, false);
    }

}
