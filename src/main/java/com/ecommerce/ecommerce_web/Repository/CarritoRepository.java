package com.ecommerce.ecommerce_web.Repository;

import com.ecommerce.ecommerce_web.model.CarritoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CarritoRepository extends JpaRepository<CarritoItem, Long> {
    List<CarritoItem> findByUsuario(Usuario usuario);
}

