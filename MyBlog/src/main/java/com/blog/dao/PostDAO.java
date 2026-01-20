package com.blog.dao;

import com.blog.model.Post;
import com.blog.util.DatabaseUtil;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PostDAO {
    
    public Post createPost(Post post) throws SQLException {
        String sql = "INSERT INTO posts (title, content, author_id, author_name, created_at, updated_at, views) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, post.getTitle());
            pstmt.setString(2, post.getContent());
            pstmt.setInt(3, post.getAuthorId());
            pstmt.setString(4, post.getAuthorName());
            pstmt.setString(5, post.getCreatedAt().toString());
            pstmt.setString(6, post.getUpdatedAt().toString());
            pstmt.setInt(7, post.getViews());
            
            pstmt.executeUpdate();
            
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    post.setId(rs.getInt(1));
                }
            }
        }
        return post;
    }
    
    public Post getPostById(int id) throws SQLException {
        String sql = "SELECT * FROM posts WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractPost(rs);
            }
        }
        return null;
    }
    
    public List<Post> getAllPosts() throws SQLException {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT * FROM posts ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                posts.add(extractPost(rs));
            }
        }
        return posts;
    }
    
    public List<Post> getPostsByAuthor(int authorId) throws SQLException {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT * FROM posts WHERE author_id = ? ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, authorId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                posts.add(extractPost(rs));
            }
        }
        return posts;
    }
    
    public boolean updatePost(Post post) throws SQLException {
        String sql = "UPDATE posts SET title = ?, content = ?, updated_at = ? WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, post.getTitle());
            pstmt.setString(2, post.getContent());
            pstmt.setString(3, LocalDateTime.now().toString());
            pstmt.setInt(4, post.getId());
            
            return pstmt.executeUpdate() > 0;
        }
    }
    
    public boolean deletePost(int id) throws SQLException {
        String sql = "DELETE FROM posts WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }
    
    public void incrementViews(int postId) throws SQLException {
        String sql = "UPDATE posts SET views = views + 1 WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, postId);
            pstmt.executeUpdate();
        }
    }
    
    private Post extractPost(ResultSet rs) throws SQLException {
        Post post = new Post();
        post.setId(rs.getInt("id"));
        post.setTitle(rs.getString("title"));
        post.setContent(rs.getString("content"));
        post.setAuthorId(rs.getInt("author_id"));
        post.setAuthorName(rs.getString("author_name"));
        post.setCreatedAt(LocalDateTime.parse(rs.getString("created_at")));
        post.setUpdatedAt(LocalDateTime.parse(rs.getString("updated_at")));
        post.setViews(rs.getInt("views"));
        return post;
    }
}
