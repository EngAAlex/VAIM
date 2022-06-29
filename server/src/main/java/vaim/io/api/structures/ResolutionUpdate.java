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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * ResolutionUpdate
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-02-25T09:31:29.265Z[GMT]")


public class ResolutionUpdate   {
	@JsonProperty("sector_quantization")
	@Valid
	private List<GraphQuantum> sectorQuantization = null;
	
	@JsonProperty("simulation_matrices")
	@Valid
	private List<SimulationChunk> simulationMatrices = null;
	
	@JsonProperty("graph_sector")
	@Valid
	private GraphSector graphSector = null;	

	public ResolutionUpdate sectorQuantization(List<GraphQuantum> sectorQuantization) {
		this.sectorQuantization = sectorQuantization;
		return this;
	}

	public ResolutionUpdate addSectorQuantizationItem(GraphQuantum sectorQuantizationItem) {
		if (this.sectorQuantization == null) {
			this.sectorQuantization = new ArrayList<GraphQuantum>();
		}
		this.sectorQuantization.add(sectorQuantizationItem);
		return this;
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
	
	public ResolutionUpdate graphSector(GraphSector graphSector) {
		this.graphSector = graphSector;
		return this;
	}
	
	/**
	 * Get graphSector
	 * @return graphSector
	 **/
	@ApiModelProperty(value = "")
	@Valid
	public GraphSector getGraphSector() {
		return graphSector;
	}

	public void setGraphSector(GraphSector graphSector) {
		this.graphSector = graphSector;
	}

	public ResolutionUpdate simulationMatrices(List<SimulationChunk> simulationMatrices) {
		this.simulationMatrices = simulationMatrices;
		return this;
	}

	public ResolutionUpdate addSimulationMatricesItem(SimulationChunk simulationMatricesItem) {
		if (this.simulationMatrices == null) {
			this.simulationMatrices = new ArrayList<SimulationChunk>();
		}
		this.simulationMatrices.add(simulationMatricesItem);
		return this;
	}

	/**
	 * Get simulationMatrices
	 * @return simulationMatrices
	 **/
	@ApiModelProperty(value = "")
	@Valid
	public List<SimulationChunk> getSimulationMatrices() {
		return simulationMatrices;
	}

	public void setSimulationMatrices(List<SimulationChunk> simulationMatrices) {
		this.simulationMatrices = simulationMatrices;
	}


	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ResolutionUpdate resolutionUpdate = (ResolutionUpdate) o;
		return Objects.equals(this.sectorQuantization, resolutionUpdate.sectorQuantization) &&
				Objects.equals(this.simulationMatrices, resolutionUpdate.simulationMatrices);
	}

	@Override
	public int hashCode() {
		return Objects.hash(sectorQuantization, simulationMatrices);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ResolutionUpdate {\n");

		sb.append("    sectorQuantization: ").append(toIndentedString(sectorQuantization)).append("\n");
		sb.append("    simulationMatrices: ").append(toIndentedString(simulationMatrices)).append("\n");
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
