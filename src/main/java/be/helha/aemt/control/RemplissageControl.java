package be.helha.aemt.control;

import java.time.LocalDate;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.NamingException;

import be.helha.aemt.ejb.ActiviteEJB;
import be.helha.aemt.ejb.CycleEJB;
import be.helha.aemt.ejb.EvenementEJB;
import be.helha.aemt.ejb.UtilisateurEJB;
import be.helha.aemt.entities.Activite;
import be.helha.aemt.entities.Adresse;
import be.helha.aemt.entities.Cycle;
import be.helha.aemt.entities.Evenement;
import be.helha.aemt.entities.Role;
import be.helha.aemt.entities.Utilisateur;

@Named
@RequestScoped
public class RemplissageControl {
	@Inject
	private ActiviteEJB activiteEJB;
	
	@Inject
	private CycleEJB cycleEJB;
	
	@Inject
	private EvenementEJB evenementEJB;
	
	@Inject
	private UtilisateurEJB utilisateurEJB;
	
	public  void remplir() throws NamingException {
		// Utilisateurs
		Utilisateur admin = new Utilisateur("d+Rn6wFp6C538JDfIXoyM1fGoVepjAN15vbbr+ApyDo=", "admin", null, Role.ADMIN);
		Utilisateur user1 = new Utilisateur("d+Rn6wFp6C538JDfIXoyM1fGoVepjAN15vbbr+ApyDo=", "user", null, Role.USER);
		Utilisateur user2 = new Utilisateur("d+Rn6wFp6C538JDfIXoyM1fGoVepjAN15vbbr+ApyDo=", "Martin", null, Role.USER);
		Utilisateur user3 = new Utilisateur("d+Rn6wFp6C538JDfIXoyM1fGoVepjAN15vbbr+ApyDo=", "Damien", null, Role.USER);
		Utilisateur user4 = new Utilisateur("d+Rn6wFp6C538JDfIXoyM1fGoVepjAN15vbbr+ApyDo=", "Florian", null, Role.USER);
		
		utilisateurEJB.add(admin);
		utilisateurEJB.add(user1);
		utilisateurEJB.add(user2);
		utilisateurEJB.add(user3);
		utilisateurEJB.add(user4);
		
		// Adresses
		Adresse adresse = new Adresse("Mons", "7000", "Rue des oiseaux", "19");
		Adresse adresse2 = new Adresse("Mons", "7000", "Rue des piétons", "42");
		Adresse adresse3 = new Adresse("La Louvière", "7100", "Rue des perdus", "51");
		Adresse adresse4 = new Adresse("Charleroi", "6000", "Rue des endormis", "10");
		Adresse adresse5 = new Adresse("Bruxelles", "1000", "Rue de l'évènement", "15");

		// Activites
		Activite activite = new Activite("Relaxation", "Relaxation dans la nature", adresse, true, null, 20, 8, LocalDate.now());
		activiteEJB.add(activite);
		activiteEJB.addPresentateur(admin, activite);
		activiteEJB.addParticipant(admin, activite);
		activiteEJB.addParticipant(user1, activite);
		activiteEJB.addParticipant(user2, activite);
		
		Activite activite2 = new Activite("Enervement", "Enervement dans la nature", adresse2, true, null, 20, 8, LocalDate.now());
		activiteEJB.add(activite2);
		activiteEJB.addParticipant(user3, activite2);
		activiteEJB.addParticipant(user4, activite2);
		
		Activite activite3 = new Activite("Bricolage", "On bricole, on bricole, on voit pas le fond du bol", adresse3, false, null, 10, 20, LocalDate.now());
		activiteEJB.add(activite3);
		activiteEJB.addParticipant(admin, activite3);
		activiteEJB.addParticipant(user1, activite3);
		activiteEJB.addParticipant(user2, activite3);
		activiteEJB.addParticipant(user3, activite3);
		activiteEJB.addParticipant(user4, activite3);
		
		// Cycles
		Cycle cycle = new Cycle("Le premier cycle de sommeil", "On dort tous ensemble pour se reposer de manière optimale", adresse4, true, null, 50, 5);
		cycle.getDates().add(LocalDate.of(2022, 01, 25));
		cycle.getDates().add(LocalDate.of(2022, 02, 25));
		cycle.getDates().add(LocalDate.of(2022, 03, 25));
		
		cycleEJB.addParticipant(user4, cycle);
		cycleEJB.addParticipant(user2, cycle);
		cycleEJB.add(cycle);
		
		Cycle cycle2 = new Cycle("Apprenez à utiliser GitHub", "Cours de github, pour merge à gogo", adresse2, false, null, 50, 5);	
		cycle2.getDates().add(LocalDate.of(2022, 01, 18));
		cycle2.getDates().add(LocalDate.of(2022, 01, 25));
		cycle2.getDates().add(LocalDate.of(2022, 01, 29));
		
		cycleEJB.addParticipant(admin, cycle2);
		cycleEJB.addParticipant(user2, cycle2);
		cycleEJB.addParticipant(user3, cycle2);
		
		cycleEJB.add(cycle2);
		
		// Evenements
		Evenement evenement = new Evenement("Evenement du siècle", "L'évenement que tout le monde attend on va faire plein de trucs", adresse5, true, null, LocalDate.of(2064, 02, 15));
		Evenement evenement2 = new Evenement("Evenement de la décennie", "L'évenement que tout le monde attend on va faire plein de trucs, mais pas trop", adresse3, false, null, LocalDate.of(2022, 01, 20));
		Evenement evenement3 = new Evenement("La fin du covid-19", "Le retour à la normale, enfin !", adresse2, false, null, LocalDate.of(2022, 01, 20));
	
		evenementEJB.add(evenement);
		evenementEJB.add(evenement2);
		evenementEJB.add(evenement3);
	}
}
