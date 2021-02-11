package four.game;

import java.awt.Color;

public enum Stone {
	
	None, RED, BLUE; 
	
	private final Color X_COLOR = Color.RED; 
	private final Color O_COLOR = new Color(110, 110, 255); 
	private final String X_COLOR_STRING = "RED"; 
	private final String O_COLOR_STRING = "BLUE"; 
	
	public String outputString() {
		if (this == RED) {
			return X_COLOR_STRING; 
		} else if (this == BLUE){
			return O_COLOR_STRING; 
		} else {
			return " - "; 
		}
	}

	public Stone otherStone() {
		if (this == RED) {
			return BLUE; 
		} else if (this == BLUE){
			return RED; 
		} else {
			return None; 
		}
	}
	
	public Color getColor() {
		if (this == RED) {
			return X_COLOR; 
		} else if (this == BLUE){
			return O_COLOR; 
		} else {
			return Color.WHITE; 
		}
	}
	
}
