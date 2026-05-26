package com.ecommerce.ecommerce_web.service;

import com.ecommerce.ecommerce_web.repository.CategoryRepository;
import com.ecommerce.ecommerce_web.model.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository repo;

    @InjectMocks
    private CategoryService service;

    @DisplayName("Should list all categories from repository")
    @Test
    void listCategoriesReturnsRepositoryCategories() {
        List<Category> categories = Arrays.asList(
                new Category(1L, "Technology"),
                new Category(2L, "Home")
        );

        when(repo.findAll()).thenReturn(categories);

        List<Category> result = service.listCategories();

        assertEquals(2, result.size());
        assertEquals("Technology", result.get(0).getName());
        verify(repo).findAll();
    }

    @DisplayName("Should add a category successfully")
    @Test
    void addCategory() {
        Category category = new Category(null, "Toys");

        service.addCategory(category);

        verify(repo).save(category);
    }

    @DisplayName("Should delete a category successfully")
    @Test
    void deleteCategory() {
        Category category = new Category(5L, "Clothes");

        service.deleteCategory(category);

        verify(repo).delete(category);
    }
}
