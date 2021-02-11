package at.jku.tk.mms.img.filters;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Properties;

import at.jku.tk.mms.img.FilterInterface;

/** Just to show correct handles in ComboBox */
public class GreyScaleFilter implements FilterInterface{
	
	@Override
	public Image runFilter(BufferedImage image, Properties settings) {
		int imgHeight = image.getHeight();
		int imgWidth = image.getWidth();
		
		BufferedImage newImg = new BufferedImage(imgWidth, imgHeight, image.getType());
		
		//Round through the image in columns
		for (int i=0; i < imgWidth; i++) {
			for (int k=0; k < imgHeight; k++) {
				//Get the color, calculate average and write it back as RGB into the new Image
				Color pxcol = new Color(image.getRGB(i, k));
				int avg = Math.round((pxcol.getRed() + pxcol.getGreen() + pxcol.getBlue()) / 3);
				newImg.setRGB(i, k, new Color(avg, avg, avg).getRGB());
			}
		}
		return newImg;
	}

	@Override
	public String[] mandatoryProperties() {
		return new String[] { };
	}

	@Override
	public String toString() {
		return "Grey Filter";
	}

}
