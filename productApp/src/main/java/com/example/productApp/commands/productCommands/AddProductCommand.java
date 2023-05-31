package com.example.productApp.commands.productCommands;

import com.example.productApp.models.Comment;
import com.example.productApp.models.Product;
import com.google.gson.JsonObject;

import java.util.HashMap;

public class AddProductCommand extends ProductCommand{

    @Override
    public Object execute(JsonObject json) throws Exception {
        Product product = new Product();
        product.setProductId(json.get("productId").getAsString());
        product.setProductName(json.get("productName").getAsString());
        product.setDescription(json.get("description").getAsString());
        product.setVersion(json.get("version").getAsString());
        product.setDownload_count(json.get("download_count").getAsInt());
        product.setCategory_id(json.get("category_id").getAsString());
        return getProductService().addNewProduct(product);
    }
}
