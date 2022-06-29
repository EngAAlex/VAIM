/**
 * Copyright © 2020-2021 Alessio Arleo 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package vaim.io.api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import vaim.core.graph.Graph;
import vaim.io.api.structures.GraphList;
import vaim.io.api.structures.GraphListInner;
import vaim.io.api.structures.Maximization;
import vaim.io.api.structures.SimulationData;
import vaim.io.db.DBWizard;
import vaim.io.db.DBWizard.QUERY_TYPE;
import vaim.simulation.infmax.InfluenceMaximization;
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-03-26T11:32:33.543Z[GMT]")
@Controller
public class LoadAvailableApiController implements LoadAvailableApi {

	private static final Logger log = LoggerFactory.getLogger(LoadAvailableApiController.class);

	private final ObjectMapper objectMapper;

	private final HttpServletRequest request;

	@org.springframework.beans.factory.annotation.Autowired
	public LoadAvailableApiController(ObjectMapper objectMapper, HttpServletRequest request) {
		this.objectMapper = objectMapper;
		this.request = request;
	}

	public ResponseEntity<GraphList> loadAvailable() {
		Connection conn = null;
		DBWizard dbW = DBWizard.getInstance();
		GraphList gl;
		
		log.info("Received LoadAvailable call");

		try {
			conn = dbW.connect();
			PreparedStatement pms = dbW.createStatement(conn,QUERY_TYPE.LIST_GRAPHS);
			ResultSet rs = pms.executeQuery();
			gl = new GraphList();  

			while(rs.next()) {
				GraphListInner cgli = new GraphListInner()
						.id(rs.getInt(1))
						.name(rs.getString(2))
						.vertexCount(rs.getInt(3))
						.edges(rs.getInt(4))
						.simulations(rs.getInt(5));	//gl.get(graphMap.get(rs.getLong(1)));									
				gl.addGraphElement(cgli);
			}

			Set<Maximization> s = InfluenceMaximization.getAvailableMethods();
			
			for(Maximization m : s)
				gl.addMaximizationsItem(m);
			
			rs.close();
			pms.close();
			conn.close();

		}catch(Exception e) {
			dbW.fail(conn, e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("Sending Response");
		return new ResponseEntity<GraphList>(gl, HttpStatus.OK);
	}

}
