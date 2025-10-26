package com.ecommerce.ecommerce_web.Repository;


import com.ecommerce.ecommerce_web.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
