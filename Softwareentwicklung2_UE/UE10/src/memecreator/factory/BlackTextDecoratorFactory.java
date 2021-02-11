package memecreator.factory;

import java.awt.Color;

import memecreator.decorator.TextDecorator;
import memecreator.model.Meme;

public enum BlackTextDecoratorFactory implements TextDecoratorFactory {
	INSTANCE;
	
	@Override
	public Meme createTextDecorator(Meme meme, String text, boolean top) {
		return new TextDecorator(meme, text, top, Color.BLACK, 0, 0);
	}

	@Override
	public Meme createShadowTextDecorator(Meme meme, String text, boolean top) {
		return createTextDecorator(new TextDecorator(meme, text, top, Color.WHITE, 2, 2), text, top);
	}

}
