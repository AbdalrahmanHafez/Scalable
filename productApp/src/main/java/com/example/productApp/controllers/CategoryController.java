package com.example.productApp.controllers;

import com.example.productApp.models.Category;
import com.example.productApp.models.Comment;
import com.example.productApp.services.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/getCategories")
    List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/getCategory/{id}")
    Category getCategory(@PathVariable("id") int id) {
        return categoryService.getCategory(id);
    }

    @PostMapping
    public Category createComment(@RequestBody Category category) {
        return categoryService.createCategory(category);
    }
}
