package at.jku.tk.mms.img.filters;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Properties;

import at.jku.tk.mms.img.FilterInterface;

/** Just to show correct handles in ComboBox */
public class SepiaFilter implements FilterInterface{

	@Override
	public Image runFilter(BufferedImage image, Properties settings) {
		int imgHeight = image.getHeight();
		int imgWidth = image.getWidth();
		
		BufferedImage newImg = new BufferedImage(imgWidth, imgHeight, image.getType());
		
		for (int i=0; i < imgWidth; i++) {
			for (int k=0; k < imgHeight; k++) {
				Color pxcol = new Color(image.getRGB(i, k));
				int sepiaRed = range(0, 255, (int)Math.round((pxcol.getRed() * 0.393) + (pxcol.getGreen() * 0.769) + (pxcol.getBlue() * 0.189)));
				int sepiaGreen = range(0, 255, (int)Math.round(pxcol.getRed() * 0.349 + pxcol.getGreen() * 0.686 + pxcol.getBlue() * 0.168));
				int sepiaBlue = range(0, 255, (int)Math.round(pxcol.getRed() * 0.272 + pxcol.getGreen() * 0.534 + pxcol.getBlue() * 0.131));
								
				newImg.setRGB(i, k, new Color(sepiaRed, sepiaGreen, sepiaBlue).getRGB());
			}
		}
		return newImg;
	}

	/**
	 * Private helper methods that checks if value is in range
	 * If it is lower or higher, the coresponding bound is returned, else the value
	 * @param lower the lower bound
	 * @param upper the upper bound
	 * @param value Value that should be checked
	 * @return
	 */
	private int range (int lower, int upper, int value) {
		if (value > upper) {
			return upper;
		} else if (value < lower) {
			return lower;
		}
		
		return value;
	}
	
	@Override
	public String[] mandatoryProperties() {
		return new String[] { };
	}

	@Override
	public String toString() {
		return "Sepia Filter";
	}

}
