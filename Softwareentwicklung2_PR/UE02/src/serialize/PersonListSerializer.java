package serialize;

import person.Person;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class PersonListSerializer implements CustomSerializer {

	@Override
	public void writeValue(Object value, PrintWriter out, ObjectWriter parent) throws Exception {
		List<Person> pList = (List<Person>)value; //Assuming we can make a explicit typecast as this is a -PersonList- Serializer
		out.println("List<Person>");
		out.println("length=" + pList.size());

		for (Person p : pList) {
			parent.writeObject(p, out);
		}
	}

	@Override
	public Object readValue(BufferedReader in, ObjectReader parent) throws Exception {
		List<Object> list = new ArrayList<>();
		//Read data type and length
		in.readLine(); //Skip the list declaration
		int length = Integer.parseInt(in.readLine().split("=")[1]);

		for (int i=0; i < length; i++) {
			list.add(parent.readObject(in));
		}

		return list;
	}
}
