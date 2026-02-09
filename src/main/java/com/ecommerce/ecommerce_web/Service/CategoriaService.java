package com.ecommerce.ecommerce_web.Service;

import com.ecommerce.ecommerce_web.model.Categoria;
import com.ecommerce.ecommerce_web.Repository.CategoriaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoriaService implements ICategoriaService{

    @Autowired
    private CategoriaRepository repo;

    @Override
    public List<Categoria> listarCategorias() {
        return repo.findAll();
    }

    @Override
    public void añadirCategoria(Categoria categoria) {
       repo.save(categoria);
    }

    @Override
    public void eliminarCategoria(Categoria categoria) {
    repo.delete(categoria);
}

}

