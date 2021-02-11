package db;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class Animal implements Comparable<Animal> {
	//FIELDS
	private Person owner;
	private final int id;
	private final String name;
	private final Animal father;
	private final Animal mother;
	private final Map<Integer, Animal> children;
	
	//CONSTRUCTORS
	Animal (Person owner, int id, String name, Animal father, Animal mother) {
		this.owner = owner;
		this.id = id;
		this.name = name;
		this.father = father;
		this.mother = mother;
		children = new HashMap<Integer, Animal>();
	}

	//GETTER and SETTER
	public Person getOwner() {
		return owner;
	}
	
	public void setOwner (Person owner) {
		this.owner = owner;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	void addChild(Animal child) {
		children.put(child.getId(), child);
	}
	
	//Other GETTER
	public SortedSet<Animal> getChildren() {
		SortedSet<Animal> sorted = new TreeSet<Animal>();
		children.entrySet().forEach(entry -> sorted.add(entry.getValue()));
		
		return sorted;
	}
	
	/**
	 * Returns a list of all ancestors of given animal 
	 * @return SortedSet of all ancestors
	 */
	public SortedSet<Animal> getAncestor() {
		SortedSet<Animal> sorted = new TreeSet<Animal>();
		
		if (father != null) {
			sorted.add(father);
			sorted.addAll(father.getAncestor());
		}
		
		if (mother != null) {
			sorted.add(mother);
			sorted.addAll(mother.getAncestor());
		}
		
		return sorted;
	}
	
	/**
	 * Returns a List of all Descendants of the animal
	 * @return SortedSet of all descendants
	 */
	public SortedSet<Animal> getDescendant() {
		SortedSet<Animal> sorted = new TreeSet<>();
		
		sorted.addAll(getChildren());
		getChildren().forEach(entry -> sorted.addAll(entry.getDescendant()));
		
		return sorted;
	}
	
	public int getNumberOfAncestors() {
		return getAncestor().size();
	}
	
	public int getNumberOfDescendants() {
		return getDescendant().size();
	}
	
	@Override
	public String toString() {
		return id +": " + name + "; Father: " + father.getName() + "; Mother: " + mother.getName();
		
	}

	@Override
	public int compareTo(Animal o) {
		return this.getId() - o.getId();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		Animal other = (Animal) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}
