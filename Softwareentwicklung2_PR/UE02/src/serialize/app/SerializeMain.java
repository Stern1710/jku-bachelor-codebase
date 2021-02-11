package serialize.app;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import person.Person;
import serialize.ObjectReader;
import serialize.ObjectWriter;

public class SerializeMain {

	public static void main(String[] args) {

		Person father = new Person(); 
		father.setName("Frank");
		Person mother = new Person(); 
		mother.setName("Ann");
		Person child1 = new Person(); 
		child1.setName("Kevin");
		Person child2 = new Person(); 
		child2.setName("Lisa");
		
		father.setSpouse(mother);
		mother.setSpouse(father);
		
		father.addChild(child1);
		father.addChild(child2);
		
		mother.setChildren(father.getChildren());
		
		child1.setFather(father);
		child1.setMother(mother);
		
		child2.setFather(father);
		child2.setMother(mother);

		ObjectWriter ow = new ObjectWriter(); 
		
		try (PrintWriter out = new PrintWriter(new FileOutputStream("SerizalizedObject.txt"))) {
			ow.writeObject(father, out); 
		} catch (Exception e) {
			e.printStackTrace();
		} 

		ObjectReader or = new ObjectReader(); 
		
		try (BufferedReader in = 
				new BufferedReader(
						new InputStreamReader(
								new FileInputStream("SerizalizedObject.txt")))) {
			Person person = (Person)or.readObject(in); 
			System.out.println(person.toString()); 
			Person p1 = person.getSpouse(); 
			System.out.println(p1.toString()); 
			
			for(Person p : person.getChildren()) {
				System.out.println(p.toString());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
