package be.helha.aemt.dao;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import be.helha.aemt.entities.Activite;
import be.helha.aemt.entities.Utilisateur;

@Stateless
@LocalBean
public class ActiviteDAO extends DAO<Activite> {
	@EJB
	private UtilisateurDAO utilisateurDAO;

	public ActiviteDAO() {

	}
	
	
	@Override
	public Activite add(Activite aAjouter) {
		if (aAjouter == null)
			return null;
		//vérification sur les doublons
		Activite activite = this.find(aAjouter.getId());
		if (activite != null)
			return null;

		List<Activite> activites = this.findAll();
		for (Activite activite2 : activites) {
			if (activite2.equals(aAjouter))
				return null;
		}

		entityManager.persist(aAjouter);
		return aAjouter;
	}

	public Utilisateur addParticipant(Utilisateur utilisateur, Activite activite) {
		Activite a = this.find(activite.getId());
		if (a == null)
			return null;

		Utilisateur u = utilisateurDAO.find(utilisateur.getId());
		if (u == null)
			return null;
		//vérification sur les doublons
		List<Utilisateur> utilisateurs = a.getParticipants();
		if (utilisateurs.contains(utilisateur))
			return null;

		activite.ajouterParticipant(utilisateur);
		entityManager.merge(activite);
		return u;
	}

	public Utilisateur deleteParticipant(Utilisateur utilisateur, Activite activite) {
		Activite a = this.find(activite.getId());
		if (a == null)
			return null;

		Utilisateur u = utilisateurDAO.find(utilisateur.getId());
		if (u == null)
			return null;

		activite.deleteParticipant(utilisateur);
		entityManager.merge(activite);
		return u;
    }

    public Utilisateur addPresentateur(Utilisateur utilisateur, Activite activite) {
		Activite a = this.find(activite.getId());
        if(a==null)return null;             
               
        Utilisateur u = utilisateurDAO.find(utilisateur.getId());
        if(u==null)return null;   
		//vérification sur les doublons
        List<Utilisateur> utilisateurs = a.getPresentateurs();
        if(utilisateurs.contains(utilisateur)) return null;

        activite.ajouterPresentateur(utilisateur);
        entityManager.merge(activite);
        return u;
    }

	@Override
	public boolean delete(Activite aSupprimer) {
		if (aSupprimer == null)
			return false;

		entityManager.remove(aSupprimer);
		return true;
	}

	@Override
	public Activite find(Integer id) {
		if (id == null)
			return null;

		Activite activite = entityManager.find(Activite.class, id);

		if (activite != null)
			entityManager.detach(activite);
		return activite;
	}

	@Override
	public List<Activite> findAll() {
		String sFind = "SELECT v FROM Activite v ORDER BY v.date ASC";
		TypedQuery<Activite> qFind = entityManager.createQuery(sFind, Activite.class);
		List<Activite> listPublications = qFind.getResultList();

		return listPublications;
	}

	@SuppressWarnings("unchecked")
	public List<Activite> findByIdUser(int id) {
		if (id < 1)
			return null;
		String sFind = "SELECT a FROM Activite a JOIN a.participants p WHERE p.id =:id ORDER BY a.date ASC";
		Query qFind = entityManager.createQuery(sFind);
		qFind.setParameter("id", id);
		List<Activite> results = qFind.getResultList();
		return results;
	}

	@Override
	public Activite update(Activite oldElement, Activite newElement) {
		if(oldElement == null || oldElement.getId() == null || newElement == null) {
			return null;
		}
		
		Activite found = this.find(oldElement.getId());

		if(found == null)
			return null;

		found.setAdresse(newElement.getAdresse());
		found.setDate(newElement.getDate());
		found.setDescription(newElement.getDescription());
		found.setNom(newElement.getNom());
		found.setPlaces(newElement.getPlaces());
		found.setPrix(newElement.getPrix());
		found.setPresentiel(newElement.isPresentiel());
		
		entityManager.merge(found);
		return found;
	}
	
	public Activite updatePicture(Activite oldElement, byte[] fileContents) {
		if (oldElement == null || oldElement.getId() == null || fileContents == null)
			return null;

		Activite found = this.find(oldElement.getId());

		if (found == null)
			return null;
		
		found.setPicture(fileContents);

		entityManager.merge(found);
		return found;
	}

	public boolean isFull(Activite a) {
		if(a==null||a.getId()==null||find(a.getId())==null)return true;
		
		Activite found = this.find(a.getId());
		if(found==null)return true;;
		int nbParticipants = (found.getParticipants()).size();
		int tailleMax = found.getPlaces();
		return tailleMax<=nbParticipants||tailleMax<=0;
	}
	
	
	public List<Utilisateur> findAllUtilisateurs(int id){
		if(id<=0)return null;
		
		Activite a = find(id);
		if(a==null)return null;
		
		return a.getParticipants();
	}
	
}
