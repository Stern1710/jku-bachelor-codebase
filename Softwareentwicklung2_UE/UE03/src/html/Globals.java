package html;

public class Globals {
	
	public static final String BLANKS = "  ";

	public static String indentLines(String lines) {
		StringBuilder b = new StringBuilder(lines);
		int idx = 0; 
		while (idx >= 0 && idx < b.length() - 1) {
			b.insert(idx, BLANKS); 
			idx = b.indexOf("\n", idx + 1 + BLANKS.length()) + 1;
		}
		return b.toString(); 
	}

}
