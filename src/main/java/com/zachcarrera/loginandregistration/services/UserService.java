package com.zachcarrera.loginandregistration.services;

import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.zachcarrera.loginandregistration.models.LoginUser;
import com.zachcarrera.loginandregistration.models.User;
import com.zachcarrera.loginandregistration.repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public User register(User newUser, BindingResult result) {
		// TO-DO - Reject values:
		// Reject if email is taken (present in database)
		// 1. Find user in the DB by email
		Optional<User> optionalUser = userRepository.findByEmail(newUser.getEmail());
		// 2. if the email is present , reject
		if (optionalUser.isPresent()) {
			result.rejectValue("email", "unique", "This email is already registered");
		}
		// Reject if password doesn't match confirmation
		if (!newUser.getPassword().equals(newUser.getConfirm())) {
			result.rejectValue("confirm", "matches", "This confirm password does not match");
		}
		// if result has errors, return
		if (result.hasErrors()) {
			return null;
		}
		// Hash and set password, save user to database

		String hashed = BCrypt.hashpw(newUser.getPassword(), BCrypt.gensalt());
		newUser.setPassword(hashed);
		return userRepository.save(newUser);
	}

	public User login(LoginUser newLogin, BindingResult result) {
		// Find user in the DB by email
		// 1. Find user in the DB by email
		Optional<User> optionalUser = userRepository.findByEmail(newLogin.getEmail());
		// 2. if the email is not present , reject
		if (!optionalUser.isPresent()) {
			result.rejectValue("email", "unique", "This email is not registered");
			return null;
		}
		// 3.1 grab the user from potential user
		User user = optionalUser.get();
		// 3.2 if BCrypt password match fails
		if (!BCrypt.checkpw(newLogin.getPassword(), user.getPassword())) {
			result.rejectValue("password", "matches", "The password does not match for this email");

		}
		// 4 if result has errors,return

		if (result.hasErrors()) {
			return null;
		}
		// Otherwise, return the user object
		return user;
	}

}
