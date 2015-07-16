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

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.tid.fiware.rss.dao.DbeAppProviderDao;
import es.tid.fiware.rss.dao.DbeTransactionDao;
import es.tid.fiware.rss.exception.RSSException;
import es.tid.fiware.rss.model.CDR;
import es.tid.fiware.rss.model.DbeAppProvider;
import es.tid.fiware.rss.model.DbeTransaction;
import es.tid.fiware.rss.model.RSSFile;

@Service
@Transactional
public class SettlementManager {

    /***
     * Logging system.
     */
    private final Logger logger = LoggerFactory.getLogger(SettlementManager.class);

    /**
     * 
     */
    @Autowired
    private DbeAppProviderDao appProviderDao;

    /**
     * 
     */
    @Autowired
    private DbeTransactionDao transactionDao;

    /**
     * 
     */
    private Runtime runtime;
    /**
     * 
     */
    @Resource(name = "rssProps")
    private Properties rssProps;

    /**
     * 
     * @throws Exception
     */
    @PostConstruct
    private void init() throws Exception {
        runtime = Runtime.getRuntime();
    }

    /**
     * Launch settlement process.
     * 
     * @param startPeriod
     * @param endPeriod
     * @param aggregatorId
     * @throws IOException
     */
    public void runSettlement(String startPeriod, String endPeriod, String aggregatorId, String providerId) {
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
