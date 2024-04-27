package com.micaeltest.QIMA.fullstackdev.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.micaeltest.QIMA.fullstackdev.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findUserByEmail(String email);
}