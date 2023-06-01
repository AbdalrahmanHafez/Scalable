package com.example.productApp.commands.commentCommands;

import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;

import java.util.HashMap;

@Component
<<<<<<< Updated upstream
public class DeleteCommentCommand extends CommentCommand {


=======
public class DeleteCommentCommand extends CommentCommand{
>>>>>>> Stashed changes
    @Override
    public Object execute(HashMap<String, Object> map) throws Exception {
        return getService().deleteComment((String)map.get("comment_id"));
    }
}

