package com.example.productApp.commands.productCommands;


import java.util.Map;

public class GetProductByCategoryIdCommand extends ProductCommand {


    @Override
    public Object execute(Map<String, Object> map) throws Exception {
        return getProductService().getProductsByCategoryId((String)map.get("category_id"));
    }
}
