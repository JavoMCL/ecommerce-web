package com.ecommerce.ecommerce_web.controller;

import com.ecommerce.ecommerce_web.dto.CategoryDTO;
import com.ecommerce.ecommerce_web.service.CategoryService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin("*")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> listCategories() {
        List<CategoryDTO> categories = categoryService.listCategoriesAsDTO();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        CategoryDTO category = categoryService.getCategoryById(id);
        if (category == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(category);
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody CategoryDTO categoryDTO) {
        try {
            if (categoryDTO.getName() == null || categoryDTO.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Category name cannot be empty");
            }
            CategoryDTO createdCategory = categoryService.addCategoryDTO(categoryDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
        } catch (DataIntegrityViolationException ex) {
            // Unique constraint on name
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Category already exists");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not create category");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
        try {
            if (categoryDTO.getName() == null || categoryDTO.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Category name cannot be empty");
            }
            CategoryDTO existingCategory = categoryService.getCategoryById(id);
            if (existingCategory == null) {
                return ResponseEntity.notFound().build();
            }
            CategoryDTO updatedCategory = categoryService.updateCategoryDTO(id, categoryDTO);
            return ResponseEntity.ok(updatedCategory);
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Category already exists");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not update category");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable (value = "id") Long categoryId) {
        try {
            CategoryDTO existingCategory = categoryService.getCategoryById(categoryId);
            if (existingCategory == null) {
                return ResponseEntity.notFound().build();
            }
            categoryService.deleteCategory(categoryService.convertToEntity(existingCategory));
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not delete category");
        }
    }
}



