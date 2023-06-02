package com.example.productApp.commands;

import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class Invoker {
    private Command commExec;
    public void SetCommand(Command comm){
        commExec = comm;
    }
    public void ExecuteCommand(HashMap<String, Object> request) {
        commExec.execute( request);
    }

}

