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
 * GraphList
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-03-02T11:05:55.322Z[GMT]")


public class GraphList   {
  @JsonProperty("graphlist")
  @Valid
  private List<GraphListInner> graphlist = null;

  @JsonProperty("maximizations")
  @Valid
  private List<Maximization> maximizations = null;

  public GraphList graphlist(List<GraphListInner> graphlist) {
    this.graphlist = graphlist;
    return this;
  }

  public void addGraphElement(GraphListInner graph) {
	  if(graphlist == null)
		  graphlist = new ArrayList<GraphListInner>();
	  graphlist.add(graph);
  } 
  
  /**
   * Get graphlist
   * @return graphlist
   **/
  @ApiModelProperty(value = "")
      @Valid
    public List<GraphListInner> getGraphlist() {
    return graphlist;
  }

  public void setGraphlist(List<GraphListInner> graphlist) {
    this.graphlist = graphlist;
  }

  public GraphList maximizations(List<Maximization> maximizations) {
    this.maximizations = maximizations;
    return this;
  }

  public GraphList addMaximizationsItem(Maximization maximizationsItem) {
    if (this.maximizations == null) {
      this.maximizations = new ArrayList<Maximization>();
    }
    this.maximizations.add(maximizationsItem);
    return this;
  }

  /**
   * Get maximizations
   * @return maximizations
   **/
  @ApiModelProperty(value = "")
      @Valid
    public List<Maximization> getMaximizations() {
    return maximizations;
  }

  public void setMaximizations(List<Maximization> maximizations) {
    this.maximizations = maximizations;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GraphList graphList = (GraphList) o;
    return Objects.equals(this.graphlist, graphList.graphlist) &&
        Objects.equals(this.maximizations, graphList.maximizations);
  }

  @Override
  public int hashCode() {
    return Objects.hash(graphlist, maximizations);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GraphList {\n");
    
    sb.append("    graphlist: ").append(toIndentedString(graphlist)).append("\n");
    sb.append("    maximizations: ").append(toIndentedString(maximizations)).append("\n");
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
