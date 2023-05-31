package com.example.productApp.commands.controller;

import com.example.productApp.commands.Command;

import java.util.HashMap;

import com.google.gson.JsonObject;

public class SetMaxThreadCountCommand implements Command {

    @Override
    public Object execute(JsonObject json) throws Exception {
        PropertiesHandler.addProperty("freeze", "true");
        return new Object();
    }

}
