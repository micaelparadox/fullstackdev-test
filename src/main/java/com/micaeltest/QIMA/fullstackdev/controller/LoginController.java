package com.micaeltest.QIMA.fullstackdev.controller;

import java.util.Collections;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.micaeltest.QIMA.fullstackdev.global.GlobalData;
import com.micaeltest.QIMA.fullstackdev.model.Role;
import com.micaeltest.QIMA.fullstackdev.model.User;
import com.micaeltest.QIMA.fullstackdev.repository.RoleRepository;
import com.micaeltest.QIMA.fullstackdev.repository.UserRepository;

@Controller
//@RequestMapping("/auth")
public class LoginController {
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;

	@GetMapping("/login")
	public String login() {
		GlobalData.cart.clear();
		return "login";
	}

	@GetMapping("/register")
	public String registerGet() {
		return "register";
	}

	@PostMapping("/register")
	public String registerUser(@ModelAttribute User user, HttpServletRequest request) {
		String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);

		// Secure role retrieval with proper exception handling
		Role userRole = roleRepository.findById(2)
				.orElseThrow(() -> new IllegalStateException("Error: Role not found."));
		user.setRoles(Collections.singletonList(userRole));

		userRepository.save(user);

		// Automatic login after registration
		try {
			request.login(user.getEmail(), user.getPassword());
		} catch (ServletException e) {
			System.err.println("Error during automatic login: " + e.getMessage());
			return "redirect:/auth/login?error";
		}

		return "redirect:/";
	}
}