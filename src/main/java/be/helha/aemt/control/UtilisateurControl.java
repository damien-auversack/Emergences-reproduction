package be.helha.aemt.control;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.Base64;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import be.helha.aemt.ejb.ActiviteEJB;
import be.helha.aemt.ejb.CycleEJB;
import be.helha.aemt.ejb.EvenementEJB;
import be.helha.aemt.ejb.UtilisateurEJB;
import be.helha.aemt.entities.Activite;
import be.helha.aemt.entities.Cycle;
import be.helha.aemt.entities.Evenement;
import be.helha.aemt.entities.Role;
import be.helha.aemt.entities.Utilisateur;
import be.helha.aemt.utils.FileTypeVerifier;

@Named
@RequestScoped
public class UtilisateurControl {
	private String username;
	private Utilisateur utilisateur;

	@Inject
	private UtilisateurEJB utilisateurEJB;

	@Inject
	private CycleEJB cycleEJB;

	@Inject
	private ActiviteEJB activiteEJB;

	@Inject
	private EvenementEJB evenementEJB;

	@Inject
	private NavigationControl navigationControl;

	private int id;
	private String login;
	private String password;
	private String confirmPassword;

	private String messageErreur = "";
	private String messageReussite = "";

	private Part uploadedFile;
	private String fileName;
	private byte[] fileContents;

	// vérifie si l'utilisateur est déjà attribué en tant que présentateur à
	// l'activité
	public boolean isPresentateurActivite(Utilisateur utilisateur) {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		int idActivite = Integer.valueOf(request.getParameter("id"));

		return this.activiteEJB.find(idActivite).getPresentateurs().contains(utilisateur);
	}
	
	public boolean isPresentateurEvenement(Utilisateur utilisateur) {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		int idActivite = Integer.valueOf(request.getParameter("id"));

		return this.evenementEJB.find(idActivite).getPresentateurs().contains(utilisateur);
	}
	
	public boolean isPresentateurCycle(Utilisateur utilisateur) {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		int idActivite = Integer.valueOf(request.getParameter("id"));

		return this.cycleEJB.find(idActivite).getPresentateurs().contains(utilisateur);
	}


	// vérifie si l'utilisateur est admin

	public boolean isAdmin() {
		Utilisateur userTmp = getCurrentUtilisateur();
		if (userTmp.getRole() == Role.ADMIN) {
			return true;
		} else {
			return false;
		}
	}

	public void upload(Utilisateur utilisateur) {
		fileName = Paths.get(uploadedFile.getSubmittedFileName()).getFileName().toString(); // MSIE fix.

		if (!FileTypeVerifier.verifierType(fileName))
			return; // si pas autorisé, on sort

		try (InputStream input = uploadedFile.getInputStream()) {
			fileContents = input.readAllBytes();

			this.utilisateurEJB.updatePicture(utilisateur, fileContents);

		} catch (IOException e) {

		}
	}

	// récupère l'utilisateur connecté
	public Utilisateur getCurrentUtilisateur() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		//
		// you can fetch user from database for authenticated principal and do some
		// action
		Principal principal = request.getUserPrincipal();
		username = principal.toString();

		utilisateur = utilisateurEJB.findByLogin(username);
		return utilisateur;
	}

	public String getMessageErreur() {
		return messageErreur;
	}

	public void setMessageErreur(String messageErreur) {
		this.messageErreur = messageErreur;
	}

	// ajoute un utilisateur
	public void addUtilisateur() throws NamingException, NoSuchAlgorithmException {
		messageReussite = "";
		if (login.length() == 0 || password.length() == 0) {
			messageErreur = "Complétez le formulaire complètement !";
			return;
		} else {
			messageErreur = "";
		}

		if (password.equals(confirmPassword)) {
			messageErreur = "";
			Utilisateur temp = new Utilisateur(encode(password), login, this.fileContents, Role.USER);
			Utilisateur reussite = utilisateurEJB.add(temp);
			if (reussite == null) {
				messageErreur = "Ce nom d'utilisateur est déjà utilisé.";
			} else {
				messageReussite = "Inscription réussie.";
				login = "";

			}
			return;
		} else {
			messageErreur = "Les mots de passes ne correspondent pas.";
		}
	}

	// ajoute un présentateur à une des réunions
	public String ajoutPresentateurActivite(Utilisateur utilisateur, int id) {

		Activite activite = activiteEJB.find(id);

		activiteEJB.addPresentateur(utilisateur, activite);

		return navigationControl.doAjoutPresentateurActivite(id);

	}
	
	// ajoute un présentateur à une des réunions
	public String ajoutPresentateurEvenement(Utilisateur utilisateur, int id) {

		Evenement evenement = evenementEJB.find(id);
			
		evenementEJB.addPresentateur(utilisateur, evenement);
			
		return navigationControl.doAjoutPresentateurEvenement(id);
	}
	
	// ajoute un présentateur à une des réunions
	public String ajoutPresentateurCycle(Utilisateur utilisateur, int id) {
				
		Cycle cycle = cycleEJB.find(id);
		cycleEJB.addPresentateur(utilisateur, cycle);
		return navigationControl.doAjoutPresentateurCycle(id);
	}

	public List<Utilisateur> getAllUtilisateurs() throws NamingException {
		return utilisateurEJB.findAll();
	}

	public void changeRole(Utilisateur u) {
		utilisateurEJB.changeRole(u);
	}

	// chiffre un text avec l'algorithme SHA-256 en base 64
	public static String encode(String clearText) throws NoSuchAlgorithmException {
		return new String(Base64.getEncoder()
				.encode(MessageDigest.getInstance("SHA-256").digest(clearText.getBytes(StandardCharsets.UTF_8))));
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public Part getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(Part uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getFileContents() {
		return fileContents;
	}

	public void setFileContents(byte[] fileContents) {
		this.fileContents = fileContents;
	}

	public String getMessageReussite() {
		return messageReussite;
	}

	public void setMessageReussite(String messageReussite) {
		this.messageReussite = messageReussite;
	}
}
