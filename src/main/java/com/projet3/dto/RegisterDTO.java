package com.projet3.dto;

public class RegisterDTO extends LoginDTO {
	private String name;
	
	public RegisterDTO(String name) {
		super();
		this.setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
