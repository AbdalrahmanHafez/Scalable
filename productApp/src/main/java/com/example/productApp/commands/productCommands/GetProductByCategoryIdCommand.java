package com.example.productApp.commands.productCommands;

import com.google.gson.JsonObject;

import java.util.HashMap;

public class GetProductByCategoryIdCommand extends ProductCommand {

    @Override
    public Object execute(JsonObject json) throws Exception {
        return getProductService().getProductsByCategoryId(json.get("catergory_Id").getAsString());
    }
}
