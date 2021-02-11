package at.jku.tk.mms.img.filters;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Properties;

import at.jku.tk.mms.img.FilterInterface;

/** Filter that implements image thresholding */
public class Threshold implements FilterInterface {


	@Override
	public Image runFilter(BufferedImage image, Properties settings) {
		int threshold = Integer.parseInt(settings.getProperty("threshold"));		
		int imgWidth = image.getWidth();
		int imgHeight = image.getHeight();
		
		BufferedImage newImg = new BufferedImage(imgWidth, imgHeight, image.getType());
		
		for (int i=0; i < imgWidth; i++) {
			for (int k=0; k < imgHeight; k++) {
				Color pxcol = new Color(image.getRGB(i, k));
				int avg = Math.round((pxcol.getRed() + pxcol.getGreen() + pxcol.getBlue()) / 3);
				
				if (avg > threshold) {
					newImg.setRGB(i, k, new Color(255, 255, 255).getRGB());
				} else {
					newImg.setRGB(i, k, new Color(0, 0, 0).getRGB());
				}
			}
		}
		
		return newImg;
	}

	@Override
	public String[] mandatoryProperties() {
		return new String[] { "threshold:n:0-255:128" };
	}

	@Override
	public String toString() {
		return "Threshold Filter";
	}

}
