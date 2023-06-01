package com.example.productApp.commands.productCommands;

import com.example.productApp.models.Product;

import java.util.HashMap;

public class Increase1StarRatingCommand extends ProductCommand{

    @Override
    public Object execute(HashMap<String, Object> map) throws Exception {
        String productId = (String) map.get("productId");
       // List<Integer> TheRating = (List<Integer>) map.get("TheRating"); is it really needed??????????????????


        Product productToUpdate = getProductService().getProductById(productId);
//        if (productToUpdate == null) {
//            throw new IllegalArgumentException("Product not found for ID: " + productId);
//        }
        if (map.containsKey("TheRating")) {
            productToUpdate.setrating((java.util.List<Integer>) map.get("TheRating"));//????????????????????????
        }
        return getProductService().increase1starrating(productId);
    }
    
}
