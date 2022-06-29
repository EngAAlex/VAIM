package vaim.simulation.infmax;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;

import vaim.core.graph.Edge;
import vaim.core.graph.Graph;
import vaim.core.graph.Vertex;
import vaim.io.db.DBWizard;

public class SingleDiscount extends InfluenceMaximization {

	
	private final String FIND_MAX = "match (n:Vertex)-[r:Edge]-(m) where n.graph=? and NOT n.id IN [" + DBWizard.LIST_PLACEHOLDER + "] return n.id, count(r) as deg order by deg desc limit 1";
	private final String CHECK_MAX = "match (n:Vertex)-[r:Edge]-(m) where n.graph=? and NOT n.id IN [" + DBWizard.LIST_PLACEHOLDER + "] \n"
			+ "with n as n, count(r) as deg \n"
			+ "where deg > ? \n"
			+ "return n.id, deg order by deg desc limit 1";

	public SingleDiscount() {
	}

	@Override
	public Set<Long> getSeeds(Connection conn, int graphId, int noOfSeeds) throws SQLException{
		DBWizard dbW = DBWizard.getInstance();
		HashSet<Long> seeds = new HashSet<Long>();

		//HashMap<Long, List<String>> g = new HashMap<Long, List<String>>(); // TEMP STRUCTURE TO KEEP DATA
		String selectedList = "";
		PreparedStatement ps;

		while(seeds.size() < noOfSeeds) {
			ps = dbW.createStatement(conn, FIND_MAX.replace(DBWizard.LIST_PLACEHOLDER, selectedList));
			ps.setInt(1, graphId);			
			boolean accept = false;
			ResultSet rs = ps.executeQuery();

			if(rs.next()) {
				long id = rs.getLong(1);
				String neighs = rs.getString(2);
				rs.close();
				ps.close();
				if(seeds.size() == 0) {
					accept=true;
				}else {
					String tempList = new String(selectedList);
					tempList = selectedList + "," + id;
					String[] n = neighs.split(",");
					while(!accept) {
						int tempVal = n.length;
						int malus = 0;
						for(String s : n)
							malus = seeds.contains(Long.parseLong(s)) ? malus + 1 : malus;
						
						tempVal -= malus;
						
						if(malus > 0 && tempVal > 0) {						
							ps = dbW.createStatement(conn, CHECK_MAX.replace(DBWizard.LIST_PLACEHOLDER, tempList));
							ps.setInt(1, graphId);
							ps.setInt(2, tempVal);

							rs = ps.executeQuery();

							if(!rs.next())
								accept = true;
							else {
								id = rs.getLong(1);
								tempList += "," + id;
								neighs = rs.getString(2);
								n = neighs.split(",");
							}

							rs.close();
							ps.close();						
						}else
							accept = true;
					}
				}

				if(accept) {
					seeds.add(id);			
					selectedList += selectedList.length() == 0 ? id : "," + id;
				}
				//g.put(id, Lists.newArrayList(neighs.substring(1, neighs.length()-2).split(",")));

			}else
				break;

		}

		return seeds;
	}

	@Override
	public String getDescription() {
		return "Single Discount";
	}

	@Override
	public String getAcronym() {
		return "SDISC";
	}


}
