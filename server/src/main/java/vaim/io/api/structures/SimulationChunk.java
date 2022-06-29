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
import vaim.core.graph.Edge;
import vaim.io.api.structures.SimulationFrame;
import vaim.io.db.DBWizard;
import vaim.io.db.DBWizard.QUERY_TYPE;
import vaim.layout.MatrixQuantizer;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * SimulationChunk
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-04-23T13:52:31.074Z[GMT]")
public class SimulationChunk   {

	@JsonProperty("simulation_id")
	private Integer simulationId = null;	

	@JsonProperty("start_frame")
	private Integer startFrame = null;

	@JsonProperty("end_frame")
	private Integer endFrame = null;

	@JsonProperty("aggregation")
	private Integer aggregation = null;

	@JsonProperty("frames")
	@Valid
	private List<SimulationFrame> frames = null;

	private int offset;

	public SimulationChunk startFrame(Integer startFrame) {
		this.startFrame = startFrame;
		return this;
	}

	public SimulationChunk simulationId(Integer simulationId) {
		this.simulationId = simulationId;
		return this;
	}  

	/**
	 * Get simulationId
	 * @return simulationId
	 **/
	@ApiModelProperty(value = "")  

	public Integer getSimulationId() {
		return simulationId;
	}

	public void setSimulationId(Integer simulationId) {
		this.simulationId = simulationId;
	}	

	/**
	 * Get startFrame
	 * @return startFrame
	 **/
	@ApiModelProperty(value = "")

	public Integer getStartFrame() {
		return startFrame;
	}

	public void setStartFrame(Integer startFrame) {
		this.startFrame = startFrame;
	}

	public SimulationChunk endFrame(Integer endFrame) {
		this.endFrame = endFrame;
		return this;
	}

	/**
	 * Get endFrame
	 * @return endFrame
	 **/
	@ApiModelProperty(value = "")

	public Integer getEndFrame() {
		return endFrame;
	}

	public void setEndFrame(Integer endFrame) {
		this.endFrame = endFrame;
	}

	public SimulationChunk aggregation(Integer aggregation) {
		this.aggregation = aggregation;
		return this;
	}

	/**
	 * Get aggregation
	 * @return aggregation
	 **/
	@ApiModelProperty(value = "")

	public Integer getAggregation() {
		return aggregation;
	}

	public void setAggregation(Integer aggregation) {
		this.aggregation = aggregation;
	}

	public SimulationChunk frames(List<SimulationFrame> frames) {
		this.frames = frames;
		return this;
	}

	public SimulationChunk addFramesItem(SimulationFrame framesItem) {
		if (this.frames == null) {
			this.frames = new ArrayList<SimulationFrame>();
		}
		this.frames.add(framesItem);
		return this;
	}

	/**
	 * Get frames
	 * @return frames
	 **/
	@ApiModelProperty(value = "")
	@Valid
	public List<SimulationFrame> getFrames() {
		return frames;
	}

	public void setFrames(List<SimulationFrame> frames) {
		this.frames = frames;
	}


	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		SimulationChunk simulationChunk = (SimulationChunk) o;
		return Objects.equals(this.startFrame, simulationChunk.startFrame) &&
				Objects.equals(this.endFrame, simulationChunk.endFrame) &&
				Objects.equals(this.aggregation, simulationChunk.aggregation) &&
				Objects.equals(this.frames, simulationChunk.frames);
	}

	@Override
	public int hashCode() {
		return Objects.hash(startFrame, endFrame, aggregation, frames);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class SimulationChunk {\n");

		sb.append("    startFrame: ").append(toIndentedString(startFrame)).append("\n");
		sb.append("    endFrame: ").append(toIndentedString(endFrame)).append("\n");
		sb.append("    aggregation: ").append(toIndentedString(aggregation)).append("\n");
		sb.append("    frames: ").append(toIndentedString(frames)).append("\n");
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

	public static SimulationChunk rebuildFromDatabase(int simulationId, Connection conn, MatrixQuantizer mq, int aggregation, int framestart, Integer offset) throws SQLException {
		DBWizard dbW = DBWizard.getInstance();
		PreparedStatement pms;
		ResultSet rs;
		SimulationChunk sc = new SimulationChunk().simulationId(simulationId);
		sc.setAggregation(aggregation);
		sc.setStartFrame(framestart);
		if(framestart == 0) {
			pms = dbW.createStatement(conn,QUERY_TYPE.LIST_SEEDS);
			pms.setInt(1, simulationId);
			rs = pms.executeQuery();
			SimulationFrame zeroSimFrame = new SimulationFrame();
			rs.next();
			Long[] arr = (Long[])rs.getArray(1).getArray();

			for(long seedId : arr) {
				//long seedId = rs.getLong(1);
				mq.processNewEdge(new Edge().source(seedId).target(seedId).weight(1.0d));
			}
			zeroSimFrame.addMatrixItems(mq.copyMatrixStateAsList());
			sc.addFramesItem(zeroSimFrame);

			rs.close();
			pms.close();
		}

		pms = dbW.createStatement(conn,QUERY_TYPE.GET_SIMULATION_DATA);
		pms.setInt(1, simulationId);

		rs = pms.executeQuery();
		rs.next();

		int steps = rs.getInt(1);
		int localOffset;
		pms.close();

		if(offset == null)
			localOffset = steps;
		else
			localOffset = offset;

		int currentCursor = framestart;
		int windowClose = computeOffset(currentCursor + aggregation, steps);
		while(true) {
			SimulationFrame sf = new SimulationFrame();

			pms = dbW.createStatement(conn,QUERY_TYPE.GET_STEP_STATISTICS);
			pms.setInt(1, simulationId);
			pms.setLong(2, currentCursor);
			pms.setLong(3, windowClose);
			rs = pms.executeQuery();    

			long tempMin = 0, tempMax = 0;					
			double tempMedian = 0, tempLowQuart = 0, tempHighQuart = 0;					

			while(rs.next()) {
				tempMax += rs.getLong(2);						
				tempMin += rs.getLong(3);
				tempMedian += rs.getDouble(4);
				tempLowQuart += rs.getDouble(5);
				tempHighQuart += rs.getDouble(6);
			}

			sf.setMedian(BigDecimal.valueOf(tempMedian/rs.getFetchSize()));
			sf.setLowQuartile(BigDecimal.valueOf(tempLowQuart/rs.getFetchSize()));
			sf.setHighQuartile(BigDecimal.valueOf(tempHighQuart/rs.getFetchSize()));
			sf.setMinActivated(Math.round(tempMin/(float)rs.getFetchSize()));
			sf.setMaxActivated(Math.round(tempMax/(float)rs.getFetchSize()));

			rs.close();
			pms.close();

			pms = dbW.createStatement(conn,QUERY_TYPE.GET_SIMULATION_FRAME);
			pms.setInt(1, simulationId);
			pms.setLong(2, currentCursor);
			pms.setLong(3, windowClose);
			rs = pms.executeQuery();        		        	
			
			while(rs.next()) {
				//long source = rs.getLong(1);
				long target = rs.getLong(1);
				mq.processNewEdge(new Edge().source((long) -1).target(target).weight(rs.getDouble(2)));        				
			}

			sc.addFramesItem(sf);

			pms.close();

			sf.addMatrixItems(mq.copyMatrixStateAsList());

			if(windowClose < localOffset && windowClose < steps) {
				currentCursor = windowClose;
				windowClose = computeOffset(currentCursor + aggregation, steps);
			}else
				break;
		}
		sc.setEndFrame(windowClose);		
		return sc.offset(localOffset);
	}

	private SimulationChunk offset(int localOffset) {
		this.offset = localOffset;
		return this;
	}
	
	public int getOffset() {
		return this.offset;
	}

	private static int computeOffset(int i, int steps) {
		if(i > steps)
			return steps;
		return i;
	}
}
