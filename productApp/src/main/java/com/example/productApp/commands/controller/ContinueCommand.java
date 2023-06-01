package com.example.productApp.commands.controller;

import com.example.productApp.commands.Command;

import java.util.Map;

public class ContinueCommand implements Command {

    public Object execute(Map <String , Object> map) throws Exception {
        PropertiesHandler.addProperty("freeze", "false");
        return new Object();
    }
}