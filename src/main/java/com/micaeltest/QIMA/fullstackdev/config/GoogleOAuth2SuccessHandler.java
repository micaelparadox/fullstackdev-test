package com.micaeltest.QIMA.fullstackdev.config;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.micaeltest.QIMA.fullstackdev.model.Role;
import com.micaeltest.QIMA.fullstackdev.model.User;
import com.micaeltest.QIMA.fullstackdev.repository.RoleRepository;
import com.micaeltest.QIMA.fullstackdev.repository.UserRepository;

@Component
public class GoogleOAuth2SuccessHandler implements AuthenticationSuccessHandler {
	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
		String email = token.getPrincipal().getAttributes().get("email").toString();

		User user = userRepository.findUserByEmail(email).orElseGet(() -> createUser(token));

		assignAdminRole(user);

		userRepository.save(user);
		redirectStrategy.sendRedirect(request, response, "/");
	}

	private User createUser(OAuth2AuthenticationToken token) {
		User newUser = new User();
		newUser.setFirstName(token.getPrincipal().getAttributes().get("given_name").toString());
		newUser.setLastName(token.getPrincipal().getAttributes().get("family_name").toString());
		newUser.setEmail(token.getPrincipal().getAttributes().get("email").toString());
		return newUser;
	}

	private void assignAdminRole(User user) throws ServletException {

		Role adminRole = roleRepository.findByName("ROLE_ADMIN")
				.orElseThrow(() -> new ServletException("Admin role not found."));
		user.setRoles(Collections.singletonList(adminRole));
	}
}