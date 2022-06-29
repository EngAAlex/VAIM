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

package vaim.simulation;

import java.util.ArrayList;

public class Common {

	public static class SimulationStatistics {
		
		public SimulationStatistics(int simulationId, int steps) {
			this.simulationId = simulationId;
			this.steps = new ArrayList<StepStatistics>();
		}
		
		public void addStep(long maxActivated, long minActivated, double median, double firstQuartile, double secondQuartile) {
			steps.add(new StepStatistics(maxActivated, minActivated, firstQuartile, median, secondQuartile));
		}

		public static class StepStatistics{
			
			long maxActivated;
			public long getMaxActivated() {
				return maxActivated;
			}

			public long getMinActivated() {
				return minActivated;
			}

			public double getMedian() {
				return median;
			}

			public double getFirstQuartile() {
				return firstQuartile;
			}

			public double getThirdQuartile() {
				return thirdQuartile;
			}

			long minActivated;
			double median;
			double firstQuartile;
			double thirdQuartile;
			
			public StepStatistics(long maxActivated, long minActivated, double firstQuartile, double median, 
					double thirdQuartile) {
				super();
				this.maxActivated = maxActivated;
				this.minActivated = minActivated;
				this.median = median;
				this.firstQuartile = firstQuartile;
				this.thirdQuartile = thirdQuartile;
			}
								
		}
		
		int simulationId;
		long avgActivated;
		float stdDev;

		public float getStdDev() {
			return stdDev;
		}

		public void setStdDev(float stdDev) {
			this.stdDev = stdDev;
		}
		
		ArrayList<StepStatistics> steps;

		public ArrayList<StepStatistics> getStepsStatistics() {
			return steps;
		}
		
		public int getSimulationId() {
			return simulationId;
		}

		public long getAvgActivated() {
			return avgActivated;
		}	
		
		public void setAvgActivated(long avgActivated) {
			this.avgActivated = avgActivated;
		}
					
	}
	
	public static class Message<T>{
		
		long sender;
		long target;
		T payload;
		
		public Message(long sender, long target, T payload) {
			super();
			this.sender = sender;
			this.target = target;
			this.payload = payload;
		}

		public Message<T> copyPayload() {
			return new Message<T>(-1, -1, getPayload());
		}
		
		public long getSender() {
			return sender;
		}

		public long getTarget() {
			return target;
		}

		public T getPayload() {
			return payload;
		}

		public void setSender(int sender) {
			this.sender = sender;
		}

		public void setTarget(int target) {
			this.target = target;
		}
			
	}
	
	public static class SimulationStatus{
		/* CURRENT RUN DATA */
		
		/*File statistics;
		PrintWriter pw;*/
		
		/* GLOBAL */
		int simulationID;
		
		int run;
		long neuterVertices;
		long activeVertices = 0;
		long deadVertices = 0;
		long sentMessages = 0;
		long step = 1;
		
		/* LAST STEP */
		long lastActiveVertices = 0;
		long lastDeadVertices = 0;
		long lastSentMessages = 0;

		private int graphId;
		
		public SimulationStatus(int id, int steps, int graphId) {
			this.simulationID = id;
			this.step = steps;
			this.graphId = graphId;
		}
		
		public SimulationStatus(long neuterVertices, int id, int run, int graphId) {
			this.simulationID = id;
			this.run = run;
			this.neuterVertices = neuterVertices;	
			this.graphId = graphId;

			/*this.statistics = statistics;
			pw = new PrintWriter(statistics);*/
		}
		
		public int getSimulationId() {
			return simulationID;
		}
		
		public int getGraphId() {
			return graphId;
		}

		public long getNeuterVertices() {
			return neuterVertices;
		}

		public long getActiveVertices() {
			return activeVertices;
		}

		public long getDeadVertices() {
			return deadVertices;
		}

		public long getSentMessages() {
			return sentMessages;
		}

		public long getLastActiveVertices() {
			return lastActiveVertices;
		}

		public long getLastDeadVertices() {
			return lastDeadVertices;
		}

		public long getLastSentMessages() {
			return lastSentMessages;
		}

		public void closeSimulation() {
			//pw.close();
		}

		public void updateSimulation(long newActiveVertices, long newDeadVertices, long  newSentMessages) {
			activeVertices += newActiveVertices;
			deadVertices += newDeadVertices;
			neuterVertices -= (newActiveVertices + newDeadVertices);
			sentMessages += newSentMessages;
			
			lastActiveVertices = newActiveVertices;
			lastDeadVertices = newDeadVertices;
			lastSentMessages = newSentMessages;
			//stepForward();
		}
		
		public void stepForward() {
			step++;
			//writeStepStatistics();
		}
		
		public long getStep() {
			return step;
		}
		
		
		public int getRun() {
			return run;
		}

		/*public void writeSimulationStatusHeader() {
			pw.println("Run #; Step #; Active Vertices Delta; Total Active; Dead Vertices Delta; Total Dead; Neuter Delta; Total Neuter; Sent Messages; Total Sent");
		}
		
		private void writeStepStatistics(){
			pw.println(run + ";" + step + ";" + lastActiveVertices + ";" + activeVertices + ";" + lastDeadVertices + ";" + deadVertices + ";" + (lastActiveVertices + lastDeadVertices) + ";" + neuterVertices + lastSentMessages + ";" + sentMessages);
		}*/
	}
	
	public static class StateChange{
		
		long id;
		long step;
//		long seed;
		short newState;		
		
		public StateChange(long id, long step, short newState) {
			super();
			this.id = id;
			this.step = step;
//			this.seed = seed;
			this.newState = newState;
		}
		
		public long getSource() {
			return id;
		}
		
//		public long getSeed() {
//			return seed;
//		}

		public long getStep() {
			return step;
		}

		public short getNewState() {
			return newState;
		}
		
	}
	
}
