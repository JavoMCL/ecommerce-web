package com.ecommerce.ecommerce_web.controller;

import com.ecommerce.ecommerce_web.repository.CategoryRepository;
import com.ecommerce.ecommerce_web.repository.ProductRepository;
import com.ecommerce.ecommerce_web.model.Category;
import com.ecommerce.ecommerce_web.model.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class HomeController {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public HomeController(CategoryRepository categoryRepo,
                          ProductRepository productRepo) {
        this.categoryRepository = categoryRepo;
        this.productRepository = productRepo;
    }

    @ModelAttribute("categoriesNavbar")
    public Iterable<Category> categoriesNavbar() {
        return categoryRepository.findAll();
    }

    @GetMapping("/")
    public String home(Model model) {

    
        List<Product> latestProducts = productRepository.findTop10ByAvailableTrueOrderByIdDesc();

        model.addAttribute("latestProducts", latestProducts);
        model.addAttribute("title", "TecByte");

        return "main";
    }
}
