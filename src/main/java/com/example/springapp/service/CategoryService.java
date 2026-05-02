package com.example.springapp.service;



import com.example.springapp.model.Category;
import java.util.List;

public interface CategoryService {
    Category addCategory(Category category);
    List<Category> getAllCategories();
    Category getCategoryById(Long id);
    Category updateCategory(Long id, Category category);
    boolean deleteCategory(Long id);
}
