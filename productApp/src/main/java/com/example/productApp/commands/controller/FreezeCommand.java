package com.example.productApp.commands.controller;

import com.example.productApp.commands.Command;

import java.util.Map;

public class FreezeCommand implements Command {

    @Override
    public Object execute(Map<String , Object> map) throws Exception {
        PropertiesHandler.addProperty("freeze", "true");
        return new Object();
    }
}
