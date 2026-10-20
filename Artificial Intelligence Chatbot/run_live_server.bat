@echo off
setlocal
title NeuroBuddy AI - Live Server

echo ====================================================================
echo      NEUROBUDDY AI - LOCAL LIVE WEB SERVER (PORT 8080)
echo ====================================================================
echo.

if exist "%USERPROFILE%\.jdk\jdk-21.0.12.1+1\bin\java.exe" (
    set "JAVA_HOME=%USERPROFILE%\.jdk\jdk-21.0.12.1+1"
    set "PATH=%USERPROFILE%\.jdk\jdk-21.0.12.1+1\bin;%PATH%"
)

where javac >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] javac compiler was not found. Please ensure Java 17+ JDK is installed.
    pause
    exit /b 1
)

echo [1/2] Compiling project...
if not exist "bin" mkdir bin
javac -encoding UTF-8 -d bin -sourcepath src src\com\neurobuddy\ai\model\*.java src\com\neurobuddy\ai\nlp\*.java src\com\neurobuddy\ai\data\*.java src\com\neurobuddy\ai\engine\*.java src\com\neurobuddy\ai\gui\*.java src\com\neurobuddy\ai\*.java

if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Compilation failed!
    pause
    exit /b 1
)

echo [2/2] Starting Live Server on http://localhost:8080 ...
java -cp bin com.neurobuddy.ai.LiveServer
pause
