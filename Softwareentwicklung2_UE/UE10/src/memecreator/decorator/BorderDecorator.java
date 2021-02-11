package memecreator.decorator;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import memecreator.model.Meme;

public class BorderDecorator extends MemeDecorator {
	private final int thickness;
	private final Color color;
	
	public BorderDecorator(Meme meme, int thickness, Color color) {
		super(meme);
		this.thickness = thickness;
		this.color = color;
	}

	@Override
	public void paint(Graphics g, int width, int height) {
		getMeme().paint(g, width, height);
		
		Graphics2D g2 = (Graphics2D)g;
		g2.setStroke(new BasicStroke(thickness));
		g2.setColor(color);
		g2.drawRect(0, 0, width, height);
	}

}
