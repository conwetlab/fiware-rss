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

/**
 * 
 */
package es.tid.fiware.rss.dao.impl.test;

import java.math.BigDecimal;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import es.tid.fiware.rss.common.test.DatabaseLoader;
import es.tid.fiware.rss.dao.impl.ObCountryDaoImpl;
import es.tid.fiware.rss.dao.impl.ObDaoImpl;
import es.tid.fiware.rss.dao.impl.PricePointDaoImpl;
import es.tid.fiware.rss.model.BmOb;
import es.tid.fiware.rss.model.BmObCountry;
import es.tid.fiware.rss.model.BmObCountryId;
import es.tid.fiware.rss.model.BmPricePoint;

/**
 * 
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:database.xml"})
public class PricePointDaoImplTest {

    /**
     * DAO for price points.
     */
    @Autowired
    private PricePointDaoImpl pricePointDAO;

    /**
     * DAO for OB-Country relation.
     */
    @Autowired
    private ObCountryDaoImpl obCountryDAO;

    /**
     * DAO for OB.
     */
    @Autowired
    private ObDaoImpl obDAO;

    @Autowired
    private DatabaseLoader databaseLoader;
    @Autowired
    @Qualifier("transactionManager")
    private HibernateTransactionManager transactionManager;

    /**
     * Method to insert data before test.
     * 
     * @throws Exception
     *             from db
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
     * Test method for
     * {@link es.tid.fiware.rss.dao.impl.PricePointDaoImpl#getListPricePoint(es.tid.fiware.rss.model.BmObCountry)}.
     */
    @Test
    public void test10GetListPricePointBmObCountry() {
        int i;

        // Call method to test
        BmObCountryId id = new BmObCountryId();
        id.setNuObId(1);
        id.setNuCountryId(1);
        BmObCountry obCountry = obCountryDAO.getById(id);
        List<BmPricePoint> list = pricePointDAO.getListPricePoint(obCountry);

        // Check result
        Assert.assertTrue("Size not equal", list.size() == 6);
        for (i = 0; i < list.size(); i++) {
            BmObCountryId itemId = list.get(i).getBmObCountry().getId();
            if (itemId.getNuObId() == 1 && itemId.getNuCountryId() == 1) {
                BigDecimal price = new BigDecimal(0);
                if (!"codeForCharge".equalsIgnoreCase(list.get(i).getId().getTxPricePointId())) {
                    switch (Integer.valueOf(list.get(i).getId().getTxPricePointId())) {
                        case 11:
                            price = new BigDecimal("9.99");
                            break;
                        case 12:
                            price = new BigDecimal("19.99");
                            break;
                        case 13:
                            price = new BigDecimal("29.99");
                            break;
                        case 14:
                            price = new BigDecimal("39.99");
                            break;
                        case 16:
                            price = new BigDecimal("49.99");
                            break;
                        default:
                            break;
                    }
                    Assert.assertTrue("Price not equal " + list.get(i).getId().getTxPricePointId(), list.get(i)
                        .getNuPrice()
                        .compareTo(price) == 0);
                }
            }
        }
    }

    /**
     * Test method for
     * {@link es.tid.fiware.rss.dao.impl.PricePointDaoImpl#getListPricePoint(es.tid.fiware.rss.model.BmOb)}.
     */
    @Test
    public void test30GetListPricePointBmOb() {
        int i;

        // Call method to test
        BmOb ob = obDAO.getById((long) 1);
        List<BmPricePoint> list = pricePointDAO.getListPricePoint(ob);

        // Check result
        Assert.assertTrue("Size not equal", list.size() == 9);
        for (i = 0; i < list.size(); i++) {
            BmObCountryId itemId = list.get(i).getBmObCountry().getId();
            if (itemId.getNuObId() == 1 && itemId.getNuCountryId() == 1) {
                if (!"codeForCharge".equalsIgnoreCase(list.get(i).getId().getTxPricePointId())) {
                    BigDecimal price = new BigDecimal("0");
                    switch (Integer.valueOf(list.get(i).getId().getTxPricePointId())) {
                        case 11:
                            price = new BigDecimal("9.99");
                            break;
                        case 12:
                            price = new BigDecimal("19.99");
                            break;
                        case 13:
                            price = new BigDecimal("29.99");
                            break;
                        case 14:
                            price = new BigDecimal("39.99");
                            break;
                        case 16:
                            price = new BigDecimal("49.99");
                            break;
                        default:
                            break;
                    }
                    Assert.assertTrue("Price not equal " + list.get(i).getId().getTxPricePointId(), list.get(i)
                        .getNuPrice()
                        .compareTo(price) == 0);
                }
            }
        }
    }

    /**
     * Test method for {@link es.tid.fiware.rss.dao.impl.PricePointDaoImpl#getListPricePoint(float)}.
     */
    @Test
    public void test40GetListPricePointFloat() {
        int i;

        // Call method to test
        List<BmPricePoint> list = pricePointDAO.getListPricePoint((float) 19.99);

        // Check result
        Assert.assertTrue("Size list not equal " + list.size(), list.size() == 2);
        for (i = 0; i < list.size(); i++) {
            BigDecimal price = new BigDecimal(0);
            String id = "0";
            long country = 0;
            switch ((int) list.get(i).getId().getNuObId()) {
                case 1:
                    price = new BigDecimal("19.99");
                    id = "12";
                    country = 1;
                    break;
                case 4:
                    price = new BigDecimal("19.99");
                    id = "12";
                    country = 4;
                    break;
                default:
                    break;
            }
            Assert.assertTrue("ID not equal " + list.get(i).getId().getTxPricePointId(),
                list.get(i).getId().getTxPricePointId().equals(id));
            Assert.assertTrue("Country not equal " + list.get(i).getId().getNuCountryId(),
                list.get(i).getId().getNuCountryId() == country);
            Assert.assertTrue("Price not equal " + list.get(i).getNuPrice(),
                list.get(i).getNuPrice().compareTo(price) == 0);
        }
    }

    /**
     * Test method for {@link es.tid.fiware.rss.dao.impl.PricePointDaoImpl#getListPricePoint(float, float)}.
     */
    @Test
    public void test50GetListPricePointFloatFloat() {
        int i;

        // Call method to test
        List<BmPricePoint> list = pricePointDAO.getListPricePoint((float) 11.0, (float) 20.0);

        // Check result
        Assert.assertTrue("Size list not equal " + list.size(), list.size() == 4);
        for (i = 0; i < list.size(); i++) {
            BigDecimal price = new BigDecimal(0);
            String id = "0";
            switch (Integer.valueOf(list.get(i).getId().getTxPricePointId())) {
                case 12:
                    price = new BigDecimal("19.99");
                    id = "12";
                    break;
                /*
                 * case 7:
                 * price = new BigDecimal("19.99");
                 * id = "7";
                 * break;
                 */
                case 2:
                    price = new BigDecimal("15");
                    id = "2";
                    break;
                /*
                 * case 13:
                 * price = new BigDecimal("20");
                 * id = "13";
                 * break;
                 * case 16:
                 * price = new BigDecimal("18.89");
                 * id = "16";
                 * break;
                 */
                case 3:
                    price = new BigDecimal("17.99");
                    id = "3";
                    break;
                default:
                    break;
            }
            Assert.assertTrue("ID not equal " + list.get(i).getId().getTxPricePointId(),
                list.get(i).getId().getTxPricePointId().equals(id));
            Assert.assertTrue("Price not equal " + list.get(i).getNuPrice(),
                list.get(i).getNuPrice().compareTo(price) == 0);
        }
    }

    /**
     * Test method for
     * {@link es.tid.fiware.rss.dao.impl.PricePointDaoImpl#getPricePoint(es.tid.fiware.rss.model.BmObCountry, long)}.
     */
    @Test
    public void test60GetPricePoint() {
        // Call method to test
        BmObCountryId id = new BmObCountryId();
        id.setNuCountryId(4);
        id.setNuObId(4);
        BmObCountry obCountry = obCountryDAO.getById(id);
        BmPricePoint pp = pricePointDAO.getPricePoint(obCountry, "41");

        // Check result
        Assert.assertTrue("Operator not equeal", pp.getBmObCountry().getId().getNuObId() == 4);
        Assert.assertTrue("Country not equeal", pp.getBmObCountry().getId().getNuCountryId() == 4);
        Assert.assertTrue("ID operator price point not equeal", pp.getId().getTxPricePointId().equals("41"));
        Assert.assertTrue("Price not equeal", pp.getNuPrice().compareTo(new BigDecimal("7.05")) == 0);
    }

}
