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

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import es.tid.fiware.rss.dao.DbeTransactionDao;
import es.tid.fiware.rss.exception.RSSException;
import es.tid.fiware.rss.model.DbeTransaction;
import es.tid.fiware.rss.settlement.SettlementTaskFactory;
import es.tid.fiware.rss.settlement.ThreadPoolManager;
import java.io.File;
import java.util.LinkedList;
import java.util.Properties;
import static org.junit.Assert.fail;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

public class SettlementManagerTest {

    @Mock private DbeTransactionDao transactionDao;
    @Mock private SettlementTaskFactory taskFactory;
    @Mock private AggregatorManager aggregatorManager;
    @Mock private ProviderManager providerManager;
    @Mock private RSSModelsManager modelsManager;
    @Mock private ThreadPoolManager poolManager;
    @Mock private Properties rssProps;
    @InjectMocks private SettlementManager toTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    //TODO:
    public void runSettlementTest() throws RSSException {

        String aggregatorId = "aggregator@mail.com";
        String providerId = "provider@mail.com";
        String productClass = "productClass";

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
