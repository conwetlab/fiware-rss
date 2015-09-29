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
package es.upm.fiware.rss.settlement;

import es.upm.fiware.rss.model.BmCurrency;
import es.upm.fiware.rss.model.DbeTransaction;
import es.upm.fiware.rss.model.RSSModel;
import es.upm.fiware.rss.model.StakeholderModel;
import es.upm.fiware.rss.service.SettlementManager;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import static org.mockito.Matchers.anyString;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

/**
 *
 * @author jortiz
 */
public class ProductSettlementTaskTest {

    @Mock private SettlementManager settlementManager;
    @Mock private Properties rssProps;
    /*@Mock private List<DbeTransaction> transactions;
    @Mock private RSSModel model;*/
    @InjectMocks private ProductSettlementTask toTest;

    public ProductSettlementTaskTest() {

    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void productSettlementTaskConstructor() {
        RSSModel model = new RSSModel();
        List<DbeTransaction> transactions = new LinkedList<>();

        toTest = new ProductSettlementTask(model, transactions);
    }

    @Test
    public void runTest() {

        BmCurrency currency = mock(BmCurrency.class);

        DbeTransaction transaction1 = new DbeTransaction();
        transaction1.setBmCurrency(currency);
        transaction1.setFtChargedAmount(BigDecimal.ZERO);
        transaction1.setFtChargedTaxAmount(BigDecimal.TEN);
        transaction1.setTcTransactionType("c");
        transaction1.setTxPbCorrelationId(Integer.MIN_VALUE);
        transaction1.setTxTransactionId(20);

        DbeTransaction transaction2 = new DbeTransaction();
        transaction2.setBmCurrency(currency);
        transaction2.setFtChargedAmount(BigDecimal.ZERO);
        transaction2.setFtChargedTaxAmount(BigDecimal.TEN);
        transaction2.setTcTransactionType("a");
        transaction2.setTxPbCorrelationId(Integer.MIN_VALUE);
        transaction2.setTxTransactionId(20);

        List <DbeTransaction> transactions = new LinkedList<>();
        transactions.add(transaction1);
        transactions.add(transaction2);

        List <StakeholderModel> stakeholders = new LinkedList<>();
        StakeholderModel stakeholderModel1 = new StakeholderModel();
        stakeholderModel1.setModelValue(BigDecimal.valueOf(20));
        stakeholderModel1.setStakeholderId("stakeholder1@mail.com");
        StakeholderModel stakeholderModel2 = new StakeholderModel();
        stakeholderModel2.setModelValue(BigDecimal.valueOf(20));
        stakeholderModel2.setStakeholderId("stakeholder2@mail.com");
        stakeholders.add(stakeholderModel1);
        stakeholders.add(stakeholderModel2);

        RSSModel model = new RSSModel();
        model.setAggregatorId("agregator@mail.com");
        model.setAggregatorShare(BigDecimal.valueOf(30));
        model.setAlgorithmType("FIXED_PERCENTAGE");
        model.setOwnerProviderId("owner@mail.com");
        model.setOwnerValue(BigDecimal.valueOf(30));
        model.setProductClass("productClass");
        model.setStakeholders(stakeholders);

        toTest = new ProductSettlementTask(model, transactions);
        MockitoAnnotations.initMocks(this);

        toTest.run();
    }

    @Test
    public void runRSSExceptionTest() {

        BmCurrency currency = mock(BmCurrency.class);

        DbeTransaction transaction1 = new DbeTransaction();
        transaction1.setBmCurrency(currency);
        transaction1.setFtChargedAmount(BigDecimal.ZERO);
        transaction1.setFtChargedTaxAmount(BigDecimal.TEN);
        transaction1.setTcTransactionType("c");
        transaction1.setTxPbCorrelationId(Integer.MIN_VALUE);
        transaction1.setTxTransactionId(20);

        DbeTransaction transaction2 = new DbeTransaction();
        transaction2.setBmCurrency(currency);
        transaction2.setFtChargedAmount(BigDecimal.ZERO);
        transaction2.setFtChargedTaxAmount(BigDecimal.TEN);
        transaction2.setTcTransactionType("a");
        transaction2.setTxPbCorrelationId(Integer.MIN_VALUE);
        transaction2.setTxTransactionId(20);

        List <DbeTransaction> transactions = new LinkedList<>();
        transactions.add(transaction1);
        transactions.add(transaction2);

        List <StakeholderModel> stakeholders = new LinkedList<>();
        StakeholderModel stakeholderModel1 = new StakeholderModel();
        stakeholderModel1.setModelValue(BigDecimal.valueOf(20));
        stakeholderModel1.setStakeholderId("stakeholder1@mail.com");
        StakeholderModel stakeholderModel2 = new StakeholderModel();
        stakeholderModel2.setModelValue(BigDecimal.valueOf(20));
        stakeholderModel2.setStakeholderId("stakeholder2@mail.com");
        stakeholders.add(stakeholderModel1);
        stakeholders.add(stakeholderModel2);

        RSSModel model = new RSSModel();
        model.setAggregatorId("agregator@mail.com");
        model.setAggregatorShare(BigDecimal.valueOf(100));
        model.setAlgorithmType("FIXED_PERCENTAGE");
        model.setOwnerProviderId("owner@mail.com");
        model.setOwnerValue(BigDecimal.valueOf(30));
        model.setProductClass("productClass");
        model.setStakeholders(stakeholders);

        when(rssProps.get(anyString())).thenThrow(IOException.class);

        toTest = new ProductSettlementTask(model, transactions);
        MockitoAnnotations.initMocks(this);

        toTest.run();
    }
}
