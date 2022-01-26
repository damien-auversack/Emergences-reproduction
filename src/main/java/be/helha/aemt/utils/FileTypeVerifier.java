package be.helha.aemt.utils;

import org.primefaces.shaded.commons.io.FilenameUtils;

public class FileTypeVerifier {
	private static String[] typesAuthorises = {"png", "jpg", "jpeg", "gif"};
	
	public static boolean verifierType(String pathFichier) {
		boolean typeOk = false;
		
		// On vérifie quue le type est bien un type de fichier autorisé
		for(String type : typesAuthorises) {
			if(type.equals(FilenameUtils.getExtension(pathFichier))) {
				typeOk = true;
				break; // pas la peine de vérifier le reste si il y a une correspondance
			}
		}
		
		return typeOk;
	}
}
