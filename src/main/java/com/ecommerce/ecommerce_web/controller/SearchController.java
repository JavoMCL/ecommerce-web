package com.ecommerce.ecommerce_web.controller;

import com.ecommerce.ecommerce_web.service.ProductService;
import com.ecommerce.ecommerce_web.model.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SearchController {

    private final ProductService productService;

    public SearchController(ProductService productoService) {
        this.productService = productoService;
    }

    @GetMapping("/search")
    public String searchProducts(@RequestParam("query") String query, Model model) {
        List<Product> results = productService.searchByNameOrDescription(query);
        model.addAttribute("results", results);
        model.addAttribute("query", query);
        return "search";
    }
}
