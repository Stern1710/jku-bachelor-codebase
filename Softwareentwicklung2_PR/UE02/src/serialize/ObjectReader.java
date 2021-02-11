package serialize;

import java.io.BufferedReader;
import java.lang.reflect.Field;
import java.util.IdentityHashMap;

public class ObjectReader {
	private int idCounter=0;
	private IdentityHashMap<Integer, Object> map = new IdentityHashMap<>();
	private CustomSerializer serializer;

	public Object readObject(BufferedReader in) throws Exception {
		//Get the new object with the readObject
		String input = in.readLine();

		//Check for number
		//If number, return existing element from the map
		if (input.matches("(0|[1-9]\\d*)")) {
			return map.get(Integer.parseInt(input));
		} else if ("null".equals(input)) {
			return null;
		}
		Class<?> oClass = Class.forName(input);
		Object o = oClass.getConstructor().newInstance();
		map.put(idCounter++, o);

		//Read all fields of the object
		for (Field f : Utilities.getAllFields(oClass)) {
			input = in.readLine(); //Throw away one line and then read all the values

			if (f.getType().equals(String.class)) {
				//Just read in the string, no matter what it is
				f.set(o, in.readLine());
			} else if (Utilities.existsCustomSerializer(f)) {
				//Use custom serializer
				serializer = f.getAnnotation(UseSerializer.class).value().getConstructor().newInstance();
				f.set(o, serializer.readValue(in, this));
			} else if (f.getType().isPrimitive()) {
				//Check for primitives types and convert
				String type = f.getClass().getSimpleName();
				if (type.equals(Byte.TYPE)) {
					f.set(o, Byte.parseByte(input));
				} else if (type.equals(Short.TYPE)) {
					f.set(o, Short.parseShort(input));
				} else if (type.equals(Integer.TYPE)) {
					f.set(o, Integer.parseInt(input));
				} else if (type.equals(Long.TYPE)) {
					f.set(o, Long.parseLong(input));
				} else if (type.equals(Character.TYPE)) {
					f.set(o, input);
				} else if (type.equals(Boolean.TYPE)) {
					f.set(o, Boolean.parseBoolean(input));
				} else if (type.equals(Double.TYPE)) {
					f.set(o, Double.parseDouble(input));
				} else if (type.equals(Float.TYPE)) {
					f.set(o, Float.parseFloat(input));
				}
			}  else {
				//Just read in a new object
				f.set(o, readObject(in));
			}
		}

		return o;
}
}
