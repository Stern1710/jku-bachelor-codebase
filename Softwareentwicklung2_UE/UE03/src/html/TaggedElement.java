package html;

/**
 * Abstract class for all Elements with tags, defines abstract method getTag
 * @author Simon Sternbauer
 *
 */
abstract class TaggedElement extends Element {
	
	
	//Getter
	/**
	 * Returns the tag of the Element
	 * @return String with the tag
	 */
	abstract String getTag();
}
