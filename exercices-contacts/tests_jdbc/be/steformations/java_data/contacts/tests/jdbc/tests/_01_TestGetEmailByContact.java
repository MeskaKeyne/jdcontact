package be.steformations.java_data.contacts.tests.jdbc.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import be.steformations.java_data.contacts.interfaces.jdbc.ContactJdbcManager;

public class _01_TestGetEmailByContact {
	
	private ContactJdbcManager manager = null;
	
	@Before
	public void init() {
		try {
			manager = ContactJdbcTestFactory.getContactJdbcManager();
			assertNotNull("ContactJdbcTestFactory.getContactJdbcManager()", manager);		
		} catch(Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testGetEmailByContactKo() {
		testKO(manager, "xxxx", "xxxx");
		testKO(manager, "betty", "boooop");
		testKO(manager, "", "boop");
		testKO(manager, "betty", "");
		testKO(manager, "betty", null);
		testKO(manager, null, "boooop");
		testKO(manager, null, null);
	}

	
	@Test
	public void testGetEmailByContactOk() {
		testOk(manager, "Betty", "Boop", "betty.boop@hollywood.com");
		testOk(manager, "betty", "boop", "betty.boop@hollywood.com");
		testOk(manager, "BETTY", "BOOP", "betty.boop@hollywood.com");
		testOk(manager, "BETTY", "boop", "betty.boop@hollywood.com");
		testOk(manager, "betty", "BOOP", "betty.boop@hollywood.com");
		testOk(manager, "bEtTy", "BoOp", "betty.boop@hollywood.com");
	}
	
	private void testKO(ContactJdbcManager manager, String firstname, String name) {
		String email = null;
		try {
			email = manager.getEmailByContact(firstname, name);
			assertNull(String.format("ContactJdbcManager.getEmailByContact(\"%s\", \"%s\")", firstname, name), email);			
		} catch(Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}		
	}
	
	private void testOk(ContactJdbcManager manager, String firstname, String name, String expected) {
		try {
			String email = manager.getEmailByContact(firstname, name);
			assertNotNull(String.format("ContactJdbcManager.getEmailByContact(\"%s\", \"%s\")", firstname, name), email);
			assertEquals(String.format("ContactJdbcManager.getEmailByContact(\"%s\", \"%s\")", firstname, name), expected, email);			
		} catch(Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}		
	}

}
