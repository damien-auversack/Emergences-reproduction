package be.helha.aemt.control;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;

import be.helha.aemt.ejb.ActiviteEJB;
import be.helha.aemt.ejb.CycleEJB;
import be.helha.aemt.ejb.EvenementEJB;
import be.helha.aemt.entities.Activite;
import be.helha.aemt.entities.Cycle;
import be.helha.aemt.entities.Evenement;

@Named
@ViewScoped
public class AgendaControl implements Serializable {
	private static final long serialVersionUID = -3588396086020511064L;

	@Inject
	private ActiviteEJB activiteEJB;
	
	@Inject
	private CycleEJB cycleEJB;
	
	@Inject
	private EvenementEJB evenementEJB;
	
	private ScheduleModel eventModel; // Contient les infos du calendrier
	
	// permet de stocker toutes les dates et noms des événements indépendaments de leur types
	// (Activite, Cycle ou Evenement)
	private Map<String, LocalDateTime> evenements;

	@PostConstruct // permet d'appeler cette méthode au chargement de la page
    	public void init() {
		this.evenements = new HashMap<String, LocalDateTime>(); // Création de la map
        	eventModel = new DefaultScheduleModel(); // Création de l'event model
        
        	// récupérer les events depuis la base de données
        	this.ajouterDatesActivites();
        	this.ajouterDatesCycles();
        	this.ajouterDatesEvenements();
        
        	this.ajouterAgenda(); // ajouter les events à l'agenda
    	}
    
    	private void ajouterDatesActivites() {
		List<Activite> activites = this.activiteEJB.findAll();

		for(Activite activite : activites) { // parcourir toutes les activités et ajouter le nom et la date
			// il faut ajouter un LocalTime car le format pour les events est LocalDateTime
			this.evenements.put(activite.getNom(), LocalDateTime.of(activite.getDate(), LocalTime.of(0, 0)));
		}
    	}
    
    	private void ajouterDatesCycles() {
    		List<Cycle> cycles = this.cycleEJB.findAll();
    	
    		for(Cycle cycle : cycles) {// parcourir tout les cycles et ajouter le nom
    			List<LocalDate> dates = cycle.getDates(); // récupérer toutes les dates du cycle
    			for(LocalDate date : dates) { // il faut ajouter un LocalTime car le format pour les events est LocalDateTime
    				this.evenements.put(cycle.getNom(), LocalDateTime.of(date, LocalTime.of(0, 0))); // ajouter les dates une par une
    			}
    		}
    	}
    
   	private void ajouterDatesEvenements() {
    		List<Evenement> evenements = this.evenementEJB.findAll();
    	
    		for(Evenement evenement : evenements) {// parcourir tout les évenements et ajouter le nom et la date
    			// il faut ajouter un LocalTime car le format pour les events est LocalDateTime
    			this.evenements.put(evenement.getNom(), LocalDateTime.of(evenement.getDate(), LocalTime.of(0, 0)));
    		}
    	}
    
    	private void ajouterAgenda() {
    		// on parcours la liste des events et on les ajoute au calendrier
    		Set<String> cles = this.evenements.keySet();
    		Iterator<String> iterator = cles.iterator();
    	
    		while(iterator.hasNext()) {
    			String key = iterator.next();
    			DefaultScheduleEvent<?> event = DefaultScheduleEvent.builder()
	            		.title(key)
	            		.startDate(this.evenements.get(key)) // on met la date en tant que début ET fin
	            		.endDate(this.evenements.get(key))
	            		.allDay(true) // MAIS on précise que l'événement dure toute la journée
	            		.build();
	       		eventModel.addEvent(event);
    		}
    	}
    
    public ScheduleModel getEventModel() {
        return eventModel;
    }
    
    public Map<String, LocalDateTime> getEvenements() {
		return evenements;
	}

	public void setEvenements(Map<String, LocalDateTime> evenements) {
		this.evenements = evenements;
	}
}
