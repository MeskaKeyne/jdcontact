package be.steformations.java_data.contacts.interfaces.jdbc;

import java.util.List;

public interface ContactJdbcManager {

	/**
	 * Recherche de l' email du contact sans tenir compte de la casse des arguments
	 * @param firstname prénom du contact
	 * @param name nom du contact
	 * @return l'email du contact ou null si le contact n'a pas d'email  
	 */
	public String getEmailByContact(String firstname, String name);
	
	/**
	 * Recherche des emails des contacts habitant dans un pays déterminé
	 * @param abbreviation abreviation de pays
	 * @return liste des emails des contacts de ce pays ou une liste vide si le pays n'existe pas ou si il n'y a pas de contacts dans ce pays
	 */
	public List<String> getEmailsByCountry(String abbreviation);
	
	/**
	 * Recherche les tags associés à un contact
	 * @param firstname prénom du contact
	 * @param name nom du contact
	 * @return la liste des tags associés au contact ou une liste vide si le contact n'existe pas ou si il n'a pas de tags
	 */
	public List<String> getTagsByContact(String firstname, String name);

	/**
	 * Ajout d'un contact et des nouveaux tags qui lui sont associés
	 * @param firstname prénom du contact
	 * @param name nom du contact
	 * @param email email du contact
	 * @param countryAbbreviation abréviation du pays du contact (éventuellement null) 
	 * @param tagsValues valeurs des tags à associer au contact (éventuellement nouveaux)
	 * @return l'identifiant du contact (lorsque le contact a été ajouté) ou 0 en cas de duplication ou de données incorrectes
	 */
	public int createAndSaveContact(String firstname, String name, String email, String countryAbbreviation, String[] tagsValues);

	/**
	 * Suppression d'un contact
	 * @param id identifiant du contact
	 */
	public void removeContact(int id);
}
