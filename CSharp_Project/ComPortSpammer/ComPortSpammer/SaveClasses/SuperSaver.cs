using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml.Serialization;

namespace ComPortSpammer
{
    public class SuperSaver : CommandSaver
    {
        [XmlElement("runner")]
        public List<string> Runner { get; set; }

        public SuperSaver() { }

        public SuperSaver(string[] commands, string[] runners) : base(commands)
        {
            Runner = new List<string>(runners);
        }
    }
}
