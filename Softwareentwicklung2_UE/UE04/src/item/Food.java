package item;

public final class Food extends Item {
	//FIELDS
	private final double weight;
	
	//CONSTRUCTORS
	public Food(String name, double price, double weight) {
		super(name, price);
		this.weight = weight;
	}
	
	//GETTER
	public double getWeight() {
		return weight;
	}
	
	//METHODS
	@Override
	public String toString() {
		return super.toString() + " (" + getWeight() + " kg)";
	}
}