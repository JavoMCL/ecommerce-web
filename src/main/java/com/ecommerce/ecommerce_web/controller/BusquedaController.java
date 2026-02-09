package com.ecommerce.ecommerce_web.controller;

import com.ecommerce.ecommerce_web.Service.ProductoService;
import com.ecommerce.ecommerce_web.model.Producto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class BusquedaController {

    private final ProductoService productoService;

    public BusquedaController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping("/buscar")
    public String buscarProductos(@RequestParam("query") String query, Model model) {
        List<Producto> resultados = productoService.buscarPorNombreODescripcion(query);
        model.addAttribute("resultados", resultados);
        model.addAttribute("query", query);
        return "busqueda"; 
    }
}
