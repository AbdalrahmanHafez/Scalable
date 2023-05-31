package com.example.productApp.commands.controller;

import com.example.productApp.commands.Command;

import java.util.HashMap;
import com.google.gson.JsonObject;
import java.util.HashMap;
public class ContinueCommand implements Command {

    public Object execute(JsonObject json) throws Exception {
        PropertiesHandler.addProperty("freeze", "false");
        return new Object();
    }


}