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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * Frames
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-02-17T10:19:00.549Z[GMT]")


public class Frames   {
  @JsonProperty("instant")
  @Valid
  private List<BigDecimal> instant = null;

  @JsonProperty("cumulative")
  @Valid
  private List<BigDecimal> cumulative = null;

  public Frames instant(List<BigDecimal> instant) {
    this.instant = instant;
    return this;
  }

  public Frames addInstantItem(BigDecimal instantItem) {
    if (this.instant == null) {
      this.instant = new ArrayList<BigDecimal>();
    }
    this.instant.add(instantItem);
    return this;
  }

  /**
   * Get instant
   * @return instant
   **/
  @ApiModelProperty(value = "")
      @Valid
    public List<BigDecimal> getInstant() {
    return instant;
  }

  public void setInstant(List<BigDecimal> instant) {
    this.instant = instant;
  }

  public Frames cumulative(List<BigDecimal> cumulative) {
    this.cumulative = cumulative;
    return this;
  }

  public Frames addCumulativeItem(BigDecimal cumulativeItem) {
    if (this.cumulative == null) {
      this.cumulative = new ArrayList<BigDecimal>();
      this.cumulative.add(cumulativeItem);
    }else {
    	double lastValue = this.cumulative.get(this.cumulative.size() - 1).doubleValue();
    	this.cumulative.add(new BigDecimal(lastValue + cumulativeItem.doubleValue()));
    }
    return this;
  }

  /**
   * Get cumulative
   * @return cumulative
   **/
  @ApiModelProperty(value = "")
      @Valid
    public List<BigDecimal> getCumulative() {
    return cumulative;
  }

  public void setCumulative(List<BigDecimal> cumulative) {
    this.cumulative = cumulative;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Frames frames = (Frames) o;
    return Objects.equals(this.instant, frames.instant) &&
        Objects.equals(this.cumulative, frames.cumulative);
  }

  @Override
  public int hashCode() {
    return Objects.hash(instant, cumulative);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Frames {\n");
    
    sb.append("    instant: ").append(toIndentedString(instant)).append("\n");
    sb.append("    cumulative: ").append(toIndentedString(cumulative)).append("\n");
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
