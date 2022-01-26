package be.helha.aemt.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

import javax.persistence.Entity;

@Entity
public class Activite extends ReunionReservee implements Serializable {
	private static final long serialVersionUID = 8837975039366956674L;

	private LocalDate date;

	public Activite() {

	}

	public Activite(String nom, String description, Adresse adresse, boolean presentiel, byte[] picture, int places,
			double prix, LocalDate date) {
		super(nom, description, adresse, presentiel, picture, places, prix);
		this.date = date;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(date);
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
		Activite other = (Activite) obj;
		return Objects.equals(date, other.date) && super.equals(obj);
	}

	@Override
	public String toString() {
		return super.toString() + " Activite [date=" + date + "]";
	}

	public LocalDate getDate() {
		return date;
	}

	//Pour récupérer une date en chaine de caractères afin de faciliter l'affichage
	public String getDateString() {
		final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMMM uuuu", Locale.FRENCH);
		String output = dtf.format(date);
		return output;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

}
