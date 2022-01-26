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

import be.helha.aemt.ejb.CycleEJB;
import be.helha.aemt.ejb.UtilisateurEJB;
import be.helha.aemt.entities.Activite;
import be.helha.aemt.entities.Adresse;
import be.helha.aemt.entities.Cycle;
import be.helha.aemt.entities.Evenement;
import be.helha.aemt.entities.Utilisateur;
import be.helha.aemt.utils.FileTypeVerifier;

@Named
@RequestScoped

public class CycleControl{
	
	@Inject
	private NavigationControl navigationCtrl;

	@Inject
	private CycleEJB cycleEJB;
	@Inject
	private UtilisateurEJB utilisateurEJB;

	private String username;
	private Utilisateur utilisateur;

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
	private String date = "";
	private String messageReussite;
	private String messageErreur;
	private Cycle selectedCycle;
	private int id;
	
	private Cycle cycle;

	private Part uploadedFile;
	private String fileName;
	private byte[] fileContents;
	
	public void upload(Cycle cycle) {
		fileName = Paths.get(uploadedFile.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
		
		if(!FileTypeVerifier.verifierType(fileName)) return; // si pas autorisé, on sort
		
		try (InputStream input = uploadedFile.getInputStream()) {
			fileContents = input.readAllBytes();

			this.cycleEJB.updatePicture(cycle, fileContents);
		} catch (IOException e) {

		}
	}
	
	public String modifierCycle() {
		messageReussite = "";
		
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		List<String> valeurs = new ArrayList<>(
			Arrays.asList( // on récupère les valeurs des inputs texts myForm = id du form, :<nom> = id de l'input text
				request.getParameter("myForm:nom"),
				request.getParameter("myForm:description"),
				request.getParameter("myForm:ville"),
				request.getParameter("myForm:rue"),
				request.getParameter("myForm:codePostal"),
				request.getParameter("myForm:numero")
			)
		);
		
        this.nom = valeurs.get(0);
        this.description = valeurs.get(1);
        this.ville = valeurs.get(2);
        this.rue = valeurs.get(3);
        this.codePostal = valeurs.get(4);
        this.numero = valeurs.get(5);
        
        // le string renvoyé est soit null (pas en string) soit on (en string)
        this.presentiel = (request.getParameter("myForm:presentiel") == null) ? false : true;
        
        this.places = Integer.valueOf(request.getParameter("myForm:places"));
        this.prix = Double.valueOf(request.getParameter("myForm:prix"));
       
		// création de l'adresse
		Adresse adresse = new Adresse(ville, codePostal, rue, numero);
		
		// création de la nouvelle activitée
		Cycle cycleModifie = new Cycle(nom, description, adresse, presentiel, null, places, prix);
		
		Cycle reussite = cycleEJB.update(this.getCycleById(Integer.valueOf(request.getParameter("myForm:idCycle"))), cycleModifie);

		if(reussite == null) {
			messageErreur = "L'évènement existe déjà";
		}
		else {
			messageReussite = "Evènement modifié";
		}
		return "/admin/cyclesPourModification.xhtml?faces-redirect=true";
	}
	
	private void dispatchValeurs(Cycle cycle) {
		this.setCodePostal(cycle.getAdresse().getCodePostal());
		
		this.setId(cycle.getId());
		this.setDescription(cycle.getDescription());
		this.setNom(cycle.getNom());
		this.setNumero(cycle.getAdresse().getNumero());
		this.setPlaces(cycle.getPlaces());
		this.setPresentiel(cycle.isPresentiel());
		this.setPrix(cycle.getPrix());
		this.setRue(cycle.getAdresse().getRue());
		this.setVille(cycle.getAdresse().getVille());
	}
	
	public List<Cycle> getAllCycle() {
		List<Cycle> listTmp = cycleEJB.findAll();
		
		return listTmp;
	}

	//vérifie si l'utilisateur est inscrit au cycle
	public boolean isUserSubscribeOnCycle(Cycle cycle) {
		Utilisateur userTmp = getCurrentUtilisateur();
		List<Cycle> listCycleTmp = getCycleByUserID(userTmp.getId());

		for (Cycle cycleTmp : listCycleTmp) {
			if (cycleTmp.getId() == cycle.getId()) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isFull(Cycle a) {
		return this.cycleEJB.isFull(a);
	}
	
	public boolean isSubscriptable(Cycle a){
		return isFull(a)||isUserSubscribeOnCycle(a);
	}

	//récupère tous les cycles en présentiel
	public List<Cycle> getPresentielCycle() {
		List<Cycle> listPresentielCycle = cycleEJB.findAll().stream().filter(cycle -> cycle.isPresentiel())
				.collect(Collectors.toList());

		return listPresentielCycle;
	}

	//récupère tous les cycles en ligne
	public List<Cycle> getOnlineCycle() {
		List<Cycle> listOnlineCycle = cycleEJB.findAll().stream().filter(cycle -> !cycle.isPresentiel())
				.collect(Collectors.toList());

		return listOnlineCycle;
	}

	public void addParticipant(Cycle cycle) throws NamingException {
		getCurrentUtilisateur();
		cycleEJB.addParticipant(this.utilisateur, cycle);
	}

	public void deleteParticipant(Cycle cycle) throws NamingException {
		getCurrentUtilisateur();
		cycleEJB.deleteParticipant(this.utilisateur, cycle);
	}
	
	public void deleteParticipant(String idCycle, int idUtilisateur) {
		Utilisateur u = utilisateurEJB.find(idUtilisateur);
		Cycle cycles = cycleEJB.find(Integer.parseInt(idCycle));
		if (u != null&& cycles!=null)
			this.cycleEJB.deleteParticipant(u, cycles);
	}

	//récupère l'utilisateur connecté
	public Utilisateur getCurrentUtilisateur() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		// you can fetch user from database for authenticated principal and do some
		// action
		Principal principal = request.getUserPrincipal();
		username = principal.toString();

		utilisateur = utilisateurEJB.findByLogin(username);
		return utilisateur;
	}
	
	public List<LocalDate> getCycleDatesByID(String id) {
		return cycleEJB.findCycleDatesByID(Integer.parseInt(id));
	}

	public List<Cycle> getCycleByUserID() {
		Utilisateur userTmp = getCurrentUtilisateur();
		return cycleEJB.findCycleByUserID(userTmp.getId());
	}

	public List<Cycle> getCycleByUserID(int id) {
		return cycleEJB.findCycleByUserID(id);
	}

	//ajoute une date au cycle
	public String addDate(String dates, String idCycle) {
		messageReussite = "";
		// conversion String à LocalDate et vérification du format
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate localDate = null;
		try {
			date = date.replaceAll("-", "/");
			String[] dateFormat = dates.split("/");
			if (dateFormat[0].length() < 2) {
				dateFormat[0] = "0" + dateFormat[0];
			}
			if (dateFormat[1].length() < 2) {
				dateFormat[1] = "0" + dateFormat[1];
			}
			dates = dateFormat[0] + "/" + dateFormat[1] + "/" + dateFormat[2];
			localDate = LocalDate.parse(dates, formatter);
		} catch (Exception e) {
			messageErreur = "Merci de respecter le bon format de date ! (JJ/MM/AAAA)";
			return null;
		}
		Cycle c = cycleEJB.find(Integer.parseInt(idCycle));
		cycleEJB.addDate(localDate, c);
		return navigationCtrl.doAjoutDatesCycle(Integer.parseInt(idCycle));
	}

	//ajoute un cycle
	public String addCycle() {
		messageReussite = "";
		if (nom.length() == 0 || description.length() == 0 || ville.length() == 0 || rue.length() == 0
				|| codePostal.length() == 0 || numero.length() == 0) {
			messageErreur = "Merci de compléter tous les champs du formulaire!";
			return null;
		} else {
			messageErreur = "";
		}
		Adresse adresse = new Adresse(ville, codePostal, rue, numero);

		Cycle cycle = new Cycle(nom, description, adresse, presentiel, null, places, prix);
		Cycle reussite = cycleEJB.add(cycle);
		if (reussite == null) {
			messageErreur = "Le cycle existe déjà";
			return null;
		} else {
			messageReussite = "Cycle créé";
			description = "";
			ville = "";
			rue = "";
			codePostal = "";
			numero = "";
			date = "";
			prix = 0;
			places = 0;
		}
		return navigationCtrl.doAjoutDatesCycle(reussite.getId());
	}
	
	//redirection vers les détails du cycle
	public String doDetails(int id) {
		// on redirige l'utilisateur et on ajoute l'id
		return "/visitor/cycleDetail.xhtml?id=" + id + "&faces-redirect=true";
	}
	
	public Cycle getCycleById() {
		// on récupère l'id de l'activité dans l'url
		String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
		int intId = Integer.valueOf(id);

		Cycle cycle = cycleEJB.find(intId);
		this.setCycle(cycle);
		this.dispatchValeurs(cycle);

		return cycle;
	}
	
	public Cycle getCycleById(int id) {
		if(id != 0) {
			Cycle cycle = cycleEJB.find(id);
			this.setCycle(cycle);
			this.dispatchValeurs(cycle);
		}
		return cycle;
	}
	public Cycle getCycleById(String id) {
        // on récupère l'id de l'activité dans l'url
        int intId = Integer.valueOf(id);
        Cycle cycle = cycleEJB.find(intId);
        this.setCycle(cycle);
		this.dispatchValeurs(cycle);
        
        return cycle;
    }

	public List<Utilisateur> getAllUtilisateurs(String id){
		int idInt = Integer.parseInt(id);
		return this.cycleEJB.getAllUtilisateurs(idInt);
	}
	
	
	public Cycle getCycle() {
		return cycle;
	}

	public void setCycle(Cycle cycle) {
		this.cycle = cycle;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
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

	public String getMessageReussite() {
		return messageReussite;
	}

	public void setMessageReussite(String messageReussite) {
		this.messageReussite = messageReussite;
	}

	public String getMessageErreur() {
		return messageErreur;
	}

	public void setMessageErreur(String messageErreur) {
		this.messageErreur = messageErreur;
	}

	public Cycle getSelectedCycle() {
		return selectedCycle;
	}

	public void setSelectedCycle(Cycle selectedCycle) {
		this.selectedCycle = selectedCycle;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
	
}
