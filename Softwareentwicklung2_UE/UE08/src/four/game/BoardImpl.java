package four.game;

import java.util.Arrays;
import java.util.stream.IntStream;

import inout.Out;

public class BoardImpl implements Board{

	public static final int ROWS = 6;
	public static final int COLS = 7;
	private final Stone[][] field = new Stone[ROWS][COLS];
	
	public BoardImpl() {
		Arrays.stream(field).forEach(row -> Arrays.fill(row, Stone.None));
	}
	
	public void print() {
		Out.println("|----------------------|");
		Out.print("|"); 
		for (int c = 0; c < COLS; c++) {
			Out.print(" " + (c + 1) + " "); 
		}
		Out.println(" |"); 
		Out.println("|----------------------|");
		for (int r = 0; r < ROWS; r++) {
			Out.print("|");
			for (int c = 0; c < COLS; c++) {
				Out.print(getStone(r, c).outputString());  
			}
			Out.println(" | "); 
		}		
		Out.println("|----------------------|");
	}


	@Override
	public Stone getStone(int row, int col) {
		return field[row][col];
	}


	@Override
	public int setStone(int col, Stone stone) {
		if (isValidMove(col)) {
			for (int row = ROWS - 1; 0 <= row; row--) {
				if (field[row][col] == Stone.None) {
					field[row][col] = stone;
					return row;
				}
			}
		}
		return -1;
	}


	@Override
	public boolean isValidMove(int col) {
		if (col < 0 || col >= COLS) {
			return false;
		}
		
		for (int row = ROWS - 1; 0 <= row; row--) {
			if (field[row][col] == Stone.None) {
				return true;
			}
		}
		return false;
	}


	@Override
	public boolean isEmpty(int row, int col) {
		return field[row][col] == Stone.None ? true:false;
	}


	@Override
	public boolean isFull() {
		for(int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				if (field[row][col] == Stone.None) {
					return false;
				}
			}
		}
		return true;
	}


	@Override
	public boolean hasFourConnected(int row, int col) {
		return maxCount(row, col) >= 4 ? true : false;
	}


	@Override
	public int countInRow(int row, int col) {
		//Start with index 1 as the current element counts to it
		int counter = 1;
		Stone reference = getStone(row, col);
		
		//If there reference stone is none, return zero as no connections are there
		if (reference == Stone.None) {
			return 0;
		}
		
		//Check left from given point
		for (int iCol = col - 1; 0 <= iCol; iCol--) {
			if (field[row][iCol] == reference) {
				counter++;
			} else {
				break;
			}
		}
		//Check right from given point
		for (int iCol = col + 1; iCol < COLS; iCol++) {
			if (field[row][iCol] == reference) {
				counter++;
			} else {
				break;
			}
		}

		return counter;
	}


	@Override
	public int countInCol(int row, int col) {
		//Start with index 1 as the current element counts to it
		int counter = 1;
		Stone reference = getStone(row, col);
		
		//If there reference stone is none, return zero as no connections are there
		if (reference == Stone.None) {
			return 0;
		}
		
		for (int iRow = row - 1; 0 <= iRow; iRow--) {
			if (field[iRow][col] == reference) {
				counter++;
			} else {
				break;
			}
		}
		for (int iRow = row + 1; iRow < ROWS; iRow++) {
			if (field[iRow][col] == reference) {
				counter++;
			} else {
				break;
			}
		}
		
		return counter;
	}


	@Override
	public int countInDiagRight(int row, int col) {
		int counter = 1;
		Stone reference = getStone(row, col);
		
		//If there reference stone is none, return zero as no connections are there
		if (reference == Stone.None) {
			return 0;
		}
		
		//Count left diag down
		for (int iRow=row-1, iCol=col+1; 0 <= iRow && iCol < COLS; iRow--, iCol++) {
			if (field[iRow][iCol] == reference) {
				counter++;
			} else {
				break;
			}
		}
		//Count left diag up
		for (int iRow=row+1, iCol=col-1; iRow < ROWS && 0 <= iCol; iRow++, iCol--) {
			if (field[iRow][iCol] == reference) {
				counter++;
			} else {
				break;
			}
		}
		
		return counter;
	}


	@Override
	public int countInDiagLeft(int row, int col) {
		//Start with index 1 as the current element counts to it
		int counter = 1;
		Stone reference = getStone(row, col);
		
		//If there reference stone is none, return zero as no connections are there
		if (reference == Stone.None) {
			return 0;
		}
		
		//Count right diag down
		for (int iRow=row-1, iCol=col-1; 0 <= iRow && 0 <= iCol; iRow--, iCol--) {
			if (field[iRow][iCol] == reference) {
				counter++;
			} else {
				break;
			}
		}
		//Count right diag up
		for (int iRow=row+1, iCol=col+1; iRow < ROWS && iCol < COLS; iRow++, iCol++) {
			if (field[iRow][iCol] == reference) {
				counter++;
			} else {
				break;
			}
		}

		return counter;
	}


	@Override
	public int maxCount(int row, int col) {

		return IntStream.of(
					countInRow(row, col),
					countInCol(row, col),
					countInDiagLeft(row, col),
					countInDiagRight(row, col))
				.max()
				.orElse(0);
	}
	
	
}