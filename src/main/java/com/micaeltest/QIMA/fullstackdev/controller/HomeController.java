package com.micaeltest.QIMA.fullstackdev.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.micaeltest.QIMA.fullstackdev.dto.ProductDTO;
import com.micaeltest.QIMA.fullstackdev.global.GlobalData;
import com.micaeltest.QIMA.fullstackdev.service.CategoryService;
import com.micaeltest.QIMA.fullstackdev.service.ProductService;


@Controller
public class HomeController {

	@Autowired
	ProductService productService;

	@Autowired
	CategoryService categoryService;

	@GetMapping({ "/", "/home" })
	public String home(Model model) {
		model.addAttribute("cartCount", GlobalData.cart.size());
		return "index";
	}

	@GetMapping("/shop")
	public String shop(Model model) {
		model.addAttribute("categories", categoryService.getAllCategory());
		List<ProductDTO> productDTOs = productService.getAllProduct();
		model.addAttribute("products", productDTOs);
		model.addAttribute("cartCount", GlobalData.cart.size());
		return "shop";
	}

	@GetMapping("/shop/category/{id}")
	public String shopByCategory(Model model, @PathVariable int id) {
		model.addAttribute("categories", categoryService.getAllCategory());
		List<ProductDTO> productDTOs = productService.getAllProductsByCategoryId(id);
		model.addAttribute("products", productDTOs);
		model.addAttribute("cartCount", GlobalData.cart.size());
		return "shop";
	}

	@GetMapping("/shop/viewproduct/{id}")
	public String viewProduct(Model model, @PathVariable long id) {

		model.addAttribute("product", productService.getProductById(id).get());
		model.addAttribute("cartCount", GlobalData.cart.size());
		return "viewProduct";
	}

}