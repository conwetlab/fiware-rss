/**
 * Revenue Settlement and Sharing System GE
 * Copyright (C) 2011-2014, Javier Lucio - lucio@tid.es
 * Telefonica Investigacion y Desarrollo, S.A.
 *
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

package es.tid.fiware.rss.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.tid.fiware.rss.dao.DbeTransactionDao;
import es.tid.fiware.rss.exception.RSSException;
import es.tid.fiware.rss.exception.UNICAExceptionType;
import es.tid.fiware.rss.model.Aggregator;
import es.tid.fiware.rss.model.DbeTransaction;
import es.tid.fiware.rss.model.RSSFile;
import es.tid.fiware.rss.model.RSSModel;
import es.tid.fiware.rss.model.RSSProvider;
import es.tid.fiware.rss.settlement.ProductSettlementTask;
import es.tid.fiware.rss.settlement.SettlementTaskFactory;
import es.tid.fiware.rss.settlement.ThreadPoolManager;

@Service
@Transactional(rollbackFor = Exception.class)
public class SettlementManager {

    /***
     * Logging system.
     */
    private final Logger logger = LoggerFactory.getLogger(SettlementManager.class);

    /**
     * 
     */
    @Autowired
    private DbeTransactionDao transactionDao;

    @Autowired
    private SettlementTaskFactory taskFactory;

    @Autowired
    private AggregatorManager aggregatorManager;

    @Autowired
    private ProviderManager providerManager;

    @Autowired
    private RSSModelsManager modelsManager;

    @Autowired
    private ThreadPoolManager poolManager;

    
    @Resource(name = "rssProps")
    private Properties rssProps;

    private List<Aggregator> getAggregators(String aggregatorId)  
            throws RSSException{

        // Get given aggregators if needed
        List<Aggregator> aggregators;
        if (aggregatorId == null || aggregatorId.isEmpty()) {
            aggregators = this.aggregatorManager.getAPIAggregators();
        } else {
            aggregators = new ArrayList<>();
            aggregators.add(this.aggregatorManager.getAggregator(aggregatorId));
        }
        return aggregators;
    }

    private List<RSSProvider> getProviders(String aggregatorId,
            String providerId) throws RSSException {

        List<RSSProvider> providers;
        if (providerId != null && !providerId.isEmpty()) {
            providers = new ArrayList<>();
            providers.add(this.providerManager.getProvider(aggregatorId, providerId));
        } else {
            providers = this.providerManager.getAPIProviders(aggregatorId);
        }
        return providers;
    }

    private List<RSSModel> getModels(String aggregatorId,
            String providerId, String productClass) throws RSSException {

        return this.modelsManager.getRssModels(aggregatorId, providerId, productClass);
    }

    /**
     * Launch settlement process.
     * 
     * @param aggregatorId
     * @param providerId
     * @param productClass
     * @throws RSSException
     */
    public void runSettlement(String aggregatorId,
            String providerId, String productClass) throws RSSException {

        // Validate fields
        if (aggregatorId != null && !aggregatorId.isEmpty()) {
            if (providerId != null && !providerId.isEmpty()) {

                // Check that the given provider belongs to the aggregator
                this.modelsManager.checkValidAppProvider(aggregatorId, providerId);

                // Check that the provider has a RS model
                // for the specified product class
                if (productClass != null && !productClass.isEmpty()
                        && !this.modelsManager.existModel(aggregatorId, providerId, productClass)) {

                    String[] args = {productClass};
                    throw new RSSException(UNICAExceptionType.NON_EXISTENT_RESOURCE_ID, args);
                }
            }
        }

        // Launch settlement for the given transactions
        for (Aggregator ag: this.getAggregators(aggregatorId)) {
            List<RSSProvider> providers = this.getProviders(ag.getAggregatorId(), providerId);

            for(RSSProvider pv: providers) {
                List<RSSModel> models =
                        this.getModels(ag.getAggregatorId(), pv.getProviderId(), productClass);

                for (RSSModel m: models) {
                    // Get related transactions
                    List<DbeTransaction> txs = this.transactionDao.
                            getTransactions(ag.getAggregatorId(), pv.getProviderId(), m.getProductClass());

                    // Set transactions as processing
                    if (txs != null && !txs.isEmpty()) {
                        this.setTxState(txs, "processing", true);

                        // Create processing task
                        ProductSettlementTask settlementTask
                                = this.taskFactory.getSettlementTask(m, txs);

                        poolManager.getExecutorService().submit(settlementTask);
                    }
                }
            }
        }
    }

    /**
     * 
     * @param transactions
     * @param state
     * @param toFlush 
     */
    public void setTxState(List<DbeTransaction> transactions,
            String state, boolean toFlush) {

        for (DbeTransaction tx: transactions) {
            tx.setState(state);
            this.transactionDao.update(tx);
        }
        // Flush state to the database
        if (toFlush) {
            this.transactionDao.flush();
        }
    }

    /**
     * Get settlement files from file System.
     * 
     * @return
     */
    public List<RSSFile> getSettlementFiles(String aggregatorId) {
        logger.debug("Into getSettlementFiles method.");
        List<RSSFile> rssFilesList = new ArrayList<RSSFile>();
        return rssFilesList;
    }

    /**
     * Get files from path.
     * 
     * @param path
     * @return
     */
    public List<RSSFile> getSettlementFilesOfPath(String path) {
        // Opening/creating the folder
        File folder = new File(path);
        List<RSSFile> rssFilesList = new ArrayList<>();
        RSSFile rssf = new RSSFile();

        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            Arrays.sort(files);

            if (files.length > 0) {
                List<File> fileList = new ArrayList<File>(Arrays.asList(files));
                ListIterator<File> lit = fileList.listIterator();

                while (lit.hasNext()) {
                    File file = lit.next();
                    logger.info(file.getAbsolutePath());

                    if (file.isDirectory()) {
                        logger.debug("Is directory. Getting more files...");
                        File[] moreFiles = file.listFiles();
                        Arrays.sort(moreFiles);
                        if (moreFiles.length > 0) {
                            for (File f : moreFiles) {
                                lit.add(f);
                                lit.previous();
                            }
                        }
                    } else {
                        rssf = new RSSFile();
                        rssf.setTxName(file.getName());
                        rssf.setTxUrl(file.getAbsolutePath());
                        rssFilesList.add(rssf);
                        logger.debug("File added");
                    }
                }
            }
        }
        return rssFilesList;
    }

    /**
     * Delete data from provider.
     * 
     * @param appProvider
     * @throws IOException
     */
    public void runClean(String appProvider) throws IOException {
        logger.debug("Deleting  transactions. Provider: {}", appProvider);
        transactionDao.deleteTransactionsByProviderId(appProvider);
        String reportsPath = (String) rssProps.get("reportsPath");
        reportsPath = reportsPath + appProvider;
        File folder = new File(reportsPath);
        deleteFolder(folder);
    }

    /**
     * Delete folders.
     * 
     * @param folder
     */
    private void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }
}
