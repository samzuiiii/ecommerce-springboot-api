package com.example.springapp.service;



import com.example.springapp.model.Category;
import com.example.springapp.repository.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    @Override
    public Category addCategory(Category category) {
        return categoryRepo.save(category);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepo.findById(id).orElse(null);
    }

    @Override
    public Category updateCategory(Long id, Category category) {
        Category existing = categoryRepo.findById(id).orElse(null);
        if (existing == null) return null;
        existing.setCategoryName(category.getCategoryName());
        existing.setDescription(category.getDescription());
        return categoryRepo.save(existing);
    }

    @Override
    public boolean deleteCategory(Long id) {
        Category existing = categoryRepo.findById(id).orElse(null);
        if (existing == null) return false;
        categoryRepo.deleteById(id);
        return true;
    }
}