package four.game;

import java.util.ArrayList;
import java.util.List;

public class GameImpl implements Game {

	private GameState gameState; 
	private Stone xPlayer; 
	private Stone oPlayer; 
	private Stone current; 
	private Stone[][] field; 
	private final List<ConnectsListener> listeners;

	public GameImpl() {
		super();
		this.listeners = new ArrayList<ConnectsListener>();
		init();
	}
	
	@Override
	public void init() {
		setGameState(GameState.REDsTurn); 
		xPlayer = Stone.RED; 
		oPlayer = Stone.BLUE; 
		current = xPlayer; 
		initBoard();
	}

	@Override
	public Stone getFrstPlayer() {
		return xPlayer; 
	}
	
	@Override
	public Stone getScndPlayer() {
		return oPlayer; 
	}	

	@Override
	public Stone getCurrent() {
		return current; 
	}
	
	@Override
	public GameState getGameState() {
		return gameState; 
	}
	
	@Override
	public Stone getStone(int row, int col) {
		if (row < 0 || row >= ROWS || col < 0 || col >= COLS) {
			return Stone.None; 
		}
		return field[row][col]; 
	}

	@Override
	public int setStone(int col, Stone stone) {
		if (!isValidMove(col)) {
			throw new IllegalArgumentException("Illegal move!");
		}
		int row = nextFreeColRow(col); 
		assert row >= 0 && row < ROWS; 
		setStone(row, col, stone); 
		updateGameState(row, col); 
		fireStoneAdded(field);
		return row; 
	}

	@Override	
	public boolean isValidMove(int col) {
		return col >= 0 && col < COLS && isEmpty(0, col) && !isGameOver(); 
	}

	@Override
	public void addConnectsListener (ConnectsListener c) {
		listeners.add(c);
	}
	
	@Override
	public void removeConnectsListener (ConnectsListener c) {
		listeners.remove(c);
	}
	
	@Override
	public void reset() {
		init();
		fireNewField(field);
	}
	
	// private section -----------------------

	private boolean isGameOver() {
		return gameState == GameState.Draw || gameState == GameState.BLUEWon || gameState == GameState.REDWon;
	}
	
	private boolean isFull() {
		for (int r = 0; r < ROWS; r++) {
			for (int c = 0; c < COLS; c++) {
				if (getStone(r, c) == Stone.None) {
					return false; 
				}
			}
		}
		return true; 
	}

	private void setGameState(GameState gameState) {
		this.gameState = gameState;
	}
	
	private void initBoard() {
		field = new Stone[ROWS][COLS];
		for (int r = 0; r < ROWS; r++) {
			for (int c = 0; c < COLS; c++) {
				setStone(r, c, Stone.None); 
			}
		}
	}

	private void updateGameState(int row, int col) {
		if (hasFourConnected(row, col)) {
			if (current == Stone.RED) {
				setGameState(GameState.REDWon); 
			} else {
				setGameState(GameState.BLUEWon); 
			}
		} else if (isFull()) {
			setGameState(GameState.Draw); 
		} else {
			changePlayer(); 
		}		
	}

	private int upperColRow(int col) {
		assert col >= 0 && col < COLS;
		int r; 
		for (r = 0; r < ROWS; r++) {
			if (!isEmpty(r, col)) {
				break; 
			}
		}
		return r;
	}

	private void setStone(int row, int col, Stone state) {
		field[row][col] = state; 
	}

	private int nextFreeColRow(int col) {
		return upperColRow(col) - 1;
	}

	private boolean isEmpty(int row, int col) {
		return getStone(row, col) == Stone.None;
	}
	
	private void changePlayer() {
		if (current == Stone.RED) {
			current = oPlayer; 
			setGameState(GameState.BLUEsTurn); 
		} else {
			current = xPlayer; 
			setGameState(GameState.REDsTurn); 
		}
	}

	private boolean hasFourConnected(int row, int col) {
		return maxCount(row, col) >= 4; 
	}
	
	private int countInRow(int row, int col) {
		Stone stone = getStone(row, col); 
		if (stone == Stone.None) {
			return 0; 
		}
		int count = 1; 
		for (int colDir = 1; col + colDir < COLS && getStone(row, col + colDir) == stone; colDir++) {
			count++; 
		}
		for (int colDir = -1; col + colDir >= 0 && getStone(row, col + colDir) == stone; colDir--) {
			count++; 
		}
		return count; 
	}

	private int countInCol(int row, int col) {
		Stone stone = getStone(row, col); 
		if (stone == Stone.None) {
			return 0; 
		}
		int count = 1; 
		for (int rowDir = 1; row + rowDir < ROWS && getStone(row + rowDir, col) == stone; rowDir++) {
			count++; 
		}
		for (int rowDir = -1; row + rowDir >= 0 && getStone(row + rowDir, col) == stone; rowDir--)  {
			count++; 
		}
		return count; 	
	}

	private int countInDiagRight(int row, int col) {
		Stone stone = getStone(row, col); 
		if (stone == Stone.None) {
			return 0; 
		}
		int count = 1; 
		for (int dir = 1; row + dir < ROWS && col + dir < COLS && getStone(row + dir, col + dir) == stone; dir++) {
			count++; 
		}
		for (int dir = -1; row + dir >= 0 && col + dir >= 0 && getStone(row + dir, col + dir) == stone; dir--)  {
			count++; 
		}
		return count; 	
	}

	private int countInDiagLeft(int row, int col) {
		Stone stone = getStone(row, col); 
		if (stone == Stone.None) {
			return 0; 
		}
		int count = 1; 
		for (int dir = 1; row + dir < ROWS && col + dir >= 0 && getStone(row + dir, col - dir) == stone; dir++) {
			count++; 
		}
		for (int dir = -1; row + dir >= 0 && col + dir < COLS && getStone(row + dir, col - dir) == stone; dir--)  {
			count++; 
		}
		return count; 	
	}

	private int maxCount(int row, int col) {
		int count = countInCol(row, col); 
		int max = count; 
		count = countInRow(row, col); 
		if (count > max) {
			max = count; 
		}
		count = countInDiagLeft(row, col); 
		if (count > max) {
			max = count; 
		}
		count = countInDiagRight(row, col); 
		if (count > max) {
			max = count; 
		}
		return  max; 
	}
	
	private void fireStoneAdded(Stone[][] field) {
		ConnectsEvent event = new ConnectsEvent(this, field);
		for (ConnectsListener c : listeners) {
			c.stoneAdded(event);
		}
	}
	
	private void fireNewField(Stone[][] field) {
		ConnectsEvent event = new ConnectsEvent(this, field);
		for (ConnectsListener c : listeners) {
			c.fieldReset(event);
		}
	}
	
}
