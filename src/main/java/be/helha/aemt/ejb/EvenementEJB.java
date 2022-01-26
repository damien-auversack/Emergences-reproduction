package be.helha.aemt.ejb;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import be.helha.aemt.dao.EvenementDAO;
import be.helha.aemt.entities.Activite;
import be.helha.aemt.entities.Evenement;
import be.helha.aemt.entities.Utilisateur;

@Stateless
@LocalBean
public class EvenementEJB {
	@EJB
	private EvenementDAO evenementDAO;
	
	public Evenement add(Evenement evenement) {
		return this.evenementDAO.add(evenement);
	}
	
	public boolean delete(Evenement evenement) {
		return this.evenementDAO.delete(evenement);
	}
	
	public void addPresentateur(Utilisateur utilisateur, Evenement evenement) {
		this.evenementDAO.addPresentateur(utilisateur, evenement);
	}
	
	public Evenement find(int id) {
		return this.evenementDAO.find(id);
	}
	
	public List<Evenement> findAll() {
		return this.evenementDAO.findAll();
	}
	
	public Evenement update(Evenement oldEvenement, Evenement newEvenement) {
		return this.evenementDAO.update(oldEvenement, newEvenement);
	}
	
	public Evenement updatePicture(Evenement oldActivite, byte[] fileContents) {
		return this.evenementDAO.updatePicture(oldActivite, fileContents);
	}
}
