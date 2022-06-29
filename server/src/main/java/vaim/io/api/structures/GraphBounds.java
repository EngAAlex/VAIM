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
 * GraphBounds
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-05-24T08:06:43.984Z[GMT]")
public class GraphBounds   {
  @JsonProperty("width")
  private BigDecimal width = null;

  @JsonProperty("height")
  private BigDecimal height = null;

  @JsonProperty("minX")
  private BigDecimal minX = null;

  @JsonProperty("maxX")
  private BigDecimal maxX = null;

  @JsonProperty("minY")
  private BigDecimal minY = null;

  @JsonProperty("maxY")
  private BigDecimal maxY = null;

  public GraphBounds width(BigDecimal width) {
    this.width = width;
    return this;
  }

  /**
   * Get width
   * @return width
  **/
  @ApiModelProperty(value = "")
  
    @Valid
    public BigDecimal getWidth() {
    return width;
  }

  public void setWidth(BigDecimal width) {
    this.width = width;
  }

  public GraphBounds height(BigDecimal height) {
    this.height = height;
    return this;
  }

  /**
   * Get height
   * @return height
  **/
  @ApiModelProperty(value = "")
  
    @Valid
    public BigDecimal getHeight() {
    return height;
  }

  public void setHeight(BigDecimal height) {
    this.height = height;
  }

  public GraphBounds minX(BigDecimal minX) {
    this.minX = minX;
    return this;
  }

  /**
   * Get minX
   * @return minX
  **/
  @ApiModelProperty(value = "")
  
    @Valid
    public BigDecimal getMinX() {
    return minX;
  }

  public void setMinX(BigDecimal minX) {
    this.minX = minX;
  }

  public GraphBounds maxX(BigDecimal maxX) {
    this.maxX = maxX;
    return this;
  }

  /**
   * Get maxX
   * @return maxX
  **/
  @ApiModelProperty(value = "")
  
    @Valid
    public BigDecimal getMaxX() {
    return maxX;
  }

  public void setMaxX(BigDecimal maxX) {
    this.maxX = maxX;
  }

  public GraphBounds minY(BigDecimal minY) {
    this.minY = minY;
    return this;
  }

  /**
   * Get minY
   * @return minY
  **/
  @ApiModelProperty(value = "")
  
    @Valid
    public BigDecimal getMinY() {
    return minY;
  }

  public void setMinY(BigDecimal minY) {
    this.minY = minY;
  }

  public GraphBounds maxY(BigDecimal maxY) {
    this.maxY = maxY;
    return this;
  }

  /**
   * Get maxY
   * @return maxY
  **/
  @ApiModelProperty(value = "")
  
    @Valid
    public BigDecimal getMaxY() {
    return maxY;
  }

  public void setMaxY(BigDecimal maxY) {
    this.maxY = maxY;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GraphBounds graphBounds = (GraphBounds) o;
    return Objects.equals(this.width, graphBounds.width) &&
        Objects.equals(this.height, graphBounds.height) &&
        Objects.equals(this.minX, graphBounds.minX) &&
        Objects.equals(this.maxX, graphBounds.maxX) &&
        Objects.equals(this.minY, graphBounds.minY) &&
        Objects.equals(this.maxY, graphBounds.maxY);
  }

  @Override
  public int hashCode() {
    return Objects.hash(width, height, minX, maxX, minY, maxY);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GraphBounds {\n");
    
    sb.append("    width: ").append(toIndentedString(width)).append("\n");
    sb.append("    height: ").append(toIndentedString(height)).append("\n");
    sb.append("    minX: ").append(toIndentedString(minX)).append("\n");
    sb.append("    maxX: ").append(toIndentedString(maxX)).append("\n");
    sb.append("    minY: ").append(toIndentedString(minY)).append("\n");
    sb.append("    maxY: ").append(toIndentedString(maxY)).append("\n");
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
