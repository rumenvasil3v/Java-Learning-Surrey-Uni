package com.blog.servlet;

import com.blog.dao.CommentDAO;
import com.blog.model.Comment;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/comments/*")
public class CommentServlet extends HttpServlet {
    private CommentDAO commentDAO = new CommentDAO();
    private Gson gson = new Gson();
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.setStatus(401);
            response.getWriter().write("{\"error\": \"Unauthorized\"}");
            return;
        }
        
        int postId = Integer.parseInt(request.getParameter("postId"));
        String content = request.getParameter("content");
        int userId = (Integer) session.getAttribute("userId");
        String username = (String) session.getAttribute("username");
        
        try {
            Comment comment = new Comment(postId, userId, username, content);
            commentDAO.createComment(comment);
            
            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(comment));
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            response.getWriter().write("{\"error\": \"Failed to create comment\"}");
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
        int commentId = Integer.parseInt(pathInfo.substring(1));
        
        try {
            commentDAO.deleteComment(commentId);
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": true}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            response.getWriter().write("{\"error\": \"Failed to delete comment\"}");
        }
    }
}
