package com.ecommerce.ecommerce_web.controller;

import com.ecommerce.ecommerce_web.Repository.CategoriaRepository;
import com.ecommerce.ecommerce_web.model.Categoria;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class HomeController {

    private final CategoriaRepository categoriaRepo;

    public HomeController(CategoriaRepository categoriaRepo) {
        this.categoriaRepo = categoriaRepo;
    }

    @ModelAttribute("categoriasNavbar")
    public Iterable<Categoria> categoriasNavbar() {
        return categoriaRepo.findAll();
    }

    @GetMapping("/")
    public String home() {
        return "main";
    }
}


