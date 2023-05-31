package com.example.productApp.commands;

import com.google.gson.JsonObject;

public interface Command {
    Object execute(JsonObject json) throws Exception;
}
