package four;

import four.game.Game;
import four.game.Stone;
import four.player.HumanPlayer;

public class ConnectFourGameMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Game game = new Game(new HumanPlayer("X", Stone.X), new HumanPlayer("O", Stone.O)); 
		game.play(); 
	}

}
