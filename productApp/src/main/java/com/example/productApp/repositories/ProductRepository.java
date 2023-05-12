package com.example.productApp.repositories;

import com.example.productApp.models.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductRepository
        extends MongoRepository<Product, String> {

//        @Query("SELECT p FROM ProductApp p WHERE p.name = ?1")
//        Optional<Product> findProductByName(String productName);

    @Query("{productName:'?0'}")
    Product findProductByName(String productName);

    @Query("{categories_id:'?0'}")
    Product findProductByCategory(String categories_id);
}
