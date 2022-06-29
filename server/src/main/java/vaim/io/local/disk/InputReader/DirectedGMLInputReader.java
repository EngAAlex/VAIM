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

package vaim.io.local.disk.InputReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import vaim.core.graph.Edge;
import vaim.core.graph.Graph;
import vaim.core.graph.Vertex;
import vaim.io.api.structures.VertexCoords;

public class DirectedGMLInputReader extends InputReader {
	
	public static Graph readInput(File f) throws Exception{
		HashMap<Long, Vertex> vertexMap = new HashMap<Long, Vertex>();
//		HashSet<DirectedEdge> edgeSet = new HashSet<DirectedEdge>();
		FileInputStream input = new FileInputStream(f);
		CharSequence fileContents = readFile(input);
		String regexRootForSections="\\s+\\[.*\\s+([^\\]]*\\s+)+]\\s";
		String regexRootForNodes="(id|x|y)\\s?-?[0-9]+(\\.[0-9]*)?";
		String regexRootForEdges="(source|target|value)\\s?[\"0-9.]+";
		String nodeRegex = "node"+regexRootForSections;
		String edgeRegex = "edge"+regexRootForSections;
		Pattern pNodeSection = Pattern.compile(nodeRegex);
		Pattern pEdgeSection = Pattern.compile(edgeRegex);
		Pattern pNode = Pattern.compile(regexRootForNodes);
		Pattern pEdge = Pattern.compile(regexRootForEdges);

		Matcher nodeGroups = pNodeSection.matcher(fileContents);
		Matcher edgeGroups = pEdgeSection.matcher(fileContents);
		int start=0;
		while(nodeGroups.find(start)){
			String section = nodeGroups.group();
			Matcher nodeMatcher = pNode.matcher(section);
			int substart=0;
			long id = -1;
			float x = 0.0f;
			float y = 0.0f;			
			while(nodeMatcher.find(substart)){
				String[] splits = nodeMatcher.group().split("\\s");
				switch(splits[0]){
					case "id": id = Long.parseLong(splits[1]);
							   break;
					case "x": x = Float.parseFloat(splits[1]); break;
					case "y": y = Float.parseFloat(splits[1]); break;										
				}
				substart = nodeMatcher.end();
			}
			vertexMap.put(id, new Vertex().id(id).coords(new VertexCoords(x, y)).degree(0));
			start=nodeGroups.end();
		}
		start=0;
		int edgeCounter = 0;
		Set<Edge> edgesSet = new HashSet<Edge>();
		
		while(edgeGroups.find(start)){
			String section = edgeGroups.group();
			Matcher edgeMatcher = pEdge.matcher(section);
			int substart=0;
			long source = -1;
			long target = -1;
			double weight = 1;
			while(edgeMatcher.find(substart)){
				String[] splits = edgeMatcher.group().split("\\s");
				switch(splits[0]){
				case "source": source = Long.parseLong(splits[1]); break;
				case "target": target = Long.parseLong(splits[1]); break;
				case "value": weight = Double.parseDouble(splits[1]); break;
				}
				substart = edgeMatcher.end();
			}
			Edge newEdge = new Edge().source(source).target(target).weight(weight);
			if(edgesSet.add(newEdge)){
				Vertex v = vertexMap.get(source);
				v.addNeighborsItem(newEdge);
				v.degree(v.getDegree() + 1);
				edgeCounter++;				
			}
			/*if(vertexMap.get(target).addEdge(newEdge));
				edgeCounter++;*/
			start=edgeGroups.end();			
		}
		
		input.close();
		
		System.out.println("Parsed " + f.getName() + "\nNodes: " + vertexMap.size() + "\nEdges: " + edgeCounter);
		
		return new Graph().name(f.getName()).vertices(vertexMap).edgesSet(edgesSet);
	}

	public static CharSequence readFile(FileInputStream input) throws IOException {
		FileChannel channel = input.getChannel();
		ByteBuffer bbuf = channel.map(FileChannel.MapMode.READ_ONLY, 0, (int)channel.size());
		CharBuffer cbuf = Charset.forName("8859_1").newDecoder().decode(bbuf);
		return cbuf;
	}

}
