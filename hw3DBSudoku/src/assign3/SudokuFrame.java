package assign3;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;


 public class SudokuFrame extends JFrame {
	
	private JButton checkButton;
	private JCheckBox autoBox;
	 
	private class SolutionListener implements DocumentListener {
		public void insertUpdate(DocumentEvent e){
			if(autoBox.isSelected()){
				checkButton.doClick();
			}
		}
		
		public void changedUpdate(DocumentEvent e){
			if(autoBox.isSelected()){
				checkButton.doClick();
			}
		}
		
		public void removeUpdate(DocumentEvent e){
			if(autoBox.isSelected()){
				checkButton.doClick();
			}
		}
	}
	 
	public SudokuFrame() {
		super("Sudoku Solver");
		
		JComponent content = (JComponent) getContentPane();
		content.setLayout(new BorderLayout(4,4));
		
		final JTextArea source = new JTextArea(15, 20);
		source.setBorder(new TitledBorder("Puzzle"));
		source.getDocument().addDocumentListener(new SolutionListener());
		
		final JTextArea result = new JTextArea(15, 20);
		result.setBorder(new TitledBorder("Solution"));
		
		content.add(source, BorderLayout.CENTER);
		content.add(result, BorderLayout.EAST);
		
		final ActionListener sourceListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String gridText = source.getText();
				try {
					Sudoku newSudoku = new Sudoku(gridText);
					int solutions = newSudoku.solve();
					if(solutions > 0){
						StringBuilder solutionString = new StringBuilder();
						solutionString.append(newSudoku.getSolutionText() + "\n");
						solutionString.append("Solutions: " + solutions + "\n");
						solutionString.append("Elapsed: " + newSudoku.getElapsed() + "\n");
						result.setText(solutionString.toString());
					} else {
						result.setText("No Solution");
					}
				} 
				catch (Exception ignored) {
					result.setText("Parsing Problem");
				}
			}
		};
		
		Panel p = new Panel();
		p.setLayout(new BorderLayout());
		checkButton =  new JButton("Check");
		checkButton.addActionListener(sourceListener);
		p.add(checkButton, BorderLayout.WEST);
		autoBox = new JCheckBox("Auto Check");
		p.add(autoBox, BorderLayout.CENTER);
		content.add(p, BorderLayout.SOUTH);
		
		setLocationByPlatform(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	
	public static void main(String[] args) {
		// GUI Look And Feel
		// Do this incantation at the start of main() to tell Swing
		// to use the GUI LookAndFeel of the native platform. It's ok
		// to ignore the exception.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }
		
		SudokuFrame frame = new SudokuFrame();
	}

}
