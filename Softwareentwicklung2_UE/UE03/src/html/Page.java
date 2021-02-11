package html;

import static html.Globals.*;

public final class Page {
	//Fields
	private final String title;
	private final Element[] elements;

	//Constructors
	/**
	 * Constructor that sets title and elements for the page
	 * @param title Title of the webpage
	 * @param body All elements of the body
	 */
	Page(String title, Element[] body) {
		this.title = title;
		this.elements = body;
		
	} 
	
	//Methods
	/**
	 * Renders the body with all body elements inside
	 * @return The finished webpage as a String
	 */
	public String render() {   
		StringBuilder builder = new StringBuilder();
				
		builder.append("<html>\n");
		builder.append(indentLines("<head>\n"));
		//Intend two times for correct alignment
		builder.append(indentLines(indentLines("<title>" + title + "</title>\n")));
		builder.append(indentLines("</head>\n"));
		builder.append(indentLines("<body>\n"));
				
		for (Element e: elements) {
			//Intend all items two times for correct alignment
			builder.append(indentLines(indentLines(e.render())));
		}
		
		builder.append(indentLines("</body>\n"));
		builder.append("</html>\n");
		
		return builder.toString();
	}

}
