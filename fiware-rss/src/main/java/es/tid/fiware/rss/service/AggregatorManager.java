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
import es.tid.fiware.rss.exception.UNICAExceptionType;
import es.tid.fiware.rss.model.Aggregator;
import es.tid.fiware.rss.model.DbeAggregator;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
@Transactional(rollbackFor = Exception.class)
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

    private boolean isValidEmail(String email) {
        String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
		+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    private List<DbeAggregator> getAggregators() {
        return aggregatorDao.getAll();
    }

    /**
     * Creates a new aggregator.
     * 
     * @param aggregator, Aggregator instance
     * @throws RSSException, If the aggregator info is not valid
     */
    public void createAggretator(Aggregator aggregator) throws RSSException {
        logger.debug("Creating aggregator: {}", aggregator.getAggregatorId());

        // Validate aggregator fields
        if (aggregator.getAggregatorId() == null || aggregator.getAggregatorId().isEmpty()) {
            String[] args = {"AggregatorID field is required for creating an aggregator"};
            throw new RSSException(UNICAExceptionType.MISSING_MANDATORY_PARAMETER, args);
        }

        if (!this.isValidEmail(aggregator.getAggregatorId())) {
            String[] args = {"AggregatorID field must be an email identifiying a valid Store owner"};
            throw new RSSException(UNICAExceptionType.INVALID_PARAMETER, args);
        }

        if (aggregator.getAggregatorName() == null || aggregator.getAggregatorName().isEmpty()) {
            String[] args = {"AggregatorName field is required for creating an aggregator"};
            throw new RSSException(UNICAExceptionType.MISSING_MANDATORY_PARAMETER, args);
        }

        // Build new aggregator object
        DbeAggregator dbAggregator = new DbeAggregator();
        dbAggregator.setTxEmail(aggregator.getAggregatorId());
        dbAggregator.setTxName(aggregator.getAggregatorName());

        // Save aggregator to the DB
        try {
            this.aggregatorDao.create(dbAggregator);
        } catch (org.hibernate.NonUniqueObjectException e) {
            String[] args = {"The given aggregator already exists"};
            throw new RSSException(UNICAExceptionType.RESOURCE_ALREADY_EXISTS, args);
        }
    }

    /**
     * Get existing aggregators from the DB in a format ready to be serialized
     * @return, A list of Aggregator instances with the information of the
     * existing aggregators
     */
    public List<Aggregator> getAPIAggregators() {
            
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

    /**
     * Retrieves an aggregator object using its id
     * @param aggregatorId, identifier of the aggregator to be retrieved
     * @return An Aggregator instance with the information of the specified
     * aggregator
     * @throws RSSException, if the specified aggrgator does not exists
     */
    public Aggregator getAggregator(String aggregatorId) throws RSSException {
        DbeAggregator ag = aggregatorDao.getById(aggregatorId);

        if (ag == null) {
            String[] args = {aggregatorId};
            throw new RSSException(UNICAExceptionType.NON_EXISTENT_RESOURCE_ID, args);
        }

        Aggregator aggregator = new Aggregator();
        aggregator.setAggregatorId(aggregatorId);
        aggregator.setAggregatorName(ag.getTxName());

        return aggregator;
    }
}
