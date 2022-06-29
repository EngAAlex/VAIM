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

package vaim.io.local.disk.OutputWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Iterator;

import vaim.core.graph.Edge;
import vaim.core.graph.Graph;
import vaim.core.graph.Vertex;

public class GMLOutputWriter {

	public static final String INDENT = "\t";

	public static void writeOutput(File f, Graph graph) {
		PrintWriter pw;
		try {
			pw = new PrintWriter(f);
			short indentLevel = 0;
			pw.println(indentString("graph [", indentLevel));
			indentLevel++;
			pw.println(indentString("directed 0", indentLevel));
			writeVerticesOnPrintWriter(graph, pw, indentLevel);
			writeEdgesOnPrintWriter(graph, pw, indentLevel);
			indentLevel--;
			pw.println(indentString("]", indentLevel));
			pw.close();			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	protected static void writeVerticesOnPrintWriter(Graph graph, PrintWriter pw, short indentLevel){
		Iterator<Vertex> itN = graph.getVertices().values().iterator();

		while(itN.hasNext()){
			Vertex current = itN.next();
			Long id = current.getId();
			//			String label = graph.getVertex(id).getLabel().equals("") ? ""+id : graph.getVertex(id).getLabel();
			pw.println(indentString("node [", indentLevel));
			indentLevel++;
			double[] coords = {current.getCoords().getX().doubleValue(), current.getCoords().getY().doubleValue()};
			pw.println(indentString("id " + id, indentLevel));
			//			pw.println(indentString("label \"" + label +"\"", indentLevel));			
			pw.println(indentString("graphics [ ", indentLevel));	
			indentLevel++;
			pw.println(indentString("x " + coords[0], indentLevel));
			pw.println(indentString("y " + coords[1], indentLevel));
			pw.println(indentString("w 5", indentLevel));
			pw.println(indentString("h 5", indentLevel));
			indentLevel--;
			pw.println(indentString("]", indentLevel));
			indentLevel--;
			pw.println(indentString("]", indentLevel));
		}
	}

	protected static void writeEdgesOnPrintWriter(Graph graph, PrintWriter pw, short indentLevel){
		Iterator<Edge> edges = graph.getEdgesSet().iterator();
		while(edges.hasNext()){
			Edge currentEdge = edges.next();
			pw.println(indentString("edge [", indentLevel));
			indentLevel++;
			pw.println(indentString("source " + currentEdge.getSource(), indentLevel));
			pw.println(indentString("target " + currentEdge.getTarget(), indentLevel));
			pw.println(indentString("graphics [", indentLevel));
			//			indentLevel++;
			//			pw.println(indentString("type \"line\"", indentLevel));
			//			pw.println(indentString("width " + (currentEdge.getWeight() == -1 ? 1 : currentEdge.getWeight() + 1), indentLevel));		
			indentLevel--;
			pw.println(indentString("]", indentLevel));
			indentLevel--;
			pw.println(indentString("]", indentLevel));
		}
	}

	protected static String indentString(String in, short indentLevel){
		String result = "";
		for(int i=0; i<indentLevel; i++)
			result += INDENT;
		return result + in;
	}

}


