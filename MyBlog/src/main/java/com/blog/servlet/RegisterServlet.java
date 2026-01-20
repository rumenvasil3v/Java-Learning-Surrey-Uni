package com.blog.servlet;

import com.blog.dao.UserDAO;
import com.blog.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Base64;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String fullName = request.getParameter("fullName");
        
        try {
            // Check if user exists
            if (userDAO.getUserByUsername(username) != null) {
                response.setContentType("application/json");
                response.getWriter().write("{\"success\": false, \"message\": \"Username already exists\"}");
                return;
            }
            
            // Hash password
            String hashedPassword = hashPassword(password);
            
            // Create user
            User user = new User(username, email, hashedPassword, fullName);
            userDAO.createUser(user);
            
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": true, \"message\": \"Registration successful\"}");
            
        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": false, \"message\": \"Registration failed\"}");
        }
    }
    
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
