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
import vaim.io.api.structures.Node;

import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Node
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-03-26T11:32:33.543Z[GMT]")
public class Node   {
  @JsonProperty("id")
  private Integer id = null;

  @JsonProperty("neighbors")
  @Valid
  private List<Node> neighbors = null;

  public Node id(Integer id) {
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

  public Node neighbors(List<Node> neighbors) {
    this.neighbors = neighbors;
    return this;
  }

  public Node addNeighborsItem(Node neighborsItem) {
    if (this.neighbors == null) {
      this.neighbors = new ArrayList<Node>();
    }
    this.neighbors.add(neighborsItem);
    return this;
  }

  /**
   * Get neighbors
   * @return neighbors
  **/
  @ApiModelProperty(value = "")
  @Valid
  public List<Node> getNeighbors() {
    return neighbors;
  }

  public void setNeighbors(List<Node> neighbors) {
    this.neighbors = neighbors;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Node node = (Node) o;
    return Objects.equals(this.id, node.id) &&
        Objects.equals(this.neighbors, node.neighbors);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, neighbors);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Node {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
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
}
