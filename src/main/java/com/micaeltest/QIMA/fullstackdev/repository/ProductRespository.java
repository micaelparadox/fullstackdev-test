package com.micaeltest.QIMA.fullstackdev.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.micaeltest.QIMA.fullstackdev.model.Product;


public interface ProductRespository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
	@Query("SELECT p FROM Product p JOIN FETCH p.category WHERE p.category.id = :id")
	List<Product> findAllByCategory_Id(int id);
}
