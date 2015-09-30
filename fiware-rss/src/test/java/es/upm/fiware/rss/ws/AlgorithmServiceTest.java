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

import es.upm.fiware.rss.ws.AlgorithmService;
import java.util.List;
import javax.ws.rs.core.Response;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.Assert;

/**
 *
 * @author jortiz
 */
public class AlgorithmServiceTest {

    private AlgorithmService toTest;

    public AlgorithmServiceTest() {
    }

    @Before
    public void setUp() {
        toTest = new AlgorithmService();
    }

    @Test
    public void getAlgoritmsTest() throws Exception {
        Response response = toTest.getAlgoritms();

        org.junit.Assert.assertEquals(200, response.getStatus());
        Assert.isInstanceOf(List.class, response.getEntity());

    }
}
