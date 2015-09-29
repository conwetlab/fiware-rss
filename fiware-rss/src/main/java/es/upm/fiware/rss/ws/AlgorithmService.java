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

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import es.upm.fiware.rss.algorithm.AlgorithmFactory;
import es.upm.fiware.rss.model.Algorithm;

/**
 *
 * @author fdelavega
 */
@Path("/")
@WebService(serviceName = "algorithms", name="algorithms")
public class AlgorithmService {

    @WebMethod
    @GET
    @Produces("application/json")
    public Response getAlgoritms() throws Exception {
        AlgorithmFactory factory = new AlgorithmFactory();
        List<Algorithm> algorithms = factory.getAlgorithms();

        Response.ResponseBuilder rb = Response.status(Response.Status.OK.getStatusCode());
        rb.entity(algorithms);

        return rb.build();
    }
}
