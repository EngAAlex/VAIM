/**
 * Copyright © 2014-2016 Paolo Simonetto
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
package vaim.io.serialization.dot;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import vaim.core.graph.Edge;
import vaim.core.graph.Graph;
import vaim.core.graph.Vertex;

public class DotWriter {

    public DotWriter() {

    }

    /**
     * Writes the graph in dot format on the given file.
     *
     * @param graph the graph.
     * @param file the output file.
     */
    public void writeGraph(Graph graph, File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            List<String> lines = writeDotLines(graph);
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException ex) {
            System.err.println("The file " + file.getName() + "is not writable.");
        }
    }

    /**
     * Writes the attributes in the maps as dot lines.
     *
     * @return the dot lines.
     */
    public List<String> writeDotLines(Graph graph) {
        List<String> dotLines = new ArrayList<>();
        dotLines.add(writeOpeningLine());
        dotLines.add(writeGraphGlobalLine());
        dotLines.add(writeNodeGlobalLine());
        dotLines.add(writeEdgeGlobalLine());

        Collection<Vertex> nodes = graph.getVertices().values();
        for (Vertex v : nodes) {
            dotLines.add(writeNodeLine(v));
        }
        
        if(graph.getEdgesSet() == null)
	        for (Vertex v : nodes) {
	        	List<Edge> neighbors = v.getNeighbors();
	        	for(Edge n : neighbors)
	        		if(graph.getVertex(n.getTarget()) != null)
	        			dotLines.add(writeEdgeLine(n));
	        }
        else
        	for(Edge e : graph.getEdgesSet())
        		if(graph.getVertex(e.getSource()) != null && graph.getVertex(e.getTarget()) != null)
        			dotLines.add(writeEdgeLine(e));

        dotLines.add("}");
        return dotLines;
    }

    /**
     * Writes the opening line of a dot file.
     *
     * @return the opening line.
     */
    private String writeOpeningLine() {
        return "digraph {";
    }

    /**
     * Writes the line of the graph attributes.
     *
     * @return the graph attribute line.
     */
    private String writeGraphGlobalLine() {
        return "\tgraph [];";
    }

    /**
     * Writes the line of the default node attributes.
     *
     * @return the default node attribute line.
     */
    private String writeNodeGlobalLine() {
        return "\tnode [];";
    }

    /**
     * Writes the line of the default edge attributes.
     *
     * @return the default edge attribute line.
     */
    private String writeEdgeGlobalLine() {
        return "\tedge [];";

    }

    /**
     * Writes the line of a node.
     *
     * @return the node line.
     */
    private String writeNodeLine(Vertex v) {
        return "\t\"" + v.getId() + "\" [" + writeAttributes(v.drawingAttributes()) + "];";
    }

//    /**
//     * Writes the line of an edge.
//     *
//     * @return the edge line.
//     */
//    private String writeEdgeLine(Vertex v) {
//    	List<Long> neighbors = v.getNeighbors();    	
//        String edgeSource = v.getId().toString();
//        if(!neighbors.isEmpty())
//	    	for(Long n : neighbors) 
//	            return "\t\"" + edgeSource + "\" -> \""+ n.toString() + "\"[weight = 1];";	    	
//        else
//        	return "";
//    }
    	
    /**
     * Writes the line of an edge when an Edge object is supplied.
     *
     * @return the edge line.
     */
    private String writeEdgeLine(Edge e) {
    	return "\t\"" + e.getSource() + "\" -> \""+ e.getTarget() + "\"[weight = " + e.getWeight() + "];";
    }

    /**
     * Write a sequence of attributes in the dot format.
     *
     * @param attributes the attributes.
     * @return the attribute line.
     */
    private String writeAttributes(Map<String, String> attributes) {
        if (attributes == null || attributes.isEmpty()) {
            return " ";
        }
        String line = "";
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            line += entry.getKey() + "=\"" + entry.getValue() + "\", ";
        }
        return line.substring(0, line.length() - 2);
    }
}
