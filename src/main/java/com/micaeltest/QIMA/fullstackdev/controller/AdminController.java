package com.micaeltest.QIMA.fullstackdev.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.micaeltest.QIMA.fullstackdev.dto.CategoryDTO;
import com.micaeltest.QIMA.fullstackdev.dto.ProductDTO;
import com.micaeltest.QIMA.fullstackdev.model.Category;
import com.micaeltest.QIMA.fullstackdev.model.CustomUserDetail;
import com.micaeltest.QIMA.fullstackdev.model.Product;
import com.micaeltest.QIMA.fullstackdev.repository.UserRepository;
import com.micaeltest.QIMA.fullstackdev.service.CategoryService;
import com.micaeltest.QIMA.fullstackdev.service.ProductService;
import com.micaeltest.QIMA.fullstackdev.service.ProductSpecification;

@Controller
public class AdminController {

	private final Path fileStorageLocation;

	public static String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/productimages";

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ProductService productService;

	@Autowired
	private UserRepository userRepository;

	public AdminController() {
		this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();

		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception ex) {
			throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
		}
	}

	@PostConstruct
	public void checkAndCreateDirectories() {
		Path uploadPath = Paths.get(uploadDir);
		if (!Files.exists(uploadPath)) {
			try {
				Files.createDirectories(uploadPath);
			} catch (IOException e) {
				throw new RuntimeException("Could not create upload directory!", e);
			}
		}
	}

	@GetMapping("/admin")
	public String adminDashboard(Model model, Authentication authentication) {
		String email = null;
		if (authentication.getPrincipal() instanceof CustomUserDetail) {
			CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();
			email = userDetails.getUsername();
		} else if (authentication.getPrincipal() instanceof OAuth2User) {
			OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
			email = oAuth2User.getAttribute("email");
		}

		String defaultName = "Admin Static";
		String fullName = userRepository.findUserByEmail(email)
				.map(user -> user.getFirstName() + " " + user.getLastName()).orElse(defaultName);

		model.addAttribute("adminName", fullName);
		return "adminHome";
	}

	@GetMapping("/admin/products/add")
	public String productAddGet(Model model) {
		model.addAttribute("product", new ProductDTO());
		model.addAttribute("categories", categoryService.getAllCategory());
		return "productsAdd";
	}

	private ProductDTO convertToProductDTO(Product product) {
		Category category = product.getCategory();
		String categoryName = category != null ? category.getName() : null;
		return new ProductDTO(product.getId(), product.getName(), category != null ? category.getId() : null,
				categoryName, product.getPrice(), product.getWeight(), product.getDescription(),
				product.getImageName());
	}

	@PostMapping("/admin/products/add")
	public String productAddPost(@ModelAttribute("product") ProductDTO productDTO,
			@RequestParam("productImage") MultipartFile file, @RequestParam("imageName") String imgName,
			RedirectAttributes redirectAttributes) throws IOException {
		if (productDTO.getCategoryId() == null) {
			redirectAttributes.addFlashAttribute("error", "You must select a valid category.");
			return "redirect:/admin/products/add";
		}

		String imageUUID;
		if (!file.isEmpty()) {
			imageUUID = saveImage(file);
			productDTO.setImageName(imageUUID);
		} else {
			productDTO.setImageName(imgName);
		}

		try {
			productService.addProduct(productDTO);
			redirectAttributes.addFlashAttribute("successMessage", "Product added successfully.");
		} catch (RuntimeException ex) {
			redirectAttributes.addFlashAttribute("errorMessage", "Error adding product: " + ex.getMessage());
			return "redirect:/admin/products/add";
		}

		return "redirect:/admin/products";
	}

	private String saveImage(MultipartFile file) throws IOException {

		String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

		Path targetLocation = this.fileStorageLocation.resolve(fileName);
		Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

		return fileName;
	}

	@GetMapping("/admin/products/update/{id}")
	public String updateProductGet(@PathVariable long id, Model model) {
		Optional<Product> productOpt = productService.getProductById(id);
		if (productOpt.isPresent()) {
			Product product = productOpt.get();
			ProductDTO productDTO = convertToProductDTO(product);
			model.addAttribute("product", productDTO);
		} else {
			model.addAttribute("product", new ProductDTO());
		}
		model.addAttribute("categories", categoryService.getAllCategory());
		return "productsAdd";
	}

	@GetMapping("/admin/products")
	public String getProducts(Model model, @RequestParam(required = false) String name,
			@RequestParam(required = false) String categoryStr, @RequestParam(required = false) Double minPrice,
			@RequestParam(required = false) Double maxPrice,
			@PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
		Specification<Product> spec = Specification.where(null);

		if (name != null && !name.isEmpty()) {
			spec = spec.and(ProductSpecification.hasName(name));
		}

		Integer categoryId = null;
		if (categoryStr != null && !categoryStr.isEmpty()) {
			try {
				categoryId = Integer.parseInt(categoryStr);
				spec = spec.and(ProductSpecification.hasCategoryId(categoryId));
			} catch (NumberFormatException e) {

			}
		}

		if (minPrice != null) {
			spec = spec.and(ProductSpecification.priceGreaterThanEqual(minPrice));
		}

		if (maxPrice != null) {
			spec = spec.and(ProductSpecification.priceLessThanEqual(maxPrice));
		}

		Page<ProductDTO> products = productService.getProducts(spec, pageable);
		List<Category> categories = categoryService.getAllCategory();

		model.addAttribute("products", products);
		model.addAttribute("nameFilter", name);
		model.addAttribute("categoryFilter", categoryId);
		model.addAttribute("minPriceFilter", minPrice);
		model.addAttribute("maxPriceFilter", maxPrice);
		model.addAttribute("categories", categories);

		return "products";
	}

	@GetMapping("/admin/categories")
	public String getCategories(Model model, @RequestParam(required = false) String name,
			@PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
		model.addAttribute("categories", categoryService.getCategories(name, pageable));
		model.addAttribute("nameFilter", name);
		return "categories";
	}

	@GetMapping("/admin/categories/add")
	public String categoryAddGet(Model model) {
		model.addAttribute("category", new CategoryDTO());
		model.addAttribute("parentCategories", categoryService.getParentCategories());
		return "categoriesAdd";
	}

	@PostMapping("/admin/categories/add")
	public String categoryAddPost(@ModelAttribute("categoryDTO") CategoryDTO categoryDTO,
			RedirectAttributes redirectAttributes) {
		try {

			categoryService.addCategory(categoryDTO);
			redirectAttributes.addFlashAttribute("successMessage", "Categoria adicionada com sucesso.");
		} catch (RuntimeException ex) {
			redirectAttributes.addFlashAttribute("errorMessage", "Erro ao adicionar categoria: " + ex.getMessage());
			return "redirect:/admin/categories/add";
		}
		return "redirect:/admin/categories";
	}

	@GetMapping("/admin/categories/update/{id}")
	public String updateCategoryGet(@PathVariable int id, Model model) {
		Optional<Category> categoryOpt = categoryService.getCategoryById(id);
		if (categoryOpt.isPresent()) {
			Category category = categoryOpt.get();
			CategoryDTO categoryDTO = new CategoryDTO();
			categoryDTO.setId(category.getId());
			categoryDTO.setName(category.getName());
			categoryDTO.setParentId(category.getParentCategory() != null ? category.getParentCategory().getId() : null);
			model.addAttribute("category", categoryDTO);
			model.addAttribute("parentCategories", categoryService.getParentCategories());
		} else {
			model.addAttribute("category", new CategoryDTO());
		}
		return "categoriesAdd";
	}

	@GetMapping("/admin/categories/delete/{id}")
	public String deleteCategory(@PathVariable int id) {
		categoryService.removeCategoryById(id);
		return "redirect:/admin/categories";
	}

	@GetMapping("/admin/products/delete/{id}")
	public String deleteProduct(@PathVariable long id) {
		productService.removeProduct(id);
		return "redirect:/admin/products";
	}

}