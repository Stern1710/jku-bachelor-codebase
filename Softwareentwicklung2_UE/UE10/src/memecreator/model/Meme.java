package memecreator.model;

import java.awt.*;
import java.awt.image.BufferedImage;

public interface Meme {
	
	String getName();
	
	BufferedImage getImage();
	
	void paint(Graphics g, int width, int height);
	
}
