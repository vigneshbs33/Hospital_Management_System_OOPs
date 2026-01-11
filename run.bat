@echo off
echo ========================================
echo  MedCare Hospital Management System
echo ========================================
echo.

cd /d "%~dp0"

:: Set Java path
set JAVA_HOME=C:\Program Files\Java\jdk-23
set JAVA_CMD=%JAVA_HOME%\bin\java
set JAVAC_CMD=%JAVA_HOME%\bin\javac

:: Check if Java exists
if not exist "%JAVAC_CMD%.exe" (
    set JAVA_HOME=C:\Program Files\Java\jdk-21
    set JAVA_CMD=%JAVA_HOME%\bin\java
    set JAVAC_CMD=%JAVA_HOME%\bin\javac
)

if not exist "%JAVAC_CMD%.exe" (
    set JAVA_HOME=C:\Program Files\Java\jdk-17
    set JAVA_CMD=%JAVA_HOME%\bin\java
    set JAVAC_CMD=%JAVA_HOME%\bin\javac
)

if not exist "%JAVAC_CMD%.exe" (
    echo ERROR: Java JDK not found!
    pause
    exit /b 1
)

:: Create output directory
if not exist "out" mkdir out

echo Compiling...
"%JAVAC_CMD%" -d out -sourcepath src src/Main.java src/models/*.java src/managers/*.java src/utils/*.java src/gui/*.java src/gui/components/*.java

if %ERRORLEVEL% NEQ 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo Starting application...
"%JAVA_CMD%" -cp out Main
pause
