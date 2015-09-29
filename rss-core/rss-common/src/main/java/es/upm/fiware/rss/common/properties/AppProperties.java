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

package es.upm.fiware.rss.common.properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class AppProperties.
 */
public class AppProperties {

    /**
     * Variable to print the trace.
     */
    private static Logger logger = LoggerFactory.getLogger(AppProperties.class);

    /**
     * Filename of properties.
     */
    private final String filename;

    /**
     * Filename of properties.
     */
    private Properties props;

    /**
     * 
     * @param filename
     *            name of properties file
     */
    public AppProperties(final String filename) {
        this.filename = filename;
        this.loadProperties();
    }

    /**
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @return the props
     */
    public Properties getProps() {
        return props;
    }

    /**
     * @param props
     *            the props to set
     */
    public void setProps(final Properties props) {
        this.props = props;
    }

    /**
     * @return props
     */
    public Properties loadProperties() {
        AppProperties.logger.debug("Into loadProperties()");
        props = AppProperties.loadProperties(filename);
        return props;
    }

    /**
     * 
     * @param key
     *            key for retrieve value
     * @return string
     */
    public String getProperty(final String key) {
        AppProperties.logger.debug("Into getProperty()");
        if (props == null) {
            this.loadProperties();
        }
        return props.getProperty(key);
    }

    /**
     * 
     * @param filename
     *            name of properties file
     * @return prop
     */
    public static Properties loadProperties(final String filename) {
        AppProperties.logger.debug("Into AppProperties.loadProperties() " + filename);

        Properties prop = new Properties();
        try {
            InputStream input = new FileInputStream(filename);
            prop.load(input);
        } catch (IOException ioe) {
            AppProperties.logger.error("Error >>>>>>> " + ioe.getMessage(), ioe);
        }
        return prop;
    }

    /**
     * 
     * @param filename
     *            name of properties file
     * @param key
     *            key for retrieve value
     * @return string
     */
    public static String getProperty(final String filename, final String key) {
        AppProperties.logger.debug("Into AppProperties.getProperty()");
        return AppProperties.loadProperties(filename).getProperty(key);
    }
}
