package com.ecommerce.ecommerce_web.service;

import com.ecommerce.ecommerce_web.model.Category;
import com.ecommerce.ecommerce_web.dto.CategoryDTO;
import com.ecommerce.ecommerce_web.repository.CategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService implements ICategoryService {

    @Autowired
    private CategoryRepository repo;

    @Override
    public List<Category> listCategories() {
        return repo.findAll();
    }

    @Override
    public void addCategory(Category category) {
       repo.save(category);
    }

    @Override
    public void deleteCategory(Category category) {
        repo.delete(category);
    }

    public List<CategoryDTO> listCategoriesAsDTO() {
        return listCategories().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CategoryDTO getCategoryById(Long id) {
        return repo.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    public CategoryDTO addCategoryDTO(CategoryDTO categoryDTO) {
        Category category = convertToEntity(categoryDTO);
        Category savedCategory = repo.save(category);
        return convertToDTO(savedCategory);
    }

    public CategoryDTO updateCategoryDTO(Long id, CategoryDTO categoryDTO) {
        categoryDTO.setId(id);
        Category category = convertToEntity(categoryDTO);
        Category updatedCategory = repo.save(category);
        return convertToDTO(updatedCategory);
    }


    public CategoryDTO convertToDTO(Category category) {
        if (category == null) {
            return null;
        }
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        return dto;
    }

    public Category convertToEntity(CategoryDTO dto) {
        if (dto == null) {
            return null;
        }
        Category category = new Category();
        category.setId(dto.getId());
        category.setName(dto.getName());
        return category;
    }
}


