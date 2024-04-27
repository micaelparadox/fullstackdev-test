package com.micaeltest.QIMA.fullstackdev.service;

import org.springframework.data.jpa.domain.Specification;

import com.micaeltest.QIMA.fullstackdev.model.Category;



public class CategorySpecification {

	public static Specification<Category> hasName(String name) {
		return (category, cq, cb) -> name == null ? cb.conjunction() : cb.equal(category.get("name"), name);
	}
}