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

package vaim.io.session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.math.NumberUtils;

import vaim.io.db.DBWizard;
import vaim.io.db.DBWizard.QUERY_TYPE;

public class SessionControl {

	private static final String DATA_PREFIX = "%_PREFIX_%";
	
	public static final String ON_MERGE_PLACEHOLDER = "#_on_match_info_#";
	public static final String ON_SET_PLACEHOLDER = "#_on_merge_info_#";
	public static final String GRAPH_ID = "graphId";
	public static final String RESOLUTION = "resolution";
	public static final String SIMULATIONS = "simulations";
	public static final String AGGREGATION = "aggregation";	
	public static final String FRAMESTART = "framestart";
	public static final String OFFSET = "offset";
	public static final String SELECTED_SECTORS = "selectedSectors";

	private static final String REGEX_PATTERN = "\"\\w*\":\"?([0-9a-zA-Z:;!-\\\\'\"\\[\\]]*)\"?";			
	
	public static void createOrUpdateSession(Connection conn, String sessionId, Map<String, String> attributes) throws Exception{
		String toPrepare = DBWizard.getStringFromQuery(DBWizard.QUERY_TYPE.CREATE_OR_UPDATE_SESSION);
		String sessionInfo = generateSessionInfo(attributes);
		//String replacement = toPrepare.replace(ON_MERGE_PLACEHOLDER, sessionInfo.replace(DATA_PREFIX, ""));
		String replacement = toPrepare.replace(ON_SET_PLACEHOLDER, sessionInfo.replace(':', '=').replace(DATA_PREFIX, "n."));
		
		PreparedStatement pms = conn.prepareStatement(replacement);
		pms.setString(1, sessionId);
		pms.setString(2, sessionId);
		
		int result = pms.executeUpdate();
		
		pms.close();		
		
		/*if(result != 1 && result != 0 && result != 2) {
			throw new Exception("Incorrect number of modified session elements: " + result);
		}*/
		
		conn.commit();
	}
	
	public static Map<String, String> recoverSessionInfo(Connection conn, String id) throws Exception{
				
		PreparedStatement pms = conn.prepareStatement(DBWizard.getStringFromQuery(QUERY_TYPE.RECOVER_SESSION));
		HashMap<String, String> container = new HashMap<String, String>();
		pms.setString(1, id);
		
		ResultSet rs = pms.executeQuery();
//		ResultSetMetaData rsm = rs.getMetaData();
		
		if(!rs.next())
			throw new Exception("No sessions found");
		
//		for(int i = 1; i<rsm.getColumnCount(); i++)
//			container.put(rsm.getColumnName(i), rs.getString(i));
		Pattern p = Pattern.compile(REGEX_PATTERN);
		Matcher m = p.matcher(rs.getString(1).substring(1, rs.getString(1).length()-1));
		
		//String[] parameters = rs.getString(1).substring(1, rs.getString(1).length()-1).replace(",","").split(" ");
		while(m.find()) {
			String[] matched = m.group().replace("\"", "").replace("\\","").replace(",", "").replace("[","").replace("]", "").split(":");
			if(matched[0].indexOf("_") >= 0 || matched.length != 2)
				continue;			
			container.put(matched[0], matched[1]);
		}
		
		rs.close();
		pms.close();
		
		return container;
		
	}
	
	private static String generateSessionInfo(Map<String, String> attributes) {
		String onMerge = "";
		//String onMatch = "";
		Boolean comma = false;
		for(Entry<String, String> att : attributes.entrySet()) {
			if(att.getKey().equals("sessionId"))
				continue;
			if(!comma) {
				comma = true;
				onMerge += " ";
			} else
				onMerge += ", ";
			onMerge += DATA_PREFIX + escapeString(att.getKey()) + ":" + 
					(NumberUtils.isParsable(att.getValue()) ?
							(att.getValue().indexOf('.') < 0 ? Integer.valueOf(att.getValue()).toString() : Float.valueOf(att.getValue())).toString()
					: "\'" + escapeString(att.getValue()).replace(" ", "").replace(",",";") +"\'");			
			//onMatch += ", " + escapeString(att.getKey()) + " = " + "\'" + escapeString(att.getValue()) +"\'";
		}
		return onMerge;
	}
	
	//#### TO IMPLEMENT
	private static String escapeString(String toEscape) {
		String escaped = toEscape;
		return escaped;
	}

}
