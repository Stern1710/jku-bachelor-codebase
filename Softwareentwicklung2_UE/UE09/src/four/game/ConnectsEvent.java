package four.game;

import java.util.EventObject;

@SuppressWarnings("serial")
public class ConnectsEvent extends EventObject {

	private final Stone[][] field;
	
	public ConnectsEvent(Object source, Stone[][] field) {
		super(source);
		this.field = field;
	}
	
	public Stone[][] getField() {
		return field;
	}

}
