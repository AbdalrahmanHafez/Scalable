package com.example.productApp.commands.commentCommands;

import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;

@Component
public class DeleteCommentCommand extends CommentCommand {
    @Override
    public Object execute(JsonObject json) throws Exception {
        return getService().deleteComment(json.get("comment_id").getAsString());
    }
}

