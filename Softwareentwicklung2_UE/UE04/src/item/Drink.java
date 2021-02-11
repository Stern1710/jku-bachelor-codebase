package item;

public final class Drink extends Item {
	//FIELDS
	private final double volume;
	
	//CONSTRUCTOR
	public Drink(String name, double price, double volume) {
		super(name, price);
		this.volume = volume;
	}
	
	//GETTER
	public double getVolume() {
		return volume;
	}

	//METHODS
	@Override
	public String toString() {
		return super.toString() + " (" + getVolume() + " L)";
	}
}