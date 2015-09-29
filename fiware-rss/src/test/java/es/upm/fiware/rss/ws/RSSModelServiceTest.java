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

package es.upm.fiware.rss.ws;

import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import es.upm.fiware.rss.model.RSSModel;
import es.upm.fiware.rss.model.RSUser;
import es.upm.fiware.rss.service.RSSModelsManager;
import es.upm.fiware.rss.service.UserManager;
import java.util.LinkedList;
import java.util.List;
import java.util.MissingFormatArgumentException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

/**
 *
 *
 */
public class RSSModelServiceTest {

    @Mock private RSSModelsManager rssModelsManager;
    @Mock private UserManager userManager;
    @InjectMocks private RSSModelService toTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createRSSModelTest() throws Exception {
        RSSModel model = new RSSModel();
        model.setAggregatorId("aggregator@mail.com");
        RSSModel returnedModel = new RSSModel();

        RSUser user = new RSUser();
        user.setDisplayName("username");
        user.setEmail("user@mail.com");

        when(userManager.isAdmin()).thenReturn(true);
        when(userManager.getCurrentUser()).thenReturn(user);
        when(rssModelsManager.createRssModel(model)).thenReturn(returnedModel);

        Response response = toTest.createRSSModel(model);
        Assert.assertEquals(returnedModel, response.getEntity());
    }

    @Test
    (expected = MissingFormatArgumentException.class)
    public void createRSSModelNotAllowedTest() throws Exception {
        RSSModel model = new RSSModel();
        model.setAggregatorId("aggregator@mail.com");
        RSSModel returnedModel = new RSSModel();

        RSUser user = new RSUser();
        user.setDisplayName("username");
        user.setEmail("user@mail.com");

        when(userManager.isAdmin()).thenReturn(false);
        when(userManager.getCurrentUser()).thenReturn(user);
        when(rssModelsManager.createRssModel(model)).thenReturn(returnedModel);

        toTest.createRSSModel(model);
    }

    @Test
    public void deleteRSSModelTest() throws Exception {
        String appProviderId = "provider@mail.com";
        String productClass = "productClass";

        RSUser user = new RSUser();
        user.setDisplayName("username");
        user.setEmail("user@mail.com");

        when(userManager.getCurrentUser()).thenReturn(user);

        toTest.deleteRSSModel(null, appProviderId, productClass);


    }

    @Test
    public void getRssModelsTest() throws Exception {
        String appProviderId = "provider@mail.com";
        String productClass = "productClass";
        String aggregatorId = "aggregator@mail.com";

        RSUser user = new RSUser();
        user.setDisplayName("username");
        user.setEmail("user@mail.com");

        List<RSSModel> rssModels = new LinkedList<>();

        when(userManager.getCurrentUser()).thenReturn(user);
        when(userManager.isAdmin()).thenReturn(true);
        when(rssModelsManager.getRssModels(aggregatorId, appProviderId, productClass)).thenReturn(rssModels);

        Response response = toTest.getRssModels(appProviderId, productClass, aggregatorId);
        Assert.assertEquals(rssModels, response.getEntity());
    }

    @Test
    public void getRssModels2Test() throws Exception {
        String appProviderId = "provider@mail.com";
        String productClass = "productClass";
        String aggregatorId = "aggregator@mail.com";

        RSUser user = new RSUser();
        user.setDisplayName("username");
        user.setEmail(aggregatorId);

        List<RSSModel> rssModels = new LinkedList<>();

        when(userManager.getCurrentUser()).thenReturn(user);
        when(userManager.isAdmin()).thenReturn(false);
        when(rssModelsManager.getRssModels(aggregatorId, appProviderId, productClass)).thenReturn(rssModels);

        Response response = toTest.getRssModels(appProviderId, productClass, aggregatorId);
        Assert.assertEquals(rssModels, response.getEntity());
    }

    @Test
    (expected = MissingFormatArgumentException.class)
    public void getRssModelsNotAllowedTest() throws Exception {
        String appProviderId = "provider@mail.com";
        String productClass = "productClass";
        String aggregatorId = "aggregator@mail.com";

        RSUser user = new RSUser();
        user.setDisplayName("username");
        user.setEmail("user@mail.com");

        List<RSSModel> rssModels = new LinkedList<>();

        when(userManager.getCurrentUser()).thenReturn(user);
        when(userManager.isAdmin()).thenReturn(false);
        when(rssModelsManager.getRssModels(aggregatorId, appProviderId, productClass)).thenReturn(rssModels);

        toTest.getRssModels(appProviderId, productClass, aggregatorId);
    }

    @Test
    public void modifyRSSModelTest() throws Exception {
        RSSModel model = new RSSModel();
        model.setAggregatorId("user@mail.com");

        RSUser user = new RSUser();
        user.setDisplayName("username");
        user.setEmail("user@mail.com");

        when(userManager.getCurrentUser()).thenReturn(user);
        when(userManager.isAdmin()).thenReturn(true);
        when(rssModelsManager.updateRssModel(model)).thenReturn(model);

        toTest.modifyRSSModel(model);
    }

    @Test
    public void modifyRSSModel2Test() throws Exception {
        RSSModel model = new RSSModel();
        model.setAggregatorId("user@mail.com");

        RSUser user = new RSUser();
        user.setDisplayName("username");
        user.setEmail("user@mail.com");

        when(userManager.getCurrentUser()).thenReturn(user);
        when(userManager.isAdmin()).thenReturn(false);
        when(rssModelsManager.updateRssModel(model)).thenReturn(model);

        toTest.modifyRSSModel(model);
    }

    @Test
    (expected = MissingFormatArgumentException.class)
    public void modifyRSSModelNotAllowedTest() throws Exception {
        RSSModel model = new RSSModel();
        model.setAggregatorId("aggregator@mail.com");

        RSUser user = new RSUser();
        user.setDisplayName("username");
        user.setEmail("user@mail.com");

        when(userManager.getCurrentUser()).thenReturn(user);
        when(userManager.isAdmin()).thenReturn(false);
        when(rssModelsManager.updateRssModel(model)).thenReturn(model);

        toTest.modifyRSSModel(model);
    }
}
