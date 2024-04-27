package com.micaeltest.QIMA.fullstackdev.config;

import java.util.Collections;
import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.micaeltest.QIMA.fullstackdev.model.Role;
import com.micaeltest.QIMA.fullstackdev.model.User;
import com.micaeltest.QIMA.fullstackdev.repository.RoleRepository;
import com.micaeltest.QIMA.fullstackdev.repository.UserRepository;



@Configuration
public class DataInitializer {

	@Bean
	CommandLineRunner initDatabase(UserRepository userRepository, RoleRepository roleRepository,
			PasswordEncoder passwordEncoder) {
		return args -> {
			Role adminRole = roleRepository.findByName("ROLE_ADMIN")
					.orElseGet(() -> roleRepository.save(new Role("ROLE_ADMIN")));
			Role userRole = roleRepository.findByName("ROLE_USER")
					.orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));

			Optional<User> existingAdmin = userRepository.findUserByEmail("micaelparadox@gmail.com");
			if (!existingAdmin.isPresent()) {
				User admin = new User();
				admin.setFirstName("Micael");
				admin.setLastName("Santana");
				admin.setEmail("micaelparadox@gmail.com");
				admin.setPassword(passwordEncoder.encode("micaelparadox"));
				admin.setRoles(Collections.singletonList(adminRole));
				userRepository.save(admin);
			}

			Optional<User> existingUser = userRepository.findUserByEmail("user@example.com");
			if (!existingUser.isPresent()) {
				User user = new User();
				user.setFirstName("User");
				user.setLastName("Normal");
				user.setEmail("user@example.com");
				user.setPassword(passwordEncoder.encode("user123"));
				user.setRoles(Collections.singletonList(userRole));
				userRepository.save(user);
			}
		};
	}
}