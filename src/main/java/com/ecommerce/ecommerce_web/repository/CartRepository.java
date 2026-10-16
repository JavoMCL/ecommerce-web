package com.ecommerce.ecommerce_web.repository;

import com.ecommerce.ecommerce_web.auth.user.User;
import com.ecommerce.ecommerce_web.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUser(User user);
}
