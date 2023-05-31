package com.example.productApp.commands.productCommands;

import com.google.gson.JsonObject;

import java.util.HashMap;

public class GetProductByCategoryIdCommand extends ProductCommand {


    @Override
    public Object execute(HashMap<String, Object> map) throws Exception {
        return getProductService().getProductsByCategoryId((String)map.get("category_id"));
    }
}
