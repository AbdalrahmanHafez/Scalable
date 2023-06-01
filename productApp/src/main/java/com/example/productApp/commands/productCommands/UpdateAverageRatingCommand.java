package com.example.productApp.commands.productCommands;

import com.example.productApp.models.Product;

import java.util.HashMap;

public class UpdateAverageRatingCommand extends ProductCommand {
    @Override
    public Object execute(HashMap<String, Object> map) throws Exception {
        String productId = (String) map.get("productId");
        double productAverageRate = (double) map.get("AverageRating");
        Product productToUpdate = getProductService().getProductById(productId);
//        if (productToUpdate == null) {
//            throw new IllegalArgumentException("Product not found for ID: " + productId);
//        }
        if (map.containsKey("AverageRating")) {
            productToUpdate.setaveragerating((double) map.get("AverageRating"));
        }
        return getProductService().updateAverageRating(productId);
    }
}
