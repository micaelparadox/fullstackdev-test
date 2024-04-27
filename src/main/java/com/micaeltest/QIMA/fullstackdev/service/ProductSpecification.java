package com.micaeltest.QIMA.fullstackdev.service;

import org.springframework.data.jpa.domain.Specification;

import com.micaeltest.QIMA.fullstackdev.model.Product;

public class ProductSpecification {

	public static Specification<Product> hasName(String name) {
		return (product, cq, cb) -> name == null ? cb.conjunction() : cb.equal(product.get("name"), name);
	}

	public static Specification<Product> hasCategoryId(Integer categoryId) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("category").get("id"), categoryId);
	}

	public static Specification<Product> priceGreaterThanEqual(double price) {
		return (product, cq, cb) -> cb.greaterThanOrEqualTo(product.get("price"), price);
	}

	public static Specification<Product> priceLessThanEqual(double price) {
		return (product, cq, cb) -> cb.lessThanOrEqualTo(product.get("price"), price);
	}

	public static Specification<Product> hasCategoryName(String categoryName) {
		return (root, query, criteriaBuilder) -> {
			if (categoryName == null || categoryName.isEmpty()) {
				return criteriaBuilder.conjunction();
			}
			return criteriaBuilder.like(root.get("category").get("name"), "%" + categoryName + "%");
		};
	}
}