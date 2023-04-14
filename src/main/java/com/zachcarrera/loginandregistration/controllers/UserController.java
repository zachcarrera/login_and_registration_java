package com.zachcarrera.loginandregistration.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.zachcarrera.loginandregistration.models.LoginUser;
import com.zachcarrera.loginandregistration.models.User;
import com.zachcarrera.loginandregistration.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;

	@GetMapping("/")
	public String loginPage(
			@ModelAttribute("newUser") User newUser,
			@ModelAttribute("newLogin") LoginUser newLogin
			) {
		return "logReg.jsp";
	}
	
	@PostMapping("/register")
	public String processRegister(
			@Valid @ModelAttribute("newUser") User newUser,
			BindingResult result,
			Model model,
			HttpSession session
			) {
		User registeredUser = userService.register(newUser, result);
		if (result.hasErrors()) {
			model.addAttribute("newLogin", new LoginUser());
			return "logReg.jsp";
		}
		
		session.setAttribute("userId", registeredUser.getId());
		session.setAttribute("userName", registeredUser.getUserName());

		return "redirect:/welcome";
	}
	
	
	@PostMapping("/login")
	public String processLogin(
			@Valid @ModelAttribute("newLogin") LoginUser newLogin,
			BindingResult result,
			Model model,
			HttpSession session
			) {
		User loggedInUser = userService.login(newLogin, result);
		if (result.hasErrors()) {
			model.addAttribute("newUser", new User());
			return "logReg.jsp";
		}
		session.setAttribute("userId", loggedInUser.getId());
		session.setAttribute("userName", loggedInUser.getUserName());
		
		return "redirect:/welcome";
	}
	
	@GetMapping("/logout")
	public String logoutUser(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
	
	
	
	@GetMapping("/welcome")
	public String welcomePage(HttpSession session) {
		
		if (session.getAttribute("userId") == null) {
			return "redirect:/";
		}
		return "welcome.jsp";
	}

}
