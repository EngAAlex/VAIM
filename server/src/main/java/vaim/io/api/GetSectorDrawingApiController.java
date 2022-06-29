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
import java.sql.PreparedStatement;
import java.util.Arrays;
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

import io.swagger.annotations.ApiParam;
import vaim.io.api.structures.GraphChunkQuery;
import vaim.io.api.structures.GraphSector;
import vaim.io.db.DBWizard;
import vaim.io.session.SessionControl;
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-10-16T13:00:45.476Z[GMT]")
@Controller
public class GetSectorDrawingApiController implements GetSectorDrawingApi {

	private static final Logger log = LoggerFactory.getLogger(GetSectorDrawingApiController.class);

	private final ObjectMapper objectMapper;

	private final HttpServletRequest request;

	@org.springframework.beans.factory.annotation.Autowired
	public GetSectorDrawingApiController(ObjectMapper objectMapper, HttpServletRequest request) {
		this.objectMapper = objectMapper;
		this.request = request;
	}

	public ResponseEntity<GraphSector> drawSector(@NotNull @ApiParam(value = "The session id", required = true) @Valid @RequestParam(value = "session_id", required = true) String sessionId
			,@ApiParam(value = "the sectors in the matrix to load" ,required=true )  @Valid @RequestBody GraphChunkQuery body
			) {
		Connection conn = null;
		PreparedStatement pms;		
		DBWizard dbW = DBWizard.getInstance();
		
		log.info("Received GetSector call");

		try {
			conn = dbW.connect();
			Map<String, String> sessionData = SessionControl.recoverSessionInfo(conn, sessionId);
			int graphId = Integer.valueOf(sessionData.get(SessionControl.GRAPH_ID).toString());
			int resolution = Integer.valueOf(sessionData.get(SessionControl.RESOLUTION).toString());
			String[] simulationIds = sessionData.get(SessionControl.SIMULATIONS).toString().split(";");
			
			log.info("\tSession " + sessionId + " Data Recovered");

			GraphSector gs = GraphSector.rebuildFromDatabase(body, graphId, resolution, simulationIds, conn);
			log.info("\tSectors Rebuilt");

			sessionData.put(SessionControl.SELECTED_SECTORS, Arrays.toString(body.toArray()));
			SessionControl.createOrUpdateSession(conn, sessionId, sessionData);
			
			conn.close();									
			log.info("Sending Response");

			return new ResponseEntity<GraphSector>(gs, HttpStatus.OK);

		} catch (IOException e) {
			dbW.fail(conn, e);
			return new ResponseEntity<GraphSector>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			dbW.fail(conn, e);
			return new ResponseEntity<GraphSector>(HttpStatus.INTERNAL_SERVER_ERROR);            	
		}
	}

}
