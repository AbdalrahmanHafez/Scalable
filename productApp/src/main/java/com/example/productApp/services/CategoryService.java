package com.example.productApp.services;

import com.example.productApp.models.Category;
import com.example.productApp.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepo;
    @Autowired
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    public CategoryService(CategoryRepository categoryRepo, ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        this.categoryRepo = categoryRepo;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
    }

    public CompletableFuture<List<Category>> getAllCategories(){
        CompletableFuture<List<Category>> categoriesCF = CompletableFuture.supplyAsync(() -> {
            return categoryRepo.findAll();
        }, threadPoolTaskExecutor);
        return categoriesCF;
    }

    public CompletableFuture<Category> getCategory(int id){
        CompletableFuture<Category> categoryCF = CompletableFuture.supplyAsync(() -> {

            return categoryRepo.findById(id);
        }, threadPoolTaskExecutor);
        return categoryCF;
    }

    public CompletableFuture<Category> createCategory(Category category) {
        CompletableFuture<Category> categoryCF = CompletableFuture.supplyAsync(() -> {

            category.setId(UUID.randomUUID().toString());
            return categoryRepo.save(category);

        }, threadPoolTaskExecutor);
        return categoryCF;
    }

}
