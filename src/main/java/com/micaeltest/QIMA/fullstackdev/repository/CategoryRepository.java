package com.micaeltest.QIMA.fullstackdev.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.micaeltest.QIMA.fullstackdev.model.Category;



public interface CategoryRepository extends JpaRepository<Category, Integer>, JpaSpecificationExecutor<Category> {
	Optional<Category> findByName(String name);

	List<Category> findByParentCategoryIsNull();

	@Query("SELECT c FROM Category c LEFT JOIN FETCH c.parentCategory WHERE c.id = :id")
	Optional<Category> findByIdWithParent(int id);

	@Query("SELECT c FROM Category c LEFT JOIN FETCH c.parentCategory")
	List<Category> findAllWithParent();
}