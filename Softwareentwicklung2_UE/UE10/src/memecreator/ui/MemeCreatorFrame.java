package memecreator.ui;

import memecreator.factory.MemeFactory;
import memecreator.model.Meme;
import memecreator.model.MemeModel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;

public class MemeCreatorFrame extends JFrame {
	
	private static final String RESOURCES = "resources";
	
	public MemeCreatorFrame() {
		setTitle("Meme Creator :noice:");
		setBounds(0, 0, 800, 700);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		
		CreationPanel panelCreation = new CreationPanel();
		contentPane.add(panelCreation, BorderLayout.CENTER);
		
		MemeModel model = new MemeModel();
		try (Stream<Path> files = Files.list(Paths.get(RESOURCES))) {
			files.map(Path::toFile)
					.map(file -> MemeFactory.crateMemeFromFile(file))
					.filter(Objects::nonNull)
					.forEach(model::addMeme);
		} catch (IOException e) {
			e.printStackTrace();
		}
		TemplatePanel panelTemplates = new TemplatePanel(model, panelCreation);
		panelTemplates.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		JScrollPane scrollPane = new JScrollPane(panelTemplates);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		contentPane.add(scrollPane, BorderLayout.NORTH);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu menuFile = new JMenu("File");
		menuBar.add(menuFile);
		
		JMenuItem menuItemAdd = new JMenuItem("Add Meme Template...");
		menuItemAdd.addActionListener(ae -> {
			JFileChooser chooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Images (*.jpg, *.gif, *.png)", "jpg", "gif", "png");
			chooser.setFileFilter(filter);
			int returnVal = chooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();
				Meme meme = MemeFactory.crateMemeFromFile(file);
				if (meme != null) {
					model.addMeme(meme);
					writeImageToFile(meme.getImage(), new File(RESOURCES + File.separator + file.getName()));
				}
			}
		});
		menuFile.add(menuItemAdd);
		
		JMenuItem menuItemSave = new JMenuItem("Save...");
		menuItemSave.addActionListener(ae -> {
			BufferedImage memeImage = panelCreation.getCreatedMemeImage();
			if (memeImage != null) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG Image (*.png)", "png");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showSaveDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					writeImageToFile(memeImage, file);
				}
			}
		});
		menuFile.add(menuItemSave);
		
		JMenuItem menuItemExit = new JMenuItem("Exit");
		menuItemExit.addActionListener(ae -> dispose());
		menuFile.add(menuItemExit);
	}
	
	private static void writeImageToFile(BufferedImage image, File file) {
		if (!file.getName().endsWith(".png")) {
			file = new File(file.toString() + ".png");
		}
		try {
			ImageIO.write(image, "png", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
