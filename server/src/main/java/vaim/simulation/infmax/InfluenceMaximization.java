package vaim.simulation.infmax;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import vaim.io.api.structures.Maximization;

public abstract class InfluenceMaximization {

	public static Set<Maximization> getAvailableMethods() {
		Set<Maximization> s = new HashSet<Maximization>();
		
		s.add(new Maximization("Random Selection", "RandomSelection"));
		s.add(new Maximization("High Degree", "HighDegree"));		
		s.add(new Maximization("Single Discount", "SingleDiscount"));

		return s;
	}
	
	public abstract String getDescription();
	
	public abstract String getAcronym();
	
	public abstract Set<Long> getSeeds(Connection conn, int graphId, int noOfSeeds) throws SQLException;
	
}
