package html;

public class Html {
	
	public static Page page(String title, Element...elements) {
		return new Page(title, elements); 
	}
	public static Text text(String text) {
		return new Text(text); 
	}
	
	public static LineBreak br() {
		return new LineBreak(); 
	}

	public static Heading h1(String h) {
		return new Heading(1, h); 
	}

	public static Heading h2(String h) {
		return new Heading(2, h); 
	}

	public static Heading h3(String h) {
		return new Heading(3, h); 
	}

	public static Paragraph para(Element...elements) {
		return new Paragraph(elements); 
	}
	
	public static List ol(ListItem...items) {
		return new List(true, items); 
	}
	
	public static List ul(ListItem...items) {
		return new List(false, items); 
	}
	
	public static ListItem item(Element...elements) {
		return new ListItem(elements); 
	}

}
