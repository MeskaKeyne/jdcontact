package be.steformations.tunsajan.jd.contact;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import be.steformations.java_data.contacts.interfaces.jdbc.ContactJdbcManager;

public class JDContact implements ContactJdbcManager{
	
	//private String url ="jdbc:postgresql://prim2014-14.fapse.priv:5432/contact";
	private String url ="jdbc:postgresql://localhost:5432/contact";
	private String user = "postgres";
	private String pwd = "postgres";
	private Connection con;

	@Override
	public String getEmailByContact(String firstname, String name) {
		if(firstname ==null && name ==null) return null;
			
		
		con = this.open();
		PreparedStatement req =null;
		String sql = 
					"SELECT email "
				+ 	"FROM contacts "
				+ 	"WHERE UPPER(prenom) = UPPER(?) "
				+ 	"AND UPPER(nom) = UPPER(?) ";
		try {
			String rsql=null;
			req = con.prepareStatement(sql);
			req.setString(1, firstname);
			req.setString(2, name);
			ResultSet r = req.executeQuery();
			if(r.next()) rsql = r.getString(1);
			close();
			return rsql;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	
	}

	@Override
	/*
	 * select contacts.email
		from contacts, pays
		where pays.id = contacts.pays
	 * (non-Javadoc)
	 * @see be.steformations.java_data.contacts.interfaces.jdbc.ContactJdbcManager#getEmailsByCountry(java.lang.String)
	 */
	public List<String> getEmailsByCountry(String abbreviation) {
		List<String> rlist= new ArrayList<String>();
		if(abbreviation ==null) return rlist;
		con = this.open();
		PreparedStatement req =null;
		String sql = 
					"SELECT contacts.email "
				+ 	"FROM contacts, pays "
				+ 	"WHERE pays.id = contacts.pays "
				+   "AND pays.abreviation = ?";
		try {
			String rsql= null;
			req = con.prepareStatement(sql);
			req.setString(1, abbreviation);
			ResultSet r = req.executeQuery();
			while(r.next()){
				rsql = r.getString(1);
				rlist.add(rsql);
			}
			close();
			//if(rlist.isEmpty()) return null;
			return rlist;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Recherche les tags associés à un contact
	 * @param firstname prénom du contact
	 * @param name nom du contact
	 * @return la liste des tags associés au contact ou une liste vide si le contact n'existe pas ou si il n'a pas de tags
	 */
	@Override
	public List<String> getTagsByContact(String firstname, String name) {
		List<String> rlist= new ArrayList<String>();
		if(firstname ==null && name ==null) return rlist;
		con = this.open();
		PreparedStatement req =null;
		String sql = 
					"SELECT tags.tag "
				+ 	"FROM contacts, contacts_tags, tags "
				+ 	"WHERE contacts.id = contacts_tags.contact "
				+   "AND contacts_tags.tag = tags.id "
				+	"AND contacts.prenom = ? "
				+	"AND contacts.nom = ?";
		try {
			String rsql= null;
			req = con.prepareStatement(sql);
			req.setString(1, firstname);
			req.setString(2, name);
			ResultSet r = req.executeQuery();
			while(r.next()){
				rsql = r.getString(1);
				rlist.add(rsql);
			}
			close();
			//if(rlist.isEmpty()) return null;
			return rlist;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * Ajout d'un contact et des nouveaux tags qui lui sont associés
	 * @param firstname prénom du contact
	 * @param name nom du contact
	 * @param email email du contact
	 * @param countryAbbreviation abréviation du pays du contact (éventuellement null) 
	 * @param tagsValues valeurs des tags à associer au contact (éventuellement nouveaux)
	 * @return l'identifiant du contact (lorsque le contact a été ajouté) ou 0 en cas de duplication ou de données incorrectes
	 */
	@Override
	public int createAndSaveContact(String firstname, String name, String email, String countryAbbreviation,
			String[] tagsValues) {
		
		if(firstname == null || name == null || email == null || email.length() <5 
				|| this.getEmailByContact(firstname, name) != null  ) return 0;
		int idPays = 0;
		if(countryAbbreviation!= null) idPays = this.rechercheIDPays((countryAbbreviation));
		
	
		PreparedStatement req =null;
		String sql = 
				"insert into contacts(prenom, nom, email, pays) "
				         + " values(?, ?, ?, ?)";
		try {
				Connection c = java.sql.DriverManager.getConnection(url, user, pwd);
				String rsql= null;
				req = c.prepareStatement(sql);
				req.setString(1, firstname);
				req.setString(2, name);
				req.setString(3, email);
				if(this.isPresentIDPays(idPays))req.setInt(4, idPays);
				else req.setNull(4, java.sql.Types.INTEGER);
				req.executeUpdate();
				int idNewContact  = this.lastIDContact();
				
				if(tagsValues != null){
					for(String t: tagsValues){
						int idTag =this.rechercheTag(t);
						if(idTag == -1){
							this.updateTag(t);
							this.contactsTags(this.lastIDTag(), idNewContact);
						}
						else this.contactsTags(idTag, idNewContact);
						
					}
				}
				
				
				c.close();
			return idNewContact;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void removeContact(int id) {
		// TODO Auto-generated method stub
		
	}
	private Connection open(){
		con=null;
		try {
			con = DriverManager.getConnection(url, user, pwd);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return con;
		
	}
	private void close(){
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private int rechercheTag(String tag){
		this.open();
		PreparedStatement req =null;
		String sql = 
					"SELECT tags.id "
				+ 	"FROM tags "
				+ 	"WHERE tags.tag = ? ";
		try {
			req = con.prepareStatement(sql);
			req.setString(1, tag);
			ResultSet r = req.executeQuery();
			close();
			if(r.next())
			return r.getInt(1);
			else return -1;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	private int rechercheIDPays(String abb){
		 this.open();
		PreparedStatement req =null;
		String sql = 
					"SELECT pays.id "
				+ 	"FROM pays "
				+ 	"WHERE pays.abreviation = ? ";
		try {
			req = con.prepareStatement(sql);
			req.setString(1, abb);
			ResultSet r = req.executeQuery();
			close();
			if(r.next()) return r.getInt(1);
			else return 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	private boolean isPresentIDPays(int id){
		 this.open();
		PreparedStatement req =null;
		String sql = 
					"SELECT pays.nom "
				+ 	"FROM pays "
				+ 	"WHERE pays.id = ? ";
		try {
			req = con.prepareStatement(sql);
			req.setInt(1, id);
			ResultSet r = req.executeQuery();
			close();
			return r.next();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	public void updateTag(String tag){
		if(tag == null ) return;
	
		this.open();
		PreparedStatement req =null;
		String sql = 
				"insert into tags(tag) "
				         + " values(?)";
		try {
				req = con.prepareStatement(sql);
				req.setString(1, tag);
				req.executeUpdate();
				System.out.println("updatetag   " + tag);
				close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public int lastIDTag(){
		
		this.open();
		PreparedStatement req =null;
		String sql = 	" SELECT tags.id "
				+ 		"	FROM tags "
				+		"ORDER BY tags.id DESC "
				+ 		"LIMIT 1";
		
		try {
			req = con.prepareStatement(sql);
			ResultSet r = req.executeQuery();
			this.close();
			if(r.next()) return r.getInt(1);
			else return -1;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	public void contactsTags(int idTag,  int contact){
	
	
		this.open();
		PreparedStatement req =null;
		String sql = 
				"insert into contacts_tags(contact, tag) "
				         + " values(?, ?)";
		try {
				req = con.prepareStatement(sql);
				req.setInt(1, contact);
				req.setInt(2, idTag);
				req.executeUpdate();
				System.out.println("update contacts_tags   ");
				close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public int lastIDContact(){
		this.open();
		PreparedStatement req =null;
		String sql = 	" SELECT contacts.id "
				+ 		"	FROM contacts "
				+		"ORDER BY contacts.id DESC "
				+ 		"LIMIT 1";
		
		try {
			req = con.prepareStatement(sql);
			ResultSet r = req.executeQuery();
			this.close();
			if(r.next()) return r.getInt(1);
			else return 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
		
	}
	public boolean checkNomPrenom(String nom, String prenom){
		this.open();
		PreparedStatement req =null;
		String sql = 
					"SELECT contacts.id "
				+ 	"FROM contacts "
				+ 	"WHERE contacts.nom = ? "
				+ "AND contacts.prenom = ?";
		try {
			req = con.prepareStatement(sql);
			req.setString(1, nom);
			req.setString(2, prenom);
			ResultSet r = req.executeQuery();
			close();
			if(r.next()) return true;
			return false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
		

}
