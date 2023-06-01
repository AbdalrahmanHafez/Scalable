package com.example.productApp.commands;


import java.util.Map;

public interface Command {
    Object execute(Map<String, Object> map) throws Exception;
}
