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

    private final ProductoService service;
    private final CategoriaRepository categoriaRepo;



    public ProductoController(ProductoService service, CategoriaRepository categoriaRepo) {
        this.service = service;
        this.categoriaRepo = categoriaRepo;
    }

    @GetMapping
    public List<Producto> listar() {
        return service.listar();
    }

    @PostMapping
    public Producto crear(
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
        producto.setActivo(true);

        if (imagen != null && !imagen.isEmpty()) {
            String nombreArchivo = UUID.randomUUID() + "_" + imagen.getOriginalFilename();
            String ruta = "src/main/resources/static/uploads/" + nombreArchivo;
            imagen.transferTo(new File(ruta));
            producto.setImagen("/uploads/" + nombreArchivo);
        }

        return service.guardar(producto);
    }

    @PutMapping("/{id}")
public Producto actualizar(
        @PathVariable Long id,
        @RequestParam("nombre") String nombre,
        @RequestParam("descripcion") String descripcion,
        @RequestParam("precio") Double precio,
        @RequestParam("categoria_id") Long categoriaId,
        @RequestParam(value = "imagen", required = false) MultipartFile imagen
) throws IOException {

    Producto producto = service.buscarPorId(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

    Categoria categoria = categoriaRepo.findById(categoriaId)
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

    producto.setNombre(nombre);
    producto.setDescripcion(descripcion);
    producto.setPrecio(precio);
    producto.setCategoria(categoria);


    if (imagen != null && !imagen.isEmpty()) {
        String nombreArchivo = UUID.randomUUID() + "_" + imagen.getOriginalFilename();
        String ruta = "src/main/resources/static/uploads/" + nombreArchivo;
        imagen.transferTo(new File(ruta));
        producto.setImagen("/uploads/" + nombreArchivo);
    }

    return service.guardar(producto);
}


    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
