package model;

import java.util.ArrayList;

public class Model {

	private String nom;
	private String date;
	private ArrayList<ArrayList<String>> retour = new ArrayList<>();
	private ArrayList<String> liste_concat = new ArrayList<>();
	
	public ArrayList<String> getListe_concat() {
		return liste_concat;
	}
	public void setListe_concat(ArrayList<String> liste_concat) {
		this.liste_concat = liste_concat;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public ArrayList<ArrayList<String>> getRetour() {
		return retour;
	}
	public void setRetour(ArrayList<ArrayList<String>> retour) {
		this.retour = retour;
	}
	
	

}
