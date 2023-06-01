package com.example.productApp.commands.controller;

import com.example.productApp.commands.Command;


import java.util.HashMap;

public class ContinueCommand implements Command {

    @Override
    public Object execute(HashMap<String, Object> map) throws Exception {
        PropertiesHandler.addProperty("freeze", "false");
        return new Object();
    }

}
