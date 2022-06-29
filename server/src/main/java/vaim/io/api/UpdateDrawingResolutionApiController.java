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

import java.io.IOException;
import java.sql.Connection;
import java.util.Map;

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
import vaim.io.api.structures.ResolutionUpdate;
import vaim.io.api.structures.SimulationChunk;
import vaim.io.db.DBWizard;
import vaim.io.session.SessionControl;
import vaim.layout.MatrixQuantizer;
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-10-09T15:18:20.913Z[GMT]")
@Controller
public class UpdateDrawingResolutionApiController implements UpdateDrawingResolutionApi {

	private static final Logger log = LoggerFactory.getLogger(UpdateDrawingResolutionApiController.class);

	private final ObjectMapper objectMapper;

	private final HttpServletRequest request;

	@org.springframework.beans.factory.annotation.Autowired
	public UpdateDrawingResolutionApiController(ObjectMapper objectMapper, HttpServletRequest request) {
		this.objectMapper = objectMapper;
		this.request = request;
	}

	public ResponseEntity<ResolutionUpdate> updateResolution(@NotNull @ApiParam(value = "The session id", required = true) @Valid @RequestParam(value = "session_id", required = true) String sessionId
			, @NotNull @ApiParam(value = "the new resolution", required = true) @Valid @RequestParam(value = "newresolution", required = true) Integer newResolution
			) {

		ResolutionUpdate ru;
		log.info("Received UpdateResolution call");
		DBWizard dbW = DBWizard.getInstance();
		Connection conn = null;

		try {

			try {
				ru = new ResolutionUpdate();
				conn = dbW.connect();

				Map<String, String> sessionData = SessionControl.recoverSessionInfo(conn, sessionId);
				int graphId = Integer.valueOf(sessionData.get(SessionControl.GRAPH_ID));			
				String simulations = sessionData.get(SessionControl.SIMULATIONS);

				log.info("\tSession Data " + sessionId + " Recovered");
				
				MatrixQuantizer mq = new MatrixQuantizer(newResolution);
				mq.rebuildFromDatabase(conn, graphId);
				log.info("\tMatrix Rebuilt");

				ru.setSectorQuantization(mq.copyMatrixStateAsList());
				log.info("\tSectors saved");
				
				log.info("\tUpdating Simulations");
				if(simulations != null) {					
					String[] simTemp = simulations/*.substring(1,simulations.length()-1)*/.split(";");
					int aggregation = Integer.valueOf(sessionData.get(SessionControl.AGGREGATION));
					int framestart = Integer.valueOf(sessionData.get(SessionControl.FRAMESTART));
					int offset = Integer.valueOf(sessionData.get(SessionControl.OFFSET));
					for(String s : simTemp) {	
						mq.clearEdgeAggregator();
						ru.addSimulationMatricesItem(SimulationChunk.rebuildFromDatabase(Integer.parseInt(s), conn, mq, aggregation, framestart, offset));
					}
					/*String sectorString = sessionData.get(SessionControl.SELECTED_SECTORS);					
					if(sectorString != null)	
						ru.setGraphSector(GraphSector.rebuildFromDatabase(GraphChunkQuery.rebuildFromDataString(sectorString/), graphId, newResolution, simTemp, conn));*/
				}
				log.info("\tSimulations OK");

				sessionData.put(SessionControl.RESOLUTION, newResolution + "");
				SessionControl.createOrUpdateSession(conn, sessionId, sessionData);
				log.info("\tSession Data " + sessionId + " Updated");

				conn.close();
				
			} catch (IOException e) {
				dbW.fail(conn, e);
				return new ResponseEntity<ResolutionUpdate>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}catch(Exception e) {
			dbW.fail(conn, e);
			return new ResponseEntity<ResolutionUpdate>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		//}
		log.info("Sending Response");
		return new ResponseEntity<ResolutionUpdate>(ru, HttpStatus.OK);
	}
}
