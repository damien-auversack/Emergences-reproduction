package be.helha.aemt.dao;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import be.helha.aemt.entities.Activite;
import be.helha.aemt.entities.Evenement;
import be.helha.aemt.entities.Utilisateur;

@Stateless
@LocalBean
public class EvenementDAO extends DAO<Evenement> {
	
	@EJB 
	private UtilisateurDAO utilisateurDAO;
	
	public EvenementDAO() {
		
	}
	
	@Override
	public Evenement add(Evenement aAjouter) {
		if(aAjouter == null) return null;
		
		Evenement evenement = this.find(aAjouter.getId());
		if(evenement != null) return null;
		
		List<Evenement> evenements = this.findAll();
        //vérification sur les doublons
		for(Evenement evenement2 : evenements) {
			if(evenement2.equals(aAjouter)) return null;
		}
			
		entityManager.persist(aAjouter);
		return aAjouter;
	}
	
	public Utilisateur addPresentateur(Utilisateur utilisateur, Evenement evenement) {
		Evenement e = this.find(evenement.getId());
        if(e==null)return null;             
               
        Utilisateur u = utilisateurDAO.find(utilisateur.getId());
        if(u==null)return null;   
        
        //vérification sur les doublons
        List<Utilisateur> utilisateurs = e.getPresentateurs();
        if(utilisateurs.contains(utilisateur)) return null;

        evenement.ajouterPresentateur(utilisateur);
        entityManager.merge(evenement);
        return u;
	}

	@Override
	public boolean delete(Evenement aSupprimer) {
		if(aSupprimer == null) return false;
		
		entityManager.remove(aSupprimer);
		return true;
	}

	@Override
	public Evenement find(Integer id) {

		if(id == null) return null;
		
		Evenement evenement = entityManager.find(Evenement.class, id);

		if(evenement != null) entityManager.detach(evenement);

		return evenement;
	}

	@Override
	public List<Evenement> findAll() {
		String sFind = "SELECT v FROM Evenement v ORDER BY v.date ASC";
		TypedQuery<Evenement> qFind = entityManager.createQuery(sFind, Evenement.class);
		List<Evenement> listEvenements = qFind.getResultList();
		
		return listEvenements;
	}

	@Override
	public Evenement update(Evenement oldElement, Evenement newElement) {
		if(oldElement == null || oldElement.getId() == null|| newElement == null) return null;
		
		Evenement found = this.find(oldElement.getId());
		
		if(found == null) return null;
		
		found.setAdresse(newElement.getAdresse());
		found.setDate(newElement.getDate());
		found.setDescription(newElement.getDescription());
		found.setNom(newElement.getNom());
		found.setPresentiel(newElement.isPresentiel());
		
		entityManager.merge(found);
		return found;
	}
	
	public Evenement updatePicture(Evenement oldElement, byte[] fileContents) {
		if (oldElement == null || oldElement.getId() == null || fileContents == null)
			return null;

		Evenement found = this.find(oldElement.getId());

		if (found == null)
			return null;
		
		found.setPicture(fileContents);

		entityManager.merge(found);
		return found;
	}
}
