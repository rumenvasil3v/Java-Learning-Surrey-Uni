# Quick Fix for Tomcat 11 - BlogHub

## The Problem
You're using **Tomcat 11** which requires **Jakarta EE** (not Java EE).
The old code used `javax.servlet.*` which doesn't exist in Tomcat 11.

## The Solution (Already Applied!)
✅ Updated `pom.xml` to use `jakarta.servlet-api`
✅ Updated all 6 servlet files to use `jakarta.servlet.*` imports
✅ Created build scripts for easy rebuilding

## What You Need to Do Now

### Step 1: Rebuild the Project

**Option A - Use the build script (Easiest)**:
```bash
build.bat
```

**Option B - Use Maven directly**:
```bash
mvn clean package
```

**Option C - Use Eclipse**:
1. Right-click project → Maven → Update Project
2. Check "Force Update of Snapshots/Releases"
3. Click OK
4. Wait for dependencies to download
5. Right-click project → Run As → Maven build
6. Goals: `clean package`
7. Run

### Step 2: Deploy to Tomcat 11

**Option A - Use the deploy script (Easiest)**:
```bash
deploy.bat
```

**Option B - Manual deployment**:
1. Stop Tomcat
2. Delete old files:
   - `C:\Program Files\Apache Tomcat 11\webapps\bloghub.war`
   - `C:\Program Files\Apache Tomcat 11\webapps\bloghub\` folder
3. Copy new WAR:
   ```bash
   copy target\bloghub.war "C:\Program Files\Apache Tomcat 11\webapps\"
   ```
4. Start Tomcat
5. Wait 15 seconds

### Step 3: Test

1. Open: http://localhost:8080/bloghub/
2. Click "Register"
3. Fill in the form:
   - Username: testuser
   - Email: test@example.com
   - Password: password123
   - Full Name: Test User
4. Click Register
5. Should see: "Registration successful! Please login."

## Files Changed

1. **pom.xml** - Updated servlet dependency
2. **LoginServlet.java** - Changed imports
3. **RegisterServlet.java** - Changed imports
4. **LogoutServlet.java** - Changed imports
5. **PostServlet.java** - Changed imports
6. **CommentServlet.java** - Changed imports
7. **UserServlet.java** - Changed imports

## New Helper Files Created

1. **build.bat** - Automated build script
2. **deploy.bat** - Automated deployment script
3. **TOMCAT11_FIX.md** - Detailed explanation
4. **TOMCAT11_QUICK_FIX.md** - This file

## Troubleshooting

### "Maven not found"
- Install Maven from: https://maven.apache.org/download.cgi
- Or use Eclipse's embedded Maven (Option C above)

### Build succeeds but registration still fails
1. Make sure you deployed the NEW WAR file (check timestamp)
2. Clear Tomcat cache:
   ```bash
   rmdir /s /q "C:\Program Files\Apache Tomcat 11\work"
   ```
3. Restart Tomcat

### Check Tomcat logs
Look at: `C:\Program Files\Apache Tomcat 11\logs\catalina.YYYY-MM-DD.log`

Should see:
```
Deploying web application archive [bloghub.war]
Deployment of web application archive [bloghub.war] has finished
```

Should NOT see:
```
ClassNotFoundException: javax.servlet
```

## Why This Happened

| Component | Old (Tomcat 9) | New (Tomcat 11) |
|-----------|----------------|-----------------|
| API | Java EE | Jakarta EE |
| Package | javax.servlet.* | jakarta.servlet.* |
| Version | Servlet 4.0 | Servlet 6.1 |

Tomcat 11 dropped support for the old `javax.*` packages entirely.

## Quick Commands

```bash
# Build
build.bat

# Deploy
deploy.bat

# Or do both manually
mvn clean package
copy target\bloghub.war "C:\Program Files\Apache Tomcat 11\webapps\"

# Start Tomcat
cd "C:\Program Files\Apache Tomcat 11\bin"
startup.bat

# View logs
type "C:\Program Files\Apache Tomcat 11\logs\catalina.YYYY-MM-DD.log"
```

## Success Checklist

- [ ] Ran build.bat (or mvn clean package)
- [ ] Saw "BUILD SUCCESS" message
- [ ] Stopped Tomcat
- [ ] Deleted old bloghub.war and bloghub folder
- [ ] Copied new target\bloghub.war to Tomcat webapps
- [ ] Started Tomcat
- [ ] Waited 15 seconds
- [ ] Opened http://localhost:8080/bloghub/
- [ ] Tested registration - SUCCESS!

---

**TL;DR**: Run `build.bat` then `deploy.bat` then restart Tomcat!
