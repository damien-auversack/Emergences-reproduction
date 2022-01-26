package be.helha.aemt.control;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import be.helha.aemt.ejb.ActiviteEJB;
import be.helha.aemt.ejb.UtilisateurEJB;
import be.helha.aemt.entities.Activite;
import be.helha.aemt.entities.Adresse;
import be.helha.aemt.entities.Utilisateur;
import be.helha.aemt.utils.FileTypeVerifier;

@Named
@RequestScoped
public class ActiviteControl{
	@Inject
	private ActiviteEJB activiteEJB;

	@Inject
	private UtilisateurEJB utilisateurEJB;

	private String username;
	private Utilisateur utilisateur;
	private Activite activite;
	
 	private String ville;
	private String codePostal;
	private String rue;
	private String numero;
	private String nom;
	private String description;
	private double prix;
	private int places;
	private boolean presentiel;
	private String image;
	private String date;
	private int id;
	
	private String messageErreur;
	private String messageReussite;

	private Part uploadedFile;
	private String fileName;
	private byte[] fileContents;

	public Part getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(Part uploadedFile) {
		this.uploadedFile = uploadedFile;
	}
	
	public void upload(Activite activite) {
		fileName = Paths.get(uploadedFile.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
		
		if(!FileTypeVerifier.verifierType(fileName)) return; // si pas autorisé, on sort
		
		try (InputStream input = uploadedFile.getInputStream()) {
			fileContents = input.readAllBytes();

			this.activiteEJB.updatePicture(activite, fileContents);
		} catch (IOException e) {

		}
	}

	//permet d'ajouter une activité aux réunions
	public void ajouterActivite() {
		messageReussite = "";
		if(nom.length() == 0 || description.length() == 0 || ville.length() == 0 || rue.length() == 0 
				|| codePostal.length() == 0 || numero.length() == 0 || date.length() == 0) {
			messageErreur = "Merci de compléter tous les champs du formulaire!";
			return;
		} else {
			messageErreur = "";
		}
		// conversion String à LocalDate et vérification du format
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate localDate = null;
		try {
			date = date.replaceAll("-", "/");
			String[] dateFormat = date.split("/");
			if (dateFormat[0].length() < 2) {
				dateFormat[0] = "0" + dateFormat[0];
			}
			if (dateFormat[1].length() < 2) {
				dateFormat[1] = "0" + dateFormat[1];
			}
			date = dateFormat[0] + "/" + dateFormat[1] + "/" + dateFormat[2];
			localDate = LocalDate.parse(date, formatter);
		} catch (Exception e) {
			messageErreur = "Merci de respecter le bon format de date ! (JJ/MM/AAAA)";
			return;
		}

		Adresse adresse = new Adresse(ville, codePostal, rue, numero);

		Activite activite = new Activite(nom, description, adresse, presentiel, null, places, prix, localDate);

		Activite reussite = activiteEJB.add(activite);

		if (reussite == null) {
			messageErreur = "L'évènement existe déjà";
			return;
		} else {
			messageReussite = "Evènement créé";
			nom = "";
			description = "";
			ville = "";
			rue = "";
			codePostal = "";
			numero = "";
			date = "";
		}
		return;
	}
	
	public String modifierActivite() {
		messageReussite = "";
		
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		List<String> valeurs = new ArrayList<>(
			Arrays.asList( // on récupère les valeurs des inputs texts myForm = id du form, :<nom> = id de l'input text
				request.getParameter("myForm:nom"),
				request.getParameter("myForm:description"),
				request.getParameter("myForm:ville"),
				request.getParameter("myForm:rue"),
				request.getParameter("myForm:codePostal"),
				request.getParameter("myForm:numero"),
				request.getParameter("myForm:date")
			)
		);
		
        this.nom = valeurs.get(0);
        this.description = valeurs.get(1);
        this.ville = valeurs.get(2);
        this.rue = valeurs.get(3);
        this.codePostal = valeurs.get(4);
        this.numero = valeurs.get(5);
        this.date = valeurs.get(6);
        
        // le string renvoyé est soit null (pas en string) soit on (en string)
        this.presentiel = (request.getParameter("myForm:presentiel") == null) ? false : true;
        
        this.places = Integer.valueOf(request.getParameter("myForm:places"));
        this.prix = Double.valueOf(request.getParameter("myForm:prix"));
        
		//conversion String à LocalDate
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate localDate = LocalDate.parse(date, formatter);

		// création de l'adresse
		Adresse adresse = new Adresse(ville, codePostal, rue, numero);
		
		// création de la nouvelle activitée
		Activite activiteModifiee = new Activite(nom, description, adresse, presentiel, null, places, prix, localDate);
		
		Activite reussite = activiteEJB.update(this.getActiviteById(Integer.valueOf(request.getParameter("myForm:idActivite"))), activiteModifiee);

		if(reussite == null) {
			messageErreur = "L'évènement existe déjà";
		}
		else {
			messageReussite = "Evènement modifié";
		}
		return "/admin/activitesPourModification.xhtml?faces-redirect=true";
	}
	
	public List<Activite> getAllActivite() {
		List<Activite> listActivite = activiteEJB.findAll();

		return listActivite;
	}
	
	public Activite getActiviteById() {
        // on récupère l'id de l'activité dans l'url
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        int intId = Integer.valueOf(id);

        Activite activite = activiteEJB.find(intId);
        this.setActivite(activite);

        return activite;
    }
	
	public Activite getActiviteById(String id) {
        // on récupère l'id de l'activité dans l'url
        int intId = Integer.valueOf(id);
        Activite activite = activiteEJB.find(intId);
        this.setActivite(activite);
		this.dispatchValeurs(activite);
        return activite;
    }
	
	public Activite getActiviteById(int id) {
		if(id != 0) {
			Activite activite = activiteEJB.find(id);
			this.setActivite(activite);
			this.dispatchValeurs(activite);
		}
		return activite;
	}

	//récupère les activités en présentiel parmis toutes les activités
	public List<Activite> getPresentielActivite() {
		List<Activite> listPresentielActivite = activiteEJB.findAll().stream()
				.filter(activite -> activite.isPresentiel()).collect(Collectors.toList());

		return listPresentielActivite;
	}

	//récupère les activités en ligne parmis toutes les activités
	public List<Activite> getOnlineActivite() {
		List<Activite> listOnlineActivite = activiteEJB.findAll().stream().filter(activite -> !activite.isPresentiel())
				.collect(Collectors.toList());

		return listOnlineActivite;
	}
	
	//récupère l'utilisateur connecté
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

	public List<Activite> getActiviteByUserID() {
		Utilisateur userTmp = getCurrentUtilisateur();

		return activiteEJB.findActiviteByUserID(userTmp.getId());
	}

	public List<Activite> getActiviteByUserID(int id) {
		return activiteEJB.findActiviteByUserID(id);
	}

	//vérifie si l'utilisateur est inscrit à une activité
	public boolean isUserSubscribeOnActivite(Activite activite) {
		Utilisateur userTmp = getCurrentUtilisateur();
		List<Activite> listActiviteTmp = getActiviteByUserID(userTmp.getId());

		for (Activite activiteTmp : listActiviteTmp) {
			if (activiteTmp.getId() == activite.getId()) {
				return true;
			}
		}
		return false;
	}

	public boolean isFull(Activite a) {
		return this.activiteEJB.isFull(a);
	}
	
	public boolean isSubscriptable(Activite a){
		return isFull(a)||isUserSubscribeOnActivite(a);
	}
	
	private void dispatchValeurs(Activite activite) {
		System.out.println("test activite");
		this.setCodePostal(activite.getAdresse().getCodePostal());
		
		LocalDate date = activite.getDate();
		String jour = (date.getDayOfMonth() < 10) ? "0" + Integer.toString(date.getDayOfMonth()) : Integer.toString(date.getDayOfMonth());
		String mois = (date.getMonthValue() < 10) ? "0" + Integer.toString(date.getMonthValue()) : Integer.toString(date.getMonthValue());
		String strDate = jour + "/" 
						+ mois + "/" 
						+ date.getYear();
		this.setDate(strDate);
		
		this.setId(activite.getId());
		this.setDescription(activite.getDescription());
		this.setNom(activite.getNom());
		this.setNumero(activite.getAdresse().getNumero());
		this.setPlaces(activite.getPlaces());
		this.setPresentiel(activite.isPresentiel());
		this.setPrix(activite.getPrix());
		this.setRue(activite.getAdresse().getRue());
		this.setVille(activite.getAdresse().getVille());
	}

	public void addParticipant(Activite activite) throws NamingException {
		getCurrentUtilisateur();
		activiteEJB.addParticipant(this.utilisateur, activite);
	}

	public void deleteParticipant(Activite activite) {
		getCurrentUtilisateur();
		activiteEJB.deleteParticipant(this.utilisateur, activite);
	}

	public void deleteParticipant(int idUtilisateur, Activite activite) {
		Utilisateur u = utilisateurEJB.find(idUtilisateur);
		if (u != null)
			this.activiteEJB.deleteParticipant(u, activite);
	}
	
	public void deleteParticipant(String idActivite, int idUtilisateur) {
		Utilisateur u = utilisateurEJB.find(idUtilisateur);
		Activite activites = activiteEJB.find(Integer.parseInt(idActivite));
		if (u != null&& activites!=null)
			this.activiteEJB.deleteParticipant(u, activites);
	}

	public List<Utilisateur> getAllUtilisateurs(String id){
		int idInt = Integer.parseInt(id);
		return this.activiteEJB.getAllUtilisateurs(idInt);
	}
	
	//redirige vers la page affichant les détails de l'activité
	public String doDetails(int id) {
		// on redirige l'utilisateur et on ajoute l'id
		return "/visitor/activiteDetail.xhtml?id=" + id + "&faces-redirect=true";
	}

	public Activite getActivite() {
		return activite;
	}

	public void setActivite(Activite activite) {
		this.activite = activite;
	}

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}

	public String getCodePostal() {
		return codePostal;
	}

	public void setCodePostal(String codePostal) {
		this.codePostal = codePostal;
	}

	public String getRue() {
		return rue;
	}

	public void setRue(String rue) {
		this.rue = rue;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrix() {
		return prix;
	}

	public void setPrix(double prix) {
		this.prix = prix;
	}

	public int getPlaces() {
		return places;
	}

	public void setPlaces(int places) {
		this.places = places;
	}

	public boolean isPresentiel() {
		return presentiel;
	}

	public void setPresentiel(boolean presentiel) {
		this.presentiel = presentiel;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getMessageErreur() {
		return messageErreur;
	}

	public void setMessageErreur(String messageErreur) {
		this.messageErreur = messageErreur;
	}

	public String getMessageReussite() {
		return messageReussite;
	}

	public void setMessageReussite(String messageReussite) {
		this.messageReussite = messageReussite;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
