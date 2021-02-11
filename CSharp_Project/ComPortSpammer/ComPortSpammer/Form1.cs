using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.IO.Ports;
using System.Xml.Serialization;
using System.IO;

namespace ComPortSpammer
{
    public partial class Form1 : Form
    {
        private static SerialPort _serial;
        private bool isConnected = false;
        
        public Form1()
        {
            InitializeComponent();
            InitUI();
        }

        /* Interface reactionary methods */
        private void btnSerialConnector_Click(object sender, EventArgs e)
        {
            if (isConnected)
            {
                _serial.Close();
                _serial = null;
                isConnected = false;
                FlipGroupEnableStatus();
            } else
            {
                try
                {
                    _serial = new SerialPort(cboComPorts.SelectedItem.ToString(), (int)nudBaudRate.Value, DetermineParity(), (int)nudDatabits.Value, DetermineStopBits());
                    if (_serial != null)
                    {
                        _serial.Open();
                        FlipGroupEnableStatus();
                        isConnected = true;
                    }
                }
                catch (UnauthorizedAccessException)
                {
                    MessageBox.Show("Com Port seems to be in use by another program", "Error on Connection", MessageBoxButtons.OK, MessageBoxIcon.Error);
                }
                catch (Exception)
                {
                    MessageBox.Show("No connection to selected ComPort possible!", "Error on Connection", MessageBoxButtons.OK, MessageBoxIcon.Error);
                }
            }
            
        }

        private void btnNewCommmand_Click(object sender, EventArgs e)
        {
            if (txtNewCommand.Text != null && txtNewCommand.Text != "")
            {
                lboCommands.Items.Add(txtNewCommand.Text);
                txtNewCommand.Text = "";
            }
        }

        private void btnAddToRunner_Click(object sender, EventArgs e)
        {
            if (lboCommands.SelectedIndex >= 0)
            {
                for (int i=0; i < (int)nudRunnerAmount.Value; i++)
                {
                    lboRunner.Items.Add(lboCommands.SelectedItem.ToString());
                }
            }
        }

        private void btnRemoveCommand_Click(object sender, EventArgs e)
        {
            if (lboCommands.SelectedIndex >= 0)
            {
                string name = lboCommands.SelectedItem.ToString();
                lboCommands.Items.RemoveAt(lboCommands.SelectedIndex);

                int numItems = lboRunner.Items.Count;
                for (int i=0, k=0; i < numItems; i++)
                {
                    if (lboRunner.Items[k].Equals(name))
                    {
                        lboRunner.Items.RemoveAt(k);
                    } else
                    {
                        k++;
                    }
                }
            }
        }

        private void btnRemFromRunner_Click(object sender, EventArgs e)
        {
            if (lboRunner.SelectedIndex >= 0)
            {
                lboRunner.Items.RemoveAt(lboRunner.SelectedIndex);
            }
        }

        private void btnStartRunner_Click(object sender, EventArgs e)
        {
            if (_serial.IsOpen && CheckLegitimate())
            {
                for (int i=0; i < lboRunner.Items.Count; i++)
                {
                    if (lboRunner.Items[i].ToString().Contains("Repeat times;"))
                    {
                        i += looper(i);
                    }
                    else if (lboRunner.Items[i].ToString().Contains("Wait;"))
                    {
                        int time;
                        int.TryParse(lboRunner.Items[i].ToString().Split(new char[] { ';' })[1], out time); //I love that line of code!
                        System.Threading.Thread.Sleep(time);
                    }
                    else
                    {
                        _serial.Write(lboRunner.Items[i].ToString() + " \n");
                    }
                }

            }
            else
            {
                MessageBox.Show("Please make sure that every repeat statement is closed by a break statement", "Error on execution", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        private void btnSaveCommands_Click(object sender, EventArgs e)
        {
            CommandSaver saver = new CommandSaver(GetCommands());
            XmlSerializer serializer = new XmlSerializer(typeof(CommandSaver));
            SaveFileDialog(saver, serializer);
        }

        private void btnSaveAll_Click(object sender, EventArgs e)
        {
            SuperSaver saver;
            string[] commands = GetCommands();
            List<string> runners = new List<string>();
            XmlSerializer serializer = new XmlSerializer(typeof(SuperSaver));

            foreach (string s in lboRunner.Items)
            {
                runners.Add(s);
            }

            saver = new SuperSaver(commands, runners.ToArray());

            SaveFileDialog(saver, serializer);

        }

        private void btnLoadCommands_Click(object sender, EventArgs e)
        {
            CommandSaver saver = OpenFileDialog<CommandSaver>();
            if (saver != null)
            {
                SetCommands(saver.Commands);
            }
           
        }

        private void btnLoadAll_Click(object sender, EventArgs e)
        {
            SuperSaver saver = OpenFileDialog<SuperSaver>();
            if (saver != null)
            {
                SetCommands(saver.Commands);
                SetRunners(saver.Runner);
            }
        }

        private void btnAddPreCommand_Click(object sender, EventArgs e)
        {
            if (cboPreCommands.SelectedIndex >= 0)
            {
                if (cboPreCommands.SelectedItem.Equals("Break repeat"))
                {
                    lboRunner.Items.Add(cboPreCommands.SelectedItem);
                } else
                {
                    lboRunner.Items.Add(cboPreCommands.SelectedItem + ";" + nudPreCommand.Value);
                }
                
            }
        }

        /* ---------- HELPERS ---------- */
        /* UI helpers */
        private void InitUI()
        {
            gpoRunner.Enabled = false;
            gpoCommands.Enabled = false;
            gpoConnection.Enabled = true;

            foreach (string port in SerialPort.GetPortNames())
            {
                cboComPorts.Items.Add(port);
            }

            if (cboComPorts.Items.Count > 0)
            {
                cboComPorts.SelectedIndex = 0;
            }

            cboParityBit.Items.Add(Parity.Even);
            cboParityBit.Items.Add(Parity.Odd);
            cboParityBit.Items.Add(Parity.None);
            cboParityBit.SelectedIndex = 2;

            cboStopBit.Items.Add(StopBits.None);
            cboStopBit.Items.Add(StopBits.One);
            cboStopBit.Items.Add(StopBits.OnePointFive);
            cboStopBit.Items.Add(StopBits.Two);
            cboStopBit.SelectedIndex = 1;

            cboPreCommands.Items.Add("Wait");
            cboPreCommands.Items.Add("Repeat times");
            cboPreCommands.Items.Add("Break repeat");

            nudPreCommand.Maximum = decimal.MaxValue;
            nudRunnerAmount.Maximum = decimal.MaxValue;
        }

        private void FlipGroupEnableStatus()
        {
            gpoRunner.Enabled = !gpoRunner.Enabled;
            gpoCommands.Enabled = !gpoCommands.Enabled;
            gpoConnection.Enabled = !gpoConnection.Enabled;

            if (btnSerialConnector.Text.Equals("Connect"))
            {
                btnSerialConnector.Text = "Disconnect";
            }
            else
            {
                btnSerialConnector.Text = "Connect";
            }
        }

        /* Serial connection helpers */
        private Parity DetermineParity()
        {
            if (cboParityBit.SelectedItem.Equals(Parity.Even))
            {
                return Parity.Even;
            }
            if (cboParityBit.SelectedItem.Equals(Parity.Odd))
            {
                return Parity.Odd;
            }

            return Parity.None;
        }

        private StopBits DetermineStopBits()
        {
            if (StopBits.None.Equals(cboStopBit.SelectedItem))
            {
                return StopBits.None;
            }
            if (StopBits.One.Equals(cboStopBit.SelectedItem))
            {
                return StopBits.One;
            }
            if (StopBits.OnePointFive.Equals(cboStopBit.SelectedItem))
            {
                return StopBits.OnePointFive;
            }

            return StopBits.Two;
        }

        /* File helpers */
        private void SaveFileDialog(CommandSaver saver, XmlSerializer serializer)
        {
            fdSaveFile.Filter = "XML | *.xml";
            fdSaveFile.Title = "Save file to destination";

            if (fdSaveFile.ShowDialog() == DialogResult.OK)
            {
                serializer.Serialize(fdSaveFile.OpenFile(), saver);
            }
        }

        private T OpenFileDialog<T> () where T:class
        {
            XmlSerializer serializer = new XmlSerializer(typeof(T));

            fdOpenFile.Filter = "XML | *.xml";
            fdSaveFile.Title = "Open file with config";

            if (fdOpenFile.ShowDialog() == DialogResult.OK)
            {
                try
                {                    
                    return (T)serializer.Deserialize(fdOpenFile.OpenFile());
                } catch (Exception)
                {
                    MessageBox.Show("Loading config went wrong.", "Error on loading config", MessageBoxButtons.OK, MessageBoxIcon.Error);
                }
            }

            return null;
        }

        private string[] GetCommands()
        {
            List<string> commands = new List<string>();

            foreach (string s in lboCommands.Items)
            {
                commands.Add(s);
            }

            return commands.ToArray();
        }

        private void SetCommands(List<string> list)
        {
            try
            {
                lboCommands.Items.Clear();
                foreach (string s in list)
                {
                    lboCommands.Items.Add(s);
                }
            } catch (Exception)
            {
                MessageBox.Show("Error on loading Commands", "Error on loading commmands", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        private void SetRunners(List<string> list)
        {
            try
            {
                lboRunner.Items.Clear();
                foreach (string s in list)
                {
                    lboRunner.Items.Add(s);
                }
            }
            catch (Exception)
            {
                MessageBox.Show("Error on loading Runners", "Error on loading runners", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        private bool CheckLegitimate()
        {
            int openCounter = 0;

            foreach (string command in lboRunner.Items)
            {
                if (command.Contains("Repeat times;"))
                {
                    openCounter++;
                }
                if (command.Equals("Break repeat"))
                {
                    if (openCounter <= 0)
                    {
                        return false;
                    }
                    openCounter--;
                }
            }

            return (openCounter == 0);
        }

        // TODO
        /* Save all items from ONE repeater run into a List and then repeat the list n times */
        private int looper (int i)
        {
            List<String> commands = new List<string>();
            int repeater;
            int counter = 1;
            int listI = 0;

            int.TryParse(lboRunner.Items[i++].ToString().Split(new char[] { ';' })[1], out repeater); //I love that line of code!

            //Prepare all commands for execution
            for (; counter > 0; i++)
            {
                commands.Add(lboRunner.Items[i].ToString());
                if (lboRunner.Items[i].ToString().Contains("Repeat times;"))
                {
                    counter++;
                } else if (lboRunner.Items[i].ToString().Equals("Break repeat"))
                {
                    counter--;
                }
            }

            counter = 0;
            while (counter < repeater)
            {
                if (commands[listI].Contains("Repeat times;"))
                {
                    listI += looper(i-commands.Count+listI) + 1;
                }
                else if (commands[listI].Contains("Break repeat"))
                {
                    listI=0;
                    counter++;
                }
                else if (commands[listI].Contains("Wait;"))
                {
                    int time;
                    int.TryParse(commands[listI++].Split(new char[] { ';' })[1], out time); //I love that line of code!
                    System.Threading.Thread.Sleep(time);
                }
                else
                {
                    _serial.Write(commands[listI++] + "\n");
                }
            }

            return commands.Count;
        }
    }
}
