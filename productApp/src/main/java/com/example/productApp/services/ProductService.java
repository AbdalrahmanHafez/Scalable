package com.example.productApp.services;

import com.example.productApp.logs.logsSender;
import com.example.productApp.models.Product;
import com.example.productApp.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private MongoTemplate mongoTemplate;
    private final ProductRepository productRepository;


    private final Logger logger;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
        this.logger = LoggerFactory.getLogger(ProductService.class);
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(String productId) {
        Optional<Product> productOptional =
                productRepository.findById(productId);

        if (productOptional.isEmpty()) {
            String errorMessage = "App with id" + " " + productId + " " + "is not here";
            logsSender.sendErrorMessage(errorMessage);
            throw new IllegalStateException(errorMessage);
        } else {
            String message = "App with id" + " " + productId + " " + "has been found successfully";
            logsSender.sendLogMessage(message);
        }

        return productOptional.get();
    }

    public Product getProductByName(String productName) {
        Optional<Product> productOptional = Optional.ofNullable(productRepository
                .findProductByName(productName));

        if (productOptional.isEmpty()) {
            String errorMessage = "App with name" + " " + productName + " " + "is not here";
            logsSender.sendErrorMessage(errorMessage);
            throw new IllegalStateException(errorMessage);
        } else {
            String message = "App with name" + " " + productName + " " + "has been found successfully";
            logsSender.sendLogMessage(message);
        }
        return productOptional.get();
    }


    public List<Product> getProductsByCategoryId(String category_id) {

        Query query = new Query();
        query.addCriteria(Criteria.where("category_id").is(category_id));

        List<Product> products = mongoTemplate.find(query, Product.class);
        if (products.isEmpty()) {
            String errorMessage = "No Apps in this category" + category_id;
            logsSender.sendErrorMessage(errorMessage);
        } else {
            String message = "Apps in category" + " " + category_id + " " + "have been found successfully";
            logsSender.sendLogMessage(message);
        }

        return products;
    }

    public String addNewProduct(Product product) {
        Optional<Product> productOptional =
                Optional.ofNullable(productRepository.findProductByName(product.getProductName()));

        if (productOptional.isPresent()) {
            String errorMessage = "Name" + " " + product.getProductName() + " " + "is taken";
            logsSender.sendErrorMessage(errorMessage);
            throw new IllegalStateException(errorMessage);
        }
        productRepository.save(product);
        String message = "App with id" + " " + product.getProductId() + " " + "has been added successfully with id" ;
        logsSender.sendLogMessage(message);
        return message;
    }


    public String deleteProduct(String productId) {
        Optional<Product> productOptional = productRepository
                .findById(productId);
        if (productOptional.isEmpty()) {
            String errorMessage = "App with id" + " " + productId + " " + "does not exists";
            logsSender.sendErrorMessage(errorMessage);
            throw new IllegalStateException(errorMessage);
        }
        productRepository.deleteById(productId);
        String message = "App with id" + " " + productId + " " + "deleted successfully";
        logsSender.sendLogMessage(message);
        return message;
    }


    public void updateProduct(String productId,
                              String productName,
                              String description,
                              String version) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "App with id" + " " + productId + " " + "does not exists"
                        ));
        ;
        if (productName != null &&
                productName.length() > 0 &&
                !Objects.equals(product.getProductName(), productName)) {
            product.setProductName(productName);
        }

        if (description != null &&
                description.length() > 0 &&
                !Objects.equals(product.getDescription(), description)) {
            product.setDescription(description);
        }

        if (version != null &&
                version.length() > 0 &&
                !Objects.equals(product.getVersion(), version)) {
            product.setVersion(version);
        }
        productRepository.save(product);
        String message = "App with id" + " " + productId + " " + "has been updated successfully";
        logsSender.sendLogMessage(message);
    }
}
