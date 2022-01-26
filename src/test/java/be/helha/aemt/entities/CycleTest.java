package be.helha.aemt.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import be.helha.aemt.test.utils.Explorateur;

public class CycleTest {
	List<LocalDate> dates;
	Cycle cycle;
	
	@Before
	public void setUp() throws Exception {
		cycle = new Cycle();
		dates = (List<LocalDate>) Explorateur.getField(cycle, "dates");
	}

	@After
	public void tearDown() throws Exception {
		dates = null;
		cycle = null;
	}

	@Test
	public void testAjouterDate() {
		assertEquals(dates.size(), 0);
		
		assertTrue(cycle.ajouterDate(LocalDate.of(2022, 1, 19)));
		
		assertEquals(dates.size(), 1);
		
		// test avec une date différente
		assertTrue(cycle.ajouterDate(LocalDate.of(2022, 1, 20)));
		
		assertEquals(dates.size(), 2);
		
		// test de duplication
		assertFalse(cycle.ajouterDate(LocalDate.of(2022, 1, 19)));
	}
}
