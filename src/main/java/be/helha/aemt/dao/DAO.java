package be.helha.aemt.dao;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@LocalBean
public class DAO<T> {
	@PersistenceContext(unitName = "dba1")
	protected EntityManager entityManager;
	
	public DAO() {

	}

	public T add(T aAjouter) {
		return null;
	}

	public boolean delete(T aSupprimer) {
		return false;
	}

	public T find(Integer id) {
		return null;
	}

	public List<T> findAll() {
		return null;
	}

	public T update(T oldElement, T newElement) {
		return null;
	}
}
