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

package vaim.io.local.disk;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;

import vaim.core.graph.Edge;
import vaim.core.graph.Graph;
import vaim.core.graph.Vertex;
import vaim.io.api.structures.GraphBounds;
import vaim.io.db.DBWizard;
import vaim.io.db.DBWizard.QUERY_TYPE;
import vaim.io.local.disk.InputReader.InputReader;
import vaim.layout.MatrixQuantizer;
import vaim.layout.sfdp.SfdpExecutor;
import vaim.layout.sfdp.SfdpExecutor.SfdpBuilder;

public class GraphReader {

	public static final String[] loadSymbols = {"\\", "|", "/"};
	private static final int TEMP_COUNTER_LIMIT = 2;

	public static boolean uploadGraphToDatabase(File f, Class<? extends InputReader> input) {
		return uploadGraphToDatabase(f, input, false);
	}

	public static boolean uploadGraphToDatabase(File f, Class<? extends InputReader> input, boolean redraw){
		boolean result = true;
		DBWizard dbW = DBWizard.getInstance();
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			Graph g = (Graph) input.getMethod("readInput", File.class).invoke(input, f);

			if(redraw) {
				System.out.print("Computing Layout... ");
				SfdpExecutor exec = SfdpBuilder.BuildSfdpExecutorReadPositionsOnly();
				exec.execute(g);
				System.out.println("done");
			}

			conn = dbW.connect();
			if(conn == null) {
				System.out.println("Connection failed!");
				return false;
			}

			int noOfNodes = g.getVertices().size();
			int noOfEdges = g.getEdgesSet().size();

			//FIRST CREATE GRAPH NODE
			ps = dbW.createStatement(conn, QUERY_TYPE.INSERT_GRAPH_NODE);
			ps.setString(1, FilenameUtils.removeExtension(f.getName())); //SET NAME
			ps.setLong(2, noOfNodes); //NO OF NODES
			ps.setLong(3, noOfEdges); //NO OF EDGES

			ResultSet rs = ps.executeQuery();
			rs.next();
			int graphId = rs.getInt(1);
			ps.close();

			//CREATE GRAPH VERTICES

			System.out.println("Loading nodes");

			float minX = Float.MAX_VALUE, minY = Float.MAX_VALUE, maxX = Float.MIN_VALUE, maxY = Float.MIN_VALUE;

			int counter = 0;
			for(Vertex v : g.getVertices().values()){
				float[] coords = {v.getCoords().getX().floatValue(), v.getCoords().getY().floatValue()};
				minX = Math.min(minX, coords[0]);
				minY = Math.min(minY, coords[1]);
				maxX = Math.max(maxX, coords[0]);
				maxY = Math.max(maxY, coords[1]);
			}

			for(Vertex v : g.getVertices().values()){
				float[] coords = {v.getCoords().getX().floatValue(), v.getCoords().getY().floatValue()};
				ps = dbW.createStatement(conn,QUERY_TYPE.INSERT_GRAPH_VERTEX);
				ps.setLong(1, v.getId());	
				ps.setInt(2, graphId);		
				ps.setFloat(3, coords[0] - minX);
				ps.setFloat(4, coords[1] - minY);
				ps.setLong(5, v.getDegree());

				v.getCoords().setX(v.getCoords().getX().subtract(new BigDecimal(minX)));
				v.getCoords().setY(v.getCoords().getY().subtract(new BigDecimal(minY)));

				ps.executeUpdate();
				ps.close();
				counter++;
				if(counter%20 == 0) 
					outputCounter(counter, noOfNodes);

			}

			System.out.println("");

			//BUILDING RESOLUTIONS
			HashMap<Integer, MatrixQuantizer> resolutionMaps = new HashMap<Integer, MatrixQuantizer>();

			int totalCells = 0;

			GraphBounds bounds = new GraphBounds()
					.width(new BigDecimal(maxX - minX))
					.height(new BigDecimal(maxY - minY))
					.minX(BigDecimal.ZERO)//new BigDecimal(rs.getDouble(6)))
					.maxX(new BigDecimal(maxX - minX))
					.minY(BigDecimal.ZERO)//new BigDecimal(rs.getDouble(8)))
					.maxY(new BigDecimal(maxY - minY));	

			for(int current : MatrixQuantizer.RESOLUTIONS) {
				resolutionMaps.put(current, new MatrixQuantizer(current, bounds));
				totalCells += Math.pow(current, 2);
			}

			counter = 0;

			System.out.println("Computing Matrices");
			for(Vertex v : g.getVertices().values()) {
				counter++;
				for(MatrixQuantizer mq : resolutionMaps.values()) {
					mq.processNewVertex(v);
				}
				if(counter%20 == 0)
					outputCounter(counter, noOfNodes*resolutionMaps.size());
			}

			System.out.println("");

			counter = 0;

			System.out.println("Storing Matrices");					

			for(Entry<Integer, MatrixQuantizer> entry : resolutionMaps.entrySet()) {
				int resolution = entry.getKey();
				MatrixQuantizer mq = entry.getValue();

				for(int i = 0; i < resolution; i++) 
					for(int t = 0; t < resolution; t++) {
						Set<Long> vInSector = mq.getVerticesInSector(i, t);

						String list = "";

						if(vInSector.size() > 0) {						
							for(Long l : vInSector)
								list += l+",";
							list = list.substring(0, list.length()-1);

							ps = dbW.createStatement(conn, DBWizard.getStringFromQuery(QUERY_TYPE.INSERT_NEW_MATRIX_CELL_EDGE_GROUP).replace(DBWizard.LIST_PLACEHOLDER, list));

							ps.setInt(1, graphId);
							ps.setInt(2, graphId);
							ps.setInt(3, resolution);
							ps.setInt(4, i);
							ps.setInt(5, t);
							ps.executeUpdate();

							ps.close();		
						}

						//						for(long l : vInSector) {
						//							ps = dbW.createStatement(conn, QUERY_TYPE.INSERT_NEW_MATRIX_CELL_EDGE);
						//							ps.setInt(1, graphId);
						//							ps.setInt(2, graphId);
						//							ps.setLong(3, l);
						//							ps.setInt(4, resolution);
						//							ps.setInt(5, i);
						//							ps.setInt(6, t);
						//							ps.executeUpdate();
						//							
						//							ps.close();
						//						}

						counter++;
						if(counter%20 == 0)
							outputCounter(counter, totalCells);
					}									
			}

			System.out.println("");

			//REGISTERING EDGES

			Iterator<Edge> ei = g.getEdgesSet().iterator();
			counter = 0;
			int tempCounter = 0;
			System.out.println("Loading edges");

			//HashSet<String> batch = new HashSet<String>();
			while(counter < g.getEdgesSet().size()) {
				String batch = "";				
				while(tempCounter < TEMP_COUNTER_LIMIT && ei.hasNext()) {
					Edge e = ei.next();

					batch += DBWizard.edgeInfoIntoBatch(e.getSource(), e.getTarget(), graphId, e.getWeight());
					tempCounter++;
					//				Edge e = ei.next();
					//				ps = dbW.createStatement(conn, QUERY_TYPE.INSERT_GRAPH_EDGE);			
					//				ps.setLong(2, e.getSource());
					//				ps.setInt(1, graphId);
					//				ps.setLong(4, e.getTarget());
					//				ps.setInt(3,  graphId);
					//				ps.setDouble(5, e.getWeight());
					//
					//				ps.executeUpdate();
					//				ps.close();
					if(tempCounter+1 == TEMP_COUNTER_LIMIT && ei.hasNext())
						batch += ",";
				}

				ps = dbW.createStatement(conn, DBWizard.getStringFromQuery(QUERY_TYPE.INSERT_GRAPH_EDGE_BATCH).replace(DBWizard.BATCH_PLACEHOLDER, batch));
				ps.executeUpdate();
				ps.close();
				
				counter += tempCounter;
				tempCounter = 0;
				outputCounter(counter, noOfEdges);

			}

			System.out.println("");

			ps = dbW.createStatement(conn, QUERY_TYPE.UPDATE_GRAPH_NODE_BOUNDS);
			ps.setInt(1, graphId);
			ps.setFloat(2, 0);
			ps.setFloat(3, 0);
			ps.setFloat(4, bounds.getMaxX().floatValue());
			ps.setFloat(5, bounds.getMaxY().floatValue());

			ps.executeUpdate();

			ps.close();

			System.out.println("Begin commit");

			dbW.commit(conn);
			conn.close();
		}catch(Exception e) {
			result = false;
			System.out.println("Error while parsing file " + f + " - loading failed. Check Exception trace");
			e.printStackTrace();
			dbW.fail(conn, e);
		}
		return result;

	}

	private static void outputCounter(int counter, int total) {
		if(counter%((int)(total*0.05)) == 0) {
			float completion = counter/(float)total;
			int symbols = (int)(completion*20);
			String symbolString = "";
			int h;
			for(h=0; h<symbols; h++) 
				symbolString += "#";
			for(int l=h; l<20; l++)
				symbolString += " ";
			System.out.print("[" + symbolString +"] " + (int)(completion*100) + "% " + loadSymbols[counter%loadSymbols.length] + "\r");
		}
	}

}
