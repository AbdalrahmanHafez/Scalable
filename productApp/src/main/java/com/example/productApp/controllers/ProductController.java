package com.example.productApp.controllers;

import com.example.productApp.models.Product;
import com.example.productApp.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping(path = "/Apps")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/getAllApps")
    public List<Product> getProducts() {
        return productService.getProducts();
    }

    @GetMapping(path = "/getApps/{productId}")
    public Product getProductById(
            @PathVariable("productId") String productId) {
        return productService.getProductById(productId);
    }

    @GetMapping(path = "/getApps/Name/{productName}")
    public Product getProductByName(
            @PathVariable("productName") String productName) {
        return productService.getProductByName(productName);
    }

    @GetMapping(path = "/getApps/Category/{category_id}")
    public List<Product> getProductByCategoryId(
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

}
