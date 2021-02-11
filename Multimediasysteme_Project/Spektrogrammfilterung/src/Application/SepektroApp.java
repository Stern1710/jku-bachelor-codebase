package Application;

import View.StartingPanel;
import View.UserFrame;

import javax.swing.*;
import javax.swing.plaf.synth.SynthLookAndFeel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;

/**
 * Application class which starts the project, holds main method
 * @author Christopher Holzweber, Stefan Paukner, Simon Sternbauer, defnitly not Benedikt Lichtneker
 */
public class SepektroApp {

	public static void main(String[] args) {
		ImageIcon img = new ImageIcon("icon.png");
		new StartingPanel(img).setIconImage(img.getImage());
	}
}