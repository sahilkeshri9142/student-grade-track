# NeuroBuddy AI - PowerShell Launcher

Write-Host "====================================================================" -ForegroundColor Cyan
Write-Host "     NEUROBUDDY AI - SMART CONVERSATIONAL ASSISTANT                " -ForegroundColor Magenta
Write-Host "====================================================================" -ForegroundColor Cyan

$jdkPath = "$env:USERPROFILE\.jdk\jdk-21.0.12.1+1\bin"
if (Test-Path "$jdkPath\java.exe") {
    $env:JAVA_HOME = "$env:USERPROFILE\.jdk\jdk-21.0.12.1+1"
    $env:Path = "$jdkPath;$env:Path"
}

if (-not (Get-Command javac -ErrorAction SilentlyContinue)) {
    Write-Host "[ERROR] javac not found in PATH or at $jdkPath" -ForegroundColor Red
    pause
    exit 1
}

Write-Host "[1/2] Compiling Java source files..." -ForegroundColor Yellow
if (-not (Test-Path "bin")) {
    New-Item -ItemType Directory -Path "bin" | Out-Null
}

$sources = Get-ChildItem -Path "src" -Recurse -Filter "*.java" | Select-Object -ExpandProperty FullName
& javac -encoding UTF-8 -d bin -sourcepath src $sources

if ($LASTEXITCODE -ne 0) {
    Write-Host "[ERROR] Compilation failed!" -ForegroundColor Red
    pause
    exit 1
}

Write-Host "[2/2] Launching NeuroBuddy AI GUI..." -ForegroundColor Green
Start-Process javaw -ArgumentList "-cp", "bin", "com.neurobuddy.ai.Main"

Write-Host "[SUCCESS] NeuroBuddy AI is running!" -ForegroundColor Cyan
