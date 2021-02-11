package memecreator.factory;

import memecreator.model.Meme;

public interface TextDecoratorFactory {
	
	Meme createTextDecorator(Meme meme, String text, boolean top);
	
	Meme createShadowTextDecorator(Meme meme, String text, boolean top);
	
}
