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
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * NeighborhoodRequest
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-03-26T11:32:33.543Z[GMT]")
public class NeighborhoodRequest   {
  @JsonProperty("nodes")
  @Valid
  private List<Integer> nodes = null;

  @JsonProperty("depth")
  private Integer depth = null;

  public NeighborhoodRequest nodes(List<Integer> nodes) {
    this.nodes = nodes;
    return this;
  }

  public NeighborhoodRequest addNodesItem(Integer nodesItem) {
    if (this.nodes == null) {
      this.nodes = new ArrayList<Integer>();
    }
    this.nodes.add(nodesItem);
    return this;
  }

  /**
   * Get nodes
   * @return nodes
  **/
  @ApiModelProperty(value = "")

  public List<Integer> getNodes() {
    return nodes;
  }

  public void setNodes(List<Integer> nodes) {
    this.nodes = nodes;
  }

  public NeighborhoodRequest depth(Integer depth) {
    this.depth = depth;
    return this;
  }

  /**
   * Get depth
   * @return depth
  **/
  @ApiModelProperty(value = "")

  public Integer getDepth() {
    return depth;
  }

  public void setDepth(Integer depth) {
    this.depth = depth;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NeighborhoodRequest neighborhoodRequest = (NeighborhoodRequest) o;
    return Objects.equals(this.nodes, neighborhoodRequest.nodes) &&
        Objects.equals(this.depth, neighborhoodRequest.depth);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nodes, depth);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NeighborhoodRequest {\n");
    
    sb.append("    nodes: ").append(toIndentedString(nodes)).append("\n");
    sb.append("    depth: ").append(toIndentedString(depth)).append("\n");
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
