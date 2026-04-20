package com.ecommerce.ecommerce_web.Service;

import com.ecommerce.ecommerce_web.model.Producto;
import com.ecommerce.ecommerce_web.model.Categoria;
import com.ecommerce.ecommerce_web.Repository.ProductoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProductoService implements IProductoService{

    @Autowired
    private ProductoRepository repo;


    @Override
    public List<Producto> listarProductos() {
        return repo.findAll();
    }

    @Override
    public List<Producto> listarPorCategoria(Categoria categoria) {
        return repo.findByCategoria(categoria);
    }

    @Override
      public List<Producto> buscarPorNombreODescripcion(String query) {
        return repo.buscarPorNombreODescripcion(query);
    }


    @Override
    public Optional<Producto> obtener(Long id) {
        return repo.findById(id);
    }

    @Override
    public void anadirProducto(Producto producto) {
        repo.save(producto);
    }

    @Override
    public void anadirProducto(Producto producto, MultipartFile imagen) throws IOException {
        if (imagen != null && !imagen.isEmpty()) {
            String nombreOriginal = Objects.requireNonNullElse(imagen.getOriginalFilename(), "imagen");
            producto.setImagen(nombreOriginal);
            producto.setTipoDeImagen(imagen.getContentType());
            producto.setImagenBytes(imagen.getBytes());
        }

        repo.save(producto);
    }

    @Override
    public void eliminarProducto(Producto producto) {
        repo.delete(producto);
    }
}
