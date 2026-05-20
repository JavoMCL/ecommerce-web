package com.ecommerce.ecommerce_web.Repository;


import com.ecommerce.ecommerce_web.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
