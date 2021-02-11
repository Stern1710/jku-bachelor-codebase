using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml.Serialization;

namespace ComPortSpammer
{
    [XmlRoot("command_saver")]
    public class CommandSaver
    { 
        [XmlElement("command")]
        public List<string> Commands { get; set; }

        /// <summary>
        /// Standard constructor needed for serialization
        /// </summary>
        public CommandSaver() { }

        public CommandSaver(string[] commands)
        {
            Commands = new List<string>(commands);
        }
    }
}
