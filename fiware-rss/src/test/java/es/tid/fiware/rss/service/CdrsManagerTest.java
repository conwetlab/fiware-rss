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

package es.tid.fiware.rss.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import es.tid.fiware.rss.common.test.DatabaseLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:database.xml", "/META-INF/spring/application-context.xml",
    "/META-INF/spring/cxf-beans.xml"})
public class CdrsManagerTest {
    /**
     * 
     */
    @Autowired
    private CdrsManager cdrsManager;
    /**
     * 
     */
    @Autowired
    private DatabaseLoader databaseLoader;

    /**
     * Method to insert data before test.
     * 
     * @throws Exception
     *             from dbb
     */
    @Before
    public void setUp() throws Exception {
        databaseLoader.cleanInsert("dbunit/CREATE_DATATEST_TRANSACTIONS.xml", true);
    }

    /**
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        databaseLoader.deleteAll("dbunit/CREATE_DATATEST_TRANSACTIONS.xml", true);
    }

    /**
     * 
     */
    @Test
    public void getCdrFileTest() {
        File cdrFile;
        try {
            cdrFile = cdrsManager.getFile();
            Assert.assertNotNull(cdrFile);
            Assert.assertTrue(cdrFile.exists());

        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }

    }

    /**
     * Test runCdrToDB method.
     */
    @Test
    public void runCdrToDB() throws IOException, InterruptedException {
        Process p = Mockito.mock(Process.class);
        Runtime runtime = Mockito.mock(Runtime.class);
        Mockito.when(runtime.exec(Matchers.any(String.class))).thenReturn(p);
        ReflectionTestUtils.setField(cdrsManager, "runtime", runtime);
        InputStream inputStream = new ByteArrayInputStream("OK".getBytes());
        Mockito.when(p.getInputStream()).thenReturn(inputStream);
        Mockito.when(p.waitFor()).thenReturn(0);
        String result = cdrsManager.runCdrToDB();
        Assert.assertNull(result);
        // Error
        inputStream = new ByteArrayInputStream("ERROR".getBytes());
        Mockito.when(p.getInputStream()).thenReturn(inputStream);
        Mockito.when(p.waitFor()).thenReturn(-1);
        result = cdrsManager.runCdrToDB();
        Assert.assertEquals("ERROR", result);
    }
}
