package com.micaeltest.QIMA.fullstackdev.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.micaeltest.QIMA.fullstackdev.model.Role;



public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);
}