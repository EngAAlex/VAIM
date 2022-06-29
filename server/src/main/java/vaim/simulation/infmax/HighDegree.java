package vaim.simulation.infmax;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import vaim.io.db.DBWizard;

public class HighDegree extends InfluenceMaximization {

	private final String query = "MATCH (n:Vertex)-[r:Edge]->() WHERE n.graph=? return n.id, Count(r) order by Count(r) desc limit ?";

	public HighDegree() {
	}
	
	@Override
	public Set<Long> getSeeds(Connection conn, int graphId, int noOfSeeds) throws SQLException{
		DBWizard dbW = DBWizard.getInstance();
		PreparedStatement ps = dbW.createStatement(conn, query);
		HashSet<Long> seeds = new HashSet<Long>();
		ps.setInt(1, graphId);
		ps.setInt(2, noOfSeeds);
		
		ResultSet rs = ps.executeQuery();
		while(rs.next())
			seeds.add(rs.getLong(1));
			
		return seeds;
	}

	@Override
	public String getDescription() {
		return "High Degree";
	}

	@Override
	public String getAcronym() {
		return "HIGHDEG";
	}
}
