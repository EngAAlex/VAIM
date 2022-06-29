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

import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.Valid;
import org.springframework.validation.annotation.Validated;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

/**
 * SimulationRequest
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-03-02T12:27:01.103Z[GMT]")


public class SimulationRequest   {
  @JsonProperty("model")
  private String model = null;

  @JsonProperty("selectionStrategy")
  private String selectionStrategy = null;

  @JsonProperty("randomSeeds")
  private BigDecimal randomSeeds = null;

  @JsonProperty("donorSimulation")
  private BigDecimal donorSimulation = null;

  @JsonProperty("seedsToAdd")
  private SeedArray seedsToAdd = null;

  @JsonProperty("seedsToRemove")
  private SeedArray seedsToRemove = null;

  public SimulationRequest model(String model) {
    this.model = model;
    return this;
  }

  /**
   * Get model
   * @return model
   **/
  @ApiModelProperty(value = "")
  
    public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public SimulationRequest selectionStrategy(String selectionStrategy) {
    this.selectionStrategy = selectionStrategy;
    return this;
  }

  /**
   * Get selectionStrategy
   * @return selectionStrategy
   **/
  @ApiModelProperty(value = "")
  
    public String getSelectionStrategy() {
    return selectionStrategy;
  }

  public void setSelectionStrategy(String selectionStrategy) {
    this.selectionStrategy = selectionStrategy;
  }

  public SimulationRequest randomSeeds(BigDecimal randomSeeds) {
    this.randomSeeds = randomSeeds;
    return this;
  }

  /**
   * Get randomSeeds
   * @return randomSeeds
   **/
  @ApiModelProperty(value = "")
  
    @Valid
    public BigDecimal getRandomSeeds() {
    return randomSeeds;
  }

  public void setRandomSeeds(BigDecimal randomSeeds) {
    this.randomSeeds = randomSeeds;
  }

  public SimulationRequest donorSimulation(BigDecimal donorSimulation) {
    this.donorSimulation = donorSimulation;
    return this;
  }

  /**
   * Get donorSimulation
   * @return donorSimulation
   **/
  @ApiModelProperty(value = "")
  
    @Valid
    public BigDecimal getDonorSimulation() {
    return donorSimulation;
  }

  public void setDonorSimulation(BigDecimal donorSimulation) {
    this.donorSimulation = donorSimulation;
  }

  public SimulationRequest seedsToAdd(SeedArray seedsToAdd) {
    this.seedsToAdd = seedsToAdd;
    return this;
  }

  /**
   * Get seedsToAdd
   * @return seedsToAdd
   **/
  @ApiModelProperty(value = "")
  
    @Valid
    public SeedArray getSeedsToAdd() {
    return seedsToAdd;
  }

  public void setSeedsToAdd(SeedArray seedsToAdd) {
    this.seedsToAdd = seedsToAdd;
  }

  public SimulationRequest seedsToRemove(SeedArray seedsToRemove) {
    this.seedsToRemove = seedsToRemove;
    return this;
  }

  /**
   * Get seedsToRemove
   * @return seedsToRemove
   **/
  @ApiModelProperty(value = "")
  
    @Valid
    public SeedArray getSeedsToRemove() {
    return seedsToRemove;
  }

  public void setSeedsToRemove(SeedArray seedsToRemove) {
    this.seedsToRemove = seedsToRemove;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SimulationRequest simulationRequest = (SimulationRequest) o;
    return Objects.equals(this.model, simulationRequest.model) &&
        Objects.equals(this.selectionStrategy, simulationRequest.selectionStrategy) &&
        Objects.equals(this.randomSeeds, simulationRequest.randomSeeds) &&
        Objects.equals(this.donorSimulation, simulationRequest.donorSimulation) &&
        Objects.equals(this.seedsToAdd, simulationRequest.seedsToAdd) &&
        Objects.equals(this.seedsToRemove, simulationRequest.seedsToRemove);
  }

  @Override
  public int hashCode() {
    return Objects.hash(model, selectionStrategy, randomSeeds, donorSimulation, seedsToAdd, seedsToRemove);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SimulationRequest {\n");
    
    sb.append("    model: ").append(toIndentedString(model)).append("\n");
    sb.append("    selectionStrategy: ").append(toIndentedString(selectionStrategy)).append("\n");
    sb.append("    randomSeeds: ").append(toIndentedString(randomSeeds)).append("\n");
    sb.append("    donorSimulation: ").append(toIndentedString(donorSimulation)).append("\n");
    sb.append("    seedsToAdd: ").append(toIndentedString(seedsToAdd)).append("\n");
    sb.append("    seedsToRemove: ").append(toIndentedString(seedsToRemove)).append("\n");
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
