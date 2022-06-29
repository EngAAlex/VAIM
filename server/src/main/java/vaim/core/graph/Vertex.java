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

import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import vaim.io.api.structures.Frames;
import vaim.io.api.structures.VertexCoords;
import vaim.io.db.DBWizard;
import vaim.io.db.DBWizard.QUERY_TYPE;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Vertex
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-05-06T15:41:53.269Z[GMT]")
public class Vertex   {
	
	//LOCAL PROPERTIES
	private HashMap<String, Object> attributes;

	
	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("coords")
	private VertexCoords coords = null;
	
	@JsonProperty("vertex_sector")
	private VertexCoords vertexSector = null;
//
	@JsonProperty("degree")
	private Long degree = Long.valueOf(-1);
//
//	@JsonProperty("seed_id")
//	private Long seedId = (long) -1;
	
	@JsonProperty("neighbors")
	@Valid
	private List<Edge> neighbors = null;
	
	@JsonProperty("frames")
	@Valid
	private Frames frames = null;

	private Map<String, String> drawingAttributes = null;

	public Vertex id(Long id) {
		this.id = id;
		return this;
	}

	/**
	 * Get id
	 * @return id
	 **/
	@ApiModelProperty(value = "")

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Vertex coords(VertexCoords coords) {
		this.coords = coords;
		return this;
	}

	/**
	 * Get coords
	 * @return coords
	 **/
	@ApiModelProperty(value = "")

	@Valid
	public VertexCoords getCoords() {
		return coords;
	}

	public void setCoords(VertexCoords coords) {
		this.coords = coords;
	}
	
	public Vertex vertexSector(VertexCoords vertexSector) {
		this.vertexSector = vertexSector;
		return this;
	}

	/**
	 * Get vertexSector
	 * @return vertexSector
	 **/
	@ApiModelProperty(value = "")

	@Valid
	public VertexCoords getVertexSector() {
		return vertexSector;
	}

	public void setGlobalCoords(VertexCoords vertexSector) {
		this.vertexSector = vertexSector;
	}
//
	public Vertex degree(long degree) {
		this.degree = degree;
		return this;
	}
	
	/**
	 * Get degree
	 * @return degree
	 **/
	@ApiModelProperty(value = "")
	
	public Long getDegree() {
		return degree;
	}

	public void setActiveAt(long degree) {
		this.degree = degree;
	}
//
//	public Vertex seedId(long seedId) {
//		this.seedId = seedId;
//		return this;
//	}
	
//	/**
//	 * Get coords
//	 * @return coords
//	 **/
//	@ApiModelProperty(value = "")
//	
//	public Long getSeedId() {
//		return seedId;
//	}
//
//	public void setSeedId(Long seedId) {
//		this.seedId = seedId;
//	}
	
	public Vertex neighbors(List<Edge> neighbors) {
		this.neighbors = neighbors;
		return this;
	}

	public Vertex addNeighborsItem(Edge neighborsItem) {
		if (this.neighbors == null) {
			this.neighbors = new ArrayList<Edge>();
		}
		this.neighbors.add(neighborsItem);
		return this;
	}

	
	public boolean checkNeighborsPresence(Long neighnorsItem) {
		return this.neighbors.contains(neighnorsItem);
	}
	
	/**
	 * Get neighbors
	 * @return neighbors
	 **/
	@ApiModelProperty(value = "")

	public List<Edge> getNeighbors() {
		if(neighbors == null)
			return List.of();
		return neighbors;
	}

	public void setNeighbors(List<Edge> neighbors) {
		this.neighbors = neighbors;
	}

	/**
	 * Get frames probability
	 * @return frames
	 **/
	@ApiModelProperty(value = "")

	public Frames getFrames() {
		return frames;
	}

	public void setFrames(Frames frames) {
		this.frames = frames;
	}

	public Vertex frames(Frames frames) {
		this.frames = frames;
		return this;
	}

	/*public Vertex addFramesItem(Double frameValue) {
		if (this.frames == null) {
			this.frames = new ArrayList<Double>();
		}
		this.frames.add(frameValue);
		return this;
	}*/


	public Map<String, String> drawingAttributes() {
		return drawingAttributes;
	}

	public void secureDrawingAttribute(String key, String value) {
		if(drawingAttributes == null)
			drawingAttributes = new HashMap<String, String>();
		drawingAttributes.put(key, value);
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Vertex vertex = (Vertex) o;
		return Objects.equals(this.id, vertex.id) &&
				Objects.equals(this.coords, vertex.coords) &&
				Objects.equals(this.neighbors, vertex.neighbors);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, coords, neighbors);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Vertex {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    coords: ").append(toIndentedString(coords)).append("\n");
		sb.append("    neighbors: ").append(toIndentedString(neighbors)).append("\n");
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
	
	public void seed(long seed) {
		putLocalAttribute("seed", seed);
	}

	public long isSeed() {
		return (long) localAttribute("seed");
	}
	
	public Iterator<Entry<String, Object>> attributesIterator(){
		if(attributes == null)
			return null;
		return attributes.entrySet().iterator();
	}

	public void putLocalAttribute(String key, Object value){
		if(attributes == null)
			attributes = new HashMap<String, Object>();
		attributes.put(key, value);
	}

	public Object localAttribute(String attribute){
		return attributes == null ? null : attributes.get(attribute);
	}

	public void removeLocalAttribute(String key) {
		attributes.remove(key);
	}
	
	public void kill(long step){
		removeLocalAttribute("active");
		putLocalAttribute("dead", step);
	}

	public void clean(int run) {
		removeLocalAttribute("active");
		removeLocalAttribute("seed");
		removeLocalAttribute("Simulation "+ run);

	}

//	public static Vertex fetchVertexFromDatabase(Connection conn, int graph, long id, long step, int simulation, int run) throws Exception{
//		DBWizard dbW = DBWizard.getInstance();
//		Vertex v = new Vertex().id(id);
//		PreparedStatement pms = dbW.createStatement(conn, QUERY_TYPE.GET_SINGLE_VERTEX_NEIGHBORS);
//		pms.setInt(1, graph);
//		pms.setInt(2, graph);
//		pms.setLong(3, id);
//
//		ResultSet rs = pms.executeQuery();
//
//		while(rs.next())  //WE ASSUME THE ID EXISTS
//			//Vertex v = new ObjectMapper().readValue(rs.getString(1), Vertex.class);			
//			v.addNeighborsItem(new Edge().source(id).target(rs.getLong(1)).weight(rs.getDouble(2)));
//		
//
//		pms.close();
//
//		pms = dbW.createStatement(conn, QUERY_TYPE.GET_VERTEX_ACTIVATION_STATUS);
//		pms.setInt(1, graph);			
//		pms.setLong(2, id);		
//		pms.setInt(3, simulation);
//		pms.setInt(4, graph);		
//		pms.setInt(5, graph);			
//		pms.setInt(6, simulation);
//		pms.setInt(7, run);
//
//		rs = pms.executeQuery();
//
//		if(rs.next()) {
//			long seed = rs.getLong(2);
//			if(rs.wasNull()) {
//				seed = rs.getLong(4);
//				if(!rs.wasNull())
//					v.seed(seed);
//			}else
//				v.seed(seed);
//		}
//				
//
//		pms.close();
//
//		/*else
//			throw new Exception("Vertex id not found");}*/
//		return v;	
//	}
	
	public void storeStateChange(Connection conn, int graph, int simulationId, long origin, byte status, long step, int run) throws Exception{
		DBWizard dbW = DBWizard.getInstance();
		PreparedStatement pms = dbW.createStatement(conn, QUERY_TYPE.INSERT_TEMP_ACTIVATION_RELATIONSHIP);
		pms.setLong(1, origin);
		pms.setInt(2, graph);
		pms.setInt(3, graph);
		pms.setLong(4, id);
		pms.setInt(5, simulationId);
		pms.setInt(6, run);
		pms.setLong(7, step);
		pms.setLong(8, isSeed());

		pms.executeUpdate();

		pms.close();

		//db.commit();

		/*if(getAttribute("Simulation " + run) == null)
			setAttribute("Simulation " + run, new HashSet<StateChange>());
		HashSet<StateChange> set = (HashSet<StateChange>) getAttribute("Simulation " + run);
		set.add(new StateChange(origin, step, status));*/
	}

}
