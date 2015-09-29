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

package es.upm.fiware.rss.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import es.upm.fiware.rss.dao.CurrencyDao;
import es.upm.fiware.rss.dao.DbeAggregatorDao;
import es.upm.fiware.rss.dao.DbeAppProviderDao;
import es.upm.fiware.rss.dao.DbeTransactionDao;
import es.upm.fiware.rss.exception.RSSException;
import es.upm.fiware.rss.model.BmCurrency;
import es.upm.fiware.rss.model.CDR;
import es.upm.fiware.rss.model.DbeAggregator;
import es.upm.fiware.rss.model.DbeAppProvider;
import es.upm.fiware.rss.model.RSUser;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;


public class CdrsManagerTest {
    /**
     *
     */
    @Mock UserManager userManagerMock;
    @Mock DbeAggregatorDao dbeAggregatorDaoMock;
    @Mock RSSModelsManager modelsManagerMock;
    @Mock DbeAppProviderDao dbeAppProviderMock;
    @Mock CurrencyDao currencyDao;
    @Mock Properties rssProps;
    @Mock Logger logger;
    @Mock DbeTransactionDao transactionDao;
    @InjectMocks private CdrsManager cdrsManager;

    /**
     * Method to insert data before test.
     *
     * @throws Exception
     *             from dbb
     */
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void createCDRsRSS() throws RSSException {
        List <CDR> cdrs = new LinkedList<>();
        RSUser rSUser = new RSUser();
        rSUser.setEmail("mail@mail.com");

        Date preDate = new Date();

        CDR cdr1 = new CDR();
        cdr1.setAppProvider("appProvider1");
        cdr1.setApplication("application1");
        cdr1.setCdrSource("mail@mail.com");
        cdr1.setChargedAmount(BigDecimal.ZERO);
        cdr1.setChargedTaxAmount(BigDecimal.TEN);
        cdr1.setCorrelationNumber(Integer.MIN_VALUE);
        cdr1.setCurrency("currency1");
        cdr1.setCustomerId("customerId1");
        cdr1.setDescription("description1");
        cdr1.setEvent("event1");
        cdr1.setProductClass("productClass1");
        cdr1.setReferenceCode("referenceCode1");
        cdr1.setTimestamp(new Date());
        cdr1.setTransactionType("C");

        cdrs.add(cdr1);

        when(userManagerMock.isAdmin()).thenReturn(true);
        when(userManagerMock.getCurrentUser()).thenReturn(rSUser);
        when(dbeAggregatorDaoMock.getById("mail@mail.com")).thenReturn(new DbeAggregator("name", "mail@mail.com"));

        DbeAppProvider dbeAppProvider = mock(DbeAppProvider.class);
        when(dbeAppProviderMock.getProvider("mail@mail.com", "appProvider1")).thenReturn(dbeAppProvider);
        when(currencyDao.getByIso4217StringCode("currency1")).thenReturn(new BmCurrency());
        when(dbeAppProvider.getTxCorrelationNumber()).thenReturn(cdr1.getCorrelationNumber());

        when(dbeAppProvider.getTxTimeStamp()).thenReturn(preDate);

        cdrsManager.createCDRs(cdrs);
    }

    @Test
    (expected = RSSException.class)
    public void createCDRsRSSExceptionNotAllowed2Test() throws RSSException {
        List <CDR> cdrs = new LinkedList<>();
        RSUser rSUser = new RSUser();
        rSUser.setEmail("mail@mails.com");

        CDR cdr1 = new CDR();
        cdr1.setAppProvider("appProvider1");
        cdr1.setApplication("application1");
        cdr1.setCdrSource("mail@mail.com");
        cdr1.setChargedAmount(BigDecimal.ZERO);
        cdr1.setChargedTaxAmount(BigDecimal.TEN);
        cdr1.setCorrelationNumber(Integer.MIN_VALUE);
        cdr1.setCurrency("currency1");
        cdr1.setCustomerId("customerId1");
        cdr1.setDescription("description1");
        cdr1.setEvent("event1");
        cdr1.setProductClass("productClass1");
        cdr1.setReferenceCode("referenceCode1");
        cdr1.setTimestamp(new Date());
        cdr1.setTransactionType("C");

        cdrs.add(cdr1);

        when(userManagerMock.isAdmin()).thenReturn(true);
        when(userManagerMock.getCurrentUser()).thenReturn(rSUser);

        cdrsManager.createCDRs(cdrs);
    }

    @Test
    (expected = RSSException.class)
    public void createCDRsRSSExceptionNonExistentResourceTest() throws RSSException {
        List <CDR> cdrs = new LinkedList<>();
        RSUser rSUser = new RSUser();
        rSUser.setEmail("mail@mail.com");

        Date preDate = new Date();

        CDR cdr1 = new CDR();
        cdr1.setAppProvider("appProvider1");
        cdr1.setApplication("application1");
        cdr1.setCdrSource("mail@mail.com");
        cdr1.setChargedAmount(BigDecimal.ZERO);
        cdr1.setChargedTaxAmount(BigDecimal.TEN);
        cdr1.setCorrelationNumber(Integer.MIN_VALUE);
        cdr1.setCurrency("currency1");
        cdr1.setCustomerId("customerId1");
        cdr1.setDescription("description1");
        cdr1.setEvent("event1");
        cdr1.setProductClass("productClass1");
        cdr1.setReferenceCode("referenceCode1");
        cdr1.setTimestamp(new Date());
        cdr1.setTransactionType("C");

        cdrs.add(cdr1);

        when(userManagerMock.isAdmin()).thenReturn(true);
        when(userManagerMock.getCurrentUser()).thenReturn(rSUser);
        when(dbeAggregatorDaoMock.getById("mail@mail.com")).thenReturn(null);

        cdrsManager.createCDRs(cdrs);
    }

    @Test
    (expected = RSSException.class)
    public void createCDRsRSSExceptionNonExistentResource2Test() throws RSSException {
        List <CDR> cdrs = new LinkedList<>();
        RSUser rSUser = new RSUser();
        rSUser.setEmail("mail@mail.com");

        Date preDate = new Date();

        CDR cdr1 = new CDR();
        cdr1.setAppProvider("appProvider1");
        cdr1.setApplication("application1");
        cdr1.setCdrSource("mail@mail.com");
        cdr1.setChargedAmount(BigDecimal.ZERO);
        cdr1.setChargedTaxAmount(BigDecimal.TEN);
        cdr1.setCorrelationNumber(Integer.MIN_VALUE);
        cdr1.setCurrency("currency1");
        cdr1.setCustomerId("customerId1");
        cdr1.setDescription("description1");
        cdr1.setEvent("event1");
        cdr1.setProductClass("productClass1");
        cdr1.setReferenceCode("referenceCode1");
        cdr1.setTimestamp(new Date());
        cdr1.setTransactionType("C");

        cdrs.add(cdr1);

        when(userManagerMock.isAdmin()).thenReturn(true);
        when(userManagerMock.getCurrentUser()).thenReturn(rSUser);
        when(dbeAggregatorDaoMock.getById("mail@mail.com")).thenReturn(null);

        DbeAppProvider dbeAppProvider = mock(DbeAppProvider.class);
        when(dbeAppProviderMock.getProvider("mail@mail.com", "appProvider1")).thenReturn(dbeAppProvider);
        when(currencyDao.getByIso4217StringCode("currency1")).thenReturn(null);

        cdrsManager.createCDRs(cdrs);
    }

    @Test
    (expected = RSSException.class)
    public void createCDRsRSSExceptionNonInvalidParameterTest() throws RSSException {
        List <CDR> cdrs = new LinkedList<>();
        RSUser rSUser = new RSUser();
        rSUser.setEmail("mail@mail.com");

        CDR cdr1 = new CDR();
        cdr1.setAppProvider("appProvider1");
        cdr1.setApplication("application1");
        cdr1.setCdrSource("mail@mail.com");
        cdr1.setChargedAmount(BigDecimal.ZERO);
        cdr1.setChargedTaxAmount(BigDecimal.TEN);
        cdr1.setCorrelationNumber(Integer.MIN_VALUE);
        cdr1.setCurrency("currency1");
        cdr1.setCustomerId("customerId1");
        cdr1.setDescription("description1");
        cdr1.setEvent("event1");
        cdr1.setProductClass("productClass1");
        cdr1.setReferenceCode("referenceCode1");
        cdr1.setTimestamp(new Date());
        cdr1.setTransactionType("C");

        cdrs.add(cdr1);

        when(userManagerMock.isAdmin()).thenReturn(true);
        when(userManagerMock.getCurrentUser()).thenReturn(rSUser);
        when(dbeAggregatorDaoMock.getById("mail@mail.com")).thenReturn(null);

        DbeAppProvider dbeAppProvider = mock(DbeAppProvider.class);
        when(dbeAppProviderMock.getProvider("mail@mail.com", "appProvider1")).thenReturn(dbeAppProvider);
        when(currencyDao.getByIso4217StringCode("currency1")).thenReturn(new BmCurrency());
        when(dbeAppProvider.getTxCorrelationNumber()).thenReturn(500);

        cdrsManager.createCDRs(cdrs);
    }

    @Test
    (expected = RSSException.class)
    public void createCDRsRSSExceptionNonInvalidParameter2Test() throws RSSException {
                List <CDR> cdrs = new LinkedList<>();
        RSUser rSUser = new RSUser();
        rSUser.setEmail("mail@mail.com");

        CDR cdr1 = new CDR();
        cdr1.setAppProvider("appProvider1");
        cdr1.setApplication("application1");
        cdr1.setCdrSource("mail@mail.com");
        cdr1.setChargedAmount(BigDecimal.ZERO);
        cdr1.setChargedTaxAmount(BigDecimal.TEN);
        cdr1.setCorrelationNumber(Integer.MIN_VALUE);
        cdr1.setCurrency("currency1");
        cdr1.setCustomerId("customerId1");
        cdr1.setDescription("description1");
        cdr1.setEvent("event1");
        cdr1.setProductClass("productClass1");
        cdr1.setReferenceCode("referenceCode1");
        cdr1.setTimestamp(new Date());
        cdr1.setTransactionType("C");

        cdrs.add(cdr1);

        when(userManagerMock.isAdmin()).thenReturn(true);
        when(userManagerMock.getCurrentUser()).thenReturn(rSUser);
        when(dbeAggregatorDaoMock.getById("mail@mail.com")).thenReturn(null);

        DbeAppProvider dbeAppProvider = mock(DbeAppProvider.class);
        when(dbeAppProviderMock.getProvider("mail@mail.com", "appProvider1")).thenReturn(dbeAppProvider);
        when(currencyDao.getByIso4217StringCode("currency1")).thenReturn(new BmCurrency());
        when(dbeAppProvider.getTxCorrelationNumber()).thenReturn(cdr1.getCorrelationNumber());

        when(dbeAppProvider.getTxTimeStamp()).thenReturn(new Date());

        cdrsManager.createCDRs(cdrs);
    }

    @Test
    (expected = RSSException.class)
    public void createCDRsRSSExceptionNonInvalidParameter3Test() throws RSSException {
        List <CDR> cdrs = new LinkedList<>();
        RSUser rSUser = new RSUser();
        rSUser.setEmail("mail@mail.com");

        Date preDate = new Date();

        CDR cdr1 = new CDR();
        cdr1.setAppProvider("appProvider1");
        cdr1.setApplication("application1");
        cdr1.setCdrSource("mail@mail.com");
        cdr1.setChargedAmount(BigDecimal.ZERO);
        cdr1.setChargedTaxAmount(BigDecimal.TEN);
        cdr1.setCorrelationNumber(Integer.MIN_VALUE);
        cdr1.setCurrency("currency1");
        cdr1.setCustomerId("customerId1");
        cdr1.setDescription("description1");
        cdr1.setEvent("event1");
        cdr1.setProductClass("productClass1");
        cdr1.setReferenceCode("referenceCode1");
        cdr1.setTimestamp(new Date());
        cdr1.setTransactionType("A");

        cdrs.add(cdr1);

        when(userManagerMock.isAdmin()).thenReturn(true);
        when(userManagerMock.getCurrentUser()).thenReturn(rSUser);
        when(dbeAggregatorDaoMock.getById("mail@mail.com")).thenReturn(null);

        DbeAppProvider dbeAppProvider = mock(DbeAppProvider.class);
        when(dbeAppProviderMock.getProvider("mail@mail.com", "appProvider1")).thenReturn(dbeAppProvider);
        when(currencyDao.getByIso4217StringCode("currency1")).thenReturn(new BmCurrency());
        when(dbeAppProvider.getTxCorrelationNumber()).thenReturn(cdr1.getCorrelationNumber());

        when(dbeAppProvider.getTxTimeStamp()).thenReturn(preDate);

        cdrsManager.createCDRs(cdrs);
    }

    @Test
    (expected = RSSException.class)
    public void createCDRsRSSExceptionMissingMandatoryParameterTest() throws RSSException {
        List <CDR> cdrs = new LinkedList<>();
        RSUser rSUser = new RSUser();
        rSUser.setEmail("mail@mail.com");

        Date preDate = new Date();

        CDR cdr1 = new CDR();
        cdr1.setAppProvider("appProvider1");
        cdr1.setApplication("application1");
        cdr1.setCdrSource("mail@mail.com");
        cdr1.setChargedAmount(BigDecimal.ZERO);
        cdr1.setChargedTaxAmount(BigDecimal.TEN);
        cdr1.setCorrelationNumber(Integer.MIN_VALUE);
        cdr1.setCurrency("currency1");
        cdr1.setCustomerId("customerId1");
        cdr1.setDescription("description1");
        cdr1.setEvent("event1");
        cdr1.setProductClass(null);
        cdr1.setReferenceCode("referenceCode1");
        cdr1.setTimestamp(new Date());
        cdr1.setTransactionType("C");

        cdrs.add(cdr1);

        when(userManagerMock.isAdmin()).thenReturn(true);
        when(userManagerMock.getCurrentUser()).thenReturn(rSUser);
        when(dbeAggregatorDaoMock.getById("mail@mail.com")).thenReturn(null);

        DbeAppProvider dbeAppProvider = mock(DbeAppProvider.class);
        when(dbeAppProviderMock.getProvider("mail@mail.com", "appProvider1")).thenReturn(dbeAppProvider);
        when(currencyDao.getByIso4217StringCode("currency1")).thenReturn(new BmCurrency());
        when(dbeAppProvider.getTxCorrelationNumber()).thenReturn(cdr1.getCorrelationNumber());

        when(dbeAppProvider.getTxTimeStamp()).thenReturn(preDate);

        cdrsManager.createCDRs(cdrs);
    }

    @Test
    (expected = RSSException.class)
    public void createCDRsRSSExceptionMissingMandatoryParameter2Test() throws RSSException {
        List <CDR> cdrs = new LinkedList<>();
        RSUser rSUser = new RSUser();
        rSUser.setEmail("mail@mail.com");

        Date preDate = new Date();

        CDR cdr1 = new CDR();
        cdr1.setAppProvider("appProvider1");
        cdr1.setApplication("application1");
        cdr1.setCdrSource("mail@mail.com");
        cdr1.setChargedAmount(BigDecimal.ZERO);
        cdr1.setChargedTaxAmount(BigDecimal.TEN);
        cdr1.setCorrelationNumber(Integer.MIN_VALUE);
        cdr1.setCurrency("currency1");
        cdr1.setCustomerId("customerId1");
        cdr1.setDescription("description1");
        cdr1.setEvent("event1");
        cdr1.setProductClass(new String());
        cdr1.setReferenceCode("referenceCode1");
        cdr1.setTimestamp(new Date());
        cdr1.setTransactionType("C");

        cdrs.add(cdr1);

        when(userManagerMock.isAdmin()).thenReturn(true);
        when(userManagerMock.getCurrentUser()).thenReturn(rSUser);
        when(dbeAggregatorDaoMock.getById("mail@mail.com")).thenReturn(null);

        DbeAppProvider dbeAppProvider = mock(DbeAppProvider.class);
        when(dbeAppProviderMock.getProvider("mail@mail.com", "appProvider1")).thenReturn(dbeAppProvider);
        when(currencyDao.getByIso4217StringCode("currency1")).thenReturn(new BmCurrency());
        when(dbeAppProvider.getTxCorrelationNumber()).thenReturn(cdr1.getCorrelationNumber());

        when(dbeAppProvider.getTxTimeStamp()).thenReturn(preDate);

        cdrsManager.createCDRs(cdrs);
    }

    @Test
    (expected = RSSException.class)
    public void createCDRsRSSExceptionMissingMandatoryParameter3Test() throws RSSException {
        List <CDR> cdrs = new LinkedList<>();
        RSUser rSUser = new RSUser();
        rSUser.setEmail("mail@mail.com");

        Date preDate = new Date();

        CDR cdr1 = new CDR();
        cdr1.setAppProvider("appProvider1");
        cdr1.setApplication("application1");
        cdr1.setCdrSource("mail@mail.com");
        cdr1.setChargedAmount(BigDecimal.ZERO);
        cdr1.setChargedTaxAmount(BigDecimal.TEN);
        cdr1.setCorrelationNumber(Integer.MIN_VALUE);
        cdr1.setCurrency("currency1");
        cdr1.setCustomerId("customerId1");
        cdr1.setDescription("description1");
        cdr1.setEvent("event1");
        cdr1.setProductClass("productClass");
        cdr1.setReferenceCode(null);
        cdr1.setTimestamp(new Date());
        cdr1.setTransactionType("C");

        cdrs.add(cdr1);

        when(userManagerMock.isAdmin()).thenReturn(true);
        when(userManagerMock.getCurrentUser()).thenReturn(rSUser);
        when(dbeAggregatorDaoMock.getById("mail@mail.com")).thenReturn(null);

        DbeAppProvider dbeAppProvider = mock(DbeAppProvider.class);
        when(dbeAppProviderMock.getProvider("mail@mail.com", "appProvider1")).thenReturn(dbeAppProvider);
        when(currencyDao.getByIso4217StringCode("currency1")).thenReturn(new BmCurrency());
        when(dbeAppProvider.getTxCorrelationNumber()).thenReturn(cdr1.getCorrelationNumber());

        when(dbeAppProvider.getTxTimeStamp()).thenReturn(preDate);

        cdrsManager.createCDRs(cdrs);
    }

    @Test
    (expected = RSSException.class)
    public void createCDRsRSSExceptionMissingMandatoryParameter4Test() throws RSSException {
        List <CDR> cdrs = new LinkedList<>();
        RSUser rSUser = new RSUser();
        rSUser.setEmail("mail@mail.com");

        Date preDate = new Date();

        CDR cdr1 = new CDR();
        cdr1.setAppProvider("appProvider1");
        cdr1.setApplication("application1");
        cdr1.setCdrSource("mail@mail.com");
        cdr1.setChargedAmount(BigDecimal.ZERO);
        cdr1.setChargedTaxAmount(BigDecimal.TEN);
        cdr1.setCorrelationNumber(Integer.MIN_VALUE);
        cdr1.setCurrency("currency1");
        cdr1.setCustomerId("customerId1");
        cdr1.setDescription("description1");
        cdr1.setEvent("event1");
        cdr1.setProductClass("productClass");
        cdr1.setReferenceCode(new String());
        cdr1.setTimestamp(new Date());
        cdr1.setTransactionType("C");

        cdrs.add(cdr1);

        when(userManagerMock.isAdmin()).thenReturn(true);
        when(userManagerMock.getCurrentUser()).thenReturn(rSUser);
        when(dbeAggregatorDaoMock.getById("mail@mail.com")).thenReturn(null);

        DbeAppProvider dbeAppProvider = mock(DbeAppProvider.class);
        when(dbeAppProviderMock.getProvider("mail@mail.com", "appProvider1")).thenReturn(dbeAppProvider);
        when(currencyDao.getByIso4217StringCode("currency1")).thenReturn(new BmCurrency());
        when(dbeAppProvider.getTxCorrelationNumber()).thenReturn(cdr1.getCorrelationNumber());

        when(dbeAppProvider.getTxTimeStamp()).thenReturn(preDate);

        cdrsManager.createCDRs(cdrs);
    }
}
