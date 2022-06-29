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

package vaim.simulation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.google.common.collect.Iterables;

import vaim.core.graph.Edge;
import vaim.core.graph.Graph;
import vaim.core.graph.Vertex;
import vaim.io.db.DBWizard;
import vaim.io.db.DBWizard.QUERY_TYPE;
import vaim.simulation.Common.Message;
import vaim.simulation.Common.SimulationStatistics;
import vaim.simulation.Common.SimulationStatistics.StepStatistics;
import vaim.simulation.Common.SimulationStatus;
import vaim.simulation.infmax.InfluenceMaximization;
import vaim.simulation.interpolation.InterpolateWithUncertainty;
import vaim.simulation.interpolation.InterpolationStrategy;
import vaim.simulation.models.DiffusionModel;

public class DiffusionSimulation {

	protected Logger log = Logger.getLogger(DiffusionSimulation.class);

	public static class RunnableSimulationWithRequestedNumberOfSeeds extends DiffusionSimulation implements Runnable {

		private final int noOfSeeds;
		private final InfluenceMaximization infMaxStrat;

		/*public RunnableSimulationWithRequestedNumberOfSeeds(int graphId, DiffusionModel model, InfluenceMaximization infMaxStrat, int noOfSeeds) throws Exception{
			super(graphId, model);
			this.noOfSeeds = noOfSeeds;
			this.infMaxStrat = infMaxStrat;
		}*/

		public RunnableSimulationWithRequestedNumberOfSeeds(int graphId, DiffusionModel model, InfluenceMaximization infMaxStrat, int noOfSeeds) throws Exception{
			super(graphId, model, infMaxStrat.getDescription());
			this.noOfSeeds = noOfSeeds;
			this.infMaxStrat = infMaxStrat;			
		}

		@Override
		public void run() {
			try {
				runSimulation(infMaxStrat.getSeeds(conn, graphId, noOfSeeds));
			}catch (Exception e) {
				log.error("ERROR while performing simulation");
			}
		}

	}

	public static class RunnableSimulationWithSeeds extends DiffusionSimulation implements Runnable {

		private final Collection<Long> seeds;

		public RunnableSimulationWithSeeds(int graphId, DiffusionModel strategy, Collection<Long> seeds) throws Exception{
			super(graphId, strategy);
			this.seeds = seeds;
			this.seedSelectionStrategy = "User";
		}		

		@Override
		public void run() {
			try {
				runSimulation(seeds);
			}catch (Exception e) {
				log.error("ERROR while performing simulation");
			}
		}

	}

	public static final byte VERTEX_ACTIVE = 1;
	public static final byte VERTEX_DEAD = 2;
	public static final byte VERTEX_NEUTER = 0;

	//	protected int id;

	int graphId;

	@SuppressWarnings("rawtypes")
	DiffusionModel diffusionModel;
	InterpolationStrategy interpolation = new InterpolateWithUncertainty();

	int maxRuns = 100;
	long stepCap = 500;

	private static final double EPSILON = 0.02;
	private static final int MINIMUM_RUNS = 0;

	protected int currentSimulationId;

	protected Connection conn;
	protected DBWizard dbW;
	protected String seedSelectionStrategy = "Random";
	private long tmpActiveSum = -1;

	//	public DiffusionSimulation(Graph g) {
	//		input = g;
	//	}
	//
	//	public DiffusionSimulation(Graph g, int runs) {
	//		this(g);
	//		this.runs = runs;
	//	}

	public DiffusionSimulation(int graphId, DiffusionModel model) throws Exception{
		this.graphId = graphId;
		this.diffusionModel = model;
		model.setInput(graphId);

		dbW = DBWizard.getInstance();
		conn = dbW.connect(false);
		model.setConnection(conn);

	}

	public DiffusionSimulation(int graphId, DiffusionModel model, String seedSelectionStrategy) throws Exception{
		this(graphId, model);
		this.seedSelectionStrategy = seedSelectionStrategy;
	}

	public DiffusionSimulation(int graphId, DiffusionModel model, int runs, int stepCap) throws Exception{
		this(graphId, model);
		this.maxRuns = runs;
		this.stepCap = stepCap;
	}

	public DiffusionSimulation(int graphId, DiffusionModel model, int runs, int stepCap, String seedSelectionStrategy) throws Exception{
		this(graphId, model, runs, stepCap);
		this.seedSelectionStrategy = seedSelectionStrategy;
	}

	public void setInterpolation(InterpolationStrategy strategy) {
		this.interpolation = strategy;
	}

	//	public SimulationStatistics runSimulation(long seeds) throws Exception{ //Run simulation with #seeds of random seeds
	//		dbW = DBWizard.getInstance();
	//		HashSet<Long> seedsArr = new HashSet<Long>();
	//		try {
	//
	//			PreparedStatement pms = dbW.createStatement(conn, QUERY_TYPE.GET_RANDOM_SEEDS);
	//			pms.setLong(1, graphId);
	//			pms.setLong(2, seeds);
	//
	//			ResultSet rs = pms.executeQuery();
	//			while(rs.next())				
	//				seedsArr.add(rs.getLong(1));
	//
	//			pms.close();
	//			return runSimulation(inf);
	//		}catch(Exception e) {
	//			dbW.fail(conn, e);
	//			throw e;
	//		}
	//
	//	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SimulationStatistics runSimulation(Collection<Long> seeds) throws Exception {
		int currentRun = 0;

		//SimulationStatus[] simulationRuns = new SimulationStatus[runs];
		LinkedList<SimulationStatus> simulationRuns = new LinkedList<SimulationStatus>();
		Collection<Vertex> activeSet;// = new HashSet<Vertex>();
		SimulationStatistics interpolated = null;

		log.info("Starting Simulation");

		long millisStart = System.currentTimeMillis();

		try {
			Set<Vertex> initialSet = preProcess(seeds);
			//diffusionStrategy.storeSeeds(seeds);
			int graphSize = Graph.getGraphVertexSize(conn, graphId);
			while(!stoppingCondition(simulationRuns)) {
				log.info("Beginning run " + (currentRun + 1));
				SimulationStatus currentStatus = new SimulationStatus(graphSize, currentSimulationId, currentRun, graphId);

				HashSet<Long> activatedNodesList = new HashSet<Long>(); 				
				activeSet = initialSet;
				activatedNodesList.addAll(seeds);

				HashSet<Message> messageCache = new HashSet<Message>();

				int currentRound = 1;

				diffusionModel.resetSimulationModel();
				do {				
					log.info("\tRound " + currentRound + " Starts");				
					diffusionModel.initRound();				
					diffusionModel.computeAndSend(activeSet, messageCache, currentStatus, activatedNodesList);
					log.info("\t\tMessage Cache size " + messageCache.size());
					activeSet = diffusionModel.analyzeMessages(messageCache, currentStatus, activatedNodesList);
					log.info("\t\tActiveSet size " + activeSet.size());

					currentStatus.updateSimulation(activatedNodesList.size() - currentStatus.getActiveVertices(), 0, messageCache.size());

					messageCache.clear();				
					currentStatus.stepForward();
					conn.commit();

					log.info("\t\tCumulative Activated vertices " + currentStatus.getActiveVertices());
					currentRound++;
				}while (/*currentStatus.neuterVertices > 0 && */ currentStatus.step < stepCap && currentStatus.getLastActiveVertices() > 0); //activeSet.size() > 0);
				currentStatus.closeSimulation();
				simulationRuns.add(currentStatus);
				currentRun++;
			}

			writeStatistics(currentRun);
			conn.commit();

			finalizeSimulation(interpolation.interpolate(conn, graphId, currentSimulationId, simulationRuns));

		}catch(Exception e) {
			dbW.fail(conn, e);
		}

		new Thread(() -> {
			try {
				cleanUp(); 
				conn.commit();
				//conn.close();
			}catch (Exception e) {
				dbW.fail(conn, e);
			} 
		}).start();

		long millisStop = System.currentTimeMillis();

		log.info("Simulation Elapsed time: " + ((int)(millisStop-millisStart)/1000) + " s");

		return interpolated;
	}	

	private boolean stoppingCondition(LinkedList<SimulationStatus> simulationRuns) {
		if(simulationRuns.size() == 0)
			return false;
		if(simulationRuns.size() >= maxRuns)
			return true;
		if(tmpActiveSum == -1)
			tmpActiveSum = simulationRuns.getLast().getActiveVertices();
		else {
			int noOfRuns = simulationRuns.size();
			long lastActive = simulationRuns.getLast().getActiveVertices();
			float lastAvg = tmpActiveSum/((float)noOfRuns - 1);
			tmpActiveSum += lastActive;
			float newAvg = tmpActiveSum/(float)noOfRuns;
			if(noOfRuns > MINIMUM_RUNS) {
				log.info("Checking stopping condition with: " + lastAvg + " " + newAvg + " " + Math.abs(lastAvg - newAvg)/lastAvg);
				if((Math.abs(lastAvg - newAvg)/lastAvg) < EPSILON)
					return true;			
			}
		}
		return false;
	}

	private void finalizeSimulation(Set<Edge> interpolatedEdges) throws Exception {
		writeEdges(interpolatedEdges);
		//computeAndStoreLayout(interpolator.getInterpolatedEdges());
	}

	/**
	 * @param interpolatedEdges
	 * @throws SQLException 
	 */
	//	private void computeAndStoreLayout(Set<Edge> interpolatedEdges) throws Exception {
	//		Graph currentGraph = new Graph().id(graphId);
	//				
	////		for(Edge e : interpolatedEdges) {
	////			if(currentGraph.getVertex(e.getSource()) == null)
	////				currentGraph.addVertex(new Vertex().id(e.getSource()));
	////			if(currentGraph.getVertex(e.getTarget()) == null)
	////				currentGraph.addVertex(new Vertex().id(e.getTarget()));				
	////		}
	//		
	//		PreparedStatement pms = conn.prepareStatement(DBWizard.getStringFromQuery(QUERY_TYPE.LIST_VERTICES));
	//		pms.setLong(1, graphId);
	//		
	//		ResultSet rs = pms.executeQuery();
	//		
	//		while(rs.next())
	//			currentGraph.addVertex(new Vertex().id(rs.getLong(1)));
	//		
	//		rs.close();
	//		pms.close();
	//		
	//		currentGraph.edgesSet(interpolatedEdges);
	//		
	//		SfdpExecutor sfdp = SfdpBuilder.BuildSfdpExecutor();
	//		sfdp.execute(currentGraph);
	//
	//		Double maxX = Double.MIN_VALUE;
	//		Double minX = Double.MAX_VALUE;
	//		Double maxY = Double.MIN_VALUE;
	//		Double minY = Double.MAX_VALUE;		
	//		
	//		
	//		for(Vertex vI : currentGraph.getVertices().values()) {
	//			VertexCoords coords = vI.getCoords();
	//			pms = conn.prepareStatement(DBWizard.getStringFromQuery(QUERY_TYPE.INSERT_SIMULATION_COORDINATES));
	//			pms.setLong(1, currentSimulationId);
	//			pms.setLong(2, vI.getId());
	//			pms.setLong(3,  graphId);
	//			pms.setDouble(4, coords.getX().doubleValue());
	//			pms.setDouble(5, coords.getY().doubleValue());
	//			
	//			if(pms.executeUpdate() == 0)
	//				throw new Exception("Could not assign coordinates to simulated vertex");				
	//			
	//			pms.close();
	//			
	//			maxX = Math.max(maxX, coords.getX().doubleValue());
	//			maxY = Math.max(maxY, coords.getY().doubleValue());
	//			minX = Math.min(minX, coords.getX().doubleValue());
	//			minY = Math.min(minY, coords.getY().doubleValue());	
	//		}			
	//		
	//		pms = conn.prepareStatement(DBWizard.getStringFromQuery(QUERY_TYPE.UPDATE_SIMULATION_BBOX));
	//		pms.setLong(1, currentSimulationId);
	//		pms.setDouble(2, maxX);
	//		pms.setDouble(3, maxY);
	//		pms.setDouble(4, minX);
	//		pms.setDouble(5, minY);
	//		
	//		if(pms.executeUpdate() == 0)
	//			throw new Exception ("Could not update simulation BBox");
	//		
	//		pms.close();			
	//		
	//	}

	private void writeEdges(Set<Edge> interpolatedEdges) throws Exception{
				
		Iterable<List<Edge>> parts = Iterables.partition(interpolatedEdges, (int)(Math.ceil(interpolatedEdges.size()/4)));		

		log.info("Preparing finalization for " + interpolatedEdges.size() + " edges");
		
		ArrayList<Boolean> t = new ArrayList<Boolean>();
		ArrayList<Boolean> excFlags = new ArrayList<Boolean>();
		for(List<Edge> pp : parts)
			t.add(false);
		for(List<Edge> pp : parts)
			excFlags.add(false);

		boolean semaphore = false;
		int i = 0;

		for(List<Edge> part : parts) {
			int iI = i;
			i++;
			new Thread(() -> {
				log.info("Starting thread " + iI + " for simulation finalization - to add " + part.size() + " edges");
				Connection tempConn = null; 
				try {
					tempConn = dbW.connect();
					for(Edge e : part) {
						PreparedStatement edgePms = dbW.createStatement(tempConn, QUERY_TYPE.INSERT_ACTIVATION_RELATIONSHIP);
						edgePms.setLong(1, e.getSource());
						edgePms.setInt(2, graphId);
						edgePms.setInt(3, graphId);
						edgePms.setLong(4, e.getTarget());
						edgePms.setLong(5, currentSimulationId);
						edgePms.setLong(6, e.getActiveAt());
						//edgePms.setLong(7, e.getSeedId());
						edgePms.setLong(7, (long)-1);
						edgePms.setDouble(8, e.getWeight());

						if(edgePms.executeUpdate() == 0)
							throw new Exception("Error while storing graphs");

						edgePms.close();
					}
					tempConn.commit();
					tempConn.close();

					log.info("Thread " + iI + " finished - Added " + part.size() + " activation relationships");
					t.set(iI,true);
					
					return;
				} catch(Exception exc) {
					dbW.fail(tempConn, exc);
					excFlags.set(iI,true);					
					try {
						tempConn.close();
					} catch (SQLException e) {
						//e.printStackTrace();
					}
					return;
				}
			}).start();						
		}

		while(!semaphore) {
			semaphore = true;
			for(boolean exce : excFlags)
				if(exce) {
					throw new Exception();
				}
			for(boolean b : t)
				semaphore &= b;
		}

		//		for(Edge e : interpolatedEdges) {
		//			PreparedStatement edgePms = dbW.createStatement(conn, QUERY_TYPE.INSERT_ACTIVATION_RELATIONSHIP);
		//
		//			edgePms.setLong(1, e.getSource());
		//			edgePms.setInt(2, graphId);
		//			edgePms.setInt(3, graphId);
		//			edgePms.setLong(4, e.getTarget());
		//			edgePms.setLong(5, currentSimulationId);
		//			edgePms.setLong(6, e.getActiveAt());
		//			//edgePms.setLong(7, e.getSeedId());
		//			edgePms.setLong(7, (long)-1);
		//			edgePms.setDouble(8, e.getWeight());
		//
		//			if(edgePms.executeUpdate() == 0)
		//				throw new Exception("Error while storing graphs");
		//
		//			edgePms.close();	
		//			
		//			log.info("Finalized Simulation - Added " + interpolatedEdges.size() + " activation relationships");
		//
		//		}
	}

	private Set<Vertex> preProcess(Collection<Long> seeds) throws Exception{
		Set<Vertex> initialSet = new HashSet<Vertex>();

		dbW = DBWizard.getInstance();
		PreparedStatement ps = null;
		ResultSet rs = null;
		//			= db.createStatement(QUERY_TYPE.GET_NO_OF_SIMULATIONS_FOR_GRAPH);
		//			ps.setInt(1, graphId);
		//			ResultSet rs = ps.executeQuery();
		//			rs.next();
		//			currentSimulationId = rs.getInt(1) + 1;
		//			ps.close();

		ps = dbW.createStatement(conn,QUERY_TYPE.INSERT_SIMULATION_NODE);
		ps.setInt(1, graphId);		
		ps.setString(2, seedSelectionStrategy);
		ps.setString(3, diffusionModel.getName());
		rs = ps.executeQuery();
		rs.next();
		currentSimulationId = rs.getInt(1);
		ps.close();

		ps = dbW.createStatement(conn,QUERY_TYPE.INSERT_SIMULATION_RELATIONSHIP);
		ps.setInt(1, graphId);
		//			ps.setInt(2, graphId);
		ps.setInt(2, currentSimulationId);
		ps.executeUpdate();
		ps.close();

		String seedList = "";

		for(Long l : seeds) {
			ps = dbW.createStatement(conn, QUERY_TYPE.INSERT_SEED_RELATIONSHIP);
			ps.setInt(1, currentSimulationId);
			ps.setInt(2, graphId);
			//				ps.setInt(3, graphId);
			ps.setLong(3, l);
			ps.executeUpdate();
			ps.close();

			seedList += l + ",";

		}
		conn.commit();

		seedList = seedList.substring(0, seedList.length() - 1);

		ps = DBWizard.getInstance().createStatement(conn, DBWizard.getStringFromQuery(DBWizard.QUERY_TYPE.GET_VERTICES_NEIGHBORHOODS).replace(DBWizard.LIST_PLACEHOLDER, seedList));

		ps.setInt(1, graphId);
		ps.setInt(2, graphId);

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
			initialSet.add(v);
		}

		rs.close();
		ps.close();

		return initialSet;
	}

	/**
	 * Removes all temporary data from the current simulation, commits
	 */
	private void cleanUp() throws Exception{
		PreparedStatement pms = dbW.createStatement(conn, QUERY_TYPE.CLEAN_UP_TEMP);
		//		pms.setInt(1, graphId);
		//		pms.setInt(2, graphId);
		pms.setInt(1, currentSimulationId);

		int removed = pms.executeUpdate();

		log.info("Executed cleanup: " + removed + " relationship deleted");

		pms.close();

	}

	private void writeStatistics(int rounds) throws Exception{
		ArrayList<StepStatistics> stepStats = new ArrayList<StepStatistics>();

		PreparedStatement pms = dbW.createStatement(conn, QUERY_TYPE.GET_STEP_by_STEP_SIMULATION_STATS);
		pms.setInt(1, currentSimulationId);
		//pms.setInt(2, maxStep);

		ResultSet rs = pms.executeQuery();		

		while(rs.next()) {
			stepStats.add(new StepStatistics(
					rs.getLong(3),
					rs.getLong(2),
					rs.getDouble(4),					
					rs.getDouble(5),
					rs.getDouble(6)
					));
		}

		rs.close();
		pms.close();

		pms = dbW.createStatement(conn, QUERY_TYPE.GET_FULL_SIM_STATS);					
		pms.setInt(1, currentSimulationId);

		rs = pms.executeQuery();
		rs.next();
		BigDecimal avg = new BigDecimal(rs.getDouble(1));
		BigDecimal stddev = new BigDecimal(rs.getDouble(2));
		int maxStep = rs.getInt(3);

		avg.setScale(3, RoundingMode.FLOOR);
		stddev.setScale(3, RoundingMode.FLOOR);

		rs.close();
		pms.close();

		pms = dbW.createStatement(conn, QUERY_TYPE.UPDATE_SIMULATION_STATISTICS);			

		pms.setInt(1, currentSimulationId);
		pms.setInt(2, rounds);
		pms.setInt(3, maxStep);
		pms.setFloat(4, avg.floatValue());
		pms.setFloat(5, stddev.floatValue());

		pms.executeUpdate();
		pms.close();

		for(int i = 0; i < stepStats.size(); i++) {
			StepStatistics s = stepStats.get(i);
			pms = dbW.createStatement(conn, QUERY_TYPE.ADD_STEP_STATISTICS);
			pms.setLong(1, s.getMaxActivated());
			pms.setLong(2, s.getMinActivated());
			pms.setDouble(3, s.getMedian());
			pms.setDouble(4, s.getFirstQuartile());
			pms.setDouble(5, s.getThirdQuartile());
			pms.setInt(6, currentSimulationId);			
			pms.setInt(7, i+1);


			if(pms.executeUpdate() == 0) {
				pms.close();
				throw new SQLException("Error while writing statistics");
			}

			pms.close();

		}

		log.info("Statistics writing OK");

	}

	public String getSeedSelectionStrategy() {
		return seedSelectionStrategy;
	}

	public void setSeedSelectionStrategy(String seedSelectionStrategy) {
		this.seedSelectionStrategy = seedSelectionStrategy;
	}

	/*private void updateDatabase(SimulationStatus status, Set<Long> seeds) {
		preProcess(status, seeds);
		DBWizard db = DBWizard.getInstance();
		PreparedStatement ps = null;s
		try {
			db.connect();
			for(long l : input.getVertexKeySet()) { //START FROM SEEDS
				Vertex v = input.getVertex(l);
				if(seeds.contains(v.getId()))
					continue;
				HashSet<StateChange> hssc = v.getStateChange();
				if(hssc == null)
					continue;
				for(StateChange sc : hssc) {
					ps = db.createStatement(QUERY_TYPE.INSERT_ACTIVATION_RELATIONSHIP);
					ps.setLong(1, sc.getSource());
					ps.setString(2, input.getName());
					ps.setString(3, input.getName());
					ps.setLong(4, l);
					ps.setInt(5, currentSimulationId);
					ps.setLong(6, sc.getStep());
					ps.setLong(7, v.getSeed());

					ps.executeUpdate();
				}				
			}
			db.commit();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			db.disconnect();
		}		
	}*/

	//private void finalUpdate(SimulationStatus status) {}

}
