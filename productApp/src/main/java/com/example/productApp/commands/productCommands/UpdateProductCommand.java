package com.example.productApp.commands.productCommands;

import com.example.productApp.models.Product;

import java.util.Map;

public class UpdateProductCommand extends ProductCommand {

    @Override
    public Object execute(Map<String, Object> map) throws Exception {
        String productId = (String) map.get("productId");
        String productName = (String) map.get("productName");
        ;
        String description = (String) map.get("description");
        String version = (String) map.get("version");
        Product productToUpdate = getProductService().getProductById(productId);

        if (productToUpdate == null) {
            throw new IllegalArgumentException("Product not found for ID: " + productId);
        }

        if (map.containsKey("productName")) {
            productToUpdate.setProductName((String) map.get("productName"));
        }

        if (map.containsKey("description")) {
            productToUpdate.setDescription((String) map.get("description"));
        }

        if (map.containsKey("version")) {
            productToUpdate.setVersion((String) map.get("version"));
        }

        return getProductService().updateProduct(productId,
                productName,
                description,
                version);
    }
}


