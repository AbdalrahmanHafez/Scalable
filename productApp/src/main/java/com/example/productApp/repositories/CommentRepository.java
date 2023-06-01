package com.example.productApp.repositories;

import com.example.productApp.models.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findByUserId(String userId);
    List<Comment> findByAppId(String appId);
    Optional<Comment> findById(String commentId);
}
