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
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * GraphChunkQuery
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-11-06T15:09:02.072Z[GMT]")


public class GraphChunkQuery   {
	@JsonProperty("start_x")
	private Integer startX = null;

	@JsonProperty("start_y")
	private Integer startY = null;

	@JsonProperty("span_x")
	private Integer spanX = null;

	@JsonProperty("span_y")
	private Integer spanY = null;  

	public GraphChunkQuery startX(Integer startX) {
		this.startX = startX;
		return this;
	}

	/**
	 * Get startX
	 * @return startX
	 **/
	@ApiModelProperty(value = "")

	public Integer getStartX() {
		return startX;
	}

	public void setStartX(Integer startX) {
		this.startX = startX;
	}

	public GraphChunkQuery startY(Integer startY) {
		this.startY = startY;
		return this;
	}

	/**
	 * Get startY
	 * @return startY
	 **/
	@ApiModelProperty(value = "")

	public Integer getStartY() {
		return startY;
	}

	public void setStartY(Integer startY) {
		this.startY = startY;
	}

	public GraphChunkQuery span_x(Integer span) {
		this.spanX = span;
		return this;
	}

	public GraphChunkQuery span_y(Integer span) {
		this.spanY = span;
		return this;
	}

	/**
	 * Get span
	 * @return span
	 **/
	@ApiModelProperty(value = "")

	public Integer getSpan_x() {
		return spanX;
	}

	public Integer getSpan_y() {
		return spanY;
	}

	public void setSpan_y(Integer span_y) {
		this.spanY = span_y;
	}

	public void setSpan_x(Integer span) {
		this.spanX = span;
	}


	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		GraphChunkQuery graphChunkQuery = (GraphChunkQuery) o;
		return Objects.equals(this.startX, graphChunkQuery.startX) &&
				Objects.equals(this.startY, graphChunkQuery.startY) &&
				Objects.equals(this.spanX, graphChunkQuery.spanX) &&
				Objects.equals(this.spanY, graphChunkQuery.spanY);
	}

	@Override
	public int hashCode() {
		return Objects.hash(startX, startY, spanX, spanY);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class GraphChunkQuery {\n");

		sb.append("    startX: ").append(toIndentedString(startX)).append("\n");
		sb.append("    startY: ").append(toIndentedString(startY)).append("\n");
		sb.append("    spanX: ").append(toIndentedString(spanX)).append("\n");
		sb.append("    spanX: ").append(toIndentedString(spanY)).append("\n");
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

	public long[] toArray() {
		return new long[] {startX, startY, spanX, spanY};
	}

	public static GraphChunkQuery rebuildFromDataString(String input) {
		GraphChunkQuery gc = null;
		String[] tmpString = input.split(";");
		int startX = -1;
		int startY = -1;
		int spanX = -1;
		int spanY = -1;
		try {
			if(input != null) {
				startX = Integer.parseInt(tmpString[0]);
				startX = Integer.parseInt(tmpString[1]);
				startX = Integer.parseInt(tmpString[2]);
				startX = Integer.parseInt(tmpString[3]);
			}
			gc = new GraphChunkQuery().startX(startX).startY(startY).span_x(spanX).span_y(spanY);
		}catch(Exception e) {
			//NO-OP
		}
		return gc;
	}
}
