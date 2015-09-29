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

import es.upm.fiware.rss.model.RSSProvider;
import es.upm.fiware.rss.model.RSUser;
import es.upm.fiware.rss.service.ProviderManager;
import es.upm.fiware.rss.service.UserManager;
import java.util.LinkedList;
import java.util.List;
import java.util.MissingFormatArgumentException;
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
public class ProviderServiceTest {

    @Mock ProviderManager providerManager;
    @Mock UserManager userManager;
    @InjectMocks ProviderService toTest;

    public ProviderServiceTest() {
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createProviderTest() throws Exception {
        String aggregatorId = "aggregator@mail.com";
        String providerId = "provider@mail.com";
        String providerName = "providerName";

        RSSProvider provider = new RSSProvider();
        provider.setAggregatorId(aggregatorId);
        provider.setProviderId(providerId);
        provider.setProviderName(providerName);

        RSUser user = new RSUser();
        user.setDisplayName("username");
        user.setEmail("user@mail.com");

        when(userManager.getCurrentUser()).thenReturn(user);
        when(userManager.isAdmin()).thenReturn(true);

        toTest.createProvider(provider);
    }

    @Test
    (expected = MissingFormatArgumentException.class)
    public void createProviderNotAdminTest() throws Exception {
        String aggregatorId = "aggregator@mail.com";
        String providerId = "provider@mail.com";
        String providerName = "providerName";

        RSSProvider provider = new RSSProvider();
        provider.setAggregatorId(aggregatorId);
        provider.setProviderId(providerId);
        provider.setProviderName(providerName);

        RSUser user = new RSUser();
        user.setDisplayName("username");
        user.setEmail("user@mail.com");

        when(userManager.getCurrentUser()).thenReturn(user);
        when(userManager.isAdmin()).thenReturn(false);

        toTest.createProvider(provider);
    }

    @Test
    public void createProvider2Test() throws Exception {
        String aggregatorId = "aggregator@mail.com";
        String providerId = "provider@mail.com";
        String providerName = "providerName";

        RSSProvider provider = new RSSProvider();
        provider.setAggregatorId(aggregatorId);
        provider.setProviderId(providerId);
        provider.setProviderName(providerName);

        RSUser user = new RSUser();
        user.setDisplayName("username");
        user.setEmail(aggregatorId);

        when(userManager.getCurrentUser()).thenReturn(user);
        when(userManager.isAdmin()).thenReturn(true);

        toTest.createProvider(provider);
    }

    @Test
    public void getProvidersIsAdminTest() throws Exception {
        String queryId = "aggregator@mail.com";
        String userId = "user@mail.com";

        RSUser user = new RSUser();
        user.setDisplayName("username");
        user.setEmail(userId);

        List <RSSProvider> providers = new LinkedList<>();

        when(userManager.getCurrentUser()).thenReturn(user);
        when(userManager.isAdmin()).thenReturn(true);
        when(providerManager.getAPIProviders(queryId)).thenReturn(providers);

        toTest.getProviders(queryId);
    }

    @Test
    public void getProvidersNoAdmin1Test() throws Exception {
        String queryId = "aggregator@mail.com";
        String userId = "user@mail.com";

        RSUser user = new RSUser();
        user.setDisplayName("username");
        user.setEmail(userId);

        List <RSSProvider> providers = new LinkedList<>();

        when(userManager.getCurrentUser()).thenReturn(user);
        when(userManager.isAdmin()).thenReturn(false);
        when(providerManager.getAPIProviders(userId)).thenReturn(providers);

        toTest.getProviders(null);
    }

    @Test
    public void getProvidersNoAdmin2Test() throws Exception {
        String queryId = "aggregator@mail.com";
        String userId = "user@mail.com";

        RSUser user = new RSUser();
        user.setDisplayName("username");
        user.setEmail(queryId);

        List <RSSProvider> providers = new LinkedList<>();

        when(userManager.getCurrentUser()).thenReturn(user);
        when(userManager.isAdmin()).thenReturn(false);
        when(providerManager.getAPIProviders(queryId)).thenReturn(providers);

        toTest.getProviders(queryId);
    }

    @Test
    (expected = MissingFormatArgumentException.class)
    public void getProvidersNotAllowedTest() throws Exception {
        String queryId = "aggregator@mail.com";
        String userId = "user@mail.com";

        RSUser user = new RSUser();
        user.setDisplayName("username");
        user.setEmail(userId);

        List <RSSProvider> providers = new LinkedList<>();

        when(userManager.getCurrentUser()).thenReturn(user);
        when(userManager.isAdmin()).thenReturn(false);

        toTest.getProviders(queryId);
    }


}
