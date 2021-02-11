package app;

import db.Database;
import db.Animal;

public class AnimalApp {
	public static void main(String[] args) {
		StringBuffer buffer = new StringBuffer();
		Database db = new Database();

		db.addPerson("Alf");
		db.addPerson("Beate");
		db.addPerson("David");
		db.addPerson("Christine");

		db.addAnimal(db.getPerson("Alf"), 0, "Hansi", null, null);
		db.addAnimal(db.getPerson("Beate"), 1, "Mausi", null, null);
		db.addAnimal(db.getPerson("David"), 2, "Fritzi", db.getAnimal(0), db.getAnimal(1));
		db.addAnimal(db.getPerson("David"), 3, "Frauli", db.getAnimal(0), db.getAnimal(1));
		db.addAnimal(db.getPerson("Christine"), 4, "Fratzi", db.getAnimal(0), db.getAnimal(1));
		db.addAnimal(db.getPerson("Christine"), 5, "Brummer", db.getAnimal(2), db.getAnimal(3));
		db.addAnimal(db.getPerson("Christine"), 6, "Mini", db.getAnimal(4), db.getAnimal(3));
		db.addAnimal(db.getPerson("Alf"), 7, "Jack", db.getAnimal(5), db.getAnimal(1));
		db.addAnimal(db.getPerson("Beate"), 8, "Beate die Zweite", db.getAnimal(0), db.getAnimal(6));

		db.tradeAnimal(db.getAnimal(0), db.getPerson("David"));

		//Implement asked queries
		buffer.append("Nachfahren von Hansi: ");
		for (Animal a : db.getAnimal(0).getDescendant()) {
			buffer.append(a.getName() + "; ");
		}
		buffer.append("\n");
		
		buffer.append("Nachfahren von Frauli: ");
		for (Animal a : db.getAnimal(3).getDescendant()) {
			buffer.append(a.getName() + "; ");
		}
		buffer.append("\n");
		
		buffer.append("Vorfahren von Jack: ");
		for (Animal a : db.getAnimal(7).getAncestor()) {
			buffer.append(a.getName() + "; ");
		}
		buffer.append("\n\n");
		
		buffer.append("Welches Tier von Beate hat die meisten Nachfahren? ");
		buffer.append(db.getPerson("Beate").getAnimalsSortedByDescendantCount().last().getName() + "\n");
		
		buffer.append("Welches Tier von Alf hat die wenigsten Nachfahren? ");
		buffer.append(db.getPerson("Alf").getAnimalsSortedByDescendantCount().first().getName() + "\n");
		
		buffer.append("Welches Tier von Christine hat den lexikalisch kleinsten Namen? ");
		buffer.append(db.getPerson("Christine").getAnimalsSortedByName().first().getName() + "\n");
		
		buffer.append("Welches Tier hat die meisten Vorfahren? ");
		buffer.append(db.getAnimals((a1, a2) -> a1.getNumberOfAncestors() - a2.getNumberOfAncestors()).last().getName() + "\n");
		
		buffer.append("Welche/r Züchter/in hat den längsten Namen? ");
		buffer.append(db.getPersons((p1, p2) -> p1.getName().length() - p2.getName().length()).last().getName() + "\n");
		
		buffer.append("Welcher Züchter hat die wenigsten Tiere? ");
		buffer.append(db.getPersons((p1, p2) -> p1.getAnimals().size() - p2.getAnimals().size()).first().getName() + "\n");
		
		System.out.println(buffer.toString());
	}
}
