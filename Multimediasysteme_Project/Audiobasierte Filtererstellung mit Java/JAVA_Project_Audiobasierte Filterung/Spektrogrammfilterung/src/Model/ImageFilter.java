package Model;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * This class provides the filter method; Picture will change its saturation for each pixel according to the spectrogram
 * The higher the Amplitude, also the pictures saturation will increase and vice versa.
 * If green in Spektrogram, MINIMUM amplitude is reached so the originalpixel will be reused.
 * @author Christopher Holzweber
 */
public class ImageFilter {
	
	//This method filters the original image with the spektrogram
	public BufferedImage filter(BufferedImage spektrogram,BufferedImage origImage) {
		
		//BufferedImage is copy of passed image, but with RGB color space
		BufferedImage bf = new BufferedImage(origImage.getWidth(),origImage.getHeight(),BufferedImage.TYPE_INT_RGB);
		
		/*
		 * HSV Converter for RGB to HSV using Color-Class
		 * @see https://stackoverflow.com/questions/2399150/convert-rgb-value-to-hsv
		 * @see https://docs.oracle.com/javase/1.5.0/docs/api/java/awt/Color.html#RGBtoHSB(int,%20int,%20int,%20float[])
		 * Look up documentation about HSV, it is possible to set saturation directly
		 * Go over each pixel and the the new pixel in BufferedImage bf, which will be returned in the end.
		 */
		
		for(int i = 0;i<origImage.getHeight();i++) { //iterate over whole picture
			for(int j = 0;j<origImage.getWidth();j++) {
				
				//get the values of the spectrogram
				Color spekcolor = new Color(spektrogram.getRGB(j, i));
				float[] spekhsbvals = new float[3]; //storage for HSB VALUES
				Color.RGBtoHSB(spekcolor.getRed(), spekcolor.getGreen(), spekcolor.getBlue(), spekhsbvals);
				
				Color origcolor = new Color(origImage.getRGB(j, i));
				float[] orighsbvals = new float[3]; //store HSB VALUES
				Color.RGBtoHSB(origcolor.getRed(), origcolor.getGreen(), origcolor.getBlue(), orighsbvals);

				//If pixel is green, orignalpictures stays the same
				if((spekhsbvals[2]>0.4) && (spekhsbvals[2]<=0.65)) { //MIDDLE amplitude YELLOW
					orighsbvals[1] = (float) 0.5;
				}
				if((spekhsbvals[2]>0.65 && spekhsbvals[2]<=0.8)) { //NEARLY HIGH amplitude ORANGE
					orighsbvals[1] = (float) 0.8;
					orighsbvals[2] = (float) 0.6;
				}
				if((spekhsbvals[2]>0.8)) { //HIGH amplitude RED
					orighsbvals[1] = (float) 1.0;
					orighsbvals[2] = (float) 1.0;
				}
				
				bf.setRGB(j, i, Color.HSBtoRGB(orighsbvals[0], orighsbvals[1], orighsbvals[2]));
			}
		}
		return bf;
	}
}
