package com.ecommerce.ecommerce_web.controller;

import com.ecommerce.ecommerce_web.model.Categoria;
import com.ecommerce.ecommerce_web.Service.CategoriaService;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin("*")
public class CategoriaController {

    private final CategoriaService service;

    public CategoriaController(CategoriaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Categoria> listar() {
        return service.listar();
    }

    @PostMapping
    public Categoria crear(@RequestBody Categoria categoria) {
        return service.guardar(categoria);
    }

    @PutMapping("/{id}")
    public Categoria actualizar(@PathVariable Long id, @RequestBody Categoria categoria) {
        categoria.setId(id);
        return service.guardar(categoria);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}

