package com.example.demo.Commands;
public class CommandInvoker {
    public Command command;

    public CommandInvoker(Command c){
        this.command=c;
    }

    public Object execute(Object obj){
       return this.command.execute(obj);
    }

    public void setCommand(Command command) {
        this.command = command;
    }
}
