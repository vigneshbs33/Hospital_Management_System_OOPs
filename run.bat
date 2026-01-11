@echo off
echo ========================================
echo  MedCare Hospital Management System
echo ========================================
echo.

cd /d "%~dp0"

if not exist "out" mkdir out

echo Compiling Java files...
"C:\Program Files\Java\jdk-23\bin\javac" -d out -sourcepath src src\Main.java src\models\*.java src\managers\*.java src\utils\*.java src\gui\*.java src\gui\components\*.java

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo Compilation failed! Please check if Java JDK is installed.
    pause
    exit /b 1
)

echo Compilation successful!
echo.
echo ========================================
echo  Role-Based Access:
echo  - Administrator: Full access
echo  - Doctor: Dashboard, Patients, Appointments
echo  - Receptionist: Dashboard, Patients, Appointments, Billing
echo ========================================
echo.
echo Starting application...
echo.

"C:\Program Files\Java\jdk-23\bin\java" -cp out Main

pause
