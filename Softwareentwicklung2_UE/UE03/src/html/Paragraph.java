package html;

public final class Paragraph extends ContainerElement {

	//Constructors
	/**
	 * Constructor that gets elements for the paragraph
	 * @param elements
	 */
	Paragraph(Element[] elements) {
		super(elements);
	}

	//Getter
	/**
	 * Returns "p" tag for the paragraph
	 * @return String "p"
	 */
	@Override
	String getTag() {
		return "p";
	}
}
