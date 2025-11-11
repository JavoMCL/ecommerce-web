package com.ecommerce.ecommerce_web.controller;

import com.ecommerce.ecommerce_web.Repository.CategoriaRepository;
import com.ecommerce.ecommerce_web.Repository.ProductoRepository;
import com.ecommerce.ecommerce_web.model.Categoria;
import com.ecommerce.ecommerce_web.model.Producto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class HomeController {

    private final CategoriaRepository categoriaRepo;
    private final ProductoRepository productoRepo;

    public HomeController(CategoriaRepository categoriaRepo,
                          ProductoRepository productoRepo) {
        this.categoriaRepo = categoriaRepo;
        this.productoRepo = productoRepo;
    }

    @ModelAttribute("categoriasNavbar")
    public Iterable<Categoria> categoriasNavbar() {
        return categoriaRepo.findAll();
    }

    @GetMapping("/")
    public String home(Model model) {

        // Últimos 10 productos agregados
        List<Producto> ultimos = productoRepo.findTop10ByOrderByIdDesc();

        model.addAttribute("ultimosProductos", ultimos);
        model.addAttribute("titulo", "TecByte");

        return "main"; // usa tu vista main.html
    }
}
