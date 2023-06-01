package com.example.productApp.commands.categoryCommands;

import com.example.productApp.commands.Command;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class GetAllCategoriesCommand extends CategoryCommand {


    @Override
    public Object execute(HashMap<String, Object> map) throws Exception {
        return getService().getAllCategories();
    }
}
