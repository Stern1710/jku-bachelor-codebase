package four.game;

import four.player.Player;
import inout.In;
import inout.Out;

public class Game {

	private Board board; 
	private GameState gameState; 
	private Player xPlayer; 
	private Player oPlayer; 
	private Player current; 
	
	
	public Game(Player xPlayer, Player oPlayer) {
		super();
		board = new BoardImpl();
		this.xPlayer = xPlayer;
		this.oPlayer = oPlayer;
	}
	
	public void play() {
		Out.print("Which players starts? Enter x for xPlayer, otherwise oPlayer starts: ");
		char starter = In.readChar();
		
		if (starter == 'x' || starter == 'X') {
			Out.println("xPlayer starts!");
			gameState = GameState.XsTurn;
			current = xPlayer;
		} else {
			Out.println("oPlayer starts!");
			gameState = GameState.OsTurn;
			current = oPlayer;
		}
		
		board.print();
		Out.println("GameState: " + gameState.toString());
		
		while (gameState == GameState.OsTurn || gameState == GameState.XsTurn) {
			int col = current.getMove(board);
			int row = board.setStone(col, current.stone);
			board.print();
			
			if (board.hasFourConnected(row, col)) {
				gameState = (current == xPlayer) ? GameState.XWon : GameState.OWon;
				Out.println("Congratulations! " + current.name + " has WON the game!");
			} else if (board.isFull()) {
				gameState = GameState.Draw;
				Out.println("There is a draw! Game has ended without a winner!");
			} else {
				gameState = (current == xPlayer) ? GameState.OsTurn : GameState.XsTurn;
				current = (current == xPlayer) ? oPlayer : xPlayer;
			}
			
			Out.println("GameState: " + gameState.toString());
		}
	}

}
