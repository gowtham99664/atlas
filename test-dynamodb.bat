@echo off
echo ===========================================
echo      DynamoDB Connection Test Script
echo ===========================================
echo.
echo This script will help you test DynamoDB connectivity.
echo.
echo Prerequisites:
echo 1. DynamoDB Local should be running on port 8002
echo 2. Start DynamoDB with: java -Djava.library.path=./DynamoDBLocal_lib -jar DynamoDBLocal.jar -sharedDb -port 8002
echo.

cd /d "C:\Users\gochendr\Downloads\atlas\iot-smart-home-dashboard"

echo Starting IoT Smart Home Dashboard...
echo.
mvn exec:java -Dexec.mainClass="com.smarthome.SmartHomeDashboard"

pause