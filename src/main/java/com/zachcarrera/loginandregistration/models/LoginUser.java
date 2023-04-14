package com.zachcarrera.loginandregistration.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class LoginUser {

	@NotEmpty
	@Email
	private String email;

	// password
	@NotEmpty
	@Size(min = 8, max = 128)
	private String password;

	// constructor
	public LoginUser() {
	}

	// getters and setters
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
