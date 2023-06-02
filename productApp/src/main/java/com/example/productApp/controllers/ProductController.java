package com.example.productApp.controllers;

import com.example.productApp.models.Product;
import com.example.productApp.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(path = "/Apps")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/getAllApps")
    public CompletableFuture<List<Product>> getProducts() {
        return productService.getProducts();
    }

    @GetMapping(path = "/getApps/{productId}")
    public Product getProductById(
            @PathVariable("productId") String productId) {
        return productService.getProductById(productId);
    }

    @GetMapping(path = "/getApps/Name/{productName}")
    public CompletableFuture<Product> getProductByName(
            @PathVariable("productName") String productName) {
        return productService.getProductByName(productName);
    }

    @GetMapping(path = "/getApps/Category/{category_id}")
    public CompletableFuture<List<Product>> getProductByCategoryId(
            @PathVariable("category_id") String category_id) {
        return productService.getProductsByCategoryId(category_id);
    }

    @PostMapping("/saveProducts")
    public void addProduct(@RequestBody Product product) {
        productService.addNewProduct(product);
    }

    @DeleteMapping(path = "/delete/{productId}")
    public void deleteProduct(
            @PathVariable("productId") String productId) {
        productService.deleteProduct(productId);

    }

    @PutMapping(path = "/update/{productId}")
    public void updateProduct(
            @PathVariable("productId") String productId,
            @RequestBody Product product) {
        String productName = product.getProductName();
        String description = product.getDescription();
        String version = product.getVersion();

        productService.updateProduct(productId, productName, description, version);
    }
    @PutMapping(path = "/increase0starrating/{productId}")
    public void increase0star(
            @PathVariable("productId") String productId
    ) {
        productService.increase0starrating(productId);

    }



    @PutMapping(path = "/increase1starrating/{productId}")
    public void increase1star(
            @PathVariable("productId") String productId
    ) {
        productService.increase1starrating(productId);

    }


    @PutMapping(path = "/increase2starrating/{productId}")
    public void increase2star(
            @PathVariable("productId") String productId
    ) {
        productService.increase2starrating(productId);

    }



    @PutMapping(path = "/increase3starrating/{productId}")
    public void increase3star(
            @PathVariable("productId") String productId
    ) {
        productService.increase3starrating(productId);

    }


    @PutMapping(path = "/increase4starrating/{productId}")
    public void increase4star(
            @PathVariable("productId") String productId
    ) {
        productService.increase4starrating(productId);

    }

    @PutMapping(path = "/updateAverageRating/{productId}")
    public void updateAverageRate(
            @PathVariable("productId") String productId
    ) {
        productService.updateAverageRating(productId);

    }
}
