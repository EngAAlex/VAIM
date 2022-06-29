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

import io.swagger.annotations.ApiModelProperty;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * SimulationData
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-02-24T10:26:00.650Z[GMT]")


public class SimulationData   {
  @JsonProperty("id")
  private Integer id = null;

  @JsonProperty("avg_activated")
  private BigDecimal avgActivated = null;

  @JsonProperty("activated_stddev")
  private BigDecimal activatedStddev = null;

  @JsonProperty("number_of_seeds")
  private Long numberOfSeeds = null;

  @JsonProperty("steps")
  private Integer steps = null;

  @JsonProperty("runs")
  private Integer runs = null;

  @JsonProperty("seed_selection")
  private String seedSelection = null;

  @JsonProperty("diffusion_strategy")
  private String diffusionStrategy = null;

  public SimulationData id(Integer id) {
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

  public SimulationData avgActivated(BigDecimal avgActivated) {
    this.avgActivated = avgActivated;
    return this;
  }

  /**
   * Get avgActivated
   * @return avgActivated
   **/
	@ApiModelProperty(value = "")
  
    public BigDecimal getAvgActivated() {
    return avgActivated;
  }

  public void setAvgActivated(BigDecimal avgActivated) {
    this.avgActivated = avgActivated;
  }

  public SimulationData activatedStddev(BigDecimal activatedStddev) {
    this.activatedStddev = activatedStddev;
    return this;
  }

  /**
   * Get activatedStddev
   * @return activatedStddev
   **/
	@ApiModelProperty(value = "")
  
    @Valid
    public BigDecimal getActivatedStddev() {
    return activatedStddev;
  }

  public void setActivatedStddev(BigDecimal activatedStddev) {
    this.activatedStddev = activatedStddev;
  }

  public SimulationData numberOfSeeds(Long numberOfSeeds) {
    this.numberOfSeeds = numberOfSeeds;
    return this;
  }

  /**
   * Get numberOfSeeds
   * @return numberOfSeeds
   **/
	@ApiModelProperty(value = "")
  
    public Long getNumberOfSeeds() {
    return numberOfSeeds;
  }

  public void setNumberOfSeeds(Long numberOfSeeds) {
    this.numberOfSeeds = numberOfSeeds;
  }

  public SimulationData steps(Integer steps) {
    this.steps = steps;
    return this;
  }

  /**
   * Get steps
   * @return steps
   **/
	@ApiModelProperty(value = "")
  
    public Integer getSteps() {
    return steps;
  }

  public void setSteps(Integer steps) {
    this.steps = steps;
  }

  public SimulationData runs(Integer runs) {
    this.runs = runs;
    return this;
  }

  /**
   * Get runs
   * @return runs
   **/
	@ApiModelProperty(value = "")
  
    public Integer getRuns() {
    return runs;
  }

  public void setRuns(Integer runs) {
    this.runs = runs;
  }

  public SimulationData seedSelection(String seedSelection) {
    this.seedSelection = seedSelection;
    return this;
  }

  /**
   * Get seedSelection
   * @return seedSelection
   **/
	@ApiModelProperty(value = "")
  
    public String getSeedSelection() {
    return seedSelection;
  }

  public void setSeedSelection(String seedSelection) {
    this.seedSelection = seedSelection;
  }

  public SimulationData diffusionStrategy(String diffusionStrategy) {
    this.diffusionStrategy = diffusionStrategy;
    return this;
  }

  /**
   * Get diffusionStrategy
   * @return diffusionStrategy
   **/
	@ApiModelProperty(value = "")
  
    public String getDiffusionStrategy() {
    return diffusionStrategy;
  }

  public void setDiffusionStrategy(String diffusionStrategy) {
    this.diffusionStrategy = diffusionStrategy;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SimulationData simulationData = (SimulationData) o;
    return Objects.equals(this.id, simulationData.id) &&
        Objects.equals(this.avgActivated, simulationData.avgActivated) &&
        Objects.equals(this.activatedStddev, simulationData.activatedStddev) &&
        Objects.equals(this.numberOfSeeds, simulationData.numberOfSeeds) &&
        Objects.equals(this.steps, simulationData.steps) &&
        Objects.equals(this.runs, simulationData.runs) &&
        Objects.equals(this.seedSelection, simulationData.seedSelection) &&
        Objects.equals(this.diffusionStrategy, simulationData.diffusionStrategy);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, avgActivated, activatedStddev, numberOfSeeds, steps, runs, seedSelection, diffusionStrategy);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SimulationData {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    avgActivated: ").append(toIndentedString(avgActivated)).append("\n");
    sb.append("    activatedStddev: ").append(toIndentedString(activatedStddev)).append("\n");
    sb.append("    numberOfSeeds: ").append(toIndentedString(numberOfSeeds)).append("\n");
    sb.append("    steps: ").append(toIndentedString(steps)).append("\n");
    sb.append("    runs: ").append(toIndentedString(runs)).append("\n");
    sb.append("    seedSelection: ").append(toIndentedString(seedSelection)).append("\n");
    sb.append("    diffusionStrategy: ").append(toIndentedString(diffusionStrategy)).append("\n");
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
