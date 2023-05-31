package com.example.productApp.commands.productCommands;

import com.google.gson.JsonObject;

import java.util.HashMap;

public class GetProductByNameCommand extends ProductCommand{


    @Override
    public Object execute(HashMap<String, Object> map) throws Exception {
        return getProductService().getProductByName((String)map.get("productName"));
    }
}
