# Tomcat 11 Compatibility Fix

## What Changed

Tomcat 11 uses **Jakarta EE 10**, which changed package names:
- **Old (Tomcat 9)**: `javax.servlet.*`
- **New (Tomcat 10+)**: `jakarta.servlet.*`

## Fixes Applied

### 1. Updated pom.xml
Changed dependency from:
```xml
<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>javax.servlet-api</artifactId>
    <version>4.0.1</version>
</dependency>
```

To:
```xml
<dependency>
    <groupId>jakarta.servlet</groupId>
    <artifactId>jakarta.servlet-api</artifactId>
    <version>6.0.0</version>
</dependency>
```

### 2. Updated All Servlet Files
Changed imports in all 6 servlet files:
- LoginServlet.java
- RegisterServlet.java
- LogoutServlet.java
- PostServlet.java
- CommentServlet.java
- UserServlet.java

From:
```java
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
```

To:
```java
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
```

## How to Rebuild

### Option 1: Use the build.bat script
```bash
build.bat
```

### Option 2: Manual Maven build
If Maven is in your PATH:
```bash
mvn clean package
```

### Option 3: Eclipse
1. Right-click project → Maven → Update Project
2. Project → Clean
3. Project → Build Project
4. Right-click project → Run As → Maven build...
5. Goals: `clean package`
6. Run

## Deployment to Tomcat 11

1. **Stop Tomcat** (if running)

2. **Remove old deployment**:
   - Delete `webapps\bloghub.war`
   - Delete `webapps\bloghub\` folder

3. **Copy new WAR file**:
   ```bash
   copy target\bloghub.war "C:\Program Files\Apache Tomcat 11\webapps\"
   ```

4. **Start Tomcat**

5. **Wait 10-15 seconds** for deployment

6. **Access application**:
   ```
   http://localhost:8080/bloghub/
   ```

## Verification

### Check Tomcat Logs
Look at `logs\catalina.out` or `logs\catalina.YYYY-MM-DD.log`

Should see:
```
Deploying web application archive [bloghub.war]
Deployment of web application archive [bloghub.war] has finished
```

Should NOT see:
```
java.lang.ClassNotFoundException: javax.servlet.http.HttpServlet
```

### Test in Browser
1. Open http://localhost:8080/bloghub/
2. Open browser console (F12)
3. Click "Register"
4. Fill form and submit
5. Should see success message (not "Registration failed")

## Troubleshooting

### Still getting errors after rebuild?

1. **Clear Tomcat work directory**:
   ```bash
   rmdir /s /q "C:\Program Files\Apache Tomcat 11\work\Catalina"
   ```

2. **Clear Tomcat temp**:
   ```bash
   rmdir /s /q "C:\Program Files\Apache Tomcat 11\temp"
   ```

3. **Restart Tomcat**

### Maven build fails?

Check Java version:
```bash
java -version
```
Should be Java 11 or higher.

Check Maven version:
```bash
mvn -version
```
Should be Maven 3.6 or higher.

### Browser shows 404?

- Wait longer (deployment can take 15-30 seconds)
- Check `webapps\bloghub\` folder exists
- Check Tomcat logs for deployment errors

### Registration/Login still fails?

1. Open browser console (F12)
2. Go to Network tab
3. Try to register
4. Check the request to `/bloghub/register`
5. Look at:
   - Status code (should be 200)
   - Response (should be JSON)
   - Any error messages

If status is 404: Servlets not deployed correctly
If status is 500: Check Tomcat logs for Java errors
If status is 200 but response is error: Check database

## Version Compatibility

| Tomcat Version | Servlet API | Package Name |
|----------------|-------------|--------------|
| Tomcat 9.x     | 4.0         | javax.*      |
| Tomcat 10.0.x  | 5.0         | jakarta.*    |
| Tomcat 10.1.x  | 6.0         | jakarta.*    |
| **Tomcat 11.x**| **6.1**     | **jakarta.*** |

Your project now uses **jakarta.servlet-api 6.0.0** which is compatible with **Tomcat 10.1+ and Tomcat 11**.

## Quick Test

After deploying, test with curl:
```bash
curl -X POST http://localhost:8080/bloghub/register -d "username=testuser&email=test@test.com&password=test123&fullName=Test User"
```

Should return:
```json
{"success": true, "message": "Registration successful"}
```

## Summary

✅ Updated pom.xml to use Jakarta EE
✅ Updated all 6 servlet files
✅ Created build.bat for easy building
✅ Ready for Tomcat 11

**Next step**: Run `build.bat` to rebuild the project!
