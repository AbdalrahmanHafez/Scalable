package com.example.productApp.commands;

import com.google.gson.JsonObject;

import java.util.HashMap;

public interface Command {
    Object execute(HashMap<String, Object> map) throws Exception;
}
