package memecreator;

import memecreator.ui.MemeCreatorFrame;

import javax.swing.*;

public class App {
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.err.println("Could not set look and feel, falling back to default");
		}
		SwingUtilities.invokeLater(() -> new MemeCreatorFrame().setVisible(true));
	}
	
}
