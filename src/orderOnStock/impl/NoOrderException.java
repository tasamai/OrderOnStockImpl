package orderOnStock.impl;

public class NoOrderException extends Exception {
	
	NoOrderException(String oid) {
		super("There is no order with identifier " + oid);
	}
}
