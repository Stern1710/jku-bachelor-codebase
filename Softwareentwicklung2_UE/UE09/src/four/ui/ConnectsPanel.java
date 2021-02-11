package four.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import four.game.ConnectsEvent;
import four.game.ConnectsListener;
import four.game.Game;

@SuppressWarnings("serial")
public class ConnectsPanel extends JPanel {
	private final Game game;
	private final int CIRCLESIZE; //Defines the diameter of the circle
	private final int DIMENSION = 400;
	
	public ConnectsPanel(Game game) {
		this.game = game;
		CIRCLESIZE = (DIMENSION / Game.COLS);
		this.setPreferredSize(new Dimension(DIMENSION, DIMENSION));
		this.addMouseListener(new ClickedListener());
		game.addConnectsListener(new ConnectsListener() {
			
			@Override
			public void stoneAdded(ConnectsEvent e) {
				repaint();
			}
			
			@Override
			public void fieldReset(ConnectsEvent e) {
				repaint();
			}
		});
	}

	private class ClickedListener extends MouseAdapter {
		
		@Override
		public void mouseClicked(MouseEvent e) {
			game.setStone(e.getX() / CIRCLESIZE, game.getCurrent());
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
				
		int rowOffset = 0;
		int colOffset = 0;
		for (int row=0; row < Game.ROWS; row++) {
			for (int col=0; col < Game.COLS; col++) {
				g.setColor(game.getStone(row, col).getColor());
				g.fillOval(col+colOffset, row+rowOffset, CIRCLESIZE, CIRCLESIZE);
				colOffset += CIRCLESIZE - 1;
			}
			rowOffset += CIRCLESIZE - 1;
			colOffset = 0;
		}
	}
}
