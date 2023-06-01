package com.example.media.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.media.models.AppMedia;

@Repository
public interface AppMediaRepository extends MongoRepository<AppMedia, String> {

	// @Query("{name:'?0'}")
	// GroceryItem findItemByName(String name);

	// @Query(value = "{category:'?0'}", fields = "{'name' : 1, 'quantity' : 1}")
	// List<GroceryItem> findAll(String category);

	public long count();

}
