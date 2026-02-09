package com.ecommerce.ecommerce_web.controller;

import com.ecommerce.ecommerce_web.model.Producto;
import com.ecommerce.ecommerce_web.model.Categoria;
import com.ecommerce.ecommerce_web.Repository.CategoriaRepository;
import com.ecommerce.ecommerce_web.Service.ProductoService;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin("*")
public class ProductoController {

    private final ProductoService productoService;
    private final CategoriaRepository categoriaRepo;

    public ProductoController(ProductoService productoService, CategoriaRepository categoriaRepo) {
        this.productoService = productoService;
        this.categoriaRepo = categoriaRepo;
    }

    @GetMapping
    public List<Producto> listar() {
        return productoService.listarProductos();
    }

    @PostMapping
    public void crear(
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("precio") Double precio,
            @RequestParam("categoria_id") Long categoriaId,
            @RequestParam(value = "imagen", required = false) MultipartFile imagen
    ) throws IOException {

        Categoria categoria = categoriaRepo.findById(categoriaId)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setPrecio(precio);
        producto.setCategoria(categoria);

        if (imagen != null && !imagen.isEmpty()) {

            String nombreArchivo = UUID.randomUUID() + "_" + imagen.getOriginalFilename();

            String ruta = System.getProperty("user.dir") + "/uploads/" + nombreArchivo;

            File destino = new File(ruta);
            destino.getParentFile().mkdirs();

            imagen.transferTo(destino);

            producto.setImagen("/uploads/" + nombreArchivo);
        }

        productoService.añadirProducto(producto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable(value = "id") Long idProducto) {
        Producto producto = new Producto();
        producto.setId(idProducto);
        productoService.eliminarProducto(producto);
    }
}
