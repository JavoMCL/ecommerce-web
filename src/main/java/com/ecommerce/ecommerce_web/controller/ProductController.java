package com.ecommerce.ecommerce_web.controller;

import com.ecommerce.ecommerce_web.dto.ProductDTO;
import com.ecommerce.ecommerce_web.model.Product;
import com.ecommerce.ecommerce_web.service.ProductService;

import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin("*")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> listProducts() {
        List<ProductDTO> products = productService.listProductsAsDTO();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        ProductDTO product = productService.getProductById(id);

        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(product);
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
    public ResponseEntity<?> createProduct(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") Double price,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "available", required = false, defaultValue = "true") boolean available,
            @RequestParam(value = "stock", required = false, defaultValue = "0") int stock
    ) throws IOException {

        try {
            ProductDTO productDTO = new ProductDTO();
            productDTO.setName(name);
            productDTO.setDescription(description);
            productDTO.setPrice(price);
            productDTO.setCategoryId(categoryId);
            productDTO.setAvailable(available);
            productDTO.setStock(stock);

            ProductDTO created = productService.addProductDTO(productDTO, image);

            return ResponseEntity.status(HttpStatus.CREATED).body(created);

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not create product");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") Double price,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "available", required = false) Boolean available,
            @RequestParam(value = "stock", required = false) Integer stock
    ) throws IOException {

        try {
            ProductDTO existing = productService.getProductById(id);

            if (existing == null) {
                return ResponseEntity.notFound().build();
            }

            ProductDTO productDTO = new ProductDTO();
            productDTO.setName(name);
            productDTO.setDescription(description);
            productDTO.setPrice(price);
            productDTO.setCategoryId(categoryId);

            if (available != null) {
                productDTO.setAvailable(available);
            }

            if (stock != null) {
                productDTO.setStock(stock);
            }

            ProductDTO updated = productService.updateProductDTO(id, productDTO, image);

            return ResponseEntity.ok(updated);

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not update product");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {

        try {
            ProductDTO existing = productService.getProductById(id);

            if (existing == null) {
                return ResponseEntity.notFound().build();
            }

            productService.deleteProduct(productService.convertToEntity(existing));

            return ResponseEntity.ok().build();

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not delete product");
        }
    }
}