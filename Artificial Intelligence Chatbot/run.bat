@echo off
setlocal enabledelayedexpansion
title NeuroBuddy AI - Smart Conversational Assistant

echo ====================================================================
echo      NEUROBUDDY AI - SMART CONVERSATIONAL ASSISTANT
echo ====================================================================
echo.

REM Check if custom JDK installed in user profile exists
if exist "%USERPROFILE%\.jdk\jdk-21.0.12.1+1\bin\java.exe" (
    set "JAVA_HOME=%USERPROFILE%\.jdk\jdk-21.0.12.1+1"
    set "PATH=%USERPROFILE%\.jdk\jdk-21.0.12.1+1\bin;%PATH%"
)

REM Verify Java compiler
where javac >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] javac compiler was not found in PATH.
    echo Please ensure Java 17+ JDK is installed.
    pause
    exit /b 1
)

echo [1/2] Compiling NeuroBuddy AI source code...
if not exist "bin" mkdir bin
javac -encoding UTF-8 -d bin -sourcepath src src\com\neurobuddy\ai\model\*.java src\com\neurobuddy\ai\nlp\*.java src\com\neurobuddy\ai\data\*.java src\com\neurobuddy\ai\engine\*.java src\com\neurobuddy\ai\gui\*.java src\com\neurobuddy\ai\*.java

if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Compilation failed!
    pause
    exit /b 1
)

echo [2/2] Launching GUI Application...
start javaw -cp bin com.neurobuddy.ai.Main
echo [SUCCESS] NeuroBuddy AI GUI window launched successfully!
echo You can now interact with the chatbot in the graphical interface.
timeout /t 3 >nul
exit /b 0
