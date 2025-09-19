package com.smarthome.util;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AnonymousCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;
public class DynamoDBConfig {
    private static DynamoDbClient dynamoDbClient;
    private static DynamoDbEnhancedClient enhancedClient;
    private static Properties properties;
    static {
        loadProperties();
        initializeDynamoDBClient();
    }
    private static void loadProperties() {
        properties = new Properties();
        try (InputStream input = DynamoDBConfig.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (input == null) {
                System.out.println("Unable to find application.properties, using defaults");
                properties.setProperty("dynamodb.local", "true");
                properties.setProperty("dynamodb.local.endpoint", "http://localhost:8002");//localhost:8002");
                properties.setProperty("dynamodb.region", "us-east-1");
            } else {
                properties.load(input);
            }
        } catch (IOException e) {
            System.err.println("Error loading properties: " + e.getMessage());
            properties.setProperty("dynamodb.local", "true");
            properties.setProperty("dynamodb.local.endpoint", "http://localhost:8002");
            properties.setProperty("dynamodb.region", "us-east-1");
        }
    }
    private static void initializeDynamoDBClient() {
        try {
            boolean isLocal = Boolean.parseBoolean(properties.getProperty("dynamodb.local", "true"));
            String region = properties.getProperty("dynamodb.region", "us-east-1");
            if (isLocal) {
                String endpoint = properties.getProperty("dynamodb.local.endpoint", "http://localhost:8002");
                AwsBasicCredentials localCredentials = AwsBasicCredentials.create("local", "local");
                dynamoDbClient = DynamoDbClient.builder()
                        .endpointOverride(URI.create(endpoint))
                        .region(Region.of(region))
                        .credentialsProvider(StaticCredentialsProvider.create(localCredentials))
                        .build();
                try {
                    dynamoDbClient.listTables();
                    System.out.println(" Successfully connected to local DynamoDB at: " + endpoint);
                } catch (Exception testEx) {
                    System.err.println(" Failed to connect to DynamoDB at " + endpoint + ": " + testEx.getMessage());
                    throw testEx;
                }
            } else {
                dynamoDbClient = DynamoDbClient.builder()
                        .region(Region.of(region))
                        .credentialsProvider(DefaultCredentialsProvider.create())
                        .build();
                System.out.println("Connected to AWS DynamoDB in region: " + region);
            }
            enhancedClient = DynamoDbEnhancedClient.builder()
                    .dynamoDbClient(dynamoDbClient)
                    .build();
        } catch (Exception e) {
            System.err.println("Failed to initialize DynamoDB client: " + e.getMessage());
            System.err.println("\n[WARNING] DynamoDB is not available. Please:");
            System.err.println("   1. Start DynamoDB Local: java -Djava.library.path=./DynamoDBLocal_lib -jar DynamoDBLocal.jar -sharedDb -port 8002");
            System.err.println("   2. Or configure AWS DynamoDB credentials");
            System.err.println("   3. Application will continue in demo mode but data won't persist.\n");
            dynamoDbClient = null;
            enhancedClient = null;
        }
    }
    public static DynamoDbClient getDynamoDbClient() {
        return dynamoDbClient;
    }
    public static DynamoDbEnhancedClient getEnhancedClient() {
        return enhancedClient;
    }
    public static boolean isConnected() {
        return dynamoDbClient != null && enhancedClient != null;
    }
    public static void testConnection() {
        if (dynamoDbClient == null) {
            System.out.println(" DynamoDB client not initialized - running in DEMO mode");
            return;
        }
        try {
            var response = dynamoDbClient.listTables();
            System.out.println(" DynamoDB connection successful!");
            System.out.println("��� Existing tables: " + response.tableNames());
        } catch (Exception e) {
            System.err.println(" DynamoDB connection test failed: " + e.getMessage());
        }
    }
    public static void shutdown() {
        if (dynamoDbClient != null) {
            dynamoDbClient.close();
        }
    }
}
