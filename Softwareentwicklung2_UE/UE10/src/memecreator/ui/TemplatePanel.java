package memecreator.ui;

import memecreator.model.Meme;
import memecreator.model.MemeEvent;
import memecreator.model.MemeListener;
import memecreator.model.MemeModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class TemplatePanel extends JPanel {
	
	private final CreationPanel creationPanel;
	
	TemplatePanel(MemeModel model, CreationPanel creationPanel) {
		this.creationPanel = creationPanel;
		setLayout(new FlowLayout(FlowLayout.LEFT));
		model.getMemes().forEach(this::addMemePanel);
		model.addMemeListener(new MemeListener() {
			@Override
			public void memeAdded(MemeEvent e) {
				new MemePanel(e.getMeme()).repaint();
			}
		});
	}
	
	private void addMemePanel(Meme meme) {
		MemePanel memePanel = new MemePanel(meme);
		add(memePanel);
		memePanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				creationPanel.setMeme(meme);
			}
		});
		validate();
	}
	
}
