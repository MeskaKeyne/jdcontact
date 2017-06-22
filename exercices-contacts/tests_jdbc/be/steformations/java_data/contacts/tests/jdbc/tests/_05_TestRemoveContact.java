package be.steformations.java_data.contacts.tests.jdbc.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import be.steformations.java_data.contacts.interfaces.jdbc.ContactJdbcManager;


public class _05_TestRemoveContact {
	
	ContactJdbcManager manager = null;
	
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
	public void testDeleteContact() {
		try {
			long millis = System.currentTimeMillis();
			String firstname = "Princesse-" + millis;
			String name = "Pandha";
			String email = "pandha@accomics";
			String abbreviation = "US";
			int id = manager.createAndSaveContact(firstname, name, email, abbreviation, null);
			String message = String.format("ContactJdbcManager.createAndSaveContact(%s, %s, %s, %s, null) > 0", 
					firstname, name, email, abbreviation);			
			assertTrue(message, id > 0);

			String emailBack = manager.getEmailByContact(firstname, name);
			assertNotNull(String.format("ContactJdbcManager.getEmailByContact(\"%s\", \"%s\")", firstname, name), emailBack);
			assertEquals(String.format("ContactJdbcManager.getEmailByContact(\"%s\", \"%s\")", firstname, name), email, emailBack);	

			manager.removeContact(id);
			
			emailBack = manager.getEmailByContact(firstname, name);
			assertNull(String.format("ContactJdbcManager.getEmailByContact(\"%s\", \"%s\")", firstname, name), emailBack);
		} catch(Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
	}
}
