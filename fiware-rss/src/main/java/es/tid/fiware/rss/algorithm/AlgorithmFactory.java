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
package es.tid.fiware.rss.algorithm;

import es.tid.fiware.rss.algorithm.impl.FixedPercentageProcessor;
import es.tid.fiware.rss.exception.RSSException;
import es.tid.fiware.rss.exception.UNICAExceptionType;

/**
 *
 * @author fdelavega
 */
public class AlgorithmFactory {

    public AlgorithmProcessor getAlgorithmProcessor(String algorithmType) 
        throws RSSException{

        AlgorithmProcessor processor = null;

        if (algorithmType.equals("FIXED_PERCENTAGE")) {
            processor = new FixedPercentageProcessor();
        } else {
            // There is no implementation for the given algorithm
            String[] args = {"No implementation found for the algorithm: " + algorithmType};
            throw new RSSException(UNICAExceptionType.NON_EXISTENT_RESOURCE_ID, args);
        }

        return processor;
    }
}
