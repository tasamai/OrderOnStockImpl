package orderOnStock.impl;

public class AlreadyCustomerException extends Exception {
	
	public AlreadyCustomerException(String email) {
		super ("There is already a customer with email " + email);
	}
}
