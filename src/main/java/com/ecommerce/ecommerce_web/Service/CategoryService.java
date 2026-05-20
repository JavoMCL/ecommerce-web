package com.ecommerce.ecommerce_web.Service;

import com.ecommerce.ecommerce_web.model.Category;
import com.ecommerce.ecommerce_web.Repository.CategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

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

}

