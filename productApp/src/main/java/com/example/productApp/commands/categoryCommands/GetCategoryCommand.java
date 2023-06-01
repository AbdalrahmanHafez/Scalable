package com.example.productApp.commands.categoryCommands;

import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class GetCategoryCommand extends CategoryCommand {

    @Override
    public Object execute(HashMap<String, Object> map) throws Exception {
        int id = (int) map.get("category_id");
        return getService().getCategory(id);
    }
}
