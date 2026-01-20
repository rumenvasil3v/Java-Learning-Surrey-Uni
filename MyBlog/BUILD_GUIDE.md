# Build & Deployment Guide - BlogHub

## Prerequisites Installation

### 1. Install Java Development Kit (JDK)

**Windows:**
1. Download JDK 11 or higher from: https://adoptium.net/
2. Run the installer
3. Set JAVA_HOME environment variable:
   - Right-click "This PC" → Properties → Advanced System Settings
   - Environment Variables → New System Variable
   - Variable name: `JAVA_HOME`
   - Variable value: `C:\Program Files\Eclipse Adoptium\jdk-11.x.x`
4. Add to PATH: `%JAVA_HOME%\bin`
5. Verify: Open CMD and run `java -version`

### 2. Install Apache Maven

**Windows:**
1. Download Maven from: https://maven.apache.org/download.cgi
2. Extract to `C:\Program Files\Apache\maven`
3. Set M2_HOME environment variable:
   - Variable name: `M2_HOME`
   - Variable value: `C:\Program Files\Apache\maven`
4. Add to PATH: `%M2_HOME%\bin`
5. Verify: Open CMD and run `mvn -version`

### 3. Install Apache Tomcat

**Windows:**
1. Download Tomcat 9 from: https://tomcat.apache.org/download-90.cgi
2. Choose "Windows Service Installer" or "zip" version
3. Extract/Install to `C:\Program Files\Apache Tomcat 9.0`
4. Note the installation directory

## Building the Project

### Step 1: Navigate to Project Directory

```bash
cd C:\Java-Learning-Surrey-Uni\MyBlog
```

### Step 2: Clean Previous Builds

```bash
mvn clean
```

This removes the `target/` directory and all compiled files.

### Step 3: Compile and Package

```bash
mvn package
```

This will:
- Download dependencies (first time only)
- Compile all Java files
- Run tests (if any)
- Create `bloghub.war` in the `target/` directory

**Expected Output:**
```
[INFO] Building war: C:\...\MyBlog\target\bloghub.war
[INFO] BUILD SUCCESS
```

### Step 4: Verify the WAR File

```bash
dir target\bloghub.war
```

You should see the WAR file with a size around 5-10 MB.

## Deployment Options

### Option 1: Deploy to Tomcat (Recommended)

#### Method A: Manual Deployment

1. **Copy WAR file:**
   ```bash
   copy target\bloghub.war "C:\Program Files\Apache Tomcat 9.0\webapps\"
   ```

2. **Start Tomcat:**
   - Navigate to Tomcat's bin directory:
     ```bash
     cd "C:\Program Files\Apache Tomcat 9.0\bin"
     ```
   - Run startup script:
     ```bash
     startup.bat
     ```

3. **Wait for deployment:**
   - Tomcat will automatically extract the WAR file
   - Check `webapps/bloghub/` directory is created
   - Takes about 10-30 seconds

4. **Access the application:**
   ```
   http://localhost:8080/bloghub/
   ```

#### Method B: Tomcat Manager (GUI)

1. **Enable Tomcat Manager:**
   - Edit `conf/tomcat-users.xml`
   - Add:
     ```xml
     <role rolename="manager-gui"/>
     <user username="admin" password="admin" roles="manager-gui"/>
     ```

2. **Access Manager:**
   - Go to: `http://localhost:8080/manager/html`
   - Login with credentials above

3. **Deploy WAR:**
   - Scroll to "WAR file to deploy"
   - Choose `target/bloghub.war`
   - Click "Deploy"

### Option 2: Run with Maven Plugin

1. **Add plugin to pom.xml** (already included)

2. **Run directly:**
   ```bash
   mvn tomcat7:run
   ```

3. **Access:**
   ```
   http://localhost:8080/bloghub/
   ```

4. **Stop:** Press `Ctrl+C` in terminal

### Option 3: Eclipse IDE Integration

1. **Import Project:**
   - File → Import → Existing Maven Projects
   - Select the MyBlog directory
   - Click Finish

2. **Configure Server:**
   - Window → Preferences → Server → Runtime Environments
   - Add → Apache Tomcat v9.0
   - Browse to Tomcat installation directory

3. **Run on Server:**
   - Right-click project → Run As → Run on Server
   - Select Tomcat 9.0
   - Click Finish

## Verification Steps

### 1. Check Tomcat Logs

**Location:** `C:\Program Files\Apache Tomcat 9.0\logs\catalina.out`

**Look for:**
```
INFO: Deployment of web application archive [...\bloghub.war] has finished
```

### 2. Test Database Creation

The SQLite database should be created automatically:
- **Location:** Tomcat's bin directory or application root
- **File:** `blog.db`

### 3. Test Application

1. **Home Page:**
   ```
   http://localhost:8080/bloghub/
   ```
   Should show the BlogHub homepage

2. **Register:**
   - Click "Register"
   - Create a test account
   - Should show success message

3. **Login:**
   - Login with test account
   - Should redirect to home with "Create Post" visible

4. **Create Post:**
   - Click "Create Post"
   - Write a test post
   - Should save and display

## Troubleshooting

### Build Issues

**Problem:** "mvn command not found"
```bash
# Solution: Add Maven to PATH or use full path
"C:\Program Files\Apache\maven\bin\mvn" package
```

**Problem:** "JAVA_HOME not set"
```bash
# Solution: Set temporarily
set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-11.x.x
mvn package
```

**Problem:** "Dependencies not downloading"
```bash
# Solution: Clear Maven cache and retry
rmdir /s /q %USERPROFILE%\.m2\repository
mvn clean package
```

### Deployment Issues

**Problem:** "Port 8080 already in use"
```bash
# Solution 1: Stop other services
netstat -ano | findstr :8080
taskkill /PID [PID_NUMBER] /F

# Solution 2: Change Tomcat port
# Edit conf/server.xml, change 8080 to 8081
```

**Problem:** "404 Not Found"
```bash
# Check:
1. WAR file deployed: Check webapps/bloghub/ exists
2. Tomcat started: Check logs
3. Correct URL: http://localhost:8080/bloghub/ (with trailing slash)
```

**Problem:** "500 Internal Server Error"
```bash
# Check Tomcat logs:
type "C:\Program Files\Apache Tomcat 9.0\logs\catalina.out"

# Common causes:
- Missing dependencies (rebuild with mvn clean package)
- Database permissions (run Tomcat as administrator)
- Servlet mapping errors (check web.xml)
```

**Problem:** "Database errors"
```bash
# Solution: Ensure write permissions
# Run Tomcat as administrator or change database location
```

### Runtime Issues

**Problem:** "Can't login after registration"
```bash
# Check:
1. Database file exists
2. No errors in Tomcat logs
3. Password hashing working (check RegisterServlet and LoginServlet)
```

**Problem:** "Session not persisting"
```bash
# Check:
1. Cookies enabled in browser
2. Session timeout in web.xml (default 30 minutes)
3. Clear browser cache and cookies
```

## Development Workflow

### Making Changes

1. **Modify code** in `src/main/java/` or `src/main/webapp/`

2. **Rebuild:**
   ```bash
   mvn clean package
   ```

3. **Redeploy:**
   - Stop Tomcat
   - Delete `webapps/bloghub/` directory
   - Copy new WAR file
   - Start Tomcat

### Hot Reload (Development)

For faster development, use Maven plugin:
```bash
mvn tomcat7:run
```
Changes to JSP/HTML/CSS are reflected immediately.
Java changes require restart (Ctrl+C, then run again).

## Production Deployment

### Security Checklist

- [ ] Change default passwords
- [ ] Use HTTPS (configure SSL in Tomcat)
- [ ] Set strong session timeout
- [ ] Enable Tomcat security manager
- [ ] Use production database (MySQL/PostgreSQL)
- [ ] Configure proper logging
- [ ] Set up backup strategy

### Performance Optimization

1. **Database:**
   - Switch from SQLite to MySQL/PostgreSQL
   - Add database indexes
   - Use connection pooling

2. **Tomcat:**
   - Increase memory: Edit `setenv.bat`
     ```
     set CATALINA_OPTS=-Xms512M -Xmx1024M
     ```
   - Enable compression in `server.xml`

3. **Application:**
   - Add caching
   - Minify CSS/JS
   - Optimize images

## Useful Commands

```bash
# Build without tests
mvn package -DskipTests

# Clean and build
mvn clean package

# Run with Maven
mvn tomcat7:run

# Check dependencies
mvn dependency:tree

# Update dependencies
mvn versions:display-dependency-updates

# Create Eclipse project files
mvn eclipse:eclipse
```

## Next Steps

1. ✅ Build successful
2. ✅ Deploy to Tomcat
3. ✅ Test all features
4. 📚 Read README.md for architecture details
5. 🎨 Customize styling
6. 🚀 Add new features

## Support

- Check `README.md` for detailed documentation
- Review code comments in Java files
- Check Tomcat logs for errors
- Verify all prerequisites are installed correctly

Happy coding! 🎉
