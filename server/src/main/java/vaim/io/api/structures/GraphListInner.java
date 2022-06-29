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
import vaim.core.graph.Graph;
import vaim.core.graph.Vertex;
import vaim.io.api.structures.SimulationData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * GraphListInner
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-03-26T11:32:33.543Z[GMT]")
public class GraphListInner   {
	
	@JsonProperty("id")
	private Integer id = null;

	@JsonProperty("name")
	private String name = null;

	@JsonProperty("vertexCount")
	private Integer vertexCount = null;

	@JsonProperty("edges")
	private Integer edges = null;
	
	@JsonProperty("simulations")
	private Integer simulations = null;
	
	public GraphListInner id(Integer id) {
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

	public GraphListInner name(String name) {
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

	public GraphListInner vertexCount(Integer vertexCount) {
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
	

	public GraphListInner edges(Integer edges) {
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

	public GraphListInner simulations(Integer simulations) {
		this.simulations = simulations;
		return this;
	}

	/**
	 * Get simulations
	 * @return simulations
	 **/
	@ApiModelProperty(value = "")
	@Valid
	public Integer getSimulations() {
		return simulations;
	}

	public void setSimulations(Integer simulations) {
		this.simulations = simulations;
	}


	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		GraphListInner graphListInner = (GraphListInner) o;
		return Objects.equals(this.id, graphListInner.id) &&
				Objects.equals(this.name, graphListInner.name) &&
				Objects.equals(this.vertexCount, graphListInner.vertexCount) &&
				Objects.equals(this.edges, graphListInner.edges) &&
				Objects.equals(this.simulations, graphListInner.simulations);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, vertexCount, edges, simulations);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class GraphListInner {\n");
		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    name: ").append(toIndentedString(id)).append("\n");
		sb.append("    vertexCount: ").append(toIndentedString(vertexCount)).append("\n");
		sb.append("    edges: ").append(toIndentedString(edges)).append("\n");		
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
}
