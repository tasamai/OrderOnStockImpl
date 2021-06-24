package orderOnStock.impl;

public class NoProductException extends Exception {

	
	NoProductException(String pid) {
		super("There is no product with identifier " + pid);
	}
}
