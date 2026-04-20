package com.ecommerce.ecommerce_web.Service;

import com.ecommerce.ecommerce_web.Repository.ProductoRepository;
import com.ecommerce.ecommerce_web.model.Categoria;
import com.ecommerce.ecommerce_web.model.Producto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository repo;

    @InjectMocks
    private ProductoService service;

    private Producto crearProducto(Long id, String nombre, String descripcion, Double precio, String imagen, Categoria categoria) {
        Producto producto = new Producto();
        producto.setId(id);
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setPrecio(precio);
        producto.setImagen(imagen);
        producto.setCategoria(categoria);
        return producto;
    }

    @Test
    void listarProductosDevuelveLosProductosDelRepositorio() {
        List<Producto> productos = Arrays.asList(
                crearProducto(1L, "Mouse", "Mouse gamer", 25.0, "mouse.jpg", null),
                crearProducto(2L, "Teclado", "Teclado mecanico", 50.0, "teclado.jpg", null)
        );

        when(repo.findAll()).thenReturn(productos);

        List<Producto> resultado = service.listarProductos();

        assertEquals(2, resultado.size());
        assertEquals("Mouse", resultado.get(0).getNombre());
        verify(repo).findAll();
    }

    @Test
    void listarPorCategoriaDevuelveLosProductosDeLaCategoria() {
        Categoria categoria = new Categoria(1L, "Tecnología");
        List<Producto> productos = List.of(crearProducto(1L, "Mouse", "Mouse gamer", 25.0, "mouse.jpg", categoria));

        when(repo.findByCategoria(categoria)).thenReturn(productos);

        List<Producto> resultado = service.listarPorCategoria(categoria);

        assertEquals(1, resultado.size());
        assertEquals("Tecnología", resultado.get(0).getCategoria().getNombre());
        verify(repo).findByCategoria(categoria);
    }

    @Test
    void buscarPorNombreODescripcionDevuelveResultados() {
        List<Producto> productos = List.of(crearProducto(1L, "Mouse", "Mouse gamer", 25.0, "mouse.jpg", null));

        when(repo.buscarPorNombreODescripcion("mouse")).thenReturn(productos);

        List<Producto> resultado = service.buscarPorNombreODescripcion("mouse");

        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getNombre().contains("Mouse"));
        verify(repo).buscarPorNombreODescripcion("mouse");
    }

    @Test
    void obtenerDevuelveUnProductoCuandoExiste() {
        Producto producto = crearProducto(10L, "Laptop", "Laptop basica", 500.0, "laptop.jpg", null);
        when(repo.findById(10L)).thenReturn(Optional.of(producto));

        Optional<Producto> resultado = service.obtener(10L);

        assertTrue(resultado.isPresent());
        assertEquals("Laptop", resultado.get().getNombre());
        verify(repo).findById(10L);
    }

    @Test
    void anadirProductoGuardaElProducto() {
        Producto producto = crearProducto(null, "Auriculares", "Auriculares bluetooth", 30.0, "audifonos.jpg", null);

        service.anadirProducto(producto);

        verify(repo).save(producto);
    }

    @Test
    void eliminarProductoBorraElProducto() {
        Producto producto = crearProducto(3L, "Monitor", "Monitor 24 pulgadas", 120.0, "monitor.jpg", null);

        service.eliminarProducto(producto);

        verify(repo).delete(producto);
    }
}
