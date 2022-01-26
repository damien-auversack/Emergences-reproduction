package be.helha.aemt.client;

import be.helha.aemt.dao.UtilisateurDAO;
import be.helha.aemt.entities.Role;
import be.helha.aemt.entities.Utilisateur;

public class Main {
	public static void main(String[] args) {
		// test doublons dao
		UtilisateurDAO uDao = new UtilisateurDAO();
		
		Utilisateur test = new Utilisateur("Florian", "d+Rn6wFp6C538JDfIXoyM1fGoVepjAN15vbbr+ApyDo=", null, Role.USER);
		
		uDao.add(test);
	}
}
