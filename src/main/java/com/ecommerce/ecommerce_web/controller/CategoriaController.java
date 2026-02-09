package com.ecommerce.ecommerce_web.controller;

import com.ecommerce.ecommerce_web.model.Categoria;
import com.ecommerce.ecommerce_web.Service.CategoriaService;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin("*")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public List<Categoria> listar() {
        return categoriaService.listarCategorias();
    }

    @PostMapping
    public void crear(@RequestBody Categoria categoria) {
         categoriaService.añadirCategoria(categoria);
    }

    @PutMapping("/{id}")
    public void actualizar(@PathVariable Long id, @RequestBody Categoria categoria) {
        categoria.setId(id);
       categoriaService.añadirCategoria(categoria);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable (value = "id") Long idCategoria) {
        Categoria categoria = new Categoria();
        categoria.setId(idCategoria);
        categoriaService.eliminarCategoria(categoria);
    }
}

