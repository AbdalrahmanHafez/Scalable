package com.example.productApp.commands.categoryCommands;

import com.example.productApp.commands.Command;
import com.example.productApp.services.CategoryService;
import com.example.productApp.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class CategoryCommand implements Command {

    @Autowired
    private CategoryService service;

    public CategoryService getService() {
        return service;
    }

    @Autowired
    public final void setService(CategoryService service) {
        this.service = service;
    }


}
