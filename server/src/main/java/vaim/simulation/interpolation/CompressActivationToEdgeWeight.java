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

package vaim.simulation.interpolation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import vaim.core.graph.Edge;
import vaim.io.db.DBWizard;
import vaim.io.db.DBWizard.QUERY_TYPE;
import vaim.simulation.Common.SimulationStatus;

/**
 * @author Alessio Arleo
 *
 * This interpolation strategy compresses the times an edge has been activated during the different runs into the edge weight.
 */
public class CompressActivationToEdgeWeight extends InterpolationStrategy {

	public CompressActivationToEdgeWeight() {
		super();
	}

	public CompressActivationToEdgeWeight(String options) {
		super(options);
	}

	@Override
	public Set<Edge> interpolate(Connection conn, int graphId, int currentSimulationId, List<SimulationStatus> s)
			throws Exception {
		return null;
//		HashSet<Edge> interpolatedEdges = new HashSet<Edge>();
//		
//		//### First: recover the edges activated thru all runs
//		DBWizard dbW = DBWizard.getInstance();
//		PreparedStatement pms = null; // = dbW.createStatement(conn, QUERY_TYPE.GET_ACTIVATED_EDGES_COUNT);
//		pms.setInt(1, currentSimulationId);
//
//		ResultSet rs = pms.executeQuery();
//
//		HashSet<Long> activatedVertices = new HashSet<Long>();
//
//		int maxStep = Integer.MIN_VALUE;
//				
//		long activatedVerticesSum = 0;
//		long sentMessagesSum = 0;
//		long deadVerticesSum = 0;
//
//		while(rs.next()) {
//
//			long source = rs.getLong(1);
//			long target = rs.getLong(2);
//			long count = rs.getLong(3);
//			long seed = rs.getLong(4);
//			int step = Math.round(rs.getFloat(5));
//			
//			if(activatedVertices.contains(target)) {
//				continue;
//			}else
//				activatedVertices.add(target);
//			
//			interpolatedEdges.add(new Edge().source(source).target(target).weight((double) count).seedId(seed).activeAt(step));
//			activatedVerticesSum++;
//			maxStep = Math.max(maxStep, step);
//
//		}
//
//		rs.close();
//		pms.close();
//
//		
//		return interpolatedEdges;
	}

}
