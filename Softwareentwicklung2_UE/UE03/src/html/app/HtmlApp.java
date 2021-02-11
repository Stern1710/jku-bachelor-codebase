package html.app;

import html.*;
import inout.Out;

import static html.Html.*;

public class HtmlApp {
	public static void main(String[] args) {
		Page page = 
			page("The rendering tool description page", 
				h1("The HTML Renderer Tool"),
				h2("Description:"),
				para(
					text("Hi everybody!"),
					text("This is a test of the html renderer, which is a fantastic tool for creating web pages.")
				),
				h2("Operations:"),
				para(
					text("A class Html is provided to create a html page. You can create: "),
					ul(
						item(text("pages with title and body: use method page")),
						item(text("paragraphs: use method para")), 
						item(text("lists: use method list")),
						item(text("items in lists: use method item")),
						item(text("text elements: use method text")), 
						item(text("line breaks: use method br"))
					),
					h2("Implementation:"),
					text("Proceed as follows: "), 
					ol(
						item(text("First create the classes")), 
						item(text("Then implement the method render, getTag etc.")),
						item(text("Finally test your program with the HtmlApp main class"))
					),
					text("Good luck!"))
				);

		String html = page.render();
		Out.open("page.html");
		Out.println(html);
		Out.close();
	}

}
