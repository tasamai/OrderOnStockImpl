package orderOnStock.impl;

public class NotEnoughStockException extends Exception {
	
	public NotEnoughStockException(int stock, int qty) {
		super("There is not enough stock (" + stock +
				") for the ordered quantity ("+qty +")");
	}

}
