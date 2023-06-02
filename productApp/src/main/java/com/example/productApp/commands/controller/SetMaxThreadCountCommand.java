package com.example.productApp.commands.controller;

import com.example.productApp.commands.Command;

import java.util.HashMap;

public class SetMaxThreadCountCommand implements Command {

    @Override
    public Object execute(HashMap<String , Object> map)  {
        PropertiesHandler.addProperty("freeze", "true");
        return new Object();
    }

}
