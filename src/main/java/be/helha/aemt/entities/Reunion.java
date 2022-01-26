package be.helha.aemt.entities;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public abstract class Reunion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String nom;
	private String description;

	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	private Adresse adresse;
	private boolean presentiel;
	//format pour stocker les images;
	private byte[] picture;
	
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JoinTable(name="activite_presentateur")
	private List<Utilisateur> presentateurs = new ArrayList<Utilisateur>();

	public Reunion() {

	}
	
	//Pour récupérer la liste de dates en chaine de caractères afin de faciliter l'affichage
		public List<String> getListPresentateurString() {

			List<String> arrayPresentateurs = new ArrayList<>();

			for (Utilisateur elt : presentateurs) {
				arrayPresentateurs.add(elt.getLogin());
			}

			return arrayPresentateurs;
		}
	
	//transforme une liste de byte en image
	public String byteArrayToImage() throws IOException {
		String imageString;
		try {
			imageString = new String(Base64.getEncoder().encodeToString(this.picture));
			return imageString;
		} catch (Exception e) {
			return "";
		}

	}

	public Reunion(String nom, String description, Adresse adresse, boolean presentiel, byte[] picture) {
		this.nom = nom;
		this.description = description;
		this.adresse = adresse;
		this.presentiel = presentiel;
		this.picture = picture;
	}

	//ajoute un présentateur à la liste
	public boolean ajouterPresentateur(Utilisateur u) {
		if(presentateurs.contains(u)) {
			return false;
		}
		return presentateurs.add(u);	
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(adresse, description, nom, presentiel);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Reunion other = (Reunion) obj;
		return Objects.equals(adresse, other.adresse) && Objects.equals(description, other.description)
				&& Objects.equals(nom, other.nom) && presentiel == other.presentiel;
	}

	@Override
	public String toString() {
		return "Reunion [id=" + id + ", nom=" + nom + ", description=" + description + ", adresse=" + adresse
				+ ", presentiel=" + presentiel + ", picture=" + picture + ", presentateurs=" + presentateurs + "]";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Adresse getAdresse() {
		return adresse;
	}

	public void setAdresse(Adresse adresse) {
		this.adresse = adresse;
	}

	public boolean isPresentiel() {
		return presentiel;
	}

	public void setPresentiel(boolean presentiel) {
		this.presentiel = presentiel;
	}

	public byte[] getPicture() {
		return picture;
	}

	public void setPicture(byte[] picture) {
		this.picture = picture;
	}

	public List<Utilisateur> getPresentateurs() {
		return presentateurs;
	}

	public void setPresentateurs(List<Utilisateur> presentateurs) {
		this.presentateurs = presentateurs;
	}
}
