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

package es.upm.fiware.rss.common.test;

import java.net.URL;

import javax.sql.DataSource;

import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.dbunit.ext.mysql.MySqlMetadataHandler;
import org.dbunit.operation.DatabaseOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.DataSourceUtils;

/**
 * The Class DatabaseLoader.
 */
public class DatabaseLoader {

    /**
     * Logging system.
     */
    private static Logger logger = LoggerFactory.getLogger(DatabaseLoader.class);

    // Get database schema from properties
    private @Value("${database.test.schema}") String schema;

    /**
     * For database access.
     */
    @Autowired
    private DataSource dataSource;

    /** The db conn. */
    private IDatabaseConnection dbConn;

    /** The loader. */
    private FlatXmlDataSetBuilder loader;

    /**
     * Clean insert.
     * 
     * @param resource
     *            the resource
     * @param commit
     *            the commit
     * @throws Exception
     *             the exception
     */
    public void cleanInsert(String resource, boolean commit) throws Exception {
        DatabaseLoader.logger.debug("cleanInsert: " + resource);
        URL url = this.getClass().getClassLoader().getResource(resource);
        DatabaseLoader.logger.debug("Resource url: " + url.getPath());
        IDataSet ds = loader.build(url);
        DatabaseOperation.CLEAN_INSERT.execute(dbConn, ds);

        if (commit) {
            dbConn.getConnection().commit();
        }

    }

    public void execute(String sql, boolean commit) throws Exception {
        dbConn.getConnection().createStatement().execute(sql);
        if (commit) {
            dbConn.getConnection().commit();
        }
    }

    /**
     * Delete all.
     * 
     * @param resource
     *            the resource
     * @param commit
     *            the commit
     * @throws Exception
     *             the exception
     */
    public void deleteAll(String resource, boolean commit) throws Exception {
        DatabaseLoader.logger.debug("deleteAll: " + resource);
        URL url = this.getClass().getClassLoader().getResource(resource);
        IDataSet ds = loader.build(url);
        DatabaseOperation.DELETE_ALL.execute(dbConn, ds);

        if (commit) {
            dbConn.getConnection().commit();
        }
    }

    /**
     * Method to insert data before test. throws Exception from DB.
     * 
     * @throws Exception
     *             the exception
     */
    public void init() throws Exception {
        DatabaseLoader.logger.debug("DatabaseLoader.init()");

        try {
            DatabaseLoader.logger.debug("starting init() in DatabaseLoader...");

            this.dbConn = new DatabaseConnection(DataSourceUtils.getConnection(dataSource), this.schema);

            DatabaseConfig config = dbConn.getConfig();
            config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new MySqlDataTypeFactory());
            config.setProperty(DatabaseConfig.PROPERTY_METADATA_HANDLER, new MySqlMetadataHandler());
            config.setProperty(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, Boolean.TRUE);
            DatabaseLoader.logger.debug("DATASOURCE." + dataSource.toString());
            this.loader = new FlatXmlDataSetBuilder();

            dbConn.getConnection().createStatement().execute("DELETE FROM dbe_transaction");
            deleteAll("dbunit/CREATE_DATATEST_III.xml", false);
            deleteAll("dbunit/CREATE_DATATEST_II.xml", false);
            deleteAll("dbunit/CREATE_DATATEST_I.xml", false);

            cleanInsert("dbunit/CREATE_DATATEST_I.xml", false);
            cleanInsert("dbunit/CREATE_DATATEST_II.xml", false);
            cleanInsert("dbunit/CREATE_DATATEST_III.xml", true);

            DatabaseLoader.logger.debug("ending init() in DatabaseLoader...");

        } catch (Exception e) {
            DatabaseLoader.logger.error("Error loading data init in DatabaseLoader");
            dbConn.getConnection().rollback();
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Method to remove data after test. throws Exception from DB.
     * 
     * @throws Exception
     *             the exception
     */
    public void cleanup() throws Exception {
        try {
            DatabaseLoader.logger.debug("starting cleanup of DatabaseLoader...");
            deleteAll("dbunit/CREATE_DATATEST_III.xml", false);
            deleteAll("dbunit/CREATE_DATATEST_II.xml", false);
            deleteAll("dbunit/CREATE_DATATEST_I.xml", true);

            DatabaseLoader.logger.debug("ending cleanup() of DatabaseLoader...");

            dbConn.close();

        } catch (Exception e) {
            DatabaseLoader.logger.error("Error loading data cleanup in DatabaseLoader");
            dbConn.getConnection().rollback();
            e.printStackTrace();
            throw e;
        }
    }

}