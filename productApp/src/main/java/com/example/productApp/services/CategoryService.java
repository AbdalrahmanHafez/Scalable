package com.example.productApp.services;

import com.example.productApp.models.Category;
import com.example.productApp.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepo;

    public List<Category> getAllCategories(){
        return categoryRepo.findAll();
    }

    public String getCategory(int id){
        return categoryRepo.findById(id);
    }
}
