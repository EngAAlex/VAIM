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

package vaim.layout;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import vaim.core.graph.Edge;
import vaim.core.graph.Graph;
import vaim.core.graph.Vertex;
import vaim.io.api.structures.GraphBounds;
import vaim.io.api.structures.GraphQuantum;
import vaim.io.api.structures.VertexCoords;
import vaim.io.db.DBWizard;
import vaim.io.db.DBWizard.QUERY_TYPE;

public class MatrixQuantizer {

	public static final int[] RESOLUTIONS = {8, 10, 12, 14, 16};
	public static final int DEFAULT_RESOLUTION = RESOLUTIONS[1];	

	public static final Function<Vertex, Integer> DEFAULT_VERTEX_AGGREGATOR =
			((Vertex v) -> {
				return 1;
			});

	public static final Function<Edge, Double> DEFAULT_EDGE_AGGREGATOR =
			((Edge e) -> {
				return e.getWeight();
			});			

	GraphQuantum[][] matrix; //TODO: Make this immutable
	GraphBounds bounds;	
	HashMap<Long, int[]> inverseVertexMap = new HashMap<Long, int[]>();
	HashMap<String, Set<Long>> directVertexMap = new HashMap<String, Set<Long>>();
	Function<Vertex, Integer> vertexAggregator;
	Function<Edge, Double> edgeAggregator;
	int resolution;

	private float cellWidth;			

	public MatrixQuantizer(int resolution) {
		matrix = new GraphQuantum[resolution][resolution];
		this.resolution = resolution;
		vertexAggregator = DEFAULT_VERTEX_AGGREGATOR;
		edgeAggregator = DEFAULT_EDGE_AGGREGATOR;
		for(int i = 0; i<resolution; i++)
			for(int t = 0; t<resolution; t++)
				matrix[i][t] = new GraphQuantum(i, t);
	}

	public MatrixQuantizer(int resolution, GraphBounds bounds) {
		this(resolution);
		//this.bounds = bounds;
		cellWidth = bounds.getWidth().floatValue() > bounds.getHeight().floatValue() ?
				bounds.getWidth().floatValue()/resolution :
					bounds.getHeight().floatValue()/resolution;
	}

	public MatrixQuantizer withVertexAggregator(Function<Vertex, Integer> agg) {
		this.vertexAggregator = agg;
		return this;
	}

	public MatrixQuantizer withEdgeAggregator(Function<Edge, Double> agg) {
		this.edgeAggregator = agg;
		return this;
	}

	public void processNewVertex(Vertex v) throws Exception {				
		VertexCoords coords = v.getCoords();
		int row, col;
		double rowD, colD;
		rowD = (int) (coords.getX().floatValue()/cellWidth);//%resolution;
		colD = (int) (coords.getY().floatValue()/cellWidth);//%resolution;

		if(rowD < 0) {
			if(rowD < 0.0001)
				throw new Exception("ERROR while quantizing");
			else
				row = (int) Math.round(rowD);
		}else if (rowD == resolution)
			row = resolution - 1;		
		else
			row = (int) Math.floor(rowD);

		if(colD < 0) {
			if(colD < 0.0001)
				throw new Exception("ERROR while quantizing");
			else
				col = (int) Math.round(colD);
		}else if (colD == resolution)
			col = resolution - 1;	
		else
			col = (int) Math.floor(colD);

		String ddcoords = row + "-" + col;
		int[] mmcoords = new int[] {row, col};
		inverseVertexMap.put(v.getId(), mmcoords);

		if(!directVertexMap.containsKey(ddcoords)) 
			directVertexMap.put(ddcoords, new HashSet<Long>());

		directVertexMap.get(ddcoords).add(v.getId());
		matrix[row][col].addElementsToVerticesAggregate(vertexAggregator.apply(v));			
	}

	public void processNewEdge(Edge e) {
		int[] tgtLoc = inverseVertexMap.get(e.getTarget());
		matrix[tgtLoc[0]][tgtLoc[1]].addElementsToEdgesAggregate(edgeAggregator.apply(e));		
	}

	public Set<Long> getVerticesInSector(int row, int col){
		if(!directVertexMap.containsKey(row + "-" + col))
			return Set.of();
		return directVertexMap.get(row + "-" + col);
	}

	public GraphQuantum[][] copyMatrixState(){
		GraphQuantum[][] copy = new GraphQuantum[matrix.length][matrix.length];
		try {
			for(int i=0; i<matrix.length; i++)
				for(int j=0; j<matrix.length; j++)
					copy[i][j] = (GraphQuantum) matrix[i][j].clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return copy;
	}

	public List<GraphQuantum> copyMatrixStateAsList(){
		LinkedList<GraphQuantum> list = new LinkedList<GraphQuantum>();
		try {
			for(int i=0; i<matrix.length; i++)
				for(int j=0; j<matrix.length; j++)
					if(matrix[i][j] == null)
						list.add(new GraphQuantum(i, j));
					else
						list.add((GraphQuantum) matrix[i][j].clone());
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}		
		return list;
	}

	public void rebuildFromDatabase(Connection conn, int graphId) throws Exception {
		DBWizard dbW = DBWizard.getInstance();
		PreparedStatement pms = dbW.createStatement(conn, QUERY_TYPE.GET_VERTEX_CELL);

		pms.setInt(1, graphId);
		pms.setInt(2, resolution);

		ResultSet rs = pms.executeQuery();

		while(rs.next()) {
			long id = rs.getLong(1);
			int row = rs.getInt(2);
			int col = rs.getInt(3);
			int[] mmcoords = new int[] {row, col};
			inverseVertexMap.put(id, mmcoords);
			String sscoords = row + "-" + col;
			//			if(matrix[row][col] == null) {
			//				matrix[row][col] = new GraphQuantum(row, col);
			if(!directVertexMap.containsKey(sscoords)) {
				directVertexMap.put(sscoords, new HashSet<Long>());
			}
			directVertexMap.get(sscoords).add(id);
			matrix[row][col].addElementsToVerticesAggregate(1);	
		}

	}

	public GraphQuantum[][] getMatrix() {
		return matrix;
	}

	public static GraphQuantum[][] quantizeGraph(Graph g, Function<Vertex, Integer> vertexAggregator, Function<Edge, Integer> edgeAggregator, int resolution){
		GraphQuantum[][] matrix = new GraphQuantum[10][10];
		GraphBounds bounds = g.getBounds();

		HashMap<Long, int[]> inverseVertexMap = new HashMap<Long, int[]>();

		for(Vertex v : g.getVertices().values()) {
			VertexCoords coords = v.getCoords();
			int row = (int)Math.floor(bounds.getWidth().floatValue()/coords.getX().floatValue()*resolution);
			int col = (int)Math.floor(bounds.getHeight().floatValue()/coords.getY().floatValue()*resolution);
			int[] mmcoords = new int[] {row, col};
			inverseVertexMap.put(v.getId(), mmcoords);
			if(matrix[row][col] == null)
				matrix[row][col] = new GraphQuantum(row, col);
			matrix[row][col].addElementsToVerticesAggregate(vertexAggregator.apply(v));			
		}

		if(edgeAggregator != null)
			for(Edge e : g.getEdgesSet()) {
				int[] tgtLoc = inverseVertexMap.get(e.getTarget());
				matrix[tgtLoc[0]][tgtLoc[1]].addElementsToEdgesAggregate(edgeAggregator.apply(e));
			}

		return matrix;
	}

	public void clearEdgeAggregator() {
		if(matrix != null)
			for(int i = 0; i < matrix.length; i++)				
				for(int t = 0; t < matrix[i].length; t++)
					if(matrix[i] != null && matrix[i][t] != null)
						matrix[i][t].clearEdgeAggregator();
	}

}
