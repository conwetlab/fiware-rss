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

import es.tid.fiware.rss.dao.CurrencyDao;
import es.tid.fiware.rss.dao.DbeAggregatorDao;
import es.tid.fiware.rss.dao.DbeAppProviderDao;
import es.tid.fiware.rss.dao.DbeTransactionDao;
import es.tid.fiware.rss.exception.RSSException;
import es.tid.fiware.rss.exception.UNICAExceptionType;
import es.tid.fiware.rss.model.BmCurrency;
import es.tid.fiware.rss.model.CDR;
import es.tid.fiware.rss.model.DbeAggregator;
import es.tid.fiware.rss.model.DbeAppProvider;
import es.tid.fiware.rss.model.DbeTransaction;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor=Exception.class)
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

    @Autowired
    private DbeTransactionDao transactionDao;

    @Autowired 
    private DbeAggregatorDao aggregatorDao;

    @Autowired
    private DbeAppProviderDao appProviderDao;

    @Autowired
    private CurrencyDao currencyDao;

    @Autowired
    private UserManager userManager;

    @Autowired
    private RSSModelsManager modelsManager;

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

    private List<CDR> getCDRsAPIFormat(List<DbeTransaction> txs) {
        List<CDR> result = new ArrayList<>();

        for (DbeTransaction tx: txs) {
            CDR cdr = new CDR();

            cdr.setAppProvider(tx.getAppProvider().getId().getTxAppProviderId());
            cdr.setApplication(tx.getTxApplicationId());
            cdr.setCdrSource(tx.getCdrSource().getTxEmail());
            cdr.setChargedAmount(tx.getFtChargedAmount());
            cdr.setChargedTaxAmount(tx.getFtChargedTaxAmount());
            cdr.setCorrelationNumber(tx.getTxPbCorrelationId());
            cdr.setCurrency(tx.getBmCurrency().getTxIso4217Code());
            cdr.setCustomerId(tx.getTxEndUserId());
            cdr.setDescription(tx.getTxOperationNature());
            cdr.setEvent(tx.getTxEvent());
            cdr.setProductClass(tx.getTxProductClass());
            cdr.setReferenceCode(tx.getTxReferenceCode());
            cdr.setTimestamp(tx.getTsClientDate());
            cdr.setTransactionType(tx.getTcTransactionType());

            result.add(cdr);
        }
        return result;
    }

    /**
     * Retrieve existing transactions filtered by aggregator and provider
     * @param aggregatorId, id of the aggregator used to filter the list
     * @param providerId, id of the provider used to filter the list
     * @return, List of CDRs 
     */
    public List<CDR> getCDRs(String aggregatorId, String providerId) {
        List<CDR> result;

        // Retrieve all pending transactions
        if (aggregatorId == null && providerId == null) {
            result = this.getCDRsAPIFormat(this.transactionDao.getTransactions());
        } else if (providerId == null) {
            result = this.getCDRsAPIFormat(
                    this.transactionDao.getTransactionByAggregatorId(aggregatorId));
        } else {
            result = this.getCDRsAPIFormat(
                    this.transactionDao.getTransactionsByProviderId(providerId));
        }
        return result;
    }

    /**
     * Saves a list of CDRs in the database as transactionss
     * @param cdrs, CDRs containing transaction information
     * @throws RSSException, If the CDR information is not valid
     */
    public void createCDRs(List<CDR> cdrs) throws RSSException {

        // Process CDRs one by one
        for (CDR cdr: cdrs) {
            // Check if the user has permission to create a new transaction
            if (!userManager.isAdmin() && 
                !userManager.getCurrentUser().getEmail().equalsIgnoreCase(cdr.getCdrSource())) {
                String[] args = {"You are not allowed to register a transaction for the Store owned by " + cdr.getCdrSource()};
                throw new RSSException(UNICAExceptionType.NON_ALLOWED_OPERATION, args);
            }

            // Check that the aggregator (CDR Source) exists
            DbeAggregator aggregator = this.aggregatorDao.getById(cdr.getCdrSource());

            if (aggregator ==  null) {
                String[] args = {"The Store identified by  " + cdr.getCdrSource() + " does not exists"};
                throw new RSSException(UNICAExceptionType.NON_EXISTENT_RESOURCE_ID, args);
            }

            // Validate the provider
            this.modelsManager.checkValidAppProvider(cdr.getCdrSource(), cdr.getAppProvider());
            DbeAppProvider provider = this.appProviderDao.getById(cdr.getAppProvider());

            // Validate the currency
            BmCurrency currency = this.currencyDao.getByIso4217StringCode(cdr.getCurrency());

            if (currency == null)  {
                String[] args = {"The currency  " + cdr.getCurrency() + " is not supported"};
                throw new RSSException(UNICAExceptionType.NON_EXISTENT_RESOURCE_ID, args);
            }

            // Validate correlation number and timestamp
            Integer nextCorr = provider.getTxCorrelationNumber();
            if (!nextCorr.equals(cdr.getCorrelationNumber())) {
                String[] args = {"Invalid correlation number, expected: " + nextCorr};
                throw new RSSException(UNICAExceptionType.INVALID_PARAMETER, args);
            }

            provider.setTxCorrelationNumber(nextCorr + 1);

            Date prevTime = provider.getTxTimeStamp();
            if (cdr.getTimestamp() == null || prevTime.after(cdr.getTimestamp())) {
                String[] args = {"Invalid timestamp: The given time is earlier that the prevoius one"};
                throw new RSSException(UNICAExceptionType.INVALID_PARAMETER, args);
            }

            provider.setTxTimeStamp(cdr.getTimestamp());

            // Validate transaction type
            if (!cdr.getTransactionType().equalsIgnoreCase("C") &&
                    !cdr.getTransactionType().equalsIgnoreCase("R")) {

                String[] args = {"The transaction type " + cdr.getTransactionType()
                        + " is not supported, must be C (charge) or R (refund)"};
                throw new RSSException(UNICAExceptionType.INVALID_PARAMETER, args);
            }

            // Save CDR to the database
            DbeTransaction tx = new DbeTransaction();

            if (cdr.getProductClass() == null || cdr.getProductClass().isEmpty()) {
                String[] args = {"Missing productClass"};
                throw new RSSException(UNICAExceptionType.MISSING_MANDATORY_PARAMETER, args);
            }

            if (cdr.getReferenceCode() == null || cdr.getReferenceCode().isEmpty()) {
                String[] args = {"Missing referenceCode"};
                throw new RSSException(UNICAExceptionType.MISSING_MANDATORY_PARAMETER, args);
            }

            tx.setTxProductClass(cdr.getProductClass());
            tx.setState("pending");
            tx.setCdrSource(aggregator);
            tx.setTxPbCorrelationId(cdr.getCorrelationNumber());
            tx.setTsClientDate(cdr.getTimestamp());
            tx.setTxApplicationId(cdr.getApplication());
            tx.setTcTransactionType(cdr.getTransactionType());
            tx.setTxEvent(cdr.getEvent());
            tx.setTxReferenceCode(cdr.getReferenceCode());
            tx.setTxOperationNature(cdr.getDescription());
            tx.setFtChargedAmount(cdr.getChargedAmount());
            tx.setFtChargedTaxAmount(cdr.getChargedTaxAmount());
            tx.setBmCurrency(currency);
            tx.setTxEndUserId(cdr.getCustomerId());
            tx.setAppProvider(provider);

            try {
                this.transactionDao.create(tx);
            } catch (org.hibernate.NonUniqueObjectException e) {
                String msg = "This transaction has been already registered:" +
                        "For a given transaction the provider, the reference, " +
                        " and the correlation number must be unique";

                String[] args = {msg};
                throw new RSSException(UNICAExceptionType.RESOURCE_ALREADY_EXISTS, args);
            }
        }
    }
}
