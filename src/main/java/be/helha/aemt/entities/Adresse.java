package be.helha.aemt.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Adresse implements Serializable {

	private static final long serialVersionUID = 1157340250231631189L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	
	private String ville;
	private String codePostal;
	private String rue;
	private String numero;
	
	// Constructors
	public Adresse() { }
	
	public Adresse(String ville, String codePostal, String rue, String numero) {
		this.ville = ville;
		this.codePostal = codePostal;
		this.rue = rue;
		this.numero = numero;
	}
	
	// Special methods
	@Override
	public int hashCode() {
		return Objects.hash(codePostal, numero, rue, ville);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Adresse other = (Adresse) obj;
		return Objects.equals(codePostal, other.codePostal) && Objects.equals(numero, other.numero)
				&& Objects.equals(rue, other.rue) && Objects.equals(ville, other.ville);
	}

	@Override
	public String toString() {
		return "Adresse [id=" + id + ", ville=" + ville + ", codePostal=" + codePostal + ", rue=" + rue + ", numero="
				+ numero + "]";
	}

	// Getters and setters
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getVille() {
		return this.ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}

	public String getCodePostal() {
		return this.codePostal;
	}

	public void setCodePostal(String codePostal) {
		this.codePostal = codePostal;
	}

	public String getRue() {
		return this.rue;
	}

	public void setRue(String rue) {
		this.rue = rue;
	}

	public String getNumero() {
		return this.numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}
}
