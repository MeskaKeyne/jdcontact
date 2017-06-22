package be.steformations.java_data.contacts.tests.jdbc.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import be.steformations.java_data.contacts.interfaces.jdbc.ContactJdbcManager;

public class _04_TestCreateAndSaveContact {
	
	ContactJdbcManager manager = null;
	
	@Before
	public void init() {
		manager = null;
		try {
			manager = ContactJdbcTestFactory.getContactJdbcManager();
			assertNotNull("ContactJdbcTestFactory.getContactJdbcManager()", manager);
		} catch(Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testInsererContactSansPaysNiTag() {
		long millis = System.currentTimeMillis();
		testOk(manager, "Selina-" + millis, "Kyle", "catwoman@dccomics.com", null, null);
	}
	
	@Test
	public void testInsererContactAvecPaysExistantEtPasDeTags() {
		long millis = System.currentTimeMillis();
		testOk(manager, "Lisa-" + millis, "Morel", "aquagirl@dccomics.com", "US", null);
	}
	
	@Test
	public void testInsererContactSansPaysEtAvecTagsExistants() {
		testOk(manager, "Betty-" + System.currentTimeMillis(), "Kane", "batgirl@dccomics.com", null, new String[] {"sex-symbol", "vamp"});
	}

	@Test
	public void testInsererContactAvecPaysExistantEtTagsExistants() {
		testOk(manager, "Pamela-" + System.currentTimeMillis(), "Isley", "poison-ivy@dccomics.com", "US", new String[] {"sex-symbol", "vamp"});
	}	
	
	@Test
	public void testInsererContactAvecPaysExistantEtTagsInexistants() {	
		long millis = System.currentTimeMillis();
		testOk(manager, "Diana-" + millis, "Prince", "wonder-woman@dccomics.com", "US", new String[] { "sex-symbol", "vamp", "pin-up-" + millis });		
	}
	
	@Test
	public void testInsererContactSansPrenom() {
		String unique = "x" + System.currentTimeMillis();
		testKo(manager, null, unique, "x", "US", new String[] {});
	}
	
	@Test
	public void testInsererContactSansNom() {
		String unique = "x" + System.currentTimeMillis();
		testKo(manager, unique, null, "x", "US", new String[] {});
	}
	
	@Test
	public void testInsererContactSansEmail() {
		String unique = "x" + System.currentTimeMillis();
		testKo(manager, unique, "x", null, "US", new String[] {});
	}
	
	@Test
	public void testInsererContactAvecPaysInexistant() {
		String unique = "x" + System.currentTimeMillis();
		testKo(manager, unique, "x", "x", "XX", new String[] {});
		testKo(manager, unique, "x", "x", "USA", new String[] {});	
	}	

	@Test
	public void testInsererContactAvecDuplication() {
		long millis = System.currentTimeMillis();
		testOk(manager, "Nura-" + millis, "Nal", "dreamgirl@dccomics.com", null, null);	
		testKo(manager, "Nura-" + millis, "Nal", "dreamgirl@dccomics.com", null, null);
		testKo(manager, "Nura-" + millis, "Nal", "dreamgirl@dccomics.com", null, null);
	}

	private void testOk(ContactJdbcManager manager, String firstname, String name, String email, String abbreviation, String[] tagsValues) {
		try {
			int id = manager.createAndSaveContact(firstname, name, email, abbreviation, tagsValues);

			String message = String.format("ContactJdbcManager.createAndSaveContact(%s, %s, %s, %s, %s) > 0", 
								firstname, name, email, abbreviation, java.util.Arrays.toString(tagsValues));
			assertTrue(message, id > 0);

			String emailBack = manager.getEmailByContact(firstname, name);
			assertNotNull(String.format("ContactJdbcManager.getEmailByContact(\"%s\", \"%s\")", firstname, name), emailBack);
			assertEquals(String.format("ContactJdbcManager.getEmailByContact(\"%s\", \"%s\")", firstname, name), email, emailBack);	
			
			if ( abbreviation != null ) {
				java.util.List<String> emails = manager.getEmailsByCountry(abbreviation);
				assertNotNull(String.format("ContactJdbcManager.getEmailsByCountry(\"%s\")", abbreviation), emails);
				assertTrue(String.format("ContactJdbcManager.getEmailsByCountry(\"%s\").contains(\"%s\")", abbreviation, email), 
								emails.contains(email));
			}
			
			if ( tagsValues != null ) {
				java.util.List<String> tags = manager.getTagsByContact(firstname, name);		
				assertNotNull(String.format("ContactJdbcManager.getTagsByContact(\"%s\", \"%s\")", firstname, name), tags);
				for (String value : tagsValues) {
					assertTrue(String.format("ContactJdbcManager.getTagsByContact(\"%s\", \"%s\").contains(\"%s\")", firstname, name, value), 
								tags.contains(value));
				}
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	private void testKo(ContactJdbcManager manager, String firstname, String name, String aka, String abbreviation, String[] tagsValues) {
		try {
			int id = manager.createAndSaveContact(firstname, name, aka, abbreviation, tagsValues);
			String message = String.format("ContactJdbcManager.createAndSaveContact(%s, %s, %s, %s, %s)", 
					firstname, name, aka, abbreviation, java.util.Arrays.toString(tagsValues));
			assertEquals(message, 0, id);
		} catch(Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
