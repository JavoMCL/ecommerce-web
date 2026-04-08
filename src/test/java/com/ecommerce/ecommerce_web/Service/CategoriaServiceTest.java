package com.ecommerce.ecommerce_web.Service;

import com.ecommerce.ecommerce_web.Repository.CategoriaRepository;
import com.ecommerce.ecommerce_web.model.Categoria;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository repo;

    @InjectMocks
    private CategoriaService service;

    @Test
    void listarCategoriasDevuelveLasCategoriasDelRepositorio() {
        List<Categoria> categorias = Arrays.asList(
                new Categoria(1L, "Tecnología"),
                new Categoria(2L, "Hogar")
        );

        when(repo.findAll()).thenReturn(categorias);

        List<Categoria> resultado = service.listarCategorias();

        assertEquals(2, resultado.size());
        assertEquals("Tecnología", resultado.get(0).getNombre());
        verify(repo).findAll();
    }

    @Test
    void añadirCategoriaGuardaLaCategoria() {
        Categoria categoria = new Categoria(null, "Juguetes");

        service.añadirCategoria(categoria);

        verify(repo).save(categoria);
    }

    @Test
    void eliminarCategoriaBorraLaCategoria() {
        Categoria categoria = new Categoria(5L, "Ropa");

        service.eliminarCategoria(categoria);

        verify(repo).delete(categoria);
    }
}
