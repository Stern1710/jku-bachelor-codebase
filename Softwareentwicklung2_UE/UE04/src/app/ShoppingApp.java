package app;

import item.Drink;
import item.Food;
import item.Item;
import list.LinkedList;

public class ShoppingApp {
	public static void main(String[] args) {
		Item bread = new Food("Brot", 2.39, 0.5);
		
		LinkedList<Item> itemList = new LinkedList<Item>();
		itemList.add(new Drink("Fanta", 1.39, 1.00));
		itemList.add(new Drink("Cola", 1.49, 1.00));
		itemList.add(new Drink("Mineralwasser", 0.79, 0.5));
		itemList.add(new Drink("Mineralwasser", 0.79, 0.5));
		itemList.add(new Drink("Mineralwasser", 0.79, 0.5));
		itemList.add(bread);
		itemList.add(new Food("Schinken", 17.49, 1.0));
		itemList.add(new Food("Schokolade", 1.00, 0.125));
		print(itemList);
		
		itemList.remove(0);
		itemList.remove(bread);
		print(itemList);
		
		print(itemList.below(new Drink("Apfelsaft", 1.0, 1.0)));
		print(itemList.above(new Drink("Apfelsaft", 1.0, 1.0)));
	}	
	
	private static <T extends Comparable<T>> void print(LinkedList<T> list) {
		for(T element : list) {
			System.out.println(element.toString());
		}
		System.out.println("---");
	}
}
