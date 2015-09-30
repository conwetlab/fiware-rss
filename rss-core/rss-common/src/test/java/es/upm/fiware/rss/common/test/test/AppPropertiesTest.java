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

package es.upm.fiware.rss.common.test.test;

import org.junit.Assert;
import org.junit.Test;

import es.upm.fiware.rss.common.properties.AppProperties;


public class AppPropertiesTest {

    public void getProperty() {
        String prop = AppProperties.getProperty("database.properties", "database.test.url");
        Assert.assertTrue(prop.startsWith("jdbc"));
    }

    public void nullProperties() {
        AppProperties props = new AppProperties("database.properties");
        String prop = props.getProperty("database.test.url");
        Assert.assertTrue(prop.startsWith("jdbc"));
        props.setProps(null);
        prop = props.getProperty("database.test.url");
        Assert.assertTrue(prop.startsWith("jdbc"));
    }

    public void getFilenaName() {
        AppProperties props = new AppProperties("database.properties");
        Assert.assertTrue(props.getFilename().equalsIgnoreCase("database.properties"));
        Assert.assertNotNull("Props can not be null", props.getProps());
    }

}
