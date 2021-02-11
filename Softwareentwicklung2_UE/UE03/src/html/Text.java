package html;

public final class Text extends Element {  
	//Fields
	private final String text;
	
	//Constructors
	/**
	 * Constructor that sets the text of the Element
	 * @param text The text of this element
	 */
	Text(String text) {
		this.text = text;
	}

	//Methods
	/**
	 * Renders the text with a newline at the end
	 * @return text with newline
	 */
	@Override
	String render() {
		return text + "\n";
	}

}
