@echo off
echo ====================================================
echo    IoT Smart Home Dashboard - Quick Start
echo ====================================================
echo.
echo This script will build and run your application.
echo Make sure DynamoDB Local is running first!
echo.
pause
echo.
echo Step 1: Building the application...
cd iot-smart-home-dashboard
call mvn clean package
echo.
if %ERRORLEVEL% NEQ 0 (
    echo Build failed! Please check the errors above.
    pause
    exit /b 1
)
echo Build successful!
echo.
echo Step 2: Starting the IoT Smart Home Dashboard...
echo.
call mvn exec:java -Dexec.mainClass="com.smarthome.SmartHomeDashboard"
echo.
echo Application finished.
pause