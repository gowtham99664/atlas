@echo off
echo ===============================================
echo    Starting DynamoDB Local Database
echo ===============================================
echo.

REM Check if we're in the right directory
if not exist "DynamoDBLocal.jar" (
    echo ❌ Error: DynamoDBLocal.jar not found!
    echo Make sure you're running this from the dynamodb-local directory.
    echo Current directory: %CD%
    echo.
    pause
    exit /b 1
)

echo 🗄️  Starting DynamoDB Local on port 8000...
echo.
echo ℹ️  Keep this window open while using the application.
echo ℹ️  Press Ctrl+C to stop the database.
echo.

REM Start DynamoDB Local
java -Djava.library.path=./DynamoDBLocal_lib -jar DynamoDBLocal.jar -sharedDb -port 8000

echo.
echo 🛑 DynamoDB Local stopped.
pause