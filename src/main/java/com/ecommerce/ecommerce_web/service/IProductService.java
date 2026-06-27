package com.ecommerce.ecommerce_web.service;

import com.ecommerce.ecommerce_web.model.Product;
import com.ecommerce.ecommerce_web.model.Category;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface IProductService {

    List<Product> listProducts();

    Optional<Product> get(Long id);

    List<Product> listByCategory(Category category);

    List<Product> searchByNameOrDescription(String query);

    void addProduct(Product product);

    void addProduct(Product product, MultipartFile imagen) throws IOException;

    void deleteProduct(Product product);
}
