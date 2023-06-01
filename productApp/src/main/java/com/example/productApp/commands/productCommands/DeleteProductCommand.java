package com.example.productApp.commands.productCommands;


import java.util.Map;

public class DeleteProductCommand extends ProductCommand{

    @Override
    public Object execute(Map<String, Object> map) throws Exception {
        return getProductService().deleteProduct((String)map.get("productId"));
    }
}
