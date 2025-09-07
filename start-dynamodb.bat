@echo off
echo Starting DynamoDB Local...
echo.
cd dynamodb-local
java -Djava.library.path=./DynamoDBLocal_lib -jar DynamoDBLocal.jar -sharedDb -port 8000
echo.
echo DynamoDB Local has stopped.
pause