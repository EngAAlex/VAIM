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

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiParam;
import vaim.core.graph.Graph;
import vaim.io.api.structures.SimulationData;
import vaim.io.db.DBWizard;
import vaim.io.db.DBWizard.QUERY_TYPE;
import vaim.io.session.SessionControl;
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-05-02T15:31:46.321Z[GMT]")
@Controller
public class LoadGraphApiController implements LoadGraphApi {

	private static final Logger log = LoggerFactory.getLogger(LoadGraphApiController.class);

	private final ObjectMapper objectMapper;

	private final HttpServletRequest request;
	
	@org.springframework.beans.factory.annotation.Autowired
	public LoadGraphApiController(ObjectMapper objectMapper, HttpServletRequest request) {
		this.objectMapper = objectMapper;
		this.request = request;
	}


	public ResponseEntity<Graph> loadGraph(@NotNull @ApiParam(value = "The graph id to load.", required = true) @Valid @RequestParam(value = "graph_id", required = true) Integer graphId
			,@NotNull @ApiParam(value = "Session variable to allow the server to recover information", required = true) @Valid @RequestParam(value = "session_id", required = true) String sessionId,			
			@NotNull @ApiParam(value = "The graph id to load.", required = true) @Valid @RequestParam(value = "res", required = false) Integer requestedResolution
			) {        
			DBWizard dbW = DBWizard.getInstance();
			Connection conn = null;
			PreparedStatement pms;		
			ResultSet rs;
			Graph g = new Graph().id(graphId);
			
			log.info("Received LoadGraph call");
			
			try {
				conn = dbW.connect();
				log.info("\t Rebuilding Graph " + (requestedResolution != null ? "with" : "w/o") + " resolution");
				g.rebuildInfoFromDatabase(conn);
				log.info("\t Graph Rebuilt");
				
				/*int resolution = MatrixQuantizer.DEFAULT_RESOLUTION;
				
				try {
					Map<String, String> recoveredSessionData = SessionControl.recoverSessionInfo(conn, sessionId);
					if(recoveredSessionData.containsKey(SessionControl.RESOLUTION))
						resolution = Integer.parseInt(recoveredSessionData.get(SessionControl.RESOLUTION));
				}catch(Exception e) {}*/
				if(requestedResolution != null) {
					g.buildSectorQuantization(conn, requestedResolution);
					log.info("\t Sector Rebuilt");
				}
				
				pms = dbW.createStatement(conn,QUERY_TYPE.GET_SIMULATIONS_FOR_GRAPH);
				pms.setLong(1, graphId);

				rs = pms.executeQuery();
				while(rs.next()) {
					SimulationData sd = new SimulationData();
					sd.setId(rs.getInt(1));
					sd.setAvgActivated(new BigDecimal(rs.getFloat(2)));
					sd.setNumberOfSeeds(rs.getLong(3));
					sd.setRuns(rs.getInt(4));
					sd.setSteps(rs.getInt(5));
					sd.setSeedSelection(rs.getString(6));        		
					sd.setDiffusionStrategy(rs.getString(7));
					sd.setActivatedStddev(new BigDecimal(rs.getFloat(8)));
					g.addSimulationsItem(sd);        		        	
				}
				
				log.info("\tSimulations Recovered");

				pms.close();
				
				if(requestedResolution != null) {
					HashMap<String, String> sessionData = new HashMap<String, String>();
					sessionData.put(SessionControl.GRAPH_ID, graphId + "");
					sessionData.put(SessionControl.RESOLUTION, requestedResolution + "");
					sessionData.put(SessionControl.SIMULATIONS, ""); //CLEAR LOADED SIMULATIONS
					sessionData.put(SessionControl.SELECTED_SECTORS, ""); //CLEAR SELECTED SECTORS
					
					SessionControl.createOrUpdateSession(conn, sessionId, sessionData);
					log.info("\tSession updated");
				}
				conn.close();
				log.info("Sending Response");
				return new ResponseEntity<Graph>(g, HttpStatus.OK);        	
			}catch(Exception e) {
				dbW.fail(conn, e);        	
				return new ResponseEntity<Graph>(HttpStatus.INTERNAL_SERVER_ERROR);        	
			}

	}

}
