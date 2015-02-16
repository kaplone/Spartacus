package model;

import java.util.ArrayList;

public class Model {
	
	private char[] intro;
	private char[] outro;
	
	private int nombre;
	private String nom;
	
	private String date;
	
	
	public char[] getIntro() {
		return intro;
	}
	public void setIntro(char[] cs) {
		this.intro = cs;
	}
	public char[] getOutro() {
		return outro;
	}
	public void setOutro(char[] outro) {
		this.outro = outro;
	}
	public int getNombre() {
		return nombre;
	}
	public void setNombre(int nombre) {
		this.nombre = nombre;
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

}
