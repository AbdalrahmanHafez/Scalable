package com.example.productApp.commands;


import java.util.HashMap;

public interface Command {
    Object execute(HashMap<String, Object> map) throws Exception;
}
