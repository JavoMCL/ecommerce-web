package com.ecommerce.ecommerce_web.controller;

import com.ecommerce.ecommerce_web.model.Producto;
import com.ecommerce.ecommerce_web.model.Categoria;
import com.ecommerce.ecommerce_web.Repository.CategoriaRepository;
import com.ecommerce.ecommerce_web.Service.ProductoService;

import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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

    @GetMapping("/{id}/imagen")
    public ResponseEntity<byte[]> obtenerImagen(@PathVariable Long id) {
        Producto producto = productoService.obtener(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        byte[] imagen = producto.getImagenBytes();
        if (imagen == null || imagen.length == 0) {
            return ResponseEntity.notFound().build();
        }

        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
        if (producto.getTipoDeImagen() != null && !producto.getTipoDeImagen().isBlank()) {
            try {
                mediaType = MediaType.parseMediaType(producto.getTipoDeImagen());
            } catch (InvalidMediaTypeException ignored) {
                mediaType = MediaType.APPLICATION_OCTET_STREAM;
            }
        }

        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(imagen);
    }

    @PostMapping
    public void crear(
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("precio") Double precio,
            @RequestParam("categoria_id") Long categoriaId,
            @RequestParam(value = "imagen", required = false) MultipartFile imagen,
            @RequestParam(value = "disponible", required = false, defaultValue = "true") boolean disponible,
            @RequestParam(value = "stock", required = false, defaultValue = "0") int stock
    ) throws IOException {
        Categoria categoria = categoriaRepo.findById(categoriaId)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));

        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setPrecio(precio);
        producto.setCategoria(categoria);
        producto.setDisponible(disponible);
        producto.setStock(stock);

        productoService.anadirProducto(producto, imagen);
    }


    @PutMapping("/{id}")
    public void actualizar(
            @PathVariable Long id,
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("precio") Double precio,
            @RequestParam("categoria_id") Long categoriaId,
            @RequestParam(value = "imagen", required = false) MultipartFile imagen,
            @RequestParam(value = "disponible", required = false) Boolean disponible,
            @RequestParam(value = "stock", required = false) Integer stock
    ) throws IOException {

        Producto producto = productoService.obtener(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Categoria categoria = categoriaRepo.findById(categoriaId)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));

        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setPrecio(precio);
        producto.setCategoria(categoria);

        if (disponible != null) {
            producto.setDisponible(disponible);
        }
        if (stock != null) {
            producto.setStock(stock);
        }

        productoService.anadirProducto(producto, imagen);
    }


    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable(value = "id") Long idProducto) {
        Producto producto = new Producto();
        producto.setId(idProducto);
        productoService.eliminarProducto(producto);
    }
}
