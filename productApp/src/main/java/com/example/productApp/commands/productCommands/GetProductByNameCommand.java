package com.example.productApp.commands.productCommands;


import java.util.HashMap;

public class GetProductByNameCommand extends ProductCommand{


    @Override
    public Object execute(HashMap<String, Object> map) {
        return getProductService().getProductByName((String)map.get("productName"));
    }
}
