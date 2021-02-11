package at.jku.tk.mms.img;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileFilter;

/**
 * Starts the GUI which allows applying image filters
 * 
 * @author matthias
 */
public class ImageFilterApp extends JFrame {
	
	private static final long serialVersionUID = 3756227063464286767L;
	
	public static final Dimension PANEL_SIZES = new Dimension(400,400);
	
	private String openFile;
	
	private ImagePanel sourceImage;
	
	private ImagePanel targetImage;
	
	private ComboBoxFilterModel comboModel;
	
	private ImageFilterCtrl ctrl;
	
	/** Constructs a new Imaging App, accepts optional File */
	public ImageFilterApp(String openFile) {
		super("MMS UE Image Filters");
		this.openFile = openFile;
		initUI();
		ctrl = new ImageFilterCtrl(sourceImage, targetImage);

		
	}
	
	/** Creates and initializes the UI */
	private void initUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setLayout(new BorderLayout());
		
		JButton load = new JButton("load");
		// workaround for this in line action listening
		final JFrame tmpframe = this; 
		load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setFileFilter(new FileFilter() {
					@Override
					public String getDescription() {
						return "JPEG/PNG Images";
					}
					@Override
					public boolean accept(File f) {
						String n = f.getName().toLowerCase();
						return n.endsWith(".jpg") || n.endsWith(".png");
					}
				});
				//fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				if(fc.showOpenDialog(tmpframe) == JFileChooser.APPROVE_OPTION) {
					File f = fc.getSelectedFile();
					BufferedImage img = loadImage(f);
					ctrl.loadImage(img);
				}
			}
		});
		
		JButton set = new JButton("set");
		set.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ctrl.setTargetAsSource();
			}
		});
		
		JPanel imagingPanel = new JPanel();
		imagingPanel.setLayout(new BoxLayout(imagingPanel, BoxLayout.LINE_AXIS));
		sourceImage = new ImagePanel(PANEL_SIZES);
		targetImage = new ImagePanel(PANEL_SIZES);
		
		comboModel = new ComboBoxFilterModel();
		final JComboBox<FilterInterface> combo = new JComboBox<FilterInterface>(comboModel);
		combo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				FilterInterface f = (FilterInterface) comboModel.getSelectedItem();
				if(combo.getSelectedIndex() != 0) {
					Properties p = new Properties();
					for(String c : f.mandatoryProperties()) {
						OptionQuestionnaire q = new OptionQuestionnaire(c);
						int value = q.askValue(tmpframe);
						p.setProperty(q.getName(), "" + value);
					}
					ctrl.applyFilter(f, p);
					combo.setSelectedIndex(0);
				}
			}
		});
		
		Box b = Box.createHorizontalBox();
		b.add(sourceImage);
		b.add(Box.createHorizontalStrut(5));
		b.add(targetImage);
		imagingPanel.add(b);
		
		JToolBar toolbar = new JToolBar();
		toolbar.add(load);
		toolbar.add(combo);
		toolbar.add(set);
		
		getContentPane().add(toolbar, BorderLayout.NORTH);
		getContentPane().add(imagingPanel, BorderLayout.CENTER);
		
	}
	
	/** Returns the preferred size */
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(PANEL_SIZES.width + PANEL_SIZES.width, PANEL_SIZES.height + 60);
	}
	
	@Override
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}
	
	@Override
	public Dimension getMaximumSize() {
		return getPreferredSize();
	}
	
	/** try loading an image stream */
	public BufferedImage loadImage(InputStream stream) {
		try {
			return ImageIO.read(stream);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		return null;
	}
	
	/** try loading an image file */
	public BufferedImage loadImage(File f) {
		try {
			return loadImage(new FileInputStream(f));
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		return null;
	}
	
	/** try loading an image file from a filename */
	public BufferedImage loadImage(String fileName) {
		return loadImage(new File(fileName));
	}

	/** Launches the app */
	private void launch() {
		if(openFile != null) {
			BufferedImage img = loadImage(openFile);
			ctrl.loadImage(img);
		}
		pack();
		setVisible(true);
	}
	
	/** Launches the app, optionally you may pass a file name to an image in order to avoid
	 *  The need to load an image manually on every testrun.
	 * @param args
	 */
	public static void main(String args[]) {
		String openFile = null;
		if(args.length > 0) {
			openFile = args[0];
		}
		ImageFilterApp app = new ImageFilterApp(openFile);
		app.launch();
	}
}
