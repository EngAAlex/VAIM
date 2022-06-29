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
import java.util.List;
import java.util.Set;

import vaim.core.graph.Edge;
import vaim.io.db.DBWizard;
import vaim.io.db.DBWizard.QUERY_TYPE;
import vaim.simulation.Common.SimulationStatus;

/**
 * @author Alessio
 *
 *	This strategy just takes the first simulation run by default, or the one passed by option.
 *
 */
public class NaiveInterpolation extends InterpolationStrategy {

	private int run = 0;

	public NaiveInterpolation(String options) {
		super(options);
		try {
			run = Integer.parseInt(options);
		}catch(Exception e) {}
	}


	@Override
	public Set<Edge> interpolate(Connection conn, int graphId, int currentSimulationId, List<SimulationStatus> s)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
