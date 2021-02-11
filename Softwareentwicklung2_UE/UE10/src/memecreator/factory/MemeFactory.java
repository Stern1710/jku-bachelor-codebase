package memecreator.factory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import memecreator.model.Meme;
import memecreator.model.MemeImpl;

public class MemeFactory {
	public static Meme crateMemeFromFile(File file) {
		BufferedImage memeImg = null;
		
		try {
			memeImg = ImageIO.read(file);
		} catch (IOException e) {
			return null;
		}
		
		return new MemeImpl("Imported Meme", memeImg);
	}
}
