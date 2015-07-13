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

import es.tid.fiware.rss.dao.DbeAggregatorDao;
import es.tid.fiware.rss.exception.RSSException;
import es.tid.fiware.rss.model.Aggregator;
import es.tid.fiware.rss.model.DbeAggregator;
import java.util.ArrayList;
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
public class AggregatorManager {

    /***
     * Logging system.
     */
    private final Logger logger = LoggerFactory.getLogger(AggregatorManager.class);

    /**
     * 
     */
    @Autowired
    private DbeAggregatorDao aggregatorDao;

    /**
     * Creates a new aggregator.
     * 
     * @param aggregator
     * @throws IOException
     */
    public void createAggretator(Aggregator aggregator) throws Exception {
        logger.debug("Creating aggregator: {}", aggregator.getAggregatorId());

        DbeAggregator dbAggregator = new DbeAggregator();
        dbAggregator.setTxEmail(aggregator.getAggregatorId());
        dbAggregator.setTxName(aggregator.getAggregatorName());
        aggregatorDao.create(dbAggregator);
    }

    /**
     * Get existing aggregators from the DB in a format ready to be serialized
     * @return
     * @throws RSSException
     */
    public List<Aggregator> getAPIAggregators() throws RSSException {
            
        List<Aggregator> apiAggregators = new ArrayList<>();
        List<DbeAggregator> aggregators = this.getAggregators();

        for (DbeAggregator aggregator: aggregators) {
            Aggregator apiAggregator = new Aggregator();
            apiAggregator.setAggregatorId(aggregator.getTxEmail());
            apiAggregator.setAggregatorName(aggregator.getTxName());
            apiAggregators.add(apiAggregator);
        }
        return apiAggregators;
    }

    public List<DbeAggregator> getAggregators() throws RSSException {
        return aggregatorDao.getAll();
    }

    public Aggregator getAggregator(String aggregatorId) {
        DbeAggregator ag = aggregatorDao.getById(aggregatorId);
        Aggregator aggregator = new Aggregator();
        aggregator.setAggregatorId(aggregatorId);
        aggregator.setAggregatorName(ag.getTxName());

        return aggregator;
    }
}
