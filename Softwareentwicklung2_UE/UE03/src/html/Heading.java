package html;

public final class Heading extends TaggedElement {
	//Fields
	private final int level;
	private final String text;

	//Constructor
	/**
	 * Constructor for Heading
	 * @param level 1-6, level of the heading
	 * @param text Text inside the heading
	 */
	Heading(int level, String text) {
		//Check if level is inside 1..6 range
		//If out, set to according bound (otherwise headings not displayed correctly)
		if (level < 1) {
			this.level = 1;
		} else if (level > 6) {
			this.level = 6;
		} else {
			this.level = level;
		}
				
		this.text = text;
	}

	//Getter
	/**
	 * Returns h tag with the level
	 * Example level = 3 returns "h3"
	 */
	@Override
	String getTag() {
		return "h" + level;
	}

	//Methods
	/**
	 * Renders the header with given level
	 * @return Rendered heading
	 */
	@Override
	String render() {
		return "<" + getTag() + ">" + text + "</" + getTag() + ">\n";
	}
	
}
