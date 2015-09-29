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
package es.upm.fiware.rss.ws;

import es.upm.fiware.rss.model.Aggregator;
import es.upm.fiware.rss.model.RSUser;
import es.upm.fiware.rss.service.AggregatorManager;
import es.upm.fiware.rss.service.UserManager;
import java.util.LinkedList;
import java.util.List;
import java.util.MissingFormatArgumentException;
import javax.ws.rs.core.Response;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

/**
 *
 * @author jortiz
 */
public class AggregatorServiceTest {

    @Mock UserManager userManager;
    @Mock AggregatorManager aggregatorManager;
    @InjectMocks AggregatorService toTest;

    public AggregatorServiceTest() {}

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createAggregatorTest() throws Exception {

        when(userManager.isAdmin()).thenReturn(true);

        Aggregator aggregator = new Aggregator();
        aggregator.setAggregatorId("aggregator@mail.com");
        aggregator.setAggregatorName("aggregatorName");

        toTest.createAggregator(aggregator);
    }

    @Test
    (expected = MissingFormatArgumentException.class)
    public void createAggregatorNotAdminTest() throws Exception {

        when(userManager.isAdmin()).thenReturn(false);

        Aggregator aggregator = new Aggregator();
        aggregator.setAggregatorId("aggregator@mail.com");
        aggregator.setAggregatorName("aggregatorName");

        toTest.createAggregator(aggregator);
    }

    @Test
    public void getAggregatorsTest() throws Exception {

        List <Aggregator> aggregators = new LinkedList<>();

        Aggregator aggregator = new Aggregator();
        aggregator.setAggregatorId("aggregator@mail.com");
        aggregator.setAggregatorName("aggregatorName");

        aggregators.add(aggregator);

        when(userManager.isAdmin()).thenReturn(true);
        when(aggregatorManager.getAPIAggregators()).thenReturn(aggregators);

        Response response = toTest.getAggregators();
    }

    @Test
    public void getAggregatorsNotAdminTest() throws Exception {

        Aggregator aggregator = new Aggregator();
        aggregator.setAggregatorId("aggregator@mail.com");
        aggregator.setAggregatorName("aggregatorName");

        RSUser user = new RSUser();
        user.setEmail("user@mail.com");

        when(userManager.isAdmin()).thenReturn(false);
        when(userManager.getCurrentUser()).thenReturn(user);
        when(aggregatorManager.getAggregator("user@mail.com")).thenReturn(aggregator);

        Response response = toTest.getAggregators();
    }

}
