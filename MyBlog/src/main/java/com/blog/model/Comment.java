package com.blog.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Comment implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private int postId;
    private int userId;
    private String username;
    private String content;
    private LocalDateTime createdAt;
    
    public Comment() {
        this.createdAt = LocalDateTime.now();
    }
    
    public Comment(int postId, int userId, String username, String content) {
        this();
        this.postId = postId;
        this.userId = userId;
        this.username = username;
        this.content = content;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getPostId() { return postId; }
    public void setPostId(int postId) { this.postId = postId; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
