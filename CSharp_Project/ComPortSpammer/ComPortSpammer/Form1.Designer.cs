namespace ComPortSpammer
{
    partial class Form1
    {
        /// <summary>
        /// Erforderliche Designervariable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Verwendete Ressourcen bereinigen.
        /// </summary>
        /// <param name="disposing">True, wenn verwaltete Ressourcen gelöscht werden sollen; andernfalls False.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Vom Windows Form-Designer generierter Code

        /// <summary>
        /// Erforderliche Methode für die Designerunterstützung.
        /// Der Inhalt der Methode darf nicht mit dem Code-Editor geändert werden.
        /// </summary>
        private void InitializeComponent()
        {
            this.gpoConnection = new System.Windows.Forms.GroupBox();
            this.cboParityBit = new System.Windows.Forms.ComboBox();
            this.cboStopBit = new System.Windows.Forms.ComboBox();
            this.lblStopBits = new System.Windows.Forms.Label();
            this.nudDatabits = new System.Windows.Forms.NumericUpDown();
            this.label4 = new System.Windows.Forms.Label();
            this.label3 = new System.Windows.Forms.Label();
            this.label1 = new System.Windows.Forms.Label();
            this.nudBaudRate = new System.Windows.Forms.NumericUpDown();
            this.cboComPorts = new System.Windows.Forms.ComboBox();
            this.lblComPort = new System.Windows.Forms.Label();
            this.btnSerialConnector = new System.Windows.Forms.Button();
            this.gpoCommands = new System.Windows.Forms.GroupBox();
            this.groupBox4 = new System.Windows.Forms.GroupBox();
            this.btnLoadCommands = new System.Windows.Forms.Button();
            this.btnSaveCommands = new System.Windows.Forms.Button();
            this.btnSaveAll = new System.Windows.Forms.Button();
            this.btnLoadAll = new System.Windows.Forms.Button();
            this.groupBox2 = new System.Windows.Forms.GroupBox();
            this.nudPreCommand = new System.Windows.Forms.NumericUpDown();
            this.btnAddPreCommand = new System.Windows.Forms.Button();
            this.cboPreCommands = new System.Windows.Forms.ComboBox();
            this.btnRemoveCommand = new System.Windows.Forms.Button();
            this.nudRunnerAmount = new System.Windows.Forms.NumericUpDown();
            this.btnAddToRunner = new System.Windows.Forms.Button();
            this.label2 = new System.Windows.Forms.Label();
            this.lboCommands = new System.Windows.Forms.ListBox();
            this.btnNewCommmand = new System.Windows.Forms.Button();
            this.txtNewCommand = new System.Windows.Forms.TextBox();
            this.gpoRunner = new System.Windows.Forms.GroupBox();
            this.lboRunner = new System.Windows.Forms.ListBox();
            this.btnStartRunner = new System.Windows.Forms.Button();
            this.btnRemFromRunner = new System.Windows.Forms.Button();
            this.fdOpenFile = new System.Windows.Forms.OpenFileDialog();
            this.fdSaveFile = new System.Windows.Forms.SaveFileDialog();
            this.gpoConnection.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.nudDatabits)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.nudBaudRate)).BeginInit();
            this.gpoCommands.SuspendLayout();
            this.groupBox4.SuspendLayout();
            this.groupBox2.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.nudPreCommand)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.nudRunnerAmount)).BeginInit();
            this.gpoRunner.SuspendLayout();
            this.SuspendLayout();
            // 
            // gpoConnection
            // 
            this.gpoConnection.Controls.Add(this.cboParityBit);
            this.gpoConnection.Controls.Add(this.cboStopBit);
            this.gpoConnection.Controls.Add(this.lblStopBits);
            this.gpoConnection.Controls.Add(this.nudDatabits);
            this.gpoConnection.Controls.Add(this.label4);
            this.gpoConnection.Controls.Add(this.label3);
            this.gpoConnection.Controls.Add(this.label1);
            this.gpoConnection.Controls.Add(this.nudBaudRate);
            this.gpoConnection.Controls.Add(this.cboComPorts);
            this.gpoConnection.Controls.Add(this.lblComPort);
            this.gpoConnection.Location = new System.Drawing.Point(13, 13);
            this.gpoConnection.Name = "gpoConnection";
            this.gpoConnection.Size = new System.Drawing.Size(593, 74);
            this.gpoConnection.TabIndex = 0;
            this.gpoConnection.TabStop = false;
            this.gpoConnection.Text = "Connection establishment";
            // 
            // cboParityBit
            // 
            this.cboParityBit.FormattingEnabled = true;
            this.cboParityBit.Location = new System.Drawing.Point(271, 20);
            this.cboParityBit.Name = "cboParityBit";
            this.cboParityBit.Size = new System.Drawing.Size(128, 21);
            this.cboParityBit.TabIndex = 12;
            // 
            // cboStopBit
            // 
            this.cboStopBit.FormattingEnabled = true;
            this.cboStopBit.Location = new System.Drawing.Point(457, 20);
            this.cboStopBit.Name = "cboStopBit";
            this.cboStopBit.Size = new System.Drawing.Size(128, 21);
            this.cboStopBit.TabIndex = 11;
            // 
            // lblStopBits
            // 
            this.lblStopBits.AutoSize = true;
            this.lblStopBits.Location = new System.Drawing.Point(405, 23);
            this.lblStopBits.Name = "lblStopBits";
            this.lblStopBits.Size = new System.Drawing.Size(46, 13);
            this.lblStopBits.TabIndex = 10;
            this.lblStopBits.Text = "StopBits";
            // 
            // nudDatabits
            // 
            this.nudDatabits.Location = new System.Drawing.Point(271, 47);
            this.nudDatabits.Maximum = new decimal(new int[] {
            10,
            0,
            0,
            0});
            this.nudDatabits.Name = "nudDatabits";
            this.nudDatabits.Size = new System.Drawing.Size(128, 20);
            this.nudDatabits.TabIndex = 7;
            this.nudDatabits.Value = new decimal(new int[] {
            8,
            0,
            0,
            0});
            // 
            // label4
            // 
            this.label4.AutoSize = true;
            this.label4.Location = new System.Drawing.Point(215, 23);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(50, 13);
            this.label4.TabIndex = 6;
            this.label4.Text = "ParityBits";
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Location = new System.Drawing.Point(219, 49);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(46, 13);
            this.label3.TabIndex = 5;
            this.label3.Text = "Databits";
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(21, 49);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(32, 13);
            this.label1.TabIndex = 4;
            this.label1.Text = "Baud";
            // 
            // nudBaudRate
            // 
            this.nudBaudRate.Location = new System.Drawing.Point(59, 47);
            this.nudBaudRate.Maximum = new decimal(new int[] {
            115200,
            0,
            0,
            0});
            this.nudBaudRate.Name = "nudBaudRate";
            this.nudBaudRate.Size = new System.Drawing.Size(150, 20);
            this.nudBaudRate.TabIndex = 3;
            this.nudBaudRate.Value = new decimal(new int[] {
            115200,
            0,
            0,
            0});
            // 
            // cboComPorts
            // 
            this.cboComPorts.FormattingEnabled = true;
            this.cboComPorts.Location = new System.Drawing.Point(59, 20);
            this.cboComPorts.Name = "cboComPorts";
            this.cboComPorts.Size = new System.Drawing.Size(150, 21);
            this.cboComPorts.TabIndex = 1;
            // 
            // lblComPort
            // 
            this.lblComPort.AutoSize = true;
            this.lblComPort.Location = new System.Drawing.Point(6, 23);
            this.lblComPort.Name = "lblComPort";
            this.lblComPort.Size = new System.Drawing.Size(47, 13);
            this.lblComPort.TabIndex = 0;
            this.lblComPort.Text = "ComPort";
            // 
            // btnSerialConnector
            // 
            this.btnSerialConnector.Location = new System.Drawing.Point(612, 33);
            this.btnSerialConnector.Name = "btnSerialConnector";
            this.btnSerialConnector.Size = new System.Drawing.Size(99, 39);
            this.btnSerialConnector.TabIndex = 2;
            this.btnSerialConnector.Text = "Connect";
            this.btnSerialConnector.UseVisualStyleBackColor = true;
            this.btnSerialConnector.Click += new System.EventHandler(this.btnSerialConnector_Click);
            // 
            // gpoCommands
            // 
            this.gpoCommands.Controls.Add(this.groupBox4);
            this.gpoCommands.Controls.Add(this.groupBox2);
            this.gpoCommands.Location = new System.Drawing.Point(13, 93);
            this.gpoCommands.Name = "gpoCommands";
            this.gpoCommands.Size = new System.Drawing.Size(387, 447);
            this.gpoCommands.TabIndex = 1;
            this.gpoCommands.TabStop = false;
            this.gpoCommands.Text = "Commands";
            // 
            // groupBox4
            // 
            this.groupBox4.Controls.Add(this.btnLoadCommands);
            this.groupBox4.Controls.Add(this.btnSaveCommands);
            this.groupBox4.Controls.Add(this.btnSaveAll);
            this.groupBox4.Controls.Add(this.btnLoadAll);
            this.groupBox4.Location = new System.Drawing.Point(6, 364);
            this.groupBox4.Name = "groupBox4";
            this.groupBox4.Size = new System.Drawing.Size(362, 74);
            this.groupBox4.TabIndex = 4;
            this.groupBox4.TabStop = false;
            this.groupBox4.Text = "Load / Save / Store";
            // 
            // btnLoadCommands
            // 
            this.btnLoadCommands.Location = new System.Drawing.Point(185, 16);
            this.btnLoadCommands.Name = "btnLoadCommands";
            this.btnLoadCommands.Size = new System.Drawing.Size(171, 23);
            this.btnLoadCommands.TabIndex = 3;
            this.btnLoadCommands.Text = "Load commands";
            this.btnLoadCommands.UseVisualStyleBackColor = true;
            this.btnLoadCommands.Click += new System.EventHandler(this.btnLoadCommands_Click);
            // 
            // btnSaveCommands
            // 
            this.btnSaveCommands.Location = new System.Drawing.Point(185, 45);
            this.btnSaveCommands.Name = "btnSaveCommands";
            this.btnSaveCommands.Size = new System.Drawing.Size(171, 23);
            this.btnSaveCommands.TabIndex = 2;
            this.btnSaveCommands.Text = "Save commands";
            this.btnSaveCommands.UseVisualStyleBackColor = true;
            this.btnSaveCommands.Click += new System.EventHandler(this.btnSaveCommands_Click);
            // 
            // btnSaveAll
            // 
            this.btnSaveAll.Location = new System.Drawing.Point(6, 45);
            this.btnSaveAll.Name = "btnSaveAll";
            this.btnSaveAll.Size = new System.Drawing.Size(173, 23);
            this.btnSaveAll.TabIndex = 1;
            this.btnSaveAll.Text = "Save Commands and Runner";
            this.btnSaveAll.UseVisualStyleBackColor = true;
            this.btnSaveAll.Click += new System.EventHandler(this.btnSaveAll_Click);
            // 
            // btnLoadAll
            // 
            this.btnLoadAll.Location = new System.Drawing.Point(6, 16);
            this.btnLoadAll.Name = "btnLoadAll";
            this.btnLoadAll.Size = new System.Drawing.Size(173, 23);
            this.btnLoadAll.TabIndex = 0;
            this.btnLoadAll.Text = "Load Commands and Runner";
            this.btnLoadAll.UseVisualStyleBackColor = true;
            this.btnLoadAll.Click += new System.EventHandler(this.btnLoadAll_Click);
            // 
            // groupBox2
            // 
            this.groupBox2.Controls.Add(this.nudPreCommand);
            this.groupBox2.Controls.Add(this.btnAddPreCommand);
            this.groupBox2.Controls.Add(this.cboPreCommands);
            this.groupBox2.Controls.Add(this.btnRemoveCommand);
            this.groupBox2.Controls.Add(this.nudRunnerAmount);
            this.groupBox2.Controls.Add(this.btnAddToRunner);
            this.groupBox2.Controls.Add(this.label2);
            this.groupBox2.Controls.Add(this.lboCommands);
            this.groupBox2.Controls.Add(this.btnNewCommmand);
            this.groupBox2.Controls.Add(this.txtNewCommand);
            this.groupBox2.Location = new System.Drawing.Point(6, 19);
            this.groupBox2.Name = "groupBox2";
            this.groupBox2.Size = new System.Drawing.Size(375, 339);
            this.groupBox2.TabIndex = 3;
            this.groupBox2.TabStop = false;
            this.groupBox2.Text = "Command List";
            // 
            // nudPreCommand
            // 
            this.nudPreCommand.Location = new System.Drawing.Point(185, 309);
            this.nudPreCommand.Maximum = new decimal(new int[] {
            10000,
            0,
            0,
            0});
            this.nudPreCommand.Minimum = new decimal(new int[] {
            1,
            0,
            0,
            0});
            this.nudPreCommand.Name = "nudPreCommand";
            this.nudPreCommand.Size = new System.Drawing.Size(54, 20);
            this.nudPreCommand.TabIndex = 11;
            this.nudPreCommand.Value = new decimal(new int[] {
            1,
            0,
            0,
            0});
            // 
            // btnAddPreCommand
            // 
            this.btnAddPreCommand.Location = new System.Drawing.Point(245, 306);
            this.btnAddPreCommand.Name = "btnAddPreCommand";
            this.btnAddPreCommand.Size = new System.Drawing.Size(124, 23);
            this.btnAddPreCommand.TabIndex = 10;
            this.btnAddPreCommand.Text = "Add to runner";
            this.btnAddPreCommand.UseVisualStyleBackColor = true;
            this.btnAddPreCommand.Click += new System.EventHandler(this.btnAddPreCommand_Click);
            // 
            // cboPreCommands
            // 
            this.cboPreCommands.FormattingEnabled = true;
            this.cboPreCommands.Location = new System.Drawing.Point(9, 308);
            this.cboPreCommands.Name = "cboPreCommands";
            this.cboPreCommands.Size = new System.Drawing.Size(170, 21);
            this.cboPreCommands.TabIndex = 9;
            // 
            // btnRemoveCommand
            // 
            this.btnRemoveCommand.Location = new System.Drawing.Point(245, 277);
            this.btnRemoveCommand.Name = "btnRemoveCommand";
            this.btnRemoveCommand.Size = new System.Drawing.Size(124, 23);
            this.btnRemoveCommand.TabIndex = 8;
            this.btnRemoveCommand.Text = "Remove Command";
            this.btnRemoveCommand.UseVisualStyleBackColor = true;
            this.btnRemoveCommand.Click += new System.EventHandler(this.btnRemoveCommand_Click);
            // 
            // nudRunnerAmount
            // 
            this.nudRunnerAmount.Location = new System.Drawing.Point(68, 251);
            this.nudRunnerAmount.Name = "nudRunnerAmount";
            this.nudRunnerAmount.Size = new System.Drawing.Size(171, 20);
            this.nudRunnerAmount.TabIndex = 7;
            this.nudRunnerAmount.Value = new decimal(new int[] {
            1,
            0,
            0,
            0});
            // 
            // btnAddToRunner
            // 
            this.btnAddToRunner.Location = new System.Drawing.Point(245, 248);
            this.btnAddToRunner.Name = "btnAddToRunner";
            this.btnAddToRunner.Size = new System.Drawing.Size(124, 23);
            this.btnAddToRunner.TabIndex = 6;
            this.btnAddToRunner.Text = "Add to runner";
            this.btnAddToRunner.UseVisualStyleBackColor = true;
            this.btnAddToRunner.Click += new System.EventHandler(this.btnAddToRunner_Click);
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(19, 253);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(43, 13);
            this.label2.TabIndex = 4;
            this.label2.Text = "Amount";
            // 
            // lboCommands
            // 
            this.lboCommands.FormattingEnabled = true;
            this.lboCommands.Location = new System.Drawing.Point(9, 19);
            this.lboCommands.Name = "lboCommands";
            this.lboCommands.Size = new System.Drawing.Size(360, 199);
            this.lboCommands.TabIndex = 0;
            // 
            // btnNewCommmand
            // 
            this.btnNewCommmand.Location = new System.Drawing.Point(245, 219);
            this.btnNewCommmand.Name = "btnNewCommmand";
            this.btnNewCommmand.Size = new System.Drawing.Size(124, 23);
            this.btnNewCommmand.TabIndex = 2;
            this.btnNewCommmand.Text = "Add new command";
            this.btnNewCommmand.UseVisualStyleBackColor = true;
            this.btnNewCommmand.Click += new System.EventHandler(this.btnNewCommmand_Click);
            // 
            // txtNewCommand
            // 
            this.txtNewCommand.Location = new System.Drawing.Point(9, 221);
            this.txtNewCommand.Name = "txtNewCommand";
            this.txtNewCommand.Size = new System.Drawing.Size(230, 20);
            this.txtNewCommand.TabIndex = 1;
            // 
            // gpoRunner
            // 
            this.gpoRunner.Controls.Add(this.lboRunner);
            this.gpoRunner.Controls.Add(this.btnStartRunner);
            this.gpoRunner.Controls.Add(this.btnRemFromRunner);
            this.gpoRunner.Location = new System.Drawing.Point(412, 93);
            this.gpoRunner.Name = "gpoRunner";
            this.gpoRunner.Size = new System.Drawing.Size(290, 447);
            this.gpoRunner.TabIndex = 4;
            this.gpoRunner.TabStop = false;
            this.gpoRunner.Text = "Command Runner";
            // 
            // lboRunner
            // 
            this.lboRunner.FormattingEnabled = true;
            this.lboRunner.Location = new System.Drawing.Point(7, 20);
            this.lboRunner.Name = "lboRunner";
            this.lboRunner.Size = new System.Drawing.Size(277, 355);
            this.lboRunner.TabIndex = 3;
            // 
            // btnStartRunner
            // 
            this.btnStartRunner.Location = new System.Drawing.Point(6, 409);
            this.btnStartRunner.Name = "btnStartRunner";
            this.btnStartRunner.Size = new System.Drawing.Size(278, 23);
            this.btnStartRunner.TabIndex = 2;
            this.btnStartRunner.Text = "Start Com-Port Runner";
            this.btnStartRunner.UseVisualStyleBackColor = true;
            this.btnStartRunner.Click += new System.EventHandler(this.btnStartRunner_Click);
            // 
            // btnRemFromRunner
            // 
            this.btnRemFromRunner.Location = new System.Drawing.Point(6, 380);
            this.btnRemFromRunner.Name = "btnRemFromRunner";
            this.btnRemFromRunner.Size = new System.Drawing.Size(278, 23);
            this.btnRemFromRunner.TabIndex = 1;
            this.btnRemFromRunner.Text = "Remove selected Item from Runner";
            this.btnRemFromRunner.UseVisualStyleBackColor = true;
            this.btnRemFromRunner.Click += new System.EventHandler(this.btnRemFromRunner_Click);
            // 
            // fdOpenFile
            // 
            this.fdOpenFile.FileName = "openFileDialog1";
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(714, 546);
            this.Controls.Add(this.gpoRunner);
            this.Controls.Add(this.gpoCommands);
            this.Controls.Add(this.gpoConnection);
            this.Controls.Add(this.btnSerialConnector);
            this.Name = "Form1";
            this.Text = "Serial Port Tester";
            this.gpoConnection.ResumeLayout(false);
            this.gpoConnection.PerformLayout();
            ((System.ComponentModel.ISupportInitialize)(this.nudDatabits)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.nudBaudRate)).EndInit();
            this.gpoCommands.ResumeLayout(false);
            this.groupBox4.ResumeLayout(false);
            this.groupBox2.ResumeLayout(false);
            this.groupBox2.PerformLayout();
            ((System.ComponentModel.ISupportInitialize)(this.nudPreCommand)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.nudRunnerAmount)).EndInit();
            this.gpoRunner.ResumeLayout(false);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.GroupBox gpoConnection;
        private System.Windows.Forms.Button btnSerialConnector;
        private System.Windows.Forms.ComboBox cboComPorts;
        private System.Windows.Forms.Label lblComPort;
        private System.Windows.Forms.GroupBox gpoCommands;
        private System.Windows.Forms.GroupBox gpoRunner;
        private System.Windows.Forms.GroupBox groupBox2;
        private System.Windows.Forms.ListBox lboCommands;
        private System.Windows.Forms.Button btnNewCommmand;
        private System.Windows.Forms.TextBox txtNewCommand;
        private System.Windows.Forms.GroupBox groupBox4;
        private System.Windows.Forms.Button btnSaveAll;
        private System.Windows.Forms.Button btnLoadAll;
        private System.Windows.Forms.NumericUpDown nudRunnerAmount;
        private System.Windows.Forms.Button btnAddToRunner;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Button btnStartRunner;
        private System.Windows.Forms.Button btnRemFromRunner;
        private System.Windows.Forms.Button btnRemoveCommand;
        private System.Windows.Forms.Button btnLoadCommands;
        private System.Windows.Forms.Button btnSaveCommands;
        private System.Windows.Forms.Label lblStopBits;
        private System.Windows.Forms.NumericUpDown nudDatabits;
        private System.Windows.Forms.Label label4;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.NumericUpDown nudBaudRate;
        private System.Windows.Forms.ComboBox cboStopBit;
        private System.Windows.Forms.ComboBox cboParityBit;
        private System.Windows.Forms.ListBox lboRunner;
        private System.Windows.Forms.OpenFileDialog fdOpenFile;
        private System.Windows.Forms.SaveFileDialog fdSaveFile;
        private System.Windows.Forms.Button btnAddPreCommand;
        private System.Windows.Forms.ComboBox cboPreCommands;
        private System.Windows.Forms.NumericUpDown nudPreCommand;
    }
}

