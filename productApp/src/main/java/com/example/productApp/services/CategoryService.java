package com.example.productApp.services;

import com.example.productApp.logs.logsSender;
import com.example.productApp.models.Category;
import com.example.productApp.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
            try {
                List<Category> categories = categoryRepo.findAll();
                logsSender.sendLogMessage("Categories returned successfully");
                return categories;
            } catch (Exception e) {
                logsSender.sendErrorMessage("Failed to return categories: " + e.getStackTrace().toString());
                throw new RuntimeException(e);
            }
        }, threadPoolTaskExecutor);
        return categoriesCF;
    }

    public CompletableFuture<Optional<Category>> getCategory(String id){
        CompletableFuture<Optional<Category>> categoryCF = CompletableFuture.supplyAsync(() -> {
            if (id == null){
                logsSender.sendErrorMessage("Id cannot be null");
            }
            try {
                Optional<Category> category = categoryRepo.findById(id);
                logsSender.sendLogMessage("Category returned successfully");
                return category;
            } catch (Exception e) {
                logsSender.sendErrorMessage("Failed to return category: " + e.getStackTrace().toString());
                throw new RuntimeException(e);
            }

        }, threadPoolTaskExecutor);
        return categoryCF;
    }

    public CompletableFuture<Category> createCategory(Category category) {
        CompletableFuture<Category> categoryCF = CompletableFuture.supplyAsync(() -> {
            try {
                category.setId(UUID.randomUUID().toString());
                Category savedCategory = categoryRepo.save(category);
                logsSender.sendLogMessage("Category created successfully");
                return savedCategory;
            } catch (Exception e) {
                logsSender.sendErrorMessage("Failed to create category: " + e.getStackTrace().toString());
                throw new RuntimeException(e);
            }

        }, threadPoolTaskExecutor);
        return categoryCF;
    }

}
