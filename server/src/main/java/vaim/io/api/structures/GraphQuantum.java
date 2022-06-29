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
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * GraphQuantum
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-10-09T15:18:20.913Z[GMT]")


public class GraphQuantum   {
	@JsonProperty("x")
	private int x;

	@JsonProperty("y")
	private int y;
	
	@JsonProperty("verticesAggregator")
	private Long verticesAggregator = Long.valueOf(0);

	@JsonProperty("edgesAggregator")
	private Double edgesAggregator = Double.valueOf(0);;

	public GraphQuantum(int row, int col) {
		this.x = row;
		this.y = col;
	}

	/**
	 * Get sectorId
	 * @return sectorId
	 **/
	@ApiModelProperty(value = "")

	public String getSectorId() {
		return x + "-" + y;
	}

	//  public void setSectorId(String sectorId) {
	//    this.sectorId = sectorId;
	//  }

	//  public GraphQuantum verticesAggregator(Integer vertices) {
	//    this.verticesAggregator = vertices;
	//    return this;
	//  }

	public void addElementToVerticesAggregate() {
		addElementsToVerticesAggregate(1);
	}

	public void addElementsToVerticesAggregate(long elements) {
		this.verticesAggregator += elements;			
	}

	public void addElementToEdgesAggregate() {
		addElementsToVerticesAggregate(1);
	}

	public void addElementsToEdgesAggregate(double elements) {
		this.edgesAggregator += elements;			
	}

	/**
	 * Get vertices aggregate value
	 * @return vertices aggregate
	 **/
	@ApiModelProperty(value = "")

	public Long getVerticesAggregator() {
		return verticesAggregator;
	}

	/**
	 * Get vertices
	 * @return vertices
	 **/
	@ApiModelProperty(value = "")

	public Double getEdgesAggregator() {
		return edgesAggregator;
	}  

	//  public void setVertices(Integer vertices) {
	//    this.verticesAggregator = vertices;
	//  }


	/**
	 * Get x sector id
	 * @return x sector Id
	 **/
	@ApiModelProperty(value = "")
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Get y sector id
	 * @return y sector Id
	 **/
	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		GraphQuantum graphQuantum = (GraphQuantum) o;
		return Objects.equals(this.x, graphQuantum.x) &&
				Objects.equals(this.y, graphQuantum.y) &&
				Objects.equals(this.verticesAggregator, graphQuantum.verticesAggregator) &&
				Objects.equals(this.edgesAggregator, graphQuantum.edgesAggregator);
	}

	@Override
	public int hashCode() {
		return Objects.hash(getSectorId(), verticesAggregator);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class GraphQuantum {\n");

		sb.append("    sectorId: ").append(toIndentedString(getSectorId())).append("\n");
		sb.append("    vertices: ").append(toIndentedString(verticesAggregator)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		GraphQuantum gq = new GraphQuantum(x, y);
		gq.addElementsToVerticesAggregate(verticesAggregator);
		gq.addElementsToEdgesAggregate(edgesAggregator);
		return gq;
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

	public void clearEdgeAggregator() {
		//verticesAggregator = (long) 0;
		edgesAggregator = 0.0d;
		// TODO Auto-generated method stub
		
	}
}
