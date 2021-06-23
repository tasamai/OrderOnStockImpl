package orderOnStock.impl;

import java.util.ArrayList;
import java.util.List;

public class Customer {
	String name, address, email, password;
	List<Order> orders;

	public Customer (String name, String address, String email, String password) {
		this.name = name;
		this.address = address;
		this.email = email;
		this.password = password;
		
		orders = new ArrayList<Order>();
	}
	
   public void addOrder (Order o) {
	   orders.add(o);
   }

   public String getName() {
	
	   return name;
   }
   
   public String getEmail() {
		
	   return email;
   }
   
   public String getAddress() {
		
	   return address;
   }
   
   public String getPassword() {
		
	   return password;
   }
   
   public boolean hasOrder(Order o) {
		return orders.contains(o);
	}
	
	public void cancelOrder (Order o) {
		orders.remove(o);
	}
}

