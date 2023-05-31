package com.example.productApp.commands.commentCommands;

import com.example.productApp.models.Comment;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Component;

@Component
public class CreateCommentCommand extends CommentCommand {
    @Override
    public Object execute(JsonObject json) throws Exception {
        Comment comment = new Comment();
        comment.setAppId(json.get("app_id").getAsString());
        comment.setComment(json.get("comment").getAsString());
        comment.setUserId(json.get("user_id").getAsString());
        return getService().createComment(comment);
    }
}
