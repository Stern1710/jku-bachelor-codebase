package memecreator.model;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MemeImpl implements Meme {
	
	private final String name;
	private final BufferedImage image;
	
	public MemeImpl(String name, BufferedImage image) {
		this.name = name;
		this.image = image;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public BufferedImage getImage() {
		return image;
	}
	
	@Override
	public void paint(Graphics g, int width, int height) {
		g.drawImage(image, 0, 0, width, height, null);
	}
	
}
