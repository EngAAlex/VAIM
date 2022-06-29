package vaim.simulation.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import vaim.core.graph.Edge;
import vaim.core.graph.Vertex;
import vaim.io.db.DBWizard;
import vaim.simulation.Common.Message;
import vaim.simulation.Common.SimulationStatus;

public class IndependentCascadeModel extends DiffusionModel<Long> {

	public static final byte ACTIVATION_REQUEST = 1;
	
	public static final double GLOBAL_ACTIVATION_PROBABILITY = 0.35d;

	public IndependentCascadeModel() {
		super();
	}
	
	public IndependentCascadeModel(String options) {
		super(options);
	}

	@Override
	public Collection<Vertex> analyzeMessages(Set<Message<Long>> messageCache,
			SimulationStatus currentStatus, Set<Long> activatedNodesList) throws Exception{
				
		if(messageCache.isEmpty())
			return new HashSet<Vertex>();
		
		HashSet<Vertex> activeSet = new HashSet<Vertex>();

		HashMap<Long, List<Long>> fetchedVertices = new HashMap<Long, List<Long>>();

		for (Message<Long> message : messageCache) {			

			if(!fetchedVertices.containsKey(message.getSender()))
				fetchedVertices.put(message.getSender(), new LinkedList<Long>());
			fetchedVertices.get(message.getSender()).add(message.getTarget());
		}
		for(Entry<Long, List<Long>> entry : fetchedVertices.entrySet()) {
			long source = entry.getKey();
			List<Long> activatedList = entry.getValue();
			
			if(activatedList.size() == 0)
				continue;
			
			String vertexList = "";
			for(Long l : activatedList) 
				vertexList += l + ",";				
			
			vertexList = vertexList.substring(0, vertexList.length() - 1);
			
			PreparedStatement pms = DBWizard.getInstance().createStatement(conn, DBWizard.getStringFromQuery(DBWizard.QUERY_TYPE.ACTIVATE_PINGED_VERTICES_FROM_SOURCE).replace(DBWizard.LIST_PLACEHOLDER, vertexList));
			
			pms.setInt(1, currentStatus.getGraphId());
			pms.setLong(2, source);
			pms.setInt(3, currentStatus.getSimulationId());			
			pms.setInt(4, currentStatus.getRun());
			pms.setLong(5, currentStatus.getStep());
			
			ResultSet rs = pms.executeQuery();
						
			while(rs.next()) {
				Vertex v = new Vertex().id(rs.getLong(1));
				Long[] neighbors = (Long[]) rs.getArray(2).getArray();
				Double[] weights = (Double[]) rs.getArray(3).getArray();
				int index = 0;
				for(long l : neighbors) {
					v.addNeighborsItem(new Edge().source(v.getId()).target(l).weight(weights[index]));
					index++;
				}
				activeSet.add(v);
			}
			
			rs.close();
			pms.close();
						
		}
		
		//lastActivatedVertices = activeSet.size();

		return activeSet;
	}

	@Override
	public void computeAndSend(Collection<Vertex> activeSet, Set<Message<Long>> messageCache, SimulationStatus currentStatus, Set<Long> activatedNodesList) throws Exception{
		for (Vertex current : activeSet){
			List<Edge> edges = current.getNeighbors();
			for(Edge i : edges) {
				if(!activatedNodesList.contains(i.getTarget()) && Math.random() <= i.getWeight()){// (1 - Math.pow(1 - GLOBAL_ACTIVATION_PROBABILITY ,i.getWeight()))) {
					messageCache.add(
							new Message<Long>(current.getId(), i.getTarget(), (long)-1)//current.isSeed())
							);
					//lastSentMessages++;
					activatedNodesList.add(i.getTarget());
				}
			}
		}
	}
	
	@Override
	public String getName() {
		return "IC";
	}

	@Override
	public String getDescription() {
		return "Every vertex tries to activate the others when it gets 'it' and stay silent. Chance of activation/deactivation changes";
	}

	@Override
	public void seedSetup(Set<Vertex> seeds) {
		//NO-OP
	}

	@Override
	public void resetSimulationModel() {
		//NO-OP
	}


}