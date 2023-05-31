package com.example.productApp.commands.commentCommands;

import com.example.productApp.commands.Command;
import com.example.productApp.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class CommentCommand implements Command {

    @Autowired
    private CommentService service;

    public CommentService getService() {
        return service;
    }

    @Autowired
    public final void setService(CommentService service) {
        this.service = service;
    }


}
