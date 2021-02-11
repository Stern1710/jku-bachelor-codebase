package four.player;

import four.game.Board;
import four.game.Stone;

public abstract class Player {
	
	public final String name; 
	public final Stone stone; 
	
	public Player(String name, Stone stone) {
		super();
		this.name = name;
		this.stone = stone; 
	}

	abstract public int getMove(Board board); 

}
