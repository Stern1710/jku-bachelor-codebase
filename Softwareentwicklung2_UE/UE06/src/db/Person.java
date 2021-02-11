package db;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class Person implements Comparable<Person>{
	//FIELDS
	private final String name;
	private final Map<Integer, Animal> animals;
	
	//CONSTRUCTOR
	Person (String name) {
		this.name = name;
		animals = new HashMap<Integer, Animal>();
	}
	
	//GETTER
	public String getName() {
		return name;
	}
	
	public SortedSet<Animal> getAnimals() {
		SortedSet<Animal> sorted = new TreeSet<Animal>();
		animals.entrySet().forEach(entry -> sorted.add(entry.getValue()));
		
		return sorted;
	}
	
	//Other getter methods
	public SortedSet<Animal> getAnimalsSortedByName() {
		SortedSet<Animal> sorted = new TreeSet<Animal>((a1, a2) -> a1.getName().compareTo(a2.getName()));
		animals.entrySet().forEach(entry -> sorted.add(entry.getValue()));
		
		return sorted;
	}
	
	public SortedSet<Animal> getAnimalsSortedByAncestorCount() {
		SortedSet<Animal> sorted = new TreeSet<Animal>((a1, a2) -> a1.getNumberOfAncestors() - a2.getNumberOfAncestors());
		animals.entrySet().forEach(entry -> sorted.add(entry.getValue()));
		
		return sorted;
	}
	
	public SortedSet<Animal> getAnimalsSortedByDescendantCount() {
		SortedSet<Animal> sorted = new TreeSet<Animal>((a1, a2) -> a1.getNumberOfDescendants() - a2.getNumberOfDescendants());
		animals.entrySet().forEach(entry -> sorted.add(entry.getValue()));
		
		return sorted;
	}
	
	//METHODS
	void add(Animal animal) {
		animals.put(animal.getId(), animal);
	}
	
	void remove (Animal animal) {
		animals.remove(animal.getId());
	}
	
	@Override
	public String toString() {
		return getName();
	}

	@Override
	public int compareTo(Person o) {
		return this.getName().compareTo(o.getName());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
}
