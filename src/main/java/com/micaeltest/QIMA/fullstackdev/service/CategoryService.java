package com.micaeltest.QIMA.fullstackdev.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.micaeltest.QIMA.fullstackdev.dto.CategoryDTO;
import com.micaeltest.QIMA.fullstackdev.model.Category;
import com.micaeltest.QIMA.fullstackdev.repository.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Transactional(readOnly = true)
	public List<Category> getAllCategory() {
		return categoryRepository.findAll();
	}

	@Transactional
	public Category addCategory(CategoryDTO categoryDTO) {
	    Category category = new Category();
	    category.setName(categoryDTO.getName());
	    if (categoryDTO.getParentId() != null) {
	        Category parentCategory = getCategoryById(categoryDTO.getParentId()).orElseThrow(
	                () -> new RuntimeException("Parent category not found for id: " + categoryDTO.getParentId()));
	        category.setParentCategory(parentCategory);
	    } else {
	        category.setParentCategory(null);
	    }
	    return categoryRepository.save(category);
	}


	@Transactional
	public void removeCategoryById(int id) {
		categoryRepository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public Optional<Category> getCategoryById(int id) {
		return categoryRepository.findById(id);
	}

	public Page<Category> getCategories(String name, Pageable pageable) {
		return categoryRepository.findAll(Specification.where(CategorySpecification.hasName(name)), pageable);
	}

	public String getCategoryPath(int categoryId) {
		Optional<Category> category = getCategoryById(categoryId);
		if (!category.isPresent())
			return "";
		StringBuilder path = new StringBuilder(category.get().getName());
		Category current = category.get().getParentCategory();
		while (current != null) {
			path.insert(0, current.getName() + " > ");
			current = current.getParentCategory();
		}
		return path.toString();
	}

	@Transactional(readOnly = true)
	public Optional<Category> findCategoryByName(String name) {
		return categoryRepository.findByName(name);
	}

	@Transactional(readOnly = true)
	public List<Category> getParentCategories() {
		return categoryRepository.findByParentCategoryIsNull();
	}

}
