package serialize;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utilities {
	
	public static  boolean existsCustomSerializer(Field f) {
		return f.isAnnotationPresent(UseSerializer.class);
	}
	
	public static Field[] getAllFields(Class<?> clss) {
		List<Field> fields = new ArrayList<>();
		Class<?> c = clss; 
		while (c != null) {
			fields.addAll(Arrays.asList(c.getDeclaredFields())); 
			c = c.getSuperclass(); 
		}
		return fields.toArray(new Field[fields.size()]); 
	}


}
