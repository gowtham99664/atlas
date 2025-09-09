@echo off
echo ====================================================
echo    IoT Smart Home Dashboard - Quick Start
echo ====================================================
echo.
echo This script will build and run your application.
echo Make sure DynamoDB Local is running on port 8002 first!
echo (Use: start-dynamodb.bat or java -Djava.library.path=./DynamoDBLocal_lib -jar DynamoDBLocal.jar -sharedDb -port 8002)
echo.
pause
echo.

echo Step 1: Building the application...
cd iot-smart-home-dashboard

echo Attempting to build without clean first...
call mvn compile
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo Compilation failed. Trying with package...
    call mvn package -Dmaven.test.skip=true
    if %ERRORLEVEL% NEQ 0 (
        echo.
        echo Build failed! Trying to force clean and rebuild...
        echo Stopping any Java processes...
        taskkill /F /IM java.exe 2>nul
        timeout /t 2 >nul
        call mvn clean package -Dmaven.test.skip=true
        if %ERRORLEVEL% NEQ 0 (
            echo.
            echo ERROR: Build failed! Please check the errors above.
            echo Try closing any running Java applications and run this script again.
            pause
            exit /b 1
        )
    )
)

echo.
echo Build successful!
echo.
echo Step 2: Starting the IoT Smart Home Dashboard...
echo.

REM Check if JAR exists and run directly, otherwise use maven exec
if exist "target\iot-smart-home-dashboard-1.0.0.jar" (
    echo Running from JAR file...
    java -jar target\iot-smart-home-dashboard-1.0.0.jar
) else (
    echo Running via Maven...
    call mvn exec:java -Dexec.mainClass="com.smarthome.SmartHomeDashboard"
)

echo.
echo Application finished.
pause