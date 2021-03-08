package com.example.final_project.web;

import com.example.final_project.model.User;
import com.example.final_project.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	private UserService userService;

	public MainController(UserService userService, BCryptPasswordEncoder passwordEncoder) {
		super();
		this.userService = userService;
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/")
	public String home(Model model, @AuthenticationPrincipal UserDetails currentUser) {
		User curUser = userService.findByUsername(currentUser.getUsername());
		if (userService.isAdmin(curUser))
			model.addAttribute("isAdmin", true);
		else
			model.addAttribute("isAdmin", false);
		return "index";
	}
}
