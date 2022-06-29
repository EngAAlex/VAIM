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

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import vaim.io.api.structures.Frames;

/**
 * Edge
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-10-09T15:18:20.913Z[GMT]")


public class Edge   {
	@JsonProperty("id")
	protected Long id = null;

	@JsonProperty("source")
	protected Long source = null;

	@JsonProperty("target")
	protected Long target = null;

	@JsonProperty("weight")
	protected Double weight = null;  	

	@JsonProperty("active_at")
	protected Integer activeAt = null;

	@JsonProperty("seed_id")
	protected Long seedId = null;

	@JsonProperty("frames")
	private Frames frames;  

	public Edge id(Long id) {
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

	public Edge source(Long source) {
		this.source = source;
		return this;
	}

	/**
	 * Get source
	 * @return source
	 **/
	@ApiModelProperty(value = "")

	public Long getSource() {
		return source;
	}

	public void setSource(Long source) {
		this.source = source;
	}

	/**
	 * Get seedId
	 * @return seedId
	 **/
	@ApiModelProperty(value = "")
	
	public Edge seedId(Long seedId) {
		this.seedId = seedId;
		return this;
	}
	
	public Long getSeedId() {
		return seedId;
	}

	public void setSeedId(Long seedId) {
		this.seedId = seedId;
	}
	
	/**
	 * Get activeAt
	 * @return activeAt
	 **/
	@ApiModelProperty(value = "")
	
	public Edge activeAt(Integer activeAt) {
		this.activeAt = activeAt;
		return this;
	}
		

	public Integer getActiveAt() {
		return activeAt;
	}

	public void setActiveAt(Integer activeAt) {
		this.activeAt = activeAt;
	}

	public Edge target(Long target) {
		this.target = target;
		return this;
	}

	/**
	 * Get target
	 * @return target
	 **/
	@ApiModelProperty(value = "")

	public Long getTarget() {
		return target;
	}

	public void setTarget(Long target) {
		this.target = target;
	}

	/**
	 * Get weight
	 * @return weight
	 **/
	@ApiModelProperty(value = "")

	public Edge weight(Double weight) {
		this.weight = weight;
		return this;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
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

	public Edge frames(Frames frames) {
		this.frames = frames;
		return this;
	}


	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Edge edge = (Edge) o;
		return Objects.equals(this.id, edge.id) &&
				Objects.equals(this.source, edge.source) &&
				Objects.equals(this.target, edge.target) &&
				Objects.equals(this.weight, edge.weight) &&
				Objects.equals(this.activeAt, edge.activeAt) &&
				Objects.equals(this.seedId, edge.seedId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, source, target, weight, activeAt, seedId);
	}
	
	/**
	 * @author Alessio Arleo
	 *
	 * These edges still contain but ignore the weight attribute in equals operations
	 *
	 */
	public static class UnWeightedEdge extends Edge{
		
		@Override
		public UnWeightedEdge id(Long id) {
			super.id(id);
			return this;
		}
		
		@Override
		public UnWeightedEdge source(Long source) {
			super.source(source);
			return this;
		}
		
		@Override
		public UnWeightedEdge target(Long target) {
			super.target(target);
			return this;
		}
		
		@Override
		public UnWeightedEdge activeAt(Integer activeAt) {
			super.activeAt(activeAt);
			return this;
		}
		
		@Override
		public UnWeightedEdge seedId(Long seedId) {
			super.seedId(seedId);
			return this;
		}
		
		@Override
		public UnWeightedEdge weight(Double weight) {
			super.weight(weight);
			return this;
		}
		
		public Edge convertToEdge() {
			return this;
		}
				
		
		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}
			Edge edge = (Edge) o;
			return Objects.equals(this.id, edge.id) &&
					Objects.equals(this.source, edge.source) &&
					Objects.equals(this.target, edge.target) &&
					Objects.equals(this.activeAt, edge.activeAt) &&
					Objects.equals(this.seedId, edge.seedId);
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(id, source, target, activeAt, seedId);
		}
		
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Edge {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    source: ").append(toIndentedString(source)).append("\n");
		sb.append("    target: ").append(toIndentedString(target)).append("\n");
		sb.append("    activeAt: ").append(toIndentedString(activeAt)).append("\n");
		sb.append("    seedId: ").append(toIndentedString(seedId)).append("\n");
		sb.append("    weight: ").append(toIndentedString(weight)).append("\n");
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
