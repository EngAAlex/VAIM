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
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

import io.swagger.annotations.ApiParam;
import vaim.io.api.structures.SimulationChunk;
import vaim.io.db.DBWizard;
import vaim.io.session.SessionControl;
import vaim.layout.MatrixQuantizer;
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-03-26T11:32:33.543Z[GMT]")
@Controller
public class GetSimulationFramesApiController implements GetSimulationFramesApi {

	private static final Logger log = LoggerFactory.getLogger(GetSimulationFramesApiController.class);

	private final ObjectMapper objectMapper;

	private final HttpServletRequest request;

	@org.springframework.beans.factory.annotation.Autowired
	public GetSimulationFramesApiController(ObjectMapper objectMapper, HttpServletRequest request) {
		this.objectMapper = objectMapper;
		this.request = request;
	}

	public ResponseEntity<List<SimulationChunk>> getSimulationFrames(@NotNull @ApiParam(value = "The current session id - necessary to recover the current matrix state", required = true) @Valid @RequestParam(value = "session_id", required = true) String sessionId
			,@ApiParam(value = "the list of simulations to load" ,required=true )  @Valid @RequestBody LinkedList<String> body
			,@ApiParam(value = "The initial frame. Defaults to zero.", defaultValue = "0") @Valid @RequestParam(value = "framestart", required = false, defaultValue="0") Integer framestart
			,@ApiParam(value = "The final frame. Defaults to the whole computation") @Valid @RequestParam(value = "offset", required = false) Integer offset
			,@ApiParam(value = "Describes how (and if) to aggregate frames. Defaults to 1.", defaultValue = "1") @Valid @RequestParam(value = "aggregation", required = false, defaultValue="1") Integer aggregation
			,@ApiParam(value = "Describes how to provide the data to the user, if only by statistics or with the exact ids of the vertices too.") @Valid @RequestParam(value = "full_delivery", required = false) Boolean fullDelivery
			) {

		DBWizard dbW = DBWizard.getInstance();
		Connection conn = null;

		if(framestart == null)
			framestart = 0;
		if(aggregation == null)
			aggregation = 1;

		//PreparedStatement pms;		
		log.info("Received GetSimulationFrames call");

		try {
			conn = dbW.connect();

			Map<String, String> sessionData = SessionControl.recoverSessionInfo(conn, sessionId);
			int graphId = Integer.valueOf(sessionData.get(SessionControl.GRAPH_ID));
			int resolution = Integer.valueOf(sessionData.get(SessionControl.RESOLUTION));
			String simulations = sessionData.get(SessionControl.SIMULATIONS);
			
			log.info("\tSession Data " + sessionId + " Recovered");
			
			List<String> loadedSimList = null;
			if(simulations == null)
				loadedSimList = Lists.newArrayList();
			else
				loadedSimList = Lists.newArrayList(simulations.split(";"));
			MatrixQuantizer mq = new MatrixQuantizer(resolution);
			mq.rebuildFromDatabase(conn, graphId);
			log.info("\tMatrix Rebuilt");

			List<SimulationChunk> sims = new LinkedList<SimulationChunk>();

			for(String s : body) {
				mq.clearEdgeAggregator();				
				if(simulations == null || !loadedSimList.contains(s)) //ONLY SEND OUT SIMULATIONS THAT ARE NOT ALREADY LOADED
					sims.add(SimulationChunk.rebuildFromDatabase(Integer.parseInt(s), conn, mq, aggregation, framestart, offset));
			}
			log.info("\tSimulation Chunks ready");

			sessionData.put(SessionControl.SIMULATIONS, Arrays.toString(body.toArray()));
			if(body.toArray().length > 0) {
				sessionData.put(SessionControl.AGGREGATION, aggregation + "");
				sessionData.put(SessionControl.FRAMESTART, framestart + "");
				if(sims.size() > 0)
					sessionData.put(SessionControl.OFFSET, sims.get(0).getOffset() + "");
				else
					sessionData.put(SessionControl.OFFSET, sessionData.get(SessionControl.OFFSET));
			}else {
				sessionData.put(SessionControl.AGGREGATION, "");
				sessionData.put(SessionControl.FRAMESTART, "");
				sessionData.put(SessionControl.OFFSET, "");
			}
			SessionControl.createOrUpdateSession(conn, sessionId, sessionData);
			log.info("\tSession " + sessionId + " updated");

			conn.close();
			log.info("Sending Response");
			return new ResponseEntity<List<SimulationChunk>>(sims, HttpStatus.OK);	
		}catch(Exception e) {
			dbW.fail(conn, e);
			e.printStackTrace();
			return new ResponseEntity<List<SimulationChunk>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}        
	}


}
