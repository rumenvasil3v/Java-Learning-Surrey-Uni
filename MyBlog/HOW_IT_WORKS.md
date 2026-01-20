# How BlogHub Works - Complete Technical Guide

This document explains exactly how the BlogHub application works, from the moment you open the browser to when data is saved in the database.

## Table of Contents
1. [System Overview](#system-overview)
2. [Application Startup](#application-startup)
3. [User Registration Flow](#user-registration-flow)
4. [User Login Flow](#user-login-flow)
5. [Creating a Blog Post](#creating-a-blog-post)
6. [Viewing Posts](#viewing-posts)
7. [Adding Comments](#adding-comments)
8. [User Profile Management](#user-profile-management)
9. [Security Mechanisms](#security-mechanisms)
10. [Database Operations](#database-operations)

---

## System Overview

BlogHub is a full-stack web application built with:
- **Frontend**: HTML5, CSS3, JavaScript (Vanilla)
- **Backend**: Java Servlets
- **Database**: SQLite
- **Server**: Apache Tomcat

### Architecture Layers

```
Browser (HTML/CSS/JS)
        ↕ HTTP Requests
Tomcat Server (Servlet Container)
        ↕ JDBC
SQLite Database (blog.db)
```

---

## Application Startup

### 1. Tomcat Starts
When you start Tomcat, it:
1. Loads the `bloghub.war` file from the webapps directory
2. Extracts it to a temporary folder
3. Reads `WEB-INF/web.xml` for configuration
4. Initializes all servlets marked with `@WebServlet`

### 2. Database Initialization
The `DatabaseUtil` class has a static block that runs once:

```java
static {
    Class.forName("org.sqlite.JDBC");  // Load SQLite driver
    initializeDatabase();               // Create tables if needed
}
```

This creates three tables if they don't exist:
- `users` - stores user accounts
- `posts` - stores blog posts
- `comments` - stores comments on posts

### 3. Servlet Registration
Tomcat scans for `@WebServlet` annotations and registers:
- `/register` → RegisterServlet
- `/login` → LoginServlet
- `/logout` → LogoutServlet
- `/posts/*` → PostServlet
- `/comments/*` → CommentServlet
- `/user/*` → UserServlet

### 4. Browser Loads Homepage
When you visit `http://localhost:8080/bloghub/`:
1. Tomcat serves `index.html`
2. Browser loads `css/style.css` for styling
3. Browser loads JavaScript files:
   - `js/app.js` (utility functions)
   - `js/auth.js` (authentication logic)
4. JavaScript runs `checkAuth()` to see if user is logged in
5. JavaScript calls `loadPosts()` to fetch all blog posts

---

## User Registration Flow

Let's trace what happens when a user registers:

### Step 1: User Fills Registration Form
```html
<form id="registerForm">
    <input name="username" value="john_doe">
    <input name="email" value="john@example.com">
    <input name="password" value="secret123">
    <input name="fullName" value="John Doe">
</form>
```

### Step 2: JavaScript Captures Form Submission
```javascript
registerForm.addEventListener('submit', async (e) => {
    e.preventDefault();  // Don't reload page
    const formData = new FormData(registerForm);
    const data = Object.fromEntries(formData);
    // data = {username: "john_doe", email: "john@example.com", ...}
    
    const result = await API.post('register', data);
});
```

### Step 3: Fetch API Sends HTTP Request
```javascript
// In app.js
async post(endpoint, data) {
    const response = await fetch(endpoint, {
        method: 'POST',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: new URLSearchParams(data)
    });
    return response.json();
}
```

This sends:
```
POST /bloghub/register HTTP/1.1
Content-Type: application/x-www-form-urlencoded

username=john_doe&email=john@example.com&password=secret123&fullName=John+Doe
```

### Step 4: RegisterServlet Receives Request
```java
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        // Extract parameters
        String username = request.getParameter("username");  // "john_doe"
        String email = request.getParameter("email");        // "john@example.com"
        String password = request.getParameter("password");  // "secret123"
        String fullName = request.getParameter("fullName");  // "John Doe"
```

### Step 5: Check if Username Already Exists
```java
User existingUser = userDAO.getUserByUsername(username);
if (existingUser != null) {
    // Username taken!
    response.getWriter().write("{\"success\": false, \"message\": \"Username already exists\"}");
    return;
}
```

UserDAO executes:
```sql
SELECT * FROM users WHERE username = 'john_doe'
```

### Step 6: Hash the Password
```java
private String hashPassword(String password) {
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    byte[] hash = md.digest(password.getBytes());
    return Base64.getEncoder().encodeToString(hash);
}
```

"secret123" becomes: `"XohImNooBHFR0OVvjcYpJ3NgPQ1qq73WKhHvch0VQtg="`

### Step 7: Create User Object
```java
User user = new User();
user.setUsername(username);
user.setEmail(email);
user.setPassword(hashedPassword);  // Hashed!
user.setFullName(fullName);
user.setCreatedAt(LocalDateTime.now());
```

### Step 8: Save to Database
```java
userDAO.createUser(user);
```

UserDAO executes:
```sql
INSERT INTO users (username, email, password, full_name, bio, created_at) 
VALUES ('john_doe', 'john@example.com', 'XohImNooBHFR0OVvjcYpJ3NgPQ1qq73WKhHvch0VQtg=', 'John Doe', NULL, '2024-12-03T10:30:00')
```

Database returns the generated ID (e.g., 1), which is set on the user object.

### Step 9: Send Success Response
```java
response.setContentType("application/json");
response.getWriter().write("{\"success\": true, \"message\": \"Registration successful\"}");
```

### Step 10: JavaScript Handles Response
```javascript
if (result.success) {
    showNotification('Registration successful! Please login.', 'success');
    closeModal('registerModal');
    openModal('loginModal');  // Show login form
}
```

---

## User Login Flow

### Step 1: User Submits Login Form
```javascript
const data = {
    username: "john_doe",
    password: "secret123"
};
const result = await API.post('login', data);
```

### Step 2: LoginServlet Receives Request
```java
String username = request.getParameter("username");
String password = request.getParameter("password");
```

### Step 3: Fetch User from Database
```java
User user = userDAO.getUserByUsername(username);
```

Executes:
```sql
SELECT * FROM users WHERE username = 'john_doe'
```

Returns user object with hashed password from database.

### Step 4: Verify Password
```java
String inputHash = hashPassword(password);  // Hash the input
if (user.getPassword().equals(inputHash)) {
    // Password correct!
}
```

Compares:
- Input: `hashPassword("secret123")` → `"XohImNooBHFR0OVvjcYpJ3NgPQ1qq73WKhHvch0VQtg="`
- Database: `"XohImNooBHFR0OVvjcYpJ3NgPQ1qq73WKhHvch0VQtg="`
- Match! ✓

### Step 5: Create Session
```java
HttpSession session = request.getSession();  // Create or get existing session
session.setAttribute("userId", user.getId());           // Store: 1
session.setAttribute("username", user.getUsername());   // Store: "john_doe"
session.setAttribute("fullName", user.getFullName());   // Store: "John Doe"
```

Tomcat creates a session ID (e.g., `"ABC123XYZ"`) and sends it as a cookie:
```
Set-Cookie: JSESSIONID=ABC123XYZ; Path=/bloghub; HttpOnly
```

### Step 6: Browser Stores Cookie
The browser automatically stores this cookie and sends it with every subsequent request.

### Step 7: Page Reloads
```javascript
if (result.success) {
    window.location.reload();  // Refresh page
}
```

### Step 8: Check Auth on Reload
```javascript
async function checkAuth() {
    const response = await fetch('user/current');  // Includes session cookie
    if (response.ok) {
        updateNavForLoggedIn();  // Show logout, create post, profile links
    }
}
```

---

## Creating a Blog Post

### Step 1: User Clicks "Create Post"
Browser navigates to `create-post.html`

### Step 2: Page Checks Authentication
```javascript
const user = await checkAuth();
if (!user) {
    window.location.href = 'index.html';  // Redirect if not logged in
}
```

### Step 3: User Fills Form
```html
<form id="createPostForm">
    <input name="title" value="My First Blog Post">
    <textarea name="content">This is my first post on BlogHub!</textarea>
</form>
```

### Step 4: JavaScript Submits Form
```javascript
const data = {
    title: "My First Blog Post",
    content: "This is my first post on BlogHub!"
};
const result = await API.post('posts/', data);
```

### Step 5: PostServlet Receives Request
```java
@WebServlet("/posts/*")
public class PostServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
```

### Step 6: Check Session
```java
HttpSession session = request.getSession(false);  // Don't create new
if (session == null || session.getAttribute("userId") == null) {
    response.setStatus(401);  // Unauthorized
    response.getWriter().write("{\"success\": false, \"message\": \"Not authenticated\"}");
    return;
}
```

### Step 7: Extract Data
```java
String title = request.getParameter("title");
String content = request.getParameter("content");
int authorId = (Integer) session.getAttribute("userId");
String authorName = (String) session.getAttribute("username");
```

### Step 8: Create Post Object
```java
Post post = new Post();
post.setTitle(title);
post.setContent(content);
post.setAuthorId(authorId);
post.setAuthorName(authorName);
post.setCreatedAt(LocalDateTime.now());
post.setUpdatedAt(LocalDateTime.now());
post.setViews(0);
```

### Step 9: Save to Database
```java
postDAO.createPost(post);
```

Executes:
```sql
INSERT INTO posts (title, content, author_id, author_name, created_at, updated_at, views)
VALUES ('My First Blog Post', 'This is my first post on BlogHub!', 1, 'john_doe', '2024-12-03T11:00:00', '2024-12-03T11:00:00', 0)
```

Database returns generated ID (e.g., 1).

### Step 10: Return Post as JSON
```java
Gson gson = new Gson();
String json = gson.toJson(post);
response.getWriter().write(json);
```

Returns:
```json
{
    "id": 1,
    "title": "My First Blog Post",
    "content": "This is my first post on BlogHub!",
    "authorId": 1,
    "authorName": "john_doe",
    "createdAt": "2024-12-03T11:00:00",
    "updatedAt": "2024-12-03T11:00:00",
    "views": 0
}
```

### Step 11: Redirect to Post
```javascript
if (result.id) {
    window.location.href = `post-detail.html?id=${result.id}`;
}
```

---

## Viewing Posts

### Step 1: Homepage Loads
```javascript
document.addEventListener('DOMContentLoaded', () => {
    loadPosts();
});
```

### Step 2: Fetch All Posts
```javascript
async function loadPosts() {
    const posts = await API.get('posts/');  // GET /bloghub/posts/
}
```

### Step 3: PostServlet Handles GET Request
```java
protected void doGet(HttpServletRequest request, HttpServletResponse response) {
    String pathInfo = request.getPathInfo();  // null for /posts/
    
    if (pathInfo == null || pathInfo.equals("/")) {
        // Get all posts
        List<Post> posts = postDAO.getAllPosts();
        String json = gson.toJson(posts);
        response.getWriter().write(json);
    }
}
```

### Step 4: PostDAO Queries Database
```java
public List<Post> getAllPosts() {
    String sql = "SELECT * FROM posts ORDER BY created_at DESC";
    // Execute query and return list
}
```

### Step 5: Display Posts
```javascript
postsGrid.innerHTML = posts.map(post => `
    <div class="post-card" onclick="window.location.href='post-detail.html?id=${post.id}'">
        <h3>${post.title}</h3>
        <div class="post-meta">By ${post.authorName} on ${formatDate(post.createdAt)}</div>
        <div class="post-excerpt">${truncate(post.content, 200)}</div>
        <div class="post-stats"><span>👁️ ${post.views} views</span></div>
    </div>
`).join('');
```

### Viewing a Single Post

### Step 1: User Clicks Post Card
```javascript
onclick="window.location.href='post-detail.html?id=1'"
```

### Step 2: Page Loads and Extracts ID
```javascript
const urlParams = new URLSearchParams(window.location.search);
const postId = urlParams.get('id');  // "1"
```

### Step 3: Fetch Post Details
```javascript
const response = await fetch(`posts/${postId}`);  // GET /bloghub/posts/1
const data = await response.json();
```

### Step 4: PostServlet Handles Request
```java
String pathInfo = request.getPathInfo();  // "/1"
if (pathInfo != null && !pathInfo.equals("/")) {
    int postId = Integer.parseInt(pathInfo.substring(1));  // Extract 1
    Post post = postDAO.getPostById(postId);
}
```

### Step 5: Increment View Count
```java
postDAO.incrementViews(postId);
```

Executes:
```sql
UPDATE posts SET views = views + 1 WHERE id = 1
```

### Step 6: Get Comments
```java
List<Comment> comments = commentDAO.getCommentsByPostId(postId);
```

Executes:
```sql
SELECT * FROM comments WHERE post_id = 1 ORDER BY created_at ASC
```

### Step 7: Return Combined Data
```java
Map<String, Object> result = new HashMap<>();
result.put("post", post);
result.put("comments", comments);
String json = gson.toJson(result);
```

Returns:
```json
{
    "post": {
        "id": 1,
        "title": "My First Blog Post",
        "content": "This is my first post on BlogHub!",
        "authorId": 1,
        "authorName": "john_doe",
        "views": 5
    },
    "comments": [
        {
            "id": 1,
            "postId": 1,
            "userId": 2,
            "username": "jane_doe",
            "content": "Great post!",
            "createdAt": "2024-12-03T11:30:00"
        }
    ]
}
```

---

## Adding Comments

### Step 1: User Types Comment
```html
<textarea id="commentContent">Great post!</textarea>
<button onclick="addComment()">Post Comment</button>
```

### Step 2: JavaScript Submits Comment
```javascript
async function addComment() {
    const content = document.getElementById('commentContent').value;
    const data = { postId: postId, content: content };
    const result = await API.post('comments/', data);
}
```

### Step 3: CommentServlet Receives Request
```java
@WebServlet("/comments/*")
public class CommentServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
```

### Step 4: Check Authentication
```java
HttpSession session = request.getSession(false);
if (session == null) {
    response.setStatus(401);
    return;
}
```

### Step 5: Create Comment Object
```java
int postId = Integer.parseInt(request.getParameter("postId"));
String content = request.getParameter("content");
int userId = (Integer) session.getAttribute("userId");
String username = (String) session.getAttribute("username");

Comment comment = new Comment();
comment.setPostId(postId);
comment.setUserId(userId);
comment.setUsername(username);
comment.setContent(content);
comment.setCreatedAt(LocalDateTime.now());
```

### Step 6: Save to Database
```java
commentDAO.createComment(comment);
```

Executes:
```sql
INSERT INTO comments (post_id, user_id, username, content, created_at)
VALUES (1, 1, 'john_doe', 'Great post!', '2024-12-03T11:30:00')
```

### Step 7: Return Comment
```java
String json = gson.toJson(comment);
response.getWriter().write(json);
```

### Step 8: Display Comment Immediately
```javascript
if (result.id) {
    displayComment(result);  // Add to page without reload
}
```

---

## User Profile Management

### Step 1: User Clicks Profile Link
```javascript
window.location.href = 'profile.html';
```

### Step 2: Fetch Current User Data
```javascript
const user = await API.get('user/current');
```

### Step 3: UserServlet Handles Request
```java
@WebServlet("/user/*")
public class UserServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String pathInfo = request.getPathInfo();  // "/current"
        
        if ("/current".equals(pathInfo)) {
            HttpSession session = request.getSession(false);
            int userId = (Integer) session.getAttribute("userId");
            User user = userDAO.getUserById(userId);
            // Return user data (without password!)
        }
    }
}
```

### Step 4: Fetch User's Posts
```javascript
const posts = await API.get(`posts/user/${user.id}`);
```

### Step 5: Display Profile
```javascript
document.getElementById('profileUsername').textContent = user.username;
document.getElementById('profileEmail').value = user.email;
document.getElementById('profileFullName').value = user.fullName;
document.getElementById('profileBio').value = user.bio || '';
```

### Updating Profile

### Step 1: User Edits and Saves
```javascript
const data = {
    email: "newemail@example.com",
    fullName: "John Updated Doe",
    bio: "I love blogging!"
};
const result = await API.put('user/current', data);
```

### Step 2: UserServlet Handles PUT Request
```java
protected void doPut(HttpServletRequest request, HttpServletResponse response) {
    HttpSession session = request.getSession(false);
    int userId = (Integer) session.getAttribute("userId");
    
    // Read JSON body
    BufferedReader reader = request.getReader();
    User updatedUser = gson.fromJson(reader, User.class);
    updatedUser.setId(userId);
    
    userDAO.updateUser(updatedUser);
}
```

### Step 3: Update Database
```sql
UPDATE users 
SET email = 'newemail@example.com', 
    full_name = 'John Updated Doe', 
    bio = 'I love blogging!'
WHERE id = 1
```

---

## Security Mechanisms

### 1. Password Hashing
```java
// Registration
String plainPassword = "secret123";
String hashedPassword = hashPassword(plainPassword);
// Store: "XohImNooBHFR0OVvjcYpJ3NgPQ1qq73WKhHvch0VQtg="

// Login
String inputPassword = "secret123";
String inputHash = hashPassword(inputPassword);
if (storedHash.equals(inputHash)) {
    // Correct!
}
```

**Why?** If database is stolen, attackers can't see actual passwords.

### 2. Session Management
```java
// Login creates session
HttpSession session = request.getSession();
session.setAttribute("userId", 1);

// Browser stores cookie
// JSESSIONID=ABC123XYZ

// Every request includes cookie
// Tomcat finds session by ID
// Servlet checks: session.getAttribute("userId")
```

**Why?** User doesn't need to send password with every request.

### 3. SQL Injection Prevention
```java
// BAD (vulnerable):
String sql = "SELECT * FROM users WHERE username = '" + username + "'";
// If username = "admin' OR '1'='1", query becomes:
// SELECT * FROM users WHERE username = 'admin' OR '1'='1'
// Returns all users!

// GOOD (safe):
String sql = "SELECT * FROM users WHERE username = ?";
PreparedStatement pstmt = conn.prepareStatement(sql);
pstmt.setString(1, username);  // Safely escaped
```

**Why?** Prevents attackers from injecting malicious SQL.

### 4. Authorization Checks
```java
// Before deleting post
Post post = postDAO.getPostById(postId);
int currentUserId = (Integer) session.getAttribute("userId");

if (post.getAuthorId() != currentUserId) {
    response.setStatus(403);  // Forbidden
    return;
}
```

**Why?** Users can only modify their own content.

---

## Database Operations

### Connection Management
```java
public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection("jdbc:sqlite:blog.db");
}
```

Each request:
1. Opens connection
2. Executes query
3. Closes connection (in try-with-resources)

### CRUD Operations

**Create:**
```java
String sql = "INSERT INTO posts (title, content, ...) VALUES (?, ?, ...)";
PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
pstmt.setString(1, title);
pstmt.executeUpdate();
ResultSet rs = pstmt.getGeneratedKeys();
int id = rs.getInt(1);  // Get auto-generated ID
```

**Read:**
```java
String sql = "SELECT * FROM posts WHERE id = ?";
PreparedStatement pstmt = conn.prepareStatement(sql);
pstmt.setInt(1, postId);
ResultSet rs = pstmt.executeQuery();
if (rs.next()) {
    Post post = new Post();
    post.setId(rs.getInt("id"));
    post.setTitle(rs.getString("title"));
    // ...
}
```

**Update:**
```java
String sql = "UPDATE posts SET title = ?, content = ? WHERE id = ?";
PreparedStatement pstmt = conn.prepareStatement(sql);
pstmt.setString(1, newTitle);
pstmt.setString(2, newContent);
pstmt.setInt(3, postId);
int rowsAffected = pstmt.executeUpdate();
```

**Delete:**
```java
String sql = "DELETE FROM posts WHERE id = ?";
PreparedStatement pstmt = conn.prepareStatement(sql);
pstmt.setInt(1, postId);
pstmt.executeUpdate();
```

---

## Complete Request-Response Cycle

Let's trace a complete cycle for viewing a post:

```
1. User clicks post card
   ↓
2. Browser: GET /bloghub/post-detail.html?id=1
   ↓
3. Tomcat serves post-detail.html
   ↓
4. Browser loads HTML, CSS, JS
   ↓
5. JavaScript: const postId = urlParams.get('id')  // "1"
   ↓
6. JavaScript: fetch('posts/1')
   ↓
7. Browser: GET /bloghub/posts/1
   Cookie: JSESSIONID=ABC123XYZ
   ↓
8. Tomcat routes to PostServlet
   ↓
9. PostServlet.doGet() called
   ↓
10. Extract pathInfo: "/1"
    ↓
11. Parse postId: 1
    ↓
12. Call postDAO.getPostById(1)
    ↓
13. PostDAO opens database connection
    ↓
14. Execute: SELECT * FROM posts WHERE id = 1
    ↓
15. Database returns row
    ↓
16. PostDAO creates Post object from row
    ↓
17. PostDAO closes connection
    ↓
18. PostServlet calls postDAO.incrementViews(1)
    ↓
19. Execute: UPDATE posts SET views = views + 1 WHERE id = 1
    ↓
20. PostServlet calls commentDAO.getCommentsByPostId(1)
    ↓
21. Execute: SELECT * FROM comments WHERE post_id = 1
    ↓
22. Create Map with post and comments
    ↓
23. Convert to JSON with Gson
    ↓
24. Write JSON to response
    ↓
25. Tomcat sends HTTP response:
    HTTP/1.1 200 OK
    Content-Type: application/json
    
    {"post": {...}, "comments": [...]}
    ↓
26. Browser receives response
    ↓
27. JavaScript: const data = await response.json()
    ↓
28. JavaScript: displayPost(data.post)
    ↓
29. JavaScript: displayComments(data.comments)
    ↓
30. User sees post and comments!
```

---

## Key Takeaways

1. **Frontend and Backend are Separate**
   - Frontend: HTML/CSS/JS in browser
   - Backend: Java servlets on server
   - Communication: HTTP requests with JSON

2. **Servlets Handle HTTP Requests**
   - `doGet()` for retrieving data
   - `doPost()` for creating data
   - `doPut()` for updating data
   - `doDelete()` for deleting data

3. **DAO Pattern Separates Concerns**
   - Servlets handle HTTP
   - DAOs handle database
   - Models represent data

4. **Sessions Track Users**
   - Login creates session
   - Session ID stored in cookie
   - Cookie sent with every request
   - Servlet checks session for authentication

5. **Security is Multi-Layered**
   - Passwords hashed (SHA-256)
   - SQL injection prevented (PreparedStatement)
   - Authorization checked (session + ownership)
   - Sessions timeout after inactivity

6. **Database is Simple but Effective**
   - SQLite for easy setup
   - Three tables with foreign keys
   - CRUD operations via JDBC
   - Auto-increment IDs

---

## What Happens When...

### ...you refresh the page?
1. Browser sends GET request for HTML
2. JavaScript runs `checkAuth()`
3. Sends request to `/user/current` with session cookie
4. Servlet checks session, returns user data
5. Page updates UI based on login status

### ...your session expires?
1. Tomcat invalidates session after 30 minutes
2. Next request: `session.getAttribute("userId")` returns null
3. Servlet returns 401 Unauthorized
4. JavaScript redirects to login page

### ...you try to delete someone else's post?
1. JavaScript sends DELETE request
2. Servlet gets post from database
3. Compares `post.authorId` with `session.userId`
4. They don't match → return 403 Forbidden
5. JavaScript shows error message

### ...database file is deleted?
1. Next request tries to connect
2. SQLite creates new empty database
3. `initializeDatabase()` creates tables
4. All data is lost (no users, posts, comments)

---

This is how BlogHub works from top to bottom! Every click, every form submission, every database query follows these patterns. Understanding this flow helps you debug issues, add features, and build similar applications.

