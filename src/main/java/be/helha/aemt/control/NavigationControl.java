package be.helha.aemt.control;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Named
@RequestScoped
public class NavigationControl {
	private static Logger log = Logger.getLogger(UtilisateurControl.class.getName());

	public String doAjouterPublication() {
		return "/admin/ajoutPublications.xhtml?faces-redirect=true";
	}

	public String doAfficherPublications() {
		return "/user/listePublications?faces-redirect=true";
	}

	/* LogIn / Inscription */
	public String doLogin() {
		return "/visitor/login.xhtml?faces-redirect=true";
	}

	public String doSignUp() {
		return "/visitor/signup.xhtml?faces-redirect=true";
	}

	/* Visitor */
	public String doIndex() {
		return "/index.xhtml?faces-redirect=true";
	}

	public String doPleineConscience() {
		return "/visitor/pleineConscience.xhtml?faces-redirect=true";
	}

	public String doCycle() {
		return "/visitor/cycle.xhtml?faces-redirect=true";
	}

	public String doEvenement() {
		return "/visitor/evenement.xhtml?faces-redirect=true";
	}

	public String doActivite() {
		return "/visitor/activite.xhtml?faces-redirect=true";
	}

	public String doAgenda() {
		return "/visitor/agenda.xhtml?faces-redirect=true";
	}

	public String doQuiSommesNous() {
		return "/visitor/quiSommesNous.xhtml?faces-redirect=true";
	}

	/* User */
	public String doUser() {
		return "/user/index.xhtml?faces-redirect=true";
	}

	public String doPleineConscienceUser() {
		return "/user/pleineConscience.xhtml?faces-redirect=true";
	}

	public String doCycleUser() {
		return "/user/cycle.xhtml?faces-redirect=true";
	}

	public String doEvenementUser() {
		return "/user/evenement.xhtml?faces-redirect=true";
	}

	public String doActiviteUser() {
		return "/user/activites.xhtml?faces-redirect=true";
	}

	public String doMesActivites() {
		return "/user/mesActivites.xhtml?faces-redirect=true";
	}

	public String doMesCycles() {
		return "/user/mesCycles.xhtml?faces-redirect=true";
	}

	public String doAdministration() {
		return "/user/administration.xhtml?faces-redirect=true";
	}

	/* Admin */
	public String doAdmin() {
		return "/admin/index.xhtml?faces-redirect=true";
	}

	public String doGestionUtilisateur() {
		return "/admin/gestionUtilisateurs.xhtml?faces-redirect=true";
	}

	public String doAjoutActivite() {
		return "/admin/ajoutActivite.xhtml?faces-redirect=true";
	}

	public String doAjoutCycle() {
		return "/admin/ajoutCycle.xhtml?faces-redirect=true";
	}

	public String doAjoutDatesCycle(int id) {
		return "/admin/ajoutDatesCycle.xhtml?id=" + id + "&faces-redirect=true";
	}

	public String doAjoutEvenement() {
		return "/admin/ajoutEvenement.xhtml?faces-redirect=true";
	}

	public String doAjoutPresentateurActivite(int id) {
		return "/admin/ajoutPresentateurActivite.xhtml?id=" + id + "&faces-redirect=true";
	}
	public String doAjoutPresentateurEvenement(int id) {
		return "/admin/ajoutPresentateurEvenement.xhtml?id=" + id + "&faces-redirect=true";
	}
	
	public String doAjoutPresentateurCycle(int id) {
		return "/admin/ajoutPresentateurCycle.xhtml?id=" + id + "&faces-redirect=true";
	}
	
	public String doModifierActivite(int id) {
		return "/admin/modifierActivite.xhtml?id=" + id + "&faces-redirect=true";
	}

	public String doModifierEvenement(int id) {
		return "/admin/modifierEvenement.xhtml?id=" + id + "&faces-redirect=true";
	}
	
	public String doModifierCycle(int id) {
		return "/admin/modifierCycle.xhtml?id=" + id + "&faces-redirect=true";
	}
	
	public String doGestionActivite() {
		return "/admin/gestionActivites.xhtml?faces-redirect=true";
	}

	public String doDetailsReunionUtilisateur(int id) {
		return "/admin/detailsReunionsUtilisateur.xhtml?id=" + id + "&faces-redirect=true";
	}

	public String doModificationActivite() {
		return "/admin/activitesPourModification.xhtml?faces-redirect=true";
	}
	
	public String doModificationEvenement() {
		return "/admin/evenementsPourModification.xhtml?faces-redirect=true";
	}
	
	public String doModificationCycle() {
		return "/admin/cyclesPourModification.xhtml?faces-redirect=true";
	}

	public String doGestionParticipantsActivite(int id) {
		return "/admin/listeUtilisateurParActivite.xhtml?id=" + id + "&faces-redirect=true";
	}

	public String doGestionParticipantsCycle(int id) {
		return "/admin/listeUtilisateurParCycle.xhtml?id=" + id + "&faces-redirect=true";
	}
	public int getDetailsReunionUtilisateurId() {
		String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
		int intId = Integer.valueOf(id.substring(0, id.length() - 1)); // on retire le caractère "?" à la fin de l'id
																		// qui se trouve dans l'url
		return intId;
	}

	/* LogOut */
	public String logout() {
		String result = "/user/index?faces-redirect=true";

		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

		try {
			request.logout();
		} catch (ServletException e) {
			log.log(Level.SEVERE, "Failed to logout user!", e);
			result = "/visitor/loginError?faces-redirect=true";
		}

		return result;
	}
}
