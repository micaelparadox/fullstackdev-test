package com.micaeltest.QIMA.fullstackdev.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.micaeltest.QIMA.fullstackdev.model.CustomUserDetail;
import com.micaeltest.QIMA.fullstackdev.model.User;
import com.micaeltest.QIMA.fullstackdev.repository.UserRepository;

@Service
public class CustomUserDetailService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public CustomUserDetail loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findUserByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
		return new CustomUserDetail(user);
	}
}