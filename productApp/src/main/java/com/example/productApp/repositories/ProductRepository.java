package com.example.productApp.repositories;

import com.example.productApp.models.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductRepository
        extends MongoRepository<Product, String> {

    @Query("{productName:'?0'}")
    Product findProductByName(String productName);

    Product findByProductId(String id);

}
