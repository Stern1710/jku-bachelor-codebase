package html;

abstract class ContainerElement extends TaggedElement {
	//Fields
	private final Element[] elements;
	
	//Constructors
	/**
	 * Constructor accessible by sub-classes to set the elements
	 * @param elements Array of Elements 
	 */
	ContainerElement (Element[] elements) {
		this.elements = elements;
	}
	
	//Getter
	/**
	 * Returns Array of Elements
	 * @return Element array
	 */
	Element[] getElements() {
		return elements;
	}
	
	/**
	 * Let getTag be abstract as the ContainerElement does not have a tag itself
	 * Must be implemented by more specified classes
	 */
	@Override
	abstract String getTag();

	//Methods
	/**
	 * Renders the elements in the local array
	 * @return Rendered Elements in a String
	 */
	@Override
	String render() {
		StringBuilder builder = new StringBuilder();
		builder.append("<" + getTag() + ">\n");
		
		for (Element e : getElements()) {
			builder.append(Globals.indentLines(e.render()));
		}
			
		builder.append("</" + getTag() + ">\n");	
		return builder.toString();
	}

}
