package be.helha.aemt.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

import javax.persistence.Entity;

@Entity
public class Evenement extends Reunion implements Serializable {
	private static final long serialVersionUID = -5230145100243148158L;

	private LocalDate date;

	public Evenement() {
	}

	public Evenement(String nom, String description, Adresse adresse, boolean presentiel, byte[] picture,
			LocalDate date) {
		super(nom, description, adresse, presentiel, picture);
		this.date = date;
	}

	@Override
	public String toString() {
		return super.toString() + " Evenement [date=" + date + "]";
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
		Evenement other = (Evenement) obj;
		return Objects.equals(date, other.date);
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	//Pour récupérer une date en chaine de caractères afin de faciliter l'affichage
	public String getDateString() {

		final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMMM uuuu", Locale.FRENCH);
		String output = dtf.format(date);
		System.out.println(output);

		return output;
	}
}
