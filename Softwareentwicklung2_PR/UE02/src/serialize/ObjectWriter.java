package serialize;

import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.IdentityHashMap;

public class ObjectWriter {
	private int idCounter = 0;
	private IdentityHashMap<Object, Integer> map = new IdentityHashMap<>();
	private CustomSerializer serializer;

	public void writeObject(Object o, PrintWriter out) throws Exception {
		if (map.containsKey(o)) {
			out.println(map.get(o));
		} else {
			map.put(o, idCounter++);

			Class<?> oClass = o.getClass();
			/* Write class name first */
			out.println(oClass.getName());

			/* Write attributes of class */
			for (Field f : Utilities.getAllFields(oClass)) {
				f.setAccessible(true);
				out.println(f.getName());
				//Decide which method to use for writing
				if (f.get(o) == null) {
					out.println("null");
				} else if (Utilities.existsCustomSerializer(f)) {
					serializer = f.getAnnotation(UseSerializer.class).value().getConstructor().newInstance();
					serializer.writeValue(f.get(o), out, this);
				}  else if (f.getType().isPrimitive() || f.getType().equals(String.class)){
					out.println(f.get(o));
				} else {
					writeObject(f.get(o), out);
				}
			}
		}
	}
}
