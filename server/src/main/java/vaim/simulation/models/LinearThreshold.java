package vaim.simulation.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import java.util.Set;

import vaim.core.graph.Edge;
import vaim.core.graph.Vertex;
import vaim.io.db.DBWizard;
import vaim.simulation.Common.Message;
import vaim.simulation.Common.SimulationStatus;

public class LinearThreshold extends DiffusionModel<Double> {

	private HashMap<Long, Vertex> cumulativeActiveVertices = new HashMap<Long, Vertex>();
	private HashMap<Long, Double> fetchedVerticesChance = new HashMap<Long, Double>();
	protected Logger log = Logger.getLogger(LinearThreshold.class);

	public LinearThreshold() {
		super();		
	}

	public LinearThreshold(String options) {
		super(options);
	}

	
	@Override
	public Collection<Vertex> analyzeMessages(Set<Message<Double>> messageCache, SimulationStatus currentStatus,
			Set<Long> activatedNodesList) throws Exception {					
		
		HashMap<Long, Double> fetchedVertices = new HashMap<Long, Double>();
		HashMap<Long, Set<Long>> fetchedVerticesIncoming = new HashMap<Long, Set<Long>>();

		for (Message<Double> message : messageCache) {			
			if(!activatedNodesList.contains(message.getTarget())) {	// SAVES PER EACH VERTEX THE 
				if(!fetchedVertices.containsKey(message.getTarget())) {
					fetchedVertices.put(message.getTarget(), message.getPayload());
					Set<Long> initialSet = new HashSet<Long>();
					initialSet.add(message.getSender());
					fetchedVerticesIncoming.put(message.getTarget(), initialSet);
					if(!fetchedVerticesChance.containsKey(message.getTarget()))
						fetchedVerticesChance.put(message.getTarget(), Math.random());
				}else {
					fetchedVertices.put(message.getTarget(), fetchedVertices.get(message.getTarget()) + message.getPayload());	    										// ACTIVATION THRESHOLD AND INCOMING MSGS WEIGHT
					fetchedVerticesIncoming.get(message.getTarget()).add(message.getSender());
				}
			}
			//fetchedVertices.get(message.getTarget()).add(message.getPayload());
		}
		int anomalies = 0;
		//long activatedCounter = 0;
		for(Entry<Long, Double> entry : fetchedVertices.entrySet()) {
			long target = entry.getKey();
			double thr = fetchedVerticesChance.get(target);
			double incWeight = entry.getValue();
				
			if(incWeight >= 1.0d) {
				anomalies++;
				incWeight *= Math.random();
			}
			
			if(incWeight == 0 || incWeight < thr)
				continue;
			else {
				activatedNodesList.add(target);
			}
			//activatedCounter++;

			String vertexList = "";

			for(Long l : fetchedVerticesIncoming.get(target)) 
				vertexList += l + ",";				

			vertexList = vertexList.substring(0, vertexList.length() - 1);

			PreparedStatement pms = DBWizard.getInstance().createStatement(conn, DBWizard.getStringFromQuery(DBWizard.QUERY_TYPE.ACTIVATE_PINGED_VERTICES_FROM_TARGET).replace(DBWizard.LIST_PLACEHOLDER, vertexList));

			pms.setInt(1, currentStatus.getGraphId());
			pms.setLong(2, target);
			pms.setInt(3, currentStatus.getSimulationId());			
			pms.setInt(4, currentStatus.getRun());
			pms.setLong(5, currentStatus.getStep());

			ResultSet rs = pms.executeQuery();

			while(rs.next()) {
				long targetId = rs.getLong(1);
				Vertex v = new Vertex().id(targetId);
				Long[] neighbors = (Long[]) rs.getArray(2).getArray();
				Double[] weights = (Double[]) rs.getArray(3).getArray();
				int index = 0;
				for(long l : neighbors) {
					v.addNeighborsItem(new Edge().source(v.getId()).target(l).weight(weights[index]));
					index++;
				}
				//activeSet.add(v);
				cumulativeActiveVertices.put(target, v);
			}

			rs.close();
			pms.close();

		}

		//lastActivatedVertices = activatedCounter;
		log.info("LT found " + anomalies + " anomalies");
		return cumulativeActiveVertices.values();
	}

	@Override
	public void computeAndSend(Collection<Vertex> activeSet, Set<Message<Double>> messageCache, SimulationStatus currentStatus, Set<Long> activatedNodesList) throws Exception{
		if(currentStatus.getStep() == 1)
			for(Vertex v : activeSet)
				cumulativeActiveVertices.put(v.getId(), v);
		for (Vertex current : activeSet){
			List<Edge> edges = current.getNeighbors();
			for(Edge i : edges) {
				if(!activatedNodesList.contains(i.getTarget())) {
					messageCache.add(
							new Message<Double>(current.getId(), i.getTarget(), i.getWeight())//current.isSeed())
							);
					//lastSentMessages++;							
				}
			}
		}
	}

	@Override
	public String getName() {
		return "LT";
	}

	@Override
	public String getDescription() {
		return "A vertex activates only if a % of its neighbors is active already.";
	}

	@Override
	public void seedSetup(Set<Vertex> seeds) {
		// TODO Auto-generated method stub
	}

	@Override
	public void resetSimulationModel() {
		cumulativeActiveVertices.clear();
		fetchedVerticesChance.clear();
	}


//	@Override
//	public boolean isVertexListening(Vertex v, long step) {
//		// TODO Auto-generated method stub
//		return false;
//	}


}
