package com.example.productApp.services;

import com.example.productApp.models.Product;
import com.example.productApp.repositories.ProductRepository;
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

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(String productId) {
        Optional<Product> productOptional =
                productRepository.findById(productId);

        if (productOptional.isEmpty()) {
            throw new IllegalStateException("App is not here");

        }

        return productOptional.get();
    }

    public Product getProductByName(String productName) {
        Optional<Product> productOptional = Optional.ofNullable(productRepository
                .findProductByName(productName));

        return productOptional.orElse(null);
    }

    @Autowired
    private MongoTemplate mongoTemplate;
    public List<Product> getProductsByCategoryId(String category_id) {


        Query query = new Query();
        query.addCriteria(Criteria.where("category_id").is(category_id));

        List<Product> result = mongoTemplate.find(query, Product.class);

        return result;
    }

    public void addNewProduct(Product product) {
        Optional<Product> productOptional =
                Optional.ofNullable(productRepository.findProductByName(product.getProductName()));

        if (productOptional.isPresent()) {
            throw new IllegalStateException("name is taken");

        }
        productRepository.save(product);
    }


    public void deleteProduct(String productId) {
        Optional<Product> productOptional = productRepository
                .findById(productId);
        if (productOptional.isEmpty()) {
            throw new IllegalStateException(
                    "product with id" + " " + productId + " " + "does not exists"
            );
        }
        productRepository.deleteById(productId);
    }


    public void updateProduct(String productId,
                              String productName,
                              String description,
                              String version) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalStateException(
                        "product with id" + " " + productId + " " + "does not exists"
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

    }
}
