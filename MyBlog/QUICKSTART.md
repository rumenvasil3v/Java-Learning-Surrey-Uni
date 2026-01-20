# Quick Start Guide - BlogHub

## 🚀 Get Started in 5 Minutes

### Step 1: Build the Project

Open terminal in project directory and run:

```bash
mvn clean package
```

This will:
- Download all dependencies (Servlet API, SQLite, Gson)
- Compile Java code
- Create a WAR file in the `target/` directory

### Step 2: Deploy to Tomcat

**Option A: Manual Deployment**
1. Locate the file: `target/bloghub.war`
2. Copy it to your Tomcat's `webapps/` folder
   - Windows: `C:\Program Files\Apache Tomcat\webapps\`
   - Mac/Linux: `/opt/tomcat/webapps/` or `~/tomcat/webapps/`
3. Start Tomcat:
   - Windows: Run `startup.bat` in Tomcat's `bin/` folder
   - Mac/Linux: Run `./startup.sh` in Tomcat's `bin/` folder

**Option B: Using Maven Plugin**
```bash
mvn tomcat7:run
```

### Step 3: Access the Application

Open your browser and go to:
```
http://localhost:8080/bloghub/
```

### Step 4: Create Your First Account

1. Click **"Register"** button
2. Fill in:
   - Username: `john_doe`
   - Email: `john@example.com`
   - Full Name: `John Doe`
   - Password: `password123`
3. Click **"Register"**

### Step 5: Login and Create a Post

1. Click **"Login"**
2. Enter your username and password
3. Click **"Create Post"** in navigation
4. Write your first blog post!

## 🎯 What You Can Do

✅ **Register & Login** - Secure authentication system  
✅ **Create Posts** - Write and publish blog articles  
✅ **View Posts** - Browse all posts with view counts  
✅ **Comment** - Engage with posts through comments  
✅ **Edit Profile** - Customize your bio and information  
✅ **Manage Posts** - Edit or delete your own posts  
✅ **User Profiles** - View your blog and post history  

## 📁 Project Files Overview

```
BlogHub/
├── src/main/java/com/blog/     # Java backend code
│   ├── dao/                     # Database operations
│   ├── model/                   # Data models (User, Post, Comment)
│   ├── servlet/                 # Request handlers
│   └── util/                    # Database utilities
├── src/main/webapp/             # Frontend files
│   ├── css/                     # Stylesheets
│   ├── js/                      # JavaScript files
│   ├── WEB-INF/                 # Configuration
│   └── *.html                   # Web pages
├── pom.xml                      # Maven configuration
└── README.md                    # Full documentation
```

## 🔧 Common Issues & Solutions

### "mvn command not found"
**Solution:** Install Apache Maven from https://maven.apache.org/download.cgi

### "Port 8080 already in use"
**Solution:** Change port in Tomcat's `server.xml` or stop other services using port 8080

### "Cannot find Tomcat"
**Solution:** Download Apache Tomcat 9 from https://tomcat.apache.org/download-90.cgi

### Database errors
**Solution:** The SQLite database is created automatically. Ensure the application has write permissions.

## 💡 Tips

- **Default Database**: SQLite database file `blog.db` is created in your Tomcat directory
- **Session Timeout**: 30 minutes (configurable in web.xml)
- **Password Security**: Passwords are hashed with SHA-256
- **Responsive Design**: Works on mobile, tablet, and desktop

## 🎨 Customization

Want to customize the look?
- Edit `src/main/webapp/css/style.css` for styling
- Modify colors, fonts, and layouts
- Rebuild with `mvn clean package`

## 📚 Learn More

Check out `README.md` for:
- Detailed architecture explanation
- API endpoints documentation
- Security features
- How to add new features
- Database schema details

## 🆘 Need Help?

1. Check the full README.md
2. Review the code comments
3. Check Tomcat logs in `logs/catalina.out`
4. Verify all dependencies are downloaded

## 🎉 You're Ready!

Start blogging and enjoy your new platform!
