package com.micaeltest.QIMA.fullstackdev.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.micaeltest.QIMA.fullstackdev.dto.ProductDTO;
import com.micaeltest.QIMA.fullstackdev.model.Category;
import com.micaeltest.QIMA.fullstackdev.model.Product;
import com.micaeltest.QIMA.fullstackdev.repository.ProductRespository;

@Service
public class ProductService {

	@Autowired
	private ProductRespository productRespository;

	@Autowired
	private CategoryService categoryService;

	public List<ProductDTO> getAllProduct() {
		List<Product> products = productRespository.findAll();
		return products.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	public Page<ProductDTO> getProducts(Specification<Product> spec, Pageable pageable) {
		Page<Product> products = productRespository.findAll(spec, pageable);
		return products.map(this::convertToDTO);
	}

	public Product addProduct(ProductDTO productDTO) {
		Category category = categoryService.getCategoryById(productDTO.getCategoryId())
				.orElseThrow(() -> new RuntimeException("Category not found with ID: " + productDTO.getCategoryId()));

		Product product = new Product();
		product.setName(productDTO.getName());
		product.setCategory(category);
		product.setPrice(productDTO.getPrice());
		product.setWeight(productDTO.getWeight());
		product.setDescription(productDTO.getDescription());
		product.setImageName(productDTO.getImageName());

		return productRespository.save(product);
	}

	public void removeProduct(long id) {
		productRespository.deleteById(id);
	}

	public Optional<Product> getProductById(long id) {
		return productRespository.findById(id);
	}

	public List<ProductDTO> getAllProductsByCategoryId(int id) {
		List<Product> products = productRespository.findAllByCategory_Id(id);
		return products.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	private ProductDTO convertToDTO(Product product) {
		ProductDTO productDTO = new ProductDTO();
		productDTO.setId(product.getId());
		productDTO.setName(product.getName());
		productDTO.setCategoryId(product.getCategory() != null ? product.getCategory().getId() : null);
		productDTO.setCategoryName(product.getCategory() != null ? product.getCategory().getName() : "No Category");
		productDTO.setPrice(product.getPrice());
		productDTO.setWeight(product.getWeight());
		productDTO.setDescription(product.getDescription());
		productDTO.setImageName(product.getImageName());
		return productDTO;
	}

}
