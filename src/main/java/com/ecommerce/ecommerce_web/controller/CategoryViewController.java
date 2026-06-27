package com.ecommerce.ecommerce_web.controller;

import com.ecommerce.ecommerce_web.repository.CategoryRepository;
import com.ecommerce.ecommerce_web.service.ProductService;
import com.ecommerce.ecommerce_web.model.Category;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CategoryViewController {

    private final CategoryRepository categoryRepository;
    private final ProductService productService;

    public CategoryViewController(CategoryRepository categoryRepo, ProductService productService) {
        this.categoryRepository = categoryRepo;
        this.productService = productService;
    }

    @ModelAttribute("categoriesNavbar")
    public Iterable<Category> categoriesNavbar() {
        return categoryRepository.findAll();
    }

    @GetMapping("/category/{id}")
    public String showProductsByCategory(@PathVariable Long id, Model model) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        model.addAttribute("category", category);
        model.addAttribute("products", productService.listByCategory(category));

        return "category";
    }
}
