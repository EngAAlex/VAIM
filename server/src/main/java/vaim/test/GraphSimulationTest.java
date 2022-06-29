package vaim.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.common.collect.Lists;

import vaim.simulation.DiffusionSimulation;
import vaim.simulation.DiffusionSimulation.RunnableSimulationWithRequestedNumberOfSeeds;
import vaim.simulation.DiffusionSimulation.RunnableSimulationWithSeeds;
import vaim.simulation.infmax.InfluenceMaximization;
import vaim.simulation.infmax.RandomSelection;
import vaim.simulation.interpolation.CompressActivationToEdgeWeight;
import vaim.simulation.interpolation.InterpolateWithUncertainty;
import vaim.simulation.interpolation.NaiveInterpolation;
import vaim.simulation.interpolation.SelectByActivationProbability;
import vaim.simulation.models.DiffusionModel;
import vaim.simulation.models.IndependentCascadeModel;

public class GraphSimulationTest {

	public GraphSimulationTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		try {
			//long graphId = Long.parseLong(args[0]);
			DiffusionModel diffStrat;
			try {
				diffStrat = (DiffusionModel) Class.forName("vaim.simulation.strategies." + args[1]).getDeclaredConstructor().newInstance();
			} catch (ClassNotFoundException cns){
				diffStrat = new IndependentCascadeModel();
			}
			//DiffusionSimulation ds = new DiffusionSimulation(Integer.parseInt(args[0]), diffStrat, 5, 150, args[2]);

			
			//			ds.setInterpolation(new NaiveInterpolation(""));
			//			ds.setInterpolation(new CompressActivationToEdgeWeight());
			if(args.length <= 4) {
				int noOfSeeds = 0;
				try {
					noOfSeeds = Integer.parseInt(args[3]);
				} catch (IndexOutOfBoundsException ie) {
					noOfSeeds = 50;
				}
				InfluenceMaximization im = new RandomSelection();
				RunnableSimulationWithRequestedNumberOfSeeds ds = new RunnableSimulationWithRequestedNumberOfSeeds(Integer.parseInt(args[0]), diffStrat, im, noOfSeeds);
				ds.setInterpolation(new InterpolateWithUncertainty());			
				ds.run(); 
			}else if(args[3].equals("-f")) { //MANUALLY INDICATES WHICH VERTICES TO REMOVE AND TO ADD USING TWO FILES 
				File seedsFile = new File(args[4]);
				JSONParser jp = new JSONParser();
				JSONObject obj = (JSONObject) jp.parse(new FileReader(seedsFile));
				//JSONArray obja = (JSONArray) obj. get("400");
				JSONArray obja = (JSONArray)(obj.values().iterator().next());
				String[] larray = (String[]) ((JSONArray) obja.get(2)).toArray(new String[0]);
				//				Long[] larray = (Long[]) ((JSONArray) obja.get(2)).toArray(new Long[0]);
				//				HashSet<Long> seeds = new HashSet<Long>(Arrays.asList(larray));
				HashSet<Long> seeds = new HashSet<Long>();
				System.out.println("Found " + larray.length + " seeds");
				for(String l : larray) {
					seeds.add(Long.parseLong(l));
				}

				if(args.length > 5 && args[5].equals("-a")) {
					File newSeedsFile = new File(args[6]);
					BufferedReader bfread = new BufferedReader(new FileReader(newSeedsFile));
					String totalToAdd = "";
					String newLine = bfread.readLine();					
					while(newLine != null) {
						totalToAdd += newLine;
						newLine = bfread.readLine();
					}
					bfread.close();
					String[] seedsToAdd = totalToAdd.split(",");
					System.out.println("Adding " + seedsToAdd.length + "seeds to seed set");
					for(String s : seedsToAdd) {
						seeds.add(Long.parseLong(s));
					}
				}

				if(args.length > 7 && args[7].equals("-r")) {
					File rmSeedsFile = new File(args[8]);
					BufferedReader bfread = new BufferedReader(new FileReader(rmSeedsFile));
					String totalToRemove = "";
					String newLine = bfread.readLine();					
					while(newLine != null) {
						totalToRemove += newLine;
						newLine = bfread.readLine();
					}
					bfread.close();
					String[] seedsToRemove = totalToRemove.split(",");
					System.out.println("Removing " + seedsToRemove.length + "seeds to seed set");					
					for(String s : seedsToRemove) {
						if(seeds.contains(Long.parseLong(s))){
							seeds.remove(Long.parseLong(s));
						}
					}
				}

				System.out.println("Final seed set size " + seeds.size());

				InfluenceMaximization im = new RandomSelection();
				RunnableSimulationWithSeeds ds = new RunnableSimulationWithSeeds(Integer.parseInt(args[0]), diffStrat, seeds);
				ds.setInterpolation(new InterpolateWithUncertainty());							
				ds.run();
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		System.exit(0);
	}

}
