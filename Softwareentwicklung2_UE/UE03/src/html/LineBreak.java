package html;

public final class LineBreak extends TaggedElement {
	
	//
	/**
	 * Default constructor
	 */
	LineBreak() {}
	
	//Getter
	/**
	 * Returns the "br" tag
	 * @return "br" for the linebreak tag
	 */
	@Override
	String getTag() {
		return "br";
	}

	//Methods
	/**
	 * Renders a <br />
	 * @return String with <br /> in it for a linebreak
	 */
	@Override
	String render() {
		return "<" + getTag() + "/>";
	}
}
