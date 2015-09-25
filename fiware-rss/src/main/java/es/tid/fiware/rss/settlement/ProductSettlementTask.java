/**
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

package es.tid.fiware.rss.settlement;

import java.util.Properties;
import java.util.List;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.tid.fiware.rss.algorithm.AlgorithmFactory;
import es.tid.fiware.rss.algorithm.AlgorithmProcessor;
import es.tid.fiware.rss.model.DbeTransaction;
import es.tid.fiware.rss.model.RSSModel;
import es.tid.fiware.rss.service.SettlementManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author fdelavega
 */
@Component
public class ProductSettlementTask implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(ProductSettlementTask.class);

    @Autowired
    private SettlementManager settlementManager;

    @Autowired
    private Properties rssProps;

    private List<DbeTransaction> transactions;
    private RSSModel model;

    public ProductSettlementTask() {
    }

    public ProductSettlementTask(RSSModel model, List<DbeTransaction> transactions) {
        this.model = model;
        this.transactions = transactions;
    }

    @Override
    public void run() {
        this.logger.info("Processing class " + this.model.getProductClass());

        // Aggregate value
        String curr = this.transactions.get(0).getBmCurrency().getTxIso4217Code();
        BigDecimal value = new BigDecimal("0");

        for (DbeTransaction tx: this.transactions) {
            if (tx.getTcTransactionType().equalsIgnoreCase("c")) {
                value = value.add(tx.getFtChargedAmount());
            } else {
                value = value.subtract(tx.getFtChargedAmount());
            }
        }

        // Calculate RS
        RSSModel sharingRes;
        try {
            AlgorithmFactory factory = new AlgorithmFactory();
            AlgorithmProcessor processor = factory.getAlgorithmProcessor(this.model.getAlgorithmType());

            sharingRes = processor.calculateRevenue(model, value);
            this.settlementManager.generateReport(sharingRes, curr);

        } catch (Exception e) {
            this.logger.info("Error processing transactions of: "
                    + this.model.getAggregatorId() + " "
                    + this.model.getOwnerProviderId() + " "
                    + this.model.getProductClass() + " "
                    + e.getMessage());

            // Set transactions as pending
            this.settlementManager.setTxState(transactions, "pending", true);
            return;
        }

        // Set transactions as processed
        this.settlementManager.setTxState(transactions, "processed", true);
    }
}
