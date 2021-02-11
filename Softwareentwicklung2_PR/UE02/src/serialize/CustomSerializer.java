package serialize;

import java.io.BufferedReader;
import java.io.PrintWriter;

public interface CustomSerializer {
	void writeValue(Object value, PrintWriter out, ObjectWriter parent) throws Exception;
	
	Object readValue(BufferedReader in, ObjectReader parent) throws Exception;
}
