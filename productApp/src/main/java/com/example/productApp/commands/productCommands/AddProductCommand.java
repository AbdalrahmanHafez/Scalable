package com.example.productApp.commands.productCommands;


import com.example.productApp.models.Product;

import java.util.HashMap;

public class AddProductCommand extends ProductCommand {
    @Override
    public Object execute(HashMap<String, Object> map)  {
        Product product = new Product();
        product.setProductId((String) map.get("productId"));
        product.setProductName((String) map.get("productName"));
        product.setDescription((String) map.get("description"));
        product.setVersion((String) map.get("version"));
        product.setDownload_count((Integer) map.get("download_count"));
        product.setCategory_id((String) map.get("category_id"));
        product.setaveragerating((double) map.get("AverageRating"));
        product.setrating((java.util.List<Integer>) map.get("TheRating"));//????????????????????
        return getProductService().addNewProduct(product);
    }
}

