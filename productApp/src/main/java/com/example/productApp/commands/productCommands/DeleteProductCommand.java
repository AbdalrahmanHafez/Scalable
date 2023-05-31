package com.example.productApp.commands.productCommands;

import com.google.gson.JsonObject;

import java.util.HashMap;

public class DeleteProductCommand extends ProductCommand{

    @Override
    public Object execute(HashMap<String, Object> map) throws Exception {
        return getProductService().deleteProduct((String)map.get("productId"));
    }
}
