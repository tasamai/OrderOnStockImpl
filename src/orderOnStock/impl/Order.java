package orderOnStock.impl;

import java.time.LocalDate;

public class Order {
   private static int last_id = 0;
   int qty;
   String oid;
   OrderStatus status;
   LocalDate orderDt, invoiceDt;
   Product product; /* Represents association 'RefersTo' of class diagram*/
   Customer customer; /* Represents association 'MakesOrder' of class diagram*/
   
   public Order (int qty, Product p, Customer c) {
	   
	   if (qty < 0) 
		   throw new IllegalArgumentException ("Negative quantity!");
	   
	   last_id++;
	   oid = "O" + last_id;
	   this.qty = qty;
	   this.status = OrderStatus.pending;
	   this.orderDt = LocalDate.now();
	   this.invoiceDt = null;
	   this.customer = c;
	   this.product = p;
	   
	   c.addOrder(this);
   }
   
   public String getOId () {
	   return oid;
   }
   
   public int getQty () {
	   return qty;
   }
   
   public OrderStatus getStatus () {
	   return status;
   }
   
   public LocalDate getOrderDt () {
	   return orderDt;
   }
   
   public LocalDate getInvoiceDt() {
	   return invoiceDt;
   }
   
   public Customer getCustomer() {
	   return customer;
   }
   
   public Product getProduct() {
	   return product;
   }
   
   public void invoice() throws NotEnoughStockException, IllegalStateException {
		
		if (status != OrderStatus.pending)
			throw new IllegalStateException("Status of order is not pending");
		product.deductStock(qty);
		
		this.status = OrderStatus.invoiced;
		this.invoiceDt = LocalDate.now();
	}
	
	public void cancel() throws IllegalStateException {
		
		if (status != OrderStatus.pending)
			throw new IllegalStateException("Order is not pending");
		
		product = null;
		customer.cancelOrder(this);
		customer = null;
	}
}
