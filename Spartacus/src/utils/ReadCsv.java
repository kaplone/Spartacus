package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Collectors;

import model.Model;

public class ReadCsv {

	public static void main(String[] args) {
		
		Model resultat = readAndParseCsv("/home/autor/Desktop/ME/export3.csv");
		ArrayList<ArrayList<String>> retour = resultat.getRetour();
		
		System.out.println("NOM : " + resultat.getNom() + "\nDATE : " + resultat.getDate() + "\n");
		System.out.println("PRESENTS : \n" + retour.get(0).stream().collect(Collectors.joining("\n")));
		System.out.println("\nEN ATTENTE : \n" + retour.get(1).stream().collect(Collectors.joining("\n")));
		System.out.println("\nCONCAT : \n" + resultat.getListe_concat().stream().collect(Collectors.joining("\n")));

	}
	
	public static Model readAndParseCsv(String s){
		
		Model model = new Model();
    			
		ArrayList<String> present = new ArrayList<>();
		ArrayList<String> attente = new ArrayList<>();
		ArrayList<String> concat = new ArrayList<>();
		
		
		
		ArrayList<ArrayList<String>> retour = new ArrayList<>();
		String n = null;
		File f = new File(s);
		try {
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			n = br.readLine(); // lit les en-tetes
			model.setNom(n.split(",")[0]);
	    	model.setDate(n.split(",")[1]);

			while ((n = br.readLine()) != null){
				for (String m : n.split(",")){
					if (! m.startsWith("•") && ! m.isEmpty() && ! m.startsWith("RESTO_0")){
						if(Paths.get("/mnt/nfs_public/pour David/me/M2V", String.format("%s.m2v", m)).toFile().exists()){
						   present.add(m);
						}
						else{
							attente.add(m);
						}
						concat.add(Paths.get("/mnt/nfs_public/pour David/me/M2V", String.format("%s.m2v", m)).toString());
					}
				}
				
			}
		} catch (IOException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		}
		
		retour.add(present);
		retour.add(attente);
		model.setRetour(retour);
		model.setListe_concat(concat);
		return model;
	}

}
