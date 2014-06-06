package assign3;

import static org.junit.Assert.*;

import org.junit.Test;

public class SudokuTest {

	@Test
	public void testToString1() {
		String gridString = "123456789123456789123456789123456789123456789123456789123456789123456789123456789";
		Sudoku testSudoku = new Sudoku(gridString);
		System.out.println(testSudoku.toString());
	}
	
	@Test
	public void testToString2() {
		Sudoku testSudoku = new Sudoku(Sudoku.easyGrid);
		System.out.println(testSudoku.toString());
		System.out.println(testSudoku.getSolutionText());
	}
	
	@Test
	public void testSolve1() {
		Sudoku testSudoku = new Sudoku(Sudoku.easyGrid);
		int solutions = testSudoku.solve();
		System.out.println(testSudoku.getSolutionText());
		assertEquals(1, solutions);
	}
	
	@Test
	public void testSolve2() {
		Sudoku testSudoku = new Sudoku(Sudoku.mediumGrid);
		int solutions = testSudoku.solve();
		System.out.println(testSudoku.getSolutionText());
		assertEquals(1, solutions);
	}
	
	@Test
	public void testSolve3() {
		Sudoku testSudoku = new Sudoku(Sudoku.hardGrid);
		int solutions = testSudoku.solve();
		System.out.println("Solutions: " + solutions);
		System.out.println(testSudoku.getSolutionText());
		assertEquals(1, solutions);
	}
 
	@Test
	public void testSolve4() {
		int[][] grid = Sudoku.stringsToGrid(
		"0 3 5 2 9 0 8 6 4",
		"0 8 2 4 1 0 7 0 3",
		"7 6 4 3 8 0 0 9 0",
		"2 1 8 7 3 9 0 4 0",
		"0 0 0 8 0 4 2 3 0",
		"0 4 3 0 5 2 9 7 0",
		"4 0 6 5 7 1 0 0 9",
		"3 5 9 0 2 8 4 1 7",
		"8 0 0 9 0 0 5 2 6");
		Sudoku testSudoku = new Sudoku(grid);
		int solutions = testSudoku.solve();
		System.out.println("Solutions: " + solutions);
		System.out.println(testSudoku.getSolutionText());
		System.out.println("Time: " + testSudoku.getElapsed());
	}
}
