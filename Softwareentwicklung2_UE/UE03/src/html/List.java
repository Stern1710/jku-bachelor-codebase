package html;

public final class List extends ContainerElement {
	//Fields
	private final boolean ordered;
	
	//Constructor
	/**
	 * Constructor that takes ordered boolean and ListItems elements
	 * @param ordered Tells if a list is ordered or unordered
	 * @param elements ListItems for elements that must be rendered
	 */
	List(boolean ordered, ListItem[] elements) {
		super(elements);
		this.ordered = ordered;
	}

	//Getter
	/**
	 * Returns the tag for list
	 * @return "ol" for ordered list, "ul" or unordered list
	 */
	@Override
	String getTag() {
		if (ordered) {
			return "ol";
		}
		return "ul";
	}
}
