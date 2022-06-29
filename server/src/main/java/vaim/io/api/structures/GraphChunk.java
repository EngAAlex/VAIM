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

package vaim.io.api.structures;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import vaim.core.graph.Edge;
import vaim.core.graph.Vertex;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * GraphChunk
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-10-19T09:09:24.285Z[GMT]")


public class GraphChunk   {
	@JsonProperty("simulation")
	private Integer simulationId = null;

	@JsonProperty("vertices")
	@Valid
	private List<Vertex> vertices = null;

	@JsonProperty("bounding_box")
	private BoundingBox boundingBox = null;

	@JsonProperty("edges")
	@Valid
	private List<Edge> edges = null;

	public GraphChunk simulationId(Integer id) {
		this.simulationId = id;
		return this;
	}

	/**
	 * Get id
	 * @return id
	 **/
	@ApiModelProperty(value = "")

	public Integer getSimulationId() {
		return simulationId;
	}

	public void setSimulationId(Integer id) {
		this.simulationId = id;
	}

	public GraphChunk vertices(List<Vertex> vertices) {
		this.vertices = vertices;
		return this;
	}

	public GraphChunk addVertexItem(Vertex verticesItem) {
		if (this.vertices == null) {
			this.vertices = new ArrayList<Vertex>();
		}
		this.vertices.add(verticesItem);
		return this;
	}

	/**
	 * Get vertices
	 * @return vertices
	 **/
	@ApiModelProperty(value = "")
	@Valid
	public List<Vertex> getVertices() {
		return vertices;
	}

	public void setVertices(List<Vertex> vertices) {
		this.vertices = vertices;
	}

	public GraphChunk boundingBox(BoundingBox boundingBox) {
		this.boundingBox = boundingBox;
		return this;
	}

	/**
	 * Get boundingBox
	 * @return boundingBox
	 **/
	@ApiModelProperty(value = "")

	@Valid
	public BoundingBox getBoundingBox() {
		return boundingBox;
	}

	public void setBoundingBox(BoundingBox boundingBox) {
		this.boundingBox = boundingBox;
	}	

	public GraphChunk edges(ArrayList<Edge> edges) {
		this.edges = edges;
		return this;
	}

//	public GraphChunk addEdgesItem(List<Edge> edgesItem) {
//		if (this.edges == null) {
//			this.edges = new ArrayList<Edge>();
//		}
//		this.edges.add(edgesItem);
//		return this;
//	}
	
	public GraphChunk addEdgeItem(Edge edge) {
		if (this.edges == null) {
			this.edges = new ArrayList<Edge>();
		}
		this.edges.add(edge);
		return this;
	}

	//	public GraphChunk addEdgesItem(int frame, Edge e) {
	//		if (this.edges == null) 
	//			this.edges = new ArrayList<List<Edge>>();
	//		
	//		try {
	//			this.edges.get(frame).add(e);
	//		}catch(IndexOutOfBoundsException ife) {
	//			try{
	//				this.edges.add(frame, new LinkedList<Edge>());
	//			}catch(IndexOutOfBoundsException ifes) {
	//				ArrayList<List<Edge>> newList = new ArrayList<List<Edge>>(frame+1);
	//				newList.addAll(this.edges);				
	//				this.edges = newList;				
	//			}
	//			this.edges.get(frame).add(e);			
	//		}
	//				
	//		return this;
	//	}


	/**
	 * Get edges
	 * @return edges
	 **/
	@ApiModelProperty(value = "")
	@Valid
	public List<Edge> getEdges() {
		return edges;
	}

	public void setEdges(List<Edge> edges) {
		this.edges = edges;
	}


	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		GraphChunk graphChunk = (GraphChunk) o;
		return Objects.equals(this.simulationId, graphChunk.simulationId) &&
				Objects.equals(this.vertices, graphChunk.vertices) &&
				Objects.equals(this.edges, graphChunk.edges);
	}

	@Override
	public int hashCode() {
		return Objects.hash(simulationId, vertices, edges);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class GraphChunk {\n");

		sb.append("    id: ").append(toIndentedString(simulationId)).append("\n");
		sb.append("    vertices: ").append(toIndentedString(vertices)).append("\n");
		sb.append("    edges: ").append(toIndentedString(edges)).append("\n");
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
}
