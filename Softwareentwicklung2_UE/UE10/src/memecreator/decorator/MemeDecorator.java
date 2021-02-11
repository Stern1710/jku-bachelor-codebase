package memecreator.decorator;

import java.awt.image.BufferedImage;

import memecreator.model.Meme;

public abstract class MemeDecorator implements Meme {
	private final Meme meme;
	
	public MemeDecorator (Meme meme) {
		this.meme = meme;
	}

	protected Meme getMeme() {
		return meme;
	}
	
	@Override
	public String getName() {
		return meme.getName();
	}

	@Override
	public BufferedImage getImage() {
		return meme.getImage();
	}
}
