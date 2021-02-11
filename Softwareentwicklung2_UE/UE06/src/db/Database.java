package db;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class Database {
	//FIELDS
	private final Map<String, Person> persons;
	private final Map<Integer, Animal> animals;
	
	//CONSTRUCTORS
	public Database() {
		persons = new HashMap<String, Person>();
		animals = new HashMap<Integer, Animal>();
	}
	
	//METHODS
	public void addPerson(String name) {
		persons.put(name, new Person(name));
	}
	
	public void addAnimal(Person owner, int id, String name, Animal father, Animal mother) {
		Animal newA = new Animal(owner, id, name, father, mother);
		
		animals.put(newA.getId(), newA);
		persons.get(owner.getName()).add(newA);
		
		if (father != null) {
			animals.get(father.getId()).addChild(newA);
		}
		if (mother != null) {
			animals.get(mother.getId()).addChild(newA);
		}
	}
	
	public void tradeAnimal(Animal animal, Person newOwner) {
		animal.getOwner().remove(animal);
		newOwner.add(animal);
	}
	
	//Other GETTER
	public SortedSet<Person> getPersons() {
		SortedSet<Person> sorted = new TreeSet<Person>();
		persons.entrySet().forEach(entry -> sorted.add(entry.getValue()));
		
		return sorted;
	}
	
	public SortedSet<Person> getPersons(Comparator<Person> comparator) {
		SortedSet<Person> sorted = new TreeSet<Person>(comparator);
		persons.entrySet().forEach(entry -> sorted.add(entry.getValue()));
		
		return sorted;
	}
	
	public SortedSet<Animal> getAnimals() {
		SortedSet<Animal> sorted = new TreeSet<Animal>();
		animals.entrySet().forEach(entry -> sorted.add(entry.getValue()));
		
		return sorted;
	}
	
	public SortedSet<Animal> getAnimals(Comparator<Animal> comparator) {
		SortedSet<Animal> sorted = new TreeSet<Animal>(comparator);
		animals.entrySet().forEach(entry -> sorted.add(entry.getValue()));
		
		return sorted;
	}
	
	//GETTER
	public Person getPerson (String name) {
		return persons.get(name);
	}
	
	public Animal getAnimal (int id) {
		return animals.get(id);
	}
}
