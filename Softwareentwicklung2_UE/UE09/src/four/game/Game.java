package four.game;

public interface Game {
	
	public static final int ROWS = 6;
	public static final int COLS = 7;

	public void init();
	
	public Stone getFrstPlayer(); 
	
	public Stone getScndPlayer(); 
	
	public Stone getCurrent(); 
	
	public GameState getGameState(); 
	
	public Stone getStone(int row, int col); 
	
	public int setStone(int col, Stone stone); 
	
	public boolean isValidMove(int col); 
	
	public void addConnectsListener (ConnectsListener c);
	
	public void removeConnectsListener (ConnectsListener c);
	
	public void reset();

}
