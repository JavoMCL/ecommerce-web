package com.ecommerce.ecommerce_web.Service;

import com.ecommerce.ecommerce_web.model.Categoria;
import com.ecommerce.ecommerce_web.Repository.CategoriaRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoriaService {
    private final CategoriaRepository repo;

    public CategoriaService(CategoriaRepository repo) {
        this.repo = repo;
    }

    public List<Categoria> listar() {
        return repo.findAll();
    }

    public Categoria guardar(Categoria categoria) {
        return repo.save(categoria);
    }
    public void eliminar(Long id) {
    repo.deleteById(id);
}

}

