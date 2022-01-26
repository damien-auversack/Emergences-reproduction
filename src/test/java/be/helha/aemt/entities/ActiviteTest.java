package be.helha.aemt.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import be.helha.aemt.test.utils.Explorateur;

public class ActiviteTest {
	List<Utilisateur> particpants;
	List<Utilisateur> presentateurs;
	Activite activite;
	
	@Before
	public void setUp() throws Exception {
		activite = new Activite();
		particpants = (List<Utilisateur>) Explorateur.getFieldSuperClasse(activite, "participants"); // les participants sont dans reunion reservee
		presentateurs = (List<Utilisateur>) Explorateur.getFieldSuperSuperClasse(activite, "presentateurs"); // les présentateurs sont dans reunion
	}

	@After
	public void tearDown() throws Exception {
		activite = null;
		particpants = null;
		presentateurs = null;
	}
	
	@Test
	public void testAjouterParticipant() {
		assertTrue(particpants.size() == 0); // on vérifie qu'il n'y a rien au départ
		
		activite.ajouterParticipant(new Utilisateur("Test", "Test", null, Role.ADMIN));
		
		assertEquals(particpants.size(), 1);
		
		// test avec un utilisateur différent
		activite.ajouterParticipant(new Utilisateur("Test", "Test 2", null, Role.ADMIN));
		
		assertEquals(particpants.size(), 2);
		
		// test de duplication
		boolean resultat = activite.ajouterParticipant(new Utilisateur("Test", "Test", null, Role.ADMIN));
		
		assertFalse(resultat);
		assertEquals(particpants.size(), 2);
	}

	@Test
	public void testDeleteParticipant() {
		assertTrue(particpants.size() == 0); // on vérifie qu'il n'y a rien au départ
		
		// vérifier que l'ajout est correct
		activite.ajouterParticipant(new Utilisateur("Test", "Test", null, Role.ADMIN));
		
		assertEquals(particpants.size(), 1);
		
		// on ne peut pas supprimer un utilisateur qui n'est pas dans la liste
		assertFalse(activite.deleteParticipant(new Utilisateur("Test", "Test2", null, Role.USER)));
		
		activite.deleteParticipant(new Utilisateur("Test", "Test", null, Role.ADMIN));
		
		assertEquals(particpants.size(), 0);
		
		// on ne peut pas supprimer un utilisateur lorsque la liste est vide
		assertFalse(activite.deleteParticipant(new Utilisateur("Test", "Test", null, Role.ADMIN)));
	}

	@Test
	public void testAjouterPresentateur() {
		assertTrue(presentateurs.size() == 0); // on vérifie qu'il n'y a rien au départ

		activite.ajouterPresentateur(new Utilisateur("Test", "Test", null, Role.ADMIN));
		
		assertEquals(presentateurs.size(), 1);
		
		// test avec un utilisateur différent
		activite.ajouterPresentateur(new Utilisateur("Test", "Test 2", null, Role.ADMIN));
		
		assertEquals(presentateurs.size(), 2);
		
		// test de duplication
		boolean resultat = activite.ajouterPresentateur(new Utilisateur("Test", "Test", null, Role.ADMIN));
		
		assertFalse(resultat);
		assertEquals(presentateurs.size(), 2);
	}
}
