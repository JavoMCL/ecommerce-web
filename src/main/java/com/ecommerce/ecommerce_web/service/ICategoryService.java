package com.ecommerce.ecommerce_web.service;
import java.util.List;

import com.ecommerce.ecommerce_web.model.Category;

public interface ICategoryService {
    public List<Category> listCategories();

    public void addCategory(Category category);

    public void deleteCategory(Category category);
}
