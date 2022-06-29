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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import com.google.common.collect.Lists;

import vaim.core.graph.Edge;
import vaim.core.graph.Edge.UnWeightedEdge;
import vaim.io.db.DBWizard;
import vaim.io.db.DBWizard.QUERY_TYPE;
import vaim.simulation.Common.SimulationStatistics;
import vaim.simulation.Common.SimulationStatus;

/**
 * @author Alessio Arleo
 *
 * This interpolation strategy compresses the times an edge has been activated during the different runs into the edge weight.
 */
public class InterpolateWithUncertainty extends InterpolationStrategy {

	public InterpolateWithUncertainty() {
		super();
	}

	public InterpolateWithUncertainty(String options) {
		super(options);
	}

	@Override
	public Set<Edge> interpolate(Connection conn, int graphId, int currentSimulationId, List<SimulationStatus> s) throws Exception {		
		HashSet<Edge> interpolatedEdges = new HashSet<Edge>();
		
		//### First: recover the edges activated thru all runs
		DBWizard dbW = DBWizard.getInstance();
		PreparedStatement pms = dbW.createStatement(conn, DBWizard.getStringFromQuery(QUERY_TYPE.GET_ACTIVATED_EDGES_WITH_CHANCE).replace(DBWizard.NUM_PLACEHOLDER, ""+s.size()));
		pms.setInt(1, currentSimulationId);

		ResultSet rs = pms.executeQuery();
	
		int maxStep = Integer.MIN_VALUE;
		//int runs = s.length;

		//### Second: save the final activation edges
		while(rs.next()) {

			long source = rs.getLong(1);
			long target = rs.getLong(2);
			int step = rs.getInt(3);
			double chance = rs.getDouble(4);
		
			maxStep = Math.max(maxStep, step);
			interpolatedEdges.add(new Edge().source(source).target(target).activeAt(step).weight(chance));
		}
		
		rs.close();
		pms.close();
		
		return interpolatedEdges;
	}

}
