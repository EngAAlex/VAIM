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
package vaim.layout.sfdp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vaim.core.graph.Graph;
import vaim.core.graph.Vertex;
import vaim.io.api.structures.VertexCoords;
import vaim.io.serialization.dot.DotReader;
import vaim.io.serialization.dot.DotWriter;

/**
 * Executor for GraphViz's Scalable Force Directed Placement algorithm.
 */
public class SfdpExecutor {

	Logger log = LoggerFactory.getLogger(SfdpExecutor.class);

	private final static String COMMAND_LINE_DEFAULT = "wsl sfdp";	
	private final static String SFDP_PROPERTY = "sfdp.command";
	
	private String COMMAND_LINE;
//    private final String[] arguments;
    private final DotReader dotReader;
    private final DotWriter dotWriter;

    /**
     * Builds a sfdp executor.
     */
    
    public static class SfdpBuilder{
    	
    	public static SfdpExecutor BuildSfdpExecutor() {
    		return new SfdpExecutor();
    	}
    	
    	public static SfdpExecutor BuildSfdpExecutorReadPositionsOnly() {
    		return new SfdpExecutor(true);
    	}    	
    	
    }
    
    /**
     * Constructs a sfdp executor.
     *
     * @param Read positions only.
     */
    private SfdpExecutor(boolean positionsOnly) {
		Properties props = new Properties();
		try {
			props.load(getClass().getResourceAsStream("/application.properties"));
			COMMAND_LINE = props.getProperty(SFDP_PROPERTY);			
		}catch(Exception e) {
			log.error("Error while reading Properties file - reverting to " + COMMAND_LINE_DEFAULT);
			log.error(e.getMessage(), e);
			COMMAND_LINE = COMMAND_LINE_DEFAULT;
		}
        checkExecutable();
        this.dotReader = new DotReader(positionsOnly);
        this.dotWriter = new DotWriter();
    }
    
    /**
     * Constructs a sfdp executor.
     */
    private SfdpExecutor() {
        checkExecutable();
        this.dotReader = new DotReader();
        this.dotWriter = new DotWriter();
    }

    /**
     * Checks if the executable exists.
     */
    private void checkExecutable() {
        try {
            DefaultExecutor executor = new DefaultExecutor();
            executor.setStreamHandler(new PumpStreamHandler(new ByteArrayOutputStream()));
            executor.execute(CommandLine.parse(COMMAND_LINE + " -V"));
        } catch (IOException ex) {
            throw new IllegalStateException("sfdp executable has not been found.");
        }
    }

    /**
     * Runs the algorithm to compute the new positions for the given graph.
     *
     * @param graph the graph.
     */
    public void execute(Graph graph) {
        List<String> dotInput = dotWriter.writeDotLines(graph);
        List<String> dotOutput = run(dotInput);
        Graph generatedGraph = dotReader.parseFile(dotOutput);
        Map<Long, Vertex> originalVertices = graph.getVertices();
        for(Entry<Long, Vertex> e : generatedGraph.getVertices().entrySet()){        	
        	originalVertices.get(e.getKey()).setCoords(new VertexCoords(e.getValue().getCoords()));
        }
    }

    /**
     * Runs the algorithm.
     *
     * @param dotInput the dot input.
     * @return the dot output.
     */
    private List<String> run(List<String> dotInput) {
//        for (String argment : arguments) {
//            assert (argment.startsWith("-") && !argment.startsWith("-o") && !argment.startsWith("-O")) : "Arguments that control the input/ouput streams cannot be used here.";
//        }

        CommandLine cmdLine = new CommandLine(CommandLine.parse(COMMAND_LINE));
//        cmdLine.addArguments(arguments);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(listToString(dotInput).getBytes());
        ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            DefaultExecutor executor = new DefaultExecutor();
            executor.setStreamHandler(new PumpStreamHandler(outputStream, errorStream, inputStream));
            //System.out.println("Executing " + COMMAND_LINE);
            executor.execute(cmdLine);
        } catch (IOException ex) {
            log.error("ERROR: " + errorStream.toString() + "\n");
            log.error("OUTPUT: " + outputStream.toString() + "\n");
            log.error(ex.getMessage(), ex);            
            throw new IllegalStateException("Error while executing sfdp.");
        }

        return stringToList(outputStream.toString());
    }

    /**
     * Converts a list of lines into a multi-line string.
     *
     * @param list the list of lines.
     * @return the multi-line string.
     */
    private static String listToString(List<String> list) {
        StringBuilder builder = new StringBuilder();
        for (String string : list) {
            builder.append(string);
            builder.append("\n");
        }
        return builder.toString();
    }

    /**
     * Converts a multi-line string into a list of lines.
     *
     * @param string the multi-line string.
     * @return the list of lines.
     */
    private static List<String> stringToList(String string) {
        return Arrays.asList(string.split("\n"));
    }

}
