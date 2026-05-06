@echo off
title Chess Game Launcher
color 0A

echo ========================================
echo    Chess Game - Java Swing
echo    Checking prerequisites...
echo ========================================
echo.

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    color 0C
    echo [ERROR] Java is not installed or not in PATH!
    echo.
    echo Please download and install Java JDK 8 or later from:
    echo https://www.oracle.com/java/technologies/downloads/
    echo.
    echo After installation, restart this script.
    echo.
    pause
    exit /b 1
)

echo [OK] Java is installed
echo.

REM Check if src directory exists
if not exist "src" (
    color 0C
    echo [ERROR] 'src' directory not found!
    echo Please make sure you are running this script from the project root.
    echo.
    pause
    exit /b 1
)

echo [OK] Source directory found
echo.

REM Create output directory if it doesn't exist
if not exist "out" (
    echo Creating output directory...
    mkdir out
)

echo ========================================
echo    Compiling project...
echo ========================================
echo.

REM Compile all Java files
javac -d out src/model/*.java src/game/*.java src/ui/*.java 2>compile_errors.txt

if %errorlevel% neq 0 (
    color 0C
    echo [ERROR] Compilation failed!
    echo Check compile_errors.txt for details.
    echo.
    type compile_errors.txt
    echo.
    pause
    exit /b 1
)

echo [OK] Compilation successful
echo.

REM Clean up error log if compilation was successful
if exist "compile_errors.txt" del compile_errors.txt

echo ========================================
echo    Starting Chess Game...
echo ========================================
echo.

REM Run the game
java -cp out ui.Main

if %errorlevel% neq 0 (
    color 0C
    echo.
    echo [ERROR] Failed to run the game!
    echo.
    pause
    exit /b 1
)

echo.
echo Game closed.
pause
