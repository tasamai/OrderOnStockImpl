package orderOnStock.impl;

public class NoCustomerException extends Exception {
	
	NoCustomerException(String email) {
		super("There's no 'Customer' with email " + email);
	}
}
