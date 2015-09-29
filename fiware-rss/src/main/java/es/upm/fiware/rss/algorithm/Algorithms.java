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

package es.upm.fiware.rss.algorithm;

import es.upm.fiware.rss.algorithm.impl.FixedPercentageProcessor;

/**
 *
 * @author fdelavega
 */
public enum Algorithms {
    // Existing algorithms declaration
    FIXED_PERCENTAGE(FixedPercentageProcessor.class, "Fixed percentage distribution of revenues");

    // ================================
    private final Class processor;
    private final String description;

    Algorithms(Class processor, String description) {
        this.processor = processor;
        this.description = description; 
    }

    public Class getProcessor() {
        return processor;
    }

    public String getDescription() {
        return description;
    }
}
