package View;

import javax.swing.JPanel;

import Model.PlayThread;
import Model.SpectogramImage;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Double;
import java.util.Vector;

public class Waveform extends JPanel implements Runnable {

	private static final long serialVersionUID = 8001545804471124718L;
	private static final int PANELHEIGHT = 200;
	private static final int PANELWIDTH = 500;

	public static final int[] bands = { 100, 200, 300, 400, 510, 630, 770, 920, 1080, 1270, 1480, 1720, 2000, 2320, 2700, 3150, 3700, 4400, 5300, 6400, 7700, 9500, 12000, 15500 };
	
	private int[] pos2Band;

	private Thread thread;
	private PlayThread playback;
	private double[][] specData;
	int windowStep;
	int nX;
	int nY;

	Vector<Double> lines = new Vector<Double>();
	Vector<Double> bars = new Vector<Double>();

	private static final int SIZE = PANELHEIGHT - 30;

	private int len;
	int position, lastposition;
	double duration; 

	public Waveform() { 

		setBackground(new Color(20, 20, 20));
		setPreferredSize(new Dimension(PANELWIDTH,PANELHEIGHT));
		setVisible(true);
	}


	public synchronized void createWaveForm() {
		lines.removeAllElements();  // clear the old vector
		bars.removeAllElements();
		if (specData == null ||  position >= specData.length ) {
			return;
		}
		len = specData[position].length;
		// create bars and lines
		int oldBand = 0;
		int band = 0;
		double average = 0;
		int cnt = 0;
		for (int i = 0; i < len; i++) {
			//			System.out.printf("i %d j %d data %f\n", position, i , specData[position][i]);
			band = pos2Band[i];
			if (band != oldBand) {
				if (cnt > 0) {
				 average = average/cnt;
				}
				bars.add(new Line2D.Double(oldBand*20, SIZE, oldBand*20, SIZE - average* SIZE));
				average = 0;
				cnt = 0;
			}
			cnt++;
			average += specData[position][i];
			oldBand = band;
			int j = 10 + i * (PANELWIDTH - 40)/ specData[0].length ;
			lines.add(new Line2D.Double(j, SIZE , j , SIZE - specData[position][i]* SIZE));
		}
		repaint();
	}


	public synchronized void paint(Graphics g) {

		Dimension d = getSize();
		int w = d.width;
		int h = d.height;
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setBackground(getBackground());
		g2.clearRect(0, 0, w, h);

		g2.setColor(Color.green);
		g2.setStroke(new BasicStroke(18));
		for (int i = 1; i < bars.size(); i++) {
			g2.draw((Line2D) bars.get(i));
		}
		
		g2.setColor(Color.red);
		g2.setStroke(new BasicStroke(1));
		for (int i = 1; i < lines.size(); i++) {
			g2.draw((Line2D) lines.get(i));
		}
	}

	public void start(PlayThread playback, double[][] specData) {
		this.playback = playback;
		this.specData = specData;
		thread = new Thread(this);
		thread.setName("WaveForm");
		thread.start();
	}

	public void stop() {
		if (thread != null) {
			thread.interrupt();
		}
		thread = null;
		position = 0;
	}

	@Override
	public void run() {
		float framerate = 0;
		initPos2band();
		windowStep = SpectogramImage.WS/ SpectogramImage.OF/ SpectogramImage.ZP;
		while (thread != null) {
			if ((playback.getSourceLine() != null) && (playback.getSourceLine().isOpen()) ) {
				nX = specData.length;
				long milliseconds = (long)(playback.getSourceLine().getMicrosecondPosition() / 1000);
				framerate = playback.getSourceLine().getFormat().getFrameRate();
				position = (int) (framerate * milliseconds / 1000.0 / windowStep);
			}
			createWaveForm();
			repaint();
			try { Thread.sleep(100); } catch (Exception e) { break; }



			while ((playback.getSourceLine() != null && !playback.getSourceLine().isOpen())) 
			{
				try { Thread.sleep(10); } catch (Exception e) { break; }
			}
		}
		framerate = 0;
		repaint();
	}

	private void initPos2band() {
		pos2Band = new int[specData[0].length];
		for (int i = 0; i < specData[0].length; i++) {
			int j = 0;
			while ((j < bands.length) && ((i * 44100/2 / specData[0].length) > bands[j])) { 
				j++; 
			}
			pos2Band[i] = j;
		}
	}

	public int getPosFromBand(int position)
	{ 
		return pos2Band[position];
	}
}