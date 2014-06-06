package assign3;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.*;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBTableController extends JFrame{
	
	private JTextField metropolisEntry;
	private JTextField continentEntry;
	private JTextField populationEntry;
	
	private JButton addButton;
	private JButton searchButton;
	private JComboBox populationInequality;
	private JComboBox matchStrength;
	
	private DBTableModel model;
	private JTable table;
	
	public DBTableController(){
		
		super("Metropolis Viewer");
		
		JComponent content = (JComponent) getContentPane();
		content.setLayout(new BorderLayout(4,4));
		
		JPanel upperPanel = new JPanel();
		upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.X_AXIS));
		
		metropolisEntry = new JTextField();
		continentEntry = new JTextField();
		populationEntry = new JTextField();
		upperPanel.add(new JLabel("Metropolis"));
		upperPanel.add(metropolisEntry);
		upperPanel.add(new JLabel("Continent"));
		upperPanel.add(continentEntry);
		upperPanel.add(new JLabel("Population"));
		upperPanel.add(populationEntry);
		
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		
		addButton = new JButton("Add");
		addButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				model.addNewEntry(metropolisEntry.getText(), continentEntry.getText(), 
						populationEntry.getText());
			}
		});
		searchButton = new JButton("Search");
		searchButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				boolean greaterThan;
				if(populationInequality.getSelectedIndex() == 0) greaterThan = true;
				else greaterThan = false;
				boolean exactMatch;
				if(matchStrength.getSelectedIndex() == 0) exactMatch = true;
				else exactMatch = false;
				model.searchCriteriaChanged(metropolisEntry.getText(), continentEntry.getText(), 
						populationEntry.getText(), greaterThan, exactMatch);
			}
		});
		JPanel innerRightPanel = new JPanel();
		innerRightPanel.setLayout(new BorderLayout(4,4));
		innerRightPanel.setBorder(new TitledBorder("Search Options"));
		populationInequality = new JComboBox();
		populationInequality.addItem("Population Larger Than");
		populationInequality.addItem("Population Less Than");
		matchStrength = new JComboBox();
		matchStrength.addItem("Exact Match");
		matchStrength.addItem("Partial Match");
		innerRightPanel.add(populationInequality, BorderLayout.NORTH);
		innerRightPanel.add(matchStrength, BorderLayout.CENTER);
		rightPanel.add(addButton);
		rightPanel.add(searchButton);
		rightPanel.add(innerRightPanel);
		
		String[] headers = {"Metropolis", "Continent", "Population"};
		model = new DBTableModel(headers);
		table = new JTable(model);
		JScrollPane sp = new JScrollPane(table);
		
		content.add(upperPanel, BorderLayout.NORTH);
		content.add(rightPanel, BorderLayout.EAST);
		content.add(sp, BorderLayout.WEST);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }
		
		DBTableController newController = new DBTableController();
		newController.setLocationByPlatform(true);
		newController.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		newController.pack();
		newController.setVisible(true);
	}

}
