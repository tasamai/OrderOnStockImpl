package orderOnStock.tests;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Test;

import orderOnStock.impl.*;

//
// Class that implements all snapshots of 'OrderOnStock' 
// as JUnit tests
public class OrderOnStockTests {
	
	OrderOnStock oos = null;
	String p1_id, p2_id = null;
	String o1_id = null;
	
	private void testInit() {
		
	    oos = new OrderOnStock();
	    
	    p1_id = oos.addProduct("Oranges Moro", UnitKind.kg, 2, 100);
		p2_id = oos.addProduct("Effective toothpaste", UnitKind.piece, 1, 100);
		
	}
	
	private void testInit2() throws AlreadyCustomerException {
		
		testInit();
		
		String addr = "10 Broad Street, B16 7SD, Birmingham, UK";
		oos.registerCustomer("John Smith", addr, "john.smith@gmail.com", "1234");
		
	}
	
	private void testInit3() throws AlreadyCustomerException, NoCustomerException, NoProductException {
		testInit2();
		o1_id = oos.orderProduct("john.smith@gmail.com", p1_id, 10);
	}
	
	private void testInit4() throws AlreadyCustomerException, NoCustomerException, NoProductException {
		testInit2();
		o1_id = oos.orderProduct("john.smith@gmail.com", p2_id, 101);
	}
	
	private void testInit5() 
			throws AlreadyCustomerException, NoCustomerException, NoProductException, NoOrderException, NotEnoughStockException {
		testInit2();
		o1_id = oos.orderProduct("john.smith@gmail.com", p2_id, 5);
		oos.invoiceOrder(o1_id);
	}
	
	@Test
	public void testRegisterCustomerPositive() throws AlreadyCustomerException, NoCustomerException {
		
		testInit();
		
		String addr = "10 Broad Street, B16 7SD, Birmingham, UK";
		oos.registerCustomer("John Smith", addr, "john.smith@gmail.com", "1234");
		
		Customer c = oos.findCustomer("john.smith@gmail.com");
		
		if (c!= null) {
			assertEquals(c.getName(), "John Smith");
			assertEquals(c.getAddress(), addr);
			assertEquals(c.getEmail(), "john.smith@gmail.com");
			assertEquals(c.getPassword(), "1234");
		}
		else 
			fail ("Customer was not registered");
	}
	
	@Test(expected = AlreadyCustomerException.class)
	public void testRegisterCustomerNegative() throws AlreadyCustomerException {
		testInit2();
		
		String addr = "7 Petonville Rd, NW7 5GS, London, UK";
		oos.registerCustomer("John Paul Smith", addr, "john.smith@gmail.com", "TrustNo1");
		
		
	}
	
	@Test
	public void testOrderProductPositive() 
			throws AlreadyCustomerException, NoProductException, NoCustomerException, NoOrderException {
		
		testInit2();
		
		String oid = oos.orderProduct("john.smith@gmail.com", p2_id, 5);
		
		Order o = oos.findOrder(oid);
		Product p = oos.findProduct(p2_id);
		Customer c = oos.findCustomer("john.smith@gmail.com");
		
		assertEquals(o.getOId(), oid);
		assertEquals(o.getQty(), 5);
		assertEquals(o.getStatus(), OrderStatus.pending);		
		assertEquals(o.getOrderDt().getMonth(), LocalDate.now().getMonth());
		assertEquals(o.getOrderDt().getDayOfMonth(), LocalDate.now().getDayOfMonth());
		assertEquals(o.getOrderDt().getYear(), LocalDate.now().getYear());
		assertNull(o.getInvoiceDt());
		assertEquals(o.getCustomer(), c);
		assertEquals(o.getProduct(), p);
	}
	
	@Test(expected = NoProductException.class)
	public void testOrderProductNegative1() throws AlreadyCustomerException, NoCustomerException, NoProductException  {
		testInit2();

        oos.orderProduct("john.smith@gmail.com", "P4", 5);
	}
	
	@Test
	public void testOrderProductNegative2() 
			throws AlreadyCustomerException, NoProductException, NoCustomerException, NoOrderException {
		
		testInit2();
		
		String oid = oos.orderProduct("john.smith@gmail.com", p2_id, 5);
		
		Order o = oos.findOrder(oid);
		
		//Fails if anything other than P2
		if(o.getProduct() != oos.findProduct(p2_id))
			fail();
		else {
			assertTrue(true);
		}
	}
	
	@Test
	public void testinvoiceOrderPositive() 
			throws AlreadyCustomerException, NoProductException, NoCustomerException, IllegalStateException, NoOrderException,
			NotEnoughStockException {
		
		testInit3();
		
		Order o = oos.findOrder(o1_id);
		// Before invoicing the order, the following should hold
		assertEquals(o.getStatus(), OrderStatus.pending);
		assertNull(o.getInvoiceDt());
		assertEquals(o.getProduct().getStock(), 100);
		
		oos.invoiceOrder(o1_id);
		
		// After invoicing the order, the following should hold
		assertEquals(o.getOId(), o1_id);
		assertEquals(o.getQty(), o.getQty());
		assertEquals(o.getStatus(), OrderStatus.invoiced);		
		assertEquals(o.getOrderDt().getMonth(), LocalDate.now().getMonth());
		assertEquals(o.getOrderDt().getDayOfMonth(), LocalDate.now().getDayOfMonth());
		assertEquals(o.getOrderDt().getYear(), LocalDate.now().getYear());
		//The order should have been invoiced recently
		assertEquals(o.getInvoiceDt().getMonth(), LocalDate.now().getMonth());
		assertEquals(o.getInvoiceDt().getDayOfMonth(), LocalDate.now().getDayOfMonth());
		assertEquals(o.getInvoiceDt().getYear(), LocalDate.now().getYear());
		// The stock has been subtracted
		assertEquals(o.getProduct().getStock(), 90);
	}
	
	@Test
	public void testInvoiceOrderNegative() throws AlreadyCustomerException, NoCustomerException, NoProductException, NoOrderException  {
		testInit4();
		
		Order o = oos.findOrder(o1_id);
		// Before invoicing the order, the following should hold
		assertEquals(o.getStatus(), OrderStatus.pending);
		assertNull(o.getInvoiceDt());
		assertEquals(o.getProduct().getStock(), 100);
		
		try {
			oos.invoiceOrder(o1_id);
			fail();
		}
		catch(NotEnoughStockException e) {
			assertTrue (true);
		}
	}
	
	@Test
	public void testcancelOrderPositive() 
			throws AlreadyCustomerException, NoProductException, NoCustomerException, IllegalStateException, NoOrderException {
		
		testInit3();
		
		Order o = oos.findOrder(o1_id);
		// Before invoicing the order, the following should hold
		assertEquals(o.getStatus(), OrderStatus.pending);
		Customer c = o.getCustomer();
		
		oos.cancelOrder(o1_id);
		
		//The order shouldn't be in the system anymore
		assertNull(oos.findOrder(o1_id));
		// The order shouldn't be in the list of orders of the customer
		assertFalse(c.hasOrder(o));
		
	}
	
	@Test
	public void testcancelOrderNegative() 
			throws AlreadyCustomerException, NoProductException, NoCustomerException, NoOrderException, NotEnoughStockException {
		
		testInit5();
		try {
			oos.cancelOrder(o1_id);
			fail();
		}
		catch (IllegalStateException e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void testupdateStockPositive() throws AlreadyCustomerException, NoProductException, NoCustomerException {
		testInit3();
		
		Product p = oos.findProduct(p2_id);
		//expectations of before state
		assertEquals(p.getStock(), 100);
		
		oos.updateStock(p2_id, 50);
		
		//expectations of before state
		assertEquals(p.getStock(), 150);
	}

	
	@Test
	public void testupdateStockNegative1() throws AlreadyCustomerException, NoProductException, NoCustomerException {
		testInit3();
		try {
			oos.updateStock("P4", 50);
			fail();
		}
		catch(NoProductException e) {
			assertTrue (true);
		}
	}
	
	@Test
	public void testupdateStockNegative2() throws AlreadyCustomerException, NoProductException, NoCustomerException  {
		testInit3();
		Product p = oos.findProduct(p2_id);
		
		int stock_prv = p.getStock();
		
		oos.updateStock(p2_id, 50);
		
		assertNotEquals(p.getStock(), stock_prv);
	}
}
