# BlogHub Architecture Guide

## System Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                        CLIENT BROWSER                        │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐   │
│  │  HTML    │  │   CSS    │  │JavaScript│  │  Fetch   │   │
│  │  Pages   │  │  Styles  │  │   Logic  │  │   API    │   │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘   │
└─────────────────────────────────────────────────────────────┘
                            ↕ HTTP/HTTPS
┌─────────────────────────────────────────────────────────────┐
│                      TOMCAT SERVER                           │
│  ┌───────────────────────────────────────────────────────┐  │
│  │              SERVLET CONTAINER                        │  │
│  │  ┌──────────────────────────────────────────────┐    │  │
│  │  │         HTTP REQUEST HANDLERS                │    │  │
│  │  │  ┌────────────┐  ┌────────────┐  ┌────────┐ │    │  │
│  │  │  │  Register  │  │   Login    │  │  Post  │ │    │  │
│  │  │  │  Servlet   │  │  Servlet   │  │Servlet │ │    │  │
│  │  │  └────────────┘  └────────────┘  └────────┘ │    │  │
│  │  │  ┌────────────┐  ┌────────────┐  ┌────────┐ │    │  │
│  │  │  │  Comment   │  │    User    │  │ Logout │ │    │  │
│  │  │  │  Servlet   │  │  Servlet   │  │Servlet │ │    │  │
│  │  │  └────────────┘  └────────────┘  └────────┘ │    │  │
│  │  └──────────────────────────────────────────────┘    │  │
│  │                         ↕                             │  │
│  │  ┌──────────────────────────────────────────────┐    │  │
│  │  │         DATA ACCESS LAYER (DAO)              │    │  │
│  │  │  ┌──────────┐  ┌──────────┐  ┌──────────┐   │    │  │
│  │  │  │ UserDAO  │  │ PostDAO  │  │CommentDAO│   │    │  │
│  │  │  └──────────┘  └──────────┘  └──────────┘   │    │  │
│  │  └──────────────────────────────────────────────┘    │  │
│  │                         ↕                             │  │
│  │  ┌──────────────────────────────────────────────┐    │  │
│  │  │         DATABASE UTILITY                     │    │  │
│  │  │         (Connection Management)              │    │  │
│  │  └──────────────────────────────────────────────┘    │  │
│  └───────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                            ↕ JDBC
┌─────────────────────────────────────────────────────────────┐
│                    SQLite DATABASE                           │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐                  │
│  │  users   │  │  posts   │  │ comments │                  │
│  │  table   │  │  table   │  │  table   │                  │
│  └──────────┘  └──────────┘  └──────────┘                  │
└─────────────────────────────────────────────────────────────┘
```

## Request Flow Diagrams

### 1. User Registration Flow

```
User Browser                RegisterServlet              UserDAO              Database
     │                            │                        │                     │
     │──── POST /register ───────>│                        │                     │
     │    (username, email,       │                        │                     │
     │     password, fullName)    │                        │                     │
     │                            │                        │                     │
     │                            │── Check if user ──────>│                     │
     │                            │   exists               │                     │
     │                            │                        │── SELECT * FROM ───>│
     │                            │                        │   users WHERE       │
     │                            │                        │   username=?        │
     │                            │                        │<─── Result ─────────│
     │                            │<─── User exists? ──────│                     │
     │                            │                        │                     │
     │                            │── Hash password        │                     │
     │                            │   (SHA-256)            │                     │
     │                            │                        │                     │
     │                            │── Create user ────────>│                     │
     │                            │                        │── INSERT INTO ─────>│
     │                            │                        │   users VALUES      │
     │                            │                        │<─── Success ────────│
     │                            │<─── User object ───────│                     │
     │                            │                        │                     │
     │<─── JSON Response ─────────│                        │                     │
     │    {success: true}         │                        │                     │
```

### 2. User Login Flow

```
User Browser                LoginServlet                 UserDAO              Database
     │                            │                        │                     │
     │──── POST /login ──────────>│                        │                     │
     │    (username, password)    │                        │                     │
     │                            │                        │                     │
     │                            │── Get user by ────────>│                     │
     │                            │   username             │                     │
     │                            │                        │── SELECT * FROM ───>│
     │                            │                        │   users WHERE       │
     │                            │                        │   username=?        │
     │                            │                        │<─── User data ──────│
     │                            │<─── User object ───────│                     │
     │                            │                        │                     │
     │                            │── Hash input password  │                     │
     │                            │   (SHA-256)            │                     │
     │                            │                        │                     │
     │                            │── Compare hashes       │                     │
     │                            │                        │                     │
     │                            │── Create session       │                     │
     │                            │   Store: userId,       │                     │
     │                            │   username, fullName   │                     │
     │                            │                        │                     │
     │<─── JSON Response ─────────│                        │                     │
     │    {success: true}         │                        │                     │
     │    + Session Cookie        │                        │                     │
```

### 3. Create Post Flow

```
User Browser                PostServlet                  PostDAO              Database
     │                            │                        │                     │
     │──── POST /posts/ ─────────>│                        │                     │
     │    (title, content)        │                        │                     │
     │    + Session Cookie        │                        │                     │
     │                            │                        │                     │
     │                            │── Check session        │                     │
     │                            │   (authenticated?)     │                     │
     │                            │                        │                     │
     │                            │── Get userId from      │                     │
     │                            │   session              │                     │
     │                            │                        │                     │
     │                            │── Create Post object   │                     │
     │                            │   (title, content,     │                     │
     │                            │    authorId, etc.)     │                     │
     │                            │                        │                     │
     │                            │── Save post ──────────>│                     │
     │                            │                        │── INSERT INTO ─────>│
     │                            │                        │   posts VALUES      │
     │                            │                        │<─── Post ID ────────│
     │                            │<─── Post with ID ──────│                     │
     │                            │                        │                     │
     │<─── JSON Response ─────────│                        │                     │
     │    {id: 1, title: ...}     │                        │                     │
```

### 4. View Post with Comments Flow

```
User Browser                PostServlet                  PostDAO/CommentDAO   Database
     │                            │                        │                     │
     │──── GET /posts/1 ─────────>│                        │                     │
     │                            │                        │                     │
     │                            │── Get post by ID ─────>│                     │
     │                            │                        │── SELECT * FROM ───>│
     │                            │                        │   posts WHERE id=1  │
     │                            │                        │<─── Post data ──────│
     │                            │<─── Post object ───────│                     │
     │                            │                        │                     │
     │                            │── Increment views ────>│                     │
     │                            │                        │── UPDATE posts ────>│
     │                            │                        │   SET views=views+1 │
     │                            │                        │<─── Success ────────│
     │                            │                        │                     │
     │                            │── Get comments ───────>│                     │
     │                            │   for post             │                     │
     │                            │                        │── SELECT * FROM ───>│
     │                            │                        │   comments WHERE    │
     │                            │                        │   post_id=1         │
     │                            │                        │<─── Comments ───────│
     │                            │<─── Comments list ─────│                     │
     │                            │                        │                     │
     │<─── JSON Response ─────────│                        │                     │
     │    {post: {...},           │                        │                     │
     │     comments: [...]}       │                        │                     │
```

## Component Interaction

### Frontend Components

```
┌─────────────────────────────────────────────────────────┐
│                    index.html                            │
│  ┌────────────┐  ┌────────────┐  ┌────────────┐        │
│  │  Navbar    │  │   Hero     │  │ Posts Grid │        │
│  │  Component │  │  Section   │  │  Component │        │
│  └────────────┘  └────────────┘  └────────────┘        │
│  ┌────────────┐  ┌────────────┐                        │
│  │   Login    │  │  Register  │                        │
│  │   Modal    │  │   Modal    │                        │
│  └────────────┘  └────────────┘                        │
└─────────────────────────────────────────────────────────┘
                        ↓ Uses
┌─────────────────────────────────────────────────────────┐
│                  JavaScript Modules                      │
│  ┌────────────┐  ┌────────────┐  ┌────────────┐        │
│  │   app.js   │  │  auth.js   │  │  post.js   │        │
│  │  (Utils)   │  │  (Auth)    │  │  (Posts)   │        │
│  └────────────┘  └────────────┘  └────────────┘        │
│  ┌────────────┐  ┌────────────┐                        │
│  │post-detail │  │ profile.js │                        │
│  │    .js     │  │ (Profile)  │                        │
│  └────────────┘  └────────────┘                        │
└─────────────────────────────────────────────────────────┘
                        ↓ Calls
┌─────────────────────────────────────────────────────────┐
│                    API Endpoints                         │
│  /register  /login  /logout  /posts  /comments  /user   │
└─────────────────────────────────────────────────────────┘
```

### Backend Components

```
┌─────────────────────────────────────────────────────────┐
│                   Servlet Layer                          │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │   Register   │  │    Login     │  │     Post     │  │
│  │   Servlet    │  │   Servlet    │  │   Servlet    │  │
│  │              │  │              │  │              │  │
│  │ @WebServlet  │  │ @WebServlet  │  │ @WebServlet  │  │
│  │ ("/register")│  │  ("/login")  │  │  ("/posts")  │  │
│  └──────────────┘  └──────────────┘  └──────────────┘  │
└─────────────────────────────────────────────────────────┘
                        ↓ Uses
┌─────────────────────────────────────────────────────────┐
│                    Model Layer                           │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │     User     │  │     Post     │  │   Comment    │  │
│  │    Model     │  │    Model     │  │    Model     │  │
│  │              │  │              │  │              │  │
│  │ - id         │  │ - id         │  │ - id         │  │
│  │ - username   │  │ - title      │  │ - postId     │  │
│  │ - email      │  │ - content    │  │ - userId     │  │
│  │ - password   │  │ - authorId   │  │ - content    │  │
│  └──────────────┘  └──────────────┘  └──────────────┘  │
└─────────────────────────────────────────────────────────┘
                        ↓ Managed by
┌─────────────────────────────────────────────────────────┐
│                     DAO Layer                            │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │   UserDAO    │  │   PostDAO    │  │ CommentDAO   │  │
│  │              │  │              │  │              │  │
│  │ - create()   │  │ - create()   │  │ - create()   │  │
│  │ - getById()  │  │ - getById()  │  │ - getByPost()│  │
│  │ - getByName()│  │ - getAll()   │  │ - delete()   │  │
│  │ - update()   │  │ - update()   │  │              │  │
│  │              │  │ - delete()   │  │              │  │
│  └──────────────┘  └──────────────┘  └──────────────┘  │
└─────────────────────────────────────────────────────────┘
                        ↓ Uses
┌─────────────────────────────────────────────────────────┐
│                 Database Utility                         │
│  ┌───────────────────────────────────────────────────┐  │
│  │              DatabaseUtil.java                    │  │
│  │                                                   │  │
│  │  - getConnection()                                │  │
│  │  - initializeDatabase()                           │  │
│  │  - Creates tables if not exist                    │  │
│  └───────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────┘
```

## Database Schema

```
┌─────────────────────────────────────────────────────────┐
│                      users                               │
├─────────────────────────────────────────────────────────┤
│ id              INTEGER PRIMARY KEY AUTOINCREMENT        │
│ username        TEXT UNIQUE NOT NULL                     │
│ email           TEXT UNIQUE NOT NULL                     │
│ password        TEXT NOT NULL (SHA-256 hashed)           │
│ full_name       TEXT NOT NULL                            │
│ bio             TEXT                                     │
│ created_at      TEXT NOT NULL                            │
└─────────────────────────────────────────────────────────┘
                        ↓ 1:N
┌─────────────────────────────────────────────────────────┐
│                      posts                               │
├─────────────────────────────────────────────────────────┤
│ id              INTEGER PRIMARY KEY AUTOINCREMENT        │
│ title           TEXT NOT NULL                            │
│ content         TEXT NOT NULL                            │
│ author_id       INTEGER NOT NULL (FK → users.id)         │
│ author_name     TEXT NOT NULL                            │
│ created_at      TEXT NOT NULL                            │
│ updated_at      TEXT NOT NULL                            │
│ views           INTEGER DEFAULT 0                        │
└─────────────────────────────────────────────────────────┘
                        ↓ 1:N
┌─────────────────────────────────────────────────────────┐
│                    comments                              │
├─────────────────────────────────────────────────────────┤
│ id              INTEGER PRIMARY KEY AUTOINCREMENT        │
│ post_id         INTEGER NOT NULL (FK → posts.id)         │
│ user_id         INTEGER NOT NULL (FK → users.id)         │
│ username        TEXT NOT NULL                            │
│ content         TEXT NOT NULL                            │
│ created_at      TEXT NOT NULL                            │
└─────────────────────────────────────────────────────────┘
```

## Security Architecture

```
┌─────────────────────────────────────────────────────────┐
│                  Security Layers                         │
├─────────────────────────────────────────────────────────┤
│                                                          │
│  1. Password Security                                    │
│     ┌──────────────────────────────────────────┐        │
│     │ Plain Password → SHA-256 → Base64        │        │
│     │ Stored in database as hash               │        │
│     └──────────────────────────────────────────┘        │
│                                                          │
│  2. Session Management                                   │
│     ┌──────────────────────────────────────────┐        │
│     │ Login → Create HttpSession               │        │
│     │ Store: userId, username, fullName        │        │
│     │ Timeout: 30 minutes                      │        │
│     │ Logout → Invalidate session              │        │
│     └──────────────────────────────────────────┘        │
│                                                          │
│  3. Authorization                                        │
│     ┌──────────────────────────────────────────┐        │
│     │ Check session before protected actions   │        │
│     │ Verify ownership before edit/delete      │        │
│     │ Return 401 if not authenticated          │        │
│     │ Return 403 if not authorized             │        │
│     └──────────────────────────────────────────┘        │
│                                                          │
│  4. SQL Injection Prevention                             │
│     ┌──────────────────────────────────────────┐        │
│     │ Use PreparedStatement everywhere         │        │
│     │ Parameterized queries                    │        │
│     │ No string concatenation in SQL           │        │
│     └──────────────────────────────────────────┘        │
│                                                          │
└─────────────────────────────────────────────────────────┘
```

## Data Flow Example: Creating a Post

```
Step 1: User clicks "Create Post"
   ↓
Step 2: JavaScript validates form
   ↓
Step 3: Fetch API sends POST request
   {
     title: "My First Post",
     content: "This is the content..."
   }
   ↓
Step 4: PostServlet receives request
   ↓
Step 5: Check session (is user logged in?)
   ↓
Step 6: Extract data from request
   - title
   - content
   - authorId (from session)
   - authorName (from session)
   ↓
Step 7: Create Post object
   Post post = new Post(title, content, authorId, authorName);
   ↓
Step 8: Call PostDAO.createPost(post)
   ↓
Step 9: PostDAO executes SQL
   INSERT INTO posts (title, content, author_id, ...) VALUES (?, ?, ?, ...)
   ↓
Step 10: Database returns generated ID
   ↓
Step 11: PostDAO sets ID on Post object
   ↓
Step 12: Return Post to servlet
   ↓
Step 13: Servlet converts to JSON (Gson)
   {
     "id": 1,
     "title": "My First Post",
     "content": "This is the content...",
     "authorId": 1,
     "authorName": "john_doe",
     "createdAt": "2024-12-03T10:30:00",
     "views": 0
   }
   ↓
Step 14: Send JSON response to browser
   ↓
Step 15: JavaScript receives response
   ↓
Step 16: Redirect to post detail page
   window.location.href = 'post-detail.html?id=1'
```

## Technology Stack Details

```
┌─────────────────────────────────────────────────────────┐
│                    Frontend Stack                        │
├─────────────────────────────────────────────────────────┤
│ HTML5          │ Semantic markup, forms, modals         │
│ CSS3           │ Flexbox, Grid, animations, responsive  │
│ JavaScript ES6 │ Async/await, Fetch API, modules        │
│ No frameworks  │ Vanilla JS for learning purposes       │
└─────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────┐
│                    Backend Stack                         │
├─────────────────────────────────────────────────────────┤
│ Java 11+       │ Core language                          │
│ Servlets 4.0   │ HTTP request handling                  │
│ JDBC           │ Database connectivity                  │
│ Gson 2.10      │ JSON serialization                     │
│ SQLite 3.42    │ Embedded database                      │
└─────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────┐
│                   Build & Deploy                         │
├─────────────────────────────────────────────────────────┤
│ Maven 3.6+     │ Build automation, dependencies         │
│ Tomcat 9.0+    │ Servlet container                      │
│ WAR packaging  │ Deployment format                      │
└─────────────────────────────────────────────────────────┘
```

## Key Design Patterns Used

1. **MVC (Model-View-Controller)**
   - Model: User, Post, Comment classes
   - View: HTML pages
   - Controller: Servlets

2. **DAO (Data Access Object)**
   - Separates business logic from data access
   - UserDAO, PostDAO, CommentDAO

3. **Singleton (Database Connection)**
   - DatabaseUtil manages connections
   - Single point of database configuration

4. **Front Controller**
   - Servlets handle specific routes
   - Centralized request processing

5. **Session Management**
   - HttpSession for user state
   - Secure authentication

This architecture provides a clean separation of concerns, making the application maintainable, scalable, and easy to understand!
