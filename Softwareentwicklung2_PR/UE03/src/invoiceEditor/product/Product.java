package invoiceEditor.product;

public class Product {
	private final String ID, name;
	private final double price;
	
	public Product(String iD, String name, double price) {
		super();
		ID = iD;
		this.name = name;
		this.price = price;
	}
	
	public String getID() {
		return ID;
	}
	public String getName() {
		return name;
	}
	public double getPrice() {
		return price;
	}

	@Override
	public String toString() {
		return name + "[ID: " + ID + "]";
	}
}
