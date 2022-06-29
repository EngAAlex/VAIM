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
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

import io.swagger.annotations.ApiParam;
import vaim.io.api.structures.SimulationRequest;
import vaim.io.db.DBWizard;
import vaim.io.db.DBWizard.QUERY_TYPE;
import vaim.simulation.DiffusionSimulation;
import vaim.simulation.infmax.InfluenceMaximization;
import vaim.simulation.models.DiffusionModel;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-03-17T16:01:30.084Z[GMT]")
@RestController
public class SimulateApiController implements SimulateApi {

	private static final Logger log = LoggerFactory.getLogger(SimulateApiController.class);

	private final ObjectMapper objectMapper;

	private final HttpServletRequest request;

	@org.springframework.beans.factory.annotation.Autowired
	public SimulateApiController(ObjectMapper objectMapper, HttpServletRequest request) {
		this.objectMapper = objectMapper;
		this.request = request;
	}

	public ResponseEntity<Void> simulate(
			@NotNull @ApiParam(value = "The graphid to simulate on", required=true) 
			@Valid @RequestParam(value = "graphId", required = true) Integer graphId, 
			@ApiParam(value = "The simulation request", required=true) @Valid @RequestBody SimulationRequest body
			) {

		DBWizard dbW = DBWizard.getInstance();
		PreparedStatement pms;
		ResultSet rs;
		Connection conn = null;
		log.info("Received Simulate call");

		try {
			
			@SuppressWarnings("unchecked")
			DiffusionModel<Long> diffStrat = 
					(DiffusionModel<Long>) Class.forName("vaim.simulation.models." + body.getModel()).getDeclaredConstructor().newInstance(); 
			
			Runnable runnableSimulation = null;
			
			if(body.getRandomSeeds().intValue() < 0) {
				conn = dbW.connect();
				pms = dbW.createStatement(conn ,QUERY_TYPE.LIST_SEEDS);
				pms.setInt(1, body.getDonorSimulation().intValue());
				rs = pms.executeQuery();
				rs.next();
				
				List<Long> seedsColl = Lists.newArrayList((Long[])rs.getArray(1).getArray());
				HashSet<Long> seedsSet = new HashSet<Long>(seedsColl);
				
				rs.close();
				pms.close();
				conn.close();
				
				seedsSet.addAll(body.getSeedsToAdd());
				seedsSet.removeAll(body.getSeedsToRemove());
				if(seedsSet.size() > 0)
					runnableSimulation = new DiffusionSimulation.RunnableSimulationWithSeeds(graphId, diffStrat, seedsSet);				
				
			} else {
				log.info("Ready to Simulate");
				InfluenceMaximization infStrat = (InfluenceMaximization) Class.forName("vaim.simulation.infmax." + body.getSelectionStrategy()).getDeclaredConstructor().newInstance();
				runnableSimulation = new DiffusionSimulation.RunnableSimulationWithRequestedNumberOfSeeds(graphId, diffStrat, infStrat, body.getRandomSeeds().intValue());
			}
			
			new Thread(runnableSimulation).start();			
			log.info("Sending Response");
			return new ResponseEntity<Void>(HttpStatus.OK);
		}catch(Exception e) {
			dbW.fail(conn, e);
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


}
