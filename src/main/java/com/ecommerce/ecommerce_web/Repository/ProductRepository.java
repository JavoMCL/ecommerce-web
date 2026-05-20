package com.ecommerce.ecommerce_web.Repository;

import com.ecommerce.ecommerce_web.model.Category;
import com.ecommerce.ecommerce_web.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(Category category);

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Product> searchByNameOrDescription(String query);

    List<Product> findTop10ByOrderByIdDesc();

}
