package person; 

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import serialize.UseSerializer;
import serialize.PersonListSerializer;

public class Person {
	
	public String name; 
	public Person father;
	public Person mother;
	public Person spouse;
	
	@UseSerializer(PersonListSerializer.class)
	public List<Person> children; 
	
	public Person() {
		this(null, null, null);
	}
	
	public Person(String name, Person father, Person mother) {
		this.father = father;
		this.mother = mother;
		this.children = new ArrayList<Person>();
	}
	
	public Person getSpouse() {
		return spouse;
	}

	public void setSpouse(Person spouse) {
		this.spouse = spouse;
	}


	public String getName() {
		return name;
	}

	public List<Person> getChildren() {
		return children;
	}

	public void setChildren(List<Person> children) {
		this.children = children;
	}
	
	public void addChild(Person child) {
		this.children.add(child);
	}

	public Person getFather() {
		return father;
	}

	public void setFather(Person father) {
		this.father = father;
	}

	public Person getMother() {
		return mother;
	}

	public void setMother(Person mother) {
		this.mother = mother;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(name);
		sb.append(": \n\tfather = ");
		sb.append(father == null ? "-" : father.getName());
		sb.append("\n\tmother = ");
		sb.append(mother == null ? "-" : mother.getName());
		sb.append("\n\tspouse = ");
		sb.append(spouse == null ? "-" : spouse.getName());
		sb.append("\n\tchildren = ");
		sb.append(children == null || children.size() == 0? "-" : children.stream().map(c -> c.getName()).collect(Collectors.joining(", ")));
		return sb.toString();
	}
	
}
