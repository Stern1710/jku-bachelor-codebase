package four.game;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BoardTest {
	//Testing in row=5 where the first stone drops into a col
	private Board board;
	
	@BeforeEach
	void setup() {
		board = new BoardImpl();
	}
	
	@Test
	void testGetStone() {
		board.setStone(0, Stone.X);
		//Set a stone in first col, test the field it has dropped to if it is really there and not any other stone
		assertEquals(Stone.X, board.getStone(5, 0));
		assertNotEquals(Stone.None, board.getStone(5, 0));
		assertNotEquals(Stone.O, board.getStone(5, 0));

		//Test that any other stone in col is still the same 
		for (int r=4; 0 <= r; r--) {
			assertEquals(Stone.None, board.getStone(r, 0));
		}
	}
	
	@Test
	void testSetStone() {
		//Check if stone drops in correctly and not to first index (as that would be top of the board) or overwrittes existing stone
		assertEquals(5, board.setStone(0, Stone.X));
		assertEquals(4, board.setStone(0, Stone.X));
		assertNotEquals(4, board.setStone(0, Stone.X));
		assertNotEquals(0, board.setStone(1, Stone.X));
	}
	
	@Test
	void testIsValidMove() {
		//Check if inserting in every col is valid
		for (int col=0; col < 7; col++) {
			assertTrue(board.isValidMove(0));
		}
		//Check if index out of bound is not accepted
		assertFalse(board.isValidMove(-1));
		assertFalse(board.isValidMove(8));
		//Fill one column with stones and the check
		for (int i=0; i < 6; i++) {
			board.setStone(0, Stone.X);
		}
		assertFalse(board.isValidMove(0));
	}
	
	@Test
	void testIsEmpty() {
		//By default everythink is empty, check that!
		for (int row=0; row < 6; row++) {
			for (int col=0; col < 7; col++) {
				assertTrue(board.isEmpty(row, col));

			}
		}
		//Set stone and then check again if not empty now
		board.setStone(0, Stone.X);
		assertFalse(board.isEmpty(5, 0));
	}
	
	@Test
	void testIsFull() {
		//At the start board is empty
		assertFalse(board.isFull());
		//Fill some stones
		board.setStone(0, Stone.X);
		assertFalse(board.isFull());
		
		//Fill the whole board
		for (int row=0; row < 6; row++) {
			for (int col=0; col < 7; col++) {
				board.setStone(col, Stone.X);
			}
		}
		assertTrue(board.isFull());
	}
	
	@Test
	void testHasFourConncted() {
		//Board is empty, no connected gems
		assertFalse(board.hasFourConnected(0, 0));
		
		//Fill with four in a row but with a gap
		board.setStone(0, Stone.X);
		board.setStone(1, Stone.X);
		board.setStone(3, Stone.X);
		board.setStone(4, Stone.X);
		assertFalse(board.hasFourConnected(5, 0));
		
		//Fill the gap with a stone from the other player
		board.setStone(2, Stone.O);
		assertFalse(board.hasFourConnected(5, 0));
		
		//Fill four stones from same player in a row
		board.setStone(5, Stone.X);
		board.setStone(6, Stone.X);
		assertFalse(board.hasFourConnected(5, 0));
		assertTrue(board.hasFourConnected(5, 3));
		assertTrue(board.hasFourConnected(5, 6));
	}
	
	@Test
	void testCountInRow() {
		//Test for an empty row
		assertEquals(0, board.countInRow(5, 0));
		
		//Fill with four in a row but with a gap
		board.setStone(0, Stone.X);
		board.setStone(1, Stone.X);
		board.setStone(3, Stone.X);
		board.setStone(4, Stone.X);
		assertEquals(2, board.countInRow(5, 0));
		assertEquals(2, board.countInRow(5, 1));
		assertEquals(2, board.countInRow(5, 3));
		assertEquals(2, board.countInRow(5, 4));
		
		//Fill the gap with a stone from the other player
		board.setStone(2, Stone.O);
		assertEquals(2, board.countInRow(5, 0));
		assertEquals(2, board.countInRow(5, 1));
		assertEquals(2, board.countInRow(5, 3));
		assertEquals(2, board.countInRow(5, 4));
		assertNotEquals(5, board.countInRow(5, 1));
		assertNotEquals(5, board.countInRow(5, 3));
		
		//Fill four stones from same player in a row
		board.setStone(5, Stone.X);
		board.setStone(6, Stone.X);
		assertEquals(4, board.countInRow(5, 3));
		assertEquals(4, board.countInRow(5, 4));
		assertEquals(4, board.countInRow(5, 5));
		assertEquals(4, board.countInRow(5, 6));
		assertEquals(2, board.countInRow(5, 0));
		assertNotEquals(5, board.countInRow(5, 1));
	}
	
	@Test
	void testCountInCol() {
		//Test for empty row
		assertEquals(0, board.countInCol(5, 0));
		
		//Fill with three
		board.setStone(0, Stone.X);
		board.setStone(0, Stone.X);
		board.setStone(0, Stone.X);
		assertEquals(3, board.countInCol(5, 0));
		assertEquals(0, board.countInCol(5, 1));
		assertEquals(0, board.countInCol(5, 2));
		assertNotEquals(3, board.countInCol(0, 0));
		
		//Fill with stone from other player
		board.setStone(0, Stone.O);
		assertNotEquals(4, board.countInCol(5, 0));
	}
	
	@Test
	void testCountInDiagRight() {
		//Test for empty diag
		assertEquals(0, board.countInDiagRight(5, 0));
		
		//Fill left diagonal with 3, test along the line (this should only give 1)
		board.setStone(0, Stone.X);
		board.setStone(0, Stone.X);
		board.setStone(0, Stone.O);
		board.setStone(1, Stone.X);
		board.setStone(1, Stone.O);
		board.setStone(2, Stone.O);
		assertEquals(1, board.countInDiagRight(5, 2));
		assertEquals(1, board.countInDiagRight(4, 1));
		assertEquals(1, board.countInDiagRight(3, 0));
		
		//Fill right diagonal with 3, test along the line (this now should give 3)
		board.setStone(3, Stone.X);
		board.setStone(4, Stone.O);
		board.setStone(4, Stone.X);
		board.setStone(5, Stone.O);
		board.setStone(5, Stone.O);
		board.setStone(5, Stone.X);
		assertEquals(3, board.countInDiagRight(5, 3));
		assertEquals(3, board.countInDiagRight(4, 4));
		assertEquals(3, board.countInDiagRight(3, 5));
	}
	
	@Test
	void testCountInDiagLeft() {
		//Test for empty diag
		assertEquals(0, board.countInDiagLeft(5, 0));
		
		//Fill left diagonal with 3, test along the line (this should give 3)
		board.setStone(0, Stone.X);
		board.setStone(0, Stone.X);
		board.setStone(0, Stone.O);
		board.setStone(1, Stone.X);
		board.setStone(1, Stone.O);
		board.setStone(2, Stone.O);
		assertEquals(3, board.countInDiagLeft(5, 2));
		assertEquals(3, board.countInDiagLeft(4, 1));
		assertEquals(3, board.countInDiagLeft(3, 0));
		
		//Fill right diagonal with 3, test along the line (this should only give 1)
		board.setStone(3, Stone.X);
		board.setStone(4, Stone.O);
		board.setStone(4, Stone.X);
		board.setStone(5, Stone.O);
		board.setStone(5, Stone.O);
		board.setStone(5, Stone.X);
		assertEquals(1, board.countInDiagLeft(5, 3));
		assertEquals(1, board.countInDiagLeft(4, 4));
		assertEquals(1, board.countInDiagLeft(3, 5));
	}
	
	@Test
	void testMaxCount() {
		//Test for empty field
		assertEquals(0, board.maxCount(5, 0));
		
		//Fill left diagonal with 3
		board.setStone(0, Stone.X);
		board.setStone(0, Stone.X);
		board.setStone(0, Stone.O);
		board.setStone(1, Stone.X);
		board.setStone(1, Stone.O);
		board.setStone(2, Stone.O);
		assertEquals(2, board.maxCount(5, 0));
		assertEquals(2, board.maxCount(5, 1));
		assertEquals(2, board.maxCount(4, 0));
		assertEquals(3, board.maxCount(3, 0));
		assertEquals(3, board.maxCount(4, 1));
		assertEquals(3, board.maxCount(5, 2));
	}
}