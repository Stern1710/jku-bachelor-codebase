package four.game;

public enum Stone {
	
	None, X, O; 
	
	public String outputString() {
		if (this == None) {
			return " - "; 
		} else {
			return " " + this.toString() + " "; 
		}
	}

	public Stone otherStone() {
		if (this == X) {
			return O; 
		} else if (this == O){
			return X; 
		} else {
			return None; 
		}
	}

}
