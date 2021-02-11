package memecreator.factory;

import java.awt.Color;

import memecreator.decorator.TextDecorator;
import memecreator.model.Meme;

public class WhiteTextDecoratorFactory implements TextDecoratorFactory {
	private static WhiteTextDecoratorFactory factory;
	
	public static WhiteTextDecoratorFactory getInstance() {
		if (factory == null) {
			factory = new WhiteTextDecoratorFactory();
		}
		return factory;
	}
	
	private WhiteTextDecoratorFactory() {
		super();
	}
	
	@Override
	public Meme createTextDecorator(Meme meme, String text, boolean top) {
		return new TextDecorator(meme, text, top, Color.WHITE, 0, 0);
	}

	@Override
	public Meme createShadowTextDecorator(Meme meme, String text, boolean top) {
		return createTextDecorator(new TextDecorator(meme, text, top, Color.BLACK, 2, 2), text, top);
	}
}
