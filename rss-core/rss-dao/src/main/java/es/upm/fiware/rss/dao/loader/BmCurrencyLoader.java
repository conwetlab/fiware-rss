package es.upm.fiware.rss.dao.loader;

/**
 * Copyright (C) 2015 CoNWeT Lab., Universidad Politécnica de Madrid
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


import es.upm.fiware.rss.dao.CurrencyDao;
import es.upm.fiware.rss.model.BmCurrency;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

/**
 *
 * @author fdelavega
 */

@Component
public class BmCurrencyLoader {

    @Autowired
    private CurrencyDao currencyDao;

    @Autowired
    @Qualifier("transactionManager")
    protected PlatformTransactionManager txManager;

    private void saveCurrency(String code, String description, String symbol, String codeNum, int decimals) {
        BmCurrency c = new BmCurrency();
        c.setTxIso4217Code(code);
        c.setTxDescription(description);
        c.setTcSymbol(symbol);
        c.setTxIso4217CodeNum(codeNum);
        c.setNuIso4217Decimals(decimals);
        this.currencyDao.create(c);
    }

    @PostConstruct
    public void init() {
        TransactionTemplate tmpl = new TransactionTemplate(txManager);
        tmpl.execute(new TransactionCallbackWithoutResult() {

            @Override
            protected void doInTransactionWithoutResult(TransactionStatus ts) {
                // Check if currencies has been loaded
                List<BmCurrency> currs = currencyDao.getAll();

                if (currs == null || currs.isEmpty()) {
                    saveCurrency("EUR","Euro","€","978", 2);
                    saveCurrency("GBP","Esterlina","£","826",2);
                    saveCurrency("BRL","Verdadero brasileno","R$","986",2);
                    saveCurrency("ARS","Peso argentino","$a","032",2);
                    saveCurrency("MXN","Peso mexicano","$","484",2);
                    saveCurrency("CLP","Peso chileno","$","152",2);
                    saveCurrency("PEN","Nuevo sol","S/.","604",2);
                    saveCurrency("VEF","Bolivar fuerte","Bs.","937",2);
                    saveCurrency("COP","Peso colombiano","$","170",2);
                    saveCurrency("USD","US Dolar","$","840",2);
                    saveCurrency("NIO","Cordoba oro","C$","558",2);
                    saveCurrency("GTQ","Quetzal","Q","320",2);
                    saveCurrency("SVC","El Salvador Colon","¢","222",2);
                    saveCurrency("PAB","Balboa","B/.","590",2);
                    saveCurrency("UYU","Peso Uruguayo","$","858",2);
                    saveCurrency("MYR","Malaysian ringgit","RM","458",2);
                    saveCurrency("NOK","Norwegian krone","kr","578",2);
                    saveCurrency("HUF","Hungarian forint","Ft","348",2);
                }
            }
        });
    }
}
