package memecreator.ui;

import memecreator.model.Meme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class MemePanel extends JPanel {
	
	private static final int WIDTH = 150;
	private static final int HEIGHT = 150;
	private static final int BORDER_THICKNESS = 2;
	
	private final Meme meme;
	
	MemePanel(Meme meme) {
		this.meme = meme;
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setOpaque(false);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, BORDER_THICKNESS));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setBorder(null);
			}
		});
		setToolTipText(meme.getName());
	}
	
	@Override
	public void paint(Graphics g) {
		meme.paint(g, getWidth(), getHeight());
		super.paint(g);
	}
	
}
