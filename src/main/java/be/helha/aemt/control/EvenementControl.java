package be.helha.aemt.control;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import be.helha.aemt.ejb.EvenementEJB;
import be.helha.aemt.entities.Adresse;
import be.helha.aemt.entities.Evenement;
import be.helha.aemt.utils.FileTypeVerifier;

@Named
@RequestScoped
public class EvenementControl {

	@Inject
	private EvenementEJB evenementEJB;
	private String messageReussite;
	private String messageErreur;
	private String nom;
	private String description;
	private String ville;
	private String rue;
	private String codePostal;
	private String numero;
	private String date;
	private boolean presentiel;
	private String image;
	private int id;

	
	private Evenement evenement;
	
	private Part uploadedFile;
	private String fileName;
	private byte[] fileContents;
	
	public void upload(Evenement evenement) {
		fileName = Paths.get(uploadedFile.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
		
		if(!FileTypeVerifier.verifierType(fileName)) return; // si pas autorisé, on sort
		
		try (InputStream input = uploadedFile.getInputStream()) {
			fileContents = input.readAllBytes();

			this.evenementEJB.updatePicture(evenement, fileContents);
		} catch (IOException e) {

		}
	}
	
	public String modifierEvenement() {
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
        
		//conversion String à LocalDate
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate localDate = LocalDate.parse(date, formatter);

		// création de l'adresse
		Adresse adresse = new Adresse(ville, codePostal, rue, numero);
		
		// création de la nouvelle activitée
		Evenement evenementModifie = new Evenement(nom, description, adresse, presentiel, null, localDate);
		
		Evenement reussite = evenementEJB.update(this.getEvenementById(Integer.valueOf(request.getParameter("myForm:idEvenement"))), evenementModifie);

		if(reussite == null) {
			messageErreur = "L'évènement existe déjà";
		}
		else {
			messageReussite = "Evènement modifié";
		}
		return "/admin/evenementsPourModification.xhtml?faces-redirect=true";
	}
	
	public Evenement getEvenementById(int id) {
		if(id != 0) {
			Evenement evenement = evenementEJB.find(id);
			this.setEvenement(evenement);
			this.dispatchValeurs(evenement);
		}
		return evenement;
	}
	
	private void dispatchValeurs(Evenement evenement) {
		System.out.println("test even");
		this.setCodePostal(evenement.getAdresse().getCodePostal());
		
		LocalDate date = evenement.getDate();
		String jour = (date.getDayOfMonth() < 10) ? "0" + Integer.toString(date.getDayOfMonth()) : Integer.toString(date.getDayOfMonth());
		String mois = (date.getMonthValue() < 10) ? "0" + Integer.toString(date.getMonthValue()) : Integer.toString(date.getMonthValue());
		String strDate = jour + "/" 
						+ mois + "/" 
						+ date.getYear();
		this.setDate(strDate);
		
		this.setId(evenement.getId());
		this.setDescription(evenement.getDescription());
		this.setNom(evenement.getNom());
		this.setNumero(evenement.getAdresse().getNumero());
		this.setPresentiel(evenement.isPresentiel());
		this.setRue(evenement.getAdresse().getRue());
		this.setVille(evenement.getAdresse().getVille());
	}

	public List<Evenement> getAllEvenement() {
		List<Evenement> listEvenement = evenementEJB.findAll();

		return listEvenement;
	}

	//récupère les évènements en présentiel
	public List<Evenement> getPresentielEvenement() {

		List<Evenement> listPresentielEvenement = evenementEJB.findAll().stream().filter(evenement -> evenement.isPresentiel())
				.collect(Collectors.toList());

		return listPresentielEvenement;
	}

	//récupère les évènements en distanciel
	public List<Evenement> getOnlineEvenement() {

		List<Evenement> listOnlineEvenement = evenementEJB.findAll().stream().filter(evenement -> !evenement.isPresentiel())
				.collect(Collectors.toList());

		return listOnlineEvenement;
	}
	
	//ajoute un évènement aux réunions
	public void addEvenement() {
		messageReussite="";
		if(nom.length()==0 || description.length()==0 || ville.length()==0 || rue.length()==0 || codePostal.length()==0 || numero.length()==0 || date.length()==0) {
			messageErreur="Merci de compléter tous les champs du formulaire!";
			return;
		}else {
			messageErreur="";
		}
		// conversion String à LocalDate et vérification du format
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate localDate = null;
		try {
			date = date.replaceAll("-", "/");
			String[] dateFormat = date.split("/");
			if(dateFormat[0].length() < 2) {
				dateFormat[0] = "0"+dateFormat[0];
			}
			if(dateFormat[1].length() < 2) {
				dateFormat[1] = "0"+dateFormat[1];
			}
			date = dateFormat[0] +"/"+ dateFormat[1] +"/"+ dateFormat[2];
			localDate = LocalDate.parse(date, formatter);
		} catch (Exception e) {
			messageErreur="Merci de respecter le bon format de date ! (JJ/MM/AAAA)";
			return;
		}
		Adresse adresse = new Adresse(ville, codePostal, rue, numero);
		Evenement evenement = new Evenement(nom, description, adresse, presentiel, null, localDate);
		Evenement reussite = evenementEJB.add(evenement);
		if(reussite==null) {
			messageErreur="L'évènement existe déjà";
			return;
		}
		else {
			messageReussite="Evènement créé";
			nom="";
			description="";
			ville="";
			rue="";
			codePostal="";
			numero="";
			date="";
		}
	}
	
	//redirige vers les détails de l'évènement
	public String doDetails(int id) {
		// on redirige l'utilisateur et on ajoute l'id
		return "/visitor/evenementDetail.xhtml?id=" + id + "&faces-redirect=true";
	}
	
	public Evenement getEvenementById() {
		// on récupère l'id de l'activité dans l'url
		String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
		int intId = Integer.valueOf(id);

		Evenement event = evenementEJB.find(intId);
		this.setEvenement(event);

		return event;
	}

	public Evenement getEvenement() {
		return evenement;
	}

	public void setEvenement(Evenement evenement) {
		this.evenement = evenement;
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

	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}

	public String getRue() {
		return rue;
	}

	public void setRue(String rue) {
		this.rue = rue;
	}

	public String getCodePostal() {
		return codePostal;
	}

	public void setCodePostal(String codePostal) {
		this.codePostal = codePostal;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
