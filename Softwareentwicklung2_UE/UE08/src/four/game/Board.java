package four.game;

public interface Board {
	/**
	 * Prints the board.
	 */
	public void print();
	
	/** 
	 * Retrieves the Stone on position (row|col).
	 * 
	 * @return the Stone (X, O, None) found in the cell
	 */
	public Stone getStone(int row, int col);

	/** 
	 * Drops a stone into a column.
	 * 
	 * @return The index of the row the stone has landed or -1 if the move is not valid
	 */
	public int setStone(int col, Stone stone);

	/**
	 * Checks if a move is valid, i.e. a stone can be dropped in column col.
	 */
	public boolean isValidMove(int col);

	/**
	 * Checks if a cell is empty, i.e. it contains Stone.None .
	 */
	public boolean isEmpty(int row, int col);
	
	/**
	 * Checks if the board is full.
	 */
	public boolean isFull();

	/**
	 * Checks if starting from a given field, 
	 * four equal stones are aligned vertically, horizontally or diagonally
	 */
	public boolean hasFourConnected(int row, int col);

	/**
	 * Counts starting from a given stone the number of equal stones
	 * which are horizontally aligned
	 */
	public int countInRow(int row, int col);

	/**
	 * Counts starting from a given stone the number of equal stones
	 * which are vertically aligned
	 */
	public int countInCol(int row, int col);

	/**
	 * Counts starting from a given stone the number of equal stones
	 * which are right diagonally aligned
	 */
	public int countInDiagRight(int row, int col);

	/**
	 * Counts starting from a given stone the number of equal stones
	 * which are left diagonally aligned
	 */
	public int countInDiagLeft(int row, int col);

	/**
	 * Counts starting from a given stone the maximum number of equal stones
	 * in any alignment
	 */
	public int maxCount(int row, int col);
}
