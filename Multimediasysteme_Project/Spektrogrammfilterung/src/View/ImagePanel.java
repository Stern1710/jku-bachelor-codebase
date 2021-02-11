package View;

import java.awt.Image;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This ImagePanel (extending JPanel) holds the picture, each of the pictures has its own panel
 * @author Christopher Holzweber
 */
@SuppressWarnings("serial")
public class ImagePanel extends JPanel {

	private final int PANELHEIGHT = 500;
	private final int PANELWIDTH = 500;
	private Image newimg;
	private JLabel imgLabel;
	
	ImagePanel() {
		imgLabel = new JLabel();
		imgLabel.setSize(PANELWIDTH, PANELHEIGHT);
		add(imgLabel);
		setSize(PANELWIDTH, PANELHEIGHT);
		setVisible(true);
	}
	
	/**
	 * Displays a image in a File-object
	 * @param f File with the image information inside
	 */
	void setImage(File f) {
		setImage(f.toString());
	}
	
	/**
	 * Sets the image using the path to the image file
	 * @param path Absolute or relative path to the image file
	 */
	void setImage(String path) {
		setImage(new ImageIcon(path).getImage());
	}
	
	/**
	 * Sets the image to the panel position
	 * @param img Image to set
	 */
	void setImage(Image img) {
		newimg = img.getScaledInstance(PANELWIDTH, PANELHEIGHT, java.awt.Image.SCALE_SMOOTH); //autosize with SCALE:SMOOTH
		imgLabel.setIcon(new ImageIcon(newimg));
		repaint();
	}
	
	Image getImage() {
		return newimg;
	}

}
