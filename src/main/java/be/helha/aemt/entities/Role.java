package be.helha.aemt.entities;

//liste des roles disponibles sur le site
public enum Role {
	ADMIN("admin"),
	USER("user");
	
	private String type;

	Role(String string) {
		this.type = string;
	}
	
	String getType() {
		return this.type;
	}
}
