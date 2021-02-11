package memecreator.ui;

import memecreator.decorator.BorderDecorator;
import memecreator.factory.BlackTextDecoratorFactory;
import memecreator.factory.TextDecoratorFactory;
import memecreator.factory.WhiteTextDecoratorFactory;
import memecreator.model.Meme;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

class CreationPanel extends JPanel {
	
	private Meme meme;
	private TextDecoratorFactory textDecoratorFactory = WhiteTextDecoratorFactory.getInstance();
	private int memeWidth = 400;
	private int memeHeight = 400;
	
	CreationPanel() {
		setLayout(new BorderLayout());
		JPanel panelOptions = new JPanel();
		panelOptions.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		
		JTextField textFieldMemeWidth = new JTextField(String.valueOf(memeWidth));
		textFieldMemeWidth.addActionListener(ae -> {
			try {
				memeWidth = Integer.parseInt(textFieldMemeWidth.getText());
				repaint();
			} catch (NumberFormatException e) {
				// do nothing (users have to figure out on their own that they messed up)
			}
		});
		textFieldMemeWidth.setPreferredSize(new Dimension(50, textFieldMemeWidth.getPreferredSize().height));
		panelOptions.add(new JLabel("Width:"));
		panelOptions.add(textFieldMemeWidth);
		
		JTextField textFieldMemeHeight = new JTextField(String.valueOf(memeHeight));
		textFieldMemeHeight.addActionListener(ae -> {
			try {
				memeHeight = Integer.parseInt(textFieldMemeHeight.getText());
				repaint();
			} catch (NumberFormatException e) {
				// do nothing (users have to figure out on their own that they messed up)
			}
		});
		textFieldMemeHeight.setPreferredSize(new Dimension(50, textFieldMemeHeight.getPreferredSize().height));
		panelOptions.add(new JLabel("Height:"));
		panelOptions.add(textFieldMemeHeight);
		
		JCheckBox checkBoxBlackText = new JCheckBox("Use black text");
		checkBoxBlackText.addActionListener(ae -> {
			textDecoratorFactory = (checkBoxBlackText.isSelected() ? BlackTextDecoratorFactory.INSTANCE : WhiteTextDecoratorFactory.getInstance());
		});
		panelOptions.add(checkBoxBlackText);
		
		JCheckBox checkBoxUseShadowedText = new JCheckBox("Use shadowed text");
		panelOptions.add(checkBoxUseShadowedText);
		
		JButton buttonAddTopText = new JButton("Add top text");
		buttonAddTopText.addActionListener(ae -> {
			if (meme != null) {
				String text = JOptionPane.showInputDialog("Enter top text:");
				Meme decoratedMeme = checkBoxUseShadowedText.isSelected() ?
						textDecoratorFactory.createShadowTextDecorator(meme, text, true) :
						textDecoratorFactory.createTextDecorator(meme, text, true);
				setMeme(decoratedMeme);
			}
		});
		panelOptions.add(buttonAddTopText);
		
		JButton buttonAddBottomText = new JButton("Add bottom text");
		buttonAddBottomText.addActionListener(ae -> {
			if (meme != null) {
				String text = JOptionPane.showInputDialog("Enter bottom text:");
				Meme decoratedMeme = checkBoxUseShadowedText.isSelected() ?
						textDecoratorFactory.createShadowTextDecorator(meme, text, false) :
						textDecoratorFactory.createTextDecorator(meme, text, false);
				setMeme(decoratedMeme);
			}
		});
		panelOptions.add(buttonAddBottomText);
		
		JButton buttonAddBorder = new JButton("Add border");
		buttonAddBorder.addActionListener(ae -> {
			if (meme != null) {
				Meme decoratedMeme = new BorderDecorator(meme, 2, Color.BLACK);
				setMeme(decoratedMeme);
			}
		});
		panelOptions.add(buttonAddBorder);
		
		add(panelOptions, BorderLayout.SOUTH);
	}
	
	BufferedImage getCreatedMemeImage() {
		if (meme != null) {
			BufferedImage image = new BufferedImage(memeWidth, memeHeight, BufferedImage.TYPE_INT_ARGB);
			meme.paint(image.getGraphics(), memeWidth, memeHeight);
			return image;
		}
		return null;
	}
	
	void setMeme(Meme meme) {
		this.meme = meme;
		repaint();
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.translate(getWidth() / 2 - memeWidth / 2, 10);
		if (meme != null) {
			meme.paint(g, memeWidth, memeHeight);
		}
	}
	
}
