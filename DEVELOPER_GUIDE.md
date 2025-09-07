# IoT Smart Home Dashboard - Developer Guide

## 📋 Table of Contents
1. [Project Overview](#project-overview)
2. [File Structure](#file-structure)
3. [Architecture Components](#architecture-components)
4. [Setup & Execution Guide](#setup--execution-guide)
5. [Development Guidelines](#development-guidelines)
6. [Database Configuration](#database-configuration)
7. [Common Issues & Solutions](#common-issues--solutions)

---

## 🏠 Project Overview

The **IoT Smart Home Dashboard** is a Java-based console application that provides centralized control and management of smart home devices. It features user authentication, device management, and secure password handling with DynamoDB integration.

### Key Features
- 🔐 **Secure Authentication**: BCrypt password hashing with progressive account lockout
- 📱 **Device Management**: Control 18 categories of smart devices (350+ models)
- 🏡 **Room Organization**: Support for 38+ room types covering Indian homes
- 🔄 **Simple Password Reset**: Streamlined password recovery without security questions
- 💾 **Dual Storage**: DynamoDB Local with automatic fallback to demo mode

---

## 📁 File Structure

```
iot-smart-home-dashboard/
├── pom.xml                           # Maven configuration & dependencies
├── src/main/java/com/smarthome/
│   ├── model/                        # Data Models
│   │   ├── Customer.java            # User entity with authentication fields
│   │   └── Gadget.java              # Smart device entity
│   ├── service/                      # Business Logic Layer
│   │   ├── CustomerService.java     # User management & authentication
│   │   ├── GadgetService.java       # Device operations & validation
│   │   └── SmartHomeService.java    # Main business orchestration
│   ├── util/                         # Utility Classes
│   │   ├── DynamoDBConfig.java      # Database connection management
│   │   └── SessionManager.java      # User session handling
│   └── SmartHomeDashboard.java      # Main application & console UI
└── target/                           # Build output directory
    └── iot-smart-home-dashboard-1.0.0.jar
```

---

## 🏗️ Architecture Components

### 1. **Main Application** (`SmartHomeDashboard.java`)
**Purpose**: Entry point and user interface controller

**What's Implemented**:
- Console-based menu system with 7 main options
- User input validation and sanitization
- Navigation between registration, login, and device management
- Error handling for all user interactions
- Graceful application shutdown with resource cleanup

**Key Methods**:
- `main()`: Application entry point
- `showMainMenu()`: Main navigation controller
- `registerCustomer()`: User registration flow
- `loginCustomer()`: Authentication flow
- `forgotPassword()`: Simplified password reset
- `controlGadgets()`: Device management interface

---

### 2. **Data Models**

#### **Customer.java**
**Purpose**: User entity with authentication and security features

**What's Implemented**:
- User profile data (email, name, password hash)
- Device ownership tracking (list of gadgets)
- Security features for account protection:
  - Failed login attempt counter
  - Account lockout timestamp
  - Last failed login time
- Helper methods for account security management

**Key Fields**:
```java
private String email;                    // Primary key & username
private String fullName;                 // User display name
private String password;                 // BCrypt hashed password
private List<Gadget> gadgets;           // User's connected devices
private int failedLoginAttempts;        // Security counter
private LocalDateTime accountLockedUntil; // Lockout expiry
```

#### **Gadget.java**
**Purpose**: Smart device representation

**What's Implemented**:
- Device identification (ID, type, model, room)
- Device status management (ON/OFF)
- Unique device generation with UUID
- String representation for console display

**Key Fields**:
```java
private String id;          // Unique device identifier
private String type;        // Device category (TV, AC, FAN, etc.)
private String model;       // Brand/model name
private String roomName;    // Location in house
private String status;      // Current state (ON/OFF)
```

---

### 3. **Service Layer**

#### **CustomerService.java**
**Purpose**: User management and authentication logic

**What's Implemented**:
- **User Registration**: Email validation, password strength checking
- **Authentication**: BCrypt password verification with security tracking
- **Account Security**: Progressive lockout system (5min → 15min → 60min)
- **Password Reset**: Simplified email-based password recovery
- **Validation Rules**: Email format, password complexity, name validation

**Key Features**:
- Password requirements: 8-128 chars, mixed case, numbers, special chars
- Common password blacklist (30+ weak passwords)
- Failed login protection with automatic account lockout
- BCrypt hashing with salt for password security

#### **GadgetService.java**
**Purpose**: Device operations and validation

**What's Implemented**:
- **Device Creation**: Generates unique devices with proper validation
- **Type Validation**: Supports 18 device categories
- **Model Validation**: 350+ Indian market device models
- **Room Validation**: 38+ room types for Indian homes
- **Business Rules**: One device type per room enforcement

**Supported Categories**:
- Entertainment: TV, Smart Speaker
- Climate: AC, Fan, Air Purifier, Thermostat
- Lighting: Smart Light, Smart Switch
- Security: Camera, Door Lock, Doorbell
- Kitchen: Refrigerator, Microwave, Washing Machine, Geyser, Water Purifier
- Cleaning: Robotic Vacuum

#### **SmartHomeService.java**
**Purpose**: Main business logic orchestration

**What's Implemented**:
- **User Operations**: Registration and login coordination
- **Device Management**: Connect, view, and control devices
- **Session Integration**: User state management
- **Password Recovery**: Simplified reset workflow
- **Data Validation**: Input sanitization and business rule enforcement

---

### 4. **Utility Classes**

#### **DynamoDBConfig.java**
**Purpose**: Database connection and configuration management

**What's Implemented**:
- DynamoDB Local connection (localhost:8000)
- Enhanced client configuration with proper error handling
- Automatic fallback to demo mode if database unavailable
- Connection lifecycle management with proper shutdown

#### **SessionManager.java**
**Purpose**: User session state management

**What's Implemented**:
- Singleton pattern for global session access
- Current user tracking during application runtime
- Login/logout state management
- Session data updates (when user data changes)
- Memory-based session storage (resets on app restart)

---

## 🚀 Setup & Execution Guide

### Prerequisites
- **Java 11+** (required for compilation and runtime)
- **Apache Maven 3.6+** (for dependency management and building)
- **DynamoDB Local** (included in project)

### Quick Start (Automated)
```bash
# From project root directory
QUICK_START.bat
```

### Manual Setup

#### Step 1: Start DynamoDB Local
```bash
# Option 1: Use provided script
start-dynamodb.bat

# Option 2: Manual startup
cd dynamodb-local
java -Djava.library.path=./DynamoDBLocal_lib -jar DynamoDBLocal.jar -sharedDb -port 8000
```

#### Step 2: Build Application
```bash
cd iot-smart-home-dashboard
mvn clean compile package
```

#### Step 3: Run Application
```bash
# Option 1: Using Maven (development)
mvn exec:java -Dexec.mainClass="com.smarthome.SmartHomeDashboard"

# Option 2: Using JAR file (production)
java -jar target/iot-smart-home-dashboard-1.0.0.jar
```

### Demo Mode
If DynamoDB is unavailable, the application automatically switches to demo mode:
- All functionality works normally
- Data is stored in memory only
- Data is lost when application closes
- Console shows: "🎮 Running in DEMO MODE"

---

## 💡 Development Guidelines

### Code Organization
- **Layered Architecture**: UI → Service → Model → Utility
- **Single Responsibility**: Each class has one main purpose
- **Dependency Injection**: Services are injected into higher layers
- **Error Handling**: Comprehensive try-catch with user-friendly messages

### Adding New Device Types
1. **Update GadgetService.java**: Add new type to `VALID_GADGET_TYPES`
2. **Add Models**: Include brand names in appropriate model validation
3. **Update UI**: Add control method in `SmartHomeDashboard.java`
4. **Test**: Verify device creation, connection, and status changes

### Adding New Rooms
1. **Update GadgetService.java**: Add room name to `VALID_ROOMS`
2. **Test**: Verify room validation works in device connection

### Security Features
- **Password Hashing**: Always use BCrypt, never store plain text
- **Input Validation**: Sanitize all user inputs
- **Account Lockout**: Configurable in `calculateLockoutMinutes()`
- **Session Management**: Automatic cleanup on application exit

---

## 🗄️ Database Configuration

### DynamoDB Local Setup
```java
// Connection configuration in DynamoDBConfig.java
private static final String ENDPOINT = "http://localhost:8000";
private static final Region REGION = Region.US_EAST_1;

// Table configuration
Table Name: "customers"
Partition Key: email (String)
```

### Table Schema
```json
{
  "email": "string (partition key)",
  "fullName": "string",
  "password": "string (BCrypt hash)",
  "gadgets": "list of gadget objects",
  "failedLoginAttempts": "number",
  "accountLockedUntil": "datetime",
  "lastFailedLoginTime": "datetime"
}
```

### Demo Mode Configuration
When DynamoDB is unavailable:
- Uses `HashMap<String, Customer>` for storage
- All validation and business logic remains active
- Data persists only during application runtime

---

## 🔧 Common Issues & Solutions

### Build Issues

#### Problem: Maven dependency resolution fails
**Solution**:
```bash
mvn clean
mvn dependency:resolve
mvn compile package
```

#### Problem: Java version compatibility
**Solution**:
```bash
# Check Java version
java -version

# Ensure Java 11+ is installed and JAVA_HOME is set correctly
echo $JAVA_HOME
```

### Runtime Issues

#### Problem: DynamoDB connection fails
**Symptoms**: Application shows connection errors
**Solution**:
1. Ensure DynamoDB Local is running: `start-dynamodb.bat`
2. Check port 8000 is available: `netstat -an | findstr 8000`
3. Application will automatically fall back to demo mode

#### Problem: Scanner input stream errors
**Symptoms**: "No line found" exceptions
**Solution**:
- Run application interactively (not with input redirection)
- Ensure console supports interactive input

#### Problem: Account lockout
**Symptoms**: "Account is locked" messages
**Solution**:
- Wait for lockout period to expire (5min/15min/60min)
- Use "Forgot Password" option to reset
- Lockout times are in `CustomerService.calculateLockoutMinutes()`

### Development Issues

#### Problem: Adding new features
**Best Practice**:
1. Follow existing code patterns
2. Add validation in service layer
3. Update UI in SmartHomeDashboard
4. Test both database and demo modes
5. Update user guide documentation

#### Problem: Debugging
**Useful Debug Points**:
- Check `DynamoDBConfig.getEnhancedClient()` for database issues
- Monitor `SessionManager.isLoggedIn()` for authentication flow
- Verify input validation in service classes

---

## 📚 Additional Resources

### Key Dependencies (pom.xml)
- **AWS SDK v2**: DynamoDB integration
- **BCrypt**: Password hashing
- **SLF4J**: Logging framework
- **Maven Shade**: Executable JAR creation

### Development Tools
- **IDE**: Any Java IDE (IntelliJ IDEA, Eclipse, VS Code)
- **Build Tool**: Apache Maven
- **Database**: DynamoDB Local
- **Version Control**: Git (recommended)

### Testing
- Application includes comprehensive validation
- Manual testing through console interface
- Demo mode for testing without database setup

---

## 💻 Code Examples

### Adding a New Device Type

**Step 1: Update GadgetService.java**
```java
// Add to VALID_GADGET_TYPES array
private static final String[] VALID_GADGET_TYPES = {
    "TV", "AC", "FAN", "LIGHT", "SWITCH", "CAMERA", "DOOR_LOCK", 
    "GEYSER", "DOORBELL", "VACUUM", "AIR_PURIFIER", "SPEAKER", 
    "WATER_PURIFIER", "THERMOSTAT", "WASHING_MACHINE", 
    "REFRIGERATOR", "MICROWAVE",
    "DISHWASHER" // New device type
};

// Add model validation
case "DISHWASHER":
    validModels = Arrays.asList(
        "Bosch", "IFB", "Siemens", "Whirlpool", "LG", "Kaff", "Faber"
    );
    break;
```

**Step 2: Update SmartHomeDashboard.java**
```java
// Add menu option in showGadgetControlMenu()
System.out.println("18. Control Dishwasher");
System.out.println("19. Exit"); // Update exit number

// Add case in switch statement
case 18: controlDishwasher(); break;

// Add control method
private static void controlDishwasher() {
    System.out.println("\n=== Control Dishwasher ===");
    
    try {
        String model = getValidatedGadgetInput("Dishwasher Model", 
                "Bosch, IFB, Siemens, Whirlpool, LG, Kaff, Faber, etc.");
        if (model == null) return;
        
        String roomName = getValidatedGadgetInput("Room Name", 
                "Kitchen, Dining Room, Utility Room, etc.");
        if (roomName == null) return;
        
        smartHomeService.connectToGadget("DISHWASHER", model, roomName);
    } catch (Exception e) {
        System.out.println("Failed to control Dishwasher. Please try again.");
    }
}
```

### Adding Custom Validation

**Email Domain Restriction Example**:
```java
// In CustomerService.java
public boolean isValidEmail(String email) {
    if (email == null || email.trim().isEmpty()) {
        return false;
    }
    email = email.trim().toLowerCase();
    
    // Basic format validation
    boolean formatValid = email.matches("^[a-z0-9+_.-]+@[a-z0-9.-]+\\.[a-z]{2,}$");
    
    // Custom domain validation (example)
    String[] allowedDomains = {"gmail.com", "yahoo.com", "outlook.com", "company.com"};
    boolean domainValid = Arrays.stream(allowedDomains)
        .anyMatch(domain -> email.endsWith("@" + domain));
    
    return formatValid && domainValid;
}
```

### Session Data Extension

**Adding User Preferences**:
```java
// In SessionManager.java
private Map<String, Object> userPreferences = new HashMap<>();

public void setUserPreference(String key, Object value) {
    if (isLoggedIn()) {
        userPreferences.put(currentUser.getEmail() + "_" + key, value);
    }
}

public Object getUserPreference(String key) {
    if (isLoggedIn()) {
        return userPreferences.get(currentUser.getEmail() + "_" + key);
    }
    return null;
}
```

---

## 🧪 Testing Guidelines

### Manual Testing Checklist

**Authentication Flow**:
- [ ] Register new user with valid data
- [ ] Register with invalid email format
- [ ] Register with weak password
- [ ] Register with existing email
- [ ] Login with correct credentials
- [ ] Login with wrong password (test lockout)
- [ ] Password reset flow

**Device Management Flow**:
- [ ] Connect new device
- [ ] Try duplicate device in same room
- [ ] Connect same device type in different room
- [ ] View all devices
- [ ] Change device status
- [ ] Test with invalid device types

**Security Testing**:
- [ ] Test account lockout (3, 5, 7 failed attempts)
- [ ] Test lockout expiry
- [ ] Test password strength validation
- [ ] Test session management

### Automated Testing Setup

For future automated testing, consider this structure:

```java
// Example test class structure
public class CustomerServiceTest {
    private CustomerService customerService;
    
    @BeforeEach
    void setUp() {
        customerService = new CustomerService();
    }
    
    @Test
    void testValidPasswordAccepted() {
        assertTrue(customerService.isValidPassword("StrongPass123!"));
    }
    
    @Test
    void testWeakPasswordRejected() {
        assertFalse(customerService.isValidPassword("weak"));
    }
    
    // Add more test methods...
}
```

---

## 🚀 Production Deployment

### Pre-Deployment Checklist

**Environment Setup**:
- [ ] Java 11+ installed on target system
- [ ] DynamoDB Local configured and tested
- [ ] Network ports available (8000 for DynamoDB)
- [ ] Sufficient disk space for logs and data
- [ ] Backup strategy for DynamoDB data

**Security Hardening**:
- [ ] Review password policy settings
- [ ] Adjust lockout timing for production use
- [ ] Configure proper logging levels
- [ ] Secure DynamoDB access
- [ ] Review error messages (remove debug info)

**Performance Optimization**:
- [ ] Test with expected user load
- [ ] Monitor memory usage
- [ ] Configure JVM parameters if needed
- [ ] Set up log rotation

### Production Configuration

**JVM Tuning** (for larger deployments):
```bash
java -Xms512m -Xmx1024m -XX:+UseG1GC \
     -jar iot-smart-home-dashboard-1.0.0.jar
```

**DynamoDB Production Setup**:
```bash
# For production, consider AWS DynamoDB instead of Local
# Update DynamoDBConfig.java with production endpoints
private static final String ENDPOINT = "https://dynamodb.region.amazonaws.com";
```

### Monitoring & Maintenance

**Log Files to Monitor**:
- Application logs (console output)
- DynamoDB Local logs
- System resource usage (CPU, memory)
- Failed login attempts (security monitoring)

**Regular Maintenance Tasks**:
- Monitor disk space (DynamoDB data files)
- Review security logs for suspicious activity
- Update dependencies periodically
- Backup user data regularly

---

## ⚡ Performance Optimization

### Memory Management

**Current Memory Usage**:
- Base application: ~50MB
- Per user session: ~1-2MB
- Per device: ~0.1MB
- DynamoDB Local: ~100-200MB

**Optimization Tips**:
```java
// In SmartHomeService.java - Connection pooling example
private static final int MAX_CONNECTIONS = 10;
private static final ConnectionPool connectionPool = 
    new ConnectionPool(MAX_CONNECTIONS);

// Lazy loading for large device lists
public List<Gadget> getDevicesLazily() {
    return devices.stream()
        .limit(100) // Paginate for large lists
        .collect(Collectors.toList());
}
```

### Database Optimization

**Query Optimization**:
```java
// Use batch operations for multiple devices
public void updateMultipleDevices(List<Gadget> gadgets) {
    // Batch update implementation
    customerTable.batchWriteItem(
        gadgets.stream()
            .map(this::createWriteRequest)
            .collect(Collectors.toList())
    );
}
```

**Connection Management**:
```java
// Proper connection lifecycle
public void shutdown() {
    try {
        if (dynamoDbClient != null) {
            dynamoDbClient.close();
        }
    } catch (Exception e) {
        System.err.println("Error closing DynamoDB connection: " + e.getMessage());
    }
}
```

---

## 🔄 Version Control & Collaboration

### Git Workflow

**Branch Strategy**:
```
master/main     - Production ready code
develop         - Integration branch
feature/*       - New features
hotfix/*        - Critical fixes
```

**Commit Message Format**:
```
type(scope): description

feat(auth): add simplified password reset
fix(device): resolve duplicate device validation
docs(guide): update developer documentation
```

### Code Review Checklist

**Security Review**:
- [ ] No hardcoded passwords or keys
- [ ] Input validation implemented
- [ ] Error messages don't expose sensitive info
- [ ] Session management properly handled

**Code Quality**:
- [ ] Follows existing patterns
- [ ] Proper error handling
- [ ] Adequate comments for complex logic
- [ ] No code duplication

---

**📝 Note**: This guide covers the current implementation. For future enhancements, follow the established patterns and update this documentation accordingly.

**📞 Support**: Refer to troubleshooting sections in the main User Guide for additional help.

**🔧 Contributing**: Before making changes, review the code examples and testing guidelines above.

---

*Developer Guide v1.1 - Last Updated: September 2025*