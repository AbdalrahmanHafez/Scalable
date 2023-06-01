package com.example.productApp.commands.productCommands;


import java.util.Map;

public class GetProductByNameCommand extends ProductCommand{


    @Override
    public Object execute(Map<String, Object> map) throws Exception {
        return getProductService().getProductByName((String)map.get("productName"));
    }
}
