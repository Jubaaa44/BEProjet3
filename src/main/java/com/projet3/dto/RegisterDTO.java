package com.projet3.dto;

public class RegisterDTO extends LoginDTO {
	// On ajoute l'attribut "name" en plus.
	private String name;
	
	// Constructeur
	public RegisterDTO(String name) {
		super();
		this.setName(name);
	}

	// Getter et Setter
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
