package com.blog.servlet;

import com.blog.dao.PostDAO;
import com.blog.dao.CommentDAO;
import com.blog.model.Post;
import com.blog.model.Comment;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/posts/*")
public class PostServlet extends HttpServlet {
    private PostDAO postDAO = new PostDAO();
    private CommentDAO commentDAO = new CommentDAO();
    private Gson gson = new Gson();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        response.setContentType("application/json");
        
        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                // Get all posts
                List<Post> posts = postDAO.getAllPosts();
                response.getWriter().write(gson.toJson(posts));
            } else if (pathInfo.startsWith("/user/")) {
                // Get posts by author
                int authorId = Integer.parseInt(pathInfo.substring(6));
                List<Post> posts = postDAO.getPostsByAuthor(authorId);
                response.getWriter().write(gson.toJson(posts));
            } else {
                // Get single post
                int postId = Integer.parseInt(pathInfo.substring(1));
                Post post = postDAO.getPostById(postId);
                
                if (post != null) {
                    postDAO.incrementViews(postId);
                    List<Comment> comments = commentDAO.getCommentsByPost(postId);
                    
                    Map<String, Object> result = new HashMap<>();
                    result.put("post", post);
                    result.put("comments", comments);
                    response.getWriter().write(gson.toJson(result));
                } else {
                    response.setStatus(404);
                    response.getWriter().write("{\"error\": \"Post not found\"}");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            response.getWriter().write("{\"error\": \"Server error\"}");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.setStatus(401);
            response.getWriter().write("{\"error\": \"Unauthorized\"}");
            return;
        }
        
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        int authorId = (Integer) session.getAttribute("userId");
        String authorName = (String) session.getAttribute("username");
        
        try {
            Post post = new Post(title, content, authorId, authorName);
            postDAO.createPost(post);
            
            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(post));
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            response.getWriter().write("{\"error\": \"Failed to create post\"}");
        }
    }
    
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.setStatus(401);
            response.getWriter().write("{\"error\": \"Unauthorized\"}");
            return;
        }
        
        String pathInfo = request.getPathInfo();
        int postId = Integer.parseInt(pathInfo.substring(1));
        
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        
        try {
            Post post = postDAO.getPostById(postId);
            if (post != null && post.getAuthorId() == (Integer) session.getAttribute("userId")) {
                post.setTitle(title);
                post.setContent(content);
                postDAO.updatePost(post);
                
                response.setContentType("application/json");
                response.getWriter().write(gson.toJson(post));
            } else {
                response.setStatus(403);
                response.getWriter().write("{\"error\": \"Forbidden\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            response.getWriter().write("{\"error\": \"Failed to update post\"}");
        }
    }
    
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.setStatus(401);
            response.getWriter().write("{\"error\": \"Unauthorized\"}");
            return;
        }
        
        String pathInfo = request.getPathInfo();
        int postId = Integer.parseInt(pathInfo.substring(1));
        
        try {
            Post post = postDAO.getPostById(postId);
            if (post != null && post.getAuthorId() == (Integer) session.getAttribute("userId")) {
                postDAO.deletePost(postId);
                response.setContentType("application/json");
                response.getWriter().write("{\"success\": true}");
            } else {
                response.setStatus(403);
                response.getWriter().write("{\"error\": \"Forbidden\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            response.getWriter().write("{\"error\": \"Failed to delete post\"}");
        }
    }
}
