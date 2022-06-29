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
import java.util.Stack;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import vaim.core.graph.Edge;
import vaim.core.graph.Graph;
import vaim.core.graph.Vertex;
import vaim.io.api.GetSectorDrawingApiController;
import vaim.io.db.DBWizard;
import vaim.io.db.DBWizard.QUERY_TYPE;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.neo4j.driver.summary.QueryType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;

/**
 * GraphSector
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-12-02T15:17:30.628Z[GMT]")


public class GraphSector   {
	@JsonProperty("sector_bounding_box")
	private BoundingBox sectorBoundingBox = null;

	@JsonProperty("structural_edges")
	@Valid
	private List<Edge> structuralEdges = null;

	@JsonProperty("structural_vertices")
	@Valid
	private List<Vertex> structuralVertices = null;

	@JsonProperty("simulation_chunks")
	@Valid
	private List<GraphChunk> simulationChunks = null;
	
	private static final Logger log = LoggerFactory.getLogger(GraphSector.class);

	public GraphSector sectorBoundingBox(BoundingBox sectorBoundingBox) {
		this.sectorBoundingBox = sectorBoundingBox;
		return this;
	}

	/**
	 * Get globalBoundingBox
	 * @return globalBoundingBox
	 **/
	@ApiModelProperty(value = "")
	@Valid
	public BoundingBox getSectorBoundingBox() {
		return sectorBoundingBox;
	}

	public void setSectorBoundingBox(BoundingBox sectorBoundingBox) {
		this.sectorBoundingBox = sectorBoundingBox;
	}

	public GraphSector structuralVertices(List<Vertex> structuralVertices) {
		this.structuralVertices = structuralVertices;
		return this;
	}

	public GraphSector addStructuralVertexItem(Vertex structuralEdgesItem) {
		if (this.structuralVertices == null) {
			this.structuralVertices = new ArrayList<Vertex>();
		}
		this.structuralVertices.add(structuralEdgesItem);
		return this;
	}

	/**
	 * Get structuralVertices
	 * @return structuralVertices
	 **/
	@ApiModelProperty(value = "")
	@Valid
	public List<Vertex> getStructuralVertices() {
		return structuralVertices;
	}

	public void setStructuralVertices(List<Vertex> structuralVertices) {
		this.structuralVertices = structuralVertices;
	}

	public GraphSector structuralEdges(List<Edge> structuralEdges) {
		this.structuralEdges = structuralEdges;
		return this;
	}

	public GraphSector addStructuralEdgesItem(Edge structuralEdgesItem) {
		if (this.structuralEdges == null) {
			this.structuralEdges = new ArrayList<Edge>();
		}
		this.structuralEdges.add(structuralEdgesItem);
		return this;
	}

	/**
	 * Get structuralEdges
	 * @return structuralEdges
	 **/
	@ApiModelProperty(value = "")
	@Valid
	public List<Edge> getStructuralEdges() {
		return structuralEdges;
	}

	public void setStructuralEdges(List<Edge> structuralEdges) {
		this.structuralEdges = structuralEdges;
	}

	public GraphSector simulationChunks(List<GraphChunk> simulationChunks) {
		this.simulationChunks = simulationChunks;
		return this;
	}

	public GraphSector addSimulationChunksItem(GraphChunk simulationChunksItem) {
		if (this.simulationChunks == null) {
			this.simulationChunks = new ArrayList<GraphChunk>();
		}
		this.simulationChunks.add(simulationChunksItem);
		return this;
	}

	/**
	 * Get simulationChunks
	 * @return simulationChunks
	 **/
	@ApiModelProperty(value = "")
	@Valid
	public List<GraphChunk> getSimulationChunks() {
		return simulationChunks;
	}

	public void setSimulationChunks(List<GraphChunk> simulationChunks) {
		this.simulationChunks = simulationChunks;
	}


	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		GraphSector graphSector = (GraphSector) o;
		return Objects.equals(this.sectorBoundingBox, graphSector.sectorBoundingBox) &&
				Objects.equals(this.structuralEdges, graphSector.structuralEdges) &&
				Objects.equals(this.simulationChunks, graphSector.simulationChunks);
	}

	@Override
	public int hashCode() {
		return Objects.hash(sectorBoundingBox, structuralEdges, simulationChunks);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class GraphSector {\n");

		sb.append("    globalBoundingBox: ").append(toIndentedString(sectorBoundingBox)).append("\n");
		sb.append("    structuralEdges: ").append(toIndentedString(structuralEdges)).append("\n");
		sb.append("    simulationChunks: ").append(toIndentedString(simulationChunks)).append("\n");
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

	public static GraphSector rebuildFromDatabase(GraphChunkQuery gq, int graphId, int resolution, String[] simulationIds, Connection conn) throws SQLException {
		DBWizard dbW = DBWizard.getInstance();
		PreparedStatement pms;	
		ResultSet rs;
		LinkedList<GraphChunk> ls = new LinkedList<GraphChunk>();

		int startX = gq.getStartX();
		int spanX = gq.getSpan_x();
		int startY = gq.getStartY();
		int spanY = gq.getSpan_y();

		Double maxX = Double.MIN_VALUE;
		Double minX = Double.MAX_VALUE;
		Double maxY = Double.MIN_VALUE;
		Double minY = Double.MAX_VALUE;	

		Stack<Graph> stackOfGraphs = new Stack<Graph>();

		for(String simulation : simulationIds) 
			stackOfGraphs.add(new Graph().id(graphId));

		LinkedList<Edge> structuralEdges = new LinkedList<Edge>();
		LinkedList<Vertex> structuralVertices = new LinkedList<Vertex>();
		HashSet<Long> loadedVertices = new HashSet<Long>();

		//		pms = conn.prepareStatement(DBWizard.getStringFromQuery(DBWizard.QUERY_TYPE.TEST_SQUARES));
//		pms.setInt(1, graphId);
//		pms.setInt(2, resolution);
//		pms.setInt(3, startX);
//		pms.setInt(4, startX + spanX);
//		pms.setInt(5, startY);
//		pms.setInt(6, startY + spanY);
//
//		rs = pms.executeQuery();
//		int counter = 0;
//		while(rs.next()) { 
//			System.out.println(rs.getLong(1) + " " + rs.getInt(2) + " " + rs.getInt(3));			
//			counter++;
//		}
//		System.out.println("Total: " + counter);
//
//		rs.close();
//		pms.close();

		pms = conn.prepareStatement(DBWizard.getStringFromQuery(DBWizard.QUERY_TYPE.GET_SUBSET_OF_VERTICES_SIMPLE)); //GET_SUBSET_OF_VERTICES_COMPLETE));
		pms.setInt(1, graphId);
		pms.setInt(2, resolution);
		pms.setInt(3, startX);
		pms.setInt(4, startX + spanX);
		pms.setInt(5, startY);
		pms.setInt(6, startY + spanY);
		//pms.setInt(7, resolution);		

		rs = pms.executeQuery();
		
		log.info("\t\tDB Request complete");
		
		String idList = "";
		
		while(rs.next()) {
			long id = rs.getLong(1);
			double x = rs.getDouble(2);
			double y = rs.getDouble(3);
			int rectX = rs.getInt(4);
			int rectY = rs.getInt(5);
			long degree = rs.getLong(6);
			
			idList += id+",";

			VertexCoords vc = new VertexCoords()
					.x(new BigDecimal(x))
					.y(new BigDecimal(y));
			maxX = Math.max(maxX, x);
			maxY = Math.max(maxY, y);
			minX = Math.min(minX, x);
			minY = Math.min(minY, y);	

			Vertex newV = new Vertex().id(id).coords(vc)
					.degree(degree)
					.vertexSector(new VertexCoords()
					.x(new BigDecimal(rectX))
					.y(new BigDecimal(rectY)) 
					);
			
			loadedVertices.add(id);

			/*try {
				Long[] arr = (Long[])rs.getArray(6).getArray();
				Double[] arrWeight = (Double[])rs.getArray(7).getArray();

				for(int i = 0; i < arr.length; i++)
					structuralEdges.add(new Edge().source(id).target(arr[i]).weight(arrWeight[i]));
			}catch(ClassCastException cce) {
				//NO OP 
			}catch(Exception e) {e.printStackTrace();}*/
			
			structuralVertices.add(newV);

		}			

		rs.close();
		pms.close();			
		
		idList = idList.substring(0, idList.length()-1);
		
		log.info("\t\tVertices recovered");
		
		pms = dbW.createStatement(conn, DBWizard.getStringFromQuery(
				QUERY_TYPE.GET_STRUCTURAL_EDGES_FROM_LIST).replace(DBWizard.LIST_PLACEHOLDER, idList));
		
		pms.setInt(1, graphId);
		
		rs = pms.executeQuery();
				
		while(rs.next()) {
			long id = rs.getLong(1);
			Long[] arr = (Long[])rs.getArray(4).getArray();
			Double[] arrWeight = (Double[])rs.getArray(5).getArray(); //REACTIVATE FOR WEIGHTS
			
			for(int i = 0; i < arr.length; i++) {
				structuralEdges.add(new Edge().source(id).target(arr[i]).weight(arrWeight[i]));
				if(!loadedVertices.contains(id)) {
					structuralVertices.add(new Vertex().id(id).coords(new VertexCoords(rs.getDouble(2), rs.getDouble(3))));
					loadedVertices.add(id);
				}
			}
		}
		
		rs.close();
		pms.close();
		
		log.info("\t\tEdges recovered");
		
		BoundingBox globalBox = new BoundingBox().maxX(new BigDecimal(maxX))
				.minX(new BigDecimal(minX))
				.maxY(new BigDecimal(maxY))
				.minY(new BigDecimal(minY));

		for(String simulation : simulationIds) {
			GraphChunk gc = new GraphChunk().simulationId(Integer.valueOf(simulation));
			int simulationId = Integer.valueOf(simulation);
			HashMap<Edge, Map<Integer, Double>> activeEdgesMap = new HashMap<Edge, Map<Integer, Double>>();
			HashMap<Long, Map<Integer, Double>> activeVerticesMap = new HashMap<Long, Map<Integer, Double>>();
			HashMap<Long, Map<Integer, Integer>> activeVerticesMapFramesCount = new HashMap<Long, Map<Integer, Integer>>();
			
			int maxFrame = 0;							

			pms = dbW.createStatement(conn, 
					DBWizard.getStringFromQuery(
							DBWizard.QUERY_TYPE.GET_ACTIVATED_VERTICES_IN_SIMULATION_FROM_LIST)
								.replace(DBWizard.LIST_PLACEHOLDER, idList));
			
			pms.setInt(1, simulationId);
			pms.setInt(2, simulationId);

			rs = pms.executeQuery();
			
			log.info("\t\tActive Edges recovered");
			
			while(rs.next()) {
				long source = rs.getLong(1);
				int frame = rs.getInt(2); 
				double edgeWeight = rs.getDouble(3);					
				long target = rs.getLong(4);

				if(rs.wasNull()) {
					//activeVerticesMap.put(rs.getLong(2), new Long[]{(long) 0, (long) -1});
					HashMap<Integer, Double> seedMap = new HashMap<Integer, Double>();
					seedMap.put(0, 1.0d);
					activeVerticesMap.put(source, seedMap);
					continue;
				}

				maxFrame = Math.max(frame, maxFrame);
//				System.out.println(frame + " max " + maxFrame);

				if(loadedVertices.contains(source)) {
					Edge e = new Edge().source(source).target(target);//.instantChance(edgeWeight);//.activeAt(frame).seedId(rs.getLong(4)).weight(rs.getDouble(5));
					//currentGraph.addEdge(e);				

					if(!activeEdgesMap.containsKey(e))
						activeEdgesMap.put(e, new TreeMap<Integer, Double>());

					Map<Integer, Double> edgeFrameMap = activeEdgesMap.get(e);
					if(!edgeFrameMap.containsKey(frame))
						edgeFrameMap.put(frame, edgeWeight);
					else
						edgeFrameMap.put(frame, (edgeFrameMap.get(frame) + edgeWeight));									
				}
				
				if(!activeVerticesMap.containsKey(target)) {
					activeVerticesMap.put(target, new TreeMap<Integer, Double>());
					activeVerticesMapFramesCount.put(target, new TreeMap<Integer, Integer>());
				}

				Map<Integer, Double> frameMap = activeVerticesMap.get(target);
				Map<Integer, Integer> countFrameMap = activeVerticesMapFramesCount.get(target);
				if(!frameMap.containsKey(frame)) {
					frameMap.put(frame, edgeWeight);
					countFrameMap.put(frame, 1);
				}else{
					countFrameMap.put(frame, countFrameMap.get(frame) + 1);
					frameMap.put(frame, (frameMap.get(frame) + edgeWeight)/countFrameMap.get(frame));					
				}

				//activeVerticesMap.put(rs.getLong(2), new Long[]{(long) frame, rs.getLong(4)});
			}

			rs.close();
			pms.close();
			
			
			log.info("\t\tActivations recovered");
			
			//}

			//REACTIVATE FOR DRAWING
			/*SfdpExecutor sfdp = SfdpBuilder.BuildSfdpExecutor();
				sfdp.execute(currentGraph);

				maxX = Double.MIN_VALUE;
				minX = Double.MAX_VALUE;
				maxY = Double.MIN_VALUE;
				minY = Double.MAX_VALUE;		

				pms = dbW.createStatement(conn, DBWizard.getStringFromQuery(QUERY_TYPE.GET_SIMULATION_COORDINATES).replace(DBWizard.LIST_PLACEHOLDER, vertexList));
				pms.setLong(1, Integer.valueOf(simulation));

				rs = pms.executeQuery();

				while(rs.next()) {
					Vertex v = new Vertex()
							.id(rs.getLong(1))
							.coords(new VertexCoords().x(BigDecimal.valueOf(rs.getDouble(2))).y(BigDecimal.valueOf(rs.getDouble(3))))
							.globalCoords(g.getVertex(rs.getLong(1)).getCoords());

					if(activeVerticesMap.containsKey(v.getId())) {
						Long[] activationData = activeVerticesMap.get(v.getId());
						v = v.activeAt(activationData[0]).seedId(activationData[1]);							
					}
					//currentGraph.getVertex(rs.getLong(1)).setCoords(new VertexCoords().x(BigDecimal.valueOf(rs.getDouble(2))).y(BigDecimal.valueOf(rs.getDouble(3))));
					gc.addVertexItem(v);
					maxX = Math.max(maxX, rs.getDouble(2));
					maxY = Math.max(maxY, rs.getDouble(3));
					minX = Math.min(minX, rs.getDouble(2));
					minY = Math.min(minY, rs.getDouble(3));	
				}

				rs.close();
				pms.close();
			 */
			
			log.info("\t\tLoading vertices into return structure");
			
			for(Vertex v : structuralVertices) {
				Vertex newV = new Vertex().id(v.getId());
				Frames currFrames = new Frames();					

				if(activeVerticesMap.containsKey(v.getId())) {
					Map<Integer, Double> activationData = activeVerticesMap.get(v.getId());
					//int lastIndex = 0, i;
					//for(Entry<Integer,Double> e : activationData.entrySet()) {
					for(int i = 0; i <= maxFrame; i++) {
						/*for(i = lastIndex; i < e.getKey(); i++) {
							currFrames.addInstantItem(new BigDecimal(0.0d)).addCumulativeItem(new BigDecimal(0.0d));
						}
						lastIndex = i + 1;*/
						if(activationData.containsKey(i))
							currFrames.addInstantItem(new BigDecimal(activationData.get(i))).addCumulativeItem(new BigDecimal(activationData.get(i)));
						else
							currFrames.addInstantItem(new BigDecimal(0.0d)).addCumulativeItem(new BigDecimal(0.0d));
					}
				}
				gc.addVertexItem(newV.frames(currFrames));
				//VertexCoords coords = vI.getCoords();
				//gc.addVertexItem(new Vertex().id(vI.getId()).coords(coords).neighbors(vI.getNeighbors()));
				//							maxX = Math.max(maxX, coords.getX().doubleValue());
				//							maxY = Math.max(maxY, coords.getY().doubleValue());
				//							minX = Math.min(minX, coords.getX().doubleValue());
				//							minY = Math.min(minY, coords.getY().doubleValue());						
			}				

			log.info("\t\tLoading edges into return structure");
			
			for(Edge e : activeEdgesMap.keySet()) {
				Frames currFrames = new Frames();					
				Map<Integer, Double> activationData = activeEdgesMap.get(e);
				//int lastIndex = 0, i;
				//for(Entry<Integer,Double> e : activationData.entrySet()) {
				for(int i = 0; i <= maxFrame; i++) {
					/*for(i = lastIndex; i < e.getKey(); i++) {
						currFrames.addInstantItem(new BigDecimal(0.0d)).addCumulativeItem(new BigDecimal(0.0d));
					}
					lastIndex = i + 1;*/
					if(activationData.containsKey(i))
						currFrames.addInstantItem(new BigDecimal(activationData.get(i))).addCumulativeItem(new BigDecimal(activationData.get(i)));
					else
						currFrames.addInstantItem(new BigDecimal(0.0d)).addCumulativeItem(new BigDecimal(0.0d));
				}
				gc.addEdgeItem(e.frames(currFrames));
			}

			ls.add(gc);
		}
		//System.out.println("Returning Structure");

		return new GraphSector().structuralVertices(structuralVertices).structuralEdges(structuralEdges).simulationChunks(ls).sectorBoundingBox(globalBox);
	}
}
