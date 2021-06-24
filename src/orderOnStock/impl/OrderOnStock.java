package orderOnStock.impl;

import java.util.HashMap;
import java.util.Map;

public class OrderOnStock {
	
	Map<String, Customer> customers;
	Map<String, Product> products;
	Map<String, Order> orders;
	
	public OrderOnStock() {
		customers = new HashMap<String, Customer>();
		products = new HashMap<String, Product>();
		orders = new HashMap<String, Order>();
	}
	
	public void registerCustomer(String name, String addr, String email, String pw) throws AlreadyCustomerException {
		
		if (customers.containsKey(email)) {
			throw new AlreadyCustomerException(email);
		}
		
		Customer c = new Customer(name, addr, email, pw);
		
		customers.put(email, c);
	}
	
	public Customer findCustomer (String email) {
		
		Customer c = customers.get(email);
		
		return c;
	}
	
	public Product findProduct (String pid) {
		
		Product p = products.get(pid);
		
		return p;
	}
	
	public Order findOrder (String oid) throws NoOrderException {
		
		
		Order o = orders.get(oid);
		
		return o;
	}

	public String orderProduct (String email, String pid, int qty) 
			throws NoCustomerException, NoProductException {
		
		
		Customer c = findCustomer(email);
		
		if (c == null)
			throw new NoCustomerException (email);
		
		Product p = findProduct(pid);
		
		if (p == null) 
			throw new NoProductException(pid);
		
		Order o = new Order(qty, p, c);
		
		orders.put(o.getOId(), o);
		
		return (o.getOId());
	}
	
	public String addProduct(String desc, UnitKind unit, int stock, int price) {
		
		Product p = new Product(desc, unit, stock, price);
		
		products.put(p.getPID(), p);
		
		return p.getPID();
		
	}
	
	public void invoiceOrder(String oid) 
			throws NoOrderException, NotEnoughStockException, IllegalStateException {
		
		Order o = findOrder(oid);
		
		if (o == null) {
			throw new NoOrderException(oid);
		}
		
		o.invoice();
	}
	
	public void cancelOrder(String oid) 
			throws NoOrderException, IllegalStateException {
		
		Order o = findOrder(oid);
		
		if (o == null) {
			throw new NoOrderException(oid);
		}
		
		o.cancel();
		
		orders.remove(o.getOId());
	}
	
	public void updateStock(String pid, int qty) throws NoProductException {
		Product p = findProduct(pid);
		
		if (p == null) 
			throw new NoProductException(pid);
		
		p.updateStock(qty);
	}
}
