package be.helha.aemt.entities;

import java.io.IOException;
import java.io.Serializable;
import java.util.Base64;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Utilisateur implements Serializable {
	private static final long serialVersionUID = -7139681684018633761L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String password;
	private String login;
	
	//format pour stocker les images;
	private byte[] picture;

	@Enumerated(EnumType.STRING)
	private Role role;

	// Constructors
	public Utilisateur() {
		
	}

	public Utilisateur(String password, String login, byte[] picture, Role role) {
		this.password = password;
		this.login = login;
		this.picture = picture;
		this.role = role;
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

	// Special methods
	@Override
	public String toString() {
		return "Utilisateur [id=" + id + ", password=" + password + ", login=" + login + ", picture=" + picture
				+ ", role=" + role + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(login, role);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Utilisateur other = (Utilisateur) obj;
		return Objects.equals(login, other.login);
	}

	// Getters and setters
	public Integer getId() {
		return this.id;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLogin() {
		return this.login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public byte[] getPicture() {
		return picture;
	}

	public void setPicture(byte[] picture) {
		this.picture = picture;
	}
}
