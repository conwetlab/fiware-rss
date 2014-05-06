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

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import es.tid.fiware.rss.model.RSSFile;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:database.xml", "/META-INF/spring/application-context.xml",
    "/META-INF/spring/cxf-beans.xml"})
public class SettlementManagerTest {

    @Autowired
    private SettlementManager settlementManager;

    @Test
    public void runSettlementTest() {
        try {
            settlementManager.runSettlement("2013-01", "2013-12", null, null);
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void getSettlementFilesTest() {
        List<RSSFile> fileList = settlementManager.getSettlementFiles(null);
        if (fileList.isEmpty()) {
            Assert.fail("No files returned");
        } else if (fileList.size() == 1) {
            if (fileList.get(0).getTxName().equals("There are not RSS Files generated for you at this moment")) {
                Assert.fail("There are not RSS Files generated for you at this moment");
            }
        }
    }

}
