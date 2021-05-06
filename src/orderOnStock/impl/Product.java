package orderOnStock.impl;

public class Product {
	private static int last_id = 0;
	String pid, desc;
	int stock, price;
	UnitKind unit;
	
	public Product (String desc, UnitKind unit, int price, int stock) {
		if (stock < 0)
			throw new IllegalArgumentException ("Negative stock!");
		
		if (price < 0) 
			throw new IllegalArgumentException ("Negative price!");
		
		last_id++;
		pid = "P" + last_id;
		this.desc = desc;
		this.stock = stock;
		this.price = price;
		this.unit = unit;
			
	}
	
	public String getPID () {
		return pid;
	}
	
	public int getStock() {
		return stock;
	}
	
	public void deductStock(int qty) throws NotEnoughStockException {
		
		if (qty > stock)
			throw new NotEnoughStockException(stock, qty);
		
		stock = stock - qty;
	}
	
	public void updateStock(int qty) throws IllegalArgumentException {
		
		if (qty < 0)
			throw new IllegalArgumentException ("Negative Quantity!");
		
		stock += qty;
	}
}

