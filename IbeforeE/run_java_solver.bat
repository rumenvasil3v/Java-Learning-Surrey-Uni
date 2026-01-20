@echo off
echo Compiling Java CTF Solver...
javac CTFSolver.java TestEvaluator.java

if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo.
echo Running tests first...
java TestEvaluator

echo.
echo Press any key to run the actual CTF solver...
pause

echo.
echo Running CTF Solver...
java CTFSolver

pause