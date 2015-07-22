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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.Date;
import java.util.Properties;
import java.util.List;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.tid.fiware.rss.algorithm.AlgorithmFactory;
import es.tid.fiware.rss.algorithm.AlgorithmProcessor;
import es.tid.fiware.rss.model.DbeTransaction;
import es.tid.fiware.rss.model.RSSModel;
import es.tid.fiware.rss.model.StakeholderModel;
import es.tid.fiware.rss.service.SettlementManager;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

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

    private String createDir(String path, String id) {
        File provFolder = new File(path, id);
        if (!provFolder.exists()) {
            provFolder.mkdir();
        }
        return provFolder.getPath();
    }

    private void generateReport(RSSModel sharingRes, String curr) throws IOException {
        this.logger.info("Generating report: " 
                    + this.model.getAggregatorId() + " "
                    + this.model.getOwnerProviderId() + " "
                    + this.model.getProductClass());

        String reportsPath = (String) rssProps.get("reportsPath");

        // Create aggregator folder if it does not exists
        String path = this.createDir(reportsPath, sharingRes.getAggregatorId());

        // Create provider folder if it does not exists
        path = this.createDir(path, sharingRes.getOwnerProviderId());

        // Create new report file
        Date date = new Date();
        DateFormat formatter = new SimpleDateFormat("yyyyMMdd_hhmmss");

        String reportId = sharingRes.getProductClass() + "_" + formatter.format(date) + ".csv";
        File reportFile = new File(path, reportId);

        FileWriter fw = new FileWriter(reportFile);

        try(BufferedWriter bw = new BufferedWriter(fw)) {
            int i = 0;
            String title = "algorithm,class,store_owner,store_value,owner,owner_value,currency";

            String value = sharingRes.getAlgorithmType() + ","
                    + sharingRes.getProductClass() + ","
                    + sharingRes.getAggregatorId() + ","
                    + sharingRes.getAggregatorValue() + ","
                    + sharingRes.getOwnerProviderId() + ","
                    + sharingRes.getOwnerValue() + ","
                    + curr;

            for (StakeholderModel st: sharingRes.getStakeholders()) {
                title += ",stakeholder_" + i + ",stakeholder_value_" + i;
                value += "," +st.getStakeholderId() + "," + st.getModelValue();
                i++;
            }

            bw.write(title + "\n");
            bw.write(value + "\n");
        }
        
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
            this.generateReport(sharingRes, curr);

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
