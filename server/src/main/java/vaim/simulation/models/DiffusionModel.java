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

package vaim.simulation.models;

import java.sql.Connection;
import java.util.Collection;
import java.util.Set;

import vaim.core.graph.Vertex;
import vaim.simulation.Common.Message;
import vaim.simulation.Common.SimulationStatus;


public abstract class DiffusionModel<T> {

	protected int input;

//	protected long lastActivatedVertices;
//	protected long lastDiedVertices;
//	protected long lastSentMessages;

	protected Set<Long> activeVertices;

	protected Connection conn;

	protected Set<Long> seedSet;

	public DiffusionModel(){
		initRound();
	}
	
	public DiffusionModel(String options) {		
		initRound();
	}

	public void setConnection(Connection conn) {
		this.conn = conn;
	}
	
//	public void updateStatistics(SimulationStatus currentStatus) {
//		currentStatus.updateSimulation(lastActivatedVertices, lastDiedVertices, lastSentMessages);
//	}

	public void initRound() {
//		lastActivatedVertices = 0;
//		lastDiedVertices = 0;
//		lastSentMessages = 0;
	}


	public void setInput(int input) {
		this.input = input;
	}
	
	/*public void storeSeeds(Set<Long> seedSet){
		this.seedSet = seedSet;	
	}*/
	/*public Set<Vertex> storeSeeds(Set<Long> seeds, int simulationId, int run) throws Exception{
		HashSet<Vertex> activeVertices = new HashSet<Vertex>();
		//DBWizard dbW = DBWizard.getInstance();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String seedList = "";
		for (Long s : seeds) {			
			//Vertex v = new Vertex().id(s);
			seedList += s + ",";
		}
		seedList = seedList.substring(0, seedList.length() - 1);
		
		ps = DBWizard.getInstance().createStatement(conn, DBWizard.getStringFromQuery(DBWizard.QUERY_TYPE.GET_VERTICES_NEIGHBORHOODS).replace(DBWizard.LIST_PLACEHOLDER, seedList));
		
		ps.setInt(1, simulationId);
		ps.setInt(2, run);		
		ps.setInt(3, input);
		ps.setInt(4, input);
		
		rs = ps.executeQuery();
		
		while(rs.next()) {
			Vertex v = new Vertex().id(rs.getLong(1));
			try {
			Long[] neighbors = (Long[]) rs.getArray(2).getArray();
			Double[] weights = (Double[]) rs.getArray(3).getArray();
			int index = 0;
			for(long l : neighbors) {
				v.addNeighborsItem(new Edge().source(v.getId()).target(l).weight(weights[index]));
				index++;
			}
			}catch(Exception e) {
				//NO-OP
			}
			activeVertices.add(v);
		}
		
		rs.close();
		ps.close();


		return activeVertices;
		//			Vertex currS = input.getVertex(s);				
		//			currS.storeStateChange(-1, DiffusionSimulation.VERTEX_ACTIVE, -1, currentStatus.getRun());
		//			lastActivatedVertices++;
		//			currS.setActive(-1);
		//			currS.setSeed(s);

	}*/

	public abstract void seedSetup(Set<Vertex> seeds);

	public abstract Collection<Vertex> analyzeMessages(Set<Message<T>> messageCache, SimulationStatus currentStatus, Set<Long> activatedNodesList) throws Exception;

	public abstract void computeAndSend(Collection<Vertex> activeSet, Set<Message<T>> messageCache, SimulationStatus currentStatus, Set<Long> activatedNodesList) throws Exception;		

	//public abstract boolean isVertexListening(Vertex v, long step);

	public abstract void resetSimulationModel();
	
	public abstract String getName();

	public abstract String getDescription();

}
