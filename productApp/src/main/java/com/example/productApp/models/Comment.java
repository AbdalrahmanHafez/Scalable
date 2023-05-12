package com.example.productApp.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    private String id;

    @Field(name = "user_id")
    private String userId;

    @Field(name = "app_id")
    private String appId;

    @Field(name = "comment_text")
    private String comment;

    public Comment(String userId, String appId, String comment) {
        this.userId = userId;
        this.appId = appId;
        this.comment = comment;
    }
}

