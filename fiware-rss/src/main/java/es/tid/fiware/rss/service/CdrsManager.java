/**
 * Revenue Settlement and Sharing System GE
 * Copyright (C) 2011-2014, Javier Lucio - lucio@tid.es
 * Telefonica Investigacion y Desarrollo, S.A.
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

import es.tid.fiware.rss.dao.DbeTransactionDao;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CdrsManager {

    /***
     * Logging system.
     */
    private final Logger logger = LoggerFactory.getLogger(CdrsManager.class);
    /**
     * Properties.
     */
    @Resource(name = "rssProps")
    private Properties rssProps;
    /*
     * Runtime Environment
     */
    private Runtime runtime = Runtime.getRuntime();

    @Autowired
    private DbeTransactionDao transactionDao;

    /**
     * Get File where save cdrs.
     * 
     * @return
     * @throws IOException
     */
    public File getFile() throws IOException {
        Date date = Calendar.getInstance().getTime();
        DateFormat formatter = new SimpleDateFormat("yyyyMMdd_hhmmss");
        String dateFormatted = formatter.format(date);
        String path = (String) rssProps.get("cdrfilepath");
        File cdrFile = new File(path + "fiwarecdr_" + dateFormatted + ".xml");
        cdrFile.createNewFile();
        logger.debug("File created: " + cdrFile.getCanonicalPath());
        return cdrFile;
    }

    /**
     * Run process to save Cdr into database.
     * 
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public String runCdrToDB() throws IOException, InterruptedException {
        String cdrToDBScript = (String) rssProps.get("cdrToDBScript");
        logger.trace("cdrToDBScript: " + cdrToDBScript);
        File cdrToDBSH = new File(cdrToDBScript);
        logger.debug("Running script: " + cdrToDBSH.getCanonicalPath());
        Process p = runtime.exec("sh " + cdrToDBSH.getCanonicalPath());
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String aLine;
        String error = null;
        while ((aLine = br.readLine()) != null) {
            if (aLine.indexOf("ERROR") != -1 && error == null) {
                error = aLine;
                logger.error(aLine);
            } else {
                logger.debug(aLine);
            }
        }
        int exitVal = p.waitFor();
        br.close();
        logger.debug("Process exitValue: " + exitVal);
        return error;
    }
}
