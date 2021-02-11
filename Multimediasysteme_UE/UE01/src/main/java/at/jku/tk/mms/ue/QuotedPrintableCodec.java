package at.jku.tk.mms.ue;

public class QuotedPrintableCodec {
	
	public static final char DEFAULT_QUOTE_CHAR	= '=';
	
	private char quoteChar;
	
	public QuotedPrintableCodec() {
		this(DEFAULT_QUOTE_CHAR);
	}
	
	public QuotedPrintableCodec(char quoteChar) {
		this.quoteChar = quoteChar;
	}
	
	/**
	 * Encodes a String from ASCII to QuitedPrintables
	 * @param plain The not decoded string
	 * @return The decoded string
	 */
	public String encode(String plain) {
		StringBuffer quoted = new StringBuffer();
		
		for (int i=0; i < plain.length(); i++) {
			char cur = plain.charAt(i);
			if (cur < 127 && cur != quoteChar) {
				//When a characters int value < 127, it can be directly written to the output string
				quoted.append(cur);
			} else {
				//Otherwise get HEX value and write it with an "=" into the output string
				quoted.append(quoteChar + Integer.toHexString(cur).toUpperCase());
			}
		}		
		return quoted.toString();
	}

	/**
	 * Decodes a QuotedPrintable back into ASCII
	 * @param quoted the quoted decimal string
	 * @return ASCII-character string
	 */
	public String decode(String quoted) {
		StringBuffer plain = new StringBuffer();
		
		for (int i=0; i < quoted.length(); i++) {
			char cur = quoted.charAt(i);
			if (cur == quoteChar) {
				//If the current char is the same as "=", parse the next to characters from hex to char (as ASCII) and append
				plain.append((char)Integer.parseInt(quoted.substring(i+1, i+3), 16));
				//Increase the counter to skip next two character as they were already used for hex to char convertion
				i += 2;
			} else {
				plain.append(cur);
			}
		}						
		return plain.toString();
	}	
}
