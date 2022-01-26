package be.helha.aemt.ejb;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import be.helha.aemt.dao.UtilisateurDAO;
import be.helha.aemt.entities.Role;
import be.helha.aemt.entities.Utilisateur;

@Stateless
@LocalBean
public class UtilisateurEJB {
	@EJB
	private UtilisateurDAO utilisateurDAO;
	
	public Utilisateur add(Utilisateur utilisateur) {
		return this.utilisateurDAO.add(utilisateur);
	}
	
	public boolean delete(Utilisateur utilisateur) {
		return this.utilisateurDAO.delete(utilisateur);
	}
	
	public Utilisateur find(int id) {
		return this.utilisateurDAO.find(id);
	}
	
	public Utilisateur findByLogin(String login) {
		return this.utilisateurDAO.findByLogin(login);
	}
	
	public List<Utilisateur> findAll() {
		return this.utilisateurDAO.findAll();
	}
	
	public Utilisateur update(Utilisateur oldUtilisateur, Utilisateur newUtilisateur) {
		return this.utilisateurDAO.update(oldUtilisateur, oldUtilisateur);
	}
	
	public Utilisateur updatePicture(Utilisateur oldUtilisateur, byte[] fileContents) {
		return this.utilisateurDAO.updatePicture(oldUtilisateur, fileContents);
	}
	
	public Role changeRole(Utilisateur utilisateur) {
		return this.utilisateurDAO.changeRole(utilisateur);
	}
}
