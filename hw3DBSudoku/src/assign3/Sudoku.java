package assign3;

import java.util.*;

/*
 * Encapsulates a Sudoku grid to be solved.
 * CS108 Stanford.
 */
public class Sudoku {
	// Provided grid data for main/testing
	// The instance variable strategy is up to you.
	
	// Provided easy 1 6 grid
	// (can paste this text into the GUI too)
	public static final int[][] easyGrid = Sudoku.stringsToGrid(
	"1 6 4 0 0 0 0 0 2",
	"2 0 0 4 0 3 9 1 0",
	"0 0 5 0 8 0 4 0 7",
	"0 9 0 0 0 6 5 0 0",
	"5 0 0 1 0 2 0 0 8",
	"0 0 8 9 0 0 0 3 0",
	"8 0 9 0 4 0 2 0 0",
	"0 7 3 5 0 9 0 0 1",
	"4 0 0 0 0 0 6 7 9");
	
	
	// Provided medium 5 3 grid
	public static final int[][] mediumGrid = Sudoku.stringsToGrid(
	 "530070000",
	 "600195000",
	 "098000060",
	 "800060003",
	 "400803001",
	 "700020006",
	 "060000280",
	 "000419005",
	 "000080079");
	
	// Provided hard 3 7 grid
	// 1 solution this way, 6 solutions if the 7 is changed to 0
	public static final int[][] hardGrid = Sudoku.stringsToGrid(
	"3 7 0 0 0 0 0 8 0",
	"0 0 1 0 9 3 0 0 0",
	"0 4 0 7 8 0 0 0 3",
	"0 9 3 8 0 0 0 1 2",
	"0 0 0 0 4 0 0 0 0",
	"5 2 0 0 0 6 7 9 0",
	"6 0 0 0 2 1 0 4 0",
	"0 0 0 5 3 0 9 0 0",
	"0 3 0 0 0 0 0 5 1");
	
	
	public static final int SIZE = 9;  // size of the whole 9x9 puzzle
	public static final int PART = 3;  // size of each 3x3 part
	public static final int MAX_SOLUTIONS = 100;
	
	private int[][] grid; //internal representation of the initial board
	private int[][] solutionGrid;//stored board of last found solution
	private boolean solutionFound = false;//boolean indicating whether a solution has been found yet
	private long solutionTime = 0;//time it took to find a solution
	
	/**
	 * Class: Spot
	 * @author skylarpeterson
	 * The inner class Spot encapsulates one square in the 81-square Sudoku grid. The Spot
	 * keeps track of where in the board it is, how many numbers can legally be assigned
	 * to that spot with the initial board configuration, and a set of those legally assignable
	 * numbers.  These numbers are found and stored in the initial setup of a Spot object. The
	 * Spot object does implement the Comparable interface to allow for comparing of two Spot
	 * objects.
	 */
	
	private class Spot implements Comparable<Spot> {
		private int row;
		private int col;
		private int numAssignsAvailable;
		private HashSet<Integer> assignableNums;
		
		public Spot(int row, int col){
			this.row = row;
			this.col = col;
			assignableNums = new HashSet<Integer>();
			if(grid[row][col] != 0) numAssignsAvailable = 0;
			else {
				assignableNums = possibleRowAssigns();
				assignableNums.retainAll(possibleColAssigns());
				assignableNums.retainAll(possibleSquareAssigns());
				numAssignsAvailable = assignableNums.size();
			}
		}
		
		/**
		 * HashSet<Integer> possibleNums
		 * @param nonPossibilities
		 * @return
		 * Given a set of numbers that aren't possibilities, this simple method steps through
		 * the numbers 1-9, storing numbers that are possibilities and returning them.
		 */
		private HashSet<Integer> possibleNums(HashSet<Integer> nonPossibilities){
			HashSet<Integer> possibilities = new HashSet<Integer>();
			for(int j = 1; j <= 9; j++) {
				if(!nonPossibilities.contains(j)) possibilities.add(j);
			}
			return possibilities;
		}
		
		/**
		 * HashSet<Integer> possibleRowAssigns
		 * @return
		 * Returns a set of the possible numbers that can be legally inserted into the Spot
		 * based on the numbers found in the same row as the Spot.
		 */
		private HashSet<Integer> possibleRowAssigns(){
			HashSet<Integer> nonPossibilities = new HashSet<Integer>();
			for(int i = 0; i < grid[row].length; i++) nonPossibilities.add(grid[row][i]);
			return possibleNums(nonPossibilities);
		}
		
		/**
		 * HashSet<Integer> possibleColAssigns
		 * @return
		 * Returns a set of the possible numbers that can be legally inserted into the Spot
		 * based on the numbers found in the same column as the Spot.
		 */
		private HashSet<Integer> possibleColAssigns(){
			HashSet<Integer> nonPossibilities = new HashSet<Integer>();
			for(int i = 0; i < grid.length; i++) nonPossibilities.add(grid[i][col]);
			return possibleNums(nonPossibilities);
		}
		
		/**
		 * HashSet<Integer> possibleSquareAssigns
		 * @return
		 * Returns a set of the possible numbers that can be legally inserted into the Spot
		 * based on the numbers found in the surround square the Spot is a part of.
		 */
		private HashSet<Integer> possibleSquareAssigns(){
			HashSet<Integer> nonPossibilities = new HashSet<Integer>();
			
			//Must first compute how far away from the upper left corner of the square
			//the given Spot is
			int rowOffset;
			int colOffset;
			if(row > 2) rowOffset = row % 3;
			else rowOffset = row;
			if(col > 2) colOffset = col % 3;
			else colOffset = col;
			
			int startRow = row - rowOffset;
			int startCol = col - colOffset;
			for(int i = startRow; i < startRow + 3; i++){
				for(int j = startCol; j < startCol + 3; j++){
					nonPossibilities.add(grid[i][j]);
				}
			}
			
			return possibleNums(nonPossibilities);
		}
		
		/**
		 * int getRow
		 * @return
		 * Returns the Spot's row.
		 */
		public int getRow(){
			return row;
		}
		
		/**
		 * int getCol
		 * @return
		 * Returns the Spot's column.
		 */
		public int getCol(){
			return col;
		}
		
		/**
		 * void setValue
		 * @param val
		 * @param gridForValue
		 * Sets the given value to the Spot in the given grid.
		 */
		public void setValue(int val, int[][] gridForValue){
			gridForValue[row][col] = val;
		}
		
		/**
		 * int getNumAssigns
		 * @return
		 * Returns the number of possible numbers that can be assigned to the Spot legally.
		 */
		public int getNumAssigns(){
			return numAssignsAvailable;
		}
		
		/**
		 * HashSet<Integer> getAssignableNums
		 * @return
		 * Returns the set of numbers that can legally be assigned to the spot.
		 */
		public HashSet<Integer> getAssignableNums(){
			return assignableNums;
		}
		
		/**
		 * int compareTo
		 * @param s
		 * Returns a 1 if the number of legal assignments the Spot has is greater than that
		 * of the passed in Spot. Returns a 0 otherwise.
		 */
		public int compareTo(Spot s){
			if(s.getNumAssigns() > this.numAssignsAvailable) return 1;
			return 0;
		}
	}
	
	// Provided various static utility methods to
	// convert data formats to int[][] grid.
	
	/**
	 * Returns a 2-d grid parsed from strings, one string per row.
	 * The "..." is a Java 5 feature that essentially
	 * makes "rows" a String[] array.
	 * (provided utility)
	 * @param rows array of row strings
	 * @return grid
	 */
	public static int[][] stringsToGrid(String... rows) {
		int[][] result = new int[rows.length][];
		for (int row = 0; row<rows.length; row++) {
			result[row] = stringToInts(rows[row]);
		}
		return result;
	}
	
	
	/**
	 * Given a single string containing 81 numbers, returns a 9x9 grid.
	 * Skips all the non-numbers in the text.
	 * (provided utility)
	 * @param text string of 81 numbers
	 * @return grid
	 */
	public static int[][] textToGrid(String text) {
		int[] nums = stringToInts(text);
		if (nums.length != SIZE*SIZE) {
			throw new RuntimeException("Needed 81 numbers, but got:" + nums.length);
		}
		
		int[][] result = new int[SIZE][SIZE];
		int count = 0;
		for (int row = 0; row<SIZE; row++) {
			for (int col=0; col<SIZE; col++) {
				result[row][col] = nums[count];
				count++;
			}
		}
		return result;
	}
	
	
	/**
	 * Given a string containing digits, like "1 23 4",
	 * returns an int[] of those digits {1 2 3 4}.
	 * (provided utility)
	 * @param string string containing ints
	 * @return array of ints
	 */
	public static int[] stringToInts(String string) {
		int[] a = new int[string.length()];
		int found = 0;
		for (int i=0; i<string.length(); i++) {
			if (Character.isDigit(string.charAt(i))) {
				a[found] = Integer.parseInt(string.substring(i, i+1));
				found++;
			}
		}
		int[] result = new int[found];
		System.arraycopy(a, 0, result, 0, found);
		return result;
	}


	// Provided -- the deliverable main().
	// You can edit to do easier cases, but turn in
	// solving hardGrid.
	public static void main(String[] args) {
		Sudoku sudoku;
		sudoku = new Sudoku(hardGrid);
		
		System.out.println(sudoku); // print the raw problem
		int count = sudoku.solve();
		System.out.println("solutions:" + count);
		System.out.println("elapsed:" + sudoku.getElapsed() + "ms");
		System.out.println(sudoku.getSolutionText());
	}

	/**
	 * Sets up based on the given ints.
	 */
	public Sudoku(int[][] ints) {
		if(ints.length != 9 || ints[0].length !=9) throw new RuntimeException("Grid was not 9 X 9");
		grid = ints;
		solutionGrid = new int[grid.length][grid[0].length];
	}
	
	/**
	 * Sets up based on the provided string of 81 numbers
	 */
	public Sudoku(String text) {
		grid = textToGrid(text);
		solutionGrid = new int[grid.length][grid[0].length];
	}
	
	/**
	 * String gridToString
	 * @param grid
	 * @return
	 * Takes a passed in grid of ints and transforms it into a string with each
	 * line indicating a new row in the grid.
	 */
	private String gridToString(int[][] grid){
		StringBuilder gridString = new StringBuilder();
		for(int i = 0; i < grid.length; i++){
			for(int j = 0; j < grid[i].length; j++){
				int num = grid[i][j];
				gridString.append(num + " ");
			}
			gridString.append("\n");
		}
		return gridString.toString();
	}
	
	/**
	 * String toString
	 * Returns the string represenation of the internal grid.
	 */
	@Override
	public String toString(){
		return gridToString(this.grid);
	}
	
	/**
	 * Solves the puzzle, invoking the underlying recursive search.
	 */
	public int solve() {
		long startTime = System.currentTimeMillis();
		ArrayList<Spot> emptySpots = new ArrayList<Spot>();
		for(int i = 0; i < grid.length; i++){
			for(int j = 0; j < grid[i].length; j++){
				if(grid[i][j] == 0) {
					Spot newSpot = new Spot(i, j);
					emptySpots.add(newSpot);
				}
			}
		}
		Collections.sort(emptySpots, new Comparator<Spot>(){
			public int compare(Spot s1, Spot s2){
				return s1.getNumAssigns() - s2.getNumAssigns();
			}
		});
		int[][] solutionGrid = new int[grid.length][grid[0].length];
		for(int i = 0; i < grid.length; i++){
			System.arraycopy(grid[i], 0, solutionGrid[i], 0, grid[i].length);
		}
		int solution = solve(emptySpots, 0, 0, solutionGrid); 
		long endTime = System.currentTimeMillis();
		solutionTime = endTime - startTime;
		return solution;
	}
	
	/**
	 * int solve
	 * @param emptySpots
	 * @param index
	 * @param numSolutions
	 * @param solutionGrid
	 * @return
	 * Returns the number of solutions in the grid before stopping at a maximum of 100
	 * solutions.  This method works recursively, checking each possible legal placement
	 * of a value on the board to check for every possible solution and modifies the
	 * solutionGrid variable to represent a possible solution once one is found.  
	 */
	private int solve(ArrayList<Spot> emptySpots, int index, int numSolutions, int[][] solutionGrid){
		if(numSolutions == MAX_SOLUTIONS) return MAX_SOLUTIONS;
		if(index == emptySpots.size()) {
			for(int i = 0; i < solutionGrid.length; i++){
				for(int j = 0; j < solutionGrid[i].length; j++){
					this.solutionGrid[i][j] = solutionGrid[i][j];
				}
			}
			this.solutionFound = true;
			return numSolutions + 1;
		}
		
		Spot s = emptySpots.get(index);
		HashSet<Integer> assignableNums = s.getAssignableNums();
		Iterator<Integer> it = assignableNums.iterator();
		while(it.hasNext()){
			int next = it.next();
			if(isLegalPlacement(next, s, solutionGrid)){
				s.setValue(next, solutionGrid);
				numSolutions = solve(emptySpots, index + 1, numSolutions, solutionGrid);
			}
			s.setValue(0, solutionGrid);
		}
		return numSolutions;
	}
	
	/**
	 * boolean isLegalPlacement
	 * @param val
	 * @param s
	 * @param solutionGrid
	 * @return
	 * If the value can be legally placed at the Spot s on the solutionGrid, the method
	 * returns true.  But first, it issues three checks: one on the row of the spot, one
	 * on the column of the spot, and one in the spot's square.  If the value is found
	 * to be an illegal place, the method returns false.
	 */
	private boolean isLegalPlacement(int val, Spot s, int[][] solutionGrid){
		int row = s.getRow();
		int col = s.getCol();
		
		// Check column for same value
		for(int i = 0; i < solutionGrid.length; i++){
			if(solutionGrid[i][col] == val) return false;
		}
		
		// Check row for same value
		for(int j = 0; j < solutionGrid[row].length; j++){
			if(solutionGrid[row][j] == val) return false;
		}
		
		// Check square for same value
		int rowOffset;
		int colOffset;
		if(row > 2) rowOffset = row % 3;
		else rowOffset = row;
		if(col > 2) colOffset = col % 3;
		else colOffset = col;
		
		int startRow = row - rowOffset;
		int startCol = col - colOffset;
		for(int i = startRow; i < startRow + 3; i++){
			for(int j = startCol; j < startCol + 3; j++){
				if(solutionGrid[i][j] == val) return false;
			}
		}
		
		return true;
	}
	
	/**
	 * String getSolutionText
	 * @return
	 * If a solution has been found, then the string representation of one of the solutions
	 * is returned. Otherwise, the empty string is returned.
	 */
	public String getSolutionText() {
		if(solutionFound) return gridToString(this.solutionGrid);
		return "";
	}
	
	/**
	 * long getElapsed
	 * @return
	 * Returns the elapsed time it took to solve the Sudoku board in milliseconds.
	 */
	public long getElapsed() {
		return solutionTime;
	}

}
