package com.ecommerce.ecommerce_web.controller;

import com.ecommerce.ecommerce_web.Repository.CategoriaRepository;
import com.ecommerce.ecommerce_web.Service.ProductoService;
import com.ecommerce.ecommerce_web.model.Categoria;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CategoryViewController {

    private final CategoriaRepository categoriaRepo;
    private final ProductoService productoService;

    public CategoryViewController(CategoriaRepository categoriaRepo, ProductoService productoService) {
        this.categoriaRepo = categoriaRepo;
        this.productoService = productoService;
    }

    // ========= Cargar navbar dinámico ==========
    @ModelAttribute("categoriasNavbar")
    public Iterable<Categoria> categoriasNavbar() {
        return categoriaRepo.findAll();
    }

    // ========= Mostrar productos por categoría ==========
    @GetMapping("/categoria/{id}")
    public String productosPorCategoria(@PathVariable Long id, Model model) {

        Categoria categoria = categoriaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        model.addAttribute("categoria", categoria);
        model.addAttribute("productos", productoService.listarPorCategoria(categoria));

        return "categoria"; // la vista reutilizable
    }
}
