<<<<<<<< HEAD:MediaApp/src/main/java/app/media/repositories/AppMediaRepository.java
package app.media.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
========
package com.example.demo.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
>>>>>>>> UserApp:src/main/java/com/example/demo/Repository/ItemRepository.java
import org.springframework.stereotype.Repository;

import app.media.models.AppMedia;

@Repository
public interface AppMediaRepository extends MongoRepository<AppMedia, String> {

	// @Query("{name:'?0'}")
	// GroceryItem findItemByName(String name);

	// @Query(value = "{category:'?0'}", fields = "{'name' : 1, 'quantity' : 1}")
	// List<GroceryItem> findAll(String category);

	public long count();

}
