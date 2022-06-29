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

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import vaim.core.graph.Edge;
import vaim.core.graph.Graph;
import vaim.core.graph.Vertex;
import vaim.io.api.structures.VertexCoords;
import vaim.io.serialization.dot.DotTools.DotAttributes;
import vaim.io.serialization.dot.DotTools.DotLineType;
import vaim.io.serialization.dot.ParserTools.EscapedString;


/**
 * Reads and writes graphs in dot format.
 */
public class DotReader {

    private final Map<String, DotAttributes> nodeAttributes = new HashMap<>();
    private final List<DotAttributes> edgeAttributes = new ArrayList<>();
	
    private final boolean positionsOnly;
    
    public DotReader(boolean positionsOnly) {
    	this.positionsOnly = positionsOnly;
    }
    
	/**
	 * Constructs a DotReader.
	 */
	public DotReader() {
		positionsOnly = false;
	}

	/**
	 * Parses a file and generates a graph.
	 *
	 * @param file the input dot file.
	 * @return the generated graph.
	 */
	public Graph parseFile(File file) {
		
		nodeAttributes.clear();
		edgeAttributes.clear();
		
		List<String> lines = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
		} catch (IOException e) {
			System.err.println("The file " + file.getName() + "is not readable.");
		}
		return parseFile(lines);
	}

	/**
	 * Parses a collection of dot lines and generates a graph.
	 *
	 * @param lines the lines in dot format.
	 * @return the generated graph.
	 */
	public Graph parseFile(List<String> lines) {

		nodeAttributes.clear();
		edgeAttributes.clear();
		
		List<String> combinedLines = combineLines(lines);
		for (String line : combinedLines) {
			EscapedString escapedLine = escapeLine(line);
			parseLine(escapedLine);
		}

		return generateGraph();
	}

	/**
	 * Combines the input lines so that the graph has a single line for global
	 * properties, nodes and edges.
	 *
	 * @param lines the input lines.
	 * @return the combined input lines.
	 */
	private static List<String> combineLines(List<String> lines) {
		List<String> combinedLines = new ArrayList<>();
		String currentLine = "";
		for (String line : lines) {
			line = line.trim();

			if (!currentLine.isEmpty()) {
				currentLine += " ";
			}
			currentLine += line;

			if (line.endsWith(";") || line.endsWith("{") || line.endsWith("}")) {
				combinedLines.add(currentLine);
				currentLine = "";
			}
		}
		return combinedLines;
	}

	/**
	 * Builds an EscapedString using dot conventions. Everything between double
	 * quotes is considered as a single entity, and is not parsed according to
	 * the dot syntax. These entities might contain a double quotes, but only if
	 * escaped with backslash.
	 *
	 * @param line the input line.
	 * @return its escaped string.
	 */
	private static EscapedString escapeLine(String line) {
		// Regular expression with negative lookbehind to ignore escaped quotes.
		String notEscapedQuotes = "(?<!\\\\)\"";
		return new EscapedString(line, notEscapedQuotes);
	}

	/**
	 * Parses a single dot line, provided as an escaped string.
	 *
	 * @param eString a dot line where the quoted portions have been substituted
	 * by place holders.
	 */
	private void parseLine(EscapedString eString) {
		List<String> tokens = splitLine(eString);
		switch (tokens.size()) {
		case 0:
			return;

		case 1:
		case 2:
			switch (DotLineType.detect(tokens.get(0))) {
			case opening:
//				parseOpeningLine(graphAttributes, eString, tokens);
				break;

			case graphGlobal:
//				parseGlobalLine(graphAttributes, eString, tokens);
				break;

			case nodeGlobal:
//				parseGlobalLine(defaultNodeAttributes, eString, tokens);
				break;

			case edgeGlobal:
//				parseGlobalLine(defaultEdgeAttributes, eString, tokens);
				break;

			case node:
				parseNodeLine(nodeAttributes, eString, tokens);
				break;

			case edge:
				if(!positionsOnly)
					parseEdgeLine(edgeAttributes, eString, tokens);
				break;
			}
			break;

		default:
			throw new UnsupportedOperationException("The line:\n" + eString.original + "\nhas an unsupported syntax.");
		}
	}

	/**
	 * Splits an input line into the heading and the eventual attribute string.
	 *
	 * @param line the input line.
	 * @return the line components.
	 */
	private static List<String> splitLine(EscapedString line) {
		String[] tokens = line.withSubstitutions.split("[\\[\\];]+");
		List<String> answer = new ArrayList<>();
		for (String token : tokens) {
			String trimmedToken = token.trim();
			if (!trimmedToken.isEmpty()) {
				answer.add(trimmedToken);
			}
		}
		return answer;
	}

	/**
	 * Parses the opening line of a dot file.
	 *
	 * @param graphAttributes the graph attributes.
	 * @param line the line.
	 * @param tokens the line components.
	 */
//	private static void parseOpeningLine(DotAttributes graphAttributes, EscapedString line, List<String> tokens) {
//		String[] headerTokens = tokens.get(0).split("[ \t{}]+");
//		for (String headerToken : headerTokens) {
//			switch (headerToken) {
//			case "strict":
//				graphAttributes.put(DotTools.strictAttr, "true");
//				break;
//			case "digraph":
//				graphAttributes.put(DotTools.directedAttr, "true");
//				break;
//			case "graph":
//				graphAttributes.put(DotTools.directedAttr, "false");
//				break;
//			default:
//				graphAttributes.put(DotTools.graphNameAttr, line.revertSubst(headerToken));
//			}
//		}
//	}

	/**
	 * Parses a line with global graph, nodes or edges attributes.
	 *
	 * @param graphAttributes the global attributes.
	 * @param line the line.
	 * @param tokens the line components.
	 */
//	private static void parseGlobalLine(DotAttributes attributes, EscapedString line, List<String> tokens) {
//		if (tokens.size() > 1) {
//			attributes.putAll(extractAttributes(line, tokens.get(1)));
//		}
//	}

	/**
	 * Parses a node line.
	 *
	 * @param nodeAttributes the node attributes.
	 * @param line the line.
	 * @param tokens the line components.
	 */
	private static void parseNodeLine(Map<String, DotAttributes> nodeAttributes, EscapedString line, List<String> tokens) {
		if (tokens.get(0).trim().split(" ").length > 1) {
			throw new UnsupportedOperationException("The line:\n" + line.original + "\nhas an unsupported syntax.");
		}

		String nodeId = line.revertSubst(tokens.get(0).trim());
		if (!nodeAttributes.containsKey(nodeId)) {
			nodeAttributes.put(nodeId, new DotAttributes());
		}
		if (tokens.size() > 1) {
			DotAttributes attributes = nodeAttributes.get(nodeId);
			attributes.putAll(extractAttributes(line, tokens.get(1)));
		}
	}

	/**
	 * Parses an edge line. Supports the definition of multiple edges in the
	 * same line, as allowed by the dot format. Example a -- b -- c.
	 *
	 * @param edgeAttributes the edge attributes.
	 * @param line the line.
	 * @param tokens the line components.
	 */
	private static void parseEdgeLine(List<DotAttributes> edgeAttributes, EscapedString line, List<String> tokens) {
		String[] multiEdgeDefinition = tokens.get(0).trim().split(String.format(ParserTools.SPLIT_KEEP_DELIMITERS, "(--|->)"));
		if (multiEdgeDefinition.length % 2 != 1) {
			throw new UnsupportedOperationException("The line:\n" + line.original + "\nhas an unsupported syntax.");
		}
		DotAttributes multiEdgeAttributes = new DotAttributes();
		if (tokens.size() > 1) {
			multiEdgeAttributes.putAll(extractAttributes(line, tokens.get(1)));
		}
		for (int i = 0; i < multiEdgeDefinition.length - 2; i = i + 2) {
			String source = line.revertSubst(multiEdgeDefinition[i]).trim();
			String target = line.revertSubst(multiEdgeDefinition[i + 2]).trim();
			String edge = line.revertSubst(multiEdgeDefinition[i + 1]);
			DotAttributes currentEdgeAttributes = new DotAttributes();
			currentEdgeAttributes.put(DotTools.edgeSourceAttr, source);
			currentEdgeAttributes.put(DotTools.edgeTargetAttr, target);
			currentEdgeAttributes.put(DotTools.directedAttr, edge.equals("->") ? "true" : "false");
			currentEdgeAttributes.putAll(multiEdgeAttributes);
			edgeAttributes.add(currentEdgeAttributes);
		}
	}

	/**
	 * Extracts the dot attributes from a line.
	 *
	 * @param line the line.
	 * @param attributeToken the attribute string of that line.
	 * @return the collection of dot attributes.
	 */
	private static DotAttributes extractAttributes(EscapedString line, String attributeToken) {
		DotAttributes attributes = new DotAttributes();
		String[] tokens = attributeToken.split("[,= \t]+");
		if (tokens.length % 2 != 0) {
			throw new UnsupportedOperationException("The attribute line:\n" + attributeToken + "\nhas an unsupported syntax.");
		}
		for (int i = 0; i < tokens.length; i++) {
			String attributeName = line.revertSubst(tokens[i]);
			String attributeValue = line.revertSubst(tokens[++i]);
			attributes.put(attributeName, attributeValue);
		}
		return attributes;
	}

	/**
	 * Generates the graph specified by the dot file.
	 *
	 * @return the generated graph.
	 */
	private Graph generateGraph() {
		Graph graph = new Graph();

		for (Entry<String, DotAttributes> entry : nodeAttributes.entrySet()) {
			Vertex newV = new Vertex().id(Long.valueOf(entry.getKey())) ;
			String[] coords = entry.getValue().get("pos").split(",");
			newV.setCoords(new VertexCoords(Double.parseDouble(coords[0]), Double.parseDouble(coords[1])));
			graph.putVerticesItem(newV.getId(), newV);
		}

		Map<Long, Vertex> vertices = graph.getVertices();
		
		for (DotAttributes entry : edgeAttributes) {
			Vertex source = vertices.get(Long.valueOf(entry.get(DotTools.edgeSourceAttr)));
			Vertex target = vertices.get(Long.valueOf(entry.get(DotTools.edgeTargetAttr))); 
			Double weight = entry.get(DotTools.edgeWeightAttr) == null ? 1.0d : Double.valueOf(entry.get(DotTools.edgeWeightAttr));			
			source.addNeighborsItem(new Edge().source(source.getId()).target(target.getId()).weight(weight));
			//target.addNeighborsItem(source.getId());
		}

		return graph;
	}

}