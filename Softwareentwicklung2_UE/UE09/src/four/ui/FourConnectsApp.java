package four.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import four.game.ConnectsEvent;
import four.game.ConnectsListener;
import four.game.Game;
import four.game.GameImpl;

public class FourConnectsApp {
	private final Game game;
	private final JFrame frame;

	public static void main(String[] args) {
		new FourConnectsApp().open();
	}
	
	private FourConnectsApp() {
		this.game = new GameImpl();
		this.frame = new JFrame("Four Connects");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		Container cp = frame.getContentPane(); 
		cp.setLayout(new BorderLayout());
		cp.add(new ConnectsPanel(game), BorderLayout.CENTER);
		JPanel btnPnl = new JPanel();
		btnPnl.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		cp.add(btnPnl, BorderLayout.NORTH);
				
		JMenuBar menubar = new JMenuBar(); 
		frame.setJMenuBar(menubar);
		JMenu fileMenu = new JMenu("File"); 
		menubar.add(fileMenu); 
		JMenuItem exitMI = new JMenuItem("Exit");
		fileMenu.add(exitMI); 
		exitMI.addActionListener(a -> {
			frame.dispose();
		});
		JMenuItem resetMI = new JMenuItem("Reset");
		fileMenu.add(resetMI);
		resetMI.addActionListener(a -> {
			game.reset();
		});
		
		JPanel msgPnl = new JPanel(); 
		msgPnl.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		cp.add(msgPnl, BorderLayout.SOUTH);
		
		JLabel actionLbl = new JLabel("Game Status");
		msgPnl.add(actionLbl);
		
		game.addConnectsListener(new ConnectsListener() {
			
			@Override
			public void stoneAdded(ConnectsEvent e) {
				actionLbl.setText(game.getGameState().toString());
			}
			
			@Override
			public void fieldReset(ConnectsEvent e) {
				actionLbl.setText("New Game");
			}
		});
	}
	
	private void open() {
		frame.pack(); 
		frame.setLocation(200, 200);
		frame.setVisible(true);
	}
}
