package com.ecommerce.ecommerce_web.Service;
import java.util.List;

import com.ecommerce.ecommerce_web.model.Categoria;

public interface ICategoriaService {
    public List<Categoria> listarCategorias();

    public void añadirCategoria(Categoria categoria);

    public void eliminarCategoria(Categoria categoria);
}
