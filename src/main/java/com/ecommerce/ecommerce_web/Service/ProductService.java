package com.ecommerce.ecommerce_web.Service;

import com.ecommerce.ecommerce_web.model.Category;
import com.ecommerce.ecommerce_web.model.Product;
import com.ecommerce.ecommerce_web.Repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProductService implements IProductService {

    @Autowired
    private ProductRepository repo;


    @Override
    public List<Product> listProducts() {
        return repo.findAll();
    }

    @Override
    public Optional<Product> get(Long id) {
        return repo.findById(id);
    }

    @Override
    public List<Product> listByCategory(Category category) {
        return repo.findByCategory(category);
    }

    @Override
    public List<Product> searchByNameOrDescription(String query) {
        return repo.searchByNameOrDescription(query);
    }

    @Override
    public void addProduct(Product product) {
        repo.save(product);
    }

    @Override
    public void addProduct(Product product, MultipartFile imagen) throws IOException {
        if (imagen != null && !imagen.isEmpty()) {
            String nombreOriginal = Objects.requireNonNullElse(imagen.getOriginalFilename(), "imagen");
            product.setImage(nombreOriginal);
            product.setImageType(imagen.getContentType());
            product.setImageBytes(imagen.getBytes());
        }

        repo.save(product);
    }

    @Override
    public void deleteProduct(Product product) {
        repo.delete(product);
    }
}
