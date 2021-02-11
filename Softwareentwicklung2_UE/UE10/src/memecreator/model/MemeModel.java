package memecreator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MemeModel {
	
	private final List<Meme> memes = new ArrayList<>();
	private final List<MemeListener> listeners = new ArrayList<>();
	
	public void addMeme(Meme meme) {
		memes.add(meme);
		for (MemeListener ml : listeners) {
			ml.memeAdded(new MemeEvent(this, meme));
		}
	}
	
	public List<Meme> getMemes() {
		return Collections.unmodifiableList(memes);
	}
	
	public void addMemeListener(MemeListener listener) {
		listeners.add(listener);
	}
	
	public void removeMemeListener(MemeListener listener) {
		listeners.remove(listener);
	}
	
}
