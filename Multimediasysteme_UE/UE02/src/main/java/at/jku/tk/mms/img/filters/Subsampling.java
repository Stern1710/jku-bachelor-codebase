package at.jku.tk.mms.img.filters;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Properties;

import at.jku.tk.mms.img.FilterInterface;

/** Perform sub sampling on the image */
public class Subsampling implements FilterInterface {

	@Override
	public Image runFilter(BufferedImage image, Properties settings) {
		int rate = Integer.parseInt(settings.getProperty("rate"));
		int imgWidth  = image.getWidth();
		int imgHeight = image.getHeight();
				
		BufferedImage bi = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_ARGB);
		
		//Run trough the picture first in lines
		for (int i=0; i < imgWidth; i = i+rate) {
			for (int k=0; k < imgHeight; k = k+rate) {
				//Get color if first upper left pixel in rate*rate area
				Color pxcol = new Color(image.getRGB(i, k));
				
				//Put color of 1st pixel in rate*rate area on every other pixel in that place
				for (int m=0; m < rate; m++) {
					for (int n=0; n < rate; n++) {
						//Check if placing of color is out of image bounds
						if (i+m < imgWidth && k+n < imgHeight) {
							bi.setRGB(i+m, k+n, pxcol.getRGB());
						}
					}
				}
			}
		}
		
		return bi;
	}

	@Override
	public String[] mandatoryProperties() {
		return new String [] { "rate:n:1-8:2" };
	}
	
	@Override
	public String toString() {
		return "subsampling";
	}
}
