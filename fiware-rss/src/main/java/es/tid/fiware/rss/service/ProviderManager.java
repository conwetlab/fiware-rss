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

package es.tid.fiware.rss.service;

import es.tid.fiware.rss.dao.DbeAggregatorAppProviderDao;
import es.tid.fiware.rss.dao.DbeAggregatorDao;
import es.tid.fiware.rss.dao.DbeAppProviderDao;
import es.tid.fiware.rss.exception.RSSException;
import es.tid.fiware.rss.exception.UNICAExceptionType;
import es.tid.fiware.rss.model.DbeAggregatorAppProvider;
import es.tid.fiware.rss.model.DbeAggregatorAppProviderId;
import es.tid.fiware.rss.model.DbeAppProvider;
import es.tid.fiware.rss.model.RSSProvider;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author fdelavega
 */
@Service
@Transactional
public class ProviderManager {
    /***
     * Logging system.
     */
    private final Logger logger = LoggerFactory.getLogger(ProviderManager.class);

    @Autowired
    private DbeAggregatorAppProviderDao aggregatorAppProviderDao;
    /**
     * 
     */
    @Autowired
    private DbeAppProviderDao appProviderDao;

    @Autowired
    private DbeAggregatorDao aggregatorDao;

    /**
     * Get providers from the DB in a format ready to be serialized
     * @param aggregatorId
     * @return
     * @throws RSSException 
     */
    public List<RSSProvider> getAPIProviders(String aggregatorId) throws RSSException {
        List<RSSProvider> apiProviders = new ArrayList<>();
        List<DbeAppProvider> providers = this.getProviders(aggregatorId);

        for(DbeAppProvider p: providers) {
            RSSProvider apiProvider = new RSSProvider();
            // Get aggregator of the provider
            apiProvider.setAggregatorId(
                    aggregatorAppProviderDao.
                            getDbeAggregatorAppProviderByProviderId(p.getTxAppProviderId()).
                            getId().getTxEmail());

            apiProvider.setProviderId(p.getTxAppProviderId());
            apiProvider.setProviderName(p.getTxName());
            apiProviders.add(apiProvider);
        }
        return apiProviders;
    }

    /**
     * Get providers from bbdd.
     * 
     * @param aggregatorId
     * @return
     * @throws RSSException
     */
    public List<DbeAppProvider> getProviders(String aggregatorId) throws RSSException {
        List<DbeAppProvider> providers = new ArrayList<>();

        if (null != aggregatorId && !aggregatorId.isEmpty()) {
            List<DbeAggregatorAppProvider> provsAgg = aggregatorAppProviderDao
                .getDbeAggregatorAppProviderByAggregatorId(aggregatorId);
            if (null != provsAgg && provsAgg.size() > 0) {
                for (DbeAggregatorAppProvider provAgg : provsAgg) {
                    providers.add(provAgg.getDbeAppProvider());
                }
            }
        } else {
            providers = appProviderDao.getAll();
        }
        return providers;
    }

    /**
     * Create a new provider for a given aggregator.
     * 
     * @param providerId
     * @param providerName
     * @param aggregatorId
     * @throws RSSException
     */
    public void createProvider(String providerId, String providerName,
            String aggregatorId) throws RSSException {

        logger.debug("Creating provider: {}", providerId);

        // Validate required fields
        if (providerId == null || providerId.isEmpty()) {
            String[] args = {"ProviderID field is required for creating a provider"};
            throw new RSSException(UNICAExceptionType.INVALID_PARAMETER, args);
        }

        if (providerName == null || providerName.isEmpty()) {
            String[] args = {"ProviderName field is required for creating a provider"};
            throw new RSSException(UNICAExceptionType.INVALID_PARAMETER, args);
        }

        if (aggregatorId == null || aggregatorId.isEmpty()) {
            String[] args = {"AggregatorID field is required for creating a provider"};
            throw new RSSException(UNICAExceptionType.INVALID_PARAMETER, args);
        }

        // Check that the aggregator exists
        if (this.aggregatorDao.getById(aggregatorId) == null) {
            String[] args = {"The given aggregator does not exists"};
            throw new RSSException(UNICAExceptionType.NON_EXISTENT_RESOURCE_ID, args);
        }
        
        // Build new Provider entity
        DbeAppProvider provider = new DbeAppProvider();
        provider.setTxAppProviderId(providerId);
        provider.setTxName(providerName);
        provider.setTxCorrelationNumber(0);
        provider.setTxTimeStamp(new Date());

        // Create provider
        try {
            appProviderDao.create(provider);
        } catch (org.hibernate.NonUniqueObjectException e) {
            String[] args = {"The given provider already exists"};
            throw new RSSException(UNICAExceptionType.RESOURCE_ALREADY_EXISTS, args);
        }

        // Create the aggregator provider object
        DbeAggregatorAppProvider object = new DbeAggregatorAppProvider();
        DbeAggregatorAppProviderId id = new DbeAggregatorAppProviderId();
        object.setId(id);
        id.setTxAppProviderId(providerId);
        id.setTxEmail(aggregatorId);
        aggregatorAppProviderDao.create(object);
    }
}
