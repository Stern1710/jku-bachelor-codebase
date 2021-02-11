package at.jku.tk.mms.voice.impl;

import javax.sound.sampled.AudioFormat;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Double;
import java.util.Vector;

public class Waveform extends JPanel {

	Vector<Double> lines = new Vector<Double>();
	
    Color jfcBlue = new Color(204, 204, 255);
    Color pink = new Color(255, 175, 175);
    int[] audioData;
    public Waveform() {
        setBackground(new Color(20, 20, 20));
        setPreferredSize(new Dimension(500,100));
    }

    public void createWaveForm(byte[] audioBytes, AudioFormat format) {    	    	
    	int groupsize = format.getSampleSizeInBits()/8;

    	//check if the MSB and LSB are not in the same byte
    	if(groupsize == 1) { //we get an 8Bit coding here
            int iAudioDataSize = audioBytes.length;  
            audioData = new int[iAudioDataSize];  
            if (format.getEncoding().toString().startsWith("PCM_SIGN")) {  
                 // PCM_SIGNED  
                 for (int i = 0; i < audioBytes.length; i++) {  
                      audioData[i] = audioBytes[i];  
                 }  
            } else {  
            	for (int i = 0; i < audioBytes.length; i++)
            	{
            	if (audioBytes[i] >= 0)
            		audioData[i] = audioBytes[i] - 128;
            	else
            		audioData[i] = audioBytes[i] + 128;
            	}
            }
    	} else { //For 16 Bit sample size
            int iAudioDataSize = audioBytes.length / groupsize;  
            audioData = new int[iAudioDataSize];  
            
            for (int i = 0; i < iAudioDataSize; i++) {
            	int lsb, msb;
            	if (format.isBigEndian()) {
            		lsb = audioBytes[groupsize * i + groupsize-1];  
                    msb = audioBytes[groupsize * i];
            	}
            	else {
            		lsb = audioBytes[groupsize * i];  
                    msb = audioBytes[groupsize * i + groupsize-1];  
            	}
            	
            	audioData[i] = msb << 8 | (255 & lsb);  
            }
    	}
    	
    	//Only take every second values as the audio comes in as two mono channels in stereo configuration, so therefore always two values are duplicates
    	int indexCounter = 0;
    	for(int i : audioData) {
    		if (i%2 == 0) {
    			lines.add(new Double(indexCounter, 0, indexCounter, i));
    			indexCounter++;
    		}
    	}

        repaint();
    }
    
    
    public void paint(Graphics g) {
    	
        Dimension d = getSize();
        int w = d.width;
        int h = d.height;
        int INFOPAD = 15;
        Graphics2D g2 = (Graphics2D) g;
        g2.setBackground(getBackground());
        g2.clearRect(0, 0, w, h);
        g2.setColor(Color.WHITE);
        g2.fillRect(0, h-INFOPAD, w, INFOPAD);
        
        if (lines.size() > 0) {
        	int valuesPerPixel = lines.size()/w;
        	Vector<Double> allMax = new Vector<Double>();
        	
        	//Collext all max values in a Vector 
        	for (int i = 0; i < lines.size(); i += valuesPerPixel) {
        		Vector<Double> subvector = new Vector<Double>();
        		for (int k=i; k < i+valuesPerPixel && k < lines.size(); k++) {
        			subvector.add(lines.get(k));
        		}
        		allMax.add(getMaxOfVector(subvector));
        	}
        	
        	//Draw all the max values and scale them by the largest factor
        	Double maxValue = getMaxOfVector(allMax);
        	
        	for (int k = 0; k < allMax.size(); k++) {
        		g2.draw(new Line2D.Double(k, (h/2) - (allMax.get(k).getY2() / (maxValue.getY2() / (h/2))), k, (h/2) + (allMax.get(k).getY2() / (maxValue.getY2() / (h/2)))));
        	}
        }
    }
    
    private Double getMaxOfVector (Vector<Double> lines) {
    	Double max = lines.get(0);
    	
    	for (int i=1; i < lines.size(); i++) {
    		if (lines.get(i).getY2()- max.getY2() > 0) {
    			max = lines.get(i);
    		}
    	}
       	return max;
    }

} // End class Waveform