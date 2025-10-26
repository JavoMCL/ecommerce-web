package com.ecommerce.ecommerce_web.Repository;

import com.ecommerce.ecommerce_web.model.Producto;
import com.ecommerce.ecommerce_web.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByCategoria(Categoria categoria);
}
