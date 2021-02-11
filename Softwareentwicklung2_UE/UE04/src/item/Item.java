package item;

public abstract class Item implements Comparable<Item> {
	//FIELDS
	private final String name;
	private final double price;
	
	//CONSTRUCTOR
	Item (String name, double price) {
		this.name = name;
		this.price = price;
	}
	
	//GETTER
	public String getName() {
		return name;
	}
	
	public double getPrice() {
		return price;
	}

	/**
	 * Compares first by price and if undecided then by name to each other
	 * -1 if passed item o is bigger, 0 if equal, 1 if o is smaller
	 */
	//METHODS
	@Override
	public int compareTo(Item o) {
		if (this.equals(o)) {
			return 0;
		}		
		if (getPrice() - o.getPrice() < 0) {
			return -1;
		} else if (getPrice() == o.getPrice()) {
			return getName().compareTo(o.getName());
		}
		
		return 1;
	}
	
	/**
	 * Gives name and price
	 */
	@Override
	public String toString() {
		return getName() + ": " + getPrice();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		long temp;
		temp = Double.doubleToLongBits(price);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		Item other = (Item) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (Double.doubleToLongBits(price) != Double.doubleToLongBits(other.price))
			return false;
		return true;
	}

}