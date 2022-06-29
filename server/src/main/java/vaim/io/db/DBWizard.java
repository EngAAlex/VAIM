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

package vaim.io.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.dbcp2.BasicDataSource;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import vaim.io.api.LoadGraphApiController;
import vaim.io.session.SessionControl;

@ConfigurationProperties(prefix = "neo4j")
public class DBWizard {

	public static final String LIST_PLACEHOLDER = "#_LIST_PLACEHOLDER_#";
	public static final String NUM_PLACEHOLDER = "#_NUM_PLACEHOLDER_#";
	public static final String BATCH_PLACEHOLDER = "#_BATCH_PLACEHOLDER_#";

	private static final Logger log = LoggerFactory.getLogger(DBWizard.class);

	public static enum QUERY_TYPE{
		GET_RANDOM_SEEDS,
		LIST_GRAPHS, 
		LIST_NODE_NEIGHBORS,
		LIST_SEEDS,		
		GET_GRAPH_SIZE,		
		INSERT_GRAPH_NODE,
		UPDATE_GRAPH_NODE_BOUNDS,
		INSERT_GRAPH_VERTEX, 
		GET_VERTICES_COMPLETE,
		GET_VERTICES_NEIGHBORHOODS,		
		INSERT_GRAPH_EDGE,
		INSERT_GRAPH_EDGE_BATCH,
		INSERT_SIMULATION_NODE,
		INSERT_SIMULATION_RELATIONSHIP,
		INSERT_SEED_RELATIONSHIP, INSERT_ACTIVATION_RELATIONSHIP,
		GET_VERTEX_INFO, 
		INSERT_TEMP_ACTIVATION_RELATIONSHIP,
		CLEAN_UP_TEMP, 
		UPDATE_SIMULATION_STATISTICS, 
		GET_SIMULATION_FRAME, 
		CREATE_OR_UPDATE_SESSION, 
		RECOVER_SESSION, 
		GET_ACTIVATED_EDGES_WITH_CHANCE, 
		GET_STEP_by_STEP_SIMULATION_STATS,
		ADD_STEP_STATISTICS, GET_STEP_STATISTICS, 
		GET_SINGLE_VERTEX_NEIGHBORS, 
		ACTIVATE_PINGED_VERTICES, ACTIVATE_PINGED_VERTICES_FROM_SOURCE, ACTIVATE_PINGED_VERTICES_FROM_TARGET,
		INSERT_NEW_MATRIX_CELL_EDGE, 
		GET_DENSITY_MATRIX, GET_VERTEX_CELL, 
		GET_SIMULATIONS_FOR_GRAPH, GET_SUBSET_OF_VERTICES_COMPLETE, 
		GET_SIMULATION_DATA, GET_ACTIVATED_VERTICES_IN_SIMULATION, 
		GET_FULL_SIM_STATS, TEST_SQUARES, GET_GRAPH_BOUNDS, GET_GRAPH_INFO, GET_SUBSET_OF_VERTICES_SIMPLE,
		GET_STRUCTURAL_EDGES_FROM_LIST, GET_ACTIVATED_VERTICES_IN_SIMULATION_FROM_LIST, INSERT_NEW_MATRIX_CELL_EDGE_GROUP;
		//GET_GRAPHS_SIZE, GET_GRAPHS_EDGE_SIZE,		
		//LIST_VERTICES,
		//GET_SIMULATION_FRAME_PER_SEED, 
		//INSERT_SIMULATION_COORDINATES, UPDATE_SIMULATION_BBOX, 
		//GET_SIMULATION_COORDINATES, 
		//GET_ACTIVATED_EDGES_SINGLE, GET_ACTIVATED_EDGES_COUNT, 
		//GET_SIMULATION_FRAME_FROM_SEED, 
		//GET_VERTEX_PATHS, 
		//REMOVE_RUN_PROPERTY, 
		//GET_VERTEX_ACTIVATION_STATUS, 
		//GET_NO_OF_SIMULATIONS_FOR_GRAPH, 
		//SET_GRAPH_VERTEX_PROPERTY,
		//, LIST_SIMULATIONS,

	}

	private static DBWizard instance = null;	
	//private static Class<?> driverClass;
	private BasicDataSource connectionPool;
	private static int initialPoolSize = 15;
	private final String driverClassName;
	private final String address;
	private final String user;
	private final String password;

	//private Connection conn = null;
	//private boolean autoShutdown = false;

	private DBWizard() throws Exception {

		Properties props = new Properties();
		try {
			props.load(getClass().getResourceAsStream("/application.properties"));
			driverClassName=props.getProperty("neo4j.driverclassname");
			address = props.getProperty("neo4j.address");
			user = props.getProperty("neo4j.user");
			password = props.getProperty("neo4j.password");

		}catch(Exception e) {
			log.error(e.getMessage(), e);;
			throw new Exception ("Unable to load Properties");
		}

		connectionPool = new BasicDataSource();
		connectionPool.setDriverClassName(driverClassName);
		connectionPool.setUrl(address);
		connectionPool.setUsername(user);
		connectionPool.setPassword(password);
		connectionPool.setInitialSize(initialPoolSize);
		connectionPool.setAutoCommitOnReturn(false);

	}

	public static DBWizard getInstance() {
		try {	
			if(instance == null)
				instance = new DBWizard();		
			return instance;
		}catch(Exception e) {
			return null;
		}
	}

	public Connection connect() throws Exception{
		return connect(false);
	}

	public Connection connect(boolean autocommit) throws Exception {
		try {				
			Connection conn = connectionPool.getConnection();			
			conn.setAutoCommit(autocommit);
			return conn;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean disconnect() {
		try {
			connectionPool.close();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public PreparedStatement createStatement(Connection conn, String s) {
		PreparedStatement result = null;
		try {
			return conn.prepareStatement(s);
		}catch(Exception e) {
			fail(conn, e);
		}
		return result;
	}

	public PreparedStatement createStatement(Connection conn, DBWizard.QUERY_TYPE query) {
		PreparedStatement result = null;
		try {
			result = conn.prepareStatement(queryTransform(query));
		}
		catch(InputMismatchException me) {
			fail(conn, me);
		}
		catch(Exception e) {			
			fail(conn, e);
		}
		return result;
	}

	/*public int executeUpdateStatement(PreparedStatement smt) {
		int modifiedRows = 0;
		try {
			modifiedRows = smt.executeUpdate();
		}
		catch(Exception e) {
			e.printStackTrace();
			modifiedRows = -1;
		}
		return modifiedRows;
	}*/

	//	public ResultSet executeSelectStatement(PreparedStatement smt) {
	//		ResultSet result = null;
	//		try{
	//			result = smt.executeQuery();
	//		}catch(SQLException e) {
	//			disconnect();
	//		}
	//		return result;
	//	}

	public boolean commit(Connection conn) {		
		try {
			conn.commit();
		}catch(Exception e) {			
			try {
				conn.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		}
		return true;
	}

	public void rollback(Connection conn) {
		try {
			conn.rollback();
		}catch(Exception e) {			
		}
	}

	/*public boolean isAutoShutdown() {
		return autoShutdown;
	}*/

	public static String getStringFromQuery(DBWizard.QUERY_TYPE query) {
		return queryTransform(query);
	}

	private static String queryTransform(DBWizard.QUERY_TYPE query) throws InputMismatchException{
		switch(query) {
		case GET_VERTEX_CELL:
			return "Match (g:Graph)-[r:MatrixCell]->(n:Vertex) where id(g) = ? and r.resolution = ? \n"
			+ "return n.id, r.x, r.y ORDER BY n.id";
		case GET_DENSITY_MATRIX:
			return "MATCH (g:Graph)-[r:MatrixCell]->(n:Vertex) WHERE id(g) = ? and r.resolution = ? \n"
			+ "RETURN r.x, r.y, COUNT(n) order by r.y, r.x";
		case INSERT_NEW_MATRIX_CELL_EDGE :
			return "MATCH (g:Graph), (n:Vertex) WHERE id(g) = ? AND n.graph = ? AND n.id = ? \n"
			+ "CREATE (g)-[s:MatrixCell {resolution: ?, x: ?, y: ?}]->(n) RETURN s;";
		case INSERT_NEW_MATRIX_CELL_EDGE_GROUP :
			return "MATCH (g:Graph), (n:Vertex) WHERE id(g) = ? AND n.graph = ? AND n.id IN [" + LIST_PLACEHOLDER + "] \n"
			+ "CREATE (g)-[s:MatrixCell {resolution: ?, x: ?, y: ?}]->(n) RETURN s;";
		case GET_RANDOM_SEEDS:
			return "MATCH (n:Vertex) WHERE rand() > 0.5 and n.graph = ? RETURN n.id LIMIT ?";
		case LIST_GRAPHS:
			return "MATCH (g:Graph) OPTIONAL MATCH (g)-[r:Simulated]->(sim:Simulation) RETURN id(g), g.name, g.nodes, g.edges, COUNT(r)";		
		case GET_GRAPH_SIZE:
			return "MATCH (n:Vertex) WHERE n.graph = ? RETURN count(n)"; 			
		case LIST_SEEDS: 
			return "MATCH (sim:Simulation)-[r:Seed]->(seeds:Vertex) WHERE id(sim) = ? RETURN COLLECT(seeds.id)";
		case LIST_NODE_NEIGHBORS:
			return "MATCH (Vertex { globalId: ? })->(neighbors:Vertex) \n" + 
			"RETURN neighbors.id";
		case INSERT_GRAPH_NODE:
			return "CREATE (n:Graph { name: ?, nodes: ?, edges: ?}) RETURN id(n)";
		case GET_GRAPH_INFO:
			return "MATCH (g:Graph) WHERE id(g) = ? RETURN g.name, g.nodes, g.edges, g.maxX - g.minX as width, g.maxY - g.minY as height, g.minX, g.maxX, g.minY, g.maxY";			
		/*case GET_GRAPH_BOUNDS:
			return "MATCH (n:Graph) WHERE id(n) = ? RETURN n.maxX, n.minX, n.maxY, n.minY";*/
		case UPDATE_GRAPH_NODE_BOUNDS:
			return "MATCH (n:Graph) WHERE id(n) = ? SET n.minX = ?, n.minY = ?, n.maxX = ?, n.maxY = ?";
		case INSERT_SIMULATION_NODE:
			return "CREATE (n:Simulation { inprogress: true, graph: ?, selectionStrategy: ?, diffusionStrategy: ?, timeStamp: datetime()}) RETURN id(n)"; 
		case INSERT_GRAPH_VERTEX:
			return "CREATE (n:Vertex { id: ?, graph: ?, x: ?, y: ?, degree: ?})";
		case GET_VERTEX_INFO:
			return "MATCH (n:Vertex) "
			+ "WHERE n.graph = ? AND n.id = ?"
			+ " RETURN  n.id, n.attributes";	
		case GET_VERTICES_COMPLETE:
			return "MATCH (n:Vertex) -[r:Edge]- (m:Vertex) WHERE n.graph = ? return n.x, n.y, n.id, collect(m.id), collect(r.weight)";
		case TEST_SQUARES:
			return "MATCH (g:Graph)-[r:MatrixCell]->(n:Vertex) "
			+ "WHERE id(g) = ? AND r.resolution = ? AND r.x >= ? AND r.x < ? AND r.y >= ? AND r.y < ? \n"
			+ "RETURN n.id, r.x, r.y ORDER BY r.x, r.y";
		case GET_SUBSET_OF_VERTICES_SIMPLE:
			return "MATCH (g:Graph)-[s:MatrixCell]->(n:Vertex)  \n"
					+ "					WHERE id(g) = ? AND s.resolution = ? AND s.x >= ? AND s.x < ? AND s.y >= ? AND s.y < ? \n"
					+ "					RETURN n.id, n.x, n.y, s.x, s.y, n.degree ORDER BY n.id";					
		case GET_SUBSET_OF_VERTICES_COMPLETE:
			return "MATCH (g:Graph)-[s:MatrixCell]->(n:Vertex) \n"
					+ "WHERE id(g) = ? AND s.resolution = ? AND s.x >= ? AND s.x < ? AND s.y >= ? AND s.y < ? \n"
					+ "WITH collect(n) AS ses \n"
					+ "	UNWIND ses AS g \n"
					+ " UNWIND ses AS m \n"
					+ "	OPTIONAL MATCH (g)-[r:Edge]->(m) WITH g, collect(endnode(r).id) as targets, collect(r.weight) as weights \n"
					+ "  OPTIONAL MATCH (g)-[x:Edge]->() WITH g, targets, weights, count(x) as degree \n"
					+ "	  MATCH ()-[s:MatrixCell]->(g) WHERE s.resolution = ? \n"
					+ "RETURN g.id, g.x, g.y, s.x, s.y, targets, weights, degree";
			/*return "MATCH (g:Graph)-[s:MatrixCell]->(n:Vertex) "
			+ "WHERE id(g) = ? and s.resolution = ? AND s.x >= ? AND s.x < ? AND s.y >= ? AND s.y < ? \n"
			+ "WITH collect(n) AS nodes, s AS cells \n"
			+ "UNWIND nodes AS n \n"
			+ "UNWIND nodes AS m \n"
			+ "OPTIONAL MATCH (n)-[r:Edge]->(m) RETURN n.id, n.x, n.y, COLLECT(endnode(r).id), COLLECT(r.weight), cells.x, cells.y";*/			
		case GET_STRUCTURAL_EDGES_FROM_LIST: 
			return "MATCH (n:Vertex) -[r:Edge]-> (m:Vertex) "
			+ "WHERE n.graph = ? \n" //"n.id IN [" + LIST_PLACEHOLDER + "] AND " ######REACTIVATE TO AVOID LOADING INVISIBLE VERTICES
			+ "AND m.id IN [" + LIST_PLACEHOLDER + "] RETURN  n.id, n.x, n.y, COLLECT(endnode(r).id) AS targets, COLLECT(r.weight) AS weights ORDER BY n.id";			
		case GET_SINGLE_VERTEX_NEIGHBORS:
			return "MATCH (n:Vertex) -[r:Edge]-> (m:Vertex) "
			+ "WHERE n.graph = ? AND m.graph = ? AND n.id = ?"
			+ " RETURN  m.id,r.weight";
		case GET_VERTICES_NEIGHBORHOODS:
			return "MATCH (n:Vertex) -[r:Edge]-> (m:Vertex) \n"
			+ "			WHERE n.graph = ? AND m.graph = ? AND n.id IN ["+ LIST_PLACEHOLDER + "] \n"
			+ "RETURN n.id, COLLECT(m.id),COLLECT(r.weight)";
		case ACTIVATE_PINGED_VERTICES_FROM_SOURCE:
			return 	/*"MATCH (t:Vertex) -[r:Activates]-> (m:Vertex) \n"
					+ "	WHERE r.simulation = ? AND r.run = ? AND m.id IN ["+ LIST_PLACEHOLDER + "] \n"
					+ "    WITH COLLECT(m.id) AS active_vertices \n"					 
					+ */
					"MATCH (n:Vertex) -[r:Edge]-> (m:Vertex) \n"
					+ "			WHERE n.graph = ? AND n.id = ? \n"
					+ "         AND m.id IN ["+ LIST_PLACEHOLDER + "] \n"//AND NOT (m.id IN active_vertices) \n"
					+ "    CREATE (n)-[s:Activates {simulation: ?, run: ?, step: ?}]->(m) \n"
					+ "WITH m \n"
					+ "MATCH (m:Vertex) -[r:Edge]-> (v:Vertex) \n"					
					+ "RETURN m.id, COLLECT(v.id), COLLECT(r.weight)";		
		case ACTIVATE_PINGED_VERTICES_FROM_TARGET:
			return 	/*"MATCH (t:Vertex) -[r:Activates]-> (m:Vertex) \n"
					+ "	WHERE r.simulation = ? AND r.run = ? AND m.id IN ["+ LIST_PLACEHOLDER + "] \n"
					+ "    WITH COLLECT(m.id) AS active_vertices \n"					 
					+ */
					"MATCH (n:Vertex) -[r:Edge]-> (m:Vertex) \n"
					+ "			WHERE n.graph = ? AND m.id = ? \n"
					+ "         AND n.id IN ["+ LIST_PLACEHOLDER + "] \n"//AND NOT (m.id IN active_vertices) \n"
					+ "    CREATE (n)-[s:Activates {simulation: ?, run: ?, step: ?}]->(m) \n"
					+ "WITH m \n"
					+ "MATCH (m:Vertex) -[r:Edge]-> (v:Vertex) \n"					
					+ "RETURN m.id, COLLECT(v.id), COLLECT(r.weight)";					
		case ACTIVATE_PINGED_VERTICES:
			return 	"MATCH (t:Vertex) -[r:Activates]-> (m:Vertex) \n"
			+ "	WHERE r.simulation = ? AND r.run = ? \n"
			+ "    WITH COLLECT(m.id) AS active_vertices \n"
			+ "MATCH (s:Simulation) -[t:Seed]-> (n:Vertex)"
			+ " WHERE id(s) = ? \n"
			+ "	   WITH active_vertices, COLLECT (n.id) AS seed_vertices \n"
			+ "MATCH (n:Vertex) -[r:Edge]-> (m:Vertex) \n"
			+ "			WHERE n.graph = ? AND m.graph = ? AND n.id IN ["+ LIST_PLACEHOLDER + "]\n"
			+ "         AND NOT (m.id IN seed_vertices) AND NOT (m.id IN active_vertices) \n"
			+ "    WITH n,r,m \n"
			+ "    CREATE (n)-[s:Activates {simulation: ?, run: ?, step: ?}]->(m) \n"
			+ "RETURN n.id, COLLECT(m.id), COLLECT(r.weight)";
			//		case SET_GRAPH_VERTEX_PROPERTY:
			//			return "MATCH (n:Vertex { id: ?, graph: ?}) SET n.? = ?"; 			
		case INSERT_GRAPH_EDGE:
			return "MATCH (a:Vertex),(b:Vertex) \n" +  
			"WHERE a.graph = ? AND a.id = ? AND b.graph = ? AND b.id = ? \n " + 
			"CREATE (a)-[r:Edge {weight: ?}]->(b)"; //+ "RETURN type(r)";

		case INSERT_GRAPH_EDGE_BATCH:
			return "UNWIND ["+ BATCH_PLACEHOLDER +"] as row \n"   
			+ "MATCH (n:Vertex) WHERE n.graph = row.graphId AND n.id = row.from \n"
			+ "MATCH (m:Vertex) WHERE m.graph = row.graphId AND m.id = row.to \n"
			+ "CREATE (n)-[r:Edge {weight: row.weight}]->(m)"; //+ "RETURN type(r)";
		
		case INSERT_SIMULATION_RELATIONSHIP:
			return "MATCH (a:Graph),(b:Simulation) \n" +  
			"WHERE id(a) = ? AND id(b) = ? \n " + 
			"CREATE (a)-[r:Simulated]->(b)"; //+ "RETURN type(r)";
		case INSERT_SEED_RELATIONSHIP:
			return "MATCH (a:Simulation),(b:Vertex) \n" +  
			"WHERE id(a) = ? AND b.graph = ? AND b.id = ? " + 
			"CREATE (a)-[r:Seed]->(b) \n"
			+"RETURN type(r)";
		case INSERT_ACTIVATION_RELATIONSHIP:
			return "MATCH (a:Vertex),(b:Vertex) \n" +  
			"WHERE a.id = ? AND a.graph = ? AND b.graph = ? AND b.id = ? \n " + 
			"CREATE (a)-[r:Activates {simulation: ?, step: ?, seed: ?, weight: ? }]->(b) \n"; //+ "RETURN type(r)";
		case INSERT_TEMP_ACTIVATION_RELATIONSHIP:
			return "MATCH (a:Vertex),(b:Vertex) \n" +  
			"WHERE a.id = ? AND a.graph = ? AND b.graph = ? AND b.id = ? \n " + 
			"CREATE (a)-[r:Activates {simulation: ?, run: ?, step: ?, seed: ? }]->(b) \n"; //+ "RETURN type(r)";			
		case CLEAN_UP_TEMP: 
			return "MATCH (n:Vertex)-[r:Activates]->(m:Vertex) "
			+ "WHERE r.simulation = ? AND r.run IS NOT NULL DELETE r";				
		case UPDATE_SIMULATION_STATISTICS:
			return "MATCH (n:Simulation) WHERE id(n) = ? REMOVE n.inprogress SET n.runs = ?, n.max_steps = ?, n.avg_activated = ?, n.std_dev = ?";
		case ADD_STEP_STATISTICS:
			return "CREATE (n:Statistics {max: ?, min: ?, median: ?, lowQuart: ?, highQuart: ?})"
			+ "WITH n "
			+ "MATCH (m:Simulation) WHERE id(m) = ? "
			+ " CREATE (m)-[r:Stats {step: ?}]->(n) RETURN r"; 
		case GET_STEP_STATISTICS:
			return "MATCH (n:Simulation)-[r:Stats]-(m) WHERE \n"
			+ "id(n) = ? AND r.step > ? AND  r.step <= ? RETURN \n"
			+ "r.step, m.max, m.min, m.median, m.lowQuart, m.highQuart ORDER BY r.step";
		case GET_SIMULATION_FRAME:
			return "MATCH (s:Vertex)-[r:Activates]->(t:Vertex) \n" + 
			"WHERE r.simulation = ? AND r.step > ? AND  r.step <= ? \n" + 
			"RETURN t.id, AVG(r.weight)";
		case GET_ACTIVATED_EDGES_WITH_CHANCE:
			return /*"MATCH ()-[r:Activates]-() where r.simulation = ? and r.run IS NOT NULL \n"
					+ "WITH r.run as darun, MAX(r.step + 1) as t_mstep \n"
					+ "WITH CEIL(AVG(t_mstep)) as mstep \n"
					+ "MATCH (t:Simulation) where id(t) = ? \n"
					+ "WITH t.iterations as iterations, mstep \n"
					+ */
					"MATCH (n)-[r:Activates]->(m) WHERE r.simulation = ? AND r.run IS NOT NULL \n"
					+ "RETURN n.id,m.id,r.step, ROUND(COUNT(r)*1.0/" + NUM_PLACEHOLDER + ",2) AS chance ORDER by n.id,m.id,r.step";
		case GET_FULL_SIM_STATS: 			
			return "MATCH ()-[r:Activates]->(m:Vertex) WHERE r.simulation = ? AND r.run IS NOT NULL \n"
			+ "WITH r.run AS RUN, count(DISTINCT m) AS count, MAX(r.step) as m_step \n"
			+ "RETURN AVG(count), STDEV(count), MAX(m_step)";
		case GET_STEP_by_STEP_SIMULATION_STATS:
			return "MATCH ()-[r:Activates]->(m:Vertex) where r.simulation = ? and r.run IS NOT NULL \n" //and r.step <= ? 
					+ "with r.run as darun, r.step as dastep, count(DISTINCT m) as dacount \n"
					+ "return dastep, MIN(dacount), max(dacount), percentileCont(dacount, 0.25), percentileCont(dacount, 0.50), percentileCont(dacount, 0.75) order by dastep";
		case CREATE_OR_UPDATE_SESSION:
			return "MERGE (n:Session {sessionId: ?}) ON MATCH SET " + SessionControl.ON_SET_PLACEHOLDER + " ON CREATE SET n.sessionId = ?," + SessionControl.ON_SET_PLACEHOLDER + " RETURN n";
		case RECOVER_SESSION:
			return "MATCH (n:Session) WHERE n.sessionId = ? RETURN n";
		case GET_SIMULATIONS_FOR_GRAPH: 
			return "MATCH (g:Graph)-[r:Simulated]->(s:Simulation) WHERE id(g) = ? AND s.inprogress IS NULL WITH s MATCH (s)-[:Seed]->(n:Vertex) \n"
			+ "RETURN id(s), s.avg_activated, COUNT(n), s.runs, s.max_steps, s.selectionStrategy, s.diffusionStrategy, s.std_dev ORDER BY id(s) DESC"; //s.avg_activated
		case GET_SIMULATION_DATA:
			return "MATCH (s:Simulation) WHERE id(s) = ? RETURN s.max_steps, s.iterations, s.avg_activated, s.selectionStrategy, s.diffusionStrategy, s.std_dev";
		case GET_ACTIVATED_VERTICES_IN_SIMULATION_FROM_LIST:
			return "MATCH (g:Vertex)-[r:Activates]->(m) \n"
			+ "WHERE r.simulation = ? AND r.run IS NULL " //AND g.id IN [" + LIST_PLACEHOLDER + "] \n" #####REACTIVATE TO AVOID LOADING INVISIBLE VERTICES
			+ "AND m.id IN [" + LIST_PLACEHOLDER + "] \n"							
			+ "RETURN g.id AS id, r.step AS step, r.weight AS weight, m.id AS target ORDER BY step \n"
			+ "UNION \n"
			+ "MATCH (g:Simulation)-[f:Seed]->(n) WHERE id(g) = ? \n"
			+ "RETURN n.id AS id, null AS step, null AS weight, null AS target ORDER BY step";		
		case GET_ACTIVATED_VERTICES_IN_SIMULATION:
			return "MATCH (g:Graph)-[r:MatrixCell]->(n:Vertex) WHERE id(g) = ? and r.resolution = ? and r.x >= ? and r.x < ? and r.y >= ? and r.y < ?\n"
			+ "WITH collect(n) as nodes \n"
			+ "UNWIND nodes as n \n"
			+ "UNWIND nodes as m \n"
			+ "MATCH (g:Vertex)-[r:Activates]->(m) WHERE r.simulation = ? AND r.run IS NULL return g.id AS id, r.step AS step, r.weight AS weight, m.id AS target order BY step \n"
			+ "UNION \n"
			+ "MATCH (g:Simulation)-[f:Seed]->(n) WHERE id(g) = ? \n"
			+ "RETURN n.id AS id, null AS step, null AS weight, null AS target order BY step";
			/*		
			 * //		case LIST_SIMULATIONS:
//			return "MATCH (g:Graph)-[r:Simulated]->(sim:Simulation) \n"
//			+ "WHERE id(g) = ? \n"
//			+ "RETURN sim.id, sim.iterations, sim.steps, sim.activated";	
			 * //		case REMOVE_RUN_PROPERTY:
//			return "MATCH (n:Vertex)-[r:Activates]->(m:Vertex) "
//			+ "WHERE n.graph = ? AND m.graph = ? AND r.simulation = ? AND r.run = ? REMOVE r.run ";
//			case GET_VERTEX_ACTIVATION_STATUS: 
//			return "MATCH (n:Vertex) where n.graph = ? and n.id = ? with n \r\n" + 
//			"OPTIONAL MATCH (g:Simulation) -[s:Seed]-> (n) where id(g) = ? and g.graph = ? \r\n" + 
//			"with g,n \r\n" + 
//			"OPTIONAL MATCH (m:Vertex) -[r:Activates]-> (n:Vertex) \r\n" + 
//			"	WHERE m.graph = ? AND r.simulation = ? AND r.run = ? \r\n" + 
//			"		RETURN n.id, id(g), m.id, r.seed";
//		case GET_NO_OF_SIMULATIONS_FOR_GRAPH:
//			return "MATCH(g:Graph)-[:Simulated]->(s:Simulation) where g.name = ? RETURN count(s);";

			 * case GET_ACTIVATED_VERTICES_IN_SIMULATION:
			return "MATCH (n:Simulation)-[r:Seed]-(m:Vertex) WHERE id(n) = ? AND m.id IN [ " + LIST_PLACEHOLDER + " ] RETURN n.id, m.id, r.step, r.seed, r.weight "
					+ "UNION MATCH (n:Vertex)-[r:Activates]->(m:Vertex) WHERE r.simulation = ? AND n.id IN [ " + LIST_PLACEHOLDER + " ] AND m.id IN [ " + LIST_PLACEHOLDER + " ] RETURN n.id, m.id, r.step, r.seed, r.weight ORDER BY r.step";								
		case INSERT_SIMULATION_COORDINATES:
			return "MATCH (n:Simulation),(m:Vertex) WHERE id(n) = ? AND m.id = ? AND m.graph = ? CREATE (n)-[r:Coordinate {x: ?, y: ?}]->(m) RETURN id(r)";
		case GET_SIMULATION_COORDINATES:
			return "MATCH (n:Simulation)-[r:Coordinate]->(m:Vertex) WHERE id(n) = ? AND m.id IN [ " + LIST_PLACEHOLDER + " ] RETURN m.id, r.x, r.y"; 
		case UPDATE_SIMULATION_BBOX:
			return "MATCH (n: Simulation) WHERE id(n) = ? SET n.maxX = ?, n.maxY = ?, n.minX = ?, n.minY = ?";
			//			return "MATCH (s:Simulation)-[r:Seed]->(:Vertex) WHERE id(s) = ? RETURN s.steps, s.graph, s.iterations, s.activated";
			//		case GET_SIMULATION_FRAME:
			//			return "MATCH (s:Vertex)-[r:Activates]->(t:Vertex) \n" + 
			//					"WHERE r.simulation = 95 AND r.step < 2 \n" + 
			//					"WITH t \n" + 
			//					"MATCH p = (t)-[rs:Activates*1..5{simulation: 95}]->(s:Vertex) \n" + 
			//					"Return t.id, s.id, Length(p), [g IN nodes(p)| g.id] As path, collect(s.id) \n" + 
			//					"Order By Length(p) ";
			//		case GET_SIMULATION_FRAME_FROM_SEED:
			//			return "MATCH (s:Simulation)-[r:Seed]->(t:Vertex) \n" + 
			//				   "WHERE id(s) = ? " + 
			//				   "WITH t " +
			//				   "MATCH p = (t)-[rs:Activates*1..@{simulation: ?}]->(s:Vertex) " +
			//				   "Return t.id, s.id, Length(p), [g IN nodes(p)| g.id] As path, collect(s.id) " +
			//				   "Order By Length(p)";
		case GET_SIMULATION_FRAME_PER_SEED:
			return "MATCH (s:Vertex)-[r:Activates]->(t:Vertex) \n" + 
			"WHERE r.simulation = ? AND r.step > ? AND  r.step <= ? \n" + 
			"RETURN collect(s.id), collect(t.id), r.seed";
		case GET_ACTIVATED_EDGES_COUNT:
			return "Match (n:Vertex)-[r:Activates]->(m:Vertex) where r.simulation = ? return n.id, m.id, count(r), r.seed, avg(r.step) order by count(r) desc";
		case GET_ACTIVATED_EDGES_SINGLE:
			return "Match (n:Vertex)-[r:Activates]->(m:Vertex) where r.simulation = ? return n.id, m.id, r.seed, r.step order by m.id desc";
						//return "MATCH (n:Vertex) WHERE n.graph = ? return n.x, n.y, n.id, n.graph";
//		case GET_VERTEX_PATHS:
//			return "Match (sourceNode:Vertex {graph: ?, id: ?}) \n" + 
//			"  With sourceNode \n" + 
//			"  Match path = (sourceNode)-[r:Edge*1..@]->(destNode:Vertex) \n" + 					
//			"With destNode, path  \n" + 
//			"      Order By Length(path) ASC \n" + 
//			"    	Match (destNode)-[r]->(neighbors:Vertex) \n" + 
//			"Return destNode.id, Length(path), [p IN nodes(path)| p.id] As path, COLLECT(neighbors.id)\r\n" + 
//			"Order By Length(path) ASC";
			 * //		case GET_GRAPHS_SIZE:
//			return "MATCH (n:Vertex) RETURN n.graph, count(n)";			
//		case GET_GRAPHS_EDGE_SIZE:
//			return "MATCH (n:Vertex)-[r:Edge]->(m:Vertex) RETURN n.graph, COUNT(r)";
//			case LIST_VERTICES:
//			return "MATCH (n:Vertex) WHERE n.graph = ? return n.id";
			 */
		default: throw new InputMismatchException("query template not recognized");
		}
	}

	public void fail(Connection conn, Exception e) {
		log.error(e.getMessage(), e);
		rollback(conn);		
		try {
			conn.close();
		} catch (SQLException e1) {
			log.error(e.getMessage(), e);
		}
	}
	
//	public static String batchGenerator(Set<String> rows) {
//		String batchList = "";
//		
//		for(String s : rows)
//			batchList += s + ",\n";
//		
//		return batchList;
//	};
	
	public static String edgeInfoIntoBatch(long from, long to, int graphId, double weight) {
		return "{from: "+from+",to:"+to+",graphId:"+graphId+",weight:"+weight+"}";
	}

}
