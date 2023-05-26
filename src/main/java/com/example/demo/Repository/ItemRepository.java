package com.example.demo.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.AppMedia;
// import com.example.demo.models.GroceryItem;

@Repository
public interface ItemRepository extends MongoRepository<AppMedia, String> {

	// @Query("{name:'?0'}")
	// GroceryItem findItemByName(String name);

	// @Query(value = "{category:'?0'}", fields = "{'name' : 1, 'quantity' : 1}")
	// List<GroceryItem> findAll(String category);

	public long count();

}
