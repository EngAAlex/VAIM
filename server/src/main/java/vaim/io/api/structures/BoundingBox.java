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
 * BoundingBox
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-11-09T17:59:58.515Z[GMT]")


public class BoundingBox   {
  @JsonProperty("maxX")
  private BigDecimal maxX = null;

  @JsonProperty("maxY")
  private BigDecimal maxY = null;

  @JsonProperty("minX")
  private BigDecimal minX = null;

  @JsonProperty("minY")
  private BigDecimal minY = null;

  public BoundingBox maxX(BigDecimal maxX) {
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

  public BoundingBox maxY(BigDecimal maxY) {
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

  public BoundingBox minX(BigDecimal minX) {
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

  public BoundingBox minY(BigDecimal minY) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BoundingBox boundingBox = (BoundingBox) o;
    return Objects.equals(this.maxX, boundingBox.maxX) &&
        Objects.equals(this.maxY, boundingBox.maxY) &&
        Objects.equals(this.minX, boundingBox.minX) &&
        Objects.equals(this.minY, boundingBox.minY);
  }

  @Override
  public int hashCode() {
    return Objects.hash(maxX, maxY, minX, minY);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BoundingBox {\n");
    
    sb.append("    maxX: ").append(toIndentedString(maxX)).append("\n");
    sb.append("    maxY: ").append(toIndentedString(maxY)).append("\n");
    sb.append("    minX: ").append(toIndentedString(minX)).append("\n");
    sb.append("    minY: ").append(toIndentedString(minY)).append("\n");
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
