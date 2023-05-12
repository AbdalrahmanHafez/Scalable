package com.example.productApp.services;

import com.example.productApp.models.Category;
import com.example.productApp.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepo;

    @Autowired
    public CategoryService(CategoryRepository categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    public List<Category> getAllCategories(){
        return categoryRepo.findAll();
    }

    public Category getCategory(int id){
        return categoryRepo.findById(id);
    }
}
