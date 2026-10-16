package com.ecommerce.ecommerce_web.service;

import com.ecommerce.ecommerce_web.dto.ProductDTO;
import com.ecommerce.ecommerce_web.model.Category;
import com.ecommerce.ecommerce_web.model.Product;
import com.ecommerce.ecommerce_web.repository.CategoryRepository;
import com.ecommerce.ecommerce_web.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService implements IProductService {

    @Autowired
    private ProductRepository repo;

    @Autowired
    private CategoryRepository categoryRepository;

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

    public List<Product> listByCategoryAvailable(Category category) {
        return repo.findByCategoryAndAvailableTrue(category);
    }

    // ...existing code...
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

    public List<ProductDTO> listProductsAsDTO() {
        return listProducts()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO getProductById(Long id) {
        return repo.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    public ProductDTO addProductDTO(ProductDTO dto, MultipartFile imagen) throws IOException {

        Product product = convertToEntity(dto);

        if (imagen != null && !imagen.isEmpty()) {
            String nombreOriginal = Objects.requireNonNullElse(imagen.getOriginalFilename(), "imagen");
            product.setImage(nombreOriginal);
            product.setImageType(imagen.getContentType());
            product.setImageBytes(imagen.getBytes());
        }

        Product saved = repo.save(product);
        return convertToDTO(saved);
    }

    public ProductDTO updateProductDTO(Long id, ProductDTO dto, MultipartFile imagen) throws IOException {

        Product existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setPrice(dto.getPrice());
        existing.setAvailable(dto.isAvailable());
        existing.setStock(dto.getStock());

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            existing.setCategory(category);
        }

        if (imagen != null && !imagen.isEmpty()) {
            String nombreOriginal = Objects.requireNonNullElse(imagen.getOriginalFilename(), "imagen");
            existing.setImage(nombreOriginal);
            existing.setImageType(imagen.getContentType());
            existing.setImageBytes(imagen.getBytes());
        }

        Product saved = repo.save(existing);
        return convertToDTO(saved);
    }

    public ProductDTO convertToDTO(Product product) {
        if (product == null) return null;

        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setImage(product.getImage());
        dto.setImageType(product.getImageType());
        dto.setAvailable(product.isAvailable());
        dto.setStock(product.getStock());
        dto.setCategoryId(
                product.getCategory() != null
                        ? product.getCategory().getId()
                        : null
        );

        return dto;
    }

    public Product convertToEntity(ProductDTO dto) {
        if (dto == null) return null;

        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setImage(dto.getImage());
        product.setImageType(dto.getImageType());
        product.setAvailable(dto.isAvailable());
        product.setStock(dto.getStock());

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
        }

        return product;
    }
}