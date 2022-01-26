package be.helha.aemt.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;

@Entity
public abstract class ReunionReservee extends Reunion implements Serializable {
	
	private static final long serialVersionUID = 6717217607380672407L;
	
	private int places;
	private double prix;
	
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	private List<Utilisateur> participants = new ArrayList<Utilisateur>();
	
	public ReunionReservee() {

	}

	public ReunionReservee(String nom, String description, Adresse adresse, boolean presentiel, byte[] picture,int places, double prix) {
		super(nom, description, adresse, presentiel, picture);
		this.places = places;
		this.prix = prix;
	}
	
	//ajoute un participant à la liste
	public boolean ajouterParticipant(Utilisateur u) {
		if(participants.contains(u)) {
			return false;
		}
		return participants.add(u);	
	}
	
	//supprime un participant de la liste
	public boolean deleteParticipant(Utilisateur u) {
		if(participants.contains(u)) {
			return participants.remove(u);
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(places, prix);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReunionReservee other = (ReunionReservee) obj;
		return places == other.places && Double.doubleToLongBits(prix) == Double.doubleToLongBits(other.prix);
	}

	@Override
	public String toString() {
		return super.toString()+ " ReunionReservee [places=" + places + ", prix=" + prix + ", participants=" + participants + "]";
	}

	public int getPlaces() {
		return places;
	}

	public void setPlaces(int places) {
		this.places = places;
	}

	public double getPrix() {
		return prix;
	}

	public void setPrix(double prix) {
		this.prix = prix;
	}
	
	public List<Utilisateur> getParticipants() {
		return participants;
	}

	public void setParticipants(List<Utilisateur> participants) {
		this.participants = participants;
	}
}
