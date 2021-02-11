package at.jku.tk.mms.img;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Properties;

/**
 * Controls the Image Filter app
 * 
 * @author Tom
 */
public class ImageFilterCtrl {

	private ImagePanel source, target;
	
	private BufferedImage scaled;
	
	private Image filtered;
	
	/** Creates new controller */
	public ImageFilterCtrl(ImagePanel source, ImagePanel target) {
		this.source = source;
		this.target = target;
	}
	
	/** Loads a new Image into this controller and displays it in the source panel */
	public void loadImage(BufferedImage img) {
		scaled = Tools.scale(img, ImageFilterApp.PANEL_SIZES.width, ImageFilterApp.PANEL_SIZES.height, 90);
		source.loadImage(scaled);
		target.loadImage(scaled);
	}
	
	/** Applies a filter to the loaded source image and shows the generated image in the target panel */
	public void applyFilter(FilterInterface f, Properties p) {
		filtered = f.runFilter(scaled, p);
		target.loadImage(filtered);
	}

	/** Sets filtered image as source image in order to apply further filters */
	public void setTargetAsSource() {
		scaled = target.getBufferedImage();
		source.loadImage(scaled);
	}
	
}
