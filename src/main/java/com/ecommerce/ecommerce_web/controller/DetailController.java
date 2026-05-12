package com.ecommerce.ecommerce_web.controller;

import com.ecommerce.ecommerce_web.Repository.CategoryRepository;
import com.ecommerce.ecommerce_web.Service.ProductService;
import com.ecommerce.ecommerce_web.model.Category;
import com.ecommerce.ecommerce_web.model.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DetailController {

    private final ProductService productService;
    private final CategoryRepository categoryRepository;

    public DetailController(ProductService productoService, CategoryRepository categoriaRepo) {
        this.productService = productoService;
        this.categoryRepository = categoriaRepo;
    }

    @ModelAttribute("categoriesNavbar")
    public Iterable<Category> categoriesNavbar() {
        return categoryRepository.findAll();
    }

    @GetMapping("/product/{id}")
    public String showProductDetails(@PathVariable Long id, Model model) {
        Product product = productService.get(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        model.addAttribute("product", product);
        return "productDetail";
    }

}
