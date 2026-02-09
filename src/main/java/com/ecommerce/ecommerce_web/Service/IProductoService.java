package com.ecommerce.ecommerce_web.Service;
import java.util.List;
import java.util.Optional;

import com.ecommerce.ecommerce_web.model.Categoria;
import com.ecommerce.ecommerce_web.model.Producto;

public interface IProductoService {

    public List<Producto> listarProductos();

    public void añadirProducto(Producto producto);

    public void eliminarProducto(Producto producto);

    public List<Producto> listarPorCategoria(Categoria categoria);

    public List<Producto> buscarPorNombreODescripcion(String query);

    public Optional<Producto> obtener(Long id);
}
