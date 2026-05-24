package com.ecommerce.ecommerce_web.service;

import com.ecommerce.ecommerce_web.repository.ProductRepository;
import com.ecommerce.ecommerce_web.model.Category;
import com.ecommerce.ecommerce_web.model.Product;
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
class ProductServiceTest {

    @Mock
    private ProductRepository repo;

    @InjectMocks
    private ProductService service;

    private Product crearProducto(Long id, String nombre, String descripcion, Double precio, String imagen, Category category) {
        Product product = new Product();
        product.setId(id);
        product.setName(nombre);
        product.setDescription(descripcion);
        product.setPrice(precio);
        product.setImage(imagen);
        product.setCategory(category);
        return product;
    }

    @Test
    void listProductsFromRepository() {
        List<Product> products = Arrays.asList(
                crearProducto(1L, "Mouse", "Mouse gamer", 25.0, "mouse.jpg", null),
                crearProducto(2L, "Keyboard", "Mechanical keyboard", 50.0, "keyboard.jpg", null)
        );

        when(repo.findAll()).thenReturn(products);

        List<Product> result = service.listProducts();

        assertEquals(2, result.size());
        assertEquals("Mouse", result.get(0).getName());
        verify(repo).findAll();
    }

    @Test
    void listByCategory() {
        Category category = new Category(1L, "Technology");
        List<Product> products = List.of(crearProducto(1L, "Mouse", "Mouse gamer", 25.0, "mouse.jpg", category));

        when(repo.findByCategory(category)).thenReturn(products);

        List<Product> result = service.listByCategory(category);

        assertEquals(1, result.size());
        assertEquals("Technology", result.get(0).getCategory().getName());
        verify(repo).findByCategory(category);
    }

    @Test
    void searchByNameOrDescriptionReturnsResults() {
        List<Product> products = List.of(crearProducto(1L, "Mouse", "Mouse gamer", 25.0, "mouse.jpg", null));

        when(repo.searchByNameOrDescription("mouse")).thenReturn(products);

        List<Product> result = service.searchByNameOrDescription("mouse");

        assertEquals(1, result.size());
        assertTrue(result.get(0).getName().contains("Mouse"));
        verify(repo).searchByNameOrDescription("mouse");
    }

    @Test
    void getReturnsProductWhenExists() {
        Product product = crearProducto(10L, "Laptop", "Basic laptop", 500.0, "laptop.jpg", null);
        when(repo.findById(10L)).thenReturn(Optional.of(product));

        Optional<Product> result = service.get(10L);

        assertTrue(result.isPresent());
        assertEquals("Laptop", result.get().getName());
        verify(repo).findById(10L);
    }

    @Test
    void addProduct() {
        Product product = crearProducto(null, "Auriculares", "Auriculares bluetooth", 30.0, "audifonos.jpg", null);

        service.addProduct(product);

        verify(repo).save(product);
    }

    @Test
    void deleteProductRemovesProduct() {
        Product product = crearProducto(3L, "Monitor", "Monitor 24 pulgadas", 120.0, "monitor.jpg", null);

        service.deleteProduct(product);

        verify(repo).delete(product);
    }
}
