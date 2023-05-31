package com.example.productApp.commands.productCommands;

import com.example.productApp.commands.Command;
import com.example.productApp.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;


public abstract class ProductCommand implements Command {



    private ProductService productService;

    public ProductService getProductService() {
        return productService;
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }
}
