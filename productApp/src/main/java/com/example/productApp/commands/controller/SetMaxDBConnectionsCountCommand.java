package com.example.productApp.commands.controller;

import com.example.productApp.commands.Command;

import java.util.HashMap;

public class SetMaxDBConnectionsCountCommand implements Command {

    @Override
    public Object execute(HashMap<String , Object> map)  {
//        int x = Integer.parseInt((String) map.get("db_conn"));
//        DBConnection.setMaxConnections(x);
        return new Object();
    }

}
