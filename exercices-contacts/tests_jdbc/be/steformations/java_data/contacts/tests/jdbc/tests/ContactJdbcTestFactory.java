package be.steformations.java_data.contacts.tests.jdbc.tests;

import be.steformations.java_data.contacts.interfaces.jdbc.ContactJdbcManager;
import be.steformations.tunsajan.jd.contact.JDContact;

public class ContactJdbcTestFactory {
	
	public static ContactJdbcManager getContactJdbcManager() {
		return (ContactJdbcManager) new JDContact();
	}
}
