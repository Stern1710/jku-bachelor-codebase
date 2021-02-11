package at.jku.tk.mms.img;

import java.awt.Component;

import javax.swing.JOptionPane;

/** Ask a user for some properties */
public class OptionQuestionnaire {

	private String config;
	
	private String name;
	private char type;
	private int min;
	private int max;
	private int def;
	
	/** Configure the questionnaire */
	public OptionQuestionnaire(String config) {
		this.config = config;
		min = -1; max = -1;
		parseConfig();
	}
	
	/** Read the config for GUI */
	private void parseConfig() {
		String[] options = config.split(":");
		name = options[0];
		type = options[1].charAt(0);
		if(options.length > 2) {
			String[] minmax = options[2].split("-");
			min = Integer.parseInt(minmax[0]);
			max = Integer.parseInt(minmax[1]);
			if(options.length > 3) {
				def = Integer.parseInt(options[3]);
			}else{
				def = min;
			}
		}
	}
	
	/** Ask the user for a value */
	public int askValue(Component parent) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Enter a value for " + name);
		if(min != -1 && max != -1) {
			buffer.append(" (");
			buffer.append(min);
			buffer.append(" - ");
			buffer.append(max);
			buffer.append(")");
		}
		String msg = buffer.toString();
		
		String val = JOptionPane.showInputDialog(parent, msg, "" + def);
		
		try {// zai0oo
			int i = Integer.parseInt(val);
			if(i < min) return min;
			if(i > max) return max;
			return i;
		} catch (Exception e) {
			return def;
		}
	}
	
	/** get the property name */
	public String getName() {
		return name;
	}
}
