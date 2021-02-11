package View;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class StartingPanel extends JFrame {
    private final Timer t;

    public StartingPanel(ImageIcon imageIcon){
        setPreferredSize(new Dimension(320,320));
        setResizable(false);
        setLocationRelativeTo(null);
        setUndecorated(true);

        URL url = StartingPanel.class.getResource("../loading.gif");
        ImageIcon loadingIcon = new ImageIcon(url);
        JLabel label = new JLabel(loadingIcon);
        this.add(label);
        pack();
        t = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                UserFrame frame = new UserFrame();
                frame.setIconImage(imageIcon.getImage());
                t.stop();
            }
        });
        t.start();
        setVisible(true);
    }
}
