package assign3;

import javax.swing.table.AbstractTableModel;
import java.sql.*;

public class DBTableModel extends AbstractTableModel{
	
	private MetropolisModel brain;
	private String[] columnNames;
	private Object[][] data;

	/**
	 * Constructs a new DBTableModel.  A MetropolisModel is initialized as the Model's brain
	 * and the headers for the column are stored.
	 * @param headers for columns
	 */
	public DBTableModel(String[] headers){
		brain = new MetropolisModel();
		columnNames = headers;
		data = new Object[0][0];
	}
	
	/**
	 * Uses the MetropolisModel brain to add a new entry into the database, then fires 
	 * a tableDataChanged() in case redrawing will yield new results.
	 * @param metropolis name
	 * @param continent name
	 * @param population number (in string form)
	 */
	public void addNewEntry(String metropolis, String continent, String population){
		brain.add(metropolis, continent, population);
		data = new Object[1][columnNames.length];
		data[0][0] = metropolis;
		data[0][1] = continent;
		data[0][2] = population;
		fireTableDataChanged();
	}
	
	/**
	 * Queries the MetropolisModel brain for the ResultSet of table entries based on the 
	 * provided parameters as search terms.  Loads results into stored data grid, then
	 * fires a tableDataChanged() to redraw the table to represent the new search.
	 * @param metropolis
	 * @param continent
	 * @param population
	 * @param populationLargerThan
	 * @param exactMatch
	 */
	public void searchCriteriaChanged(String metropolis, String continent, String population, boolean populationLargerThan, boolean exactMatch){
		ResultSet rs = brain.search(metropolis, continent, population, populationLargerThan, exactMatch);
		try {
			rs.last();
			data = new Object[rs.getRow()][columnNames.length];
			int i = 0;
			rs.beforeFirst();
			while(rs.next()){
				data[i][0] = rs.getString("metropolis");
				data[i][1] = rs.getString("continent");
				data[i][2] = rs.getInt("population");
				i++;
			}
			fireTableDataChanged();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Override of super class. Retrieves the column names from stored headers.
	 * @return name of column
	 */
	@Override
	public String getColumnName(int columnIndex){
		return columnNames[columnIndex];
	}
		
	/**
	 * Override of super class. Returns the number of columns based on the number
	 * of headers.
	 * @return number of columns
	 */
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	/**
	 * Override of super class. Returns the number of rows based on the underlying
	 * data grid.
	 * @return number of rows
	 */
	@Override
	public int getRowCount() {
		return data.length;
	}

	/**
	 * Override of super class. Returns the object found in the row and column of 
	 * the data grid.
	 * @return the metropolis name, continent name, or population number of the given row
	 */
	@Override
	public Object getValueAt(int row, int col) {
		return data[row][col];
	}
	
}
