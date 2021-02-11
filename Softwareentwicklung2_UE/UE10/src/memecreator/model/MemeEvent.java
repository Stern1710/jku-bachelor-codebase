package memecreator.model;

import java.util.EventObject;

public class MemeEvent extends EventObject {
	private final Meme meme;
	
	public MemeEvent(Object source, Meme meme) {
		super(source);
		this.meme = meme;
	}

	public Meme getMeme() {
		return meme;
	}
}
