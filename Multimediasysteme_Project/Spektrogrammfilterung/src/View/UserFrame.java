package View;

import Model.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

/**
 * This class is the basic class for the user interaction. Here following parts
 * of the GUI is created: Load image button Load music button Get spektrogram
 * button the three image panels copyright field
 * <p>
 * In order to use the program properly, you have to do following steps:
 * 1 you need to load an image.
 * 2 you need to get an wav file from a wish directory.
 * Note, you do not need to load a picture, to get a spektrogram, as soon as you
 * loaded the file you can also play it with the play button and stop it with the stop button!
 * 3 you now have unlocked the spektrogramm button, press it to get the spektrogramm of the
 * soundfile 4 as soon as a spektrogramm is created, you can use the filter button, to filter your chosen picture with the spektrogram
 * <p>
 * As soon as a picture AND a music file is loaded properly (look out for file
 * endings!) the program starts to filter the image.
 *
 * @author Christopher Holzweber, Simon Sternbauer, Stefan Paukner, definitly not Benedikt Licktnecker
 */
@SuppressWarnings("serial")
public class UserFrame extends JFrame implements ActionListener {

    //------- WAV Converter
    private WAVConverter wavCon;

    //------- AUDIO player
    private AudioPlayer ap;
    //Imagepanels for spektrogram and picture
    private ImagePanel ipSpek;
    private ImagePanel ipOrig;
    private ImagePanel ipfiltered;
    //------- Buttons for loading, filtering, playing and exporting
    private JButton bLoadSpek;
    private JButton bfilterImage;
    private JButton bPlayFile;
    private JButton bExport;
    private JButton bExportPicture;
    private JButton bSetFilterAsNewImage;
    //------ PANEL for the waveform;
    private Waveform waveform;
    private boolean bSpekCreated; // boolean to check if spek was created
    private boolean bPictureLoaded; // checks if image was loaded

    // -------stores the converted music data in an array
    private double musicDataArray[];
    private double samplingRate;
    private SpectogramImage fft;

    //Holds a string with the type of the currently loaded file
    private String fileType;
    //String that points to where the file is loaded from
    private String filepath;


    private Image normalPlayIcon;
    private Image hoverPlayIcon;
    private Image clickedPlayIcon;
    private final Color buttonColor = Color.decode("#5e5e5e");
    private final Color borderColor = Color.decode("#2b2b2b");
    private final Color hoverButton = Color.decode("#5e9bff");
    private final Color clickedButton = Color.decode("#b7cee3");
    private final Color normalBackgroundColor = Color.decode("#939393");

    /**
     * user frame sets the panels and some variables and then calls the
     * setUserInterface method
     */
    public UserFrame() {
        bSpekCreated = false;
        bPictureLoaded = false;
        ipSpek = new ImagePanel();
        ipOrig = new ImagePanel();
        ipfiltered = new ImagePanel();
        waveform = new Waveform();

        setUserInterface();
    }

    /**
     * method sets up the visualization
     */
    private void setUserInterface() {
        ArrayList<JButton> buttons = new ArrayList<>();
        ArrayList<JPanel> panels = new ArrayList<>();

        try {
            normalPlayIcon = ImageIO.read(new File("iconNormal.jpg"))
                    .getScaledInstance(10, 10, java.awt.Image.SCALE_SMOOTH);

            hoverPlayIcon = ImageIO.read(new File("iconHover.jpg"))
                    .getScaledInstance(10, 10, java.awt.Image.SCALE_SMOOTH);

            clickedPlayIcon = ImageIO.read(new File("iconClicked.jpg"))
                    .getScaledInstance(10, 10, java.awt.Image.SCALE_SMOOTH);
        } catch (Exception ex) {
            System.out.println(ex);
        }

        // ---------------BUTTONLIST-------------------
        bLoadSpek = new JButton("Create spektrogram");
        buttons.add(bLoadSpek);
        bLoadSpek.setEnabled(false); // enables as soon as the music file is loaded

        bPlayFile = new JButton("Play");
        buttons.add(bPlayFile);
        // set play image for button
        bPlayFile.setEnabled(false); // enables as soon as the music file is loaded

        JButton bLoadMusic = new JButton("Load Music");
        JButton bLoadImage = new JButton("Load Image");

        bfilterImage = new JButton("Filter Image");
        bfilterImage.setEnabled(false); // enables as soon as spektrogramm is created AND image is loaded

        bExport = new JButton("Export Music");
        bExport.setEnabled(false); //Enable when music is loaded

        bExportPicture = new JButton("Export Picture");
        bExportPicture.setEnabled(false); //Enable when music is loaded

        bSetFilterAsNewImage = new JButton("Set as new Picture");
        bSetFilterAsNewImage.setEnabled(false); //Enable when music is loaded

        buttons.add(bLoadMusic);
        buttons.add(bLoadImage);
        buttons.add(bfilterImage);
        buttons.add(bExport);
        buttons.add(bExportPicture);
        buttons.add(bSetFilterAsNewImage);
        // --------------------------------------------
        setTitle("MMS SS2019 - Spektrogramfilter");

        // closes the application when exit
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // windows is not resizable
        setResizable(false);

        setLocationRelativeTo(null);

        //Adds according listeners on UserFrame to Buttons
        bLoadMusic.addActionListener(this);
        bPlayFile.addActionListener(this);
        bLoadImage.addActionListener(this);
        bLoadSpek.addActionListener(this);
        bfilterImage.addActionListener(this);
        bExport.addActionListener(this);
        bExportPicture.addActionListener(this);
        bSetFilterAsNewImage.addActionListener(this);

        // ------PANEL FOR PICTURES
        JPanel picturePanel = new JPanel();
        picturePanel.setLayout(new GridLayout(1, 3));
        picturePanel.add(ipSpek);
        picturePanel.add(ipOrig);
        picturePanel.add(ipfiltered);
        panels.add(picturePanel);

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(picturePanel, BorderLayout.NORTH);
        mainPanel.add(waveform, BorderLayout.SOUTH);
        panels.add(mainPanel);

        // -------SET LAYOUT of Frame
        setLayout(new BorderLayout());
        // -------TOOLBAR
        JPanel toolBar = new JPanel();
        toolBar.setLayout(new GridLayout(1, buttons.size(), 10, 0));
        toolBar.setBorder(BorderFactory.createEmptyBorder(10, 5, 0, 5));
        toolBar.add(bLoadImage);
        toolBar.add(bLoadMusic);
        toolBar.add(bPlayFile);
        toolBar.add(bLoadSpek);
        toolBar.add(bfilterImage);
        toolBar.add(bExport);
        toolBar.add(bExportPicture);
        toolBar.add(bSetFilterAsNewImage);
        panels.add(toolBar);
        // -------FOOTNOTE
        JLabel copyrightLabel = new JLabel(
                "\u00A9 Christopher Holzweber,Stefan Paukner, Simon Sternbauer, Benedikt Lichtnecker");
        // -------SET FRAME LAYOUT
        getContentPane().add(toolBar, BorderLayout.NORTH);
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        getContentPane().add(copyrightLabel, BorderLayout.SOUTH);

        pack(); // autosize the frame
        setVisible(true);

        panels.add(ipfiltered);
        panels.add(ipOrig);
        panels.add(ipSpek);

        setStyle(buttons, panels);
    }

    private void setStyle(ArrayList<JButton> buttons, ArrayList<JPanel> panels) {
        for (JButton button : buttons) {
            button.setBackground(buttonColor);
            button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(borderColor, 2, true),
                    BorderFactory.createLineBorder(buttonColor, 5)));

            button.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    if (button.isEnabled()) {
                        button.setBackground(clickedButton);
                        button.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(borderColor, 2, true),
                                BorderFactory.createLineBorder(clickedButton, 5)));
                    }


                    if (button.equals(bPlayFile)) {
                        bPlayFile.setIcon(new ImageIcon(clickedPlayIcon));
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (button.isEnabled()) {
                        colorButton(button);
                    }
                }

                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    if (button.isEnabled()) {
                        button.setBackground(hoverButton);
                        button.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(borderColor, 2, true),
                                BorderFactory.createLineBorder(hoverButton, 5)));

                        if (button.equals(bPlayFile)) {
                            bPlayFile.setIcon(new ImageIcon(hoverPlayIcon));
                        }
                    }
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    if (button.isEnabled()) {
                        colorButton(button);
                    }
                }
            });
        }

        for (JPanel panel : panels) {
            panel.setBackground(normalBackgroundColor);
        }
    }


    private void colorButton(JButton button) {
        button.setBackground(buttonColor);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 2, true),
                BorderFactory.createLineBorder(buttonColor, 5)));

        if (button.equals(bPlayFile)) {
            bPlayFile.setIcon(new ImageIcon(normalPlayIcon));
        }
    }

    //---------------------ACTION LISTENER--------------------------------
    @Override
    /**
     * reactes as soon as a button is pressed
     */
    public void actionPerformed(ActionEvent action) {
        switch (action.getActionCommand()) {
            case "Load Image":
                loadimage();
                break;
            case "Load Music":
                loadMusic();
                break;
            case "Create spektrogram":
                createspektrogram();
                break;
            case "Filter Image":
                filterimage();
                break;
            case "Play":
                playMusic();
                break;
            case "Export Music":
                storeAsOtherFormat();
                break;
            case "Export Picture":
                exportFilteredPicture();
                break;
            case "Set as new Picture":
                setFilterImageAsNewImage();
                break;
        }

    }

    private void setFilterImageAsNewImage() {
        ipOrig.setImage(ipfiltered.getImage());
    }

    private void exportFilteredPicture() {
    	JFileChooser fileChooser = new JFileChooser();

        fileChooser.setDialogTitle("Specify a file to save");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public String getDescription() {// set description for filechooser
                return ".jpg";
            }

            @Override
            public boolean accept(File f) {
                String sName = f.getName().toLowerCase();
                return sName.endsWith(".jpg");
            }
        });
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String uriFileToSave = fileChooser.getSelectedFile().toURI().getPath();
            uriFileToSave = (!uriFileToSave.endsWith(".jpg") ? uriFileToSave + ".jpg" : uriFileToSave);

            try {
                File fileToSave = new File(uriFileToSave);
                fileToSave.getParentFile().mkdirs();
                fileToSave.createNewFile();
                Image im = ipfiltered.getImage();
                BufferedImage bi = new BufferedImage
                        (im.getWidth(null), im.getHeight(null), BufferedImage.TYPE_INT_RGB);
                Graphics bg = bi.getGraphics();
                bg.drawImage(im, 0, 0, null);
                bg.dispose();

                ImageIO.write(bi, "jpg", fileToSave);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Plays the loaded File or stops the playing
     */
    private void playMusic() {
        if (ap != null && ap.threadIsAlive()) {
            ap.stopplay();
            waveform.stop();
        } else {
            ap = new AudioPlayer(wavCon.getOriginalFilePath()); //no file is playing, create a new Player
            ap.startplay();
            if (fft != null) {
            	waveform.start(ap.getPlayThread(), fft.getSpecData());
            }    
        }
    }

    // ---------------------BUTTON METHODS-----------------------------------
    // TODO
    private void filterimage() {
        ImageFilter imgFilter = new ImageFilter(); // create new ImageFilter object
        BufferedImage image;
        if (ipSpek.getImage() instanceof BufferedImage) // already a buffered image
        {
            image = imgFilter.filter(fft.getSpectogramImageRGB(), (BufferedImage) ipOrig.getImage()); // filter
            // image
        } else {
            // Create a buffered image of Spektrogram
            // Draw the image into the buffered image

            // Create a buffered image of OriginalPicture
            BufferedImage bimageOrig = new BufferedImage(ipOrig.getImage().getWidth(null),
                    ipOrig.getImage().getHeight(null), BufferedImage.TYPE_INT_ARGB);
            // Draw the image on to the buffered image
            Graphics2D bGr2 = bimageOrig.createGraphics();
            bGr2.drawImage(ipOrig.getImage(), 0, 0, null);
            bGr2.dispose();
            image = imgFilter.filter(fft.getSpectogramImageRGB(), bimageOrig);
        }

        ipfiltered.setImage(image); // set Image in the right panel
        bExportPicture.setEnabled(true);
        bSetFilterAsNewImage.setEnabled(true);
    }

    // TODO
    private void createspektrogram() {
        if (bPictureLoaded) {
            bfilterImage.setEnabled(true);
            colorButton(bfilterImage);
        } else {
            ipfiltered.setImage("defaultFilter.png");
            ipOrig.setImage("defaultOriginal.png");
            pack(); // autosize the frame
        }
        fft = new SpectogramImage(musicDataArray, samplingRate, ipSpek.getSize());
        ipSpek.setImage(fft.getSpectogramImage());
        bSpekCreated = true;
    }

    /**
     * loads music file for the spectrogram
     */
    private void loadMusic() {
        JFileChooser filechooser = new JFileChooser();
        filechooser.setCurrentDirectory(new File(".")); // start in current directory
        filechooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        // ONLY allow WAV and MP3 files therefore we need a file-filter
        filechooser.setFileFilter(new FileFilter() {
            @Override
            public String getDescription() {
                return "wav, mp3";
            }

            @Override
            public boolean accept(File f) {
                String sName = f.getName().toLowerCase();
                if (sName.endsWith("mp3")) {
                    return true;
                }
                return sName.endsWith("wav");
            }
        });

        if (filechooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File musicFile = filechooser.getSelectedFile();
            //Check if WAV or MP3
            filepath = musicFile.getAbsolutePath();
            if (musicFile.getName().endsWith(".wav")) {
                loadWAVData(musicFile);
                MP3WavConverter.convertWAVToMP3File(musicFile.getAbsolutePath());
                fileType = "wav";
            } else if (musicFile.getName().endsWith(".mp3")) {
                //If MP3, convert to wav and then pass on to get the wav file
                loadWAVData(MP3WavConverter.convertMP3ToWavFile(musicFile.getAbsolutePath()));
                fileType = "mp3";
            } else {
                System.err.println("WRONG FILE, NEEDS TO BE A .WAV or .MP3 FILE");
                loadMusic();
            }
        }
    }

    /**
     * load image from database (you can choose) and sets the middle and the right
     * panel
     */
    private void loadimage() {
        JFileChooser filechooser = new JFileChooser();
        filechooser.setCurrentDirectory(new File(".")); // start in current directory
        filechooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        // ONLY allow JPG and PNG files therefore we need a filefilter
        filechooser.setFileFilter(new FileFilter() {
            @Override
            public String getDescription() {// set description for filechooser
                return "jpg & png File";
            }

            @Override
            public boolean accept(File f) {
                String sName = f.getName().toLowerCase();
                return sName.endsWith(".png") || sName.endsWith(".jpg"); // only files with ending wav
            }
        });
        if (filechooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File imgFile = filechooser.getSelectedFile();

            if (imgFile.getPath().endsWith(".png") || imgFile.getPath().endsWith(".jpg")) {
                ipfiltered.setImage(imgFile);
                ipOrig.setImage(imgFile);
                if (!bSpekCreated) { // set default image if spek is ot created
                    ipSpek.setImage("deafultFilter.png");
                }

                pack(); // autosize the frame
                bPictureLoaded = true;
                if (bSpekCreated) { // if also the spek is created, we are now able to filter this image
                    bfilterImage.setEnabled(true);
                }
            } else { // wrong file format, start again
                System.err.println("YOU NEED TO SELECT A PNG OR A JPG FILE!");
                loadimage();
            }

        }
    }

    /**
     * Stores the input audio file as an MP3 to the system
     */
    private void storeAsOtherFormat() {
        JFileChooser fileChooser = new JFileChooser();
        String fileFrom = System.getProperty("user.dir") + "\\converted\\" + new File(filepath).getName();
        String newExtension;

        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        if ("wav".equals(fileType)) {
            fileChooser.setFileFilter(new FileFilter() {

                @Override
                public String getDescription() {
                    return "mp3";
                }

                @Override
                public boolean accept(File arg0) {
                    String name = arg0.getName().toLowerCase();
                    return name.endsWith(".mp3") ? true : false;
                }
            });
            fileFrom = fileFrom.replaceAll(".wav", ".mp3");
            newExtension = ".mp3";
        } else {
            fileChooser.setFileFilter(new FileFilter() {

                @Override
                public String getDescription() {
                    return "wav";
                }

                @Override
                public boolean accept(File arg0) {
                    String name = arg0.getName().toLowerCase();
                    return name.endsWith(".wav") ? true : false;
                }
            });
            fileFrom = fileFrom.replaceAll(".mp3", ".wav");
            newExtension = ".wav";
        }

        fileChooser.showSaveDialog(this);

        try (FileOutputStream fo = new FileOutputStream(fileChooser.getSelectedFile() + newExtension)) {
            FileInputStream is = new FileInputStream(new File(fileFrom));
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                fo.write(buffer, 0, length);
            }
            is.close();
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadWAVData(File musicFile) {
        try {
            wavCon = new WAVConverter(musicFile.getAbsolutePath());
            musicDataArray = wavCon.getDataArray();
            samplingRate = wavCon.getSamplingRate();
            bLoadSpek.setEnabled(true); // now we can create a spektrogram with the data!
            bPlayFile.setEnabled(true);
            bExport.setEnabled(true);
            colorButton(bLoadSpek);
            colorButton(bPlayFile);
            colorButton(bExport);
        } catch (WAVFormatException e) {
            e.printStackTrace();
        }
    }
}