package com.example.productApp.commands.productCommands;


import java.util.Map;

public class GetProductByIdCommand extends ProductCommand {

    @Override
    public Object execute(Map<String, Object> map) throws Exception {
        return getProductService().getProductById((String)map.get("productId"));
    }
}
