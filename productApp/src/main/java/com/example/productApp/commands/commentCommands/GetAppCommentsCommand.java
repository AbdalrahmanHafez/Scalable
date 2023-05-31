package com.example.productApp.commands.commentCommands;

import com.google.gson.JsonObject;
import org.springframework.stereotype.Component;

@Component
public class GetAppCommentsCommand extends CommentCommand {
    @Override
    public Object execute(JsonObject json) throws Exception {
        return getService().getAppComments(json.get("app_id").getAsString());
    }
}
