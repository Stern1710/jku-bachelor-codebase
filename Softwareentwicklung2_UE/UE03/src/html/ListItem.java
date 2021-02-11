package html;

public final class ListItem extends ContainerElement {
	
	//Constructor
	/**
	 * Constructor that passes all elements for list items
	 * @param elements Elements in the list
	 */
	ListItem(Element[] elements) {
		super(elements);
	}

	//Getter
	/**
	 * Returns the tag for ListItem
	 * @return String with "li" Tag
	 */
	@Override
	String getTag() {
		return "li";
	}
}
