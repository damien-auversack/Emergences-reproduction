package be.helha.aemt.ejb;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import be.helha.aemt.dao.ActiviteDAO;
import be.helha.aemt.entities.Activite;
import be.helha.aemt.entities.Utilisateur;

@Stateless
@LocalBean
public class ActiviteEJB {
	@EJB
	private ActiviteDAO activiteDAO;

	public Activite add(Activite activite) {
		return this.activiteDAO.add(activite);
	}

	public boolean delete(Activite activite) {
		return this.activiteDAO.delete(activite);
	}
	
	public void addPresentateur(Utilisateur utilisateur, Activite activite) {
		this.activiteDAO.addPresentateur(utilisateur, activite);
	}
	
	public Activite find(int id) {
		return this.activiteDAO.find(id);
	}

	public List<Activite> findAll() {
		return this.activiteDAO.findAll();
	}

	public List<Activite> findActiviteByUserID(int id) {
		return this.activiteDAO.findByIdUser(id);
	}

	public Activite update(Activite oldActivite, Activite newActivite) {
		return this.activiteDAO.update(oldActivite, newActivite);
	}
	
	public Activite updatePicture(Activite oldActivite, byte[] fileContents) {
		return this.activiteDAO.updatePicture(oldActivite, fileContents);
	}

	public void addParticipant(Utilisateur utilisateur, Activite activite) {
		this.activiteDAO.addParticipant(utilisateur, activite);
	}

	public void deleteParticipant(Utilisateur utilisateur, Activite activite) {
		this.activiteDAO.deleteParticipant(utilisateur, activite);
	}
	
	public boolean isFull(Activite a) {
		return this.activiteDAO.isFull(a);
	}
	
	public List<Utilisateur> getAllUtilisateurs(int id){
		return this.activiteDAO.findAllUtilisateurs(id);
	}

}
