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
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * SimulationFrame
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-01-15T09:38:53.935Z[GMT]")


public class SimulationFrame   {
  @JsonProperty("min_activated")
  private Integer minActivated = null;

  @JsonProperty("max_activated")
  private Integer maxActivated = null;

  @JsonProperty("low_quartile")
  private BigDecimal lowQuartile = null;

  @JsonProperty("high_quartile")
  private BigDecimal highQuartile = null;

  @JsonProperty("median")
  private BigDecimal median = null;

  @JsonProperty("activated")
  private Integer activated = null;

  @JsonProperty("messages_sent")
  private Integer messagesSent = null;

//  @JsonProperty("activated_ids")
//  @Valid
//  private List<Activations> activatedIds = null;

  @JsonProperty("matrix")
  @Valid
  private List<GraphQuantum> matrix = null;

  public SimulationFrame minActivated(Integer minActivated) {
    this.minActivated = minActivated;
    return this;
  }

  /**
   * Get minActivated
   * @return minActivated
   **/
  @ApiModelProperty(value = "")
  
    public Integer getMinActivated() {
    return minActivated;
  }

  public void setMinActivated(Integer minActivated) {
    this.minActivated = minActivated;
  }

  public SimulationFrame maxActivated(Integer maxActivated) {
    this.maxActivated = maxActivated;
    return this;
  }

  /**
   * Get maxActivated
   * @return maxActivated
   **/
  @ApiModelProperty(value = "")
  
    public Integer getMaxActivated() {
    return maxActivated;
  }

  public void setMaxActivated(Integer maxActivated) {
    this.maxActivated = maxActivated;
  }

  public SimulationFrame lowQuartile(BigDecimal lowQuartile) {
    this.lowQuartile = lowQuartile;
    return this;
  }

  /**
   * Get lowQuartile
   * @return lowQuartile
   **/
  @ApiModelProperty(value = "")
  
    @Valid
    public BigDecimal getLowQuartile() {
    return lowQuartile;
  }

  public void setLowQuartile(BigDecimal lowQuartile) {
    this.lowQuartile = lowQuartile;
  }

  public SimulationFrame highQuartile(BigDecimal highQuartile) {
    this.highQuartile = highQuartile;
    return this;
  }

  /**
   * Get highQuartile
   * @return highQuartile
   **/
  @ApiModelProperty(value = "")
  
    @Valid
    public BigDecimal getHighQuartile() {
    return highQuartile;
  }

  public void setHighQuartile(BigDecimal highQuartile) {
    this.highQuartile = highQuartile;
  }

  public SimulationFrame median(BigDecimal median) {
    this.median = median;
    return this;
  }

  /**
   * Get median
   * @return median
   **/
  @ApiModelProperty(value = "")
  
    @Valid
    public BigDecimal getMedian() {
    return median;
  }

  public void setMedian(BigDecimal median) {
    this.median = median;
  }

  public SimulationFrame activated(Integer activated) {
    this.activated = activated;
    return this;
  }

  /**
   * Get activated
   * @return activated
   **/
  @ApiModelProperty(value = "")
  
    public Integer getActivated() {
    return activated;
  }

  public void setActivated(Integer activated) {
    this.activated = activated;
  }

  public SimulationFrame messagesSent(Integer messagesSent) {
    this.messagesSent = messagesSent;
    return this;
  }

  /**
   * Get messagesSent
   * @return messagesSent
   **/
  @ApiModelProperty(value = "")
  
    public Integer getMessagesSent() {
    return messagesSent;
  }

  public void setMessagesSent(Integer messagesSent) {
    this.messagesSent = messagesSent;
  }

//  public SimulationFrame activatedIds(List<Activations> activatedIds) {
//    this.activatedIds = activatedIds;
//    return this;
//  }
//
//  public SimulationFrame addActivatedIdsItem(Activations activatedIdsItem) {
//    if (this.activatedIds == null) {
//      this.activatedIds = new ArrayList<Activations>();
//    }
//    this.activatedIds.add(activatedIdsItem);
//    return this;
//  }
//
//  /**
//   * Get activatedIds
//   * @return activatedIds
//   **/
//  @ApiModelProperty(value = "")
//      @Valid
//    public List<Activations> getActivatedIds() {
//    return activatedIds;
//  }
//
//  public void setActivatedIds(List<Activations> activatedIds) {
//    this.activatedIds = activatedIds;
//  }

  public SimulationFrame matrix(List<GraphQuantum> matrix) {
    this.matrix = matrix;
    return this;
  }

  public SimulationFrame addMatrixItems(List<GraphQuantum> matrixItems) {
    if (this.matrix == null) {
        this.matrix = new ArrayList<GraphQuantum>();
      }
    this.matrix.addAll(matrixItems);
    return this;
  }
  
  public SimulationFrame addMatrixItem(GraphQuantum matrixItem) {
    if (this.matrix == null) {
      this.matrix = new ArrayList<GraphQuantum>();
    }
    this.matrix.add(matrixItem);
    return this;
  }

  /**
   * Get matrix
   * @return matrix
   **/
  @ApiModelProperty(value = "")
      @Valid
    public List<GraphQuantum> getMatrix() {
    return matrix;
  }

  public void setMatrix(List<GraphQuantum> matrix) {
    this.matrix = matrix;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SimulationFrame simulationFrame = (SimulationFrame) o;
    return Objects.equals(this.minActivated, simulationFrame.minActivated) &&
        Objects.equals(this.maxActivated, simulationFrame.maxActivated) &&
        Objects.equals(this.lowQuartile, simulationFrame.lowQuartile) &&
        Objects.equals(this.highQuartile, simulationFrame.highQuartile) &&
        Objects.equals(this.median, simulationFrame.median) &&
        Objects.equals(this.activated, simulationFrame.activated) &&
        Objects.equals(this.messagesSent, simulationFrame.messagesSent) &&
//        Objects.equals(this.activatedIds, simulationFrame.activatedIds) &&
        Objects.equals(this.matrix, simulationFrame.matrix);
  }

  @Override
  public int hashCode() {
    return Objects.hash(minActivated, maxActivated, lowQuartile, highQuartile, median, activated, messagesSent, /*activatedIds,*/ matrix);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SimulationFrame {\n");
    
    sb.append("    minActivated: ").append(toIndentedString(minActivated)).append("\n");
    sb.append("    maxActivated: ").append(toIndentedString(maxActivated)).append("\n");
    sb.append("    lowQuartile: ").append(toIndentedString(lowQuartile)).append("\n");
    sb.append("    highQuartile: ").append(toIndentedString(highQuartile)).append("\n");
    sb.append("    median: ").append(toIndentedString(median)).append("\n");
    sb.append("    activated: ").append(toIndentedString(activated)).append("\n");
    sb.append("    messagesSent: ").append(toIndentedString(messagesSent)).append("\n");
//    sb.append("    activatedIds: ").append(toIndentedString(activatedIds)).append("\n");
    sb.append("    matrix: ").append(toIndentedString(matrix)).append("\n");
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
