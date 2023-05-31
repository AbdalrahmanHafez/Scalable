package com.example.productApp.commands.productCommands;

import com.google.gson.JsonObject;

import java.util.HashMap;

public class DeleteProductCommand extends ProductCommand{
    @Override
    public Object execute(JsonObject json) throws Exception {

        return getProductService().deleteProduct(json.get("productId").getAsString());
    }
}
