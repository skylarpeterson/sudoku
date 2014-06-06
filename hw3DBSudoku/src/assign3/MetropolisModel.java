package assign3;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

/* 
 * CS108 Student: This file will be used to help the staff grade your assignment.
 * You can modify this file as much as you like, provided three restrictions:
 * 1) Do not change the class/file name
 * 		- The class/file name should be MetropolisModel
 * 2) Do not modify the MetropolisControl interface
 * 3) MetropolisModel must implement the MetropolisControl interface
 * 		- You can modify MetropolisModel to inherit/implement any additional class/interface
 */
public class MetropolisModel implements MetropolisControl {
	
	private Connection con;
	private Statement stmt;
	
	public MetropolisModel(){
		try {
			con = MyDB.getConnection();
			stmt = con.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ResultSet search(String metropolis, String continent, String population, boolean populationLargerThan, boolean exactMatch) {
		StringBuilder queryString = new StringBuilder();
		queryString.append("SELECT * FROM metropolises");
		if(!metropolis.isEmpty()) {
			if(exactMatch) queryString.append(" WHERE metropolis = \"" + metropolis +"\"");
			else queryString.append(" WHERE metropolis LIKE \"%" + metropolis +"%\"");
			if(!continent.isEmpty() || !population.isEmpty()) queryString.append(" AND ");
		}
		if(!continent.isEmpty()) {
			if(metropolis.isEmpty()) queryString.append(" WHERE ");
			if(exactMatch) queryString.append("continent = \"" + continent +"\"");
			else queryString.append("continent LIKE \"%" + continent +"%\"");
			if(!population.isEmpty()) queryString.append(" AND ");
		}
		if(!population.isEmpty()) {
			if(metropolis.isEmpty() && continent.isEmpty()) queryString.append(" WHERE ");
			queryString.append("population ");
			if(populationLargerThan) queryString.append("> " + population);
			else queryString.append("< " + population);
		}
		
		try {
			return stmt.executeQuery(queryString.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public void add(String metropolis, String continent, String population) {
		StringBuilder insertString = new StringBuilder();
		insertString.append("INSERT INTO metropolises VALUES (\"" + 
				metropolis + "\", \"" + continent + "\", " + population + ")");
		try {
			stmt.executeUpdate(insertString.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void endConnection(){
		MyDB.close();
	}
}

interface MetropolisControl {
	/**
	 * Searches the Metropolis data-set for the provided search parameters.
	 * Returns the query results as a java.sql.ResultSet
	 * 
	 * @param metropolis value of the metropolis field
	 * @param continent value of the continent field
	 * @param population value of the population field
	 * @param populationLargerThan True if "Population Larger Than" has been selected
	 * @param exactMatch True if "Exact Match" has been selected
	 * 
	 * @return resultSet Results for the given query
	 */
	public ResultSet search(String metropolis, String continent, String population, boolean populationLargerThan, boolean exactMatch);
	
	/**
	 * Adds the entry to the Metropolis data-set.
	 * 
	 * @param metropolis value of the metropolis field
	 * @param continent value of the continent field
	 * @param population value of the population field
	 */
	public void add(String metropolis, String continent, String population);
}