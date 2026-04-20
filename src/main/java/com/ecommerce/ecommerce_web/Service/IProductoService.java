package com.ecommerce.ecommerce_web.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.ecommerce.ecommerce_web.model.Categoria;
import com.ecommerce.ecommerce_web.model.Producto;

public interface IProductoService {

    List<Producto> listarProductos();

    void anadirProducto(Producto producto);

    void anadirProducto(Producto producto, MultipartFile imagen) throws IOException;

    void eliminarProducto(Producto producto);

    List<Producto> listarPorCategoria(Categoria categoria);

    List<Producto> buscarPorNombreODescripcion(String query);

    Optional<Producto> obtener(Long id);
}
