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

package es.tid.fiware.rss.common.test.test;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import es.tid.fiware.rss.common.properties.AppProperties;

/**
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:database.xml" })
public class AppPropertiesTest {

    /**
     * Properties for database connection.
     */
    @Autowired
    @Qualifier("databaseProperties")
    private AppProperties databaseProp;

    @Test
    public void getProperty() {
        String prop = AppProperties.getProperty("database.properties", "database.url");
        Assert.assertTrue(prop.startsWith("jdbc"));
    }

    @Test
    public void nullProperties() {
        AppProperties props = new AppProperties("database.properties");
        String prop = props.getProperty("database.url");
        Assert.assertTrue(prop.startsWith("jdbc"));
        props.setProps(null);
        prop = props.getProperty("database.url");
        Assert.assertTrue(prop.startsWith("jdbc"));
    }

}
