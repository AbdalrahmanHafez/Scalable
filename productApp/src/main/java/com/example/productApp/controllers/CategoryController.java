package com.example.productApp.controllers;

import com.example.productApp.models.Category;
import com.example.productApp.services.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@AllArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/getCategories")
    List<Category> getAllCategories(){
        return categoryService.getAllCategories();
    }

    @GetMapping("/getCategory/{id}")
    Category getCategory(@PathVariable("id") int id){
        return categoryService.getCategory(id);
    }
}
