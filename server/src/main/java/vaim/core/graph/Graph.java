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

package vaim.core.graph;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import vaim.io.api.structures.GraphBounds;
import vaim.io.api.structures.GraphQuantum;
import vaim.io.api.structures.SimulationData;
import vaim.io.api.structures.VertexCoords;
import vaim.io.db.DBWizard;
import vaim.io.db.DBWizard.QUERY_TYPE;
import vaim.layout.MatrixQuantizer;

/**
 * Graph
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-03-22T11:13:09.373Z[GMT]")
public class Graph   {
	
	@JsonProperty("id")
	private Integer id = null;

	@JsonProperty("name")
	private String name = null;

	@JsonProperty("vertexCount")
	private Integer vertexCount = null;

	private Map<Long, Vertex> vertices = null;

	@JsonProperty("edges")
	private Integer edges = null;

	@JsonProperty("bounds")
	private GraphBounds bounds = null;

	@JsonProperty("sectorQuantization")
	@Valid
	private List<GraphQuantum> sectorQuantization = null;

	@JsonProperty("simulations")
	@Valid
	private List<SimulationData> simulations = null;

	private Set<Edge> edgesSet = null;
	
	private MatrixQuantizer mq;

	public Set<Edge> getEdgesSet() {
		return edgesSet;
	}

	public Graph edgesSet(Set<Edge> edgesSet) {
		this.edgesSet = edgesSet;
		return this;
	}

	public Graph id(Integer id) {
		this.id = id;
		return this;
	}

	/**
	 * Get id
	 * @return id
	 **/
	@ApiModelProperty(value = "")

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Graph name(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Get name
	 * @return name
	 **/
	@ApiModelProperty(value = "")

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Graph vertexCount(Integer vertexCount) {
		this.vertexCount = vertexCount;
		return this;
	}

	/**
	 * Get vertexCount
	 * @return vertexCount
	 **/
	@ApiModelProperty(value = "")

	public Integer getVertexCount() {
		return vertexCount;
	}

	public void setVertexCount(Integer vertexCount) {
		this.vertexCount = vertexCount;
	}

	public Graph vertices(Map<Long, Vertex> vertices) {
		this.vertices = vertices;
		return this;
	}


	public Graph putVerticesItem(Long key, Vertex verticesItem) {
		if (this.vertices == null) {
			this.vertices = new HashMap<Long, Vertex>();
		}
		this.vertices.put(key, verticesItem);
		return this;
	}


	public Vertex getVertex(Long key) {
		if(this.vertices != null)
			return vertices.get(key);
		return null;
	}
	
	public Map<Long, Vertex> getVertices() {
		return vertices;
	}

	public void setVertices(Map<Long, Vertex> vertices) {
		this.vertices = vertices;
	}

	public Graph edges(Integer edges) {
		this.edges = edges;
		return this;
	}

	/**
	 * Get edges
	 * @return edges
	 **/
	@ApiModelProperty(value = "")

	public Integer getEdges() {
		return edges;
	}

	public void setEdges(Integer edges) {
		this.edges = edges;
	}

	public Graph bounds(GraphBounds bounds) {
		this.bounds = bounds;
		return this;
	}
	
	public void addEdge(Edge e) {
		if(edgesSet == null)
			edgesSet = new HashSet<Edge>();
		edgesSet.add(e);
	}

	public void addVertex(Vertex v) {
		if(vertices == null)
			vertices = new HashMap<Long, Vertex>();
		vertices.put(v.getId(), v);
	}
	
	/**
	 * Get bounds
	 * @return bounds
	 **/
	@ApiModelProperty(value = "")

	@Valid
	public GraphBounds getBounds() {
		return bounds;
	}

	public void setBounds(GraphBounds bounds) {
		this.bounds = bounds;
	}

	public Graph simulations(List<SimulationData> simulations) {
		this.simulations = simulations;
		return this;
	}

	public Graph addSimulationsItem(SimulationData simulationsItem) {
		if (this.simulations == null) {
			this.simulations = new ArrayList<SimulationData>();
		}
		this.simulations.add(simulationsItem);
		return this;
	}

	/**
	 * Get simulations
	 * @return simulations
	 **/
	@ApiModelProperty(value = "")
	@Valid
	public List<SimulationData> getSimulations() {
		return simulations;
	}

	public void setSimulations(List<SimulationData> simulations) {
		this.simulations = simulations;
	}

	public Graph sectorQuantization(List<GraphQuantum> sectorQuantization) {
		this.sectorQuantization = sectorQuantization;
		return this;
	}

	public Graph addSectorQuantizationItem(GraphQuantum sectorQuantizationItem) {
		if (this.sectorQuantization == null) {
			this.sectorQuantization = new ArrayList<GraphQuantum>();
		}
		this.sectorQuantization.add(sectorQuantizationItem);
		return this;
	}
	
	private void addAllToSectorQuantization(List<GraphQuantum> matrixState) {
		if (this.sectorQuantization == null) {
			this.sectorQuantization = new ArrayList<GraphQuantum>();
		}
		this.sectorQuantization.addAll(matrixState);
	}

	/**
	 * Get sectorQuantization
	 * @return sectorQuantization
	 **/
	@ApiModelProperty(value = "")
	@Valid
	public List<GraphQuantum> getSectorQuantization() {
		return sectorQuantization;
	}

	public void setSectorQuantization(List<GraphQuantum> sectorQuantization) {
		this.sectorQuantization = sectorQuantization;
	}


	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Graph graph = (Graph) o;
		return Objects.equals(this.id, graph.id) &&
				Objects.equals(this.name, graph.name) &&
				Objects.equals(this.vertexCount, graph.vertexCount) &&
				Objects.equals(this.vertices, graph.vertices) &&
				Objects.equals(this.edges, graph.edges) &&
				Objects.equals(this.bounds, graph.bounds) &&
				Objects.equals(this.simulations, graph.simulations);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, vertexCount, vertices, edges, bounds, simulations);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Graph {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    name: ").append(toIndentedString(name)).append("\n");
		sb.append("    vertexCount: ").append(toIndentedString(vertexCount)).append("\n");
		sb.append("    vertices: ").append(toIndentedString(vertices)).append("\n");
		sb.append("    edges: ").append(toIndentedString(edges)).append("\n");
		sb.append("    bounds: ").append(toIndentedString(bounds)).append("\n");
		sb.append("    simulations: ").append(toIndentedString(simulations)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}

//	public void populateWithSimulationEdges(Connection conn, int resolution, String simulationId) {
//		DBWizard dbW = DBWizard.getInstance();
//		
//	}
	
	public void buildSectorQuantization(Connection conn, int resolution) throws Exception {
		DBWizard dbW = DBWizard.getInstance();
		PreparedStatement pms = dbW.createStatement(conn, QUERY_TYPE.GET_DENSITY_MATRIX);
		
		pms.setInt(1, id);
		pms.setInt(2, resolution);

		ResultSet rs = pms.executeQuery();			
		
		HashMap<Integer, Map<Integer, GraphQuantum>> qmap = new HashMap<Integer, Map<Integer, GraphQuantum>>();
		
		while(rs.next()) {
			int currX = rs.getInt(1);
			int currY = rs.getInt(2);
			long value = rs.getLong(3);			
			
			if(!qmap.containsKey(currX))
				qmap.put(currX, new HashMap<Integer, GraphQuantum>());
			GraphQuantum toAdd = new GraphQuantum(currX, currY);
			toAdd.addElementsToVerticesAggregate(value);
			qmap.get(currX).put(currY, toAdd);
		}
		
		rs.close();
		pms.close();
		
		for(int i = 0; i<resolution; i++)
			for(int t = 0; t< resolution; t++) {
				if(!qmap.containsKey(i))
					qmap.put(i, new HashMap<Integer, GraphQuantum>());
				Map<Integer, GraphQuantum> tt = qmap.get(i); 
				if(tt.containsKey(t))
					addSectorQuantizationItem(tt.get(t));
				else
					addSectorQuantizationItem(new GraphQuantum(i, t));
			}
//		GraphQuantum toAdd = new GraphQuantum(currX, currY);
//		toAdd.addElementsToVerticesAggregate(value);
//		addSectorQuantizationItem(toAdd);

	}
	
	public void rebuildInfoFromDatabase(Connection conn) throws SQLException {
		DBWizard dbW = DBWizard.getInstance();
		PreparedStatement pms = dbW.createStatement(conn, QUERY_TYPE.GET_GRAPH_INFO);	
		pms.setLong(1, id);
		
		ResultSet rs = pms.executeQuery();
		rs.next();
		name(rs.getString(1));
		vertexCount(rs.getInt(2));
		edges(rs.getInt(3));
		bounds(new GraphBounds().maxX(new BigDecimal(rs.getDouble(7)))
								.minX(new BigDecimal(rs.getDouble(6)))
								.maxY(new BigDecimal(rs.getDouble(9)))
								.minY(new BigDecimal(rs.getDouble(8)))
								.width(new BigDecimal(rs.getDouble(4)))
								.height(new BigDecimal(rs.getDouble(5)))
								);
		
		rs.close();
		pms.close();
	}
	
	/*
	 * 		GraphQuantum toAdd = new GraphQuantum(currX, currY);
		toAdd.addElementsToVerticesAggregate(value);
		addSectorQuantizationItem(toAdd);

	 * */
	
	/*public void buildFromDatabase(Connection conn, int resolution) throws Exception {
		DBWizard dbW = DBWizard.getInstance();
		PreparedStatement pms = dbW.createStatement(conn, QUERY_TYPE.GET_GRAPH_INFO);
		
		pms.setLong(1, id);
		
		ResultSet rs = pms.executeQuery();
		rs.next();
		
		GraphBounds bounds = new GraphBounds()
				.width(new BigDecimal(rs.getDouble(4)))
				.height(new BigDecimal(rs.getDouble(5)))
				.minX(BigDecimal.ZERO)//new BigDecimal(rs.getDouble(6)))
				.maxX(new BigDecimal(rs.getDouble(7) - rs.getDouble(6)))
				.minY(BigDecimal.ZERO)//new BigDecimal(rs.getDouble(8)))
				.maxY(new BigDecimal(rs.getDouble(9) - rs.getDouble(8)));				
		
		setName(rs.getString(1));
		setVertexCount(rs.getInt(2));
		setEdges(rs.getInt(3));
		setBounds(
				bounds
				);

		BigDecimal minY = new BigDecimal(rs.getDouble(8));
		BigDecimal minX = new BigDecimal(rs.getDouble(6));

		pms.close();
		
//		mq = new MatrixQuantizer(resolution, this.bounds);

		pms = dbW.createStatement(conn,QUERY_TYPE.GET_VERTICES_COMPLETE);
		pms.setLong(1, id);
		rs = pms.executeQuery();

		//GraphQuantum[][] matrix = new GraphQuantum[DEFAULT_RESOLUTION][DEFAULT_RESOLUTION];
		MatrixQuantizer mq = new MatrixQuantizer(MatrixQuantizer.DEFAULT_RESOLUTION, bounds);
		
		while(rs.next()) {
			Vertex v = new Vertex().id(rs.getLong(3)).coords(
					new VertexCoords().x(new BigDecimal(rs.getDouble(1))).y(new BigDecimal(rs.getDouble(2))));
//					new VertexCoords().x(new BigDecimal(rs.getDouble(1)).subtract(minX)).y(new BigDecimal(rs.getDouble(2)).subtract(minY)));
			putVerticesItem(Long.valueOf(rs.getInt(3)), v);
			mq.processNewVertex(v);
		}				
		
		rs.close();
		pms.close();
		
		setMatrixQuantization(mq);
		
		addAllToSectorQuantization(mq.copyMatrixStateAsList());
	}*/

	public MatrixQuantizer getMatrixQuantization() {
		return mq;
	}

	public void setMatrixQuantization(MatrixQuantizer mq) {
		this.mq = mq;
	}

//	public void buildFromDatabase(Connection conn) throws Exception {
//		this.buildFromDatabase(conn, MatrixQuantizer.DEFAULT_RESOLUTION);
//	}

	public void clearEdges() {
		this.edgesSet.clear();
	}
	
	public static int getGraphVertexSize(Connection conn, int graph) throws Exception{
		DBWizard dbW = DBWizard.getInstance();
		int size = -1;
		conn = dbW.connect();
		PreparedStatement pms = dbW.createStatement(conn, QUERY_TYPE.GET_GRAPH_SIZE);
		pms.setInt(1, graph);
		ResultSet rs = pms.executeQuery();
		rs.next();
		size = rs.getInt(1);

		pms.close();

		return size;
	}

}
