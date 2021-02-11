package invoiceEditor.product;

import java.util.ArrayList;
import java.util.List;

public final class DummyProductList {
	
	private static List<Product> products;
	
	static {
		products = new ArrayList<>();
		
		products.add(new Product("CP1", "CoolProduct", 12.3d));
		products.add(new Product("NP1", "NewProduct", 17));
		products.add(new Product("BP1", "BasicProduct", 5.5d));
	}
	
	public static List<Product> getDummyProducts() {
		return products;
	}
}
