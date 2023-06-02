package com.example.productApp.commands.productCommands;


import java.util.HashMap;

public class GetProductByIdCommand extends ProductCommand {

    @Override
    public Object execute(HashMap<String, Object> map) {
        return getProductService().getProductById((String)map.get("productId"));
    }
}
