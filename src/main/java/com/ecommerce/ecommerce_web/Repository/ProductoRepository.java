package com.ecommerce.ecommerce_web.Repository;

import com.ecommerce.ecommerce_web.model.Categoria;
import com.ecommerce.ecommerce_web.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByCategoria(Categoria categoria);

     @Query("SELECT p FROM Producto p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Producto> buscarPorNombreODescripcion(String query);


    List<Producto> findTop10ByOrderByIdDesc();

}
