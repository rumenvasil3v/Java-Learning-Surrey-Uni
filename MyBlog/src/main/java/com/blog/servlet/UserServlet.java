package com.blog.servlet;

import com.blog.dao.UserDAO;
import com.blog.model.User;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/user/*")
public class UserServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();
    private Gson gson = new Gson();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        response.setContentType("application/json");
        
        if (session != null && session.getAttribute("userId") != null) {
            try {
                int userId = (Integer) session.getAttribute("userId");
                User user = userDAO.getUserById(userId);
                
                if (user != null) {
                    user.setPassword(null); // Don't send password
                    response.getWriter().write(gson.toJson(user));
                } else {
                    response.setStatus(404);
                    response.getWriter().write("{\"error\": \"User not found\"}");
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.setStatus(500);
                response.getWriter().write("{\"error\": \"Server error\"}");
            }
        } else {
            response.setStatus(401);
            response.getWriter().write("{\"error\": \"Not logged in\"}");
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
        
        String email = request.getParameter("email");
        String fullName = request.getParameter("fullName");
        String bio = request.getParameter("bio");
        
        try {
            int userId = (Integer) session.getAttribute("userId");
            User user = userDAO.getUserById(userId);
            
            if (user != null) {
                user.setEmail(email);
                user.setFullName(fullName);
                user.setBio(bio);
                userDAO.updateUser(user);
                
                user.setPassword(null);
                response.setContentType("application/json");
                response.getWriter().write(gson.toJson(user));
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            response.getWriter().write("{\"error\": \"Failed to update profile\"}");
        }
    }
}
