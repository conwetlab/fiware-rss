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

import es.tid.fiware.rss.common.test.DatabaseLoader;
import es.tid.fiware.rss.dao.DbeAggregatorDao;
import es.tid.fiware.rss.exception.RSSException;
import es.tid.fiware.rss.model.Aggregator;
import es.tid.fiware.rss.model.DbeAggregator;
import java.util.List;
import org.hibernate.NonUniqueObjectException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import static org.mockito.Matchers.*;
import org.mockito.Mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author jortiz
 */

public class AggregatorManagerTest {

    @Mock private Logger loggerMock;
    @Mock private DbeAggregatorDao dbeAggregatorDaoMock;
    @InjectMocks private AggregatorManager toTest;


    public AggregatorManagerTest() {
    }

    @Before
    public void setUp() throws Exception {
	MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void createAggretatorTest() throws RSSException {

        Aggregator aggregator = new Aggregator();
        aggregator.setAggregatorId("aggregator@id.com");
        aggregator.setAggregatorName("aggregatorName");

        toTest.createAggretator(aggregator);
    }

    @Test
    (expected = RSSException.class)
    public void createAggretatorRSSExceptionNotIDTest() throws RSSException {
        Aggregator aggregator = new Aggregator();
        aggregator.setAggregatorName("aggregatorName");

        toTest.createAggretator(aggregator);
    }

    @Test
    (expected = RSSException.class)
    public void createAggretatorRSSExceptionBadIDTest() throws RSSException {
        Aggregator aggregator = new Aggregator();
        aggregator.setAggregatorId("aggregatorID");
        aggregator.setAggregatorName("aggregatorName");

        toTest.createAggretator(aggregator);
    }

    @Test
    (expected = RSSException.class)
    public void createAggretatorRSSExceptionNotNameTest() throws RSSException {
        Aggregator aggregator = new Aggregator();
        aggregator.setAggregatorId("aggregator@id.com");

        toTest.createAggretator(aggregator);
    }

    @Test
    (expected = RSSException.class)
    public void createAggretatorRSSExceptionAlreadyExistsTest() throws RSSException {
        Aggregator aggregator = new Aggregator();
        aggregator.setAggregatorId("aggregator@id.com");
        aggregator.setAggregatorName("aggregatorName");

        doThrow(NonUniqueObjectException.class).when(dbeAggregatorDaoMock).create(any(DbeAggregator.class));

        toTest.createAggretator(aggregator);
    }

    @Test
    public void getAPIAggregatorsTest() throws RSSException {
        List <Aggregator> returned = toTest.getAPIAggregators();

        Assert.assertEquals(0, returned.size());
    }

    @Test
    public void getAPIAggregatorsVoidTest() {

        List <Aggregator> returned = toTest.getAPIAggregators();

        Assert.assertTrue(returned.isEmpty());
    }

    @Test
    public void getAggregatorTest() throws RSSException {
        String aggregatorID = "aggregator@id.com";

        Aggregator aggregator = new Aggregator();
        aggregator.setAggregatorId(aggregatorID);
        aggregator.setAggregatorName("aggregatorName");

        DbeAggregator dbeAggregator = new DbeAggregator("aggregatorName", aggregatorID);

        when(dbeAggregatorDaoMock.getById(aggregatorID)).thenReturn(dbeAggregator);

        Aggregator returned = toTest.getAggregator(aggregatorID);

        Assert.assertEquals(aggregator.getAggregatorId(), returned.getAggregatorId());
        Assert.assertEquals(aggregator.getAggregatorName(), returned.getAggregatorName());
    }

    @Test
    (expected = RSSException.class)
    public void getAggregatorRSSExceptionTest() throws RSSException {
        String aggregatorID = "aggregator@id.com";

        toTest.getAggregator(aggregatorID);
    }

}
