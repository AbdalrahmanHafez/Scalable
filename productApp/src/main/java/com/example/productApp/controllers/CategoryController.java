package com.example.productApp.controllers;

import com.example.productApp.models.Category;
import com.example.productApp.models.Comment;
import com.example.productApp.services.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/getCategories")
    CompletableFuture<List<Category>> getAllCategories(){
        return categoryService.getAllCategories();
    }

    @GetMapping("/getCategory/{id}")
    CompletableFuture<Optional<Category>> getCategory(@PathVariable("id") String id){
        return categoryService.getCategory(id);
    }

    @PostMapping("/createCategory")
    public CompletableFuture<Category> createCategory(@RequestBody Category category) {
        return categoryService.createCategory(category);
    }
}
