# BlogHub - Modern Blog Platform

A full-featured blog platform built with Java Servlets, HTML, CSS, and JavaScript.

## Features

- **User Authentication**: Register and login with secure password hashing
- **Create & Manage Posts**: Write, edit, and delete your blog posts
- **User Profiles**: Personalized profile pages with bio and post history
- **Comments System**: Engage with posts through comments
- **Responsive Design**: Works seamlessly on desktop and mobile devices
- **View Counter**: Track post popularity with view counts
- **Modern UI**: Clean, intuitive interface with smooth animations

## Technologies Used

### Backend
- **Java 11+**: Core programming language
- **Java Servlets**: Web application framework
- **SQLite**: Lightweight database
- **Gson**: JSON serialization/deserialization
- **SHA-256**: Password hashing

### Frontend
- **HTML5**: Semantic markup
- **CSS3**: Modern styling with flexbox and grid
- **JavaScript (ES6+)**: Dynamic interactions
- **Fetch API**: Asynchronous HTTP requests

## Project Structure

```
src/main/
├── java/com/blog/
│   ├── dao/              # Data Access Objects
│   │   ├── UserDAO.java
│   │   ├── PostDAO.java
│   │   └── CommentDAO.java
│   ├── model/            # Data models
│   │   ├── User.java
│   │   ├── Post.java
│   │   └── Comment.java
│   ├── servlet/          # HTTP request handlers
│   │   ├── RegisterServlet.java
│   │   ├── LoginServlet.java
│   │   ├── LogoutServlet.java
│   │   ├── PostServlet.java
│   │   ├── CommentServlet.java
│   │   └── UserServlet.java
│   └── util/             # Utilities
│       └── DatabaseUtil.java
└── webapp/
    ├── css/
    │   └── style.css
    ├── js/
    │   ├── app.js
    │   ├── auth.js
    │   ├── post.js
    │   ├── post-detail.js
    │   └── profile.js
    ├── WEB-INF/
    │   └── web.xml
    ├── index.html
    ├── create-post.html
    ├── post-detail.html
    └── profile.html
```

## Setup Instructions

### Prerequisites
- Java Development Kit (JDK) 11 or higher
- Apache Maven 3.6+
- Apache Tomcat 9.0+ or any Java EE compatible server

### Installation Steps

1. **Clone or download the project**

2. **Build the project with Maven**
   ```bash
   mvn clean package
   ```

3. **Deploy to Tomcat**
   - Copy the generated `bloghub.war` from `target/` directory
   - Paste it into Tomcat's `webapps/` directory
   - Start Tomcat server

4. **Access the application**
   - Open browser and navigate to: `http://localhost:8080/bloghub/`

### Alternative: Run with Maven Tomcat Plugin

Add this plugin to pom.xml:
```xml
<plugin>
    <groupId>org.apache.tomcat.maven</groupId>
    <artifactId>tomcat7-maven-plugin</artifactId>
    <version>2.2</version>
    <configuration>
        <port>8080</port>
        <path>/bloghub</path>
    </configuration>
</plugin>
```

Then run:
```bash
mvn tomcat7:run
```

## How It Works

### Architecture Overview

The application follows a **3-tier architecture**:

1. **Presentation Layer** (Frontend)
   - HTML pages for user interface
   - CSS for styling
   - JavaScript for dynamic behavior

2. **Business Logic Layer** (Servlets)
   - Handle HTTP requests
   - Process business logic
   - Manage sessions

3. **Data Access Layer** (DAO)
   - Database operations
   - CRUD operations for entities

### Database Schema

**Users Table**
- id (PRIMARY KEY)
- username (UNIQUE)
- email (UNIQUE)
- password (hashed)
- full_name
- bio
- created_at

**Posts Table**
- id (PRIMARY KEY)
- title
- content
- author_id (FOREIGN KEY)
- author_name
- created_at
- updated_at
- views

**Comments Table**
- id (PRIMARY KEY)
- post_id (FOREIGN KEY)
- user_id (FOREIGN KEY)
- username
- content
- created_at

### Key Components Explained

#### 1. User Authentication Flow

**Registration:**
```
User fills form → RegisterServlet → Hash password → UserDAO → Save to DB
```

**Login:**
```
User enters credentials → LoginServlet → Verify password → Create session → Return success
```

#### 2. Post Management

**Create Post:**
```
User writes post → PostServlet (POST) → PostDAO → Save to DB → Return post ID
```

**View Post:**
```
Request post → PostServlet (GET) → PostDAO → Increment views → Return post + comments
```

**Edit/Delete:**
```
Check ownership → PostServlet (PUT/DELETE) → PostDAO → Update/Delete → Return result
```

#### 3. Comment System

**Add Comment:**
```
User submits comment → CommentServlet (POST) → CommentDAO → Save to DB
```

#### 4. Session Management

- Sessions created on login
- Session stores: userId, username, fullName
- Protected routes check session before allowing access
- Logout invalidates session

### Frontend-Backend Communication

The frontend uses the Fetch API to communicate with servlets:

```javascript
// Example: Creating a post
const response = await fetch('posts/', {
    method: 'POST',
    body: formData
});
const result = await response.json();
```

### Security Features

1. **Password Hashing**: SHA-256 algorithm
2. **Session-based Authentication**: Secure session management
3. **Authorization Checks**: Users can only edit/delete their own posts
4. **SQL Injection Prevention**: PreparedStatements used throughout
5. **XSS Protection**: Content sanitization on display

## Usage Guide

### For Users

1. **Register an Account**
   - Click "Register" in navigation
   - Fill in username, email, full name, and password
   - Submit to create account

2. **Login**
   - Click "Login"
   - Enter credentials
   - Access full features

3. **Create a Post**
   - Click "Create Post" (when logged in)
   - Write title and content
   - Click "Publish Post"

4. **View Posts**
   - Browse posts on home page
   - Click any post to read full content
   - View comments and statistics

5. **Comment on Posts**
   - Open any post
   - Scroll to comments section
   - Write and submit comment (requires login)

6. **Manage Your Profile**
   - Click "My Profile"
   - View your posts
   - Edit profile information

### For Developers

#### Adding New Features

**1. Add a new servlet:**
```java
@WebServlet("/your-endpoint")
public class YourServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, 
                        HttpServletResponse response) {
        // Your logic
    }
}
```

**2. Create a DAO method:**
```java
public YourEntity getEntity(int id) throws SQLException {
    String sql = "SELECT * FROM table WHERE id = ?";
    // Implementation
}
```

**3. Add frontend functionality:**
```javascript
async function yourFunction() {
    const data = await API.get('your-endpoint');
    // Process data
}
```

#### Database Initialization

The database is automatically created on first run by `DatabaseUtil.java`. Tables are created if they don't exist.

#### Customization

- **Styling**: Modify `css/style.css`
- **Database**: Change connection string in `DatabaseUtil.java`
- **Session timeout**: Adjust in `web.xml`

## API Endpoints

### Authentication
- `POST /register` - Register new user
- `POST /login` - Login user
- `GET /logout` - Logout user

### Posts
- `GET /posts/` - Get all posts
- `GET /posts/{id}` - Get single post with comments
- `GET /posts/user/{userId}` - Get posts by user
- `POST /posts/` - Create new post
- `PUT /posts/{id}` - Update post
- `DELETE /posts/{id}` - Delete post

### Comments
- `POST /comments/` - Add comment
- `DELETE /comments/{id}` - Delete comment

### User
- `GET /user/current` - Get current user info
- `PUT /user/update` - Update user profile

## Troubleshooting

**Issue: Database not found**
- Solution: The database is created automatically. Ensure write permissions in the application directory.

**Issue: 404 errors**
- Solution: Check that servlets are properly annotated with `@WebServlet`

**Issue: Session not persisting**
- Solution: Ensure cookies are enabled in browser

**Issue: Can't login after registration**
- Solution: Check that password hashing is consistent between registration and login

## Future Enhancements

- Image upload for posts
- Rich text editor
- Post categories and tags
- Search functionality
- Like/dislike system
- Email verification
- Password reset
- Social media sharing
- Admin dashboard
- Post drafts

## License

This project is open source and available for educational purposes.

## Contributing

Feel free to fork, modify, and submit pull requests!
