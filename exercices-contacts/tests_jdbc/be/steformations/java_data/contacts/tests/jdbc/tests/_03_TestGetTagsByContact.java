package be.steformations.java_data.contacts.tests.jdbc.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import be.steformations.java_data.contacts.interfaces.jdbc.ContactJdbcManager;

public class _03_TestGetTagsByContact {
	
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
	public void testGetTagsByContactOk() {
		testOk(manager, "Betty", "Boop", new String[] {"vamp", "sex-symbol"});
		testOk(manager, "Jessica", "Rabbit", new String[] {"vamp", "sex-symbol"});
	}
	
	@Test
	public void testGetTagsByContactKo() {	
		testKo(manager, "Betty", "Booooop");
		testKo(manager, "Sally", "Jupiter");
		testKo(manager, "Betty", null);
		testKo(manager, null, "Boop");
		testKo(manager, null, null);
		testKo(manager, "Betty", "");
		testKo(manager, "", "Boop");
		testKo(manager, "", "");
	}
	
	protected static void testOk(ContactJdbcManager manager, String firstname, String name, String[] expected) {
		try {
			java.util.List<String> tags = manager.getTagsByContact(firstname, name);		
			assertNotNull(String.format("ContactJdbcManager.getTagsByContact(\"%s\", \"%s\")", firstname, name), tags);
			for (String value : expected) {
				assertTrue(String.format("ContactJdbcManager.getTagsByContact(\"%s\", \"%s\").contains(\"%s\")", firstname, name, value), 
							tags.contains(value));
			}
		} catch(Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	protected static void testKo(ContactJdbcManager manager, String firstname, String name) {
		try {
			java.util.List<String> tags = manager.getTagsByContact(firstname, name);
			assertNotNull(String.format("ContactJdbcManager.getTagsByContact(\"%s\", \"%s\")", firstname, name), tags);
			assertEquals(String.format("ContactJdbcManager.getTagsByContact(\"%s\", \"%s\").size()", firstname, name), 0, tags.size());
		} catch(Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
