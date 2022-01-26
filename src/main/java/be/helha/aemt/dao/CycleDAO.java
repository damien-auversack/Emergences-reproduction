package be.helha.aemt.dao;

import java.time.LocalDate;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import be.helha.aemt.entities.Activite;
import be.helha.aemt.entities.Cycle;
import be.helha.aemt.entities.Evenement;
import be.helha.aemt.entities.Utilisateur;

@Stateless
@LocalBean
public class CycleDAO extends DAO<Cycle> {	
	@EJB 
	private UtilisateurDAO utilisateurDAO;
	
	public CycleDAO() {
		
	}
	
	public Utilisateur addParticipant(Utilisateur utilisateur, Cycle cycle) {
		Cycle c = this.find(cycle.getId());
        if(c==null)return null;             
               
        Utilisateur u = utilisateurDAO.find(utilisateur.getId());
        if(u==null)return null;   
        //vérification sur les doublons
        List<Utilisateur> utilisateurs = c.getParticipants();
        if(utilisateurs.contains(utilisateur)) return null;

        cycle.ajouterParticipant(utilisateur);
        entityManager.merge(cycle);
        return u;
	}
	
	public Utilisateur deleteParticipant(Utilisateur utilisateur, Cycle cycle) {	
		Cycle c = this.find(cycle.getId());
        if(c==null)return null;             
               
        Utilisateur u = utilisateurDAO.find(utilisateur.getId());
        if(u==null)return null;             

        cycle.deleteParticipant(utilisateur);
        entityManager.merge(cycle);
        return u;
	}
	
	public Utilisateur addPresentateur(Utilisateur utilisateur, Cycle cycle) {
		Cycle c = this.find(cycle.getId());
        if(c==null)return null;             
               
        Utilisateur u = utilisateurDAO.find(utilisateur.getId());
        if(u==null)return null;   
        //vérification sur les doublons
        List<Utilisateur> utilisateurs = c.getPresentateurs();
        if(utilisateurs.contains(utilisateur)) return null;

        cycle.ajouterPresentateur(utilisateur);
        entityManager.merge(cycle);
        return u;
	}
	
	@Override
	public Cycle add(Cycle aAjouter) {
		if(aAjouter == null) return null;
		
		Cycle cycle = this.find(aAjouter.getId());
		if(cycle != null) return null;
        //vérification sur les doublons
		List<Cycle> cycles = this.findAll();
		for(Cycle cycle2 : cycles) {
			if(cycle2.equals(aAjouter)) return null;
		}
		
		entityManager.persist(aAjouter);
		return aAjouter;
	}
	
	public LocalDate addDate(LocalDate date, Cycle cycle) {
		Cycle c = this.find(cycle.getId());
        if(c==null)return null;                      

        cycle.ajouterDate(date);
        entityManager.merge(cycle);
        return date;
	}

	@Override
	public boolean delete(Cycle aSupprimer) {
		if(aSupprimer == null) return false;
		
		entityManager.remove(aSupprimer);
		return true;
	}

	@Override
	public Cycle find(Integer id) {
		if(id == null) return null;
		
		Cycle cycle = entityManager.find(Cycle.class, id);
		
		if(cycle != null) entityManager.detach(cycle);
		return cycle;
	}
	
	@Override
	public List<Cycle> findAll() {
		//tri des cycle en fonction de la date la plus petite de la liste de dates
		String sFind = "SELECT v FROM Cycle v LEFT JOIN v.dates ds GROUP BY v.id ORDER BY ds";
		TypedQuery<Cycle> qFind = entityManager.createQuery(sFind, Cycle.class);
		List<Cycle> listPublications = qFind.getResultList();
		
		return listPublications;
	}

	@Override
	public Cycle update(Cycle oldElement, Cycle newElement) {
		if(oldElement == null || oldElement.getId() == null|| newElement == null) return null;
		
		Cycle found = this.find(oldElement.getId());
		
		if(found == null) return null;
		
		found.setAdresse(newElement.getAdresse());
		found.setDescription(newElement.getDescription());
		found.setNom(newElement.getNom());
		found.setPlaces(newElement.getPlaces());
		found.setPrix(newElement.getPrix());
		found.setPresentiel(newElement.isPresentiel());
		
		entityManager.merge(found);
		return found;
	}
	
	public Cycle updatePicture(Cycle oldElement, byte[] fileContents) {
		if (oldElement == null || oldElement.getId() == null || fileContents == null)
			return null;

		Cycle found = this.find(oldElement.getId());

		if (found == null)
			return null;
		
		found.setPicture(fileContents);

		entityManager.merge(found);
		return found;
	}

	public List<Cycle> findByIdUser(int id) {
		if(id < 1)return null;
		String sFind = "SELECT c FROM Cycle c JOIN c.participants p WHERE p.id =:id";
		Query qFind = entityManager.createQuery(sFind);
		qFind.setParameter("id", id);
		List<Cycle> results = qFind.getResultList();
		return results;
	}
	
	public List<LocalDate>findCycleDatesByID(int id){
		if(id < 1)return null;
		String sFind = "SELECT c.dates FROM Cycle c WHERE c.id =:id";
		Query qFind = entityManager.createQuery(sFind);
		qFind.setParameter("id", id);
		List<LocalDate> results = qFind.getResultList();
		return results;
	}

	public boolean isFull(Cycle a) {
		if(a==null||a.getId()==null||find(a.getId())==null)return true;
		
		Cycle found = this.find(a.getId());
		if(found==null)return true;;
		int nbParticipants = (found.getParticipants()).size();
		int tailleMax = found.getPlaces();
		return tailleMax<=nbParticipants||tailleMax<=0;
	}
	
	public List<Utilisateur> findAllUtilisateurs(int id){
		if(id<=0)return null;
		
		Cycle a = find(id);
		if(a==null)return null;
		
		return a.getParticipants();
	}

}
