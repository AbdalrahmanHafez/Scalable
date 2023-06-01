package com.example.productApp.commands;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Invoker {
    private Command commExec;
    public void SetCommand(Command comm){
        commExec = comm;
    }
    public void ExecuteCommand(Map<String, Object> request) throws Exception {
        commExec.execute( request);
    }

}

