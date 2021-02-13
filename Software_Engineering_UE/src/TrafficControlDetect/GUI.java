package TrafficControlDetect;

import common.TrafficControlDetect.StreetName;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
/**
 * This user-interface, show the latest logs of the Detection Network Server, one tab for each street individuals.
 * @author Christopher Holzweber
 *
 */
public class GUI extends JFrame implements ActionListener
{

	private static final long serialVersionUID = 1L;
    static GUI thegui;
    private final int SCROLL_BUFFER_SIZE = 15;
    JPanel pnPanel0;
    JToolBar tlbSaveBar;
    JButton btSaveBar_1;
    JButton btSaveBar_2;
    JButton btSaveBar_3;
    JTabbedPane tbpTabbedPane4;

    JPanel pnPanel1;
    JTextArea taArea1;

    JPanel pnPanel2;
    JTextArea taArea2;

    String helpMessage = "On the \"Tabs\" you can switch between the detected streets\n "
            + "Clicking the \"Save\" button, will save the open Tab into a txt File in the current directors "
            + "\n Clicking on the button \"Broken\" will print the broken devices into a txt file in the current directory";


    private Map<StreetName, JTextArea> panelMap;
    private Map<Integer, StreetName> idxMap;
/**
 */
    public GUI()
    {
       super( "Traffic Detection and Control" );
       panelMap = new HashMap<>();
       pnPanel0 = new JPanel();
       idxMap = new HashMap<>();
       GridBagLayout gbPanel0 = new GridBagLayout();
       GridBagConstraints gbcPanel0 = new GridBagConstraints();
       pnPanel0.setLayout( gbPanel0 );

       tlbSaveBar = new JToolBar( "mainBar"  );
       btSaveBar_1 = new JButton( "Save" );
       btSaveBar_1.addActionListener(this);
       btSaveBar_1.setActionCommand("Save");
       btSaveBar_2 = new JButton( "Help" );
       btSaveBar_2.addActionListener(this);
       btSaveBar_2.setActionCommand("Help");
       tlbSaveBar.add( btSaveBar_1 );
       tlbSaveBar.add( btSaveBar_2 );
       btSaveBar_3 = new JButton( "Broken" );
       btSaveBar_3.addActionListener(this);
       btSaveBar_3.setActionCommand("Broken");
       tlbSaveBar.add( btSaveBar_3 );
       gbcPanel0.gridx = 0;
       gbcPanel0.gridy = 0;
       gbcPanel0.gridwidth = 26;
       gbcPanel0.gridheight = 1;
       gbcPanel0.fill = GridBagConstraints.BOTH;
       gbcPanel0.weightx = 1;
       gbcPanel0.weighty = 0;
       gbcPanel0.anchor = GridBagConstraints.NORTH;
       gbPanel0.setConstraints( tlbSaveBar, gbcPanel0 );
       pnPanel0.add( tlbSaveBar );

       tbpTabbedPane4 = new JTabbedPane( );
       //creating panels - one for each street
       int cnt = 0;
       for(StreetName name : StreetName.values()) {
           pnPanel1 = new JPanel();
           GridBagLayout gbPanel1 = new GridBagLayout();
           GridBagConstraints gbcPanel1 = new GridBagConstraints();
           pnPanel1.setLayout( gbPanel1 );

           taArea1 = new JTextArea(2,10);
           panelMap.put(name,taArea1);
           gbcPanel1.gridx = 0;
           gbcPanel1.gridy = 0;
           gbcPanel1.gridwidth = 1;
           gbcPanel1.gridheight = 1;
           gbcPanel1.fill = GridBagConstraints.BOTH;
           gbcPanel1.weightx = 1;
           gbcPanel1.weighty = 1;
           gbcPanel1.anchor = GridBagConstraints.NORTH;
           gbPanel1.setConstraints( taArea1, gbcPanel1 );
           pnPanel1.add( taArea1 );
           tbpTabbedPane4.addTab(name.toString(),pnPanel1);
           idxMap.put(cnt++, name);
       }
       cnt = 0;


       gbcPanel0.gridx = 0;
       gbcPanel0.gridy = 1;
       gbcPanel0.gridwidth = 26;
       gbcPanel0.gridheight = 19;
       gbcPanel0.fill = GridBagConstraints.BOTH;
       gbcPanel0.weightx = 1;
       gbcPanel0.weighty = 1;
       gbcPanel0.anchor = GridBagConstraints.NORTH;
       gbPanel0.setConstraints( tbpTabbedPane4, gbcPanel0 );
       pnPanel0.add( tbpTabbedPane4 );

       setDefaultCloseOperation( EXIT_ON_CLOSE );

       setContentPane( pnPanel0 );
       this.setSize(800,300);
       setVisible( true );
       for(StreetName name : StreetName.values()) {
           setData(name,"Got no data until now");
       }
    }

    /**
     * prints a new dataset to the streetname tab
     * @param name
     * @param data
     */
    public void setData(StreetName name, String data) {
        JTextArea temp = panelMap.get(name);
        temp.append("\n"+data);
        trunkTextArea(temp);

    }

    /**
     * Only a maximum of lines can be printed
     * @param txtWin
     */
    public void trunkTextArea(JTextArea txtWin)
    {
        int numLinesToTrunk = txtWin.getLineCount() - SCROLL_BUFFER_SIZE;
        if(numLinesToTrunk > 0)
        {
            try
            {
                int posOfLastLineToTrunk = txtWin.getLineEndOffset(numLinesToTrunk - 1);
                txtWin.replaceRange("",0,posOfLastLineToTrunk);
            }
            catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * saved data in a file called StreetNamex.nxt
     * @param name
     */
    public void saveData(StreetName name) {
        String save = panelMap.get(name).getText();
        try {
              FileWriter myWriter = new FileWriter(name+".txt");
              myWriter.write(save);
              myWriter.close();

            } catch (IOException e) {
              System.out.println("An error occurred while writing. "+name);
              e.printStackTrace();
            }
    }

    /**
     * User has done a new action, checks which box was ticked
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Save")) {
            saveData(idxMap.get(tbpTabbedPane4.getSelectedIndex()));
        }else if(e.getActionCommand().equals("Help")) {
             JOptionPane.showMessageDialog(this, helpMessage, "Help Window", JOptionPane.INFORMATION_MESSAGE);
        }else if(e.getActionCommand().equals("Broken")) {
             JOptionPane.showMessageDialog(this, "Not implemented yet", "Broken Devices", JOptionPane.ERROR_MESSAGE);
        }
    }
} 
