package be.steformations.java_data.contacts.tests.jdbc.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import be.steformations.java_data.contacts.interfaces.jdbc.ContactJdbcManager;

public class _02_TestGetEmailsByCountry {
	
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
	public void testGetEmailsByCountryKo() {
		testKo(manager, "XX");
		testKo(manager, "USA");
		testKo(manager, "");
		testKo(manager, null);
	}

	@Test
	public void testGetEmailsByCountryOk() {
		testOk(manager, "US", new String[] {"betty.boop@hollywood.com", "jessica.rabbit@hollywood.com"});
	}
	
	protected static void testOk(ContactJdbcManager manager, String abbreviation, String[] expected) {
		try {
			java.util.List<String> emails = manager.getEmailsByCountry(abbreviation);
			assertNotNull(String.format("ContactJdbcManager.getEmailsByCountry(\"%s\")", abbreviation), emails);
			for (String email : expected) {
				assertTrue(String.format("ContactJdbcManager.getEmailsByCountry(\"%s\").contains(\"%s\")", abbreviation, email), 
							emails.contains(email));
			}			
		} catch(Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	protected static void testKo(ContactJdbcManager manager, String abbreviation) {
		try {
			java.util.List<String> emails = manager.getEmailsByCountry(abbreviation);
			assertNotNull(String.format("ContactJdbcManager.getEmailsByCountry(\"%s\")", abbreviation), emails);
			assertEquals(String.format("ContactJdbcManager.getEmailsByCountry(\"%s\").size()", abbreviation), 0, emails.size());
		} catch(Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	
}
