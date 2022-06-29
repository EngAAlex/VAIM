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
import java.math.BigDecimal;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * VertexCoords
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-03-22T10:35:45.405Z[GMT]")
public class VertexCoords   {
  @JsonProperty("x")
  private BigDecimal x = null;

  @JsonProperty("y")
  private BigDecimal y = null;
  
  public VertexCoords() {}
  
  public VertexCoords(double x, double y) {
	  this.x = new BigDecimal(x);
	  this.y = new BigDecimal(y);
  }

  public VertexCoords(VertexCoords vc) {
	  this.x = vc.getX();
	  this.y = vc.getY();
  }
  
  public VertexCoords x(BigDecimal x) {
    this.x = x;
    return this;
  }

  /**
   * Get x
   * @return x
  **/
  @ApiModelProperty(value = "")
  
    @Valid
    public BigDecimal getX() {
    return x;
  }

  public void setX(BigDecimal x) {
    this.x = x;
  }

  public VertexCoords y(BigDecimal y) {
    this.y = y;
    return this;
  }

  /**
   * Get y
   * @return y
  **/
  @ApiModelProperty(value = "")
  
    @Valid
    public BigDecimal getY() {
    return y;
  }

  public void setY(BigDecimal y) {
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
    VertexCoords vertexCoords = (VertexCoords) o;
    return Objects.equals(this.x, vertexCoords.x) &&
        Objects.equals(this.y, vertexCoords.y);
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VertexCoords {\n");
    
    sb.append("    x: ").append(toIndentedString(x)).append("\n");
    sb.append("    y: ").append(toIndentedString(y)).append("\n");
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
