package be.helha.aemt.ejb;

import java.time.LocalDate;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import be.helha.aemt.dao.CycleDAO;
import be.helha.aemt.entities.Cycle;
import be.helha.aemt.entities.Evenement;
import be.helha.aemt.entities.Utilisateur;

@Stateless
@LocalBean
public class CycleEJB {
	@EJB
	private CycleDAO cycleDAO;
	
	public Cycle add(Cycle cycle) {
		return this.cycleDAO.add(cycle);
	}
	
	public boolean delete(Cycle cycle) {
		return this.cycleDAO.delete(cycle);
	}
	
	public Cycle find(int id) {
		return this.cycleDAO.find(id);
	}
	
	public List<Cycle> findAll() {
		return this.cycleDAO.findAll();
	}
	
	public Cycle update(Cycle oldCycle, Cycle newCycle) {
		return this.cycleDAO.update(oldCycle, newCycle);
	}
	
	public void addParticipant(Utilisateur utilisateur, Cycle cycle) {
		this.cycleDAO.addParticipant(utilisateur, cycle);
	}	
	
	public void deleteParticipant(Utilisateur utilisateur, Cycle cycle) {
		this.cycleDAO.deleteParticipant(utilisateur, cycle);		
	}	
	
	public void addDate(LocalDate date, Cycle cycle) {
		this.cycleDAO.addDate(date, cycle);
	}
	
	public void addPresentateur(Utilisateur utilisateur, Cycle cycle) {
		this.cycleDAO.addPresentateur(utilisateur, cycle);
	}
	
	public List<Cycle> findCycleByUserID(int id) {
		return this.cycleDAO.findByIdUser(id);
	}
	
	public List<LocalDate>findCycleDatesByID(int id){
		return this.cycleDAO.findCycleDatesByID(id);
	}
	
	public boolean isFull(Cycle a) {
		return this.cycleDAO.isFull(a);
	}
	
	public Cycle updatePicture(Cycle oldActivite, byte[] fileContents) {
		return this.cycleDAO.updatePicture(oldActivite, fileContents);
    }
    public List<Utilisateur> getAllUtilisateurs(int id){
		return this.cycleDAO.findAllUtilisateurs(id);
	}
}
