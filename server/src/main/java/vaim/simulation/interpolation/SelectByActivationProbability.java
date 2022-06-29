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
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.Lists;

import vaim.core.graph.Edge;
import vaim.core.graph.Edge.UnWeightedEdge;
import vaim.io.db.DBWizard;
import vaim.io.db.DBWizard.QUERY_TYPE;
import vaim.simulation.Common.SimulationStatus;

/**
 * @author Alessio Arleo
 *
 * This interpolation strategy compresses the times an edge has been activated during the different runs into the edge weight.
 */
public class SelectByActivationProbability extends InterpolationStrategy {

	public SelectByActivationProbability() {
		super();
	}

	public SelectByActivationProbability(String options) {
		super(options);
	}

	@Override
	public Set<Edge> interpolate(Connection conn, int graphId, int currentSimulationId, List<SimulationStatus> s)
			throws Exception {
		/*
		 * interpolatedEdges = new HashSet<Edge>();
		
		//### First: recover the edges activated thru all runs
		DBWizard dbW = DBWizard.getInstance();
//		PreparedStatement pms = dbW.createStatement(conn, QUERY_TYPE.GET_ACTIVATED_EDGES_COUNT);
		PreparedStatement pms = dbW.createStatement(conn, QUERY_TYPE.GET_ACTIVATED_EDGES_SINGLE);
		pms.setInt(1, currentSimulationId);

		ResultSet rs = pms.executeQuery();

		HashMap<Long, HashMap<Integer, List<UnWeightedEdge>>> edgesMap = new HashMap<Long, HashMap<Integer, List<UnWeightedEdge>>>();
		HashMap<Long, HashMap<Integer, Double>> activationsWeightMap = new HashMap<Long, HashMap<Integer, Double>>();
		HashMap<Long, Integer> activeAtMap = new HashMap<Long, Integer>();

		int maxStep = Integer.MIN_VALUE;
		int runs = s.length;

		//### Second: save the final activation edges
		while(rs.next()) {

			long source = rs.getLong(1);
			long target = rs.getLong(2);
			long seed = rs.getLong(3);
			int step = rs.getInt(4);
			
			if(!edgesMap.containsKey(target))
				edgesMap.put(target, new HashMap<Integer, List<UnWeightedEdge>>());
	
			if(!activationsWeightMap.containsKey(target))
				activationsWeightMap.put(target, new HashMap<Integer, Double>());			
			HashMap<Integer, List<UnWeightedEdge>> currV = edgesMap.get(target);
			HashMap<Integer, Double> currW = activationsWeightMap.get(target);
			
			if(!currV.containsKey(step))
				currV.put(step, new ArrayList<UnWeightedEdge>());
			
			Double weight = 0.0;
			if(currW.containsKey(step))
				weight = currW.get(step);
			
			List<UnWeightedEdge> hotEdges = currV.get(step);
			UnWeightedEdge currEdge = new UnWeightedEdge().source(source).target(target).seedId(seed).activeAt(step).weight(1.0);
			if(!hotEdges.contains(currEdge)) {				
				hotEdges.add(currEdge);	
			}else {
				UnWeightedEdge selectedEdge = hotEdges.get(hotEdges.indexOf(currEdge));
				selectedEdge.weight(selectedEdge.getWeight() + currEdge.getWeight());
				//UnWeightedEdge selectedEdge = hotEdges.get(hotEdges.indexOf(currEdge));
				//hotEdges.add(selectedEdge.weight(selectedEdge.getWeight() + currEdge.getWeight()));
			}
			currW.put(step, weight + currEdge.getWeight());
			maxStep = Math.max(maxStep, step);

		}
		

		rs.close();
		pms.close();
		
		SimulationStatus interpolated = new SimulationStatus(currentSimulationId, maxStep);
		
		long activatedVerticesSum = 0;
		long sentMessagesSum = 0;
		long deadVerticesSum = 0;
		
		for(Entry<Long, HashMap<Integer, Double>> v : activationsWeightMap.entrySet()) {
			long currTarget = v.getKey();
			HashMap<Integer, Double> weightData = v.getValue();			
			//HashMap<Integer, List<UnWeightedEdge>> vertexData = v.getValue();
			LinkedList<Integer> keySet = Lists.newLinkedList(weightData.keySet());
			Collections.sort(keySet, new Comparator<Integer>() {
				@Override
				public int compare(Integer o1, Integer o2) {			
					double valueOfo1 = weightData.get(o1) == null ? 0 : weightData.get(o1); 
					double valueOfo2 = weightData.get(o2) == null ? 0 : weightData.get(o2);
					
					if(valueOfo1 < valueOfo2)
						return -1;
					if(valueOfo1 > valueOfo2)
						return 1;
					return 0;
				}				
			});
			double sum = 0;
			for(double d : weightData.values())
				sum += d;
			if(sum >= Math.floor(runs/2))
				activeAtMap.put(currTarget, keySet.peekFirst());
		}
		
		for(Long currTarget : activeAtMap.keySet()) {	
			//HashMap<Integer, List<UnWeightedEdge>> vertexData = edgesMap.get(currTarget);			
			List<UnWeightedEdge> edges = edgesMap.get(currTarget).get(activeAtMap.get(currTarget));
			Collections.sort(edges, new Comparator<UnWeightedEdge>() {
				@Override
				public int compare(UnWeightedEdge o1, UnWeightedEdge o2) {
					if(o1 == null && o2 == null)
						return 0;
					double weight1 = o1.getWeight();
					double weight2 = o2.getWeight();
					if(weight1 < weight2)
						return 1;
					if(weight1 > weight2)
						return -1;
					return 0;
				}
				
			}); 
			int index = 0;
			while(index < edges.size()) {
				Edge selectedEdge = edges.get(index);
				long source = selectedEdge.getSource();
				if(source != selectedEdge.getSeedId() && !activeAtMap.containsKey(source))
					index++;
				else {
					int sourceActivationStep = source == selectedEdge.getSeedId() ? 0 : activeAtMap.get(source);
					interpolatedEdges.add(new Edge().source(source).target(currTarget)
						.weight(selectedEdge.getWeight()) 
						.activeAt(Math.max(sourceActivationStep, selectedEdge.getActiveAt()))
						.seedId(selectedEdge.getSeedId())
						);
					activatedVerticesSum++;
					break;
				}
			}
		}
		
		/*for(SimulationStatus curr : s) {
			activatedVerticesSum += curr.getActiveVertices();
			deadVerticesSum += curr.getDeadVertices();
			sentMessagesSum += curr.getSentMessages();
		}*/	

		/*activatedVerticesSum = Math.round(activatedVerticesSum/(float)s.length);
		sentMessagesSum = Math.round(sentMessagesSum/(float)s.length);
		deadVerticesSum = Math.round(deadVerticesSum/(float)s.length);*/
		/*
		interpolated.updateSimulation(activatedVerticesSum, deadVerticesSum, sentMessagesSum);

		return interpolated;
		 */
		return null;
	}

}
