package be.helha.aemt.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;

@Entity
public class Cycle extends ReunionReservee implements Serializable {
	private static final long serialVersionUID = -4962756648350439432L;

	//n'est pas présent dans le persistence.xml et donc est considéré en tant que collection d'élément
	@ElementCollection(fetch = FetchType.EAGER)
	private ArrayList<LocalDate> dates = new ArrayList<LocalDate>();

	public Cycle() {

	}

	public Cycle(String nom, String description, Adresse adresse, boolean presentiel, byte[] picture, int places,
			double prix) {
		super(nom, description, adresse, presentiel, picture, places, prix);
	}

	//ajoute une date à dates
	public boolean ajouterDate(LocalDate l) {
		if (dates.contains(l)) {
			return false;
		}
		return dates.add(l);
	}

	//Pour récupérer la liste de dates en chaine de caractères afin de faciliter l'affichage
	public List<String> getListDateString() {

		List<String> arrayCycle = new ArrayList<>();

		for (LocalDate elt : dates) {
			final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMMM uuuu", Locale.FRENCH);
			arrayCycle.add(dtf.format(elt).toString());
		}

		return arrayCycle;
	}

	@Override
	public String toString() {
		return super.toString() + " Cycle [dates=" + dates + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(dates);
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
		Cycle other = (Cycle) obj;
		return Objects.equals(dates, other.dates) && super.equals(obj);
	}

	public ArrayList<LocalDate> getDates() {
		return dates;
	}

	public void setDates(ArrayList<LocalDate> dates) {
		this.dates = dates;
	}
}
