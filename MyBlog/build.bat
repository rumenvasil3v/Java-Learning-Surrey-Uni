@echo off
echo ========================================
echo Building BlogHub for Tomcat 11
echo ========================================
echo.

REM Try to find Maven
set MAVEN_CMD=

if exist "C:\Program Files\apache-maven-3.9.9\bin\mvn.cmd" (
    set MAVEN_CMD=C:\Program Files\apache-maven-3.9.9\bin\mvn.cmd
)

if exist "C:\Program Files\Apache\Maven\bin\mvn.cmd" (
    set MAVEN_CMD=C:\Program Files\Apache\Maven\bin\mvn.cmd
)

if exist "C:\apache-maven\bin\mvn.cmd" (
    set MAVEN_CMD=C:\apache-maven\bin\mvn.cmd
)

REM Check if mvn is in PATH
where mvn >nul 2>&1
if %ERRORLEVEL% EQU 0 (
    set MAVEN_CMD=mvn
)

if "%MAVEN_CMD%"=="" (
    echo ERROR: Maven not found!
    echo.
    echo Please install Maven or add it to your PATH
    echo Download from: https://maven.apache.org/download.cgi
    echo.
    pause
    exit /b 1
)

echo Found Maven: %MAVEN_CMD%
echo.
echo Cleaning previous build...
call "%MAVEN_CMD%" clean

echo.
echo Building project...
call "%MAVEN_CMD%" package

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo BUILD SUCCESSFUL!
    echo ========================================
    echo.
    echo WAR file created: target\bloghub.war
    echo.
    echo To deploy:
    echo 1. Copy target\bloghub.war to your Tomcat 11 webapps folder
    echo 2. Start Tomcat
    echo 3. Access http://localhost:8080/bloghub/
    echo.
) else (
    echo.
    echo ========================================
    echo BUILD FAILED!
    echo ========================================
    echo.
    echo Check the error messages above
    echo.
)

pause
