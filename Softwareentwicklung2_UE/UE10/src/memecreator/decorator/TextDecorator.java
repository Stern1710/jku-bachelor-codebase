package memecreator.decorator;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import memecreator.model.Meme;

public class TextDecorator extends MemeDecorator{
	private final String text;
	private final boolean top;
	private final Color color;
	private final int xShift;
	private final int yShift;
	
	public TextDecorator(Meme meme, String text, boolean top, Color color, int xShift, int yShift) {
		super(meme);
		this.text = text;
		this.top = top;
		this.color = color;
		this.xShift = xShift;
		this.yShift = yShift;
	}

	@Override
	public void paint(Graphics g, int width, int height) {
		getMeme().paint(g, width, height);
	
		g.setColor(color);
		g.setFont(new Font("Arial", Font.BOLD, 20));
		FontMetrics metrics = g.getFontMetrics();
		
		int x = width/2 - (metrics.stringWidth(text)/2);
		int y = top ? metrics.getHeight() : height - metrics.getHeight();
		
		g.drawString(text, x + xShift, y + yShift);
	}

}
