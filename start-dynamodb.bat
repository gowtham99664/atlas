@echo off
echo ===========================================
echo     Starting DynamoDB Local Server
echo ===========================================
echo.
echo Starting DynamoDB Local on port 8002...
echo Press Ctrl+C to stop the server.
echo.

cd /d "C:\Users\gochendr\Downloads\atlas\dynamodb-local"

java -Djava.library.path=./DynamoDBLocal_lib -jar DynamoDBLocal.jar -sharedDb -port 8002

pause
