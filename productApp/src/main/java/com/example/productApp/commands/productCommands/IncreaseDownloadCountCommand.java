package com.example.productApp.commands.productCommands;

import com.example.productApp.models.Product;

import java.util.HashMap;
//final
public class IncreaseDownloadCountCommand extends ProductCommand{
    @Override
    public Object execute(HashMap<String, Object> map) {
        String productId = (String) map.get("productId");
        int downloadcounteramount = (int) map.get("download_count");
        Product productToUpdate = getProductService().getProductById(productId);
//        if (productToUpdate == null) {
//            throw new IllegalArgumentException("Product not found for ID: " + productId);
//        }
        if (map.containsKey("download_count")) {
            productToUpdate.setDownload_count((int) map.get("download_count"));
        }
        return getProductService().updateAverageRating(productId);
    }
}
