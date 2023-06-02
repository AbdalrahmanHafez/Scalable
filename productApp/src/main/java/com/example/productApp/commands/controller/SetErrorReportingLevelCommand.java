package com.example.productApp.commands.controller;


import com.example.productApp.commands.Command;

import java.util.HashMap;



public class SetErrorReportingLevelCommand implements Command {
    @Override
    public Object execute(HashMap<String, Object> map)  {
        PropertiesHandler.addProperty("logging.level.root", (String) map.get("logging_level"));
        return null;
    }
}


