package at.jku.tk.mms.img.filters;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Properties;

import at.jku.tk.mms.img.FilterInterface;
import at.jku.tk.mms.img.pixels.Pixel;

/** Apply sub sampling of color values only */
public class ChromaSubsamling implements FilterInterface {

	@Override
	public Image runFilter(BufferedImage image, Properties settings) {
		int horizontal = Integer.parseInt(settings.getProperty("horizontal"));
		int vertical = Integer.parseInt(settings.getProperty("vertical"));
		int imgWidth = image.getWidth();
		int imgHeight = image.getHeight();
				
		BufferedImage subsampled = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_ARGB);
		
		for (int i=0; i < imgWidth; i=i+horizontal) {
			for (int k=0; k < imgHeight; k = k+vertical) {
				Pixel px = new Pixel(image.getRGB(i, k));
								
				for (int m=0; m < horizontal; m++) {
					for (int n=0; n < vertical; n++) {
						//Check if placing of color is out of image bounds
						if (i+m < imgWidth && k+n < imgHeight) {
							Color tempcol = new Color(image.getRGB(i+m, k+n));
							Pixel tempxp = new Pixel(tempcol.getRed(), tempcol.getGreen(), tempcol.getBlue(), 255);
							
							tempxp.setCb(px.getCb());
							tempxp.setCr(px.getCr());
							subsampled.setRGB(i+m, k+n, tempxp.getRawRGBA());
						}
					}
				}
				
			}
		}
		
		return subsampled;
	}

	@Override
	public String[] mandatoryProperties() {
		return new String [] { "horizontal:s:1-8:2", "vertical:n:1-8:2" };
	}
	
	@Override
	public String toString() {
		return "subsampling - chroma";
	}

}
