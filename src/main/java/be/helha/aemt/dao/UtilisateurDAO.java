package be.helha.aemt.dao;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import be.helha.aemt.entities.Role;
import be.helha.aemt.entities.Utilisateur;

@Stateless
@LocalBean
public class UtilisateurDAO extends DAO<Utilisateur> {
	
	public UtilisateurDAO() {
		
	}
	
	@Override
	public Utilisateur add(Utilisateur aAjouter) {
		if(aAjouter == null) return null;
		
		Utilisateur utilisateur = this.find(aAjouter.getId());
		
		if(utilisateur != null) return null;
        //vérification sur les doublons
		Utilisateur utilisateur2 = this.findByLogin(aAjouter.getLogin());
		if(utilisateur2 != null) return null;
			
		entityManager.persist(aAjouter);
		return aAjouter;
	}

	@Override
	public boolean delete(Utilisateur aSupprimer) {
		if(aSupprimer == null) return false;
		
		entityManager.remove(aSupprimer);
		return true;
	}

	@Override
	public Utilisateur find(Integer id) {

		if(id == null) return null;
		
		Utilisateur utilisateur = entityManager.find(Utilisateur.class, id);

		if(utilisateur != null) entityManager.detach(utilisateur);

		return utilisateur;
	}
	
	@SuppressWarnings("unchecked")
	public Utilisateur findByLogin(String login) {
		if(login == null)return null;
		String sFind = "SELECT u FROM Utilisateur u WHERE u.login=:login";
		Query qFind = entityManager.createQuery(sFind);
		
		qFind.setParameter("login", login);
		List<Utilisateur> results = qFind.getResultList();
		
		return results.isEmpty() ? null:results.get(0);
	}

	@Override
	public List<Utilisateur> findAll() {
		String sFind = "SELECT v FROM Utilisateur v ORDER BY v.login ASC";
		TypedQuery<Utilisateur> qFind = entityManager.createQuery(sFind, Utilisateur.class);
		List<Utilisateur> listPublications = qFind.getResultList();
		
		return listPublications;
	}

	@Override
	public Utilisateur update(Utilisateur oldElement, Utilisateur newElement) {
		if(oldElement == null || oldElement.getId() == null|| newElement == null) return null;
		
		Utilisateur found = this.find(oldElement.getId());
		
		if(found == null) return null;
		
		entityManager.merge(found);
		return found;
	}
	
	public Utilisateur updatePicture(Utilisateur oldElement, byte[] fileContents) {
		if(oldElement == null || oldElement.getId() == null|| fileContents == null) return null;
		
		Utilisateur found = this.find(oldElement.getId());
		
		if(found == null) return null;
		
		found.setPicture(fileContents);
		
		entityManager.merge(found);
		return found;
	}
	
	public Role changeRole(Utilisateur u) {
		if(u==null||this.find(u.getId())==null)return null;
			Role role;
		//change le role en fonction du role actuellement attribué
		if(u.getRole()==Role.ADMIN) {
			role = Role.USER;
			u.setRole(Role.USER);
		}
		else {
			role = Role.ADMIN;
			u.setRole(Role.ADMIN);
		}
		
		entityManager.merge(u);
		
		return role;
	}
}
