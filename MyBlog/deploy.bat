@echo off
echo ========================================
echo Deploy BlogHub to Tomcat 11
echo ========================================
echo.

REM Check if WAR file exists
if not exist "target\bloghub.war" (
    echo ERROR: WAR file not found!
    echo Please run build.bat first
    echo.
    pause
    exit /b 1
)

REM Try to find Tomcat
set TOMCAT_HOME=

if exist "C:\Program Files\Apache Tomcat 11" (
    set TOMCAT_HOME=C:\Program Files\Apache Tomcat 11
)

if exist "C:\Program Files\Apache Software Foundation\Tomcat 11.0" (
    set TOMCAT_HOME=C:\Program Files\Apache Software Foundation\Tomcat 11.0
)

if exist "C:\apache-tomcat-11" (
    set TOMCAT_HOME=C:\apache-tomcat-11
)

if exist "C:\Tomcat11" (
    set TOMCAT_HOME=C:\Tomcat11
)

if "%TOMCAT_HOME%"=="" (
    echo Tomcat 11 not found in common locations.
    echo.
    set /p TOMCAT_HOME="Enter your Tomcat 11 installation path: "
)

if not exist "%TOMCAT_HOME%\webapps" (
    echo ERROR: Invalid Tomcat path!
    echo webapps folder not found in: %TOMCAT_HOME%
    echo.
    pause
    exit /b 1
)

echo Found Tomcat: %TOMCAT_HOME%
echo.

REM Stop Tomcat if running
echo Checking if Tomcat is running...
tasklist /FI "IMAGENAME eq java.exe" 2>NUL | find /I "java.exe" >NUL
if %ERRORLEVEL% EQU 0 (
    echo Tomcat appears to be running.
    echo Please stop Tomcat before deploying.
    echo.
    set /p CONTINUE="Continue anyway? (y/n): "
    if /i not "%CONTINUE%"=="y" (
        echo Deployment cancelled.
        pause
        exit /b 0
    )
)

REM Remove old deployment
echo.
echo Removing old deployment...
if exist "%TOMCAT_HOME%\webapps\bloghub.war" (
    del /q "%TOMCAT_HOME%\webapps\bloghub.war"
    echo Deleted old WAR file
)

if exist "%TOMCAT_HOME%\webapps\bloghub" (
    rmdir /s /q "%TOMCAT_HOME%\webapps\bloghub"
    echo Deleted old bloghub folder
)

REM Copy new WAR file
echo.
echo Copying new WAR file...
copy /y "target\bloghub.war" "%TOMCAT_HOME%\webapps\"

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo DEPLOYMENT SUCCESSFUL!
    echo ========================================
    echo.
    echo WAR file copied to: %TOMCAT_HOME%\webapps\
    echo.
    echo Next steps:
    echo 1. Start Tomcat
    echo 2. Wait 10-15 seconds for deployment
    echo 3. Access http://localhost:8080/bloghub/
    echo.
    echo To start Tomcat:
    echo   cd "%TOMCAT_HOME%\bin"
    echo   startup.bat
    echo.
) else (
    echo.
    echo ========================================
    echo DEPLOYMENT FAILED!
    echo ========================================
    echo.
    echo Could not copy WAR file
    echo Check permissions and try running as Administrator
    echo.
)

pause
