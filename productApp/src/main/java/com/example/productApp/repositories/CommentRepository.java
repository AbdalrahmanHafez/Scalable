package com.example.productApp.repositories;

import com.example.productApp.models.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findByUserId(String userId);
    List<Comment> findByAppId(String appId);
}
