package com.ecommerce.ecommerce_web.controller;

import com.ecommerce.ecommerce_web.Repository.CategoriaRepository;
import com.ecommerce.ecommerce_web.Repository.ProductoRepository;
import com.ecommerce.ecommerce_web.Service.ProductoService;
import com.ecommerce.ecommerce_web.model.Categoria;
import com.ecommerce.ecommerce_web.model.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DetalleProductoController {

    private final ProductoService productoService;
    private final CategoriaRepository categoriaRepo;

    public DetalleProductoController(ProductoService productoService, CategoriaRepository categoriaRepo) {
        this.productoService = productoService;
        this.categoriaRepo = categoriaRepo;
    }

    @ModelAttribute("categoriasNavbar")
    public Iterable<Categoria> categoriasNavbar() {
        return categoriaRepo.findAll();
    }

    @GetMapping("/producto/{id}")
    public String detalleProducto(@PathVariable Long id, Model model) {
        Producto producto = productoService.obtener(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        model.addAttribute("producto", producto);
        return "detalleProducto";
    }

}
