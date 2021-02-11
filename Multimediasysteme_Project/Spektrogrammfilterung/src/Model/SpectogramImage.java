package Model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import Model.minim.analysis.*;

/**
 * Creates SpectogrammPicture from an double[] with a given Dimension with  creates an HSB image or an RGB image from the FFT data.
 * Sources
 * @see http://edoc.mpg.de/395068
 * @see https://www.programcreek.com/java-api-examples/index.php?source_dir=Minim-Android-master/src/ddf/minim/analysis/BeatDetect.java
 * @see http://www.antcom.de/~roland.reichwein/studium/diplomarbeit/diplom.pdf
 * @see http://www.dspguide.com/ch12/2.htm
 * @see https://github.com/Cyborg101/AudioVisualization
 * @author Stefan Paukner
 */

public class SpectogramImage {
	private final double[] rawData;
	private final Dimension dim;

	// fixes FFT parameters:
	public static final int WS =1024 ; // WS = window size 1024
	public static final int OF = 8;    // OF = overlap factor 2
	public static final int ZP = 1;    // Zeropadding factor  1

	private boolean converted;
	// amplitude per time and frequency between 0 and 1
	private final double[][] specImageData;
	private final double[][] specData;
	private final double samplingRate;
	private int windowStep;
	private int nX;
	private int nY;

	public SpectogramImage(double[] rawData, double samplingRate, Dimension dim) {
		super();
		this.rawData = rawData;
		this.samplingRate = samplingRate;
		this.dim = dim;
		windowStep = WS/ OF/ ZP;
		nX = (rawData.length-WS)/windowStep;
		nY = WS / 2 + 1 ;
		specImageData = new double[dim.width][dim.height];
		specData = new double[nX][nY];

	}
	/**
	 * gets the SpectogramImage from the rawData
	 * @return BufferedImage in RGB where the spectogram data is set in HSB Colorspace
	 */
	public BufferedImage getSpectogramImage() {
		if (!converted) {
			convert();
		}
		BufferedImage img = null;
		img = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				Color newColor = getColor(1.0-specImageData[x][y]);
				img.setRGB(x, y, newColor.getRGB());
			}
		}
		return img;
	}
	/**
	 * gets the SpectogramImage from the rawData
	 * @return BufferedImage in RGB where the Color red shows the spectogram data
	 */
	public BufferedImage getSpectogramImageRGB() {
		if (!converted) {
			convert();
		}
		BufferedImage img = null;
		img = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				img.setRGB(x, y, ((0xFF) << 24) | 
						(  (  (int) (specImageData[x][y] * 0xFFFF) << 8 & ((0xFF<< 16)) )) ); 
			}
		}
		return img;
	}
	/**
	 * gets Spectogram from the rawData 
	 * @return a double array where the first dim is the time and the 2nd dim is the frequency with the size form the constructor.
	 */
	public double[][] getSpectrogramData() {
		if (!converted) {
			convert();
		}
		// copy resulting byte[][]
		return java.util.Arrays.stream(specImageData).map(el -> el.clone()).toArray(double[][]::new);

	}

	public double[][] getSpecData() {
		return specData;
	}
	/**
	 * does the FFT transformation from rawData into specData.
	 */
	private void convert() {
		double SR = samplingRate;

		//initialize plotData array
		double[][] plotData = new double[nX][nY]; 

		//apply FFT and find MAX and MIN amplitudes
		double maxAmp = Double.MIN_VALUE;
		double maxDataAmp = Double.MIN_VALUE;
		double minAmp = Double.MAX_VALUE;

		FFT fft = new FFT(WS, (int) SR);
		fft.window(new RectangularWindow());
		double[] raw2;
		
		for (int i = 0; i < nX; i++){ 
			raw2 = Arrays.copyOfRange(rawData, i*windowStep, i*windowStep+WS);
			fft.forward(raw2);
			for (int j = 0; j < nY; j++){
				specData [i][j] = fft.getBand(j);
				if (specData[i][j] > maxDataAmp) {
					maxDataAmp = specData[i][j];
				}

				plotData[i][nY-j-1] = 10 * Math.log10(fft.getBand(j)); //to get 0Hz at the bottom
				//find MAX and MIN amplitude
				if (plotData[i][j] > maxAmp)
					maxAmp = plotData[i][j];
				else if (plotData[i][j] < minAmp && plotData[i][j] >= 0)
					minAmp = plotData[i][j];
			}
		}

		//Normalization
		double diff = maxAmp - minAmp;
		for (int i = 0; i < nX; i++){
			for (int j = 0; j < nY; j++){
				specData [i][j] = specData [i][j] / maxDataAmp;
				plotData[i][j] = (plotData[i][j]-minAmp)/diff;
				// fits specImageData to the size of the picture
				specImageData[i*dim.width / nX ][j* dim.height / nY] =  ((plotData[i][j])  );
			}
		}
		converted = true;
	}

	private static Color getColor(double power) { //Returns HSB Color Model in Color object
		double H = power * 0.4; // Hue (note 0.4 = Green, see huge chart below)
		double sat = 1.0;
		double bright = 1.0;
		return Color.getHSBColor((float)H, (float)sat, (float)bright);
	}

}
