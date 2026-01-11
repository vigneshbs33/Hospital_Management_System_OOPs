@echo off
echo ========================================
echo  MedCare Hospital Management System
echo ========================================
echo.

cd /d "%~dp0"

:: Check if Java is installed
where java >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Java is not installed or not in PATH!
    echo Please install Java JDK and add it to your PATH.
    echo Download from: https://www.oracle.com/java/technologies/downloads/
    pause
    exit /b 1
)

:: Create output directory if not exists
if not exist "out" mkdir out

echo Compiling Java files...
javac -d out -sourcepath src src\Main.java src\models\*.java src\managers\*.java src\utils\*.java src\gui\*.java src\gui\components\*.java 2>&1

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo Compilation failed!
    echo Make sure you have Java JDK installed (not just JRE).
    pause
    exit /b 1
)

echo Compilation successful!
echo.
echo ========================================
echo  Starting Hospital Management System...
echo ========================================
echo.

java -cp out Main

pause
