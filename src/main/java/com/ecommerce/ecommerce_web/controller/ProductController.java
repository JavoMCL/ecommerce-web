package com.ecommerce.ecommerce_web.controller;

import com.ecommerce.ecommerce_web.repository.CategoryRepository;
import com.ecommerce.ecommerce_web.service.ProductService;
import com.ecommerce.ecommerce_web.model.Category;
import com.ecommerce.ecommerce_web.model.Product;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin("*")
public class ProductController {

    private final ProductService productService;
    private final CategoryRepository categoryRepository;

    public ProductController(ProductService productService, CategoryRepository categoryRepository) {
        this.productService = productService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public List<Product> listProducts() {
        return productService.listProducts();
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        Product product = productService.get(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        byte[] image = product.getImageBytes();
        if (image == null || image.length == 0) {
            return ResponseEntity.notFound().build();
        }

        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
        if (product.getImageType() != null && !product.getImageType().isBlank()) {
            try {
                mediaType = MediaType.parseMediaType(product.getImageType());
            } catch (InvalidMediaTypeException ignored) {
                mediaType = MediaType.APPLICATION_OCTET_STREAM;
            }
        }

        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(image);
    }

    @PostMapping
    public void createProduct(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") Double price,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "available", required = false, defaultValue = "true") boolean available,
            @RequestParam(value = "stock", required = false, defaultValue = "0") int stock
    ) throws IOException {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setCategory(category);
        product.setAvailable(available);
        product.setStock(stock);

        productService.addProduct(product, image);
    }

    @PutMapping("/{id}")
    public void updateProduct(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") Double price,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "available", required = false) Boolean available,
            @RequestParam(value = "stock", required = false) Integer stock
    ) throws IOException {

        Product product = productService.get(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setCategory(category);

        if (available != null) {
            product.setAvailable(available);
        }
        if (stock != null) {
            product.setStock(stock);
        }

        productService.addProduct(product, image);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable(value = "id") Long productId) {
        Product product = new Product();
        product.setId(productId);
        productService.deleteProduct(product);
    }
}
